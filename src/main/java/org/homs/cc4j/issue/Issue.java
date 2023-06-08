package org.homs.cc4j.issue;

public class Issue {

    public final Location location;
    public final IssueSeverity severity;
    public final String message;

    // TODO + categoria [CleanCode], [Conventions]
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
    public String toString() {
//        if (location.isEmpty()) {
//            return String.format("%s %s", severity.getSymbol(), message);
//        } else {
        return String.format("%s %s (at %s)", severity.getSymbol(), message, location);
//        }
    }
}