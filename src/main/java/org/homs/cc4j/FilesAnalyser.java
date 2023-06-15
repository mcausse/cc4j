package org.homs.cc4j;

import com.sun.source.tree.CompilationUnitTree;
import com.sun.source.util.JavacTask;
import org.homs.cc4j.issue.IssuesReport;
import org.homs.cc4j.issue.Location;
import org.homs.cc4j.visitors.Java19MetricsTreeVisitor;
import org.homs.cc4j.visitors.RuleTreeVisitor;

import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;

public class FilesAnalyser {

    public static FilesAnalyser forFiles(List<File> files) {
        final JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();

        List<CompilationUnitTree> r = new ArrayList<>();

        try (final StandardJavaFileManager fileManager = compiler.getStandardFileManager(null, null, StandardCharsets.UTF_8)) {
            for (var file : files) {
                final Iterable<? extends JavaFileObject> compilationUnits = fileManager.getJavaFileObjectsFromFiles(List.of(file));
                final JavacTask javacTask = (JavacTask) compiler.getTask(null, fileManager, null, null, null, compilationUnits);
                final Iterable<? extends CompilationUnitTree> compilationUnitTrees = javacTask.parse();

                for (var cut : compilationUnitTrees) {
                    r.add(cut);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return new FilesAnalyser(files, r);
    }

    public static FilesAnalyser forFile(File file) {
        return forFiles(List.of(file));
    }

    final List<File> files;
    final List<CompilationUnitTree> compilationUnitTrees;

    public FilesAnalyser(List<File> files, List<CompilationUnitTree> compilationUnitTrees) {
        this.files = files;
        this.compilationUnitTrees = compilationUnitTrees;
    }

    public void forEachFile(Consumer<File> f) {
        this.files.forEach(f);
    }

    public void acceptRuleVisitors(IssuesReport issuesReport, Collection<RuleTreeVisitor<?>> ruleVisitors) {
        for (var ruleVisitor : ruleVisitors) {
            for (CompilationUnitTree compUnit : this.compilationUnitTrees) {
                ruleVisitor.setIssuesReport(issuesReport);
                ruleVisitor.setLocation(new Location(compUnit.getSourceFile().getName()));
                compUnit.accept(ruleVisitor, null);
            }
        }
    }

    public void acceptVisitor(Java19MetricsTreeVisitor<?> visitor) {
        for (CompilationUnitTree compUnit : this.compilationUnitTrees) {
            compUnit.accept(visitor, null);
        }
    }
}
