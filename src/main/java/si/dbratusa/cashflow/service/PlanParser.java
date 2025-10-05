package si.dbratusa.cashflow.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class PlanParser {

    public static List<TransactionRow> parse(String csv, IBankStatementCsvParsePlan plan) {
        Objects.requireNonNull(csv, "csv");
        Objects.requireNonNull(plan, "plan");
        var delimiter = Objects.requireNonNullElse(plan.delimiter(), ",").charAt(0);
        var headerRowIndex = plan.headerRowIndex() == null ? 0 : plan.headerRowIndex();
        var startRow = Math.max(0, headerRowIndex + 1);
        var dateFmt = DateTimeFormatter.ofPattern(
            Objects.requireNonNullElse(plan.dateFormatPattern(), "yyyy-MM-dd"),
            Locale.ROOT
        );
        var decSep = Objects.requireNonNullElse(plan.decimalSeparator(), ".");
        var thouSep = Objects.requireNonNullElse(plan.thousandSeparator(), "");
        var amountIdx = plan.amountColumnIndex();
        var creditIdx = plan.creditColumnIndex();
        var debitIdx  = plan.debitColumnIndex();
        var currencyIdx = plan.currencyColumnIndex();
        var descIdx     = plan.transactionDescriptionColumnIndex();
        var bookingIdx  = plan.bookingDateColumnIndex();
        var refIdx      = plan.referenceNumberColumnIndex();
        var out = new ArrayList<TransactionRow>();
        var lines = csv.split("\r?\n");
        for (int i = startRow; i < lines.length; i++) {
            var line = lines[i];
            if (line == null) {
                continue;
            }
            line = line.trim();
            if (line.isEmpty()) {
                continue;
            }
            var cols = splitSimple(line, delimiter);
            if (looksLikeHeaderRow(cols, dateFmt, bookingIdx)) {
                continue;
            }
            var bookingDate = safeParseDate(get(cols, bookingIdx), dateFmt);
            var amount = extractAmount(cols, amountIdx, creditIdx, debitIdx, decSep, thouSep);
            var currency = get(cols, currencyIdx);
            var description = get(cols, descIdx);
            var reference = get(cols, refIdx);
            if (bookingDate == null && amount == null) {
                continue;
            }
            out.add(new TransactionRow(bookingDate, amount, currency, description, reference, cols));
        }
        return out;
    }

    private static String[] splitSimple(String line, char delimiter) {
        if (line.indexOf('"') < 0) {
            return line.split(java.util.regex.Pattern.quote(String.valueOf(delimiter)), -1);
        }
        return line.split(java.util.regex.Pattern.quote(String.valueOf(delimiter)), -1);
    }

    private static String get(String[] cols, Integer idx) {
        if (idx == null || idx < 0) {
            return null;
        }
        return idx < cols.length ? trimToNull(cols[idx]) : null;
    }

    private static String trimToNull(String s) {
        if (s == null) {
            return null;
        }
        var t = s.trim();
        return t.isEmpty() ? null : t;
    }

    private static LocalDate safeParseDate(String s, DateTimeFormatter fmt) {
        if (s == null) {
            return null;
        }
        try {
            return LocalDate.parse(s.trim(), fmt);
        } catch (DateTimeParseException e) {
            return null;
        }
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
        var credit = parseAmount(get(cols, creditIdx), decSep, thouSep);
        var debit  = parseAmount(get(cols, debitIdx),  decSep, thouSep);
        if (credit == null && debit == null) {
            return null;
        }
        if (credit == null) {
            return debit.negate();
        }
        if (debit == null) {
            return credit;
        }
        return credit.subtract(debit);
    }

    private static BigDecimal parseAmount(String s, String decSep, String thouSep) {
        if (s == null) {
            return null;
        }
        var cleaned = s.trim();
        if (!thouSep.isEmpty()) {
            cleaned = cleaned.replace(thouSep, "");
        }
        cleaned = cleaned.replace(decSep, ".");
        try {
            return new BigDecimal(cleaned);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private static boolean looksLikeHeaderRow(String[] cols, DateTimeFormatter fmt, Integer bookingIdx) {
        if (bookingIdx == null || bookingIdx < 0 || bookingIdx >= cols.length) {
            return false;
        }
        var v = cols[bookingIdx];
        if (v == null){
            return false;
        }
        for (int i = 0; i < v.length(); i++) {
            if (Character.isLetter(v.charAt(i))) {
                return true;
            }
        }
        try {
            LocalDate.parse(v.trim(), fmt);
            return false;
        } catch (Exception e) {
            return true;
        }
    }
}
