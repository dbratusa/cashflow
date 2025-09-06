package si.dbratusa;

import io.quarkus.qute.Location;
import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import java.io.IOException;
import java.nio.file.Files;

@ApplicationScoped
@Produces(MediaType.TEXT_HTML)
@Path("bank-statement.html")
public class BankStatementResource {

    @Location("bank-statement.html")
    Template template;

    @Inject
    private BankStatementAIService bankStatementService;

    @GET
    public TemplateInstance get() {
        return template.instance();
    }

    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public TemplateInstance uploadAndProcessBankStatement(FileUploadInput input) {
        input.file.forEach(file -> {
			try {
                var documents = bankStatementService.process(Files.readString(file.uploadedFile()));
				documents.getTransactions().forEach(System.out::println);
            } catch (IOException e) {
				throw new RuntimeException(e);
			}
		});
        return template.instance();
    }
}
