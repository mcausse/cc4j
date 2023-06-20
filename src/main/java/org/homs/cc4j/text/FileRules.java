package org.homs.cc4j.text;

import org.homs.cc4j.issue.IssuesReport;
import org.homs.cc4j.text.rules.*;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileRules {

    final TextRule[] textRules = {
            new AddSpacesToIncreaseTheReadibilityRule(),
            new ClassMaxLineWidthRule(),
            new ClassMaxNumberOfLinesRule(),
            new PendingFixmesRule(),
            new PendingTodosRule(),
            new TechnicalDebtRule()
    };

    final IssuesReport issuesReport;

    public FileRules(IssuesReport issuesReport) {
        this.issuesReport = issuesReport;
    }

    public void check(String javaFilename, String sourceCode) {
        for (var textRule : textRules) {
            textRule.execute(issuesReport, javaFilename, sourceCode);
        }
    }

    public static int checkForRegexp(String sourceCode, String regexp) {
        int hits = 0;
        Pattern p = Pattern.compile(regexp, Pattern.MULTILINE);
        Matcher m = p.matcher(sourceCode);
        while (m.find()) {
            hits++;
        }
        return hits;
    }
}
