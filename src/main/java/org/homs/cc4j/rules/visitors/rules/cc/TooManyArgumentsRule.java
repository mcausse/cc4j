package org.homs.cc4j.rules.visitors.rules.cc;

import com.sun.source.tree.MethodTree;
import org.homs.cc4j.RuleInfo;
import org.homs.cc4j.rules.visitors.RuleTreeVisitor;

public class TooManyArgumentsRule extends RuleTreeVisitor<Void> {

    static final int THR_ERROR = 5;
    static final int THR_CRITICAL = 4;
    static final int THR_WARNING = 3;

    @Override
    public RuleInfo getRuleInfo() {
        return new RuleInfo("cc", 3, "Avoid too many arguments for a function.");
    }

    public Integer visitMethod(MethodTree node, Void p) {
        super.visitMethod(node, p);
        location.push(node.getName().toString() + "(..)");

        generateIssueIfThreshold("too many arguments: %s (>%s warning, >%s critical, >%s error)",
                node.getParameters().size(),
                THR_WARNING, THR_CRITICAL, THR_ERROR);

        location.pop();
        return 0;
    }
}
// TODO Constructor has 8 parameters, which is greater than 7 authorized.

