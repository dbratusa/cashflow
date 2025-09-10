package si.dbratusa.cashflow.plan;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.HexFormat;
import java.util.Objects;

public class PlanHasher {

	private static final ObjectMapper mapper = new ObjectMapper();

	public static String fingerprint(final IBankStatementCsvParsePlan plan) {
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			String content = String.join("|",
				Objects.toString(plan.charset(), ""),
				Objects.toString(plan.delimiter(), ""),
				Objects.toString(plan.headerRowIndex(), ""),
				Objects.toString(plan.dateFormatPattern(), ""),
				Objects.toString(plan.decimalSeparator(), ""),
				Objects.toString(plan.thousandSeparator(), ""),
				Objects.toString(plan.creditColumnIndex(), ""),
				Objects.toString(plan.debitColumnIndex(), ""),
				Objects.toString(plan.amountColumnIndex(), ""),
				Objects.toString(plan.currencyColumnIndex(), ""),
				Objects.toString(plan.transactionDescriptionColumnIndex(), ""),
				Objects.toString(plan.bookingDateColumnIndex(), ""),
				Objects.toString(plan.referenceNumberColumnIndex(), "")
			);
			byte[] digest = md.digest(content.getBytes(StandardCharsets.UTF_8));
			return "sha256:" + HexFormat.of().formatHex(digest);
		} catch (final Exception e) {
			throw new RuntimeException("Failed to compute plan fingerprint", e);
		}
	}
}
