//package org.homs.cc4j.rules.visitors.rules.cc;
//
//import com.sun.source.tree.*;
//import org.homs.cc4j.RuleInfo;
//import org.homs.cc4j.issue.Thresholds;
//import org.homs.cc4j.rules.visitors.RuleTreeVisitor;
//
//import java.util.List;
//
//public class MaxIndentLevelRule extends RuleTreeVisitor<Void> {
//
//    static final Thresholds THRESHOLDS = new Thresholds(3, 4, 5);
//
//    @Override
//    public RuleInfo getRuleInfo() {
//        return new RuleInfo("cc", 5, "Avoid too deply nested code (hadookens). (" + THRESHOLDS + ")");
//    }
//
//    @Override
//    public Integer visitMethod(MethodTree node, Void p) {
//        location.push(node.getName().toString() + "(..)");
//        analizeBlockTree(node.getBody());
//        location.pop();
//        return 0;
//    }
//
//    @Override
//    public int visitClassInitializer(BlockTree classInitializer, Void unused) {
//        location.push("{}");
//        analizeBlockTree(classInitializer);
//        location.pop();
//        return 0;
//    }
//
//    private void analizeBlockTree(BlockTree block) {
//        if (block != null) {
//            int indentMaxLevel = inspectStatements(block.getStatements(), 0);
//            generateIssueIfThreshold("%s indent levels (%s)", indentMaxLevel, THRESHOLDS);
//        }
//    }
//
//    int inspectStatements(List<? extends StatementTree> stms, int level) {
//        int maxlevel = 0;
//        for (StatementTree stm : stms) {
//            int maxlocallevel = inspectStatement(stm, level + 1);
//            if (maxlevel < maxlocallevel) {
//                maxlevel = maxlocallevel;
//            }
//        }
//        return maxlevel;
//    }
//
//    int inspectStatement(StatementTree stm, int level) {
//        if (stm instanceof BlockTree) {
//            return inspectStatements(((BlockTree) stm).getStatements(), level);
//        } else if (stm instanceof DoWhileLoopTree) {
//            return inspectStatement(((DoWhileLoopTree) stm).getStatement(), level);
//        } else if (stm instanceof ForLoopTree) {
//            return inspectStatement(((ForLoopTree) stm).getStatement(), level);
//        } else if (stm instanceof EnhancedForLoopTree) {
//            return inspectStatement(((EnhancedForLoopTree) stm).getStatement(), level);
//        } else if (stm instanceof IfTree) {
//            return Math.max(
//                    inspectStatement(((IfTree) stm).getThenStatement(), level),
//                    inspectStatement(((IfTree) stm).getElseStatement(), level));
//        } else
////        if (stm instanceof LambdaExpressionTree) {
////            inspectStatement(issuesReport, location, ((LambdaExpressionTree) stm).getBody().);
////        }else
//            if (stm instanceof SwitchTree) {
//                return inspectSwitchTree((SwitchTree) stm, level);
//            } else if (stm instanceof TryTree) {
//                return inspectTryTree(level, (TryTree) stm);
//            } else if (stm instanceof WhileLoopTree) {
//                return inspectStatement(((WhileLoopTree) stm).getStatement(), level);
//            } else {
//                // XXX és l'indent level fulla, però no té pq ser el més fondo!!!
//                return level;
//            }
//    }
//
//    private int inspectSwitchTree(SwitchTree stm, int level) {
//        var cases = stm.getCases();
//        int localLevel = level;
//        for (var casee : cases) {
//            if (casee.getStatements() != null) {
//                int caseMaxLevel = inspectStatements(casee.getStatements(), level);
//                if (localLevel < caseMaxLevel) {
//                    localLevel = caseMaxLevel;
//                }
//            }
//        }
//        return localLevel;
//    }
//
//    private int inspectTryTree(int level, TryTree stmTree) {
//        int maxLevel = inspectStatements(stmTree.getBlock().getStatements(), level);
//        for (CatchTree catchTree : stmTree.getCatches()) {
//            int catchMaxLevel = inspectStatement(catchTree.getBlock(), level);
//            if (maxLevel < catchMaxLevel) {
//                maxLevel = catchMaxLevel;
//            }
//        }
//        if (stmTree.getFinallyBlock() != null) {
//            int finallyMaxLevel = inspectStatements(stmTree.getFinallyBlock().getStatements(), level);
//            if (maxLevel < finallyMaxLevel) {
//                maxLevel = finallyMaxLevel;
//            }
//        }
//        return maxLevel;
//    }
//
//}
