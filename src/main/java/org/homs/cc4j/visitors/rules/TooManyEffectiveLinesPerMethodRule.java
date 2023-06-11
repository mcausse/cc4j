package org.homs.cc4j.visitors.rules;

import com.sun.source.tree.MethodTree;
import org.homs.cc4j.visitors.RuleTreeVisitor;

public class TooManyEffectiveLinesPerMethodRule extends RuleTreeVisitor<Void> {

    static int THR_ERROR = 45;
    static int THR_CRITICAL = 35;
    static int THR_WARNING = 25;

    public Integer visitMethod(MethodTree node, Void p) {
        location.push(node.getName().toString() + "(..)");

        final int linesOfEffectiveCode;
        if (node.getBody() == null) {
            linesOfEffectiveCode = 0;
        } else {
            linesOfEffectiveCode = node.getBody().toString().split("\\n").length - 2 /* { & } */;
        }

        generateIssueIfThreshold("too many effective lines of code: %s (>%s warning, >%s critical, >%s error)",
                linesOfEffectiveCode,
                THR_WARNING, THR_CRITICAL, THR_ERROR);

        location.pop();
        return 0;
    }
}