import com.sun.source.tree.MethodTree;
import org.homs.cc4j.rules.visitors.rules.co.ClassMembersOrderingRule;

import javax.lang.model.element.Modifier;
import java.util.List;

public class Jou {
    void a3(int a, int b, int c) {
        if (a == 1) {
            switch (b) {
                case 2:
                    try {
                        System.out.println(b);
                    } catch (Exception e) {
                        System.out.println(c);
                    }
                    break;
                default:
            }
        }
    }
    void a4(int a, int b, int c) {
        if (a == 1) {
            switch (b) {
                case 2:
                    try {
                        for (int i = 0; i < b; i++) {
                            System.out.println(b);
                        }
                    } catch (Exception e) {
                        for (int i = 0; i < c; i++) {
                            System.out.println(c);
                        }
                    }
                    break;
                default:
            }
        }
    }
    void a5(int a, int b, int c) {
        if (a == 1) {
            switch (b) {
                case 2:
                    try {
                        for (int i = 0; i < b; i++) {
                            if (i == 0) {
                                System.out.println(b);
                            }
                        }
                    } catch (Exception e) {
                        for (int i = 0; i < c; i++) {
                            System.out.println(c);
                        }
                    }
                    break;
                default:
            }
        }
    }

    protected void inspectMethod_1(List<ClassMembersOrderingRule.Member> r, MethodTree method) {
        var name = method.getName().toString();
        if (method.getReturnType() == null) {
            r.add(ClassMembersOrderingRule.Member.CTOR);
        } else if ("equals".equals(name) && method.getParameters().size() == 1 && "boolean".equals(method.getReturnType().toString())) {
            r.add(ClassMembersOrderingRule.Member.EQUALS_HASHCODE);
        } else if ("hashCode".equals(name) && method.getParameters().isEmpty() && "int".equals(method.getReturnType().toString())) {
            r.add(ClassMembersOrderingRule.Member.EQUALS_HASHCODE);
        } else if ("toString".equals(name) && method.getParameters().isEmpty() && "String".equals(method.getReturnType().toString())) {
            r.add(ClassMembersOrderingRule.Member.TOSTRING);
        } else if (!method.getModifiers().getFlags().contains(Modifier.STATIC)) {
            // els static poden anar a on es vulgui
            r.add(ClassMembersOrderingRule.Member.METHOD);
        }
    }
}