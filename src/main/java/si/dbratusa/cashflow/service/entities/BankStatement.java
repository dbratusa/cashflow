package si.dbratusa.cashflow.service.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "BANK_STATEMENT")
public class BankStatement extends CashflowBaseEntity {
}
