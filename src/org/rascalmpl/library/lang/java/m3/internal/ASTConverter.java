package org.rascalmpl.library.lang.java.m3.internal;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.jdt.core.dom.*;

import io.usethesource.vallang.IList;
import io.usethesource.vallang.IListWriter;
import io.usethesource.vallang.ISourceLocation;
import io.usethesource.vallang.IString;
import io.usethesource.vallang.IValue;

@SuppressWarnings({"rawtypes"})
public class ASTConverter extends JavaToRascalConverter {
    /* 
     * TODO:
     * Type parameters need to come out of annotations
     * calls may need to be broken up into superconstructor, constructor, supermethod, method calls or separate them in bindings
     */
    public ASTConverter(final LimitedTypeStore typeStore, Map<String, ISourceLocation> cache, boolean collectBindings) {
        super(typeStore, cache, collectBindings);
    }

    @Override
    public void postVisit(ASTNode node) {
        if (getAdtType() == DATATYPE_RASCAL_AST_MODIFIER_NODE_TYPE || getAdtType() == DATATYPE_RASCAL_AST_TYPE_NODE_TYPE) {
            return;
        }
        setKeywordParameter("src", getSourceLocation(node));

        if (collectBindings) {
            ISourceLocation decl = resolveBinding(node);
            if (!decl.getScheme().equals("unknown")) {
                setKeywordParameter("decl", decl); 
            }
            if (getAdtType() != DATATYPE_RASCAL_AST_STATEMENT_NODE_TYPE) {
                IValue type = resolveType(node);

                setKeywordParameter("typ", type);
            }
        }
    }

    private IValue resolveType(ASTNode node) {
        try {
            if (node instanceof Expression) {
                if (node instanceof Name) {
                    IBinding b = ((Name) node).resolveBinding();
                    return bindingsResolver.resolveType(b, false);
                }
                ITypeBinding binding = ((Expression) node).resolveTypeBinding();
                return bindingsResolver.resolveType(binding, false);
            }
            else if (node instanceof TypeDeclaration) {
                ITypeBinding binding = ((TypeDeclaration) node).resolveBinding();
                return bindingsResolver.resolveType(binding, true);
            }
            else if (node instanceof MethodDeclaration) {
                IMethodBinding binding = ((MethodDeclaration) node).resolveBinding();
                return bindingsResolver.resolveType(binding, true);
            }
            else if (node instanceof VariableDeclaration) {
                IVariableBinding binding = ((VariableDeclaration) node).resolveBinding();
                return bindingsResolver.resolveType(binding.getType(), false);
            }
        } catch (NullPointerException e) {
            System.err.println("Got NPE for node " + node + ((e.getStackTrace().length > 0) ? ("\n\t" + e.getStackTrace()[0]) : ""));
        }

        return null;
    }

    
    @Override
    public boolean visit(AnnotationTypeDeclaration node) {
        IList extendedModifiers = parseExtendedModifiers(node.modifiers());
        IValue name = values.string(node.getName().getFullyQualifiedName());

        IListWriter bodyDeclarations = values.listWriter();
        for (Iterator it = node.bodyDeclarations().iterator(); it.hasNext();) {
            BodyDeclaration d = (BodyDeclaration) it.next();
            bodyDeclarations.append(visitChild(d));
        }

        ownValue = constructDeclarationNode("annotationType", name, bodyDeclarations.done());
        setKeywordParameters("modifiers", extendedModifiers);
        return false;
    }

    @Override
    public boolean visit(AnnotationTypeMemberDeclaration node) {
        IList extendedModifiers = parseExtendedModifiers(node.modifiers());
        IValue typeArgument = visitChild(node.getType());

        String name = node.getName().getFullyQualifiedName();

        IValue defaultBlock = node.getDefault() == null ? null : visitChild(node.getDefault());
        ownValue = constructDeclarationNode("annotationTypeMember", typeArgument, values.string(name), defaultBlock);
        setKeywordParameters("modifiers", extendedModifiers);
        return false;
    }

    @Override
    public boolean visit(AnonymousClassDeclaration node) {
        IListWriter bodyDeclarations = values.listWriter();

        for (Iterator it = node.bodyDeclarations().iterator(); it.hasNext();) {
            BodyDeclaration b = (BodyDeclaration) it.next();
            bodyDeclarations.append(visitChild(b));
        }
        ownValue = constructDeclarationNode("class", bodyDeclarations.done());

        return false;
    }

    @Override
    public boolean visit(ArrayAccess node) {
        IValue array = visitChild(node.getArray());
        IValue index = visitChild(node.getIndex());

        ownValue = constructExpressionNode("arrayAccess", array, index);

        return false;
    }

    @Override
    public boolean visit(ArrayCreation node) {
        IValue type = visitChild(node.getType().getElementType());

        IListWriter dimensions = values.listWriter();
        for (Iterator it = node.dimensions().iterator(); it.hasNext();) {
            Expression e = (Expression) it.next();
            dimensions.append(visitChild(e));
        }

        IValue initializer = node.getInitializer() == null ? null : visitChild(node.getInitializer());

        ownValue = constructExpressionNode("newArray", type, dimensions.done(), initializer);

        return false;
    }

    @Override
    public boolean visit(ArrayInitializer node) {
        IListWriter expressions = values.listWriter();
        for (Iterator it = node.expressions().iterator(); it.hasNext();) {
            Expression e = (Expression) it.next();
            expressions.append(visitChild(e));
        }

        ownValue = constructExpressionNode("arrayInitializer", expressions.done());

        return false;
    }

    @Override
    public boolean visit(ArrayType node) {
        IValue type = visitChild(node.getComponentType());

        ownValue = constructTypeNode("arrayType", type);

        return false;
    }

    @Override
    public boolean visit(AssertStatement node) {
        IValue expression = visitChild(node.getExpression());
        IValue message = node.getMessage() == null ? null : visitChild(node.getMessage());

        ownValue = constructStatementNode("assert", expression, message);

        return false;
    }

    @Override
    public boolean visit(Assignment node) {
        IValue leftSide = visitChild(node.getLeftHandSide());
        IValue rightSide = visitChild(node.getRightHandSide());

        ownValue = constructExpressionNode("assignment", leftSide, values.string(node.getOperator().toString()), rightSide);

        return false;
    }

    @Override
    public boolean visit(Block node) {
        IListWriter statements = values.listWriter();
        for (Iterator it = node.statements().iterator(); it.hasNext();) {
            Statement s = (Statement) it.next();
            statements.append(visitChild(s));
        }

        ownValue = constructStatementNode("block", statements.done());

        return false;
    }
    
    @Override
    public boolean visit(BlockComment node) {
        return false;
    }

    @Override
    public boolean visit(BooleanLiteral node) {
        IValue booleanValue = values.bool(node.booleanValue());

        ownValue = constructExpressionNode("booleanLiteral", booleanValue);

        return false;
    }

    @Override
    public boolean visit(BreakStatement node) {
        IValue label = node.getLabel() == null ? values.string("") : values.string(node.getLabel().getFullyQualifiedName());
        ownValue = constructStatementNode("break", label);

        return false;
    }

    @Override
    public boolean visit(CastExpression node) {
        IValue type = visitChild(node.getType());
        IValue expression = visitChild(node.getExpression());

        ownValue = constructExpressionNode("cast", type, expression);

        return false;
    }

    @Override
    public boolean visit(CatchClause node) {
        IValue exception = visitChild(node.getException());
        IValue body = visitChild(node.getBody());

        ownValue = constructStatementNode("catch", exception, body);

        return false;
    }

    @Override
    public boolean visit(CharacterLiteral node) {
        IValue value = values.string(node.getEscapedValue()); 

        ownValue = constructExpressionNode("characterLiteral", value);

        return false;
    }

    @Override
    public boolean visit(ClassInstanceCreation node) {
        IValue expression = node.getExpression() == null ? null : visitChild(node.getExpression());

        IValue type = null;
        IListWriter genericTypes = values.listWriter();
        if (node.getAST().apiLevel() == AST.JLS2) {
            type = visitChild(node.getName());
        } 
        else {
            type = visitChild(node.getType()); 

            if (!node.typeArguments().isEmpty()) {
                for (Iterator it = node.typeArguments().iterator(); it.hasNext();) {
                    Type t = (Type) it.next();
                    genericTypes.append(visitChild(t));
                }
            }
        }

        IListWriter arguments = values.listWriter();
        for (Iterator it = node.arguments().iterator(); it.hasNext();) {
            Expression e = (Expression) it.next();
            arguments.append(visitChild(e));
        }

        IValue anonymousClassDeclaration = node.getAnonymousClassDeclaration() == null ? null : visitChild(node.getAnonymousClassDeclaration());

        ownValue = constructExpressionNode("newObject", expression, type, arguments.done(), anonymousClassDeclaration);
        //setKeywordParameters("typeParameters", genericTypes);
        return false;
    }

    @Override
    public boolean visit(CompilationUnit node) {
        IValue packageOfUnit = node.getPackage() == null ? null : visitChild(node.getPackage());

        IListWriter imports = values.listWriter();
        for (Iterator it = node.imports().iterator(); it.hasNext();) {
            ImportDeclaration d = (ImportDeclaration) it.next();
            imports.append(visitChild(d));
        }

        IListWriter typeDeclarations = values.listWriter();
        for (Iterator it = node.types().iterator(); it.hasNext();) {
            AbstractTypeDeclaration d = (AbstractTypeDeclaration) it.next();
            typeDeclarations.append(visitChild(d));
        }

        ownValue = constructDeclarationNode("compilationUnit", packageOfUnit, imports.done(), typeDeclarations.done());		
        return false;
    }

    @Override
    public boolean visit(ConditionalExpression node) {
        IValue expression = visitChild(node.getExpression());
        IValue thenBranch = visitChild(node.getThenExpression());
        IValue elseBranch = visitChild(node.getElseExpression());

        ownValue = constructExpressionNode("conditional", expression, thenBranch, elseBranch);

        return false;
    }

    @Override
    public boolean visit(ConstructorInvocation node) {
        IListWriter types = values.listWriter();
        if (node.getAST().apiLevel() >= AST.JLS3) {
            if (!node.typeArguments().isEmpty()) {

                for (Iterator it = node.typeArguments().iterator(); it.hasNext();) {
                    Type t = (Type) it.next();
                    types.append(visitChild(t));
                }
            }
        }

        IListWriter arguments = values.listWriter();
        for (Iterator it = node.arguments().iterator(); it.hasNext();) {
            Expression e = (Expression) it.next();
            arguments.append(visitChild(e));
        }

        ownValue = constructStatementNode("constructorCall", values.bool(false),  arguments.done());
        //setKeywordParameters("typeParameters", types);

        return false;
    }

    @Override
    public boolean visit(ContinueStatement node) {

        IValue label = node.getLabel() == null ? null : values.string(node.getLabel().getFullyQualifiedName());
        ownValue = constructStatementNode("continue", label);

        return false;
    }

    @Override
    public boolean visit(DoStatement node) {

        IValue body = visitChild(node.getBody());
        IValue whileExpression = visitChild(node.getExpression());

        ownValue = constructStatementNode("do", body, whileExpression);

        return false;
    }

    @Override
    public boolean visit(EmptyStatement node) {

        ownValue = constructStatementNode("empty");

        return false;
    }

    @Override
    public boolean visit(EnhancedForStatement node) {

        IValue parameter = visitChild(node.getParameter());
        IValue collectionExpression = visitChild(node.getExpression());
        IValue body = visitChild(node.getBody());

        ownValue = constructStatementNode("foreach", parameter, collectionExpression, body);

        return false;
    }

    @Override
    public boolean visit(EnumConstantDeclaration node) {

        IList extendedModifiers = parseExtendedModifiers(node.modifiers());
        IValue name = values.string(node.getName().getFullyQualifiedName()); 

        IListWriter arguments = values.listWriter();
        if (!node.arguments().isEmpty()) {
            for (Iterator it = node.arguments().iterator(); it.hasNext();) {
                Expression e = (Expression) it.next();
                arguments.append(visitChild(e));
            }
        }

        IValue anonymousClassDeclaration = node.getAnonymousClassDeclaration() == null ? null : visitChild(node.getAnonymousClassDeclaration());

        ownValue = constructDeclarationNode("enumConstant", name, arguments.done(), anonymousClassDeclaration);
        setKeywordParameters("modifiers", extendedModifiers);
        return false;
    }

    @Override
    public boolean visit(EnumDeclaration node) {

        IList extendedModifiers = parseExtendedModifiers(node.modifiers());
        IValue name = values.string(node.getName().getFullyQualifiedName()); 

        IListWriter implementedInterfaces = values.listWriter();
        if (!node.superInterfaceTypes().isEmpty()) {
            for (Iterator it = node.superInterfaceTypes().iterator(); it.hasNext();) {
                Type t = (Type) it.next();
                implementedInterfaces.append(visitChild(t));
            }
        }

        IListWriter enumConstants = values.listWriter();
        for (Iterator it = node.enumConstants().iterator(); it.hasNext();) {
            EnumConstantDeclaration d = (EnumConstantDeclaration) it.next();
            enumConstants.append(visitChild(d));
        }

        IListWriter bodyDeclarations = values.listWriter();
        if (!node.bodyDeclarations().isEmpty()) {
            for (Iterator it = node.bodyDeclarations().iterator(); it.hasNext();) {
                BodyDeclaration d = (BodyDeclaration) it.next();
                bodyDeclarations.append(visitChild(d));
            }
        }

        ownValue = constructDeclarationNode("enum", name, implementedInterfaces.done(), enumConstants.done(), bodyDeclarations.done());
        setKeywordParameters("modifiers", extendedModifiers);
        return false;
    }

    @Override
    public boolean visit(ExpressionStatement node) {

        IValue expression = visitChild(node.getExpression());
        ownValue = constructStatementNode("expressionStatement", expression);

        return false;
    }

    @Override
    public boolean visit(FieldAccess node) {

        IValue expression = visitChild(node.getExpression());
        IValue name = values.string(node.getName().getFullyQualifiedName());

        ownValue = constructExpressionNode("fieldAccess", values.bool(false), expression, name);

        return false;
    }

    @Override
    public boolean visit(FieldDeclaration node) {

        IList extendedModifiers = parseExtendedModifiers(node);
        IValue type = visitChild(node.getType());

        IListWriter fragments = values.listWriter();
        for (Iterator it = node.fragments().iterator(); it.hasNext();) {
            VariableDeclarationFragment f = (VariableDeclarationFragment) it.next();
            fragments.append(visitChild(f));
        }

        ownValue = constructDeclarationNode("field", type, fragments.done());
        setKeywordParameters("modifiers", extendedModifiers);
        return false;
    }

    @Override
    public boolean visit(ForStatement node) {

        IListWriter initializers = values.listWriter();
        for (Iterator it = node.initializers().iterator(); it.hasNext();) {
            Expression e = (Expression) it.next();
            initializers.append(visitChild(e));
        }

        IValue booleanExpression = node.getExpression() == null ? null : visitChild(node.getExpression());

        IListWriter updaters = values.listWriter();
        for (Iterator it = node.updaters().iterator(); it.hasNext();) {
            Expression e = (Expression) it.next();
            updaters.append(visitChild(e));
        }

        IValue body = visitChild(node.getBody());

        ownValue = constructStatementNode("for", initializers.done(), booleanExpression, updaters.done(), body);

        return false;
    }

    @Override
    public boolean visit(IfStatement node) {

        IValue booleanExpression = visitChild(node.getExpression());
        IValue thenStatement = visitChild(node.getThenStatement());
        IValue elseStatement = node.getElseStatement() == null ? null : visitChild(node.getElseStatement());

        ownValue = constructStatementNode("if", booleanExpression, thenStatement, elseStatement);

        return false;
    }

    @Override
    public boolean visit(ImportDeclaration node) {
        String name = node.getName().getFullyQualifiedName();

        IListWriter importModifiers = values.listWriter();
        if (node.getAST().apiLevel() >= AST.JLS3) {
            if (node.isStatic())
                importModifiers.append(constructModifierNode("static"));

            if (node.isOnDemand())
                importModifiers.append(constructModifierNode("onDemand"));
        }

        ownValue = constructDeclarationNode("import", values.string(name));
        setKeywordParameters("modifiers", importModifiers.done());

        return false;
    }

    @Override
    public boolean visit(InfixExpression node) {

        IValue operator = values.string(node.getOperator().toString());
        IValue leftSide = visitChild(node.getLeftOperand());
        IValue rightSide = visitChild(node.getRightOperand());

        IValue intermediateExpression = constructExpressionNode("infix", leftSide, operator, rightSide);
        for (Iterator it = node.extendedOperands().iterator(); it.hasNext();) {
            Expression e = (Expression) it.next();
            intermediateExpression = constructExpressionNode("infix", intermediateExpression, operator, visitChild(e));
        }

        ownValue = intermediateExpression;

        return false;
    }

    @Override
    public boolean visit(Initializer node) {

        IList extendedModifiers = parseExtendedModifiers(node);
        IValue body = visitChild(node.getBody());

        ownValue = constructDeclarationNode("initializer", body);
        setKeywordParameters("modifiers", extendedModifiers);
        return false;
    }

    @Override
    public boolean visit(InstanceofExpression node) {

        IValue leftSide = visitChild(node.getLeftOperand());
        IValue rightSide = visitChild(node.getRightOperand());

        ownValue = constructExpressionNode("instanceof", leftSide, rightSide);

        return false;
    }

    @Override
    public boolean visit(Javadoc node) {

        return false;
    }

    @Override
    public boolean visit(LabeledStatement node) {

        IValue label = values.string(node.getLabel().getFullyQualifiedName());
        IValue body = visitChild(node.getBody());

        ownValue = constructStatementNode("label", label, body);

        return false;
    }

    @Override
    public boolean visit(LineComment node) {

        return false;
    }

    @Override
    public boolean visit(MarkerAnnotation node) {

        IValue typeName = values.string(node.getTypeName().getFullyQualifiedName());
        ownValue = constructExpressionNode("markerAnnotation", typeName);

        return false;
    }

    @Override
    public boolean visit(MemberRef node) {
        return false;
    }

    @Override
    public boolean visit(MemberValuePair node) {

        IValue name = values.string(node.getName().getFullyQualifiedName());
        IValue value = visitChild(node.getValue());

        ownValue = constructExpressionNode("memberValuePair", name, value);

        return false;
    }

    @Override
    public boolean visit(MethodDeclaration node) {
        String constructorName = "method";
        IList extendedModifiers = parseExtendedModifiers(node);

        IListWriter genericTypes = values.listWriter();
        if (node.getAST().apiLevel() >= AST.JLS3) {
            if (!node.typeParameters().isEmpty()) {
                for (Iterator it = node.typeParameters().iterator(); it.hasNext();) {
                    TypeParameter t = (TypeParameter) it.next();
                    genericTypes.append(visitChild(t));
                }
            }
        }

        IValue returnType = null;
        if (!node.isConstructor()) {
            if (node.getAST().apiLevel() == AST.JLS2) {
                returnType = visitChild(node.getReturnType());
            } else if (node.getReturnType2() != null) {
                returnType = visitChild(node.getReturnType2());
            } else {


                returnType = constructTypeNode("void");
            }
        } else {
            constructorName = "constructor";
        }

        IValue name = values.string(node.getName().getFullyQualifiedName());

        IListWriter parameters = values.listWriter();
        for (Iterator it = node.parameters().iterator(); it.hasNext();) {
            SingleVariableDeclaration v = (SingleVariableDeclaration) it.next();
            parameters.append(visitChild(v));
        }

        IListWriter possibleExceptions = values.listWriter();

        var apiLevel = node.getAST().apiLevel();
        if (apiLevel == AST.JLS2 || apiLevel == AST.JLS3 || apiLevel == AST.JLS4) {
            if (!node.thrownExceptions().isEmpty()) {
                for (Iterator it = node.thrownExceptions().iterator(); it.hasNext();) {
                    Name n = (Name) it.next();
                    possibleExceptions.append(visitChild(n));
                }
            }
        }
        else {
            if (!node.thrownExceptionTypes().isEmpty()) {
                ((List<?>) node.thrownExceptionTypes()).stream()
                    .map(o -> ((ASTNode) o))
                    .map((ASTNode n) -> visitChild(n))
                    .collect(possibleExceptions);
            }
        }

        IValue body = node.getBody() == null ? null : visitChild(node.getBody()); 
        if (body == null && constructorName.equals("constructor")) {
            body = constructStatementNode("empty");
        }

        ownValue = constructDeclarationNode(constructorName, returnType, name, parameters.done(), possibleExceptions.done(), body);
        setKeywordParameters("modifiers", extendedModifiers);
        // FIXME: this doesn't seem to be in use anymore
        //setKeywordParameters("typeParameters", genericTypes);
        return false;
    }

    @Override
    public boolean visit(MethodInvocation node) {

        IValue expression = node.getExpression() == null ? null : visitChild(node.getExpression());

        IListWriter genericTypes = values.listWriter();
        if (node.getAST().apiLevel() >= AST.JLS3) {
            if (!node.typeArguments().isEmpty()) {
                for (Iterator it = node.typeArguments().iterator(); it.hasNext();) {
                    Type t = (Type) it.next();
                    genericTypes.append(visitChild(t));
                }
            }
        }

        IValue name = values.string(node.getName().getFullyQualifiedName());

        IListWriter arguments = values.listWriter();
        for (Iterator it = node.arguments().iterator(); it.hasNext();) {
            Expression e = (Expression) it.next();
            arguments.append(visitChild(e));
        }		

        ownValue = constructExpressionNode("methodCall", values.bool(false), expression, name, arguments.done());
        //setKeywordParameters("typeParameters", genericTypes);
        return false;
    }

    @Override
    public boolean visit(MethodRef node) {
        return false;
    }

    @Override
    public boolean visit(MethodRefParameter node) {
        return false;
    }

    @Override
    public boolean visit(Modifier node) {
        String modifier = node.getKeyword().toString();
        ownValue = constructModifierNode(modifier);

        return false;
    }

    @Override
    public boolean visit(NormalAnnotation node) {

        IValue typeName = values.string(node.getTypeName().getFullyQualifiedName());

        IListWriter memberValuePairs = values.listWriter();
        for (Iterator it = node.values().iterator(); it.hasNext();) {
            MemberValuePair p = (MemberValuePair) it.next();
            memberValuePairs.append(visitChild(p));
        }

        ownValue = constructExpressionNode("normalAnnotation", typeName, memberValuePairs.done());

        return false;
    }

    @Override
    public boolean visit(NullLiteral node) {

        ownValue = constructExpressionNode("null");

        return false;
    }

    @Override
    public boolean visit(NumberLiteral node) {

        IValue number = values.string(node.getToken());

        ownValue = constructExpressionNode("number", number);

        return false;
    }

    @Override
    public boolean visit(PackageDeclaration node) {
        IList annotations = parseExtendedModifiers(node.annotations());

        ownValue = null;
        for (String component: node.getName().getFullyQualifiedName().split("\\.")) {
            if (ownValue == null) {
                ownValue = constructDeclarationNode("package", values.string(component));
                setKeywordParameter("decl", resolveBinding(component));
                continue;
            }
            ownValue = constructDeclarationNode("package", ownValue, values.string(component));
            setKeywordParameter("decl", resolveBinding(component));
        }
        setKeywordParameters("modifiers", annotations);
        return false;
    }

    @Override
    public boolean visit(ParameterizedType node) {

        IValue type = visitChild(node.getType());

        IListWriter genericTypes = values.listWriter();
        for (Iterator it = node.typeArguments().iterator(); it.hasNext();) {
            Type t = (Type) it.next();
            genericTypes.append(visitChild(t));
        }

        ownValue = constructTypeNode("parameterizedType", type);
        //setKeywordParameters("typeParameters", genericTypes);
        return false;
    }

    @Override
    public boolean visit(ParenthesizedExpression node) {

        IValue expression = visitChild(node.getExpression());
        ownValue = constructExpressionNode("bracket", expression);

        return false;
    }

    @Override
    public boolean visit(PostfixExpression node) {

        IValue operand = visitChild(node.getOperand());
        IValue operator = values.string(node.getOperator().toString());

        ownValue = constructExpressionNode("postfix", operand, operator);

        return false;
    }

    @Override
    public boolean visit(PrefixExpression node) {

        IValue operand = visitChild(node.getOperand());
        IValue operator = values.string(node.getOperator().toString());

        ownValue = constructExpressionNode("prefix", operator, operand);

        return false;
    }

    @Override
    public boolean visit(PrimitiveType node) {
        ownValue = constructTypeNode(node.toString());

        return false;
    }

    @Override
    public boolean visit(QualifiedName node) {

        IValue qualifier = visitChild(node.getQualifier());


        IValue name = visitChild(node.getName());

        ownValue = constructExpressionNode("qualifiedName", qualifier, name);

        return false;
    }

    @Override
    public boolean visit(QualifiedType node) {
        IValue qualifier = visitChild(node.getQualifier());


        IValue name = visitChild(node.getName());

        ownValue = constructTypeNode("qualifiedType", qualifier, name);

        return false;
    }

    @Override
    public boolean visit(ReturnStatement node) {

        IValue expression = node.getExpression() == null ? null : visitChild(node.getExpression());
        ownValue = constructStatementNode("return", expression);

        return false;
    }

    @Override
    public boolean visit(SimpleName node) {

        IValue value = values.string(node.getFullyQualifiedName());

        ownValue = constructExpressionNode("simpleName", value);

        return false;
    }

    @Override
    public boolean visit(SimpleType node) {
        IValue value = visitChild(node.getName());
        ownValue = constructTypeNode("simpleType", value);

        return false;
    }

    @Override
    public boolean visit(SingleMemberAnnotation node) {

        IValue name = values.string(node.getTypeName().getFullyQualifiedName());
        IValue value = visitChild(node.getValue());

        ownValue = constructExpressionNode("singleMemberAnnotation", name, value);

        return false;
    }

    @Override
    public boolean visit(SingleVariableDeclaration node) {

        IValue name = values.string(node.getName().getFullyQualifiedName());

        IList extendedModifiers = parseExtendedModifiers(node.modifiers());

        IValue type = visitChild(node.getType());
        IValue initializer = node.getInitializer() == null ? null : visitChild(node.getInitializer());

        ownValue = constructDeclarationNode("parameter", type, name, values.integer(node.getExtraDimensions()), initializer);
        if (node.getAST().apiLevel() >= AST.JLS3 && node.isVarargs())
            ownValue = constructDeclarationNode("vararg", type, name);

        setKeywordParameters("modifiers", extendedModifiers);

        return false;
    }

    @Override
    public boolean visit(StringLiteral node) {

        IString escaped = values.string(node.getEscapedValue());		
        IString literal = values.string(node.getLiteralValue());
        ownValue = constructExpressionNode("stringLiteral", escaped).asWithKeywordParameters().setParameter("literal", literal);

        return false;
    }

    @Override
    public boolean visit(TextBlock node) {
        IString escaped = values.string(node.getEscapedValue());		
        IString literal = values.string(node.getLiteralValue());
        ownValue = constructExpressionNode("textBlock", escaped).asWithKeywordParameters().setParameter("literal", literal);

        return false;
    }


    @Override
    public boolean visit(YieldStatement node) {
        IValue exp = visitChild(node.getExpression());
        ownValue = constructStatementNode("yield", exp);
        return false;
    }

    
    @Override
    public boolean visit(LambdaExpression node) {
        IListWriter parameters = values.listWriter();
        for (Iterator it = node.parameters().iterator(); it.hasNext();) {
            SingleVariableDeclaration v = (SingleVariableDeclaration) it.next();
            parameters.append(visitChild(v));
        }

        ownValue = constructExpressionNode("lambda", parameters.done(), visitChild(node.getBody()));
        return false;
    }

    @Override
    public boolean visit(SwitchExpression node) {
        IValue expression = visitChild(node.getExpression());

        IListWriter statements = values.listWriter();
        for (Iterator it = node.statements().iterator(); it.hasNext();) {
            Statement s = (Statement) it.next();
            statements.append(visitChild(s));
        }

        ownValue = constructExpressionNode("switch", expression, statements.done());

        return false;
    }

    @Override
    public boolean visit(SuperConstructorInvocation node) {

        IValue expression = node.getExpression() == null ? null : visitChild(node.getExpression());

        IListWriter genericTypes = values.listWriter();	
        if (node.getAST().apiLevel() >= AST.JLS3) {
            if (!node.typeArguments().isEmpty()) {
                for (Iterator it = node.typeArguments().iterator(); it.hasNext();) {
                    Type t = (Type) it.next();
                    genericTypes.append(visitChild(t));
                }
            }
        }

        IListWriter arguments = values.listWriter();
        for (Iterator it = node.arguments().iterator(); it.hasNext();) {
            Expression e = (Expression) it.next();
            arguments.append(visitChild(e));
        }

        ownValue = constructStatementNode("constructorCall", values.bool(true), expression, arguments.done());
        //setKeywordParameters("typeParameters", genericTypes);
        return false;
    }

    @Override
    public boolean visit(SuperFieldAccess node) {

        IValue qualifier = node.getQualifier() == null ? null : visitChild(node.getQualifier());
        IValue name = values.string((node.getName().getFullyQualifiedName()));

        ownValue = constructExpressionNode("fieldAccess", values.bool(true), qualifier, name);

        return false;
    }

    @Override
    public boolean visit(SuperMethodInvocation node) {

        IValue qualifier = node.getQualifier() == null ? null : visitChild(node.getQualifier());

        IListWriter genericTypes = values.listWriter();
        if (node.getAST().apiLevel() >= AST.JLS3) {
            if (!node.typeArguments().isEmpty()) {
                for (Iterator it = node.typeArguments().iterator(); it.hasNext();) {
                    Type t = (Type) it.next();
                    genericTypes.append(visitChild(t));
                }
            }
        }

        IValue name = values.string(node.getName().getFullyQualifiedName());

        IListWriter arguments = values.listWriter();
        for (Iterator it = node.arguments().iterator(); it.hasNext();) {
            Expression e = (Expression) it.next();
            arguments.append(visitChild(e));
        }

        ownValue = constructExpressionNode("methodCall", values.bool(true), qualifier, name, arguments.done());
        //setKeywordParameters("typeParameters", genericTypes);
        return false;
    }

    @Override
    public boolean visit(SwitchCase node) {
        IValue expression = node.getExpression() == null ? null : visitChild(node.getExpression());
        List<?> expressions = node.expressions();

        String constructorName = "case";

        IList exprs = expression == null 
            ? values.list(expression)
            : expressions.stream()
                .map(o -> (Expression) o)
                .map(e -> visitChild(e)).collect(values.listWriter());
        
        if (node.isSwitchLabeledRule()) {
            constructorName = "caseRule";
            ownValue = constructStatementNode(constructorName, exprs);
        }
        else if (node.isDefault()) {
            constructorName = "defaultCase";
            ownValue = constructStatementNode(constructorName);
        }
        else {
            ownValue = constructStatementNode(constructorName, exprs);
        }

        return false;
    }

    @Override
    public boolean visit(TypeMethodReference node) {
        IValue type = visitChild(node.getType());
        IList args = ((List<?>) node.typeArguments())
            .stream()
            .map(o -> (Type) o).map(t -> visitChild(t)).collect(values.listWriter());
        IString name = values.string(node.getName().getIdentifier());

        ownValue = constructExpressionNode("methodReference", type, args, name);
        return false;
    }

    @Override
    public boolean visit(ExpressionMethodReference node) {
        IValue type = visitChild(node.getExpression());
        IList args = ((List<?>) node.typeArguments())
            .stream()
            .map(o -> (Type) o).map(t -> visitChild(t)).collect(values.listWriter());
        IString name = values.string(node.getName().getIdentifier());

        ownValue = constructExpressionNode("methodReference", type, args, name);
        return false;
    }

    @Override
    public boolean visit(ModuleDeclaration node) {
        IList mod = node.isOpen() ? values.list(constructModifierNode("open")) : values.list();
        IString name = node.getName().isSimpleName() 
            ? values.string(((SimpleName) node.getName()).getIdentifier()) 
            : values.string(node.getName().getFullyQualifiedName());

        IList stats
            = ((List<?>) node.moduleStatements())
                .stream()
                .map(o -> (ASTNode) o)
                .map(s -> visitChild(s))
                .collect(values.listWriter());

        ownValue = constructDeclarationNode("module", mod, name, stats);
        return false;
    }

    @Override
    public boolean visit(ModuleModifier node) {
        if (node.isStatic()) {
            ownValue = constructModifierNode("static");
        }
        else if (node.isTransitive()) {
            ownValue = constructModifierNode("static");
        }
        else {
            // unknown module requirement modifier?
            assert false;
        }

        return false;
    }

    @Override
    public boolean visit(OpensDirective node) {
        IString name = values.string(node.getName().getFullyQualifiedName());
        IList modules = ((List<?>) node.modules()).stream()
            .map(e -> ((ASTNode) e)) 
            .map(n -> visitChild(n)) // TODO: check what type of AST node is used for the module names
            .collect(values.listWriter());
        
        ownValue = constructDeclarationNode("opensPackage", name, modules);
        return false;
    }

    @Override
    public boolean visit(ProvidesDirective node) {
        IString name = values.string(node.getName().getFullyQualifiedName());
        IList implementations = ((List<?>) node.implementations()).stream()
            .map(e -> ((ASTNode) e)) 
            .map(n -> visitChild(n)) // TODO: check what type of AST node is used for the module names
            .collect(values.listWriter());
        
        ownValue = constructDeclarationNode("providesImplementations", name, implementations);
        return false;
    }

    @Override
    public boolean visit(RequiresDirective node) {
        IList modifiers = ((List<?>) node.modifiers()).stream()
            .map(e -> ((ASTNode) e)) 
            .map(n -> visitChild(n)) 
            .collect(values.listWriter());
        IString name = values.string(node.getName().getFullyQualifiedName());

        ownValue = constructDeclarationNode("requires", modifiers, name);
        return false;
    }

    @Override
    public boolean visit(UsesDirective node) {
        IString name = values.string(node.getName().getFullyQualifiedName());

        ownValue = constructDeclarationNode("uses", name);
        return false;
    }

    @Override
    public boolean visit(ExportsDirective node) {
        IString name = values.string(node.getName().getFullyQualifiedName());

        ownValue = constructDeclarationNode("exports", name);
        return false;
    }

    @Override
    public boolean visit(SuperMethodReference node) {
        IList args = ((List<?>) node.typeArguments())
            .stream().map(o -> (Type) o)
            .map(t -> visitChild(t)).collect(values.listWriter());
        IString name = values.string(node.getName().getIdentifier());

        ownValue = constructExpressionNode("superMethodReference", args, name);
        return false;
    }
        
    @Override
    public boolean visit(SwitchStatement node) {
        IValue expression = visitChild(node.getExpression());

        IListWriter statements = values.listWriter();
        for (Iterator it = node.statements().iterator(); it.hasNext();) {
            Statement s = (Statement) it.next();
            statements.append(visitChild(s));
        }

        ownValue = constructStatementNode("switch", expression, statements.done());

        return false;
    }

    @Override
    public boolean visit(SynchronizedStatement node) {

        IValue expression = visitChild(node.getExpression());
        IValue body = visitChild(node.getBody());

        ownValue = constructStatementNode("synchronizedStatement", expression, body);

        return false;
    }

    @Override
    public boolean visit(TagElement node) {
        // TODO: what should be added here?
        return false;
    }

    @Override
    public boolean visit(TextElement node) {
        // TODO: what should happen here?
        return false;
    }

    @Override
    public boolean visit(ThisExpression node) {

        IValue qualifier = node.getQualifier() == null ? null : visitChild(node.getQualifier());

        ownValue = constructExpressionNode("this", qualifier);

        return false;
    }

    @Override
    public boolean visit(ThrowStatement node) {

        IValue expression = visitChild(node.getExpression());

        ownValue = constructStatementNode("throw", expression);

        return false;
    }

    @Override
    public boolean visit(TryStatement node) {

        IValue body = visitChild(node.getBody());

        IListWriter catchClauses = values.listWriter();
        for (Iterator it = node.catchClauses().iterator(); it.hasNext();) {
            CatchClause cc = (CatchClause) it.next();
            catchClauses.append(visitChild(cc));
        }

        IValue finallyBlock = node.getFinally() == null ? null : visitChild(node.getFinally()); 

        ownValue = constructStatementNode("try", body, catchClauses.done(), finallyBlock);

        return false;
    }

    @Override
    public boolean visit(TypeDeclaration node) {

        IList extendedModifiers = parseExtendedModifiers(node);
        String objectType = node.isInterface() ? "interface" : "class";
        IValue name = values.string(node.getName().getFullyQualifiedName()); 

        IListWriter genericTypes = values.listWriter();
        if (node.getAST().apiLevel() >= AST.JLS3) {
            if (!node.typeParameters().isEmpty()) {			
                for (Iterator it = node.typeParameters().iterator(); it.hasNext();) {
                    TypeParameter t = (TypeParameter) it.next();
                    genericTypes.append(visitChild(t));			
                }
            }
        }

        IListWriter extendsClass = values.listWriter();
        IListWriter implementsInterfaces = values.listWriter();

        if (node.getAST().apiLevel() == AST.JLS2) {
            if (node.getSuperclass() != null) {
                extendsClass.append(visitChild(node.getSuperclass()));
            }
            if (!node.superInterfaces().isEmpty()) {
                for (Iterator it = node.superInterfaces().iterator(); it.hasNext();) {
                    Name n = (Name) it.next();
                    implementsInterfaces.append(visitChild(n));
                }
            }
        } else if (node.getAST().apiLevel() >= AST.JLS3) {
            if (node.getSuperclassType() != null) {
                extendsClass.append(visitChild(node.getSuperclassType()));
            }
            if (!node.superInterfaceTypes().isEmpty()) {
                for (Iterator it = node.superInterfaceTypes().iterator(); it.hasNext();) {
                    Type t = (Type) it.next();
                    implementsInterfaces.append(visitChild(t));
                }
            }
        }

        IListWriter bodyDeclarations = values.listWriter();
        for (Iterator it = node.bodyDeclarations().iterator(); it.hasNext();) {
            BodyDeclaration d = (BodyDeclaration) it.next();
            bodyDeclarations.append(visitChild(d));
        }

        ownValue = constructDeclarationNode(objectType, name, extendsClass.done(), implementsInterfaces.done(), bodyDeclarations.done());
        setKeywordParameters("modifiers", extendedModifiers);
        //setKeywordParameters("typeParameters", genericTypes);
        return false;
    }

    @Override
    public boolean visit(TypeDeclarationStatement node) {

        IValue typeDeclaration;
        if (node.getAST().apiLevel() == AST.JLS2) {
            typeDeclaration = visitChild(node.getTypeDeclaration());
        }
        else {
            typeDeclaration = visitChild(node.getDeclaration());
        }

        ownValue = constructStatementNode("declarationStatement", typeDeclaration);

        return false;
    }

    @Override
    public boolean visit(TypeLiteral node) {

        IValue type = visitChild(node.getType());

        ownValue = constructExpressionNode("type", type);

        return false;
    }

    @Override
    public boolean visit(TypeParameter node) {

        IValue name = values.string(node.getName().getFullyQualifiedName());

        IListWriter extendsList = values.listWriter();
        if (!node.typeBounds().isEmpty()) {
            for (Iterator it = node.typeBounds().iterator(); it.hasNext();) {
                Type t = (Type) it.next();
                extendsList.append(visitChild(t));
            }
        }

        ownValue = constructDeclarationNode("typeParameter", name, extendsList.done());

        return false;
    }

    @Override
    public boolean visit(UnionType node) {

        IListWriter typesValues = values.listWriter();
        for(Iterator types = node.types().iterator(); types.hasNext();) {
            Type type = (Type) types.next();
            typesValues.append(visitChild(type));
        }

        ownValue = constructTypeNode("unionType", typesValues.done());

        return false;
    }

    @Override
    public boolean visit(IntersectionType node) {

        IListWriter typesValues = values.listWriter();
        for(Iterator types = node.types().iterator(); types.hasNext();) {
            Type type = (Type) types.next();
            typesValues.append(visitChild(type));
        }

        ownValue = constructTypeNode("intersectionType", typesValues.done());

        return false;
    }

    @Override
    public boolean visit(VariableDeclarationExpression node) {

        IList extendedModifiers = parseExtendedModifiers(node.modifiers());


        IValue type = visitChild(node.getType());

        IListWriter fragments = values.listWriter();
        for (Iterator it = node.fragments().iterator(); it.hasNext();) {
            VariableDeclarationFragment f = (VariableDeclarationFragment) it.next();
            fragments.append(visitChild(f));
        }

        ownValue = constructDeclarationNode("variables", type, fragments.done());
        setKeywordParameters("modifiers", extendedModifiers);

        ownValue = constructExpressionNode("declarationExpression", ownValue);


        return false;
    }

    @Override
    public boolean visit(VariableDeclarationFragment node) {

        IValue name = values.string(node.getName().getFullyQualifiedName());

        IValue initializer = node.getInitializer() == null ? null : visitChild(node.getInitializer());

        ownValue = constructExpressionNode("variable", name, values.integer(node.getExtraDimensions()), initializer);


        return false;
    }

    @Override
    public boolean visit(VariableDeclarationStatement node) {

        IList extendedModifiers = parseExtendedModifiers(node.modifiers());


        IValue type = visitChild(node.getType());

        IListWriter fragments = values.listWriter();
        for (Iterator it = node.fragments().iterator(); it.hasNext();) {
            VariableDeclarationFragment f = (VariableDeclarationFragment) it.next();
            fragments.append(visitChild(f));
        }

        ownValue = constructDeclarationNode("variables", type, fragments.done());
        setKeywordParameters("modifiers", extendedModifiers);

        ownValue = constructStatementNode("declarationStatement", ownValue);

        return false;
    }

    @Override
    public boolean visit(WhileStatement node) {
        IValue expression = visitChild(node.getExpression());
        IValue body = visitChild(node.getBody());

        ownValue = constructStatementNode("while", expression, body);

        return false;
    }

    @Override
    public boolean visit(WildcardType node) {
        //FIXME: upperbound/lowerbound that should have been type annotation are replaced by TypeSymbol
        IValue type = null;
        String name = "wildcard";

        if (node.getBound() != null) {
            type = visitChild(node.getBound());
            if (node.isUpperBound()) {
                name = "upperbound";
            } 
            else {
                name = "lowerbound";
            }
        }
        ownValue = constructTypeNode(name, type);

        return false;
    }

}
