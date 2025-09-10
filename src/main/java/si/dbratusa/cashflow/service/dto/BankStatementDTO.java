package si.dbratusa.cashflow.service.dto;

import java.time.LocalDate;
import java.util.List;

public record BankStatementDTO(
	String bankName,
	String accountIban,
	String accountHolder,
	String currency,
	LocalDate periodFrom,
	LocalDate periodTo,
	Double openingBalance,
	Double closingBalance,
	List<BankTransactionDTO> transactions
) {}
