package si.dbratusa.cashflow.plan;

public interface IBankStatementCsvParsePlan {

	String headerFingerprint();

	default String getHeaderFingerprint() {
		return headerFingerprint();
	}

	String charset();

	default String getCharset() {
		return charset();
	}

	String delimiter();

	default String getDelimiter() {
		return delimiter();
	}

	Integer headerRowIndex();

	default Integer getHeaderRowIndex() {
		return headerRowIndex();
	}

	String dateFormatPattern();

	default String getDateFormatPattern() {
		return dateFormatPattern();
	}

	String decimalSeparator();

	default String getDecimalSeparator() {
		return decimalSeparator();
	}

	String thousandSeparator();

	default String getThousandSeparator() {
		return thousandSeparator();
	}

	Integer creditColumnIndex();

	default Integer getCreditColumnIndex() {
		return creditColumnIndex();
	}

	Integer debitColumnIndex();

	default Integer getDebitColumnIndex() {
		return debitColumnIndex();
	}

	Integer amountColumnIndex();

	default Integer getAmountColumnIndex() {
		return amountColumnIndex();
	}

	Integer currencyColumnIndex();

	default Integer getCurrencyColumnIndex() {
		return currencyColumnIndex();
	}

	Integer transactionDescriptionColumnIndex();

	default Integer getTransactionDescriptionColumnIndex() {
		return transactionDescriptionColumnIndex();
	}

	Integer bookingDateColumnIndex();

	default Integer getBookingDateColumnIndex() {
		return bookingDateColumnIndex();
	}

	Integer referenceNumberColumnIndex();

	default Integer getReferenceNumberColumnIndex() {
		return referenceNumberColumnIndex();
	}
}
