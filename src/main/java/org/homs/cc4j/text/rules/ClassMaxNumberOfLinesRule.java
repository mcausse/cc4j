package org.homs.cc4j.text.rules;

import org.homs.cc4j.RuleInfo;
import org.homs.cc4j.issue.IssueSeverity;
import org.homs.cc4j.issue.IssuesReport;
import org.homs.cc4j.issue.Location;
import org.homs.cc4j.issue.Thresholds;
import org.homs.cc4j.text.TextRule;

import static org.homs.cc4j.issue.IssueSeverity.getIssueSeverity;

public class ClassMaxNumberOfLinesRule implements TextRule {

    @Override
    public RuleInfo getRuleInfo() {
        return new RuleInfo("cc", 9, "Classes should be small.");
    }

    @Override
    public void execute(IssuesReport issuesReport, String javaFileName, String sourceCode) {
        int classLines = sourceCode.split("\\n").length;

        var thrs = new Thresholds(200, 350, 500);
        IssueSeverity severity = getIssueSeverity(classLines, thrs);
        if (severity != null) {
            var msg = String.format(
                    "file has a total of %s lines (>%s warning, >%s critical, >%s error)",
                    classLines, thrs.warningThr, thrs.criticalThr, thrs.errorThr);

            issuesReport.registerIssue(new Location(javaFileName), severity, getRuleInfo(), msg);
        }
    }
}
