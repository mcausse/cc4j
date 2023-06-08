package org.homs.cc4j.issue;

import java.io.PrintStream;
import java.util.LinkedHashSet;
import java.util.Set;

import static org.homs.cc4j.issue.IssueSeverity.*;

public class PerClassIssuesReportVisitor implements IssuesReportVisitor {

    @Override
    public void visit(IssuesReport issuesReport, PrintStream ps) {
        Set<String> fileNames = new LinkedHashSet<>();
        for (var e : issuesReport.getIssues()) {
            fileNames.add(e.getLocation().getJavaFileName());
        }

        for (var fileName : fileNames) {
            ps.println("-------------------------------------------------------------");
            ps.println(fileName);
            ps.println();
            for (var severity : IssueSeverity.values()) {
                for (var e : issuesReport.getIssues()) {
                    if (severity == e.getSeverity() && fileName.equals(e.getLocation().getJavaFileName())) {
                        ps.println(e);
                    }
                }
            }
            ps.println();
        }

        ps.printf("%40s %4s %4s %4s%n", "", "*", "+", "-");
        ps.println("-------------------------------------------------------------");
        for (var fileName : fileNames) {
            ps.printf("%40s %4d %4d %4d%n",
                    fileName,
                    getIssuesCountBySeverity(issuesReport, fileName, ERROR),
                    getIssuesCountBySeverity(issuesReport, fileName, CRITICAL),
                    getIssuesCountBySeverity(issuesReport, fileName, WARNING));
        }
        ps.println();
    }

    public int getIssuesCountBySeverity(IssuesReport issuesReport, String fileName, IssueSeverity severity) {
        int count = 0;
        for (var e : issuesReport.getIssues()) {
            if (e.getSeverity() == severity && e.getLocation().getJavaFileName().equals(fileName)) {
                count++;
            }
        }
        return count;
    }
}