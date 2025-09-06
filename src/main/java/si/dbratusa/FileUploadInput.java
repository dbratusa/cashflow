package si.dbratusa;

import jakarta.ws.rs.FormParam;
import org.jboss.resteasy.reactive.multipart.FileUpload;

import java.util.List;

public class FileUploadInput {

	@FormParam("text")
	private String text;

	@FormParam("file")
	public List<FileUpload> file;
}
