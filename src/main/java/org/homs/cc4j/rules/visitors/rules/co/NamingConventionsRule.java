package org.homs.cc4j.rules.visitors.rules.co;

import com.sun.source.tree.ClassTree;
import com.sun.source.tree.MethodTree;
import com.sun.source.tree.Tree;
import com.sun.source.tree.VariableTree;
import org.homs.cc4j.RuleInfo;
import org.homs.cc4j.issue.IssueSeverity;
import org.homs.cc4j.rules.visitors.RuleTreeVisitor;

import javax.lang.model.element.Modifier;

public class NamingConventionsRule extends RuleTreeVisitor<Void> {

    static final String CONSTANT_PATTERN = "^[A-Z][A-Z0-9]*(_[A-Z0-9]+)*$";
    static final String VARIABLE_NAME_PATTERN = "^[a-z][a-zA-Z0-9]*$";
    static final String METHOD_NAME_PATTERN = "^[a-z][a-zA-Z0-9]*$";
    static final String CLASS_NAME_PATTERN = "^[A-Z][a-zA-Z0-9]*$";

    static final IssueSeverity SEVERITY = IssueSeverity.CRITICAL;

    @Override
    public RuleInfo getRuleInfo() {
        return new RuleInfo("co", 3,
                "Follow the naming conventions for classes, constants, properties, methods, and arguments. (" + SEVERITY.getName() + ")");
    }

    public Integer visitClass(ClassTree node, Void p) {

        var className = node.getSimpleName().toString();

        if (className.isEmpty()) {
            return super.visitClass(node, p);
        }

        location.push(node.getSimpleName().toString());

        if (!className.matches(CLASS_NAME_PATTERN)) {
            generateIssue(SEVERITY,
                    String.format("class '%s' should comply with a naming convention: %s",
                            className, CLASS_NAME_PATTERN));
        }

        for (Tree member : node.getMembers()) {
            if (member instanceof VariableTree) {
                checkProperty((VariableTree) member);
            } else if (member instanceof MethodTree) {
                checkMethod(node, ((MethodTree) member));
            }
        }

        location.pop();
        return super.visitClass(node, p);
    }

    void checkProperty(VariableTree property) {
        String propertyName = property.getName().toString();
        boolean isStatic = property.getModifiers().getFlags().contains(Modifier.STATIC);
        if (isStatic) {
            if (!propertyName.matches(CONSTANT_PATTERN)) {
                generateIssue(SEVERITY,
                        String.format("constant '%s' should comply with a naming convention: %s",
                                propertyName, CONSTANT_PATTERN));
            }
        } else {
            if (!propertyName.matches(VARIABLE_NAME_PATTERN)) {
                generateIssue(SEVERITY,
                        String.format("property '%s' should comply with a naming convention: %s",
                                propertyName, VARIABLE_NAME_PATTERN));
            }
        }
    }

    void checkMethod(ClassTree classTree, MethodTree methodTree) {
        //                /**
        //                 * Returns the return type of the method being declared.
        //                 * Returns {@code null} for a constructor.
        //                 * @return the return type
        //                 */
        //                Tree getReturnType();
        if (methodTree.getReturnType() == null) {
            // just skip the c'tors
            return;
        }
        var className = classTree.getSimpleName().toString();
        if (className.endsWith("Test") || className.endsWith("Should")) {
            // we ignore UT classes with methods like "fail_parsing_an_unclosed_list"
            // or "throw_an_exception_when_tokenizing_a_program_with_an_unclosed_multiline_string".
            return;
        }

        location.push(methodTree.getName().toString() + "(..)");

        String name = methodTree.getName().toString();
        if (!name.matches(METHOD_NAME_PATTERN)) {
            generateIssue(SEVERITY,
                    String.format("method '%s' should comply with a naming convention: %s",
                            name, METHOD_NAME_PATTERN));
        }
        checkMethodArguments(methodTree);

        location.pop();
    }

    void checkMethodArguments(MethodTree methodTree) {
        for (var paramTree : methodTree.getParameters()) {
            String paramName = paramTree.getName().toString();
            if (!paramName.matches(VARIABLE_NAME_PATTERN)) {
                generateIssue(SEVERITY,
                        String.format("parameter '%s' should comply with a naming convention: %s",
                                paramName, VARIABLE_NAME_PATTERN));
            }
        }
    }
}
