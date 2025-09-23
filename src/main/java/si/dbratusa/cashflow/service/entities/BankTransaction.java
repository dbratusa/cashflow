package si.dbratusa.cashflow.service.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(
	name = "BANK_TRANSACTION",
	indexes = {
		@Index(name = "IDX_TX_PLAN_DATE", columnList = "plan_id, bookingDate")
	}
)
public class BankTransaction extends CashflowBaseEntity {

	@Column(nullable = false)
	public LocalDate bookingDate;

	@Column(precision = 19, scale = 2, nullable = false)
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

	@ManyToOne
	@JoinColumn(name="account_id")
	public BankAccount account;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "statement_id", foreignKey = @ForeignKey(name="fk_tx_statement"))
	public BankStatement statement;

	@ManyToOne @JoinColumn(name="category_id")
	public Category category;

	@ManyToOne @JoinColumn(name="merchant_id")
	public Merchant merchant;

	@ManyToMany
	@JoinTable(name="bank_tx_tag",
		joinColumns = @JoinColumn(name="tx_id"),
		inverseJoinColumns = @JoinColumn(name="tag_id"))
	public List<Tag> tags = new ArrayList<>();
}
