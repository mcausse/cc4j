package org.homs.cc4j;

import com.sun.source.tree.CompilationUnitTree;
import com.sun.source.util.JavacTask;
import org.homs.cc4j.issue.IssuesReportJ;
import org.homs.cc4j.util.FileUtils;
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

    public IssuesReportJ analyseJavaFile(File file, Listener listener) throws IOException {
        return analyseJavaFiles(List.of(file), listener);
    }

    public IssuesReportJ analyseJavaFiles(List<File> files, Listener listener) throws IOException {

        final JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();

        try (final StandardJavaFileManager fileManager = compiler.getStandardFileManager(null, null, StandardCharsets.UTF_8)) {
            for (var file : files) {
                analizeFile(listener, compiler, fileManager, file);
            }
            listener.displayReport();
        }
        return listener.getIssuesReport();
    }

    void analizeFile(Listener listener, JavaCompiler compiler, StandardJavaFileManager fileManager, File file) throws IOException {

        String sourceCode = FileUtils.loadFile(file.toString());

        listener.verifyClassSourceCode(file.getName(), sourceCode);

        final Iterable<? extends JavaFileObject> compilationUnits = fileManager.getJavaFileObjectsFromFiles(List.of(file));

        final JavacTask javacTask = (JavacTask) compiler.getTask(null, fileManager, null, null, null, compilationUnits);
        final Iterable<? extends CompilationUnitTree> compilationUnitTrees = javacTask.parse();

        for (CompilationUnitTree compUnit : compilationUnitTrees) {

            compUnit.accept(new TooComplicatedConditionRule(listener, new Location(file.getName())), null);
            compUnit.accept(new TooManyArgumentsRule(listener, new Location(file.getName())), null);
            compUnit.accept(new TooManyMethodsRule(listener, new Location(file.getName())), null);
            compUnit.accept(new NamingConventionsRule(listener, new Location(file.getName())), null);
            compUnit.accept(new TooManyEffectiveLinesPerMethodRule(listener, new Location(file.getName())), null);
            compUnit.accept(new ClassMembersOrderingRule(listener, new Location(file.getName())), null);
            compUnit.accept(new MaxIndentLevelRule(listener, new Location(file.getName())), null);
            compUnit.accept(new UsePronounceableNamesRule(listener, new Location(file.getName())), null);
        }
    }

}

