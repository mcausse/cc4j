package org.homs.cc4j.rules.visitors.rules;

import com.sun.source.tree.*;
import org.homs.cc4j.RuleInfo;
import org.homs.cc4j.issue.Thresholds;
import org.homs.cc4j.rules.visitors.RuleTreeVisitor;

public class HomsCognoCyclomaticComplexityTooHighRule extends RuleTreeVisitor<NestingStatus> {

    static final Thresholds DEFAULT_THRESHOLDS = new Thresholds(15, 20, 25);

    final Thresholds thresholds;

    public HomsCognoCyclomaticComplexityTooHighRule(Thresholds thresholds) {
        this.thresholds = thresholds;
    }

    public HomsCognoCyclomaticComplexityTooHighRule() {
        this(DEFAULT_THRESHOLDS);
    }

    @Override
    public RuleInfo getRuleInfo() {
        return new RuleInfo("cy", 3, "Cogno-Cyclomatic complexity too high. (" + thresholds + ")");
    }

    void generateIssueIfThreshold(int metricValue) {
        generateIssueIfThreshold("Cogno/Cyclomatic complexity too high: %s (%s)", metricValue, thresholds);
    }

    @Override
    public Integer visitClass(ClassTree node, NestingStatus _nestingStatus) {
        return super.visitClass(node, NestingStatus.build());
    }

    @Override
    public Integer visitMethod(MethodTree node, NestingStatus _nestingStatus) {
        location.push(node.getName().toString() + "(..)");
        int r = 1 + super.visitMethod(node, NestingStatus.build());
        generateIssueIfThreshold(r);
        location.pop();
        return 0;
    }

    @Override
    public int visitClassInitializer(BlockTree classInitializer, NestingStatus _nestingStatus) {
        location.push("{}");
        int r = 1 + super.visitClassInitializer(classInitializer, NestingStatus.build());
        generateIssueIfThreshold(r);
        location.pop();
        return 0;
    }

    @Override
    public Integer visitIf(IfTree node, NestingStatus nestingStatus) {
//        var r = Math.max(1, nestingStatus.nestedLevel);
        var r = 1 + nestingStatus.nestedLevel;

        r += node.getCondition().accept(this, nestingStatus);
        if (node.getThenStatement() != null) {
            r += node.getThenStatement().accept(this, nestingStatus.incNestedLevel());
        }
        if (node.getElseStatement() != null) {
            final NestingStatus nextNestingStatus;
            if (node.getElseStatement() instanceof IfTree) {
                // threat an "elseif" with no additional nesting cost (like switch-case)
                nextNestingStatus = nestingStatus;
            } else {
                nextNestingStatus = nestingStatus.incNestedLevel();
            }
            r += node.getElseStatement().accept(this, nextNestingStatus);
        }
        return r;
    }

    @Override
    public Integer visitWhileLoop(WhileLoopTree node, NestingStatus nestingStatus) {
//        var r = Math.max(1, nestingStatus.nestedLevel);
        var r = 1 + nestingStatus.nestedLevel;
        r += node.getCondition().accept(this, nestingStatus);
        r += node.getStatement().accept(this, nestingStatus.incNestedLevel());
        return r;
    }

    @Override
    public Integer visitDoWhileLoop(DoWhileLoopTree node, NestingStatus nestingStatus) {
//        var r = Math.max(1, nestingStatus.nestedLevel);
        var r = 1 + nestingStatus.nestedLevel;
        r += node.getCondition().accept(this, nestingStatus);
        r += node.getStatement().accept(this, nestingStatus.incNestedLevel());
        return r;
    }

    @Override
    public Integer visitForLoop(ForLoopTree node, NestingStatus nestingStatus) {
//        var r = Math.max(1, nestingStatus.nestedLevel);
        var r = 1 + nestingStatus.nestedLevel;
        r += node.getCondition().accept(this, nestingStatus);
        r += node.getStatement().accept(this, nestingStatus.incNestedLevel());
        return r;
    }

    @Override
    public Integer visitEnhancedForLoop(EnhancedForLoopTree node, NestingStatus nestingStatus) {
//        var r = Math.max(1, nestingStatus.nestedLevel);
        var r = 1 + nestingStatus.nestedLevel;
        r += node.getStatement().accept(this, nestingStatus.incNestedLevel());
        return r;
    }

    @Override
    public Integer visitLambdaExpression(LambdaExpressionTree node, NestingStatus nestingStatus) {
//        return super.visitLambdaExpression(node, nestingStatus) + 1;
        var r = 1 + nestingStatus.nestedLevel;
        for (VariableTree tree : node.getParameters()) {
            r += tree.accept(this, nestingStatus);
        }
        r += node.getBody().accept(this, nestingStatus.incNestedLevel());
        return r;
    }

    @Override
    public Integer visitCatch(CatchTree node, NestingStatus nestingStatus) {
//        var r = Math.max(1, nestingStatus.nestedLevel);
        var r = 1 + nestingStatus.nestedLevel;
        r += node.getParameter().accept(this, nestingStatus);
        r += node.getBlock().accept(this, nestingStatus);
        return r;
    }

    @Override
    public Integer visitCase(CaseTree node, NestingStatus nestingStatus) {
        if (node.getExpressions().isEmpty()) {
            // default:
            return super.visitCase(node, nestingStatus); // no penalty: like an "else" part.
        } else {
            // case:
            super.visitCase(node, nestingStatus);
            return 1;
        }
    }

    // ? :
    @Override
    public Integer visitConditionalExpression(ConditionalExpressionTree node, NestingStatus nestingStatus) {
        return super.visitConditionalExpression(node, nestingStatus) + 1;
    }

    // ===

    @Override
    public Integer visitBinary(BinaryTree node, NestingStatus nestingStatus) {
        var n = 0;
        if (node.getKind() == Tree.Kind.CONDITIONAL_AND || node.getKind() == Tree.Kind.CONDITIONAL_OR) {
            n = 1;
        }
        return super.visitBinary(node, nestingStatus) + n;
    }

}
