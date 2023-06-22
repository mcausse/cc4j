package org.homs.cc4j.rules.text.rules;

import org.homs.cc4j.RuleInfo;
import org.homs.cc4j.issue.IssuesReport;
import org.homs.cc4j.issue.Location;
import org.homs.cc4j.rules.text.TextRule;
import org.homs.cc4j.util.CodeCleaner;

import static org.homs.cc4j.issue.IssueSeverity.CRITICAL;
import static org.homs.cc4j.rules.text.FileRules.checkForRegexp;

public class PendingFixmesRule implements TextRule {

    @Override
    public RuleInfo getRuleInfo() {
        return new RuleInfo("td", 1, "A pending FIXME is a postponed technical debt.");
    }

    @Override
    public void execute(IssuesReport issuesReport, String javaFileName, String sourceCode) {
        sourceCode = new CodeCleaner(false, true).cleanTheCode(sourceCode);
        int hits;

        hits = checkForRegexp(sourceCode, "FIXME ");
        hits += checkForRegexp(sourceCode, "FIXME\\n");
        hits += checkForRegexp(sourceCode, "FIXME\\:");
        if (hits > 0) {
            issuesReport.registerIssue(new Location(javaFileName), CRITICAL, getRuleInfo(), String.format("%s pending FIXME(s) found", hits));
        }
    }
}