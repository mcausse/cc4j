package org.homs.cc4j.rules.text.rules;

import org.homs.cc4j.RuleInfo;
import org.homs.cc4j.issue.IssueSeverity;
import org.homs.cc4j.issue.IssuesReport;
import org.homs.cc4j.issue.Location;
import org.homs.cc4j.issue.Thresholds;
import org.homs.cc4j.rules.text.TextRule;

import static org.homs.cc4j.issue.IssueSeverity.getIssueSeverity;

public class ClassMaxNumberOfLinesRule implements TextRule {

    static final Thresholds THRESHOLDS = new Thresholds(200, 350, 500);

    @Override
    public RuleInfo getRuleInfo() {
        return new RuleInfo("cc", 9, "Classes should be small. (" + THRESHOLDS + ")");
    }

    @Override
    public void execute(IssuesReport issuesReport, String javaFileName, String sourceCode) {
        int classLines = sourceCode.split("\\n").length;

        IssueSeverity severity = getIssueSeverity(classLines, THRESHOLDS);
        if (severity != null) {
            var msg = String.format("file has a total of %s lines (%s)", classLines, THRESHOLDS);
            issuesReport.registerIssue(new Location(javaFileName), severity, getRuleInfo(), msg);
        }
    }
}
