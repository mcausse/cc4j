package org.homs.cc4j.issue;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;

import static org.homs.cc4j.issue.IssueSeverity.*;

public class SimpleIssuesReportVisitor implements IssuesReportVisitor {

    @Override
    public void visit(IssuesReport issuesReport, PrintStream ps) {

        var issues = new ArrayList<>(issuesReport.getIssues());
        Collections.sort(issues);

        for (var e : issues) {
            ps.println(e);
        }
        ps.println();

        ps.println("-------------------------------------------------------------");
        ps.printf("%4d Errors(s) found.%n", issuesReport.getIssuesCountBySeverity(ERROR));
        ps.printf("%4d Critical(s) found.%n", issuesReport.getIssuesCountBySeverity(CRITICAL));
        ps.printf("%4d Warning(s) found.%n", issuesReport.getIssuesCountBySeverity(WARNING));
        ps.println("-------------------------------------------------------------");
    }
}