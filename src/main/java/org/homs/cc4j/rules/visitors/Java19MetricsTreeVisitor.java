package org.homs.cc4j.rules.visitors;

import com.sun.source.tree.*;

import java.util.List;

public class Java19MetricsTreeVisitor<P> implements TreeVisitor<Integer, P> {

    @Override
    public Integer visitAnnotatedType(AnnotatedTypeTree node, P p) {
        var r = 0;
        for (AnnotationTree tree : node.getAnnotations()) {
            r += tree.accept(this, p);
        }
        return r;
    }

    @Override
    public Integer visitAnnotation(AnnotationTree node, P p) {
        var r = 0;
        for (ExpressionTree tree : node.getArguments()) {
            r += tree.accept(this, p);
        }
        return r;
    }

    @Override
    public Integer visitMethodInvocation(MethodInvocationTree node, P p) {
        var r = 0;
        for (Tree tree1 : node.getTypeArguments()) {
            r += tree1.accept(this, p);
        }
        for (ExpressionTree tree : node.getArguments()) {
            r += tree.accept(this, p);
        }
        node.getMethodSelect().accept(this, p);
        return r;
    }

    @Override
    public Integer visitAssert(AssertTree node, P p) {
        var r = 0;
        if (node.getCondition() != null) {
            r += node.getCondition().accept(this, p);
        }
        if (node.getDetail() != null) {
            r += node.getDetail().accept(this, p);
        }
        return r;
    }

    @Override
    public Integer visitAssignment(AssignmentTree node, P p) {
        var r = 0;
        r += node.getVariable().accept(this, p);
        r += node.getExpression().accept(this, p);
        return r;
    }

    @Override
    public Integer visitCompoundAssignment(CompoundAssignmentTree node, P p) {
        var r = 0;
        r += node.getVariable().accept(this, p);
        r += node.getExpression().accept(this, p);
        return r;
    }

    @Override
    public Integer visitBinary(BinaryTree node, P p) {
        var r = 0;
        r += node.getLeftOperand().accept(this, p);
        r += node.getRightOperand().accept(this, p);
        return r;
    }

    @Override
    public Integer visitBlock(BlockTree node, P p) {
        var r = 0;
        for (StatementTree tree : node.getStatements()) {
            r += tree.accept(this, p);
        }
        return r;
    }

    @Override
    public Integer visitBreak(BreakTree node, P p) {
        return 0;
    }

    @Override
    public Integer visitCase(CaseTree node, P p) {
        var r = 0;
        if (node.getBody() != null)
            r += node.getBody().accept(this, p);
        for (ExpressionTree expressionTree : node.getExpressions()) {
            r += expressionTree.accept(this, p);
        }
        if (node.getStatements() != null) {
            for (StatementTree tree : node.getStatements()) {
                r += tree.accept(this, p);
            }
        }
        return r;
    }

    @Override
    public Integer visitCatch(CatchTree node, P p) {
        var r = 0;
        r += node.getParameter().accept(this, p);
        r += node.getBlock().accept(this, p);
        return r;
    }

    @Override
    public Integer visitClass(ClassTree node, P p) {
        var r = 0;
        for (Tree tree1 : node.getMembers()) {
            if (tree1 instanceof BlockTree) {
                r += visitClassInitializer((BlockTree) tree1, p);
            } else {
                r += tree1.accept(this, p);
            }
        }
        for (Tree tree1 : node.getImplementsClause()) {
            r += tree1.accept(this, p);
        }
        for (Tree tree1 : node.getPermitsClause()) {
            r += tree1.accept(this, p);
        }
        for (TypeParameterTree tree : node.getTypeParameters()) {
            r += tree.accept(this, p);
        }
        if (node.getExtendsClause() != null) {
            r += node.getExtendsClause().accept(this, p);
        }
        r += node.getModifiers().accept(this, p);
        return r;
    }

    public int visitClassInitializer(BlockTree classInitializer, P p) {
        return classInitializer.accept(this, p);
    }

    @Override
    public Integer visitConditionalExpression(ConditionalExpressionTree node, P p) {
        var r = 0;
        r += node.getCondition().accept(this, p);
        r += node.getTrueExpression().accept(this, p);
        r += node.getFalseExpression().accept(this, p);
        return r;
    }

    @Override
    public Integer visitContinue(ContinueTree node, P p) {
        return 0;
    }

    @Override
    public Integer visitDoWhileLoop(DoWhileLoopTree node, P p) {
        var r = 0;
        r += node.getCondition().accept(this, p);
        r += node.getStatement().accept(this, p);
        return r;
    }

    @Override
    public Integer visitErroneous(ErroneousTree node, P p) {
        var r = 0;
        for (Tree tree : node.getErrorTrees()) {
            r += tree.accept(this, p);
        }
        return r;
    }

    @Override
    public Integer visitExpressionStatement(ExpressionStatementTree node, P p) {
        var r = 0;
        r += node.getExpression().accept(this, p);
        return r;
    }

    @Override
    public Integer visitEnhancedForLoop(EnhancedForLoopTree node, P p) {
        var r = 0;
        r += node.getVariable().accept(this, p);
        r += node.getExpression().accept(this, p);
        r += node.getStatement().accept(this, p);
        return r;
    }

    @Override
    public Integer visitForLoop(ForLoopTree node, P p) {
        var r = 0;
        for (StatementTree statementTree : node.getInitializer()) {
            r += statementTree.accept(this, p);
        }
        if (node.getCondition() != null) {
            r += node.getCondition().accept(this, p);
        }
        for (ExpressionStatementTree expressionStatementTree : node.getUpdate()) {
            r += expressionStatementTree.accept(this, p);
        }
        for (ExpressionStatementTree tree : node.getUpdate()) {
            r += tree.accept(this, p);
        }
        r += node.getStatement().accept(this, p);
        return r;
    }

    @Override
    public Integer visitIdentifier(IdentifierTree node, P p) {
        return 0;
    }

    @Override
    public Integer visitIf(IfTree node, P p) {
        var r = 0;
        r += node.getCondition().accept(this, p);
        if (node.getThenStatement() != null)
            r += node.getThenStatement().accept(this, p);
        if (node.getElseStatement() != null)
            r += node.getElseStatement().accept(this, p);
        return r;
    }

    @Override
    public Integer visitImport(ImportTree node, P p) {
        var r = 0;
        r += node.getQualifiedIdentifier().accept(this, p);
        return r;
    }

    @Override
    public Integer visitArrayAccess(ArrayAccessTree node, P p) {
        var r = 0;
        r += node.getExpression().accept(this, p);
        r += node.getIndex().accept(this, p);
        return r;
    }

    @Override
    public Integer visitLabeledStatement(LabeledStatementTree node, P p) {
        return 0;
    }

    @Override
    public Integer visitLiteral(LiteralTree node, P p) {
        return 0;
    }

    @Override
    public Integer visitBindingPattern(BindingPatternTree node, P p) {
//        bindingPatternTree.getVariable().accept(this); // TODO
        return 0;
    }

    @Override
    public Integer visitDefaultCaseLabel(DefaultCaseLabelTree node, P p) {
        return 0;
    }

    @Override
    public Integer visitConstantCaseLabel(ConstantCaseLabelTree node, P p) {
        var r = 0;
        r += node.getConstantExpression().accept(this, p);
        return r;
    }

    @Override
    public Integer visitPatternCaseLabel(PatternCaseLabelTree node, P p) {
        var r = 0;
        r += node.getPattern().accept(this, p);
        r += node.getGuard().accept(this, p);
        return r;
    }

    @Override
    public Integer visitDeconstructionPattern(DeconstructionPatternTree node, P p) {
        var r = 0;
        r += node.getVariable().accept(this, p);
        r += node.getDeconstructor().accept(this, p);
        for (PatternTree tree : node.getNestedPatterns()) {
            r += tree.accept(this, p);
        }
        return r;
    }

    @Override
    public Integer visitMethod(MethodTree node, P p) {
        var r = 0;
        for (VariableTree variableTree : node.getParameters()) {
            r += variableTree.accept(this, p);
        }
        if (node.getReturnType() != null)
            r += node.getReturnType().accept(this, p);
        if (node.getBody() != null)
            r += node.getBody().accept(this, p);
        for (TypeParameterTree typeParameterTree : node.getTypeParameters()) {
            r += typeParameterTree.accept(this, p);
        }
        for (ExpressionTree tree : node.getThrows()) {
            r += tree.accept(this, p);
        }
        if (node.getDefaultValue() != null)
            r += node.getDefaultValue().accept(this, p);
        r += node.getModifiers().accept(this, p);
        if (node.getReceiverParameter() != null)
            r += node.getReceiverParameter().accept(this, p);
        return r;
    }

    @Override
    public Integer visitModifiers(ModifiersTree node, P p) {
        var r = 0;
        for (AnnotationTree tree : node.getAnnotations()) {
            r += tree.accept(this, p);
        }
        return r;
    }

    @Override
    public Integer visitNewArray(NewArrayTree node, P p) {
        var r = 0;
        for (AnnotationTree annotationTree : node.getAnnotations()) {
            r += annotationTree.accept(this, p);
        }
        for (List<? extends AnnotationTree> annotationTrees : node.getDimAnnotations()) {
            for (AnnotationTree tree2 : annotationTrees) {
                r += tree2.accept(this, p);
            }
        }
        for (ExpressionTree expressionTree : node.getDimensions()) {
            r += expressionTree.accept(this, p);
        }
        if (node.getInitializers() != null) {
            for (ExpressionTree tree : node.getInitializers()) {
                r += tree.accept(this, p);
            }
        }
        if (node.getType() != null)
            r += node.getType().accept(this, p);
        return r;
    }

    @Override
    public Integer visitParenthesizedPattern(ParenthesizedPatternTree node, P p) {
        var r = 0;
        r += node.getPattern().accept(this, p);
        return r;
    }

    @Override
    public Integer visitNewClass(NewClassTree node, P p) {
        var r = 0;
        r += node.getIdentifier().accept(this, p);
        for (Tree tree1 : node.getTypeArguments()) {
            r += tree1.accept(this, p);
        }
        for (ExpressionTree tree : node.getArguments()) {
            r += tree.accept(this, p);
        }
        if (node.getClassBody() != null)
            r += node.getClassBody().accept(this, p);
        if (node.getEnclosingExpression() != null)
            r += node.getEnclosingExpression().accept(this, p);
        return r;
    }

    @Override
    public Integer visitLambdaExpression(LambdaExpressionTree node, P p) {
        var r = 0;
        for (VariableTree tree : node.getParameters()) {
            r += tree.accept(this, p);
        }
        r += node.getBody().accept(this, p);
        return r;
    }

    @Override
    public Integer visitPackage(PackageTree node, P p) {
        var r = 0;
        for (AnnotationTree tree : node.getAnnotations()) {
            r += tree.accept(this, p);
        }
        r += node.getPackageName().accept(this, p);
        return r;
    }

    @Override
    public Integer visitParenthesized(ParenthesizedTree node, P p) {
        var r = 0;
        r += node.getExpression().accept(this, p);
        return r;
    }

    @Override
    public Integer visitReturn(ReturnTree node, P p) {
        var r = 0;
        if (node.getExpression() != null)
            r += node.getExpression().accept(this, p);
        return r;
    }

    @Override
    public Integer visitMemberSelect(MemberSelectTree node, P p) {
        var r = 0;
        r += node.getExpression().accept(this, p);
        return r;
    }

    @Override
    public Integer visitMemberReference(MemberReferenceTree node, P p) {
        var r = 0;
        if (node.getTypeArguments() != null) {
            for (ExpressionTree tree : node.getTypeArguments()) {
                r += tree.accept(this, p);
            }
        }
        if (node.getQualifierExpression() != null)
            r += node.getQualifierExpression().accept(this, p);
        return r;
    }

    @Override
    public Integer visitEmptyStatement(EmptyStatementTree node, P p) {
        return 0;
    }

    @Override
    public Integer visitSwitch(SwitchTree node, P p) {
        var r = 0;
        r += node.getExpression().accept(this, p);
        for (CaseTree tree : node.getCases()) {
            r += tree.accept(this, p);
        }
        return r;
    }

    @Override
    public Integer visitSwitchExpression(SwitchExpressionTree node, P p) {
        var r = 0;
        for (CaseTree tree : node.getCases()) {
            r += tree.accept(this, p);
        }
        r += node.getExpression().accept(this, p);
        return r;
    }

    @Override
    public Integer visitSynchronized(SynchronizedTree node, P p) {
        var r = 0;
        r += node.getExpression().accept(this, p);
        r += node.getBlock().accept(this, p);
        return r;
    }

    @Override
    public Integer visitThrow(ThrowTree node, P p) {
        var r = 0;
        r += node.getExpression().accept(this, p);
        return r;
    }

    @Override
    public Integer visitCompilationUnit(CompilationUnitTree node, P p) {
        var r = 0;
        for (Tree tree1 : node.getTypeDecls()) {
            r += tree1.accept(this, p);
        }
        for (ImportTree importTree : node.getImports()) {
            r += importTree.accept(this, p);
        }
        for (AnnotationTree tree : node.getPackageAnnotations()) {
            r += tree.accept(this, p);
        }
        if (node.getPackage() != null)
            r += node.getPackage().accept(this, p);
        if (node.getModule() != null)
            r += node.getModule().accept(this, p);
        if (node.getPackageName() != null)
            r += node.getPackageName().accept(this, p);
        return r;
    }

    @Override
    public Integer visitTry(TryTree node, P p) {
        var r = 0;
        if (node.getResources() != null) {
            for (Tree tree : node.getResources()) {
                r += tree.accept(this, p);
            }
        }
        if (node.getBlock() != null) {
            r += node.getBlock().accept(this, p);
        }
        for (CatchTree catchTree : node.getCatches()) {
            r += catchTree.accept(this, p);
        }
        if (node.getFinallyBlock() != null)
            r += node.getFinallyBlock().accept(this, p);
        return r;
    }

    @Override
    public Integer visitParameterizedType(ParameterizedTypeTree node, P p) {
        var r = 0;
        for (Tree tree : node.getTypeArguments()) {
            r += tree.accept(this, p);
        }
        return r;
    }

    @Override
    public Integer visitUnionType(UnionTypeTree node, P p) {
        var r = 0;
        for (Tree tree : node.getTypeAlternatives()) {
            r += tree.accept(this, p);
        }
        return r;
    }

    @Override
    public Integer visitIntersectionType(IntersectionTypeTree node, P p) {
        var r = 0;
        for (Tree tree : node.getBounds()) {
            r += tree.accept(this, p);
        }
        return r;
    }

    @Override
    public Integer visitArrayType(ArrayTypeTree node, P p) {
        return 0;
    }

    @Override
    public Integer visitTypeCast(TypeCastTree node, P p) {
        var r = 0;
        r += node.getType().accept(this, p);
        r += node.getExpression().accept(this, p);
        return r;
    }

    @Override
    public Integer visitPrimitiveType(PrimitiveTypeTree node, P p) {
        return 0;
    }

    @Override
    public Integer visitTypeParameter(TypeParameterTree node, P p) {
        var r = 0;
        for (AnnotationTree annotationTree : node.getAnnotations()) {
            r += annotationTree.accept(this, p);
        }
        for (Tree tree : node.getBounds()) {
            r += tree.accept(this, p);
        }
        return r;
    }

    @Override
    public Integer visitInstanceOf(InstanceOfTree node, P p) {
        var r = 0;
        r += node.getExpression().accept(this, p);
        r += node.getType().accept(this, p);
        if (node.getPattern() != null)
            r += node.getPattern().accept(this, p);
        return r;
    }

    @Override
    public Integer visitUnary(UnaryTree node, P p) {
        var r = 0;
        r += node.getExpression().accept(this, p);
        return r;
    }

    @Override
    public Integer visitVariable(VariableTree node, P p) {
        var r = 0;
        if (node.getType() != null)
            r += node.getType().accept(this, p);
        if (node.getInitializer() != null)
            r += node.getInitializer().accept(this, p);
        if (node.getModifiers() != null)
            r += node.getModifiers().accept(this, p);
        if (node.getNameExpression() != null)
            r += node.getNameExpression().accept(this, p);
        return r;
    }

    @Override
    public Integer visitWhileLoop(WhileLoopTree node, P p) {
        var r = 0;
        r += node.getCondition().accept(this, p);
        r += node.getStatement().accept(this, p);
        return r;
    }

    @Override
    public Integer visitWildcard(WildcardTree node, P p) {
        var r = 0;
        if (node.getBound() != null)
            r += node.getBound().accept(this, p);
        return r;
    }

    @Override
    public Integer visitModule(ModuleTree node, P p) {
        var r = 0;
        for (AnnotationTree annotationTree : node.getAnnotations()) {
            r += annotationTree.accept(this, p);
        }
        for (DirectiveTree tree : node.getDirectives()) {
            r += tree.accept(this, p);
        }
        r += node.getName().accept(this, p);
        return r;
    }

    @Override
    public Integer visitExports(ExportsTree node, P p) {
        var r = 0;
        for (ExpressionTree tree : node.getModuleNames()) {
            r += tree.accept(this, p);
        }
        r += node.getPackageName().accept(this, p);
        return r;
    }

    @Override
    public Integer visitOpens(OpensTree node, P p) {
        var r = 0;
        for (ExpressionTree tree : node.getModuleNames()) {
            r += tree.accept(this, p);
        }
        r += node.getPackageName().accept(this, p);
        return r;
    }

    @Override
    public Integer visitProvides(ProvidesTree node, P p) {
        var r = 0;
        for (ExpressionTree tree : node.getImplementationNames()) {
            r += tree.accept(this, p);
        }
        r += node.getServiceName().accept(this, p);
        return r;
    }

    @Override
    public Integer visitRequires(RequiresTree node, P p) {
        var r = 0;
        r += node.getModuleName().accept(this, p);
        return r;
    }

    @Override
    public Integer visitUses(UsesTree node, P p) {
        var r = 0;
        r += node.getServiceName().accept(this, p);
        return r;
    }

    @Override
    public Integer visitOther(Tree node, P p) {
        return 0;
    }

    @Override
    public Integer visitYield(YieldTree node, P p) {
        return 0;
    }
}
