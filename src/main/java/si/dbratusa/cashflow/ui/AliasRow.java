package si.dbratusa.cashflow.ui;

import java.time.Instant;

public record AliasRow(
	Long aliasId,
	String alias,             // filename
	Long planId,
	String headerFingerprint,
	Instant updatedAt
) {}
