package org.homs.cc4j.rules.visitors.rules;

import com.sun.source.tree.*;
import org.homs.cc4j.RuleInfo;
import org.homs.cc4j.issue.Thresholds;
import org.homs.cc4j.rules.visitors.RuleTreeVisitor;

// TODO intentar injectar per C'tor el Thresholds, per poder fer millors testos
public class CyclomaticComplexityTooHighRule extends RuleTreeVisitor<Void> {

    static final Thresholds THRESHOLDS = new Thresholds(10, 20, 30);

    @Override
    public RuleInfo getRuleInfo() {
        return new RuleInfo("cy", 1, "Cyclomatic complexity too high. (" + THRESHOLDS + ")");
    }

    void generateIssueIfThreshold(int metricValue) {
        generateIssueIfThreshold("cyclomatic complexity too high: %s (%s)", metricValue, THRESHOLDS);
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
