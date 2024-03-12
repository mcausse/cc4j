package org.homs.cc4j.rules.visitors.rules.cc;

import com.sun.source.tree.*;
import org.homs.cc4j.RuleInfo;
import org.homs.cc4j.issue.Thresholds;
import org.homs.cc4j.rules.visitors.RuleTreeVisitor;

public class MaxIndentLevelRule2 extends RuleTreeVisitor<MaxIndentLevelRule2.DeepExplorer> {

    static final Thresholds THRESHOLDS = new Thresholds(2, 3, 4);

    @Override
    public RuleInfo getRuleInfo() {
        return new RuleInfo("cc", 5, "Avoid too deply nested code (hadookens). (" + THRESHOLDS + ")");
    }

    @Override
    public Integer visitClass(ClassTree node, DeepExplorer p) {
        DeepExplorer deep = DeepExplorer.build();
        super.visitClass(node, deep);
        generateIssueIfThreshold("%s indent levels (%s)", deep.getMaxDeep(), THRESHOLDS);
        return 0;
    }

    @Override
    public Integer visitMethod(MethodTree node, MaxIndentLevelRule2.DeepExplorer p) {
        location.push(node.getName().toString() + "(..)");
        DeepExplorer deep = DeepExplorer.build();
        super.visitMethod(node, deep);
        generateIssueIfThreshold("%s indent levels (%s)", deep.getMaxDeep(), THRESHOLDS);
        location.pop();
        return 0;
    }

    @Override
    public int visitClassInitializer(BlockTree node, MaxIndentLevelRule2.DeepExplorer p) {
        location.push("{}");
        DeepExplorer deep = DeepExplorer.build();
        super.visitClassInitializer(node, deep);
        generateIssueIfThreshold("%s indent levels (%s)", deep.getMaxDeep(), THRESHOLDS);
        location.pop();
        return 0;
    }

    static class DeepExplorer {

        private int maxDeep;
        private int deep;

        private DeepExplorer(int maxDeep, int deep) {
            this.maxDeep = maxDeep;
            this.deep = deep;
        }

        public static DeepExplorer build() {
            return new DeepExplorer(0, 0);
        }

        public void incrementDeep() {
            deep++;
            if (maxDeep < deep) {
                maxDeep = deep;
            }
        }

        public void decrementDeep() {
            deep--;
        }

        public int getMaxDeep() {
            return maxDeep;
        }
    }
    // ===

    @Override
    public Integer visitIf(IfTree node, MaxIndentLevelRule2.DeepExplorer deep) {
        if (node.getThenStatement() != null) {
            deep.incrementDeep();
            node.getThenStatement().accept(this, deep);
            deep.decrementDeep();
        }
        if (node.getElseStatement() != null) {
            // els else...if no compten com a indentació (a menys que else { if...)
            if (node.getElseStatement() instanceof BlockTree) {
                deep.incrementDeep();
            }
            node.getElseStatement().accept(this, deep);
            if (node.getElseStatement() instanceof BlockTree) {
                deep.decrementDeep();
            }
        }
        return 0;
    }

    @Override
    public Integer visitWhileLoop(WhileLoopTree node, MaxIndentLevelRule2.DeepExplorer deep) {
        deep.incrementDeep();
        super.visitWhileLoop(node, deep);
        deep.decrementDeep();
        return 0;
    }

    @Override
    public Integer visitDoWhileLoop(DoWhileLoopTree node, MaxIndentLevelRule2.DeepExplorer deep) {
        deep.incrementDeep();
        super.visitDoWhileLoop(node, deep);
        deep.decrementDeep();
        return 0;
    }

    @Override
    public Integer visitForLoop(ForLoopTree node, MaxIndentLevelRule2.DeepExplorer deep) {
        deep.incrementDeep();
        super.visitForLoop(node, deep);
        deep.decrementDeep();
        return 0;
    }

    @Override
    public Integer visitEnhancedForLoop(EnhancedForLoopTree node, MaxIndentLevelRule2.DeepExplorer deep) {
        deep.incrementDeep();
        super.visitEnhancedForLoop(node, deep);
        deep.decrementDeep();
        return 0;
    }

    @Override
    public Integer visitCase(CaseTree node, MaxIndentLevelRule2.DeepExplorer deep) {
        deep.incrementDeep();
        super.visitCase(node, deep);
        deep.decrementDeep();
        return 0;
    }

    @Override
    public Integer visitTry(TryTree node, DeepExplorer deep) {
        deep.incrementDeep();
        super.visitTry(node, deep);
        deep.decrementDeep();
        return 0;
    }
}