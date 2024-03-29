package org.homs.cc4j.issue;

public enum IssueSeverity {

    ERROR("error", "*", 1),
    CRITICAL("critical", "+", 2),
    WARNING("warning", "-", 3);

    final String name;
    final String symbol;
    final int order;

    IssueSeverity(String name, String symbol, int order) {
        this.name = name;
        this.symbol = symbol;
        this.order = order;
    }

    public String getName() {
        return name;
    }

    public String getSymbol() {
        return symbol;
    }

    public int getOrder() {
        return order;
    }

    public static IssueSeverity getIssueSeverity(int currentMetric, Thresholds thresholds) {
        IssueSeverity severity = null;
        if (currentMetric > thresholds.errorThr) {
            severity = ERROR;
        } else if (currentMetric > thresholds.criticalThr) {
            severity = CRITICAL;
        } else if (currentMetric > thresholds.warningThr) {
            severity = WARNING;
        }
        return severity;
    }
}