package org.homs.cc4j.rules.visitors.rules;

import com.sun.source.tree.*;
import org.homs.cc4j.RuleInfo;
import org.homs.cc4j.issue.Thresholds;
import org.homs.cc4j.rules.visitors.RuleTreeVisitor;

import java.util.regex.Pattern;

/**
 * - pràcticament ignora switch-case's i cadenes de if..else's si ténen condicions senzilles (sense && ||).
 * - penalitza les condicions amb combinacions de && ||.
 * - penalitza exageradament combinacions en condicions en les anidacions.
 * <p>
 * - o sigui: cada expresió booleana +1. +1 per cada combinació de && ||. i x nest level.
 * <p>
 * <p>
 */
// TODO intentar injectar per C'tor el Thresholds, per poder fer millors testos
public class MartinCognitiveComplexityRule extends RuleTreeVisitor<Integer> {

    static final Thresholds THRESHOLDS = new Thresholds(7, 12, 20);

    @Override
    public RuleInfo getRuleInfo() {
        return new RuleInfo("cy", 3, "M. Homs complexity too high. (" + THRESHOLDS + ")");
    }

    void generateIssueIfThreshold(int metricValue) {
        generateIssueIfThreshold("M.Homs complexity metric is too high: %s (%s)", metricValue, THRESHOLDS);
    }

    @Override
    public Integer visitClass(ClassTree node, Integer p) {
        return super.visitClass(node, 0);
    }

    @Override
    public Integer visitMethod(MethodTree node, Integer p) {
        int complexity = super.visitMethod(node, 1);
        location.push(node.getName().toString() + "(..)");
        generateIssueIfThreshold(complexity);
        location.pop();
        return complexity;
    }

    @Override
    public int visitClassInitializer(BlockTree classInitializer, Integer p) {
        int complexity = super.visitClassInitializer(classInitializer, 1);
        location.push("{}");
        generateIssueIfThreshold(complexity);
        location.pop();
        return 0;
    }

    @Override
    public Integer visitIf(IfTree node, Integer deepLevel) {
        int metricValue = computeExpression(node.getCondition().toString());
        if (node.getElseStatement() instanceof IfTree) {
            return metricValue * deepLevel + super.visitIf(node, deepLevel);
        }
        return metricValue * deepLevel + super.visitIf(node, deepLevel + 1);
    }

    @Override
    public Integer visitWhileLoop(WhileLoopTree node, Integer deepLevel) {
        int metricValue = computeExpression(node.getCondition().toString());
        return metricValue * deepLevel + super.visitWhileLoop(node, deepLevel + 1);
    }

    @Override
    public Integer visitDoWhileLoop(DoWhileLoopTree node, Integer deepLevel) {
        int metricValue = computeExpression(node.getCondition().toString());
        return metricValue * deepLevel + super.visitDoWhileLoop(node, deepLevel + 1);
    }

    @Override
    public Integer visitForLoop(ForLoopTree node, Integer deepLevel) {
        int metricValue = computeExpression(node.getCondition().toString());
        return metricValue * deepLevel + super.visitForLoop(node, deepLevel + 1);
    }

    @Override
    public Integer visitEnhancedForLoop(EnhancedForLoopTree node, Integer deepLevel) {
        int metricValue = 1;
        return metricValue * deepLevel + super.visitEnhancedForLoop(node, deepLevel + 1);
    }

    @Override
    public Integer visitSwitch(SwitchTree node, Integer deepLevel) {
        int metricValue = 1;
        return metricValue * deepLevel + super.visitSwitch(node, deepLevel + 1);
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

        return s.length() + 1;
    }
}