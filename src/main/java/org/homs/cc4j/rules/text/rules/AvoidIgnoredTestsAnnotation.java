package org.homs.cc4j.rules.text.rules;

import org.homs.cc4j.RuleInfo;
import org.homs.cc4j.issue.IssueSeverity;
import org.homs.cc4j.issue.IssuesReport;
import org.homs.cc4j.issue.Location;
import org.homs.cc4j.rules.text.AbstractLineBasedTextRule;

import static org.homs.cc4j.issue.IssueSeverity.CRITICAL;
import static org.homs.cc4j.rules.text.FileRules.checkForRegexp;

public class AvoidIgnoredTestsAnnotation extends AbstractLineBasedTextRule {

    static final IssueSeverity SEVERITY = CRITICAL;

    @Override
    public RuleInfo getRuleInfo() {
        return new RuleInfo("td", 4, "An @Ignored/@Disabled Unit Test is a postponed technical debt. (" + SEVERITY.getName() + ")");
    }

    protected void checkTechnicalDebtSmells(IssuesReport issuesReport, Location location, String line) {
        int hits = checkForRegexp(line, "\\@Ignore([^(]|$)");
        hits += checkForRegexp(line, "\\@org\\.junit\\.Ignore([^(]|$)");
        if (hits > 0) {
            issuesReport.registerIssue(location, SEVERITY, getRuleInfo(),
                    String.format("%s pending @Ignore(s) (without justification) found", hits));
        }

        hits = checkForRegexp(line, "\\@Disabled([^(]|$)");
        hits += checkForRegexp(line, "\\@org\\.junit\\.jupiter\\.api\\.Disabled([^(]|$)");
        if (hits > 0) {
            issuesReport.registerIssue(location, SEVERITY, getRuleInfo(),
                    String.format("%s pending @Disabled(s) (without justification) found", hits));
        }
    }
}