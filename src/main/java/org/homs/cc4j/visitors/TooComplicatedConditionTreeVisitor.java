package org.homs.cc4j.visitors;

import com.sun.source.tree.*;
import org.homs.cc4j.Listener;
import org.homs.cc4j.Location;
import org.homs.cc4j.issue.IssueSeverity;

public class TooComplicatedConditionTreeVisitor extends Java19MetricsTreeVisitor<Void> {

    static int THR_ERROR = 7;
    static int THR_CRITICAL = 5;
    static int THR_WARNING = 3;

    final Listener listener;

    Location location;

    public TooComplicatedConditionTreeVisitor(Listener listener) {
        this.listener = listener;
    }

    public Integer visitClass(ClassTree node, Void p) {
        this.location = new Location(node.getSimpleName().toString());
        return super.visitClass(node, p);
    }

    public Integer visitMethod(MethodTree node, Void p) {
        location.push(node.getName().toString() + "(..)");
        return super.visitMethod(node, p);
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

    private void generateIssueIfThreshold(int metricValue, String expression) {
        final String message = "too complicated logical condition, rated as " + metricValue + "; expression=" + expression;

        if (metricValue > THR_ERROR) {
            this.listener.getIssuesReport().registerIssue(IssueSeverity.ERROR, location.toString(), message);
        } else if (metricValue > THR_CRITICAL) {
            this.listener.getIssuesReport().registerIssue(IssueSeverity.CRITICAL, location.toString(), message);
        } else if (metricValue > THR_WARNING) {
            this.listener.getIssuesReport().registerIssue(IssueSeverity.WARNING, location.toString(), message);
        }
    }

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


//    public Void visitParenthesized(ParenthesizedTree node, Void p) {
//        this.strb.append('(');
//        var r = super.visitParenthesized(node, p);
//        this.strb.append(')');
//        return r;
//    }
}
