package org.homs.cc4j.visitors.rules.cc;

import com.sun.source.tree.ClassTree;
import com.sun.source.tree.MethodTree;
import com.sun.source.tree.Tree;
import org.homs.cc4j.RuleInfo;
import org.homs.cc4j.visitors.RuleTreeVisitor;

public class TooManyMethodsRule extends RuleTreeVisitor<Void> {

    static final int THR_ERROR = 30;
    static final int THR_CRITICAL = 25;
    static final int THR_WARNING = 15;

    @Override
    public RuleInfo getRuleInfo() {
        return new RuleInfo("cc", 7, "Avoid too many methods per class.");
    }

    public Integer visitClass(ClassTree node, Void p) {
        location.push(node.getSimpleName().toString());

        var r = 0;
        for (Tree tree1 : node.getMembers()) {
            r += tree1.accept(this, p);
        }
        generateIssueIfThreshold("too many methods: %s (>%s warning, >%s critical, >%s error)",
                r,
                THR_WARNING, THR_CRITICAL, THR_ERROR);

        location.pop();
        return 0;
    }

    public Integer visitMethod(MethodTree node, Void p) {
        super.visitMethod(node, p);
        if (node.getReturnType() == null) { // Returns null for a constructor.
            return 0;
        }
        return 1;
    }
}
