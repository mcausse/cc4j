package org.homs.cc4j.rules.visitors;

import org.homs.cc4j.rules.visitors.rules.CognitiveComplexityTooHighRule;
import org.homs.cc4j.rules.visitors.rules.CyclomaticComplexityTooHighRule;
import org.homs.cc4j.rules.visitors.rules.HomsCognoCyclomaticComplexityTooHighRule;
import org.homs.cc4j.rules.visitors.rules.cc.*;
import org.homs.cc4j.rules.visitors.rules.co.ClassMembersOrderingRule;
import org.homs.cc4j.rules.visitors.rules.co.NamingConventionsRule;

import java.util.List;

public class AstRules {

    public static final List<RuleTreeVisitor<?>> RULES = List.of(
            new CyclomaticComplexityTooHighRule(),
            new CognitiveComplexityTooHighRule(),
//            new MartinCognitiveComplexityRule(),
            new HomsCognoCyclomaticComplexityTooHighRule(),

            new MaxIndentLevelRule2(),
            new TooManyArgumentsRule(),
            new TooComplicatedConditionRule(),
            new ClassMembersOrderingRule(),
            new NamingConventionsRule(),
            new TooManyEffectiveLinesPerMethodRule(),
            new TooManyMethodsRule(),
            new AvoidOptionalArgumentsRule(),
            new UsePronounceableNamesRule()
    );
}
