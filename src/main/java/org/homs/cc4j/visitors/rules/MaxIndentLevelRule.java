package org.homs.cc4j.visitors.rules;

import com.sun.source.tree.*;
import org.homs.cc4j.visitors.RuleTreeVisitor;

import java.util.List;

public class MaxIndentLevelRule extends RuleTreeVisitor<Void> {

    static int THR_ERROR = 5;
    static int THR_CRITICAL = 4;
    static int THR_WARNING = 3;

    public Integer visitMethod(MethodTree node, Void p) {
        location.push(node.getName().toString() + "(..)");

        if (node.getBody() != null) {
            int indentMaxLevel = inspectStatements(node.getBody().getStatements(), 0);

            generateIssueIfThreshold("%s indent levels (>%s warning, >%s critical, >%s error)",
                    indentMaxLevel, THR_WARNING, THR_CRITICAL, THR_ERROR);
        }

        location.pop();
        return 0;
    }

    int inspectStatements(List<? extends StatementTree> stms, int level) {
        int maxlevel = 0;
        for (StatementTree stm : stms) {
            int maxlocallevel = inspectStatement(stm, level + 1);
            if (maxlevel < maxlocallevel) {
                maxlevel = maxlocallevel;
            }
        }
        return maxlevel;
    }

    int inspectStatement(StatementTree stm, int level) {
        if (stm instanceof BlockTree) {
            return inspectStatements(((BlockTree) stm).getStatements(), level);
        } else if (stm instanceof DoWhileLoopTree) {
            return inspectStatement(((DoWhileLoopTree) stm).getStatement(), level);
        } else if (stm instanceof ForLoopTree) {
            return inspectStatement(((ForLoopTree) stm).getStatement(), level);
        } else if (stm instanceof EnhancedForLoopTree) {
            return inspectStatement(((EnhancedForLoopTree) stm).getStatement(), level);
        } else if (stm instanceof IfTree) {
            return Math.max(
                    inspectStatement(((IfTree) stm).getThenStatement(), level),
                    inspectStatement(((IfTree) stm).getElseStatement(), level));
        } else
//        if (stm instanceof LambdaExpressionTree) {
//            inspectStatement(issuesReport, location, ((LambdaExpressionTree) stm).getBody().);
//        }else
            if (stm instanceof SwitchTree) {
                var cases = ((SwitchTree) stm).getCases();
                int localLevel = level;
                for (var casee : cases) {
                    int caseMaxLevel = inspectStatements(casee.getStatements(), level);
                    if (localLevel < caseMaxLevel) {
                        localLevel = caseMaxLevel;
                    }
                }
                return localLevel;
            } else if (stm instanceof TryTree stmTree) {
                int maxLevel = inspectStatements(stmTree.getBlock().getStatements(), level);
                for (CatchTree catchTree : stmTree.getCatches()) {
                    int catchMaxLevel = inspectStatement(catchTree.getBlock(), level);
                    if (maxLevel < catchMaxLevel) {
                        maxLevel = catchMaxLevel;
                    }
                }
                if (stmTree.getFinallyBlock() != null) {
                    int finallyMaxLevel = inspectStatements(stmTree.getFinallyBlock().getStatements(), level);
                    if (maxLevel < finallyMaxLevel) {
                        maxLevel = finallyMaxLevel;
                    }
                }
                return maxLevel;
            } else if (stm instanceof WhileLoopTree) {
                return inspectStatement(((WhileLoopTree) stm).getStatement(), level);
            } else {
                // XXX és l'indent level fulla, però no té pq ser el més fondo!!!
                return level;
            }
    }

}
