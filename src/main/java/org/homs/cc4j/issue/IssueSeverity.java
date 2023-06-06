package org.homs.cc4j.issue;

public enum IssueSeverity {

    ERROR("error", "*"),
    CRITICAL("critical", "+"),
    WARNING("warning", "-");

    final String name;
    final String symbol;

    IssueSeverity(String name, String symbol) {
        this.name = name;
        this.symbol = symbol;
    }

    public String getName() {
        return name;
    }

    public String getSymbol() {
        return symbol;
    }
}