package si.dbratusa.cashflow.impl;

import io.quarkiverse.langchain4j.RegisterAiService;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import jakarta.enterprise.context.ApplicationScoped;

@RegisterAiService
@ApplicationScoped
@SystemMessage("""
You transform arbitrary bank CSVs into a normalized JSON that exactly matches
the Java type 'BankStatement' and its nested 'BankTransaction'.
Rules:
- CSV can use ; , or \t and various encodings; treat first non-empty line with the most separators as header if present.
- Detect dates and parse to ISO-8601 (yyyy-MM-dd).
- Amounts: convert to a single 'amount' where credits are positive and debits negative. Handle columns like DEBIT/CREDIT, +/- signs, or separate "In"/"Out".
- Currency: prefer a column value, else infer from symbols, else fallback to account currency.
- Ignore empty lines and totals rows.
- If a field is missing in source, set it to null.
""")
public interface StatementAiService {

	@UserMessage("""
Parse the statement CSV delimited by --- and return ONLY a JSON instance of BankStatement.
CSV:
---
{csv}
---
If there is ambiguity (e.g., multiple date columns), pick the one most consistent for transaction rows.
""")
	BankStatement parse(String csv);
}
