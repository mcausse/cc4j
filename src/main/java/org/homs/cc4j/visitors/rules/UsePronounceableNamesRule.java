package org.homs.cc4j.visitors.rules;

import com.sun.source.tree.ClassTree;
import com.sun.source.tree.MethodTree;
import com.sun.source.tree.Tree;
import com.sun.source.tree.VariableTree;
import org.homs.cc4j.Listener;
import org.homs.cc4j.Location;
import org.homs.cc4j.visitors.RuleTreeVisitor;

public class UsePronounceableNamesRule extends RuleTreeVisitor<Void> {

    public UsePronounceableNamesRule(Listener listener, Location location) {
        super(listener, location);
    }

    public Integer visitClass(ClassTree node, Void p) {

        var className = node.getSimpleName().toString();
//        if (!className.matches(CLASS_NAME_PATTERN)) {
//            generateIssue(IssueSeverity.CRITICAL, String.format("class '%s' should comply with a naming convention: %s", className, CLASS_NAME_PATTERN));
//        }
        pronounceableAnalysis(className);

        for (Tree member : node.getMembers()) {
            if (member instanceof VariableTree) {
                checkProperty((VariableTree) member);
            } else if (member instanceof MethodTree) {
                checkMethod(((MethodTree) member));
            } else if (member instanceof ClassTree) {
                member.accept(this, p);
            }
        }
        return 0;
    }

    void checkProperty(VariableTree property) {
        String propertyName = property.getName().toString();
//        boolean isStatic = property.getModifiers().getFlags().contains(Modifier.STATIC);
//        if (isStatic) {
//            if (!propertyName.matches(CONSTANT_PATTERN)) {
//                generateIssue(IssueSeverity.CRITICAL, String.format("constant '%s' should comply with a naming convention: %s", propertyName, CONSTANT_PATTERN));
//            }
//        } else {
//            if (!propertyName.matches(VARIABLE_NAME_PATTERN)) {
//                generateIssue(IssueSeverity.CRITICAL, String.format("property '%s' should comply with a naming convention: %s", propertyName, VARIABLE_NAME_PATTERN));
//            }
//        }
        pronounceableAnalysis(propertyName);
    }

    void checkMethod(MethodTree methodTree) {
        //                /**
        //                 * Returns the return type of the method being declared.
        //                 * Returns {@code null} for a constructor.
        //                 * @return the return type
        //                 */
        //                Tree getReturnType();
        if (methodTree.getReturnType() == null) {
            // we skip c'tors
            return;
        }
        String methodName = methodTree.getName().toString();
        pronounceableAnalysis(methodName);
//        if (!name.matches(METHOD_NAME_PATTERN)) {
//            generateIssue(IssueSeverity.CRITICAL, String.format("method '%s' should comply with a naming convention: %s", name, METHOD_NAME_PATTERN));
//        }
        checkMethodArguments(methodTree);
    }

    void checkMethodArguments(MethodTree methodTree) {
        for (var paramTree : methodTree.getParameters()) {
            String paramName = paramTree.getName().toString();
//            if (!paramName.matches(VARIABLE_NAME_PATTERN)) {
//                generateIssue(IssueSeverity.CRITICAL, String.format("parameter '%s' should comply with a naming convention: %s", paramName, VARIABLE_NAME_PATTERN));
//            }
            pronounceableAnalysis(paramName);
        }
    }


    void pronounceableAnalysis(String name) {
        int consonants = consonantAnalysis(name);
        generateIssueIfThreshold("use pronounceable names: '" + name + "' scored with %s (>%s warning, >%s critical, >%s error)",
                consonants, 4, 5, 6);
    }

    int consonantAnalysis(String name) {
        String vowels = "aeiouAEIOUwyWY_";

        int maxRun = 0;
        int run = 0;
        char last = 'a';
        for (char c : name.toCharArray()) {
            if (vowels.indexOf(c) >= 0) {
                run = 0;
            } else if (Character.isLowerCase(last) != Character.isLowerCase(c)) {
                run = 1;
            } else {
                run++;
            }

            if (maxRun < run) {
                maxRun = run;
            }

            last = c;
        }
        return maxRun;
    }
}
