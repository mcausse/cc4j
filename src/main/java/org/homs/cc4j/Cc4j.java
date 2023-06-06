package org.homs.cc4j;

import com.sun.source.tree.*;
import com.sun.source.util.JavacTask;
import org.homs.cc4j.issue.IssuesReportJ;
import org.homs.cc4j.util.FileUtils;
import org.homs.cc4j.visitors.TooComplicatedConditionTreeVisitor;

import javax.lang.model.element.Modifier;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
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

//            var vv=new Java19TreeVisitor();
//            compUnit.accept(vv,null);

            var vv2 = new TooComplicatedConditionTreeVisitor(listener);
            compUnit.accept(vv2, null);

            analizeCompilationUnit(listener, file, compUnit);
        }
    }

    void analizeCompilationUnit(Listener listener, File file, CompilationUnitTree compUnit) {
        var location = new Location(file.getName());

        for (Tree typeDeclaration : compUnit.getTypeDecls()) {
            if (typeDeclaration instanceof ClassTree) {
                inspectClass(listener, location, (ClassTree) typeDeclaration);
                inspectClassMembers(listener, location, (ClassTree) typeDeclaration);
            }
        }
    }

    void inspectClass(Listener listener, Location location, ClassTree classDecl) {
        location.push(classDecl.getSimpleName().toString());

        listener.checkClassName(location, classDecl.getSimpleName().toString());

        int constantsCount = 0;
        int propertiesCount = 0;
        int methodsCount = 0;
        int innerClassesCount = 0;
        for (Tree member : classDecl.getMembers()) {
            if (member instanceof VariableTree) {
                var var = (VariableTree) member;
                boolean isStatic = var.getModifiers().getFlags().contains(Modifier.STATIC);
                if (isStatic) {
                    inspectConstant(listener, location, var);
                    constantsCount++;
                } else {
                    inspectProperty(listener, location, var);
                    propertiesCount++;
                }
            } else if (member instanceof MethodTree) {
                inspectMethod(listener, location, (MethodTree) member);
                methodsCount++;
            } else if (member instanceof ClassTree) {
                inspectClass(listener, location, (ClassTree) member);
                innerClassesCount++;
            }
        }

        listener.onClassMembersCount(location, constantsCount, propertiesCount, methodsCount, innerClassesCount);

        location.pop();
    }

    public enum Member {
        LOGGER(1), STATIC(2), PROPERTY(3), CTOR(4), METHOD(5), EQUALS_HASHCODE(6), TOSTRING(7);

        final int order;

        Member(int order) {
            this.order = order;
        }

        public int getOrder() {
            return order;
        }
    }

    void inspectClassMembers(Listener listener, Location location, ClassTree classDecl) {
        location.push(classDecl.getSimpleName().toString());

        List<Member> r = new ArrayList<>();
        for (Tree member : classDecl.getMembers()) {
            if (member instanceof VariableTree) {
                var var = (VariableTree) member;
                boolean isStatic = var.getModifiers().getFlags().contains(Modifier.STATIC);
                var name = var.getName().toString();
                if (name.equalsIgnoreCase("log") || name.equalsIgnoreCase("logger")) {
                    r.add(Member.LOGGER);
                } else {
                    if (isStatic) {
                        r.add(Member.STATIC);
                    } else {
                        r.add(Member.PROPERTY);
                    }
                }
            } else if (member instanceof MethodTree) {
                var method = (MethodTree) member;

                //                /**
                //                 * Returns the return type of the method being declared.
                //                 * Returns {@code null} for a constructor.
                //                 * @return the return type
                //                 */
                //                Tree getReturnType();

                var name = method.getName().toString();
                if (method.getReturnType() == null) {
                    r.add(Member.CTOR);
                } else if (name.equals("equals") && method.getParameters().size() == 1 && method.getReturnType().toString().equals("boolean")) {
                    r.add(Member.EQUALS_HASHCODE);
                } else if (name.equals("hashCode") && method.getParameters().isEmpty() && method.getReturnType().toString().equals("int")) {
                    r.add(Member.EQUALS_HASHCODE);
                } else if (name.equals("toString") && method.getParameters().isEmpty() && method.getReturnType().toString().equals("String")) {
                    r.add(Member.TOSTRING);
                } else {
                    r.add(Member.METHOD);
                }
            } else if (member instanceof ClassTree) {
            }
        }

        listener.onClassMembersOrder(location, r);

        location.pop();
    }

    void inspectMethod(Listener listener, Location location, MethodTree method) {

        final String methodName = method.getName().toString();
        location.push(methodName + "(..)");

        if (method.getReturnType() != null) {
            // is not a C'tor
            listener.checkMethodName(location, methodName);
        }

        for (var parameter : method.getParameters()) {
            listener.checkArgumentName(location, parameter.getName().toString());
        }

        var argumentsCount = method.getParameters().size();
        final int linesOfEffectiveCode;
        if (method.getBody() == null) {
            linesOfEffectiveCode = 0;
        } else {
            linesOfEffectiveCode = method.getBody().toString().split("\\n").length - 2 /* { & } */;
        }
        listener.checkMethodMetrics(location, argumentsCount, linesOfEffectiveCode);

        if (method.getBody() != null) {
            int indentMaxLevel = inspectStatements(listener, location, method.getBody().getStatements(), 0);
            listener.onMethodMaxIndentLevel(location, indentMaxLevel);
        }

        location.pop();
    }

    int inspectStatements(Listener listener, Location location, List<? extends StatementTree> stms, int level) {
        int maxlevel = 0;
        for (StatementTree stm : stms) {
            int maxlocallevel = inspectStatement(listener, location, stm, level + 1);
            if (maxlevel < maxlocallevel) {
                maxlevel = maxlocallevel;
            }
        }
        return maxlevel;
    }

    int inspectStatement(Listener listener, Location location, StatementTree stm, int level) {
        if (stm instanceof BlockTree) {
            return inspectStatements(listener, location, ((BlockTree) stm).getStatements(), level);
        } else if (stm instanceof DoWhileLoopTree) {
            return inspectStatement(listener, location, ((DoWhileLoopTree) stm).getStatement(), level);
        } else if (stm instanceof ForLoopTree) {
            return inspectStatement(listener, location, ((ForLoopTree) stm).getStatement(), level);
        } else if (stm instanceof EnhancedForLoopTree) {
            return inspectStatement(listener, location, ((EnhancedForLoopTree) stm).getStatement(), level);
        } else if (stm instanceof IfTree) {
            return Math.max(
                    inspectStatement(listener, location, ((IfTree) stm).getThenStatement(), level),
                    inspectStatement(listener, location, ((IfTree) stm).getElseStatement(), level));
        } else
//        if (stm instanceof LambdaExpressionTree) {
//            inspectStatement(issuesReport, location, ((LambdaExpressionTree) stm).getBody().);
//        }else
            if (stm instanceof SwitchTree) {
                var cases = ((SwitchTree) stm).getCases();
                int localLevel = level;
                for (var casee : cases) {
                    int caseMaxLevel = inspectStatements(listener, location, casee.getStatements(), level);
                    if (localLevel < caseMaxLevel) {
                        localLevel = caseMaxLevel;
                    }
                }
                return localLevel;
            } else if (stm instanceof TryTree) {
                var stmTree = (TryTree) stm;
                int maxLevel = inspectStatements(listener, location, stmTree.getBlock().getStatements(), level);
                for (CatchTree catchTree : stmTree.getCatches()) {
                    int catchMaxLevel = inspectStatement(listener, location, catchTree.getBlock(), level);
                    if (maxLevel < catchMaxLevel) {
                        maxLevel = catchMaxLevel;
                    }
                }
                if (stmTree.getFinallyBlock() != null) {
                    int finallyMaxLevel = inspectStatements(listener, location, stmTree.getFinallyBlock().getStatements(), level);
                    if (maxLevel < finallyMaxLevel) {
                        maxLevel = finallyMaxLevel;
                    }
                }
                return maxLevel;
            } else if (stm instanceof WhileLoopTree) {
                return inspectStatement(listener, location, ((WhileLoopTree) stm).getStatement(), level);
            } else {
                // XXX és l'indent level fulla, però no té pq ser el més fondo!!!
                return level;
            }
    }

    void inspectConstant(Listener listener, Location location, VariableTree property) {
        location.push(property.getName().toString());
        listener.checkConstantName(location, property.getName().toString());
        location.pop();
    }

    void inspectProperty(Listener listener, Location location, VariableTree property) {
        location.push(property.getName().toString());
        listener.checkPropertyName(location, property.getName().toString());
        location.pop();
    }

}

