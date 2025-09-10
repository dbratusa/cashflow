package si.dbratusa.cashflow.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

/**
 * Minimal, deterministic CSV parser that uses BankStatementCsvParsePlan.
 *
 * Notes:
 * - Intentionally simple: splits by the configured single-character delimiter only
 *   (no RFC4180 quotes/escapes). If you need full CSV semantics, swap the splitter
 *   with OpenCSV/uniVocity but keep the same extraction rules.
 * - 0-based column indexes as defined in the plan.
 */
public class PlanParser {

    /** Lightweight DTO for parsed rows. Extend/replace with your own DTO. */
    public record TransactionRow(
            LocalDate bookingDate,
            BigDecimal amount,
            String currency,
            String description,
            String reference,
            String[] raw
    ) {}

    /**
     * Parse CSV text into transaction rows according to the given plan.
     * @param csv  raw CSV text (already decoded as UTF-8 or per your charset handling)
     * @param plan plan with 0-based indexes and formatting info
     */
    public static List<TransactionRow> parse(String csv, IBankStatementCsvParsePlan plan) {
        Objects.requireNonNull(csv, "csv");
        Objects.requireNonNull(plan, "plan");

        final char delimiter = Objects.requireNonNullElse(plan.delimiter(), ",").charAt(0);
        final int headerRowIndex = plan.headerRowIndex() == null ? 0 : plan.headerRowIndex();
        final int startRow = Math.max(0, headerRowIndex + 1);

        final DateTimeFormatter dateFmt = DateTimeFormatter.ofPattern(
                Objects.requireNonNullElse(plan.dateFormatPattern(), "yyyy-MM-dd"),
                Locale.ROOT
        );

        final String decSep = Objects.requireNonNullElse(plan.decimalSeparator(), ".");
        final String thouSep = Objects.requireNonNullElse(plan.thousandSeparator(), "");

        final Integer amountIdx = plan.amountColumnIndex();
        final Integer creditIdx = plan.creditColumnIndex();
        final Integer debitIdx  = plan.debitColumnIndex();

        final Integer currencyIdx = plan.currencyColumnIndex();
        final Integer descIdx     = plan.transactionDescriptionColumnIndex();
        final Integer bookingIdx  = plan.bookingDateColumnIndex();
        final Integer refIdx      = plan.referenceNumberColumnIndex();

        var out = new ArrayList<TransactionRow>();

        // Split lines on any of \r?\n
        String[] lines = csv.split("\r?\n");
        for (int i = startRow; i < lines.length; i++) {
            String line = lines[i];
            if (line == null) continue;
            line = line.trim();
            if (line.isEmpty()) continue;

            String[] cols = splitSimple(line, delimiter);

            // Skip rows that are obviously header-like (optional)
            if (looksLikeHeaderRow(cols, dateFmt, bookingIdx)) continue;

            LocalDate bookingDate = safeParseDate(get(cols, bookingIdx), dateFmt);
            BigDecimal amount = extractAmount(cols, amountIdx, creditIdx, debitIdx, decSep, thouSep);
            String currency = get(cols, currencyIdx);
            String description = get(cols, descIdx);
            String reference = get(cols, refIdx);

            if (bookingDate == null && amount == null) {
                // nothing meaningful
                continue;
            }

            out.add(new TransactionRow(bookingDate, amount, currency, description, reference, cols));
        }

        return out;
    }

    // --- helpers ---

    /** naive splitter for a single-char delimiter, no quotes support */
    private static String[] splitSimple(String line, char delimiter) {
        // fast path
        if (line.indexOf('"') < 0) {
            return line.split(java.util.regex.Pattern.quote(String.valueOf(delimiter)), -1);
        }
        // if someone sent quoted CSV, still do a naive split (documented limitation)
        return line.split(java.util.regex.Pattern.quote(String.valueOf(delimiter)), -1);
    }

    private static String get(String[] cols, Integer idx) {
        if (idx == null || idx < 0) return null;
        return idx < cols.length ? trimToNull(cols[idx]) : null;
    }

    private static String trimToNull(String s) {
        if (s == null) return null;
        String t = s.trim();
        return t.isEmpty() ? null : t;
        }

    private static LocalDate safeParseDate(String s, DateTimeFormatter fmt) {
        if (s == null) return null;
        try { return LocalDate.parse(s.trim(), fmt); }
        catch (DateTimeParseException e) { return null; }
    }

    private static BigDecimal extractAmount(String[] cols,
                                           Integer amountIdx,
                                           Integer creditIdx,
                                           Integer debitIdx,
                                           String decSep,
                                           String thouSep) {
        if (amountIdx != null) {
            return parseAmount(get(cols, amountIdx), decSep, thouSep);
        }
        BigDecimal credit = parseAmount(get(cols, creditIdx), decSep, thouSep);
        BigDecimal debit  = parseAmount(get(cols, debitIdx),  decSep, thouSep);
        if (credit == null && debit == null) return null;
        if (credit == null) return debit == null ? null : debit.negate(); // treat sole debit as negative
        if (debit == null)  return credit;                                 // sole credit
        // both present: credit - debit
        return credit.subtract(debit);
    }

    private static BigDecimal parseAmount(String s, String decSep, String thouSep) {
        if (s == null) return null;
        String cleaned = s.trim();
        if (!thouSep.isEmpty()) cleaned = cleaned.replace(thouSep, "");
        cleaned = cleaned.replace(decSep, ".");
        // allow leading +/-, and optional spaces
        try {
            return new BigDecimal(cleaned);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private static boolean looksLikeHeaderRow(String[] cols, DateTimeFormatter fmt, Integer bookingIdx) {
        if (bookingIdx == null || bookingIdx < 0 || bookingIdx >= cols.length) return false;
        String v = cols[bookingIdx];
        if (v == null) return false;
        // if the supposed date cell contains letters, likely header
        for (int i = 0; i < v.length(); i++) {
            if (Character.isLetter(v.charAt(i))) return true;
        }
        // if it parses as date, it is data, not header
        try { LocalDate.parse(v.trim(), fmt); return false; }
        catch (Exception e) { return true; }
    }
}
