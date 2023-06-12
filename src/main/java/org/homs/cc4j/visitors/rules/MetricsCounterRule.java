//package org.homs.cc4j.visitors.rules;
//
//import com.sun.source.tree.ClassTree;
//import com.sun.source.tree.MethodTree;
//import org.homs.cc4j.visitors.RuleTreeVisitor;
//
//public class MetricsCounterRule extends RuleTreeVisitor<Void> {
//
//    int numClasses = 0;
//    int numMethods = 0;
//    int numEffectiveLines = 0;
//
//    public Integer visitClass(ClassTree node, Void p) {
//        numClasses++;
//        return super.visitClass(node, p);
//    }
//
//    public Integer visitMethod(MethodTree node, Void p) {
//        numMethods++;
//        numEffectiveLines += TooManyEffectiveLinesPerMethodRule.getLinesOfEffectiveCode(node);
//        return super.visitMethod(node, p);
//    }
//
//    public void printMetricsCount() {
//        System.out.println("======================================================");
//        System.out.printf("# classes: %4d%n", numClasses);
//        System.out.printf("# methods: %4d (%4.2f methods/class)%n", numMethods, (double)numMethods/numClasses);
//        System.out.printf("# elc:     %4d (%4.2f elc/class)%n", numEffectiveLines, (double)numEffectiveLines/numClasses);
//        System.out.println("======================================================");
//    }
//
//}
