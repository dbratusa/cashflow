package si.dbratusa.cashflow.service;

import java.math.BigDecimal;
import java.time.LocalDate;

public record TransactionRow(
	LocalDate bookingDate,
	BigDecimal amount,
	String currency,
	String description,
	String reference,
	String[] raw
) {}
