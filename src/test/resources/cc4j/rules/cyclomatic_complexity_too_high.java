import com.sun.source.tree.MethodTree;
import org.homs.cc4j.rules.visitors.rules.co.ClassMembersOrderingRule;

import javax.lang.model.element.Modifier;
import java.util.List;

public class Jou {

    {
        for (var i = 0; i < 10; i++) {
            long r = i % 2 == 0 ? 100L : 1_000L:
        }
    }

    void cyclomatic12(int a, int b, int c) {
        while (a > 0 && b > 0 && c > 0) {
            switch (b) {
                case 0:
                    System.out.println(0);
                    break;
                case 1:
                    System.out.println(1);
                    break;
                default:
                    //System.out.println(a == 5 || b == 5 || c == 5);
            }
        }
        if (a == b || a == 1 || b == 0 || a + b + c % 2 == 0) {
            return;
        }
        if (a == b) {
            if (b == c) {
                System.out.println("a");
            } else {
                System.out.println("b");
            }
        }
    }

    void cyclomatic14(int a, int b, int c) {
        while (a > 0 && b > 0 && c > 0) {
            switch (b) {
                case 0:
                    System.out.println(0);
                    break;
                case 1:
                    System.out.println(1);
                    break;
                default:
                    System.out.println(a == 5 || b == 5 || c == 5); // <=== es conten les && i || de tot arreu!
            }
        }
        if (a == b || a == 1 || b == 0 || a + b + c % 2 == 0) {
            return;
        }
        if (a == b) {
            if (b == c) {
                System.out.println("a");
            } else {
                System.out.println("b");
            }
        }
    }

    void cyclomatic15(int a, int b, int c, List<String> ks) {
        for (var k : ks) {
        }
        do {
        } while (a > 0 && b > 0 || c > 0);

        for (int i = 0; i < ks.size() && a > 0 || b > 0; i++) {
        }

        for (var k : ks) {
        }
        do {
        } while (a > 0 && b > 0 || c > 0);
        for (int i = 0; i < ks.size() && a > 0 || b > 0; i++) {
        }
    }

    protected void inspectMethod12(List<ClassMembersOrderingRule.Member> r, MethodTree method) {
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


    void lambdas_and_catches_3_1() {
        try {

        } catch (Exception e) {
            BiFunction k = (a, b) -> a * b;
        }
    }

    void lambdas_and_catches_3_2() {
        try {

        } catch (Exception e) {
            BiFunction k = (a, b) -> {
                return a * b;
            };
        }
    }
}