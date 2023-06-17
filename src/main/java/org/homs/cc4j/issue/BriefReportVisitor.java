package org.homs.cc4j.issue;

import java.io.PrintStream;
import java.util.*;

import static org.homs.cc4j.issue.IssueSeverity.*;

public class BriefReportVisitor implements IssuesReportVisitor {

    @Override
    public void visit(IssuesReport issuesReport, PrintStream ps) {
        Set<String> fileNames = new LinkedHashSet<>();
        for (var e : issuesReport.getIssues()) {
            fileNames.add(e.getLocation().getJavaFileName());
        }

        var issues = new ArrayList<>(issuesReport.getIssues());
        Collections.sort(issues);

        reportByFileBrief(issues, ps, fileNames);
    }

    protected void reportByFileBrief(List<Issue> issues, PrintStream ps, Set<String> fileNames) {
        ps.printf("%4s %4s %4s %s%n", "*", "+", "-", "");
        ps.println("-------------------------------------------------------------");
        //int total = 0;
        for (var fileName : fileNames) {
            int errors = getIssuesCountBySeverity(issues, fileName, ERROR);
            int criticals = getIssuesCountBySeverity(issues, fileName, CRITICAL);
            int warnings = getIssuesCountBySeverity(issues, fileName, WARNING);
            //total += errors * 3 + criticals * 2 + warnings;
            ps.printf("%4s %4s %4s %s%n",
                    errors == 0 ? "" : errors,
                    criticals == 0 ? "" : criticals,
                    warnings == 0 ? "" : warnings,
                    fileName);
        }
//        ps.println("=> " + total); // TODO
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