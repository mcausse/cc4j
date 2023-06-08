package org.homs.cc4j;

import org.homs.cc4j.issue.IssueSeverity;
import org.homs.cc4j.issue.IssuesReport;
import org.homs.cc4j.issue.Location;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.homs.cc4j.issue.IssueSeverity.*;

public class FileRules {

    final IssuesReport issuesReport;

    public FileRules(IssuesReport issuesReport) {
        this.issuesReport = issuesReport;
    }

    private void registerIssue(Location location, String message, int currentMetric, int warningThr, int criticalThr, int errorThr) {
        var msg = String.format(message, currentMetric, warningThr, criticalThr, errorThr);

        IssueSeverity severity = null;
        if (currentMetric > errorThr) {
            severity = ERROR;
        } else if (currentMetric > criticalThr) {
            severity = CRITICAL;
        } else if (currentMetric > warningThr) {
            severity = WARNING;
        }

        if (severity != null) {
            issuesReport.registerIssue(severity, location, msg);
        }
    }

    public void checkTodosAndFixmes(String javaFileName, String sourceCode) {
        checkForRegexp(new Location(javaFileName), sourceCode, "TODO ", CRITICAL, "%s pending TODO(s) found");
        checkForRegexp(new Location(javaFileName), sourceCode, "FIXME ", CRITICAL, "%s pending FIXME(s) found");
        checkForRegexp(new Location(javaFileName), sourceCode, "@Deprecated", CRITICAL, "%s pending @Deprecated(s) found");
        checkForRegexp(new Location(javaFileName), sourceCode, "@Ignore[^(]", CRITICAL, "%s pending @Ignore(s) (without justification) found");
        checkForRegexp(new Location(javaFileName), sourceCode, "@Disabled[^(]", CRITICAL, "%s pending @Disabled(s) (without justification) found");
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

        registerIssue(new Location(javaFileName),
                "file has a line (line #" + (maxWidthNumLine + 1) + ") of %s columns width (>%s warning, >%s critical, >%s error)",
                maxWidth, 130, 160, 190);
    }

    public void checkClassMaxNumberOfLines(String javaFileName, String sourceCode) {
        int classLines = sourceCode.split("\\n").length;
        registerIssue(new Location(javaFileName), "file has a total of %s lines (>%s warning, >%s critical, >%s error)",
                classLines, 200, 350, 500);
    }

    void checkForRegexp(Location location, String sourceCode, String regexp, IssueSeverity severity, String message) {
        int hits = 0;
        Pattern p = Pattern.compile(regexp);
        Matcher m = p.matcher(sourceCode);
        while (m.find()) {
            hits++;
        }
        if (hits > 0) {
            issuesReport.registerIssue(severity, location, String.format(message, hits));
        }
    }

    public void checkForAddSpacesToIncreaseReadibility(String javaFileName, String sourceCode) {
        sourceCode = new CodeCleaner().cleanTheCode(sourceCode);
        String[] sourceLines = sourceCode.split("\\n");

        var location = new Location(javaFileName);
        for (int i = 0; i < sourceLines.length; i++) {
            location.push("line " + (i + 1));

            String line = sourceLines[i];
            checkForRegexp(location, line, ",[^\\s]", WARNING, "%s (after ',') spaces pending to add to increase the readibility");
            checkForRegexp(location, line, "[^\\s]\\&\\&[^\\s]", WARNING, "%s ('&&') spaces pending to add to increase the readibility");
            checkForRegexp(location, line, "[^\\s]\\|\\|[^\\s]", WARNING, "%s ('||') spaces pending to add to increase the readibility");
            checkForRegexp(location, line, "[^\\s]\\=+[^\\s]", WARNING, "%s ('=') spaces pending to add to increase the readibility");
            checkForRegexp(location, line, "\\)\\{", WARNING, "%s ('){') spaces pending to add to increase the readibility");

            location.pop();
        }
    }
}
