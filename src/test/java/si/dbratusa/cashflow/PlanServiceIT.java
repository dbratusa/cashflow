package si.dbratusa.cashflow;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import io.restassured.config.EncoderConfig;
import io.restassured.http.ContentType;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import si.dbratusa.cashflow.plan.PlanService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


@QuarkusTest
public class PlanServiceIT {

	@Inject
	PlanService planService;

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
	public void testPlanGeneration() {
		var parsePlan = planService.generatePlan("test_plan", SAMPLE_CSV);
		assertNotNull(parsePlan);
		assertEquals("test_plan", parsePlan.name());
		assertEquals(";", parsePlan.delimiter());
		assertEquals("UTF-8", parsePlan.charset());
		assertEquals(0, parsePlan.headerRowIndex());
		assertEquals("yyyy-MM-dd", parsePlan.dateFormatPattern());
		assertEquals(",", parsePlan.decimalSeparator());
		assertEquals("", parsePlan.thousandSeparator());
		assertEquals(5, parsePlan.amountColumnIndex());
		assertEquals(2, parsePlan.transactionDescriptionColumnIndex());
		assertEquals(0, parsePlan.bookingDateColumnIndex());
	}
}
