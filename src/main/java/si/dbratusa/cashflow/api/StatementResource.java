package si.dbratusa.cashflow.api;

import jakarta.inject.Inject;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import si.dbratusa.cashflow.service.BankStatement;
import si.dbratusa.cashflow.ai.StatementAiService;

@Path("/ai/statement")
public class StatementResource {

	@Inject
	StatementAiService ai;

	@POST
	@Consumes({"text/csv", MediaType.TEXT_PLAIN})
	@Produces(CustMediaType.APPLICATION_JSON_UTF8)
	public BankStatement parseCsv(String csvBody) {
		if (csvBody == null || csvBody.isBlank()) {
			throw new BadRequestException("CSV body is empty");
		}
		return ai.parse(csvBody);
	}
}
