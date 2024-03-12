package org.homs.cc4j;

import org.homs.cc4j.rules.visitors.rules.CognitiveComplexityTooHighRule;
import org.homs.cc4j.rules.visitors.rules.CyclomaticComplexityTooHighRule;
import org.homs.cc4j.rules.visitors.rules.HomsCognoCyclomaticComplexityTooHighRule;

import java.util.List;

public class ComplexityAssessmentCc4j {

    public static Cc4j buildCc4jForComplexityAssessment() {
        var complexityRules = List.of(
                new CyclomaticComplexityTooHighRule(),
                new CognitiveComplexityTooHighRule(),
                new HomsCognoCyclomaticComplexityTooHighRule()
        );

        return new Cc4j(complexityRules, List.of());
    }
}
