package si.dbratusa;

import org.apache.commons.csv.*;

import java.nio.charset.Charset;
import java.nio.file.*;

public class BankClean {

	private static final Charset WIN1250 = Charset.forName("windows-1250");
	private static final Charset UTF8 = java.nio.charset.StandardCharsets.UTF_8;

	public static void main(String[] args) throws Exception {
		if (args.length == 0) {
			System.err.println("No file name provided. Usage: bank-clean <filename.csv>");
			System.exit(1);
		}
		var inPath = Paths.get(args[0]);
		var outPath = inPath.resolveSibling("cleaned_" + inPath.getFileName());

		// Build CSV formats properly
		var parseFormat = CSVFormat.DEFAULT.builder()
			.setDelimiter(';')
			.setHeader()
			.setSkipHeaderRecord(true)
			.setIgnoreEmptyLines(true)
			.setTrim(true)
			.build();

		var printFormat = CSVFormat.DEFAULT.builder()
			.setDelimiter(';')
			.setHeader("DATE", "DEBIT", "CREDIT", "DESCRIPTION")
			.build();

		try (var reader = Files.newBufferedReader(inPath, WIN1250);
			 var parser = new CSVParser(reader, parseFormat);
			 var writer = Files.newBufferedWriter(outPath, UTF8);
			 var printer = new CSVPrinter(writer, printFormat)) {

			for (var record : parser) {
				if (isAllEmpty(record)) continue;

				String date        = getTrim(record, "DATUM KNJIŽENJA");
				String debit       = getTrim(record, "DOBRO");
				String credit      = getTrim(record, "BREME");
				String purpose     = getTrim(record, "NAMEN");
				String participant = getTrim(record, "UDELEŽENEC - NAZIV");

				String description = purpose.equals(participant) || participant.isEmpty()
					? purpose
					: (purpose.isEmpty() ? participant : purpose + " " + participant);

				printer.printRecord(date, debit, credit, description);
			}
		}
		System.out.println("Done → " + outPath.toAbsolutePath());
	}

	private static boolean isAllEmpty(CSVRecord r) {
		for (var v : r) {
			if (v != null && !v.trim().isEmpty()) return false;
		}
		return true;
	}

	private static String getTrim(CSVRecord r, String header) {
		var v = r.isMapped(header) ? r.get(header) : null;
		return v == null ? "" : v.trim();
	}
}
