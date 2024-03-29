package org.homs.cc4j.rules.visitors.rules;

import com.sun.source.tree.*;
import org.homs.cc4j.RuleInfo;
import org.homs.cc4j.issue.Thresholds;
import org.homs.cc4j.rules.visitors.RuleTreeVisitor;

public class CyclomaticComplexityTooHighRule extends RuleTreeVisitor<Void> {

    static final Thresholds DEFAULT_THRESHOLDS = new Thresholds(10, 20, 30);

    final Thresholds thresholds;

    public CyclomaticComplexityTooHighRule(Thresholds thresholds) {
        this.thresholds = thresholds;
    }

    public CyclomaticComplexityTooHighRule() {
        this(DEFAULT_THRESHOLDS);
    }

    @Override
    public RuleInfo getRuleInfo() {
        return new RuleInfo("cy", 1, "Cyclomatic complexity too high. (" + thresholds + ")");
    }

    void generateIssueIfThreshold(int metricValue) {
        generateIssueIfThreshold("cyclomatic complexity too high: %s (%s)", metricValue, thresholds);
    }

    @Override
    public Integer visitClass(ClassTree node, Void p) {
        return super.visitClass(node, p);
    }

    @Override
    public Integer visitMethod(MethodTree node, Void p) {
        location.push(node.getName().toString() + "(..)");
        int r = 1 + super.visitMethod(node, p);
        generateIssueIfThreshold(r);
        location.pop();
        return 0;
    }

    @Override
    public int visitClassInitializer(BlockTree classInitializer, Void p) {
        location.push("{}");
        int r = 1 + super.visitClassInitializer(classInitializer, p);
        generateIssueIfThreshold(r);
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
    public Integer visitForLoop(ForLoopTree node, Void p) {
        return super.visitForLoop(node, p) + 1;
    }

    @Override
    public Integer visitEnhancedForLoop(EnhancedForLoopTree node, Void unused) {
        return super.visitEnhancedForLoop(node, unused) + 1;
    }

    @Override
    public Integer visitLambdaExpression(LambdaExpressionTree node, Void unused) {
        return super.visitLambdaExpression(node, unused) + 1;
    }

    @Override
    public Integer visitCatch(CatchTree node, Void unused) {
        return super.visitCatch(node, unused) + 1;
    }

    @Override
    public Integer visitCase(CaseTree node, Void p) {
        if (node.getExpressions().isEmpty()) {
            // default:
            return super.visitCase(node, p); // no penalty: like an "else" part.
        } else {
            // case:
            return super.visitCase(node, p) + 1;
        }
    }

    // ? :
    @Override
    public Integer visitConditionalExpression(ConditionalExpressionTree node, Void p) {
        return super.visitConditionalExpression(node, p) + 1;
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

}
