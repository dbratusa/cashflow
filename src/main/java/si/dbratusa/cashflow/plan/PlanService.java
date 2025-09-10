package si.dbratusa.cashflow.plan;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import si.dbratusa.cashflow.ai.PlanAiService;
import si.dbratusa.cashflow.service.BankStatementCsvParsePlanDTO;

@ApplicationScoped
public class PlanService {

	@Inject
	PlanAiService planAi;

	@Transactional(Transactional.TxType.REQUIRED)
	public IBankStatementCsvParsePlan generatePlan(final String name, final String csv) {
		var draft = planAi.proposePlanWithName(name, csv);
		draft.setHeaderFingerprint(null);
		var fingerprint = PlanHasher.fingerprint(draft);
		var plan = BankStatementCsvParsePlan.<BankStatementCsvParsePlan>
				find("headerFingerprint", fingerprint)
			.firstResult();
		if (plan != null) {
			BankStatementCsvParsePlanAlias.find("alias", name)
				.firstResultOptional()
				.orElseGet(() -> {
					var a = new BankStatementCsvParsePlanAlias();
					a.alias = name;
					a.plan = plan;
					a.persist();
					return a;
				});
			return BankStatementCsvParsePlanDTO.from(plan);
		}
		draft.setHeaderFingerprint(fingerprint);
		draft.persist();
		BankStatementCsvParsePlanAlias.find("alias", name)
			.firstResultOptional()
			.orElseGet(() -> {
				var a = new BankStatementCsvParsePlanAlias();
				a.alias = name;
				a.plan = draft;
				a.persist();
				return a;
			});
		return BankStatementCsvParsePlanDTO.from(draft);
	}

	@Transactional(Transactional.TxType.REQUIRED)
	public IBankStatementCsvParsePlan findByName(final String name) {
		var plan = BankStatementCsvParsePlanAlias.<BankStatementCsvParsePlanAlias> find("alias", name).firstResult();
		if (plan == null) {
			return null;
		}
		return BankStatementCsvParsePlanDTO.from(plan.plan);
	}
}
