package org.homs.cc4j.issue;

import org.homs.cc4j.RuleInfo;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

public class IssuesReport {

    final List<Issue> issues = new ArrayList<>();

    public void registerIssue(Location location, IssueSeverity severity, RuleInfo ruleInfo, String message) {
        issues.add(new Issue(location, severity, ruleInfo, message));
    }

    public List<Issue> getIssues() {
        return issues;
    }

    public void acceptReportVisitor(PrintStream ps, IssuesReportVisitor issuesVisitor) {
        issuesVisitor.visit(this, ps);
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
