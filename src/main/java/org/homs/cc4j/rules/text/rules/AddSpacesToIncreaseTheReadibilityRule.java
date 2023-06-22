package org.homs.cc4j.rules.text.rules;

import org.homs.cc4j.RuleInfo;
import org.homs.cc4j.issue.IssuesReport;
import org.homs.cc4j.issue.Location;
import org.homs.cc4j.rules.text.TextRule;
import org.homs.cc4j.util.CodeCleaner;

import static org.homs.cc4j.issue.IssueSeverity.WARNING;
import static org.homs.cc4j.rules.text.FileRules.checkForRegexp;

public class AddSpacesToIncreaseTheReadibilityRule implements TextRule {

    @Override
    public RuleInfo getRuleInfo() {
        return new RuleInfo("co", 1, "Add spaces to increase the readability. Just format your code.");
    }

    @Override
    public void execute(IssuesReport issuesReport, String javaFileName, String sourceCode) {
        sourceCode = new CodeCleaner(true, true).cleanTheCode(sourceCode);
        String[] sourceLines = sourceCode.split("\\n");

        var location = new Location(javaFileName);
        for (int i = 0; i < sourceLines.length; i++) {
            location.push("line " + (i + 1));

            String line = sourceLines[i];

            checkLackOfFormatting(issuesReport, location, line);

            location.pop();
        }
    }

    void checkLackOfFormatting(IssuesReport issuesReport, Location location, String line) {
        int hits;

        hits = checkForRegexp(line, ",[^\\s]");
        if (hits > 0) {
            issuesReport.registerIssue(location, WARNING, getRuleInfo(),
                    String.format("%s (after ',') spaces pending to add to increase the readibility", hits));
        }
        hits = checkForRegexp(line, "[^\\s]\\&\\&[^\\s]");
        if (hits > 0) {
            issuesReport.registerIssue(location, WARNING, getRuleInfo(),
                    String.format("%s ('&&') spaces pending to add to increase the readibility", hits));
        }
        hits = checkForRegexp(line, "[^\\s]\\|\\|[^\\s]");
        if (hits > 0) {
            issuesReport.registerIssue(location, WARNING, getRuleInfo(),
                    String.format("%s ('||') spaces pending to add to increase the readibility", hits));
        }
        hits = checkForRegexp(line, "[^\\s]\\=+[^\\s]");
        if (hits > 0) {
            issuesReport.registerIssue(location, WARNING, getRuleInfo(),
                    String.format("%s ('=') spaces pending to add to increase the readibility", hits));
        }
        hits = checkForRegexp(line, "\\)\\{");
        if (hits > 0) {
            issuesReport.registerIssue(location, WARNING, getRuleInfo(),
                    String.format("%s ('){') spaces pending to add to increase the readibility", hits));
        }
    }
}
