package org.homs.cc4j.issue;

import java.io.PrintStream;
import java.util.*;

public class PerClassIssuesReportVisitor implements IssuesReportVisitor {

    @Override
    public void visit(IssuesReport issuesReport, PrintStream ps) {
        Set<String> fileNames = new LinkedHashSet<>();
        for (var e : issuesReport.getIssues()) {
            fileNames.add(e.getLocation().getJavaFileName());
        }

        var issues = new ArrayList<>(issuesReport.getIssues());
        Collections.sort(issues);

        reportByFileDetailed(issues, ps, fileNames);
    }

    protected void reportByFileDetailed(List<Issue> issues, PrintStream ps, Set<String> fileNames) {
        for (var fileName : fileNames) {
            reportFileDetail(issues, ps, fileName);
        }
    }

    protected void reportFileDetail(List<Issue> issues, PrintStream ps, String fileName) {
        ps.println("-------------------------------------------------------------");
        ps.println(fileName);
        ps.println();
        for (var e : issues) {
            if (fileName.equals(e.getLocation().getJavaFileName())) {
                ps.println(e);
            }
        }
        ps.println();
    }

    public int getIssuesCountBySeverity(List<Issue> issues, String fileName, IssueSeverity severity) {
        int count = 0;
        for (var e : issues) {
            if (e.getSeverity() == severity && e.getLocation().getJavaFileName().equals(fileName)) {
                count++;
            }
        }
        return count;
    }
}