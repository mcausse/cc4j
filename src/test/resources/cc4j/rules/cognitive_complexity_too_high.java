import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Jou {

    void cognitive29(int a, int b, int c, List<String> ks) {
        for (var k : ks) {
            do {
                for (int i = 0; i < ks.size() && a > 0 || b > 0; i++) {
                    for (var k2 : ks) {
                        do {
                            for (int j = 0; j < ks.size() && a > 0 || b > 0; j++) {
                            }
                        } while (a > 0 && b > 0 || c > 0);
                    }
                }
            } while (a > 0 && b > 0 || c > 0);
        }
    }

    void cognitive28(int a, int b, int c, List<String> ks) {
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
        for (int j = 0; j < ks.size() && a > 0 || b > 0; j++) {
        }

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
        for (int j = 0; j < ks.size() && a > 0 || b > 0; j++) {
        }
    }

    public Object evalAstCognitive24(Ast ast) throws Throwable {

        if (evaluatingAstsListener != null) {
            evaluatingAstsListener.accept(env, ast);
        }

        try {
            if (ast instanceof InterpolationStringAst) {
                return evaluateStringInterpolation((InterpolationStringAst) ast);
            } else if (ast instanceof StringAst) {
                return ((StringAst) ast).value;
            } else if (ast instanceof NumberAst) {
                return ((NumberAst) ast).value;
            } else if (ast instanceof NullAst) {
                return null;
            } else if (ast instanceof BooleanAst) {
                return ((BooleanAst) ast).value;
            } else if (ast instanceof SymbolAst) {
                String sym = ((SymbolAst) ast).value;
                return env.get(sym);
            } else if (ast instanceof ListAst) {
                List<Ast> listAstValues = ((ListAst) ast).values;
                return evalListAst(listAstValues);
            } else if (ast instanceof MapAst) {
                Map<Ast, Ast> mapAstValues = ((MapAst) ast).values;
                return evalMapAst(mapAstValues);
            } else if (ast instanceof ParenthesisAst) {
                ParenthesisAst parenthesisAst = (ParenthesisAst) ast;

                Object op = evalAst(parenthesisAst.operator);
                if (op instanceof Func) {
                    final List<Object> args;
                    if (isLazyFunc(parenthesisAst.operator)) {
                        args = new ArrayList<>(parenthesisAst.arguments);
                    } else {
                        args = evalListAst(parenthesisAst.arguments);
                    }
                    Func f = (Func) evalAst(parenthesisAst.operator);
                    return f.eval(ast.getTokenAt(), this, args);
                } else {

                    List<Ast> args = parenthesisAst.arguments;

                    // TODO validate arguments?
                    String methodName = (String) evalAst(args.get(0));

                    var evaluatedArgs = new ArrayList<>();
                    for (int i = 1; i < args.size(); i++) {
                        var evaluatedArg = evalAst(args.get(i));
                        evaluatedArgs.add(evaluatedArg);
                    }
                    return ReflectUtils.callMethod(op, methodName, evaluatedArgs.toArray());
                }
            } else {
                throw new RuntimeException(ast.getClass().getName());
            }
        } catch (Exception e) {
            throw new RuntimeException("error evaluating: " + ast.toString() + "; " + ast.getTokenAt().toString(), e);
        } catch (AssertionError e) {
            throw new AssertionError(e.getMessage(), new AssertionError(ast + "; " + ast.getTokenAt().toString()));
        } catch (ValidationError e) {
            throw new ValidationError(e.getMessage(), new ValidationError(ast + "; " + ast.getTokenAt().toString()));
        }
    }

    void aaa21(int a, int b, int c) {

        if (a == 1) {
        } else if (a == 2) {
            if (b == 1) {
            } else {
            }
        } else if (a == 3) {
            if (b == 1) {
            } else {
            }
        } else if (a == 4) {
            if (b == 1) {
            } else {
            }
        } else if (a == 5) {
            if (b == 1) {
            } else {
            }
        } else {
            if (b == 1) {
            } else {
            }
        }
    }


    void aaa13(int a, int b, int c) {

        if (a == 1) {               // 1
        } else if (a == 2) {        // 1
            if (b == 1) {           // 2
            } else {                // 1
            }
        } else if (a == 2) {        // 1
            if (b == 1) {           // 2
            } else {                // 1
            }
        } else {                    // 1
            if (b == 11) {          // 2
            } else {                // 1
            }
        }
    }
}