package org.homs.cc4j.rules.text;

import org.homs.cc4j.issue.IssuesReport;
import org.homs.cc4j.issue.Location;
import org.homs.cc4j.util.CodeCleaner;

public abstract class AbstractLineBasedTextRule implements TextRule {

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

    protected abstract void checkTechnicalDebtSmells(IssuesReport issuesReport, Location location, String line);
}
