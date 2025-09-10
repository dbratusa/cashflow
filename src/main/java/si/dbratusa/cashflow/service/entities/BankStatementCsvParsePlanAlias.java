package si.dbratusa.cashflow.service.entities;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name = "BANK_STATEMENT_CSV_PARSE_PLAN_ALIAS", uniqueConstraints = {
	@UniqueConstraint(name = "UK_PLAN_ALIAS_NAME", columnNames = "alias")
})
public class BankStatementCsvParsePlanAlias extends PanacheEntity {

	@Column(nullable = false, length = 128)
	public String alias;

	@ManyToOne(optional = false)
	@JoinColumn(name = "plan_id")
	public BankStatementCsvParsePlan plan;
}
