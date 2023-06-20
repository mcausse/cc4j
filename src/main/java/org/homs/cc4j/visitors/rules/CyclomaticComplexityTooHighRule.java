package org.homs.cc4j.visitors.rules;

import com.sun.source.tree.*;
import org.homs.cc4j.RuleInfo;
import org.homs.cc4j.visitors.RuleTreeVisitor;

public class CyclomaticComplexityTooHighRule extends RuleTreeVisitor<Void> {

    static final int THR_ERROR = 30;
    static final int THR_CRITICAL = 20;
    static final int THR_WARNING = 10;

    @Override
    public RuleInfo getRuleInfo() {
        return new RuleInfo("cy", 1, "Cyclomatic complexity too high.");
    }

    void generateIssueIfThreshold(int metricValue) {
        generateIssueIfThreshold(
                "cyclomatic complexity too high: %s (>%s warning, >%s critical, >%s error",
                metricValue, THR_WARNING, THR_CRITICAL, THR_ERROR);
    }

    @Override
    public Integer visitMethod(MethodTree node, Void p) {
        location.push(node.getName().toString() + "(..)");
        int r = super.visitMethod(node, p);
        generateIssueIfThreshold(r + 1);
        location.pop();
        return 0;
    }

    @Override
    public Integer visitIf(IfTree node, Void p) {
        return super.visitIf(node, p) + 1;
    }

    @Override
    public Integer visitWhileLoop(WhileLoopTree node, Void p) {
        return super.visitWhileLoop(node, p) + 1;
    }

    @Override
    public Integer visitDoWhileLoop(DoWhileLoopTree node, Void p) {
        return super.visitDoWhileLoop(node, p) + 1;
    }

    @Override
    public Integer visitForLoop(ForLoopTree node, Void unused) {
        return super.visitForLoop(node, unused) + 1;
    }

    @Override
    public Integer visitEnhancedForLoop(EnhancedForLoopTree node, Void unused) {
        return super.visitEnhancedForLoop(node, unused) + 1;
    }

    @Override
    public Integer visitCase(CaseTree node, Void p) {
        return super.visitCase(node, p) + 1;
    }

    //    @Override
    //    public Integer visitReturn(ReturnTree node, Void p) {
    //        return super.visitReturn(node, p) + 1;
    //    }

    // ===

    @Override
    public Integer visitBinary(BinaryTree node, Void p) {
        var n = 0;
        if (node.getKind() == Tree.Kind.CONDITIONAL_AND || node.getKind() == Tree.Kind.CONDITIONAL_OR) {
            n = 1;
        }
        return super.visitBinary(node, p) + n;
    }

    @Override
    public Integer visitConditionalExpression(ConditionalExpressionTree node, Void p) {
        var r = super.visitConditionalExpression(node, p);
        return r + 1;
    }
}
