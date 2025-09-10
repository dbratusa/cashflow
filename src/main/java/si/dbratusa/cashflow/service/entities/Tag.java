package si.dbratusa.cashflow.service.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;


@Entity
@Table(name = "TAG",
	uniqueConstraints = @UniqueConstraint(name="UK_TAG_NAME", columnNames="name")
)
public class Tag extends CashflowBaseEntity {

	@Column(length = 64)
	public String name;
}
