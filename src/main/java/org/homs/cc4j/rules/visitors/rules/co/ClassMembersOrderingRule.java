package org.homs.cc4j.rules.visitors.rules.co;

import com.sun.source.tree.ClassTree;
import com.sun.source.tree.MethodTree;
import com.sun.source.tree.Tree;
import com.sun.source.tree.VariableTree;
import org.homs.cc4j.RuleInfo;
import org.homs.cc4j.rules.visitors.RuleTreeVisitor;

import javax.lang.model.element.Modifier;
import java.util.ArrayList;
import java.util.List;

import static org.homs.cc4j.issue.IssueSeverity.CRITICAL;

public class ClassMembersOrderingRule extends RuleTreeVisitor<Void> {

    @Override
    public RuleInfo getRuleInfo() {
        return new RuleInfo("co", 2, "Vertical ordering of the class members.");
    }

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

    void inspectClassMembersOrder(ClassTree node) {
        StringBuilder s = getMembersSequence(node);

        // LOGGER(1), STATIC(2), PROPERTY(3), CTOR(4), METHOD(5), EQUALS_HASHCODE(6), TOSTRING(7);
        var pattern = "^1?2*3*4*5*6*7*$";
        if (!s.toString().matches(pattern)) {
            generateIssue(CRITICAL, String.format("class members should be ordered as the convention: %s (pattern is: %s)", s, pattern));
        }
    }

    StringBuilder getMembersSequence(ClassTree node) {
        List<Member> r = new ArrayList<>();
        for (Tree member : node.getMembers()) {
            if (member instanceof VariableTree) {
                inspectProperty(r, (VariableTree) member);
            } else if (member instanceof MethodTree) {
                inspectMethod(r, (MethodTree) member);
            }
            // - ignoring class initializers (BlockTree's hanging directly from ClassTree#getMembers()
            // - ignoring inner classes (if (member instanceof ClassTree))
        }

        var s = new StringBuilder();
        for (var m : r) {
            s.append(m.getOrder());
        }
        return s;
    }

    protected void inspectMethod(List<Member> r, MethodTree method) {
        //                /**
        //                 * Returns the return type of the method being declared.
        //                 * Returns {@code null} for a constructor.
        //                 * @return the return type
        //                 */
        //                Tree getReturnType();

        if (method.getReturnType() == null) {
            r.add(Member.CTOR);
        } else if (isEqualsMethod(method)) {
            r.add(Member.EQUALS_HASHCODE);
        } else if (isHashCodeMethod(method)) {
            r.add(Member.EQUALS_HASHCODE);
        } else if (isToStringMethod(method)) {
            r.add(Member.TOSTRING);
        } else if (!method.getModifiers().getFlags().contains(Modifier.STATIC)) {
            // els static poden anar a on es vulgui
            r.add(Member.METHOD);
        }
    }

    boolean isEqualsMethod(MethodTree method) {
        return "equals".equals(method.getName().toString())
                && method.getParameters().size() == 1
                && "boolean".equals(method.getReturnType().toString());
    }

    boolean isHashCodeMethod(MethodTree method) {
        return "hashCode".equals(method.getName().toString())
                && method.getParameters().isEmpty()
                && "int".equals(method.getReturnType().toString());
    }

    boolean isToStringMethod(MethodTree method) {
        return "toString".equals(method.getName().toString())
                && method.getParameters().isEmpty()
                && "String".equals(method.getReturnType().toString());
    }

    protected void inspectProperty(List<Member> r, VariableTree variableTree) {
        boolean isStatic = variableTree.getModifiers().getFlags().contains(Modifier.STATIC);
        var name = variableTree.getName().toString();
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
