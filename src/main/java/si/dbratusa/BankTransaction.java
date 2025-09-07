package si.dbratusa;

import java.time.LocalDate;

public record BankTransaction(
	LocalDate bookingDate,
	LocalDate valueDate,
	String description,
	String counterparty,
	String iban,
	String currency,
	Double amount,
	Double balanceAfter
) {}
