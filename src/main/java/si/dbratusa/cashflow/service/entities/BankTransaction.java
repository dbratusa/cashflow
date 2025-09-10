package si.dbratusa.cashflow.service.entities;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "BANK_TRANSACTION",
	indexes = {
		@Index(name = "IDX_TX_PLAN_DATE", columnList = "plan_id, bookingDate")
	})
public class BankTransaction extends PanacheEntity {

	@Column(nullable = false)
	public LocalDate bookingDate;

	@Column(precision = 19, scale = 4, nullable = false)
	public BigDecimal amount;

	@Column(length = 12)
	public String currency;

	@Column(length = 1024)
	public String description;

	@Column(length = 128)
	public String reference;

	@ManyToOne(optional = false)
	@JoinColumn(name = "plan_id")
	public BankStatementCsvParsePlan plan;
}
