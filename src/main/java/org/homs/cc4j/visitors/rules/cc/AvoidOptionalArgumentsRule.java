package org.homs.cc4j.visitors.rules.cc;

import com.sun.source.tree.MethodTree;
import com.sun.source.tree.ParameterizedTypeTree;
import org.homs.cc4j.RuleInfo;
import org.homs.cc4j.visitors.RuleTreeVisitor;

import java.util.Optional;

import static org.homs.cc4j.issue.IssueSeverity.CRITICAL;

public class AvoidOptionalArgumentsRule extends RuleTreeVisitor<Void> {

    @Override
    public RuleInfo getRuleInfo() {
        return new RuleInfo("cc", 4, "Avoid Optional<..> arguments.");
    }

    @Override
    public Integer visitMethod(MethodTree node, Void p) {
        location.push(node.getName().toString() + "(..)");

        for (var param : node.getParameters()) {
            if (ParameterizedTypeTree.class.isAssignableFrom(param.getType().getClass())) {
                checkParameter(param);
            }
        }

        location.pop();
        return super.visitMethod(node, p);
    }

    private void checkParameter(com.sun.source.tree.VariableTree param) {
        var type = (ParameterizedTypeTree) param.getType();
        if (type.getType() == null) {
            return;
        }
        final String typeName = type.getType().toString();
        if (typeName.equals(Optional.class.getSimpleName()) || typeName.equals(Optional.class.getName())) {
            generateIssue(CRITICAL, "avoid Optional<..> arguments");
        }
    }
}
