package si.dbratusa.cashflow.service.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name = "MERCHANT",
	uniqueConstraints = @UniqueConstraint(name = "UK_MERCHANT_NAME", columnNames = "displayName")
)
public class Merchant extends CashflowBaseEntity {

	@Column
	public String displayName;

	@Column(length = 34)
	public String iban;

	@Column(length = 2)
	public String country;

	@Column(length = 128)
	public String normalizedName;
}
