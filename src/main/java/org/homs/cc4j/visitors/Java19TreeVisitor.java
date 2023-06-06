package org.homs.cc4j.visitors;

import com.sun.source.tree.*;

public class Java19TreeVisitor<R, P> implements TreeVisitor<R, P> {

    @Override
    public R visitAnnotatedType(AnnotatedTypeTree node, P p) {
        node.getAnnotations().forEach(tree -> tree.accept(this, p));
        return null;
    }

    @Override
    public R visitAnnotation(AnnotationTree node, P p) {
        node.getArguments().forEach(tree -> tree.accept(this, p));
        return null;
    }

    @Override
    public R visitMethodInvocation(MethodInvocationTree node, P p) {
        node.getTypeArguments().forEach(tree -> tree.accept(this, p));
        node.getArguments().forEach(tree -> tree.accept(this, p));
        node.getMethodSelect().accept(this, p);
        return null;
    }

    @Override
    public R visitAssert(AssertTree node, P p) {
        if (node.getCondition() != null) node.getCondition().accept(this, p);
        if (node.getDetail() != null) node.getDetail().accept(this, p);
        return null;
    }

    @Override
    public R visitAssignment(AssignmentTree node, P p) {
        node.getVariable().accept(this, p);
        node.getExpression().accept(this, p);
        return null;
    }

    @Override
    public R visitCompoundAssignment(CompoundAssignmentTree node, P p) {
        node.getVariable().accept(this, p);
        node.getExpression().accept(this, p);
        return null;
    }

    @Override
    public R visitBinary(BinaryTree node, P p) {
        node.getLeftOperand().accept(this, p);
        node.getRightOperand().accept(this, p);
        return null;
    }

    @Override
    public R visitBlock(BlockTree node, P p) {
        node.getStatements().forEach(tree -> tree.accept(this, p));
        return null;
    }

    @Override
    public R visitBreak(BreakTree node, P p) {
        return null;
    }

    @Override
    public R visitCase(CaseTree node, P p) {
        if (node.getBody() != null) node.getBody().accept(this, p);
        node.getExpressions().forEach(tree -> tree.accept(this, p));
        node.getStatements().forEach(tree -> tree.accept(this, p));
        return null;
    }

    @Override
    public R visitCatch(CatchTree node, P p) {
        node.getParameter().accept(this, p);
        node.getBlock().accept(this, p);
        return null;
    }

    @Override
    public R visitClass(ClassTree node, P p) {
        node.getMembers().forEach(tree -> tree.accept(this, p));
        node.getImplementsClause().forEach(tree -> tree.accept(this, p));
        node.getPermitsClause().forEach(tree -> tree.accept(this, p));
        node.getTypeParameters().forEach(tree -> tree.accept(this, p));
        if (node.getExtendsClause() != null) node.getExtendsClause().accept(this, p);
        node.getModifiers().accept(this, p);
        return null;
    }

    @Override
    public R visitConditionalExpression(ConditionalExpressionTree node, P p) {
        node.getCondition().accept(this, p);
        node.getTrueExpression().accept(this, p);
        node.getFalseExpression().accept(this, p);
        return null;
    }

    @Override
    public R visitContinue(ContinueTree node, P p) {
        return null;
    }

    @Override
    public R visitDoWhileLoop(DoWhileLoopTree node, P p) {
        node.getCondition().accept(this, p);
        node.getStatement().accept(this, p);
        return null;
    }

    @Override
    public R visitErroneous(ErroneousTree node, P p) {
        node.getErrorTrees().forEach(tree -> tree.accept(this, p));
        return null;
    }

    @Override
    public R visitExpressionStatement(ExpressionStatementTree node, P p) {
        node.getExpression().accept(this, p);
        return null;
    }

    @Override
    public R visitEnhancedForLoop(EnhancedForLoopTree node, P p) {
        node.getVariable().accept(this, p);
        node.getExpression().accept(this, p);
        node.getStatement().accept(this, p);
        return null;
    }

    @Override
    public R visitForLoop(ForLoopTree node, P p) {
        node.getInitializer().forEach(tree -> tree.accept(this, p));
        if (node.getCondition() != null) node.getCondition().accept(this, p);
        node.getUpdate().forEach(tree -> tree.accept(this, p));
        node.getStatement().accept(this, p);
        return null;
    }

    @Override
    public R visitIdentifier(IdentifierTree node, P p) {
        return null;
    }

    @Override
    public R visitIf(IfTree node, P p) {
        node.getCondition().accept(this, p);
        if (node.getThenStatement() != null) node.getThenStatement().accept(this, p);
        if (node.getElseStatement() != null) node.getElseStatement().accept(this, p);
        return null;
    }

    @Override
    public R visitImport(ImportTree node, P p) {
        node.getQualifiedIdentifier().accept(this, p);
        return null;
    }

    @Override
    public R visitArrayAccess(ArrayAccessTree node, P p) {
        node.getExpression().accept(this, p);
        node.getIndex().accept(this, p);
        return null;
    }

    @Override
    public R visitLabeledStatement(LabeledStatementTree node, P p) {
        return null;
    }

    @Override
    public R visitLiteral(LiteralTree node, P p) {
        return null;
    }

    @Override
    public R visitBindingPattern(BindingPatternTree node, P p) {
//        bindingPatternTree.getVariable().accept(this); // TODO
        return null;
    }

    @Override
    public R visitDefaultCaseLabel(DefaultCaseLabelTree node, P p) {
        return null;
    }

    @Override
    public R visitConstantCaseLabel(ConstantCaseLabelTree node, P p) {
        node.getConstantExpression().accept(this, p);
        return null;
    }

    @Override
    public R visitPatternCaseLabel(PatternCaseLabelTree node, P p) {
        node.getPattern().accept(this, p);
        node.getGuard().accept(this, p);
        return null;
    }

    @Override
    public R visitDeconstructionPattern(DeconstructionPatternTree node, P p) {
        node.getVariable().accept(this, p);
        node.getDeconstructor().accept(this, p);
        node.getNestedPatterns().forEach(tree -> tree.accept(this, p));
        return null;
    }

    @Override
    public R visitMethod(MethodTree node, P p) {
        node.getParameters().forEach(tree -> tree.accept(this, p));
        if (node.getReturnType() != null) node.getReturnType().accept(this, p);
        if (node.getBody() != null) node.getBody().accept(this, p);
        node.getTypeParameters().forEach(tree -> tree.accept(this, p));
        node.getThrows().forEach(tree -> tree.accept(this, p));
        if (node.getDefaultValue() != null)
            node.getDefaultValue().accept(this, p);
        node.getModifiers().accept(this, p);
        if (node.getReceiverParameter() != null) node.getReceiverParameter().accept(this, p);
        return null;
    }

    @Override
    public R visitModifiers(ModifiersTree node, P p) {
        node.getAnnotations().forEach(tree -> tree.accept(this, p));
        return null;
    }

    @Override
    public R visitNewArray(NewArrayTree node, P p) {
        node.getAnnotations().forEach(tree -> tree.accept(this, p));
        node.getDimAnnotations().forEach(tree -> tree.forEach(tree2 -> tree2.accept(this, p)));
        node.getDimensions().forEach(tree -> tree.accept(this, p));
        if (node.getInitializers() != null) node.getInitializers().forEach(tree -> tree.accept(this, p));
        if (node.getType() != null) node.getType().accept(this, p);
        return null;
    }

    @Override
    public R visitParenthesizedPattern(ParenthesizedPatternTree node, P p) {
        node.getPattern().accept(this, p);
        return null;
    }

    @Override
    public R visitNewClass(NewClassTree node, P p) {
        node.getIdentifier().accept(this, p);
        node.getTypeArguments().forEach(tree -> tree.accept(this, p));
        node.getArguments().forEach(tree -> tree.accept(this, p));
        if (node.getClassBody() != null) node.getClassBody().accept(this, p);
        if (node.getEnclosingExpression() != null) node.getEnclosingExpression().accept(this, p);
        return null;
    }

    @Override
    public R visitLambdaExpression(LambdaExpressionTree node, P p) {
        node.getParameters().forEach(tree -> tree.accept(this, p));
        node.getBody().accept(this, p);
        return null;
    }

    @Override
    public R visitPackage(PackageTree node, P p) {
        node.getAnnotations().forEach(tree -> tree.accept(this, p));
        node.getPackageName().accept(this, p);
        return null;
    }

    @Override
    public R visitParenthesized(ParenthesizedTree node, P p) {
        node.getExpression().accept(this, p);
        return null;
    }

    @Override
    public R visitReturn(ReturnTree node, P p) {
        if (node.getExpression() != null) node.getExpression().accept(this, p);
        return null;
    }

    @Override
    public R visitMemberSelect(MemberSelectTree node, P p) {
        node.getExpression().accept(this, p);
        return null;
    }

    @Override
    public R visitMemberReference(MemberReferenceTree node, P p) {
        if (node.getTypeArguments() != null) node.getTypeArguments().forEach(tree -> tree.accept(this, p));
        if (node.getQualifierExpression() != null) node.getQualifierExpression().accept(this, p);
        return null;
    }

    @Override
    public R visitEmptyStatement(EmptyStatementTree node, P p) {
        return null;
    }

    @Override
    public R visitSwitch(SwitchTree node, P p) {
        node.getExpression().accept(this, p);
        node.getCases().forEach(tree -> tree.accept(this, p));
        return null;
    }

    @Override
    public R visitSwitchExpression(SwitchExpressionTree node, P p) {
        node.getCases().forEach(tree -> tree.accept(this, p));
        node.getExpression().accept(this, p);
        return null;
    }

    @Override
    public R visitSynchronized(SynchronizedTree node, P p) {
        node.getExpression().accept(this, p);
        node.getBlock().accept(this, p);
        return null;
    }

    @Override
    public R visitThrow(ThrowTree node, P p) {
        node.getExpression().accept(this, p);
        return null;
    }

    @Override
    public R visitCompilationUnit(CompilationUnitTree node, P p) {
        node.getTypeDecls().forEach(tree -> tree.accept(this, p));
        node.getImports().forEach(tree -> tree.accept(this, p));
        node.getPackageAnnotations().forEach(tree -> tree.accept(this, p));
        if (node.getPackage() != null) node.getPackage().accept(this, p);
        if (node.getModule() != null) node.getModule().accept(this, p);
        if (node.getPackageName() != null) node.getPackageName().accept(this, p);
        return null;
    }

    @Override
    public R visitTry(TryTree node, P p) {
        if (node.getResources() != null) node.getResources().forEach(tree -> tree.accept(this, p));
        if (node.getBlock() != null) node.getBlock().accept(this, p);
        node.getCatches().forEach(tree -> tree.accept(this, p));
        if (node.getFinallyBlock() != null) node.getFinallyBlock().accept(this, p);
        return null;
    }

    @Override
    public R visitParameterizedType(ParameterizedTypeTree node, P p) {
        node.getTypeArguments().forEach(tree -> tree.accept(this, p));
        return null;
    }

    @Override
    public R visitUnionType(UnionTypeTree node, P p) {
        node.getTypeAlternatives().forEach(tree -> tree.accept(this, p));
        return null;
    }

    @Override
    public R visitIntersectionType(IntersectionTypeTree node, P p) {
        node.getBounds().forEach(tree -> tree.accept(this, p));
        return null;
    }

    @Override
    public R visitArrayType(ArrayTypeTree node, P p) {
        return null;
    }

    @Override
    public R visitTypeCast(TypeCastTree node, P p) {
        node.getType().accept(this, p);
        node.getExpression().accept(this, p);
        return null;
    }

    @Override
    public R visitPrimitiveType(PrimitiveTypeTree node, P p) {
        return null;
    }

    @Override
    public R visitTypeParameter(TypeParameterTree node, P p) {
        node.getAnnotations().forEach(tree -> tree.accept(this, p));
        node.getBounds().forEach(tree -> tree.accept(this, p));
        return null;
    }

    @Override
    public R visitInstanceOf(InstanceOfTree node, P p) {
        node.getExpression().accept(this, p);
        node.getType().accept(this, p);
        if (node.getPattern() != null) node.getPattern().accept(this, p);
        return null;
    }

    @Override
    public R visitUnary(UnaryTree node, P p) {
        node.getExpression().accept(this, p);
        return null;
    }

    @Override
    public R visitVariable(VariableTree node, P p) {
        if (node.getType() != null) node.getType().accept(this, p);
        if (node.getInitializer() != null) node.getInitializer().accept(this, p);
        if (node.getModifiers() != null) node.getModifiers().accept(this, p);
        if (node.getNameExpression() != null) node.getNameExpression().accept(this, p);
        return null;
    }

    @Override
    public R visitWhileLoop(WhileLoopTree node, P p) {
        node.getCondition().accept(this, p);
        node.getStatement().accept(this, p);
        return null;
    }

    @Override
    public R visitWildcard(WildcardTree node, P p) {
        if (node.getBound() != null) node.getBound().accept(this, p);
        return null;
    }

    @Override
    public R visitModule(ModuleTree node, P p) {
        node.getAnnotations().forEach(tree -> tree.accept(this, p));
        node.getDirectives().forEach(tree -> tree.accept(this, p));
        node.getName().accept(this, p);
        return null;
    }

    @Override
    public R visitExports(ExportsTree node, P p) {
        node.getModuleNames().forEach(tree -> tree.accept(this, p));
        node.getPackageName().accept(this, p);
        return null;
    }

    @Override
    public R visitOpens(OpensTree node, P p) {
        node.getModuleNames().forEach(tree -> tree.accept(this, p));
        node.getPackageName().accept(this, p);
        return null;
    }

    @Override
    public R visitProvides(ProvidesTree node, P p) {
        node.getImplementationNames().forEach(tree -> tree.accept(this, p));
        node.getServiceName().accept(this, p);
        return null;
    }

    @Override
    public R visitRequires(RequiresTree node, P p) {
        node.getModuleName().accept(this, p);
        return null;
    }

    @Override
    public R visitUses(UsesTree node, P p) {
        node.getServiceName().accept(this, p);
        return null;
    }

    @Override
    public R visitOther(Tree node, P p) {
        return null;
    }

    @Override
    public R visitYield(YieldTree node, P p) {
        return null;
    }
}
