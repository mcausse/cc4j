package org.homs.cc4j.visitors.rules;

import com.sun.source.tree.ClassTree;
import com.sun.source.tree.MethodTree;
import com.sun.source.tree.Tree;
import org.homs.cc4j.Listener;
import org.homs.cc4j.Location;
import org.homs.cc4j.visitors.RuleTreeVisitor;

public class TooManyMethodsRule extends RuleTreeVisitor {

    static int THR_ERROR = 30;
    static int THR_CRITICAL = 25;
    static int THR_WARNING = 15;

    public TooManyMethodsRule(Listener listener, Location location) {
        super(listener, location);
    }

    public Integer visitClass(ClassTree node, Void p) {
        var r = 0;
        for (Tree tree1 : node.getMembers()) {
            r += tree1.accept(this, p);
        }
        generateIssueIfThreshold("too many methods: %s (>%s warning, >%s critical, >%s error)",
                r,
                THR_WARNING, THR_CRITICAL, THR_ERROR);

        return 0;
    }

    public Integer visitMethod(MethodTree node, Void p) {
        super.visitMethod(node, p);
        return 1;
    }
}
