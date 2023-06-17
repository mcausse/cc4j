package org.homs.cc4j.visitors.rules;

import com.sun.source.tree.*;
import org.homs.cc4j.visitors.RuleTreeVisitor;

public class TooComplicatedConditionRule extends RuleTreeVisitor<Void> {

    static final int THR_ERROR = 7;
    static final int THR_CRITICAL = 5;
    static final int THR_WARNING = 3;

    void generateIssueIfThreshold(int metricValue, String expression) {
        String message = "too complicated logical condition, rated as %s (>%s warning, >%s critical, >%s error); expression=" +
                expression.replace("%", "%%");
        generateIssueIfThreshold(message, metricValue, THR_WARNING, THR_CRITICAL, THR_ERROR);
    }

    public Integer visitIf(IfTree node, Void p) {
        int metricValue = node.getCondition().accept(this, p);
        generateIssueIfThreshold(metricValue, node.getCondition().toString());

        return super.visitIf(node, p);
    }

    public Integer visitWhileLoop(WhileLoopTree node, Void p) {
        int metricValue = node.getCondition().accept(this, p);
        generateIssueIfThreshold(metricValue, node.getCondition().toString());

        return super.visitWhileLoop(node, p);
    }

    public Integer visitDoWhileLoop(DoWhileLoopTree node, Void p) {
        int metricValue = node.getCondition().accept(this, p);
        generateIssueIfThreshold(metricValue, node.getCondition().toString());

        return super.visitDoWhileLoop(node, p);
    }

    @Override
    public Integer visitReturn(ReturnTree node, Void p) {
        if (node.getExpression() != null) {
            int metricValue = node.getExpression().accept(this, p);
            generateIssueIfThreshold(metricValue, node.getExpression().toString());
        }
        return super.visitReturn(node, p);
    }

    // ===

    public Integer visitBinary(BinaryTree node, Void p) {
        var r = super.visitBinary(node, p);
        if (node.getKind() == Tree.Kind.CONDITIONAL_AND || node.getKind() == Tree.Kind.CONDITIONAL_OR) {
            r++;
        }
        return r;
    }

    public Integer visitUnary(UnaryTree node, Void p) {
        var r = super.visitUnary(node, p);
        if (node.getKind() == Tree.Kind.LOGICAL_COMPLEMENT) {
            r++;
        }
        return r;
    }

    public Integer visitConditionalExpression(ConditionalExpressionTree node, Void p) {
        var r = super.visitConditionalExpression(node, p);
        return r + 3;
    }
}
