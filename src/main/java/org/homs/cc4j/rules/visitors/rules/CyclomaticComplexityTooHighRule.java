package org.homs.cc4j.rules.visitors.rules;

import com.sun.source.tree.*;
import org.homs.cc4j.RuleInfo;
import org.homs.cc4j.issue.Thresholds;
import org.homs.cc4j.rules.visitors.RuleTreeVisitor;

// TODO intentar injectar per C'tor el Thresholds, per poder fer millors testos
public class CyclomaticComplexityTooHighRule extends RuleTreeVisitor<Boolean> {

    static final Thresholds THRESHOLDS = new Thresholds(10, 20, 30);

    @Override
    public RuleInfo getRuleInfo() {
        return new RuleInfo("cy", 1, "Cyclomatic complexity too high. (" + THRESHOLDS + ")");
    }

    void generateIssueIfThreshold(int metricValue) {
        generateIssueIfThreshold("cyclomatic complexity too high: %s (%s)", metricValue, THRESHOLDS);
    }

    @Override
    public Integer visitClass(ClassTree node, Boolean p) {
        return super.visitClass(node, false);
    }

    @Override
    public Integer visitMethod(MethodTree node, Boolean p) {
        location.push(node.getName().toString() + "(..)");
        int r = super.visitMethod(node, false);
        generateIssueIfThreshold(r + 1);
        location.pop();
        return 0;
    }

    @Override
    public int visitClassInitializer(BlockTree classInitializer, Boolean p) {
        location.push("{}");
        int r = super.visitClassInitializer(classInitializer, false);
        generateIssueIfThreshold(r + 1);
        location.pop();
        return 0;
    }

    @Override
    public Integer visitIf(IfTree node, Boolean p) {
        var r = 1;
        r += node.getCondition().accept(this, true);
        r += super.visitIf(node, p);
        return r;
    }

    @Override
    public Integer visitWhileLoop(WhileLoopTree node, Boolean p) {
        var r = 1;
        r += node.getCondition().accept(this, true);
        r += super.visitWhileLoop(node, p);
        return r;
    }

    @Override
    public Integer visitDoWhileLoop(DoWhileLoopTree node, Boolean p) {
        var r = 1;
        r += node.getCondition().accept(this, true);
        r += super.visitDoWhileLoop(node, p);
        return r;
    }

    @Override
    public Integer visitForLoop(ForLoopTree node, Boolean p) {
        var r = 1;
        r += node.getCondition().accept(this, true);
        r += super.visitForLoop(node, p);
        return r;
    }

    @Override
    public Integer visitEnhancedForLoop(EnhancedForLoopTree node, Boolean unused) {
        return super.visitEnhancedForLoop(node, unused) + 1;
    }

    @Override
    public Integer visitCase(CaseTree node, Boolean p) {
        if (node.getExpressions().isEmpty()) {
            // default:
            return super.visitCase(node, p); // no penalty: like an "else" part.
        } else {
            // case:
            return super.visitCase(node, p) + 1;
        }
    }

    // ===

    @Override
    public Integer visitBinary(BinaryTree node, Boolean p) {
        var n = 0;
        if (Boolean.TRUE.equals(p)) {
            if (node.getKind() == Tree.Kind.CONDITIONAL_AND || node.getKind() == Tree.Kind.CONDITIONAL_OR) {
                n = 1;
            }
        }
        return super.visitBinary(node, p) + n;
    }

    @Override
    public Integer visitConditionalExpression(ConditionalExpressionTree node, Boolean p) {
        var r = 1;
        if (Boolean.TRUE.equals(p)) {
            r += node.getCondition().accept(this, true);
        }
        r += super.visitConditionalExpression(node, p);
        return r;
    }
}
