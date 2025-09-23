package si.dbratusa.cashflow.service.entities;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "BANK_STATEMENT")
public class BankStatement extends CashflowBaseEntity {

	@Column
	public String fileName;

	@OrderBy("bookingDate ASC, id ASC")
	@OneToMany(mappedBy = "statement", cascade = CascadeType.ALL, orphanRemoval = true)
	public List<BankTransaction> transactions = new ArrayList<>();

	@Transient
	public void addTransaction(BankTransaction transaction) {
		this.transactions.add(transaction);
	}
}
