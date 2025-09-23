package si.dbratusa.cashflow;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import io.restassured.config.EncoderConfig;
import io.restassured.http.ContentType;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import si.dbratusa.cashflow.service.PlanService;
import si.dbratusa.cashflow.service.TransactionService;

import java.time.Duration;

import static io.restassured.RestAssured.given;
import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@QuarkusTest
class StatementResourceIT {

	@Inject
	PlanService planService;

	@Inject
	TransactionService txService;

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
	@Transactional
	void testStatementResourceReturnsPlan() {
		given()
			.contentType("text/csv; charset=UTF-8")
			.header("X-File-Name", "statement-2025-09.csv")
			.body(SAMPLE_CSV)
			.when()
			.post("/statement")
			.then()
			.statusCode(200);

		await()
			.atMost(Duration.ofSeconds(30))
			.pollDelay(Duration.ofMillis(100))     // initial delay
			.pollInterval(Duration.ofMillis(250))  // poll cadence
			.until(() -> planService.findByName("statement-2025-09.csv") != null);

		var plan = planService.findByName("statement-2025-09.csv");
		assertNotNull(plan);
		assertEquals(";", plan.delimiter());
		assertEquals("UTF-8", plan.charset());
		assertEquals(0, plan.headerRowIndex());
		assertEquals("yyyy-MM-dd", plan.dateFormatPattern());
		assertEquals(",", plan.decimalSeparator());
		assertEquals("", plan.thousandSeparator());
		assertEquals(5, plan.amountColumnIndex());
		assertEquals(2, plan.transactionDescriptionColumnIndex());
		assertEquals(0, plan.bookingDateColumnIndex());

		var statement = txService.getStatement("statement-2025-09.csv");
		assertEquals(4, statement.transactions.size());
	}
}