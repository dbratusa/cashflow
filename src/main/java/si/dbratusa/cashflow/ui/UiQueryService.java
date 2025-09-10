package si.dbratusa.cashflow.ui;

import java.util.Date;
import java.util.List;

import jakarta.transaction.Transactional;
import si.dbratusa.cashflow.service.entities.BankStatementCsvParsePlan;
import si.dbratusa.cashflow.service.entities.BankStatementCsvParsePlanAlias;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class UiQueryService {

	@Transactional
    public List<AliasRow> listRecentAliases(int limit) {
        var aliases = BankStatementCsvParsePlanAlias
                .find("order by id desc")
                .page(0, limit)
                .list();

        return aliases.stream().map(a -> {
			BankStatementCsvParsePlanAlias alias = (BankStatementCsvParsePlanAlias) a;
			BankStatementCsvParsePlan plan = alias.plan;
            return new AliasRow(
				alias.id,
				alias.alias,
				plan.id,
                plan.getHeaderFingerprint(),
				new Date().toInstant()
            );
        }).toList();
    }

	@Transactional
    public boolean aliasExists(String alias) {
        return BankStatementCsvParsePlanAlias.find("alias", alias)
                .firstResultOptional()
                .isPresent();
    }
}
