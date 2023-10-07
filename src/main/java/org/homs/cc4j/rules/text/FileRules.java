package org.homs.cc4j.rules.text;

import org.homs.cc4j.rules.text.rules.*;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileRules {

    public final static List<TextRule> TEXT_RULES = Arrays.asList(
            new AddSpacesToIncreaseTheReadibilityRule(),
            new ClassMaxLineWidthRule(),
            new ClassMaxNumberOfLinesRule(),
            new PendingFixmesRule(),
            new PendingTodosRule(),
            new AvoidDeprecatedAnnotation(),
            new AvoidIgnoredTestsAnnotation()
    );

    // TODO això cap a un utils?
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
