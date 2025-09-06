package si.dbratusa;

import java.util.List;

public class BankStatementResponse {

	private String fileName;
	private List<Transaction> transactions;

	public BankStatementResponse(final String fileName, final List<Transaction> transactions) {
		this.fileName = fileName;
		this.transactions = transactions;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(final String fileName) {
		this.fileName = fileName;
	}

	public List<Transaction> getTransactions() {
		return transactions;
	}

	public void setTransactions(final List<Transaction> transactions) {
		this.transactions = transactions;
	}

	public void addTransaction(final Transaction transaction) {
		this.transactions.add(transaction);
	}
}
