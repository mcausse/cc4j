package org.homs.cc4j.rules.visitors.rules;

import com.sun.source.tree.*;
import org.homs.cc4j.RuleInfo;
import org.homs.cc4j.issue.Thresholds;
import org.homs.cc4j.rules.visitors.RuleTreeVisitor;

import java.util.regex.Pattern;

public class CognitiveComplexityTooHighRule extends RuleTreeVisitor<NestingStatus> {

    static final Thresholds THRESHOLDS = new Thresholds(10, 20, 30);

    @Override
    public RuleInfo getRuleInfo() {
        return new RuleInfo("cy", 2, "Cognitive complexity too high. (" + THRESHOLDS + ")");
    }

    void generateIssueIfThreshold(int metricValue) {
        generateIssueIfThreshold(
                "cognitive complexity too high: %s (%s)", metricValue, THRESHOLDS);
    }

    @Override
    public Integer visitClass(ClassTree node, NestingStatus p) {
        return super.visitClass(node, NestingStatus.build());
    }

    @Override
    public Integer visitMethod(MethodTree node, NestingStatus nestingStatus) {
        location.push(node.getName().toString() + "(..)");
        int r = super.visitMethod(node, NestingStatus.build());
        generateIssueIfThreshold(r);
        location.pop();
        return 0;
    }

    @Override
    public int visitClassInitializer(BlockTree classInitializer, NestingStatus nestingStatus) {
        location.push("{}");
        int r = super.visitClassInitializer(classInitializer, NestingStatus.build());
        generateIssueIfThreshold(r);
        location.pop();
        return 0;
    }

    @Override
    public Integer visitIf(IfTree node, NestingStatus nestingStatus) {
        int r = 1;
        if (nestingStatus.nestingBonus) {
            r += nestingStatus.nestedLevel;
        }
        r += computeExpression(node.getCondition().toString());
        r += node.getThenStatement().accept(this, nestingStatus.incNestedLevel());

        //        hybrid increments for:
        //             else if, elif, else, …
        //        No nesting increment is assessed for these structures because the mental cost has already
        //        been paid when reading the if.
        if (node.getElseStatement() != null) {
            if (node.getElseStatement() instanceof IfTree) {
                r += node.getElseStatement().accept(this, nestingStatus.incNestedLevelButAvoidNextNestingBonus());
            } else {
                r += node.getElseStatement().accept(this, nestingStatus.incNestedLevel()) + 1;
            }
        }

        return r;
    }

    @Override
    public Integer visitWhileLoop(WhileLoopTree node, NestingStatus nestingStatus) {
        return super.visitWhileLoop(node, nestingStatus.incNestedLevel())
                + 1
                + nestingStatus.nestedLevel
                + computeExpression(node.getCondition().toString());
    }

    @Override
    public Integer visitDoWhileLoop(DoWhileLoopTree node, NestingStatus nestingStatus) {
        return super.visitDoWhileLoop(node, nestingStatus.incNestedLevel())
                + 1
                + nestingStatus.nestedLevel
                + computeExpression(node.getCondition().toString());
    }

    @Override
    public Integer visitForLoop(ForLoopTree node, NestingStatus nestingStatus) {
        var expression = 0;
        if (node.getCondition() != null) {
            expression += computeExpression(node.getCondition().toString());
        }
        return super.visitForLoop(node, nestingStatus.incNestedLevel()) + 1 + nestingStatus.nestedLevel + expression;
    }

    @Override
    public Integer visitEnhancedForLoop(EnhancedForLoopTree node, NestingStatus nestingStatus) {
        return super.visitEnhancedForLoop(node, nestingStatus.incNestedLevel()) + 1 + nestingStatus.nestedLevel;
    }

    @Override
    public Integer visitCatch(CatchTree node, NestingStatus nestingStatus) {
        return super.visitCatch(node, nestingStatus.incNestedLevel()) + 1 + nestingStatus.nestedLevel;
    }

    @Override
    public Integer visitSwitch(SwitchTree node, NestingStatus nestingStatus) {
        return super.visitSwitch(node, nestingStatus.incNestedLevel()) + 1 + nestingStatus.nestedLevel;
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
