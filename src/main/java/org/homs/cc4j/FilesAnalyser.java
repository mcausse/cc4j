package org.homs.cc4j;

import com.sun.source.tree.CompilationUnitTree;
import com.sun.source.util.JavacTask;
import org.homs.cc4j.issue.IssuesReport;
import org.homs.cc4j.issue.Location;
import org.homs.cc4j.rules.visitors.Java19MetricsTreeVisitor;
import org.homs.cc4j.rules.visitors.RuleTreeVisitor;

import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.function.Consumer;

public class FilesAnalyser {

    public static FilesAnalyser forFiles(List<File> files) {
        final JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        Map<File, ArrayList<CompilationUnitTree>> r = new LinkedHashMap<>();

        try (final StandardJavaFileManager fileManager = compiler.getStandardFileManager(null, null, StandardCharsets.UTF_8)) {
            for (var file : files) {
                final Iterable<? extends JavaFileObject> compilationUnits = fileManager.getJavaFileObjectsFromFiles(List.of(file));
                final JavacTask javacTask = (JavacTask) compiler.getTask(null, fileManager, null, null, null, compilationUnits);
                final Iterable<? extends CompilationUnitTree> compilationUnitTrees = javacTask.parse();

                r.putIfAbsent(file, new ArrayList<>());
                for (var cut : compilationUnitTrees) {
                    r.get(file).add(cut);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return new FilesAnalyser(r);
    }

    public static FilesAnalyser forFile(File file) {
        return forFiles(List.of(file));
    }

    final Map<File, ArrayList<CompilationUnitTree>> compilationUnitTrees;

    protected FilesAnalyser(Map<File, ArrayList<CompilationUnitTree>> compilationUnitTrees) {
        this.compilationUnitTrees = compilationUnitTrees;
    }

    public void forEachFile(Consumer<File> f) {
        this.compilationUnitTrees.keySet().forEach(f);
    }

    public void acceptRuleVisitors(IssuesReport issuesReport, Collection<RuleTreeVisitor<?>> ruleVisitors) {
        for (var ruleVisitor : ruleVisitors) {
            for (File file : this.compilationUnitTrees.keySet()) {
                String fileName = file.getName();
                for (CompilationUnitTree compUnit : this.compilationUnitTrees.get(file)) {
                    ruleVisitor.setIssuesReport(issuesReport);
                    ruleVisitor.setLocation(new Location(fileName));
                    compUnit.accept(ruleVisitor, null);
                }
            }
        }
    }

    public void acceptVisitor(Java19MetricsTreeVisitor<?> visitor) {
        for (var entry : this.compilationUnitTrees.entrySet()) {
            for (CompilationUnitTree compUnit : entry.getValue()) {
                compUnit.accept(visitor, null);
            }
        }
    }
}
