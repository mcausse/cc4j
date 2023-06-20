package org.homs.cc4j.visitors.rules.cc;

import com.sun.source.tree.MethodTree;
import org.homs.cc4j.RuleInfo;
import org.homs.cc4j.visitors.RuleTreeVisitor;

public class TooManyEffectiveLinesPerMethodRule extends RuleTreeVisitor<Void> {

    static final int THR_ERROR = 45;
    static final int THR_CRITICAL = 35;
    static final int THR_WARNING = 25;

    @Override
    public RuleInfo getRuleInfo() {
        return new RuleInfo("cc", 6, "Avoid too many effective lines of code in function.");
    }

    public Integer visitMethod(MethodTree node, Void p) {
        location.push(node.getName().toString() + "(..)");

        final int linesOfEffectiveCode = getLinesOfEffectiveCode(node);

        generateIssueIfThreshold("too many effective lines of code: %s (>%s warning, >%s critical, >%s error)",
                linesOfEffectiveCode,
                THR_WARNING, THR_CRITICAL, THR_ERROR);

        location.pop();
        return 0;
    }

    public static int getLinesOfEffectiveCode(MethodTree node) {
        final int linesOfEffectiveCode;
        if (node.getBody() == null) {
            linesOfEffectiveCode = 0;
        } else {
            linesOfEffectiveCode = node.getBody().toString().split("\\n").length - 2 /* { & } */;
        }
        return linesOfEffectiveCode;
    }
}
