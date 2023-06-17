package org.homs.cc4j.visitors.rules;

import com.sun.source.tree.ClassTree;
import com.sun.source.tree.MethodTree;
import com.sun.source.tree.Tree;
import com.sun.source.tree.VariableTree;
import org.homs.cc4j.visitors.RuleTreeVisitor;

import javax.lang.model.element.Modifier;
import java.util.ArrayList;
import java.util.List;

import static org.homs.cc4j.issue.IssueSeverity.CRITICAL;

public class ClassMembersOrderingRule extends RuleTreeVisitor<Void> {

    enum Member {
        LOGGER(1), STATIC(2), PROPERTY(3), CTOR(4), METHOD(5), EQUALS_HASHCODE(6), TOSTRING(7);

        final int order;

        Member(int order) {
            this.order = order;
        }

        public int getOrder() {
            return order;
        }
    }

    @Override
    public Integer visitClass(ClassTree node, Void p) {
        location.push(node.getSimpleName().toString());
        inspectClassMembersOrder(node);
        location.pop();
        return 0;
    }

    private void inspectClassMembersOrder(ClassTree node) {
        List<Member> r = new ArrayList<>();
        for (Tree member : node.getMembers()) {
            if (member instanceof VariableTree) {
                inspectProperty(r, (VariableTree) member);
            } else if (member instanceof MethodTree) {
                inspectMethod(r, (MethodTree) member);
            } else if (member instanceof ClassTree) {
                // -
            }
        }

        var s = new StringBuilder();
        for (var m : r) {
            s.append(m.getOrder());
        }

        // LOGGER(1), STATIC(2), PROPERTY(3), CTOR(4), METHOD(5), EQUALS_HASHCODE(6), TOSTRING(7);
        var pattern = "^1?2*3*4*5*6*7*$";
        if (!s.toString().matches(pattern)) {
            generateIssue(CRITICAL, String.format("class members should be ordered as the convention: %s (pattern is: %s)", s, pattern));
        }
    }

    protected void inspectMethod(List<Member> r, MethodTree method) {
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
    }

    protected void inspectProperty(List<Member> r, VariableTree var) {
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
    }
}
