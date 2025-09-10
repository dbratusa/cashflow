package si.dbratusa.cashflow.ui;

import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.jboss.resteasy.reactive.RestForm;
import org.jboss.resteasy.reactive.multipart.FileUpload;
import si.dbratusa.cashflow.service.PlanService;

import java.nio.file.Files;
import java.nio.charset.StandardCharsets;


@Path("/ui")
@Produces(MediaType.TEXT_HTML)
public class UiResource {

	@Inject Template ui_index;
	@Inject Template ui_aliases;
	@Inject UiQueryService ui;
	@Inject
	PlanService planService;

	@GET
	public TemplateInstance index() {
		return ui_index.data("aliases", ui.listRecentAliases(50));
	}

	@POST
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public void upload(@RestForm("file") FileUpload file,
	                   @RestForm("fileName") String fileName) throws java.io.IOException {
		if (file == null) {
			throw new BadRequestException("No file");
		}
		String name = (fileName != null && !fileName.isBlank()) ? fileName : file.fileName();
		byte[] bytes = Files.readAllBytes(file.uploadedFile());
		String csv = new String(bytes, StandardCharsets.UTF_8);
		planService.generatePlan(name, csv);
	}

	// partial refresh for the table
	@GET @Path("/aliases")
	public TemplateInstance aliases() {
		return ui_aliases.data("aliases", ui.listRecentAliases(50));
	}
}
