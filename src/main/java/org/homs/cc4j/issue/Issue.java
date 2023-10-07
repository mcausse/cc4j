package org.homs.cc4j.issue;

import org.homs.cc4j.RuleInfo;

public class Issue implements Comparable<Issue> {

    public final Location location;
    public final IssueSeverity severity;
    public final RuleInfo ruleInfo;
    public final String message;

    public Issue(Location location, IssueSeverity severity, RuleInfo ruleInfo, String message) {
        this.location = new Location(location);
        this.severity = severity;
        this.ruleInfo = ruleInfo;
        this.message = message;
    }

    public IssueSeverity getSeverity() {
        return severity;
    }

    public Location getLocation() {
        return location;
    }

    @Override
    public int compareTo(Issue o) {
        return Integer.compare(severity.getOrder(), o.getSeverity().getOrder());
    }

    @Override
    public String toString() {
        return String.format("%s [%s] %s (at %s)", severity.getSymbol(), ruleInfo, message, location);
    }
}