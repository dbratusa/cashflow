package si.dbratusa.api.service.impl;

import java.time.LocalDate;
import java.util.List;

public record BankStatement(
	String bankName,
	String accountIban,
	String accountHolder,
	String currency,
	LocalDate periodFrom,
	LocalDate periodTo,
	Double openingBalance,
	Double closingBalance,
	List<BankTransaction> transactions
) {}
