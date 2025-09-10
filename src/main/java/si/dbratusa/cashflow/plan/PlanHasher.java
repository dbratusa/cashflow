package si.dbratusa.cashflow.plan;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.HexFormat;

public class PlanHasher {

	private static final ObjectMapper mapper = new ObjectMapper();

	public static String fingerprint(final IBankStatementCsvParsePlan plan) {
		try {
			var json = mapper.writeValueAsString(plan);
			var md = MessageDigest.getInstance("SHA-256");
			var digest = md.digest(json.getBytes(StandardCharsets.UTF_8));
			return HexFormat.of().formatHex(digest);
		} catch (final Exception e) {
			throw new RuntimeException("Failed to compute plan fingerprint", e);
		}
	}
}
