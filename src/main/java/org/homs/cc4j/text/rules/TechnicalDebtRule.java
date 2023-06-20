package org.homs.cc4j.text.rules;

import org.homs.cc4j.RuleInfo;
import org.homs.cc4j.issue.IssuesReport;
import org.homs.cc4j.issue.Location;
import org.homs.cc4j.text.TextRule;
import org.homs.cc4j.util.CodeCleaner;

import static org.homs.cc4j.issue.IssueSeverity.CRITICAL;
import static org.homs.cc4j.text.FileRules.checkForRegexp;

public class TechnicalDebtRule implements TextRule {

    @Override
    public RuleInfo getRuleInfo() {
        return new RuleInfo("td", 3, "Avoid pospone technical debt.");
//        return "td03";
    }

    @Override
    public void execute(IssuesReport issuesReport, String javaFileName, String sourceCode) {
        sourceCode = new CodeCleaner(true, true).cleanTheCode(sourceCode);
        String[] sourceLines = sourceCode.split("\\n");

        var location = new Location(javaFileName);
        for (int i = 0; i < sourceLines.length; i++) {
            location.push("line " + (i + 1));

            String line = sourceLines[i];

            checkTechnicalDebtSmells(issuesReport, location, line);

            location.pop();
        }
    }

    void checkTechnicalDebtSmells(IssuesReport issuesReport, Location location, String line) {
        int hits;
        hits = checkForRegexp(line, "\\@Deprecated\\s*$");
        if (hits > 0) {
            issuesReport.registerIssue(location, CRITICAL, getRuleInfo(),
                    String.format("%s pending @Deprecated(s) found", hits));
        }

        hits = checkForRegexp(line, "\\@Ignore\\s*$");
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