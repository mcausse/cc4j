package org.homs.cc4j.rules.visitors.rules.cc;

import com.sun.source.tree.MethodTree;
import org.homs.cc4j.RuleInfo;
import org.homs.cc4j.issue.Thresholds;
import org.homs.cc4j.rules.visitors.RuleTreeVisitor;

public class TooManyEffectiveLinesPerMethodRule extends RuleTreeVisitor<Void> {

    static final Thresholds THRESHOLDS = new Thresholds(25, 35, 45);

    @Override
    public RuleInfo getRuleInfo() {
        return new RuleInfo("cc", 6, "Avoid too many effective lines of code in function. (" + THRESHOLDS + ")");
    }

    public Integer visitMethod(MethodTree node, Void p) {
        location.push(node.getName().toString() + "(..)");

        final int linesOfEffectiveCode = getLinesOfEffectiveCode(node);

        generateIssueIfThreshold("too many effective lines of code: %s (%s)", linesOfEffectiveCode, THRESHOLDS);

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
