package org.homs.cc4j.visitors;

import com.sun.source.tree.*;
import org.homs.cc4j.Listener;
import org.homs.cc4j.issue.IssueSeverity;

public class TooComplicatedConditionTreeVisitor extends Java19TreeVisitor<Void, Void> {

    static int THR_ERROR = 7;
    static int THR_CRITICAL = 5;
    static int THR_WARNING = 3;

    final Listener listener;

    final StringBuilder location = new StringBuilder();
    final StringBuilder strb = new StringBuilder();

    public TooComplicatedConditionTreeVisitor(Listener listener) {
        this.listener = listener;
    }

    public Void visitClass(ClassTree node, Void p) {
        location.append(node.getSimpleName().toString());
        return super.visitClass(node, p);
    }

    public Void visitIf(IfTree node, Void p) {
        this.strb.setLength(0);
        node.getCondition().accept(this, p);
        generateIssueIfThreshold(node.getCondition());
        return super.visitIf(node, p);
    }

    public Void visitWhileLoop(WhileLoopTree node, Void p) {
        this.strb.setLength(0);
        node.getCondition().accept(this, p);
        generateIssueIfThreshold(node.getCondition());
        return super.visitWhileLoop(node, p);
    }

    public Void visitDoWhileLoop(DoWhileLoopTree node, Void p) {
        this.strb.setLength(0);
        node.getCondition().accept(this, p);
        generateIssueIfThreshold(node.getCondition());
        return super.visitDoWhileLoop(node, p);
    }

    private void generateIssueIfThreshold(Tree condition) {
        final String message = "too complicated logical condition: '" + strb + "': " + condition;

        if (strb.length() > THR_ERROR) {
            this.listener.getIssuesReport().registerIssue(IssueSeverity.ERROR, location.toString(), message);
        } else if (strb.length() > THR_CRITICAL) {
            this.listener.getIssuesReport().registerIssue(IssueSeverity.CRITICAL, location.toString(), message);
        } else if (strb.length() > THR_WARNING) {
            this.listener.getIssuesReport().registerIssue(IssueSeverity.WARNING, location.toString(), message);
        }
    }

    public Void visitBinary(BinaryTree node, Void p) {
        var r = super.visitBinary(node, p);
        if (node.getKind() == Tree.Kind.CONDITIONAL_AND) {
            this.strb.append('&');
        }
        if (node.getKind() == Tree.Kind.CONDITIONAL_OR) {
            this.strb.append('|');
        }
        return r;
    }

    public Void visitUnary(UnaryTree node, Void p) {
        var r = super.visitUnary(node, p);
        if (node.getKind() == Tree.Kind.LOGICAL_COMPLEMENT) {
            this.strb.append('!');
        }
        return r;
    }

    public Void visitConditionalExpression(ConditionalExpressionTree node, Void p) {
        var r = super.visitConditionalExpression(node, p);
        this.strb.append("?:");
        return r;
    }


//    public Void visitParenthesized(ParenthesizedTree node, Void p) {
//        this.strb.append('(');
//        var r = super.visitParenthesized(node, p);
//        this.strb.append(')');
//        return r;
//    }
}
