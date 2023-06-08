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

//    @Deprecated
//    public void displayReport() {
//        for (var severity : IssueSeverity.values()) {
//            for (var e : issues) {
//                if (severity == e.getSeverity()) {
//                    System.out.println(e);
//                }
//            }
//        }
//        System.out.println();
//
//        System.out.println("-------------------------------------------------------------");
//        System.out.printf("%4d Errors(s) found.%n", getIssuesCountBySeverity(ERROR));
//        System.out.printf("%4d Critical(s) found.%n", getIssuesCountBySeverity(CRITICAL));
//        System.out.printf("%4d Warning(s) found.%n", getIssuesCountBySeverity(WARNING));
//        System.out.println("-------------------------------------------------------------");
//    }

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
