package org.homs.cc4j.rules.visitors.rules.cc;

import com.sun.source.tree.MethodTree;
import org.homs.cc4j.RuleInfo;
import org.homs.cc4j.issue.Thresholds;
import org.homs.cc4j.rules.visitors.RuleTreeVisitor;

public class TooManyArgumentsRule extends RuleTreeVisitor<Void> {

    static final Thresholds CTOR_THRESHOLDS = new Thresholds(5, 7, 9);
    static final Thresholds METHOD_THRESHOLDS = new Thresholds(3, 4, 5);

    @Override
    public RuleInfo getRuleInfo() {
        return new RuleInfo("cc", 3,
                "Avoid too many arguments for a C'tor or a function. C'tors=(" + CTOR_THRESHOLDS + "), methods=(" + METHOD_THRESHOLDS + ")");
    }

    public Integer visitMethod(MethodTree node, Void p) {
        super.visitMethod(node, p);
        location.push(node.getName().toString() + "(..)");

        if (node.getReturnType() == null) {
            // C'tor
            generateIssueIfThreshold("too many arguments for a C'tor: %s (%s)",
                    node.getParameters().size(), CTOR_THRESHOLDS);
        } else {
            // Method
            generateIssueIfThreshold("too many arguments: %s (%s)",
                    node.getParameters().size(), METHOD_THRESHOLDS);
        }
        location.pop();
        return 0;
    }
}

