package si.dbratusa.cashflow.ui;


import java.time.Instant;

public record PlanRow(String planId, String name, String fingerprint, String status, Instant updatedAt) {}
