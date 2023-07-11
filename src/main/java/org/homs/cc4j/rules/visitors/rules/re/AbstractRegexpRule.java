package org.homs.cc4j.rules.visitors.rules.re;

import com.sun.source.tree.LiteralTree;
import org.homs.cc4j.rules.visitors.RuleTreeVisitor;

public abstract class AbstractRegexpRule extends RuleTreeVisitor<Void> {

    @Override
    public Integer visitLiteral(LiteralTree node, Void unused) {
        var value = node.getValue();
        if (value instanceof String) {
            checkString((String) value);
        }
        return super.visitLiteral(node, unused);
    }

    protected abstract void checkString(String value);
}
