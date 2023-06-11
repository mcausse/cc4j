package org.homs.cc4j;

import com.sun.source.tree.CompilationUnitTree;
import com.sun.source.util.JavacTask;
import org.homs.cc4j.issue.IssuesReport;
import org.homs.cc4j.issue.Location;
import org.homs.cc4j.issue.SimpleIssuesReportVisitor;
import org.homs.cc4j.util.FileUtils;
import org.homs.cc4j.visitors.RuleTreeVisitor;
import org.homs.cc4j.visitors.rules.*;

import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class Cc4j {

    static {
        System.out.println("=============================================================");
        System.out.println("cc4j - 0.0.1");
        System.out.println("@see https://code.roche.com/homscaum/cc4j");
        System.out.println("=============================================================");
    }

    final IssuesReport issuesReport;

    public Cc4j(IssuesReport issuesReport) {
        this.issuesReport = issuesReport;
    }

    public void analyseJavaFile(File file) throws IOException {
        analyseJavaFiles(List.of(file));
    }

    public void analyseJavaFiles(List<File> files) throws IOException {

        final JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();

        try (final StandardJavaFileManager fileManager = compiler.getStandardFileManager(null, null, StandardCharsets.UTF_8)) {
            for (var file : files) {
                analizeFile(compiler, fileManager, file);
            }
            issuesReport.acceptReportVisitor(new SimpleIssuesReportVisitor());
        }
    }

    void analizeFile(JavaCompiler compiler, StandardJavaFileManager fileManager, File file) throws IOException {

        {
            String sourceCode = FileUtils.loadFile(file.toString());

            final FileRules fileRules = new FileRules(issuesReport);
            fileRules.checkTodosAndFixmes(file.getName(), sourceCode);
            fileRules.checkClassMaxLineWidth(file.getName(), sourceCode);
            fileRules.checkClassMaxNumberOfLines(file.getName(), sourceCode);
            fileRules.checkForAddSpacesToIncreaseReadibility(file.getName(), sourceCode);
        }

        final Iterable<? extends JavaFileObject> compilationUnits = fileManager.getJavaFileObjectsFromFiles(List.of(file));

        final JavacTask javacTask = (JavacTask) compiler.getTask(null, fileManager, null, null, null, compilationUnits);
        final Iterable<? extends CompilationUnitTree> compilationUnitTrees = javacTask.parse();

        List<RuleTreeVisitor<?>> rules = List.of(
                new ClassMembersOrderingRule(),
                new MaxIndentLevelRule(),
                new NamingConventionsRule(),
                new TooComplicatedConditionRule(),
                new TooManyArgumentsRule(),
                new TooManyEffectiveLinesPerMethodRule(),
                new TooManyMethodsRule(),
                new UsePronounceableNamesRule()
        );

        for (CompilationUnitTree compUnit : compilationUnitTrees) {
            for (var rule : rules) {
                rule.setIssuesReport(issuesReport);
                rule.setLocation(new Location(file.getName()));

                compUnit.accept(rule, null);
            }
        }
    }

}

