package org.homs.cc4j.issue;

import java.util.ArrayList;
import java.util.List;


public class IssuesReport {

    final List<Issue> issues = new ArrayList<>();

    public void registerIssue(IssueSeverity severity, Location location, String message) {
        issues.add(new Issue(location, severity, message));
    }

    public List<Issue> getIssues() {
        return issues;
    }

    public void acceptReportVisitor(IssuesReportVisitor issuesVisitor) {
        issuesVisitor.visit(this, System.out);
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
