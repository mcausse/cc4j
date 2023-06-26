package org.homs.cc4j.rules.visitors;

import com.sun.source.tree.ClassTree;
import com.sun.source.tree.CompilationUnitTree;
import com.sun.source.tree.MethodTree;
import org.homs.cc4j.issue.IssueSeverity;
import org.homs.cc4j.issue.IssuesReport;
import org.homs.cc4j.rules.visitors.rules.cc.TooManyEffectiveLinesPerMethodRule;

import java.util.LinkedHashSet;
import java.util.Set;

public class MetricsCounterVisitor extends Java19MetricsTreeVisitor<String> {

    int compilationUnits = 0;
    final Set<String> classes = new LinkedHashSet<>();
    final Set<String> methods = new LinkedHashSet<>();
    int totalEffectiveLinesOfCode = 0;

    @Override
    public Integer visitCompilationUnit(CompilationUnitTree node, String p) {
        final String packageName;
        if (node.getPackageName() == null) {
            packageName = "<nopackage>";
        } else {
            packageName = node.getPackageName().toString();
        }
        compilationUnits++;
        return super.visitCompilationUnit(node, packageName);
    }

    @Override
    public Integer visitClass(ClassTree node, String packageName) {
        var simpleClassName = node.getSimpleName().toString();
        if (simpleClassName.isEmpty()) {
            simpleClassName = "<anonymous>";
        }
        String className = packageName + "." + simpleClassName;
        classes.add(className);
        return super.visitClass(node, className);
    }

    @Override
    public Integer visitMethod(MethodTree node, String fullyQualifiedClassName) {
        var methodName = fullyQualifiedClassName + "#" + node.getName().toString();
        if (!methods.contains(methodName)) {
            methods.add(methodName);
            this.totalEffectiveLinesOfCode += TooManyEffectiveLinesPerMethodRule.getLinesOfEffectiveCode(node);
        }
        return super.visitMethod(node, methodName);
    }

    public void printMetricsCount(IssuesReport issuesReport) {
        System.out.println("=============================================================");
        System.out.println("Inspected classes:");
        System.out.println("-------------------------------------------------------------");
        for (var className : classes) {
            System.out.println("- " + className);
        }
        // TODO aixo es una mierda
        System.out.println("-------------------------------------------------------------");
        System.out.printf(" #files:   %7d%n", compilationUnits);
        System.out.printf(" #classes: %7d%n", classes.size());
        System.out.printf(" #methods: %7d (%5.2f methods/file)%n", methods.size(), (double) methods.size() / compilationUnits);
        System.out.printf(" #elc:     %7d (%5.2f elc/file)%n", totalEffectiveLinesOfCode, (double) totalEffectiveLinesOfCode / compilationUnits);
        System.out.println("-------------------------------------------------------------");
        if (totalEffectiveLinesOfCode > 0) {
            int errors = issuesReport.getIssuesCountBySeverity(IssueSeverity.ERROR);
            int criticals = issuesReport.getIssuesCountBySeverity(IssueSeverity.CRITICAL);
            int warnings = issuesReport.getIssuesCountBySeverity(IssueSeverity.WARNING);
            System.out.printf(" errors/Kelc:    %5.2f%n", 1_000.0 * errors / totalEffectiveLinesOfCode);
            System.out.printf(" criticals/Kelc: %5.2f%n", 1_000.0 * criticals / totalEffectiveLinesOfCode);
            System.out.printf(" warnings/Kelc:  %5.2f%n", 1_000.0 * warnings / totalEffectiveLinesOfCode);
            System.out.println("-------------------------------------------------------------");
            System.out.printf(" hits/Kelc:  %5.2f%n", 1_000.0 * (errors + criticals + warnings) / totalEffectiveLinesOfCode);
            System.out.println("-------------------------------------------------------------");
            System.out.printf(" technical debt score:  %5.2f%n", 1_000.0 * (errors * 3 + criticals * 2 + warnings) / totalEffectiveLinesOfCode);
            System.out.println("-------------------------------------------------------------");
        }
    }

}
