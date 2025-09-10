package si.dbratusa.cashflow.service.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "BANK_ACCOUNT")
public class BankAccount extends CashflowBaseEntity {

	public String name;

	@Column(length = 34)
	public String iban;

	@Column(length = 3)
	public String currency;
}
