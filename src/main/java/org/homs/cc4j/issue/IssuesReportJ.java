package org.homs.cc4j.issue;

import java.util.ArrayList;
import java.util.List;

import static org.homs.cc4j.issue.IssueSeverity.*;


public class IssuesReportJ {

    public class Issue {

        public final String location;
        public final IssueSeverity severity;
        public final String message;

        public Issue(String location, IssueSeverity severity, String message) {
            this.location = location;
            this.severity = severity;
            this.message = message;
        }

        public IssueSeverity getSeverity() {
            return severity;
        }

        @Override
        public String toString() {
            if (location.isEmpty()) {
                return String.format("%s %s", severity.getSymbol(), message);
            } else {
                return String.format("%s %s (at %s)", severity.getSymbol(), message, location);
            }
        }
    }

    final List<Issue> issues = new ArrayList<>();

    public void registerIssue(IssueSeverity severity, String location, String message) {
        issues.add(new Issue(location, severity, message));
    }

    public List<Issue> getIssues() {
        return issues;
    }

    //TODO passar PrintStream "System.out"
    public void displayReport() {

        for (var severity : IssueSeverity.values()) {
            for (var e : issues) {
                if (severity == e.getSeverity()) {
                    System.out.println(e);
                }
            }
        }
        System.out.println();

        System.out.println("-------------------------------------------------------------");
        System.out.printf("%4d Errors(s) found.%n", getIssuesCountBySeverity(ERROR));
        System.out.printf("%4d Critical(s) found.%n", getIssuesCountBySeverity(CRITICAL));
        System.out.printf("%4d Warning(s) found.%n", getIssuesCountBySeverity(WARNING));
        System.out.println("-------------------------------------------------------------");
    }


    public int getIssuesCountBySeverity(IssueSeverity severity) {
        int count = 0;
        for (var e : issues) {
            if (e.getSeverity() == severity) {
                count++;
            }
        }
        return count;
    }

}
