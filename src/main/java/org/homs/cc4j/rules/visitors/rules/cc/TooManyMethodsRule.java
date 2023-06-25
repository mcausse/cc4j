package org.homs.cc4j.rules.visitors.rules.cc;

import com.sun.source.tree.ClassTree;
import com.sun.source.tree.MethodTree;
import com.sun.source.tree.Tree;
import org.homs.cc4j.RuleInfo;
import org.homs.cc4j.issue.Thresholds;
import org.homs.cc4j.rules.visitors.RuleTreeVisitor;

public class TooManyMethodsRule extends RuleTreeVisitor<Void> {

    static final Thresholds THRESHOLDS = new Thresholds(15, 25, 30);

    @Override
    public RuleInfo getRuleInfo() {
        return new RuleInfo("cc", 7, "Avoid too many methods per class. (" + THRESHOLDS + ")");
    }

    public Integer visitClass(ClassTree node, Void p) {
        location.push(node.getSimpleName().toString());

        var r = 0;
        for (Tree tree1 : node.getMembers()) {
            r += tree1.accept(this, p);
        }
        generateIssueIfThreshold("too many methods: %s (%s)", r, THRESHOLDS);

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
