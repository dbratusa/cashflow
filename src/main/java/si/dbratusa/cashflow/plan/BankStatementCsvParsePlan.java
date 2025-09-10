package si.dbratusa.cashflow.plan;


import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name = "BANK_STATEMENT_CSV_PARSE_PLAN", uniqueConstraints = @UniqueConstraint(name = "UK_BS_PARSE_PLAN_NAME", columnNames = "name"))
public class BankStatementCsvParsePlan extends PanacheEntity implements IBankStatementCsvParsePlan {

	private String name;
	private String headerFingerprint;
	private String charset;
	private String delimiter;
	private Integer headerRowIndex;
	private String dateFormatPattern;
	private String decimalSeparator;
	private String thousandSeparator;
	private Integer creditColumnIndex;
	private Integer debitColumnIndex;
	private Integer amountColumnIndex;
	private Integer currencyColumnIndex;
	private Integer transactionDescriptionColumnIndex;
	private Integer bookingDateColumnIndex;
	private Integer referenceNumberColumnIndex;

	protected BankStatementCsvParsePlan() {
		super();
	}

	@Override
	@Column(name = "name", nullable = false, unique = true)
	public String getName() {
		return name;
	}

	@Override
	public String name() {
		return getName();
	}

	public void setName(final String name) {
		this.name = name;
	}


	@Override
	public String getHeaderFingerprint() {
		return headerFingerprint;
	}

	@Override
	public String headerFingerprint() {
		return getHeaderFingerprint();
	}

	public void setHeaderFingerprint(final String headerFingerprint) {
		this.headerFingerprint = headerFingerprint;
	}

	@Override
	public String getCharset() {
		return charset;
	}

	@Override
	public String charset() {
		return getCharset();
	}

	public void setCharset(final String charset) {
		this.charset = charset;
	}

	@Override
	public String getDelimiter() {
		return delimiter;
	}

	@Override
	public String delimiter() {
		return getDelimiter();
	}

	public void setDelimiter(final String delimiter) {
		this.delimiter = delimiter;
	}

	@Override
	public Integer getHeaderRowIndex() {
		return headerRowIndex;
	}

	@Override
	public Integer headerRowIndex() {
		return getHeaderRowIndex();
	}

	public void setHeaderRowIndex(final Integer headerRowIndex) {
		this.headerRowIndex = headerRowIndex;
	}

	@Override
	public String getDateFormatPattern() {
		return dateFormatPattern;
	}

	@Override
	public String dateFormatPattern() {
		return getDateFormatPattern();
	}

	public void setDateFormatPattern(final String dateFormatPattern) {
		this.dateFormatPattern = dateFormatPattern;
	}

	@Override
	public String getDecimalSeparator() {
		return decimalSeparator;
	}

	@Override
	public String decimalSeparator() {
		return getDecimalSeparator();
	}

	public void setDecimalSeparator(final String decimalSeparator) {
		this.decimalSeparator = decimalSeparator;
	}

	@Override
	public String getThousandSeparator() {
		return thousandSeparator;
	}

	@Override
	public String thousandSeparator() {
		return getThousandSeparator();
	}

	public void setThousandSeparator(final String thousandSeparator) {
		this.thousandSeparator = thousandSeparator;
	}

	@Override
	public Integer getCreditColumnIndex() {
		return creditColumnIndex;
	}

	@Override
	public Integer creditColumnIndex() {
		return getCreditColumnIndex();
	}

	public void setCreditColumnIndex(final Integer creditColumnIndex) {
		this.creditColumnIndex = creditColumnIndex;
	}

	@Override
	public Integer getDebitColumnIndex() {
		return debitColumnIndex;
	}

	@Override
	public Integer debitColumnIndex() {
		return getDebitColumnIndex();
	}

	public void setDebitColumnIndex(final Integer debitColumnIndex) {
		this.debitColumnIndex = debitColumnIndex;
	}

	@Override
	public Integer getAmountColumnIndex() {
		return amountColumnIndex;
	}

	@Override
	public Integer amountColumnIndex() {
		return getAmountColumnIndex();
	}

	public void setAmountColumnIndex(final Integer amountColumnIndex) {
		this.amountColumnIndex = amountColumnIndex;
	}

	@Override
	public Integer getCurrencyColumnIndex() {
		return currencyColumnIndex;
	}

	@Override
	public Integer currencyColumnIndex() {
		return getCurrencyColumnIndex();
	}

	public void setCurrencyColumnIndex(final Integer currencyColumnIndex) {
		this.currencyColumnIndex = currencyColumnIndex;
	}

	@Override
	public Integer getTransactionDescriptionColumnIndex() {
		return transactionDescriptionColumnIndex;
	}

	@Override
	public Integer transactionDescriptionColumnIndex() {
		return getTransactionDescriptionColumnIndex();
	}

	public void setTransactionDescriptionColumnIndex(final Integer transactionDescriptionColumnIndex) {
		this.transactionDescriptionColumnIndex = transactionDescriptionColumnIndex;
	}

	@Override
	public Integer getBookingDateColumnIndex() {
		return bookingDateColumnIndex;
	}

	@Override
	public Integer bookingDateColumnIndex() {
		return getBookingDateColumnIndex();
	}

	public void setBookingDateColumnIndex(final Integer bookingDateColumnIndex) {
		this.bookingDateColumnIndex = bookingDateColumnIndex;
	}

	@Override
	public Integer getReferenceNumberColumnIndex() {
		return referenceNumberColumnIndex;
	}

	@Override
	public Integer referenceNumberColumnIndex() {
		return getReferenceNumberColumnIndex();
	}

	public void setReferenceNumberColumnIndex(final Integer referenceNumberColumnIndex) {
		this.referenceNumberColumnIndex = referenceNumberColumnIndex;
	}
}
