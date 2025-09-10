package si.dbratusa.cashflow.ui;

import java.time.Instant;
import java.util.UUID;

public record AliasRow(
	UUID aliasId,
	String alias,             // filename
	UUID planId,
	String headerFingerprint,
	Instant updatedAt
) {}
