package si.dbratusa.cashflow.service.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name = "CATEGORY",
	uniqueConstraints = @UniqueConstraint(name = "UK_CATEGORY_NAME", columnNames = "name")
)
public class Category extends CashflowBaseEntity {

	@Column(length = 128)
	public String name;
}
