package org.homs.cc4j.issue;

public class Issue implements Comparable<Issue> {

    public final Location location;
    public final IssueSeverity severity;
    public final String message;

    public Issue(Location location, IssueSeverity severity, String message) {
        this.location = new Location(location);
        this.severity = severity;
        this.message = message;
    }

    public IssueSeverity getSeverity() {
        return severity;
    }

    public Location getLocation() {
        return location;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public int compareTo(Issue o) {
        return Integer.compare(severity.getOrder(), o.getSeverity().getOrder());
    }

    @Override
    public String toString() {
        return String.format("%s %s (at %s)", severity.getSymbol(), message, location);
    }
}