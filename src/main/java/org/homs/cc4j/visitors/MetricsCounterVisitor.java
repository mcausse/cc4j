package org.homs.cc4j.visitors;

import com.sun.source.tree.ClassTree;
import com.sun.source.tree.CompilationUnitTree;
import com.sun.source.tree.MethodTree;
import org.homs.cc4j.visitors.rules.cc.TooManyEffectiveLinesPerMethodRule;

import java.util.LinkedHashSet;
import java.util.Set;

public class MetricsCounterVisitor extends Java19MetricsTreeVisitor<String> {

    final Set<String> classes = new LinkedHashSet<>();
    final Set<String> methods = new LinkedHashSet<>();
    int totalEffectiveLinesOfCode = 0;

    public Integer visitCompilationUnit(CompilationUnitTree node, String p) {
        final String packageName;
        if (node.getPackageName() == null) {
            packageName = "<nopackage>";
        } else {
            packageName = node.getPackageName().toString();
        }
        return super.visitCompilationUnit(node, packageName);
    }

    public Integer visitClass(ClassTree node, String packageName) {
        String className = packageName + "." + node.getSimpleName().toString();
        classes.add(className);
        return super.visitClass(node, className);
    }

    public Integer visitMethod(MethodTree node, String fullyQualifiedClassName) {
        var methodName = fullyQualifiedClassName + "#" + node.getName().toString();
        if (!methods.contains(methodName)) {
            methods.add(methodName);
            this.totalEffectiveLinesOfCode += TooManyEffectiveLinesPerMethodRule.getLinesOfEffectiveCode(node);
        }
        return super.visitMethod(node, methodName);
    }

    public void printMetricsCount(/*IssuesReport issuesReport*/) {
        System.out.println("=============================================================");
        System.out.println("Inspected classes:");
        System.out.println("-------------------------------------------------------------");
        for (var className : classes) {
            System.out.println("- " + className);
        }
        System.out.println("-------------------------------------------------------------");
        System.out.printf(" #classes: %7d%n", classes.size());
        System.out.printf(" #methods: %7d (%5.2f methods/class)%n", methods.size(), (double) methods.size() / classes.size());
        System.out.printf(" #elc:     %7d (%5.2f elc/class)%n", totalEffectiveLinesOfCode, (double) totalEffectiveLinesOfCode / classes.size());
        System.out.println("-------------------------------------------------------------");
//        if (totalEffectiveLinesOfCode > 0) {
//            System.out.printf(" errors/Kelc:    %3d%n", 1_000 * issuesReport.getIssuesCountBySeverity(IssueSeverity.ERROR) / totalEffectiveLinesOfCode);
//            System.out.printf(" criticals/Kelc: %3d%n", 1_000 * issuesReport.getIssuesCountBySeverity(IssueSeverity.CRITICAL) / totalEffectiveLinesOfCode);
//            System.out.printf(" warnings/Kelc:  %3d%n", 1_000 * issuesReport.getIssuesCountBySeverity(IssueSeverity.WARNING) / totalEffectiveLinesOfCode);
//        }
        System.out.println("=============================================================");
    }

}
