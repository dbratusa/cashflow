package si.dbratusa.cashflow.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import si.dbratusa.cashflow.service.entities.BankTransaction;
import si.dbratusa.cashflow.service.entities.BankStatementCsvParsePlan;

import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.util.HexFormat;
import java.util.List;
import java.util.Objects;

@ApplicationScoped
public class TransactionImportService {

	@Transactional
	public int parseAndPersist(String csv,
							   Charset charset,
							   IBankStatementCsvParsePlan planEntity) {
		// decode if needed
		String body = charset == null ? csv : new String(csv.getBytes(charset), charset);

		List<PlanParser.TransactionRow> rows = PlanParser.parse(body, planEntity);
		int inserted = 0;

		for (var r : rows) {
			if (r.bookingDate() == null || r.amount() == null) continue; // basic guard

			var tx = new BankTransaction();
			tx.bookingDate = r.bookingDate();
			tx.amount = r.amount();
			tx.currency = r.currency();
			tx.description = r.description();
			tx.reference = r.reference();
			tx.plan = (BankStatementCsvParsePlan) planEntity;

			// If you keep unique fingerprint: skip on conflict
			try {
				tx.persist();
				inserted++;
			} catch (jakarta.persistence.PersistenceException e) {
				// assume unique violation → skip
			}
		}
		return inserted;
	}

	private static String rowFp(String planFp, PlanParser.TransactionRow r) {
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			String s = String.join("|",
				Objects.toString(planFp, ""),
				Objects.toString(r.bookingDate(), ""),
				Objects.toString(r.amount(), ""),
				Objects.toString(r.currency(), ""),
				Objects.toString(r.reference(), ""),
				// keep desc shorter to avoid accidental huge strings
				Objects.toString(r.description(), "").substring(0,
					Math.min(120, Objects.toString(r.description(), "").length()))
			);
			return "sha256:" + HexFormat.of().formatHex(md.digest(s.getBytes(java.nio.charset.StandardCharsets.UTF_8)));
		} catch (Exception e) {
			return null;
		}
	}
}