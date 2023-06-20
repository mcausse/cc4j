package org.homs.cc4j.text.rules;

import org.homs.cc4j.issue.IssuesReport;
import org.homs.cc4j.issue.Location;
import org.homs.cc4j.text.TextRule;
import org.homs.cc4j.util.CodeCleaner;

import static org.homs.cc4j.issue.IssueSeverity.CRITICAL;
import static org.homs.cc4j.text.FileRules.checkForRegexp;

public class PendingFixmesRule implements TextRule {

    @Override
    public String getRuleId() {
        return "td01";
    }

    @Override
    public void execute(IssuesReport issuesReport, String javaFileName, String sourceCode) {
        sourceCode = new CodeCleaner(false, true).cleanTheCode(sourceCode);
        int hits;

        hits = checkForRegexp(sourceCode, "FIXME ");
        hits += checkForRegexp(sourceCode, "FIXME\\n");
        if (hits > 0) {
            issuesReport.registerIssue(new Location(javaFileName), CRITICAL, getRuleId(), String.format("%s pending FIXME(s) found", hits));
        }
    }
}