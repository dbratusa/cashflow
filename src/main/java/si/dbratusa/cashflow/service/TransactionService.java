package si.dbratusa.cashflow.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import si.dbratusa.cashflow.service.entities.BankStatement;
import si.dbratusa.cashflow.service.entities.BankStatementCsvParsePlan;
import si.dbratusa.cashflow.service.entities.BankStatementCsvParsePlanAlias;
import si.dbratusa.cashflow.service.entities.BankTransaction;

import java.nio.charset.Charset;

@ApplicationScoped
public class TransactionService {

	@Transactional
	public BankStatement getStatement(final String fileName) {
		return BankStatement.find("fileName", fileName).firstResult();
	}

	@Transactional(Transactional.TxType.REQUIRED)
	public void parseAndPersist(String csv,
							   Charset charset,
							   IBankStatementCsvParsePlan planEntity) {
		var body = charset == null ? csv : new String(csv.getBytes(charset), charset);
		var rows = PlanParser.parse(body, planEntity);
		var bankStatement = new BankStatement();
		var alias = BankStatementCsvParsePlanAlias.<BankStatementCsvParsePlanAlias> find("plan", planEntity).firstResult();
		bankStatement.fileName = alias.alias;
		bankStatement.persist();
		for (var r : rows) {
			if (r.bookingDate() == null || r.amount() == null) {
				continue;
			}
			var tx = new BankTransaction();
			tx.bookingDate = r.bookingDate();
			tx.amount = r.amount();
			tx.currency = r.currency();
			tx.description = r.description();
			tx.reference = r.reference();
			tx.plan = (BankStatementCsvParsePlan) planEntity;
			tx.persist();
			tx.statement = bankStatement;
			bankStatement.addTransaction(tx);
		}
	}
}