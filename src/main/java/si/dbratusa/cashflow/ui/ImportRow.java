package si.dbratusa.cashflow.ui;

import java.time.Instant;

public record ImportRow(String importJobId, String planJobId, String fileName, String status, Instant createdAt) {}
