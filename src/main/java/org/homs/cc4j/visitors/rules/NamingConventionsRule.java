package org.homs.cc4j.visitors.rules;

import com.sun.source.tree.ClassTree;
import com.sun.source.tree.MethodTree;
import com.sun.source.tree.Tree;
import com.sun.source.tree.VariableTree;
import org.homs.cc4j.issue.IssueSeverity;
import org.homs.cc4j.visitors.RuleTreeVisitor;

import javax.lang.model.element.Modifier;

public class NamingConventionsRule extends RuleTreeVisitor<Void> {

    static final String CONSTANT_PATTERN = "^[A-Z][A-Z0-9]*(_[A-Z0-9]+)*$";
    static final String VARIABLE_NAME_PATTERN = "^[a-z][a-zA-Z0-9]*$";
    static final String METHOD_NAME_PATTERN = "^[a-z][a-zA-Z0-9]*$";
    static final String CLASS_NAME_PATTERN = "^[A-Z][a-zA-Z0-9]*$";

    public Integer visitClass(ClassTree node, Void p) {
        location.push(node.getSimpleName().toString());

        var className = node.getSimpleName().toString();
        if (!className.matches(CLASS_NAME_PATTERN)) {
            generateIssue(IssueSeverity.CRITICAL, String.format("class '%s' should comply with a naming convention: %s", className, CLASS_NAME_PATTERN));
        }

        for (Tree member : node.getMembers()) {
            if (member instanceof VariableTree) {
                checkProperty((VariableTree) member);
            } else if (member instanceof MethodTree) {
                checkMethod(((MethodTree) member));
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
                generateIssue(IssueSeverity.CRITICAL, String.format("constant '%s' should comply with a naming convention: %s", propertyName, CONSTANT_PATTERN));
            }
        } else {
            if (!propertyName.matches(VARIABLE_NAME_PATTERN)) {
                generateIssue(IssueSeverity.CRITICAL, String.format("property '%s' should comply with a naming convention: %s", propertyName, VARIABLE_NAME_PATTERN));
            }
        }
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
        location.push(methodTree.getName().toString() + "(..)");

        String name = methodTree.getName().toString();
        if (!name.matches(METHOD_NAME_PATTERN)) {
            generateIssue(IssueSeverity.CRITICAL, String.format("method '%s' should comply with a naming convention: %s", name, METHOD_NAME_PATTERN));
        }
        checkMethodArguments(methodTree);

        location.pop();
    }

    void checkMethodArguments(MethodTree methodTree) {
        for (var paramTree : methodTree.getParameters()) {
            String paramName = paramTree.getName().toString();
            if (!paramName.matches(VARIABLE_NAME_PATTERN)) {
                generateIssue(IssueSeverity.CRITICAL, String.format("parameter '%s' should comply with a naming convention: %s", paramName, VARIABLE_NAME_PATTERN));
            }
        }
    }
}
