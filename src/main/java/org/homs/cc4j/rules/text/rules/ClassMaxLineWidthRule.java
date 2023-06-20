package org.homs.cc4j.rules.text.rules;

import org.homs.cc4j.RuleInfo;
import org.homs.cc4j.issue.IssueSeverity;
import org.homs.cc4j.issue.IssuesReport;
import org.homs.cc4j.issue.Location;
import org.homs.cc4j.issue.Thresholds;
import org.homs.cc4j.rules.text.TextRule;

import static org.homs.cc4j.issue.IssueSeverity.getIssueSeverity;

public class ClassMaxLineWidthRule implements TextRule {

    @Override
    public RuleInfo getRuleInfo() {
        return new RuleInfo("cc", 8, "Avoid writing long lines. Avoid spaghetti lines.");
    }

    @Override
    public void execute(IssuesReport issuesReport, String javaFileName, String sourceCode) {
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
                    "file has a line (line #%s) of %s columns width (>%s warning, >%s critical, >%s error)",
                    maxWidthNumLine + 1, maxWidth, thrs.warningThr, thrs.criticalThr, thrs.errorThr);
            issuesReport.registerIssue(new Location(javaFileName), severity, getRuleInfo(), msg);
        }
    }
}