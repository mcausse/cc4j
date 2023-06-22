package org.homs.cc4j.rules.text.rules;

import org.homs.cc4j.RuleInfo;
import org.homs.cc4j.issue.IssuesReport;
import org.homs.cc4j.issue.Location;
import org.homs.cc4j.rules.text.AbstractLineBasedTextRule;

import static org.homs.cc4j.issue.IssueSeverity.CRITICAL;
import static org.homs.cc4j.rules.text.FileRules.checkForRegexp;

public class AvoidIgnoredTestsAnnotation extends AbstractLineBasedTextRule {

    @Override
    public RuleInfo getRuleInfo() {
        return new RuleInfo("td", 4, "An ignored Unit Test is a postponed technical debt.");
    }

    protected void checkTechnicalDebtSmells(IssuesReport issuesReport, Location location, String line) {
        // TODO considerar @org.junit.Disabled
        int hits = checkForRegexp(line, "\\@Ignore\\s*$");
        if (hits > 0) {
            issuesReport.registerIssue(location, CRITICAL, getRuleInfo(),
                    String.format("%s pending @Ignore(s) (without justification) found", hits));
        }

        hits = checkForRegexp(line, "\\@Disabled\\s*$");
        if (hits > 0) {
            issuesReport.registerIssue(location, CRITICAL, getRuleInfo(),
                    String.format("%s pending @Disabled(s) (without justification) found", hits));
        }
    }
}