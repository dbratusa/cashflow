package si.dbratusa.cashflow.api;

import jakarta.inject.Inject;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import si.dbratusa.cashflow.service.dto.BankStatementEnqueueDTO;
import si.dbratusa.cashflow.service.ImportService;

@Path("/statement")
public class StatementResource {

	@Inject
	ImportService importService;

	@POST
	@Consumes({"text/csv", MediaType.TEXT_PLAIN})
	@Produces(CustMediaType.APPLICATION_JSON_UTF8)
	public BankStatementEnqueueDTO parseCsv(final String csvBody, @HeaderParam("X-File-Name") final String fileName) {
		if (csvBody == null || csvBody.isBlank()) {
			throw new BadRequestException("CSV body is empty");
		}
		importService.enqueueImportBankStatement(csvBody, fileName);
		return new BankStatementEnqueueDTO(csvBody, fileName);
	}
}
