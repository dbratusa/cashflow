package si.dbratusa;

import java.util.Date;

public class Transaction {

	private double amount;
	private String currency;
	private double balance;
	private String description;
	private Date validityDate;

	public Transaction(final double amount, final String currency, final double balance, final String description, final Date validityDate) {
		this.amount = amount;
		this.currency = currency;
		this.balance = balance;
		this.description = description;
		this.validityDate = validityDate;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(final double amount) {
		this.amount = amount;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(final String currency) {
		this.currency = currency;
	}

	public double getBalance() {
		return balance;
	}

	public void setBalance(final double balance) {
		this.balance = balance;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(final String description) {
		this.description = description;
	}

	public Date getValidityDate() {
		return validityDate;
	}

	public void setValidityDate(final Date validityDate) {
		this.validityDate = validityDate;
	}
}
