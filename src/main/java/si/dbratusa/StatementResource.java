package si.dbratusa;

import jakarta.inject.Inject;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/ai/statement")
public class StatementResource {

	@Inject StatementAiService ai;

	// Accept text/csv OR plain text with the CSV content
	@POST
	@Consumes({ "text/csv", MediaType.TEXT_PLAIN })
	@Produces(CustMediaType.APPLICATION_JSON_UTF8)
	public BankStatement parseCsv(String csvBody) {
		if (csvBody == null || csvBody.isBlank()) {
			throw new BadRequestException("CSV body is empty");
		}
		return ai.parse(csvBody);
	}
}
