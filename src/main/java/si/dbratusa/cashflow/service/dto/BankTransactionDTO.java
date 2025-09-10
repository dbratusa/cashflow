package si.dbratusa.cashflow.service.dto;

import java.time.LocalDate;

public record BankTransactionDTO(
	LocalDate bookingDate,
	LocalDate valueDate,
	String description,
	String counterparty,
	String iban,
	String currency,
	Double amount,
	Double balanceAfter
) {}
