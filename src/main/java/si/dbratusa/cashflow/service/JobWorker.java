package si.dbratusa.cashflow.service;

import io.quarkus.vertx.ConsumeEvent;
import io.smallrye.common.annotation.Blocking;
import io.vertx.core.MultiMap;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class JobWorker {

	@Inject
	PlanService planService;

	@Blocking
	@ConsumeEvent(WorkerJobs.IMPORT_BANK_STATEMENT)
	public void processImportBankStatement(MultiMap headers, String payload) {
		var fileName = headers.get("fileName");
		planService.generatePlan(fileName, payload);
	}
}
