package org.homs.cc4j;

import org.homs.cc4j.issue.IssueSeverity;
import org.homs.cc4j.issue.IssuesReportJ;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.homs.cc4j.issue.IssueSeverity.*;

public class Rules {

    static final String CONSTANT_PATTERN = "^[A-Z][A-Z0-9]*(_[A-Z0-9]+)*$";
    static final String VARIABLE_NAME_PATTERN = "^[a-z][a-zA-Z0-9]*$";
    static final String METHOD_NAME_PATTERN = "^[a-z][a-zA-Z0-9]*$";
    static final String CLASS_NAME_PATTERN = "^[A-Z][a-zA-Z0-9]*$";

    final IssuesReportJ issuesReport;

    public Rules(IssuesReportJ issuesReport) {
        this.issuesReport = issuesReport;
    }

    public IssuesReportJ getIssuesReport() {
        return issuesReport;
    }

    private void registerIssue(Location location, String message, int currentMetric, int warningThr, int criticalThr, int errorThr) {
        var msg = String.format(message, currentMetric, warningThr, criticalThr, errorThr);

        IssueSeverity severity = null;
        if (currentMetric > errorThr) {
            severity = ERROR;
        } else if (currentMetric > criticalThr) {
            severity = CRITICAL;
        } else if (currentMetric > warningThr) {
            severity = WARNING;
        }

        if (severity != null) {
            issuesReport.registerIssue(severity, location.toString(), msg);
        }
    }

    public void checkConstantNameConvention(Location location, String propertyName) {
        validateNamingConvention(location, CONSTANT_PATTERN, "constant", propertyName);
    }

    public void checkPropertyNameConvention(Location location, String propertyName) {
        validateNamingConvention(location, VARIABLE_NAME_PATTERN, "property", propertyName);
    }

    public void checkClassNameConvention(Location location, String className, String s) {
        validateNamingConvention(location, CLASS_NAME_PATTERN, s, className);
    }

    public void checkMethodNameConvention(Location location, String methodName) {
//        if (methodName.equals("<init>")) {
//            return;
//        }
        validateNamingConvention(location, METHOD_NAME_PATTERN, "method", methodName);
    }

    public void checkArgumentNameConvention(Location location, String argumentName) {
        validateNamingConvention(location, VARIABLE_NAME_PATTERN, "argument", argumentName);
    }

    public void checkTodosAndFixmes(String javaFileName, String sourceCode) {
        checkForRegexp(sourceCode, javaFileName, "TODO ");
        checkForRegexp(sourceCode, javaFileName, "FIXME ");
    }

    public void checkClassMaxLineWidth(String javaFileName, String sourceCode) {
        String[] classLines = sourceCode.split("\\n");
        int maxWidth = 0;
        int maxWidthNumLine = -1;
        for (int numLine = 0; numLine < classLines.length; numLine++) {
            String line = classLines[numLine];
            if (maxWidth < line.length()) {
                maxWidth = line.length();
                maxWidthNumLine = numLine;
            }
        }

        registerIssue(new Location(javaFileName),
                "class has a line (line #" + (maxWidthNumLine + 1) + ") of %s columns width (>%s warning, >%s critical, >%s error)",
                maxWidth, 120, 160, 180);
    }

//    public void checkTooManyMethodsPerClass(Location location, int methodsCount) {
//        registerIssue(location, "too many methods in a class: %s (>%s warning, >%s critical, >%s error)",
//                methodsCount, 15, 25, 30);
//    }

    public void checkTooManyPropertiesPerClass(Location location, int methodsCount) {
        registerIssue(location, "too many properties in a class: %s (>%s warning, >%s critical, >%s error)",
                methodsCount, 15, 25, 30);
    }

    public void checkTooManyEffectiveLinesPerMethod(Location location, int linesOfEffectiveCode) {
        registerIssue(location, "too many effective lines of code: %s (>%s warning, >%s critical, >%s error)",
                linesOfEffectiveCode, 25, 35, 45);
    }

//    public void checkTooManyMethodArguments(Location location, int argumentsCount) {
//        registerIssue(location, "too many arguments: %s (>%s warning, >%s critical, >%s error)",
//                argumentsCount, 3, 4, 5);
//    }

    public void checkTooDeplyNestedCode(Location location, int indentMaxLevel) {
        registerIssue(location, "%s indent levels (>%s warning, >%s critical, >%s error)",
                indentMaxLevel, 3, 4, 5);
    }


    public void checkClassMaxNumberOfLines(String javaFileName, String sourceCode) {
        int classLines = sourceCode.split("\\n").length;
        registerIssue(new Location(javaFileName), "class has a total of %s lines (>%s warning, >%s critical, >%s error)",
                classLines, 200, 350, 500);
    }

    void checkForRegexp(String sourceCode, String filename, String regexp) {
        int hits = 0;
        Pattern p = Pattern.compile(regexp);
        Matcher m = p.matcher(sourceCode);
        while (m.find()) {
            hits++;
        }
        if (hits > 0) {
            issuesReport.registerIssue(CRITICAL, filename, String.format("%s pending %s(s) found", hits, regexp.trim()));
        }
    }

    void validateNamingConvention(Location location, String regexp, String elementName, String name) {
        if (!name.matches(regexp)) {
            this.issuesReport.registerIssue(CRITICAL, location.toString(),
                    String.format("%s name should comply with a naming convention: %s", elementName, name));
        }
    }

    // LOGGER(1), STATIC(2), PROPERTY(3), CTOR(4), METHOD(5), EQUALS_HASHCODE(6), TOSTRING(7);
    public void checkClassMembersOrdering(Location location, List<Cc4j.Member> r) {
        var s = new StringBuilder();
        for (var m : r) {
            s.append(m.getOrder());
        }

        var pattern = "^1?2*3*4*5*6*7*$";
        if (!s.toString().matches(pattern)) {
            this.issuesReport.registerIssue(CRITICAL, location.toString(),
                    String.format("class members should be ordered as the convention: %s (pattern is: %s)", s, pattern));
        }
    }

    public void pronounceableAnalysis(Location location, String name) {
        int consonants = consonantAnalysis(name);
        registerIssue(location, "use pronounceable names: '" + name + "' scored with %s (>%s warning, >%s critical, >%s error)",
                consonants, 4, 5, 6);
    }

    int consonantAnalysis(String name) {
        String vowels = "aeiouAEIOUwyWY_";

        int maxRun = 0;
        int run = 0;
        char last = 'a';
        for (char c : name.toCharArray()) {
            if (vowels.indexOf(c) >= 0) {
                run = 0;
            } else if (Character.isLowerCase(last) != Character.isLowerCase(c)) {
                run = 1;
            } else {
                run++;
            }

            if (maxRun < run) {
                maxRun = run;
            }

            last = c;
        }
        return maxRun;
    }
}
