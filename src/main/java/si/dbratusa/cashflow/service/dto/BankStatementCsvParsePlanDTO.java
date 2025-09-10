package si.dbratusa.cashflow.service.dto;

import si.dbratusa.cashflow.service.IBankStatementCsvParsePlan;

public record BankStatementCsvParsePlanDTO(
	String headerFingerprint,
	String charset,
	String delimiter,
	Integer headerRowIndex,
	String dateFormatPattern,
	String decimalSeparator,
	String thousandSeparator,
	Integer creditColumnIndex,
	Integer debitColumnIndex,
	Integer amountColumnIndex,
	Integer currencyColumnIndex,
	Integer transactionDescriptionColumnIndex,
	Integer bookingDateColumnIndex,
	Integer referenceNumberColumnIndex
) implements IBankStatementCsvParsePlan {

	public static BankStatementCsvParsePlanDTO from(final IBankStatementCsvParsePlan plan) {
		if (plan == null) {
			return null;
		}
		return new BankStatementCsvParsePlanDTO(
			plan.headerFingerprint(),
			plan.charset(),
			plan.delimiter(),
			plan.headerRowIndex(),
			plan.dateFormatPattern(),
			plan.decimalSeparator(),
			plan.thousandSeparator(),
			plan.creditColumnIndex(),
			plan.debitColumnIndex(),
			plan.amountColumnIndex(),
			plan.currencyColumnIndex(),
			plan.transactionDescriptionColumnIndex(),
			plan.bookingDateColumnIndex(),
			plan.referenceNumberColumnIndex()
		);
	}
}
