package org.homs.cc4j;

import org.homs.cc4j.issue.IssueSeverity;
import org.homs.cc4j.issue.IssuesReport;
import org.homs.cc4j.issue.Location;
import org.homs.cc4j.util.CodeCleaner;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.homs.cc4j.issue.IssueSeverity.*;

public class FileRules {

    final IssuesReport issuesReport;

    public FileRules(IssuesReport issuesReport) {
        this.issuesReport = issuesReport;
    }

    static class Thresholds {
        public final int warningThr;
        public final int criticalThr;
        public final int errorThr;

        public Thresholds(int warningThr, int criticalThr, int errorThr) {
            this.warningThr = warningThr;
            this.criticalThr = criticalThr;
            this.errorThr = errorThr;
        }
    }

    private IssueSeverity getIssueSeverity(int currentMetric, Thresholds thresholds) {
        IssueSeverity severity = null;
        if (currentMetric > thresholds.errorThr) {
            severity = ERROR;
        } else if (currentMetric > thresholds.criticalThr) {
            severity = CRITICAL;
        } else if (currentMetric > thresholds.warningThr) {
            severity = WARNING;
        }
        return severity;
    }

    public void checkClassMaxLineWidth(String javaFileName, String sourceCode) {
        String[] classLines = sourceCode.split("\\n");
        int maxWidth = 0;
        int maxWidthNumLine = -1;
        for (int numLine = 0; numLine < classLines.length; numLine++) {
            String line = classLines[numLine];
            if (maxWidth < line.length()) {
                maxWidth = line.length();
                maxWidthNumLine = numLine;
            }
        }

        var thrs = new Thresholds(140, 160, 190);
        IssueSeverity severity = getIssueSeverity(maxWidth, thrs);
        if (severity != null) {
            var msg = String.format(
                    "file has a line (line #" + (maxWidthNumLine + 1) + ") of %s columns width (>%s warning, >%s critical, >%s error)",
                    maxWidth, thrs.warningThr, thrs.criticalThr, thrs.errorThr);
            issuesReport.registerIssue(severity, new Location(javaFileName), msg);
        }
    }

    public void checkClassMaxNumberOfLines(String javaFileName, String sourceCode) {
        int classLines = sourceCode.split("\\n").length;

        var thrs = new Thresholds(200, 350, 500);
        IssueSeverity severity = getIssueSeverity(classLines, thrs);
        if (severity != null) {
            var msg = String.format(
                    "file has a total of %s lines (>%s warning, >%s critical, >%s error)",
                    classLines, thrs.warningThr, thrs.criticalThr, thrs.errorThr);

            issuesReport.registerIssue(severity, new Location(javaFileName), msg);
        }
    }

    public void checkTodosAndFixmes(String javaFileName, String sourceCode) {

        sourceCode = new CodeCleaner(false, true).cleanTheCode(sourceCode);
        int hits;

        hits = checkForRegexp(sourceCode, "TODO ");
        hits += checkForRegexp(sourceCode, "TODO\\n");
        if (hits > 0) {
            issuesReport.registerIssue(CRITICAL, new Location(javaFileName), String.format("%s pending TODO(s) found", hits));
        }

        hits = checkForRegexp(sourceCode, "FIXME ");
        hits += checkForRegexp(sourceCode, "FIXME\\n");
        if (hits > 0) {
            issuesReport.registerIssue(CRITICAL, new Location(javaFileName), String.format("%s pending FIXME(s) found", hits));
        }
    }

    public void checkForAddSpacesToIncreaseReadibility(String javaFileName, String sourceCode) {
        sourceCode = new CodeCleaner(true, true).cleanTheCode(sourceCode);
        String[] sourceLines = sourceCode.split("\\n");

        var location = new Location(javaFileName);
        for (int i = 0; i < sourceLines.length; i++) {
            location.push("line " + (i + 1));

            String line = sourceLines[i];

            checkTechnicalDebtSmells(location, line);
            checkLackOfFormatting(location, line);

            location.pop();
        }
    }

    void checkLackOfFormatting(Location location, String line) {
        int hits;

        hits = checkForRegexp(line, ",[^\\s]");
        if (hits > 0) {
            issuesReport.registerIssue(WARNING, location,
                    String.format("%s (after ',') spaces pending to add to increase the readibility", hits));
        }
        hits = checkForRegexp(line, "[^\\s]\\&\\&[^\\s]");
        if (hits > 0) {
            issuesReport.registerIssue(WARNING, location,
                    String.format("%s ('&&') spaces pending to add to increase the readibility", hits));
        }
        hits = checkForRegexp(line, "[^\\s]\\|\\|[^\\s]");
        if (hits > 0) {
            issuesReport.registerIssue(WARNING, location,
                    String.format("%s ('||') spaces pending to add to increase the readibility", hits));
        }
        hits = checkForRegexp(line, "[^\\s]\\=+[^\\s]");
        if (hits > 0) {
            issuesReport.registerIssue(WARNING, location,
                    String.format("%s ('=') spaces pending to add to increase the readibility", hits));
        }
        hits = checkForRegexp(line, "\\)\\{");
        if (hits > 0) {
            issuesReport.registerIssue(WARNING, location,
                    String.format("%s ('){') spaces pending to add to increase the readibility", hits));
        }
    }

    void checkTechnicalDebtSmells(Location location, String line) {
        int hits;
        hits = checkForRegexp(line, "\\@Deprecated\\s*$");
        if (hits > 0) {
            issuesReport.registerIssue(CRITICAL, location,
                    String.format("%s pending @Deprecated(s) found", hits));
        }

        hits = checkForRegexp(line, "\\@Ignore\\s*$");
        if (hits > 0) {
            issuesReport.registerIssue(CRITICAL, location,
                    String.format("%s pending @Ignore(s) (without justification) found", hits));
        }
        hits = checkForRegexp(line, "\\@Disabled\\s*$");
        if (hits > 0) {
            issuesReport.registerIssue(CRITICAL, location,
                    String.format("%s pending @Disabled(s) (without justification) found", hits));
        }
    }

    int checkForRegexp(String sourceCode, String regexp) {
        int hits = 0;
        Pattern p = Pattern.compile(regexp, Pattern.MULTILINE);
        Matcher m = p.matcher(sourceCode);
        while (m.find()) {
            hits++;
        }
        return hits;
    }
}
