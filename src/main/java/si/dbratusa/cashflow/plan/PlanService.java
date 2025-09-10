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
		var fingerprint = PlanHasher.fingerprint(draft);
		draft.setHeaderFingerprint(fingerprint);
		draft.persist();
		return BankStatementCsvParsePlanDTO.from(draft);
	}

	@Transactional(Transactional.TxType.REQUIRED)
	public IBankStatementCsvParsePlan findByName(final String name) {
		var plan = BankStatementCsvParsePlan.<BankStatementCsvParsePlan> find("name", name).firstResult();
		return BankStatementCsvParsePlanDTO.from(plan);
	}
}
