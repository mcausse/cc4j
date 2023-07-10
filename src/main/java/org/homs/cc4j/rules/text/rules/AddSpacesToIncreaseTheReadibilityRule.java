package org.homs.cc4j.rules.text.rules;

import org.homs.cc4j.RuleInfo;
import org.homs.cc4j.issue.IssueSeverity;
import org.homs.cc4j.issue.IssuesReport;
import org.homs.cc4j.issue.Location;
import org.homs.cc4j.rules.text.TextRule;
import org.homs.cc4j.util.CodeCleaner;

import static org.homs.cc4j.issue.IssueSeverity.WARNING;
import static org.homs.cc4j.rules.text.FileRules.checkForRegexp;

public class AddSpacesToIncreaseTheReadibilityRule implements TextRule {

    static final IssueSeverity SEVERITY = WARNING;

    @Override
    public RuleInfo getRuleInfo() {
        return new RuleInfo("co", 1, "Add spaces to increase the readability. Just format your code. (" + SEVERITY.getName() + ")");
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
        addSpacesAfterComma(issuesReport, location, line);
        addSpacesToSeparateAndOr(issuesReport, location, line);
        addSpacesToSeparateAnAssignment(issuesReport, location, line);
        addSpaceAfterIfOrWhile(issuesReport, location, line);
    }

    void addSpacesAfterComma(IssuesReport issuesReport, Location location, String line) {
        int hits = checkForRegexp(line, ",[^\\s]");
        if (hits > 0) {
            issuesReport.registerIssue(location, SEVERITY, getRuleInfo(),
                    String.format("%s (after ',') spaces pending to add to increase the readibility", hits));
        }
    }

    void addSpacesToSeparateAnAssignment(IssuesReport issuesReport, Location location, String line) {
        int hits;
        hits = checkForRegexp(line, "[^\\s]\\=[^\\s]");
        if (hits > 0) {
            issuesReport.registerIssue(location, SEVERITY, getRuleInfo(),
                    String.format("%s ('=') spaces pending to add to increase the readibility", hits));
        }
    }

    void addSpacesToSeparateAndOr(IssuesReport issuesReport, Location location, String line) {
        int hits;
        hits = checkForRegexp(line, "[^\\s]\\&\\&[^\\s]");
        if (hits > 0) {
            issuesReport.registerIssue(location, SEVERITY, getRuleInfo(),
                    String.format("%s ('&&') spaces pending to add to increase the readibility", hits));
        }
        hits = checkForRegexp(line, "[^\\s]\\|\\|[^\\s]");
        if (hits > 0) {
            issuesReport.registerIssue(location, SEVERITY, getRuleInfo(),
                    String.format("%s ('||') spaces pending to add to increase the readibility", hits));
        }
    }

    void addSpaceAfterIfOrWhile(IssuesReport issuesReport, Location location, String line) {
        int hits = checkForRegexp(line, "\\sif\\(");
        if (hits > 0) {
            issuesReport.registerIssue(location, SEVERITY, getRuleInfo(),
                    String.format("%s ('if(') spaces pending to add to increase the readibility", hits));
        }
        hits = checkForRegexp(line, "\\swhile\\(");
        if (hits > 0) {
            issuesReport.registerIssue(location, SEVERITY, getRuleInfo(),
                    String.format("%s ('while(') spaces pending to add to increase the readibility", hits));
        }
        hits = checkForRegexp(line, "\\)\\{");
        if (hits > 0) {
            issuesReport.registerIssue(location, SEVERITY, getRuleInfo(),
                    String.format("%s ('){') spaces pending to add to increase the readibility", hits));
        }
    }
}
