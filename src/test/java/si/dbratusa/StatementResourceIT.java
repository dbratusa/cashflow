package si.dbratusa;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import io.restassured.config.EncoderConfig;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.nullValue;

@QuarkusTest
class StatementResourceIT {

	@BeforeAll
	static void restAssuredUtf8() {
		RestAssured.config = RestAssured.config().encoderConfig(
			EncoderConfig.encoderConfig()
				.defaultContentCharset("UTF-8")
				.encodeContentTypeAs("text/csv", ContentType.TEXT)
		);
	}

	// Known-good CSV with clear headers and values the LLM should map 1:1.
	private static final String SAMPLE_CSV = """
            Datum knjiženja;Valuta;Opis;Protistranka;IBAN;Znesek;Valuta
            2025-08-01;2025-08-01;Nakup Mercator;;SI56123400000000000;-23,45;EUR
            2025-08-02;2025-08-02;Nakazilo plača;Podjetje d.o.o.;SI56000099999999999;1500,00;EUR
            2025-08-03;2025-08-03;SEPA kreditno plačilo;Janez Novak;SI56000011111111111;100,00;EUR
            2025-08-04;2025-08-04;Provizija;;SI56123400000000000;-0,45;EUR
            """;

	@Test
	void realAi_parsesEveryField() {
		given()
			.contentType("text/csv; charset=UTF-8")
			.body(SAMPLE_CSV)
			.when()
			.post("/ai/statement")
			.then()
			.statusCode(200)

			// Transactions size and structure
			.body("transactions", hasSize(greaterThanOrEqualTo(4)))

			// Tx #0
			.body("transactions[0].bookingDate", equalTo("2025-08-01"))
			.body("transactions[0].valueDate", equalTo("2025-08-01"))
			.body("transactions[0].description", containsString("Nakup"))
			.body("transactions[0].counterparty", anyOf(nullValue(), equalTo(""))) // purchase may not have counterparty
			.body("transactions[0].iban", equalTo("SI56123400000000000"))
			.body("transactions[0].currency", equalTo("EUR"))
			.body("transactions[0].amount", equalTo(-23.45f))

			// Tx #1
			.body("transactions[1].bookingDate", equalTo("2025-08-02"))
			.body("transactions[1].valueDate", equalTo("2025-08-02"))
			.body("transactions[1].description", containsString("Nakazilo plača"))
			.body("transactions[1].counterparty", equalTo("Podjetje d.o.o."))
			.body("transactions[1].iban", equalTo("SI56000099999999999"))
			.body("transactions[1].currency", equalTo("EUR"))
			.body("transactions[1].amount", equalTo(1500.00f))

			// Tx #2
			.body("transactions[2].bookingDate", equalTo("2025-08-03"))
			.body("transactions[2].valueDate", equalTo("2025-08-03"))
			.body("transactions[2].description", containsString("SEPA"))
			.body("transactions[2].counterparty", equalTo("Janez Novak"))
			.body("transactions[2].iban", equalTo("SI56000011111111111"))
			.body("transactions[2].currency", equalTo("EUR"))
			.body("transactions[2].amount", equalTo(100.00f))

			// Tx #3
			.body("transactions[3].bookingDate", equalTo("2025-08-04"))
			.body("transactions[3].valueDate", equalTo("2025-08-04"))
			.body("transactions[3].description", containsString("Provizija"))
			.body("transactions[3].iban", equalTo("SI56123400000000000"))
			.body("transactions[3].currency", equalTo("EUR"))
			.body("transactions[3].amount", equalTo(-0.45f));
	}
}