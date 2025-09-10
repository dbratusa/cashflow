package si.dbratusa.cashflow.ai;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import io.quarkiverse.langchain4j.RegisterAiService;
import jakarta.enterprise.context.ApplicationScoped;
import si.dbratusa.cashflow.plan.BankStatementCsvParsePlan;

@RegisterAiService
@ApplicationScoped
@SystemMessage("""
	You are a CSV normalization expert. Given raw bank statement CSV text, produce a JSON object
	that maps 1:1 to the Java class 'BankStatementCsvParsePlan' (JPA entity) with these fields:
	- name: string, short stable name for this layout (e.g., "nlb-v1"). If unknown, synthesize from bank in header or "draft-<fingerprint>".
	- headerFingerprint: string, stable lowercase sha256-like fingerprint of the detected header line (you may hash the normalized header, or echo a stable placeholder if hashing is unavailable).
	- charset: string, IANA name; default "UTF-8" unless CSV clearly uses a legacy codepage.
	- delimiter: string of length 1; one of ";", ",", or "\\t". Detect by the line with the most repeated separator chars.
	- headerRowIndex: 0-based index of the header row.
	- dateFormatPattern: a single java.time DateTimeFormatter pattern for transaction rows (e.g., "yyyy-MM-dd" or "dd.MM.yyyy").
	- decimalSeparator: "," or "."
	- thousandSeparator: "." or "," (use empty string if there is none).
	- creditColumnIndex: 0-based column index that holds credit amounts (nullable). Use when CSV splits IN/OUT.
	- debitColumnIndex: 0-based column index that holds debit amounts (nullable). Use when CSV splits IN/OUT.
	- amountColumnIndex: 0-based column index that holds signed amount (nullable). Use when CSV has a single column.
	  Exactly one of (amountColumnIndex) or (creditColumnIndex/debitColumnIndex) must be set.
	- currencyColumnIndex: 0-based index of currency if present, else null.
	- transactionDescriptionColumnIndex: 0-based index of description/memo/purpose text if present, else null.
	- bookingDateColumnIndex: 0-based index of booking/post date.
	- referenceNumberColumnIndex: 0-based index of reference/id (e.g., payment reference), nullable.
	
	Rules:
	- Use 0-based indices. Infer from the detected header row.
	- Normalize header by trimming, collapsing spaces, and lowercasing for matching.
	- Common header synonyms:
	  bookingDate ~ ["datum knjiženja","booking date","date","transaction date"]
	  valueDate   ~ ["valuta","value date"]
	  description ~ ["opis","namen","description","memo","details"]
	  counterparty~ ["protistranka","payee","payer","participant","beneficiary"]
	  iban        ~ ["iban","account","account number"]
	  amount      ~ ["znesek","amount","amount eur","total"]
	  debit/credit~ ["out","in","bremenitev","dobropis","debit","credit"]
	  currency    ~ ["valuta","currency","cur"]
	  reference   ~ ["sklic","reference","ref","id","transaction id"]
	- If multiple candidates exist, choose the one that best matches data patterns on the first 20 rows.
	- If dates vary, pick the dominant pattern; prefer ISO "yyyy-MM-dd" when ambiguous.
	- If thousands separator is absent, set thousandSeparator to empty string "".
	- Never invent columns. If a field is missing in the CSV, return null for its index.
	- Output ONLY the JSON instance for BankStatementCsvParsePlan. No prose.
	- If a amount column contains both positive and negative values, use amountColumnIndex and set creditColumnIndex and debitColumnIndex to null.
	""")
public interface PlanAiService {

	/**
	 * Propose a parse plan for this CSV.
	 *
	 * @param csv The raw CSV text (UTF-8). It may contain a preamble before header.
	 */
	@UserMessage("""
		Detect a parsing plan for this CSV (0-based indices), and return ONLY a JSON object for BankStatementCsvParsePlan.
		CSV:
		---
		{csv}
		---
		""")
	BankStatementCsvParsePlan proposePlan(String csv);

	/**
	 * Same as above but let caller hint a name (e.g., bank/layout) to stabilize results.
	 */
	@UserMessage("""
		Detect a parsing plan for this CSV. Prefer the provided name as the plan 'name'.
		Return ONLY a JSON object for BankStatementCsvParsePlan.
		Name: {name}
		CSV:
		---
		{csv}
		---
		""")
	BankStatementCsvParsePlan proposePlanWithName(String name, String csv);
}