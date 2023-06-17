package org.homs.cc4j.visitors.rules;

import com.sun.source.tree.*;
import org.homs.cc4j.visitors.RuleTreeVisitor;

import java.util.regex.Pattern;

public class CognitiveComplexityTooHighRule extends RuleTreeVisitor<Integer> {

    static final int THR_ERROR = 30;
    static final int THR_CRITICAL = 20;
    static final int THR_WARNING = 10;

    void generateIssueIfThreshold(int metricValue) {
        generateIssueIfThreshold(
                "cognitive complexity too high: %s (>%s warning, >%s critical, >%s error",
                metricValue, THR_WARNING, THR_CRITICAL, THR_ERROR);
    }

    @Override
    public Integer visitClass(ClassTree node, Integer p) {
        return super.visitClass(node, 0);
    }

    @Override
    public Integer visitMethod(MethodTree node, Integer nestedLevel) {
        location.push(node.getName().toString() + "(..)");
        int r = super.visitMethod(node, 0);
        generateIssueIfThreshold(r);
        location.pop();
        return 0;
    }

    @Override
    public Integer visitIf(IfTree node, Integer nestedLevel) {
        int r = 1;
        r += computeExpression(node.getCondition().toString());
        r += node.getThenStatement().accept(this, nestedLevel + 1);
        if (node.getElseStatement() != null) {
            if (node.getElseStatement() instanceof IfTree) {
                // Manté el nested level per "elseif"
                r += node.getElseStatement().accept(this, nestedLevel);
            } else {
                r += node.getElseStatement().accept(this, nestedLevel + 1) + 1;
            }
        }
        return r;
    }

    @Override
    public Integer visitWhileLoop(WhileLoopTree node, Integer nestedLevel) {
        return super.visitWhileLoop(node, nestedLevel + 1) + 1 + nestedLevel + computeExpression(node.getCondition().toString());
    }

    @Override
    public Integer visitDoWhileLoop(DoWhileLoopTree node, Integer nestedLevel) {
        return super.visitDoWhileLoop(node, nestedLevel + 1) + 1 + nestedLevel + computeExpression(node.getCondition().toString());
    }

    @Override
    public Integer visitForLoop(ForLoopTree node, Integer nestedLevel) {
        return super.visitForLoop(node, nestedLevel + 1) + 1 + nestedLevel + computeExpression(node.getCondition().toString());
    }

    @Override
    public Integer visitEnhancedForLoop(EnhancedForLoopTree node, Integer nestedLevel) {
        return super.visitEnhancedForLoop(node, nestedLevel + 1) + 1 + nestedLevel;
    }

    @Override
    public Integer visitCatch(CatchTree node, Integer nestedLevel) {
        return super.visitCatch(node, nestedLevel + 1) + 1 + nestedLevel;
    }

    // ===

    int computeExpression(String expression) {
        var s = "";
        var p = Pattern.compile("((\\&\\&)|(\\|\\|))");
        var m = p.matcher(expression);
        while (m.find()) {
            s += m.group(1);
        }

        s = s.replaceAll("\\&+", "&").replaceAll("\\|+", "|");

        return s.length();
    }

}
