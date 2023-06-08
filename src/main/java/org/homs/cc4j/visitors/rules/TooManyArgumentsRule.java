package org.homs.cc4j.visitors.rules;

import com.sun.source.tree.MethodTree;
import org.homs.cc4j.visitors.RuleTreeVisitor;

public class TooManyArgumentsRule extends RuleTreeVisitor<Void> {

    static int THR_ERROR = 5;
    static int THR_CRITICAL = 4;
    static int THR_WARNING = 3;

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
