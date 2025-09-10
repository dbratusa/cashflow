package si.dbratusa.cashflow.service;

import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.core.eventbus.EventBus;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class ImportService {

	@Inject
	EventBus bus;

	public void enqueueImportBankStatement(final String payload, final String fileName) {
		var opts = new DeliveryOptions().addHeader("fileName", fileName);
		this.bus.publish(WorkerJobs.IMPORT_BANK_STATEMENT, payload, opts);
	}
}
