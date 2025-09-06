package si.dbratusa;

import dev.langchain4j.service.SystemMessage;
import io.quarkiverse.langchain4j.RegisterAiService;
import jakarta.enterprise.context.RequestScoped;

@RegisterAiService
@SystemMessage("""
	You are a bank statement reader. You can read bank statements and extract information from them.
	You will receive the bank statement as a CSV file.
	The header of the CSV file will be the first line.
	Each subsequent line will contain the data for one transaction.
	Each line will contain the following columns:
	- Date (in the format YYYY-MM-DD)
	- Description
	- Amount (in the format 1234.56)
	- Currency (3-letter code)
	- Balance (in the format 1234.56)
	You will extract the following information:
	- The total number of transactions
	- The total amount of all transactions
	- The average amount of all transactions
	- The total amount of all transactions in EUR
	- The average amount of all transactions in EUR
	- The total amount of all transactions in USD
	- The average amount of all transactions in USD
	""")
@RequestScoped
public interface BankStatementAIService {

	@SystemMessage("""
            		You will receive a CSV file with bank statement data.
            		You will extract the bank statement information and return it as a list of Transactions.
            """)
	BankStatementResponse process(String input);
}
