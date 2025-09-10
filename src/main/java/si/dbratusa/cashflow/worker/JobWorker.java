package si.dbratusa.cashflow.worker;

import io.quarkus.vertx.ConsumeEvent;
import io.smallrye.common.annotation.Blocking;
import io.vertx.core.MultiMap;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import si.dbratusa.cashflow.plan.PlanService;
import si.dbratusa.cashflow.service.Jobs;

@ApplicationScoped
public class JobWorker {

	@Inject
	PlanService planService;

	@Blocking
	@ConsumeEvent(Jobs.IMPORT_BANK_STATEMENT)
	public void processImportBankStatement(MultiMap headers, String payload) {
		var fileName = headers.get("fileName");
		planService.generatePlan(fileName, payload);
	}
}
