package org.rascalmpl.library.experiments.m3.internal;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.eclipse.imp.pdb.facts.IMapWriter;
import org.eclipse.imp.pdb.facts.ISetWriter;
import org.eclipse.imp.pdb.facts.ISourceLocation;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.eclipse.imp.pdb.facts.type.TypeFactory;
import org.eclipse.imp.pdb.facts.type.TypeStore;
import org.eclipse.jdt.core.dom.*;
import org.rascalmpl.values.ValueFactoryFactory;

public class JavaToRascalConverter extends ASTVisitor {
	private static final String DATATYPE_RASCAL_AST_NODE				= "AstNode";
	private static final String DATATYPE_RASCAL_AST_DECLARATION_NODE 	= "Declaration";
	private static final String DATATYPE_RASCAL_AST_EXPRESSION_NODE 	= "Expression";
	private static final String DATATYPE_RASCAL_AST_STATEMENT_NODE 		= "Statement";
	private static final String DATATYPE_RASCAL_AST_TYPE_NODE 			= "Type";
	private static final String DATATYPE_RASCAL_AST_MODIFIER_NODE		= "Modifier";
	
	private IValue ownValue;

	private static final IValueFactory values = ValueFactoryFactory.getValueFactory();
	private static final TypeFactory TF = TypeFactory.getInstance();
	private final TypeStore typeStore;
	
	private BindingsResolver bindingsResolver;
		
	private CompilationUnit compilUnit;
	private ISourceLocation loc;
	private String project;
	
	private final org.eclipse.imp.pdb.facts.type.Type DATATYPE_RASCAL_AST_NODE_TYPE;
	private final org.eclipse.imp.pdb.facts.type.Type DATATYPE_RASCAL_AST_DECLARATION_NODE_TYPE;
	private final org.eclipse.imp.pdb.facts.type.Type DATATYPE_RASCAL_AST_EXPRESSION_NODE_TYPE;
	private final org.eclipse.imp.pdb.facts.type.Type DATATYPE_RASCAL_AST_STATEMENT_NODE_TYPE;
	private final org.eclipse.imp.pdb.facts.type.Type DATATYPE_RASCAL_AST_TYPE_NODE_TYPE;
	private final org.eclipse.imp.pdb.facts.type.Type DATATYPE_RASCAL_AST_MODIFIER_NODE_TYPE;
	
	private static final org.eclipse.imp.pdb.facts.type.Type locType 		= TF.sourceLocationType();
	private static final org.eclipse.imp.pdb.facts.type.Type m3TupleType 	= TF.tupleType(locType, locType);
	private static final org.eclipse.imp.pdb.facts.type.Type m3MapType   	= TF.mapType(locType, locType);
	
	private IMapWriter source;
	private ISetWriter containment;
	private ISetWriter inheritance;
	private ISetWriter access;
	private ISetWriter reference;
	private ISetWriter invocation;
	private ISetWriter imports;
	private IMapWriter types;
	private IMapWriter documentation;
	private ISetWriter modifiers;
	
	public JavaToRascalConverter(final TypeStore typeStore, boolean collectBindings) {
		this.typeStore = typeStore;
		this.bindingsResolver = new BindingsResolver(collectBindings);
		this.DATATYPE_RASCAL_AST_NODE_TYPE				= this.typeStore.lookupAbstractDataType(DATATYPE_RASCAL_AST_NODE);
		this.DATATYPE_RASCAL_AST_DECLARATION_NODE_TYPE 	= this.typeStore.lookupAbstractDataType(DATATYPE_RASCAL_AST_DECLARATION_NODE);
		this.DATATYPE_RASCAL_AST_EXPRESSION_NODE_TYPE 	= this.typeStore.lookupAbstractDataType(DATATYPE_RASCAL_AST_EXPRESSION_NODE);
		this.DATATYPE_RASCAL_AST_STATEMENT_NODE_TYPE 	= this.typeStore.lookupAbstractDataType(DATATYPE_RASCAL_AST_STATEMENT_NODE);
		this.DATATYPE_RASCAL_AST_TYPE_NODE_TYPE 		= this.typeStore.lookupAbstractDataType(DATATYPE_RASCAL_AST_TYPE_NODE);
		this.DATATYPE_RASCAL_AST_MODIFIER_NODE_TYPE		= this.typeStore.lookupAbstractDataType(DATATYPE_RASCAL_AST_MODIFIER_NODE);
	}
	
	public void set(CompilationUnit compilUnit) {
		this.compilUnit = compilUnit;
	}
	
	public void set(ISourceLocation loc) {
		this.loc = loc;
		this.project = loc.getURI().getAuthority();
		bindingsResolver.setProject(this.project);
	}
	
	public IValue getValue() {
		return this.ownValue;
	}
	
	public void set(String project) {
		this.project = project;
	}
	
	private IValueList parseModifiers(int modifiers) {
		IValueList modifierList = new IValueList(values);
		
		for (String constructor: java.lang.reflect.Modifier.toString(modifiers).split(" ")) {
			Set<org.eclipse.imp.pdb.facts.type.Type> constrs = typeStore.lookupConstructor(DATATYPE_RASCAL_AST_MODIFIER_NODE_TYPE, constructor);
			for (org.eclipse.imp.pdb.facts.type.Type constr: constrs) {
				modifierList.add(values.constructor(constr));
			}
		}
		
		
//		if (Modifier.isPublic(modifiers)) {
//			
//			
//		}
//		if (Modifier.isProtected(modifiers)) {
//			modifierList.add(Core.CONS_PROTECTED.make(values));
//		}
//		if (Modifier.isPrivate(modifiers)) {
//			modifierList.add(Core.CONS_PRIVATE.make(values));
//		}
//		if (Modifier.isStatic(modifiers)) {
//			modifierList.add(Core.CONS_STATIC.make(values));
//		}
//		if (Modifier.isAbstract(modifiers)) {
//			modifierList.add(Core.CONS_ABSTRACT.make(values));
//		}
//		if (Modifier.isFinal(modifiers)) {
//			modifierList.add(Core.CONS_FINAL.make(values));
//		}
//		if (Modifier.isSynchronized(modifiers)) {
//			modifierList.add(Core.CONS_SYNCHRONIZED.make(values));
//		}
//		if (Modifier.isVolatile(modifiers)) {
//			modifierList.add(Core.CONS_VOLATILE.make(values));
//		}
//		if (Modifier.isNative(modifiers)) {
//			modifierList.add(Core.CONS_NATIVE.make(values));
//		}
//		if (Modifier.isStrictfp(modifiers)) {
//			modifierList.add(Core.CONS_STRICTFP.make(values));
//		}
//		if (Modifier.isTransient(modifiers)) {
//			modifierList.add(Core.CONS_TRANSIENT.make(values));
//		}
		
		return modifierList;
	}
	
	private java.util.Map.Entry<IValueList, IValueList> parseExtendedModifiers(List ext) {
		IValueList modifierList = new IValueList(values);
		IValueList annotationsList = new IValueList(values);
	
		for (Iterator it = ext.iterator(); it.hasNext();) {
			ASTNode p = (ASTNode) it.next();
			if(p instanceof IExtendedModifier) {
				IValue val = visitChild(p);
				if(((IExtendedModifier) p).isModifier()) {
					modifierList.add(val);
				} else if(((IExtendedModifier) p).isAnnotation()) {
					annotationsList.add(val);
				}
			}
		}
		return new java.util.AbstractMap.SimpleEntry<IValueList, IValueList>(modifierList, annotationsList);
	}
	
	private java.util.Map.Entry<IValueList, IValueList> parseExtendedModifiers(BodyDeclaration node) {
		if (node.getAST().apiLevel() == AST.JLS2) {
			return new java.util.AbstractMap.SimpleEntry<IValueList, IValueList>(parseModifiers(node.getModifiers()), new IValueList(values));
		} else {
			return parseExtendedModifiers(node.modifiers());
		}
	}
	
	private IValue visitChild(ASTNode node) {
//		System.out.println(getSourceLocation(node));
		node.accept(this);
		return this.getValue();
	}
	
	private String getNodeName(ASTNode node) {
		return node.getClass().getSimpleName();
	}
	
	private IValue[] removeNulls(IValue... withNulls) {
		List<IValue> withOutNulls = new ArrayList<IValue>();
		for (IValue child : withNulls) {
			if (!(child == null))
				withOutNulls.add(child);
		}
		return withOutNulls.toArray(new IValue[withOutNulls.size()]);
	}
	
	private IValue constructAstNode(IValue... children) {
		org.eclipse.imp.pdb.facts.type.Type args = TF.tupleType(removeNulls(children));
		org.eclipse.imp.pdb.facts.type.Type constr = typeStore.lookupConstructor(DATATYPE_RASCAL_AST_NODE_TYPE, "astNode", args);
		return values.constructor(constr, removeNulls(children));
	}
	
	private IValue constructDeclarationNode(String constructor, IValue... children) {
		org.eclipse.imp.pdb.facts.type.Type args = TF.tupleType(removeNulls(children));
		org.eclipse.imp.pdb.facts.type.Type constr = typeStore.lookupConstructor(DATATYPE_RASCAL_AST_DECLARATION_NODE_TYPE, constructor, args);
		return values.constructor(constr, removeNulls(children));
	}
	
	private IValue constructExpressionNode(String constructor, IValue... children) {
		org.eclipse.imp.pdb.facts.type.Type args = TF.tupleType(removeNulls(children));
		org.eclipse.imp.pdb.facts.type.Type constr = typeStore.lookupConstructor(DATATYPE_RASCAL_AST_EXPRESSION_NODE_TYPE, constructor, args);
		return values.constructor(constr, removeNulls(children));
	}
	
	private IValue constructStatementNode(String constructor, IValue... children) {
		org.eclipse.imp.pdb.facts.type.Type args = TF.tupleType(removeNulls(children));
		org.eclipse.imp.pdb.facts.type.Type constr = typeStore.lookupConstructor(DATATYPE_RASCAL_AST_STATEMENT_NODE_TYPE, constructor, args);
		return values.constructor(constr, removeNulls(children));
	}
	
	private IValue constructTypeNode(String constructor, IValue... children) {
		org.eclipse.imp.pdb.facts.type.Type args = TF.tupleType(removeNulls(children));
		org.eclipse.imp.pdb.facts.type.Type constr = typeStore.lookupConstructor(DATATYPE_RASCAL_AST_TYPE_NODE_TYPE, constructor, args);
		return values.constructor(constr, removeNulls(children));
	}
	
	public boolean visit(AnnotationTypeDeclaration node) {
		java.util.Map.Entry<IValueList, IValueList> extendedModifiers = parseExtendedModifiers(node.modifiers());
		IValue name = values.string(node.getName().getFullyQualifiedName()); 
		
		IValueList bodyDeclarations = new IValueList(values);
		for (Iterator it = node.bodyDeclarations().iterator(); it.hasNext();) {
			BodyDeclaration d = (BodyDeclaration) it.next();
			bodyDeclarations.add(visitChild(d));
		}
		//System.out.println(node.resolveBinding().toString());
		ITypeBinding tb = node.resolveBinding();
	
		ownValue = constructDeclarationNode("annotationType", name, bodyDeclarations.asList());
		ownValue = constructAstNode(ownValue, getSourceLocation(node), values.sourceLocation(bindingsResolver.resolveBinding(node)));
		return false;
	}
	
	public boolean visit(AnnotationTypeMemberDeclaration node) {
		java.util.Map.Entry<IValueList, IValueList> extendedModifiers = parseExtendedModifiers(node.modifiers());
		IValue typeArgument = visitChild(node.getType());
		
		String name = node.getName().getFullyQualifiedName();
		
		IValue defaultBlock = node.getDefault() == null ? null : visitChild(node.getDefault());
		//System.out.println(node.resolveBinding().toString());
		ownValue = constructDeclarationNode("annotationTypeMember", typeArgument, values.string(name), defaultBlock);
		ownValue = constructAstNode(ownValue, getSourceLocation(node), values.sourceLocation(bindingsResolver.resolveBinding(node)));
		return false;
	}
	
	public boolean visit(AnonymousClassDeclaration node) {
		IValueList bodyDeclarations = new IValueList(values);
	
		for (Iterator it = node.bodyDeclarations().iterator(); it.hasNext();) {
			BodyDeclaration b = (BodyDeclaration) it.next();
			bodyDeclarations.add(visitChild(b));
		}
		//System.out.println(node.resolveBinding().toString());
		ownValue = constructDeclarationNode("class", bodyDeclarations.asList());
		ownValue = constructAstNode(ownValue, getSourceLocation(node), values.sourceLocation(bindingsResolver.resolveBinding(node)));
		return false;
	}
	
	public boolean visit(ArrayAccess node) {
		IValue array = visitChild(node.getArray());
		IValue index = visitChild(node.getIndex());
		//System.out.println(node.resolveTypeBinding().toString());
		ownValue = constructExpressionNode("arrayAccess", array, index);
		//ownValue = constructAstNode(ownValue, getSourceLocation(node), getBinding("java+arrayAccess", bindingsImporter.resolveBinding()(), null, null));
		ownValue = constructAstNode(ownValue, getSourceLocation(node), values.sourceLocation(bindingsResolver.resolveBinding(node)));
		return false;
	}
	
	public boolean visit(ArrayCreation node) {
		IValue type = visitChild(node.getType().getElementType());
	
		IValueList dimensions = new IValueList(values);
		for (Iterator it = node.dimensions().iterator(); it.hasNext();) {
			Expression e = (Expression) it.next();
			dimensions.add(visitChild(e));
		}
	
		IValue initializer = node.getInitializer() == null ? null : visitChild(node.getInitializer());
		//System.out.println(node.resolveTypeBinding().toString());
		ownValue = constructExpressionNode("newArray", type, dimensions.asList(), initializer);
		//ownValue = constructAstNode(ownValue, getSourceLocation(node), getBinding("java+newArray", bindingsImporter.resolveBinding()(), null, null));
		ownValue = constructAstNode(ownValue, getSourceLocation(node), values.sourceLocation(bindingsResolver.resolveBinding(node)));
		return false;
	}
	
	public boolean visit(ArrayInitializer node) {
		IValueList expressions = new IValueList(values);
		for (Iterator it = node.expressions().iterator(); it.hasNext();) {
			Expression e = (Expression) it.next();
			expressions.add(visitChild(e));
		}
		//System.out.println(node.resolveTypeBinding().toString());
		ownValue = constructExpressionNode("arrayInitializer", expressions.asList());
		//ownValue = constructAstNode(ownValue, getSourceLocation(node), getBinding("java+arrayInitializer", bindingsImporter.resolveBinding()(), null, null));
		ownValue = constructAstNode(ownValue, getSourceLocation(node), values.sourceLocation(bindingsResolver.resolveBinding(node)));
		return false;
	}
	
	public boolean visit(ArrayType node) {
		IValue type = visitChild(node.getComponentType());
		// dimensions???
		//System.out.println(node.resolveBinding().toString());
		ownValue = constructTypeNode("arrayType", type);
		//ownValue = constructAstNode(ownValue, getSourceLocation(node), getBinding("java+arrayType", bindingsImporter.resolveBinding()(), null, null));
		ownValue = constructAstNode(ownValue, getSourceLocation(node), values.sourceLocation(bindingsResolver.resolveBinding(node)));
		return false;
	}
	
	public boolean visit(AssertStatement node) {
		IValue expression = visitChild(node.getExpression());
		IValue message = node.getMessage() == null ? null : visitChild(node.getMessage());
		//System.out.println(node.toString());
		ownValue = constructStatementNode("assert", expression, message);
		ownValue = constructAstNode(ownValue, getSourceLocation(node), values.sourceLocation(bindingsResolver.resolveBinding(node)));
		return false;
	}
	
	public boolean visit(Assignment node) {
		IValue leftSide = visitChild(node.getLeftHandSide());
		IValue rightSide = visitChild(node.getRightHandSide());
		//System.out.println(node.toString());
		ownValue = constructExpressionNode("assignment", leftSide, rightSide);
		ownValue = constructAstNode(ownValue, getSourceLocation(node), values.sourceLocation(bindingsResolver.resolveBinding(node)));
		return false;
	}
	
	public boolean visit(Block node) {
		IValueList statements = new IValueList(values);
		for (Iterator it = node.statements().iterator(); it.hasNext();) {
			Statement s = (Statement) it.next();
			statements.add(visitChild(s));
		}
		//System.out.println(node.toString());
		ownValue = constructStatementNode("block", statements.asList());
		ownValue = constructAstNode(ownValue, getSourceLocation(node), values.sourceLocation(bindingsResolver.resolveBinding(node)));
		return false;
	}
	
	public boolean visit(BlockComment node) {
	//	ownValue = constructDeclarationNode(node);
		return false;
	}
	
	public boolean visit(BooleanLiteral node) {
		IValue booleanValue = values.bool(node.booleanValue());
		//System.out.println(node.resolveTypeBinding().toString());
		ownValue = constructExpressionNode("boolean", booleanValue);
		//ownValue = constructAstNode(ownValue, getSourceLocation(node), getBinding("java+boolean", bindingsImporter.resolveBinding()(), null, null));
		ownValue = constructAstNode(ownValue, getSourceLocation(node), values.sourceLocation(bindingsResolver.resolveBinding(node)));
		return false;
	}
	
	public boolean visit(BreakStatement node) {
		IValue label = node.getLabel() == null ? values.string("") : values.string(node.getLabel().getFullyQualifiedName());
		ownValue = constructStatementNode("break", label);
		//System.out.println(node.toString());
		ownValue = constructAstNode(ownValue, getSourceLocation(node), values.sourceLocation(bindingsResolver.resolveBinding(node)));
		return false;
	}
	
	public boolean visit(CastExpression node) {
		IValue type = visitChild(node.getType());
		IValue expression = visitChild(node.getExpression());
		//System.out.println(node.toString());
		ownValue = constructExpressionNode("cast", type, expression);
		ownValue = constructAstNode(ownValue, getSourceLocation(node), values.sourceLocation(bindingsResolver.resolveBinding(node)));
		return false;
	}
	
	public boolean visit(CatchClause node) {
		IValue exception = visitChild(node.getException());
		IValue body = visitChild(node.getBody());
		//System.out.println(node.toString());
		ownValue = constructStatementNode("catch", exception, body);
		ownValue = constructAstNode(ownValue, getSourceLocation(node), values.sourceLocation(bindingsResolver.resolveBinding(node)));
		return false;
	}
	
	public boolean visit(CharacterLiteral node) {
		IValue value = values.string(node.getEscapedValue()); 
		//System.out.println(node.toString());
		ownValue = constructExpressionNode("string", value);
		ownValue = constructAstNode(ownValue, getSourceLocation(node), values.sourceLocation(bindingsResolver.resolveBinding(node)));
		return false;
	}
	
	public boolean visit(ClassInstanceCreation node) {
		IValue expression = node.getExpression() == null ? null : visitChild(node.getExpression());
	
		IValue type = null;
		IValueList genericTypes = new IValueList(values);
		if (node.getAST().apiLevel() == AST.JLS2) {
			type = visitChild(node.getName());
		} 
		else {
			type = visitChild(node.getType()); 
	
			if (!node.typeArguments().isEmpty()) {
				for (Iterator it = node.typeArguments().iterator(); it.hasNext();) {
					Type t = (Type) it.next();
					genericTypes.add(visitChild(t));
				}
			}
		}
	
		IValueList arguments = new IValueList(values);
		for (Iterator it = node.arguments().iterator(); it.hasNext();) {
			Expression e = (Expression) it.next();
			arguments.add(visitChild(e));
		}
	
		IValue anonymousClassDeclaration = node.getAnonymousClassDeclaration() == null ? null : visitChild(node.getAnonymousClassDeclaration());
		//System.out.println(node.toString());
		ownValue = constructExpressionNode("newObject", expression, type, genericTypes.asList(), arguments.asList(), anonymousClassDeclaration);
		ownValue = constructAstNode(ownValue, getSourceLocation(node), values.sourceLocation(bindingsResolver.resolveBinding(node)));
		return false;
	}
	
	public boolean visit(CompilationUnit node) {
		IValue packageOfUnit = node.getPackage() == null ? null : visitChild(node.getPackage());
		for (Iterator it = node.getCommentList().iterator(); it.hasNext();) {
			Comment c = (Comment)it.next();
		}
		IValueList imports = new IValueList(values);
		for (Iterator it = node.imports().iterator(); it.hasNext();) {
			ImportDeclaration d = (ImportDeclaration) it.next();
			imports.add(visitChild(d));
		}
	
		IValueList typeDeclarations = new IValueList(values);
		for (Iterator it = node.types().iterator(); it.hasNext();) {
			AbstractTypeDeclaration d = (AbstractTypeDeclaration) it.next();
			typeDeclarations.add(visitChild(d));
		}
		
		ownValue = constructDeclarationNode("compilationUnit", packageOfUnit, imports.asList(), typeDeclarations.asList());
		ownValue = constructAstNode(ownValue, getSourceLocation(node), values.sourceLocation(bindingsResolver.resolveBinding(node)));
		
		return false;
	}
	
	public boolean visit(ConditionalExpression node) {
		IValue expression = visitChild(node.getExpression());
		IValue thenBranch = visitChild(node.getThenExpression());
		IValue elseBranch = visitChild(node.getElseExpression());
		//System.out.println(node.toString());
		ownValue = constructExpressionNode("conditional", expression, thenBranch, elseBranch);
		ownValue = constructAstNode(ownValue, getSourceLocation(node), values.sourceLocation(bindingsResolver.resolveBinding(node)));
		return false;
	}
	
	public boolean visit(ConstructorInvocation node) {
		IValueList types = new IValueList(values);
		if (node.getAST().apiLevel() >= AST.JLS3) {
			if (!node.typeArguments().isEmpty()) {
	
				for (Iterator it = node.typeArguments().iterator(); it.hasNext();) {
					Type t = (Type) it.next();
					types.add(visitChild(t));
				}
			}
		}
	
		IValueList arguments = new IValueList(values);
		for (Iterator it = node.arguments().iterator(); it.hasNext();) {
			Expression e = (Expression) it.next();
			arguments.add(visitChild(e));
		}
		//System.out.println(node.toString());
		ownValue = constructExpressionNode("constructor", types.asList(), arguments.asList());
		ownValue = constructAstNode(ownValue, getSourceLocation(node), values.sourceLocation(bindingsResolver.resolveBinding(node)));
		return false;
	}
	
	public boolean visit(ContinueStatement node) {
		//System.out.println(node.toString());
		IValue label = node.getLabel() == null ? null : values.string(node.getLabel().getFullyQualifiedName());
		ownValue = constructStatementNode("continue", label);
		ownValue = constructAstNode(ownValue, getSourceLocation(node), values.sourceLocation(bindingsResolver.resolveBinding(node)));
		return false;
	}
	
	public boolean visit(DoStatement node) {
		//System.out.println(node.toString());
		IValue body = visitChild(node.getBody());
		IValue whileExpression = visitChild(node.getExpression());
	
		ownValue = constructStatementNode("do", body, whileExpression);
		ownValue = constructAstNode(ownValue, getSourceLocation(node), values.sourceLocation(bindingsResolver.resolveBinding(node)));
		return false;
	}
	
	public boolean visit(EmptyStatement node) {
		//System.out.println(node.toString());
		ownValue = constructStatementNode("empty");
		ownValue = constructAstNode(ownValue, getSourceLocation(node), values.sourceLocation(bindingsResolver.resolveBinding(node)));
		return false;
	}
	
	public boolean visit(EnhancedForStatement node) {
		//System.out.println(node.toString());
		IValue parameter = visitChild(node.getParameter());
		IValue collectionExpression = visitChild(node.getExpression());
		IValue body = visitChild(node.getBody());
	
		ownValue = constructStatementNode("foreach", parameter, collectionExpression, body);
		ownValue = constructAstNode(ownValue, getSourceLocation(node), values.sourceLocation(bindingsResolver.resolveBinding(node)));
		return false;
	}
	
	public boolean visit(EnumConstantDeclaration node) {
		//System.out.println(node.toString());
		java.util.Map.Entry<IValueList, IValueList> extendedModifiers = parseExtendedModifiers(node.modifiers());
		IValue name = values.string(node.getName().getFullyQualifiedName()); 
	
		IValueList arguments = new IValueList(values);
		if (!node.arguments().isEmpty()) {
			for (Iterator it = node.arguments().iterator(); it.hasNext();) {
				Expression e = (Expression) it.next();
				arguments.add(visitChild(e));
			}
		}
	
		IValue anonymousClassDeclaration = node.getAnonymousClassDeclaration() == null ? null : visitChild(node.getAnonymousClassDeclaration());
		
		ownValue = constructDeclarationNode("enumConstant", /*extendedModifiers.getKey().asList(), extendedModifiers.getValue().asList(),*/ 
										name, arguments.asList(), anonymousClassDeclaration);
		ownValue = constructAstNode(ownValue, getSourceLocation(node), values.sourceLocation(bindingsResolver.resolveBinding(node)));
		return false;
	}
	
	public boolean visit(EnumDeclaration node) {
		//System.out.println(node.resolveBinding().toString());
		java.util.Map.Entry<IValueList, IValueList> extendedModifiers = parseExtendedModifiers(node.modifiers());
		IValue name = values.string(node.getName().getFullyQualifiedName()); 
	
		IValueList implementedInterfaces = new IValueList(values);
		if (!node.superInterfaceTypes().isEmpty()) {
			for (Iterator it = node.superInterfaceTypes().iterator(); it.hasNext();) {
				Type t = (Type) it.next();
				implementedInterfaces.add(visitChild(t));
			}
		}
	
		IValueList enumConstants = new IValueList(values);
		for (Iterator it = node.enumConstants().iterator(); it.hasNext();) {
			EnumConstantDeclaration d = (EnumConstantDeclaration) it.next();
			enumConstants.add(visitChild(d));
		}
	
		IValueList bodyDeclarations = new IValueList(values);
		if (!node.bodyDeclarations().isEmpty()) {
			for (Iterator it = node.bodyDeclarations().iterator(); it.hasNext();) {
				BodyDeclaration d = (BodyDeclaration) it.next();
				bodyDeclarations.add(visitChild(d));
			}
		}
	
		ownValue = constructDeclarationNode("enum", /*extendedModifiers.getKey().asList(), extendedModifiers.getValue().asList(), */ 
										name, implementedInterfaces.asList(), enumConstants.asList(), bodyDeclarations.asList());
		ownValue = constructAstNode(ownValue, getSourceLocation(node), values.sourceLocation(bindingsResolver.resolveBinding(node)));
		return false;
	}
	
	public boolean visit(ExpressionStatement node) {
		//System.out.println(node.toString());
		IValue expression = visitChild(node.getExpression());
		ownValue = constructStatementNode("expression", expression);
		ownValue = constructAstNode(ownValue, getSourceLocation(node), values.sourceLocation(bindingsResolver.resolveBinding(node)));
		return false;
	}
	
	public boolean visit(FieldAccess node) {
		//System.out.println(node.toString());
		IValue expression = visitChild(node.getExpression());
		IValue name = values.string(node.getName().getFullyQualifiedName());
	
		ownValue = constructExpressionNode("fieldAccess", expression, name);
		ownValue = constructAstNode(ownValue, getSourceLocation(node), values.sourceLocation(bindingsResolver.resolveBinding(node)));
		return false;
	}
	
	public boolean visit(FieldDeclaration node) {
		//System.out.println(node.toString());
		java.util.Map.Entry<IValueList, IValueList> extendedModifiers = parseExtendedModifiers(node);
		IValue type = visitChild(node.getType());
	
		IValueList fragments = new IValueList(values);
		for (Iterator it = node.fragments().iterator(); it.hasNext();) {
			VariableDeclarationFragment f = (VariableDeclarationFragment) it.next();
			fragments.add(visitChild(f));
		}
		
		ownValue = constructDeclarationNode("field", type, fragments.asList());
		ownValue = constructAstNode(ownValue, getSourceLocation(node), values.sourceLocation(bindingsResolver.resolveBinding(node)));
		return false;
	}
	
	public boolean visit(ForStatement node) {
		//System.out.println(node.toString());
		IValueList initializers = new IValueList(values);
		for (Iterator it = node.initializers().iterator(); it.hasNext();) {
			Expression e = (Expression) it.next();
			initializers.add(visitChild(e));
		}
	
		IValue booleanExpression = node.getExpression() == null ? null : visitChild(node.getExpression());
	
		IValueList updaters = new IValueList(values);
		for (Iterator it = node.updaters().iterator(); it.hasNext();) {
			Expression e = (Expression) it.next();
			updaters.add(visitChild(e));
		}
	
		IValue body = visitChild(node.getBody());
	
		ownValue = constructStatementNode("for", initializers.asList(), booleanExpression, updaters.asList(), body);
		ownValue = constructAstNode(ownValue, getSourceLocation(node), values.sourceLocation(bindingsResolver.resolveBinding(node)));
		return false;
	}
	
	public boolean visit(IfStatement node) {
		//System.out.println(node.toString());
		IValue booleanExpression = visitChild(node.getExpression());
		IValue thenStatement = visitChild(node.getThenStatement());
		IValue elseStatement = node.getElseStatement() == null ? null : visitChild(node.getElseStatement());
	
		ownValue = constructStatementNode("if", booleanExpression, thenStatement, elseStatement);
		ownValue = constructAstNode(ownValue, getSourceLocation(node), values.sourceLocation(bindingsResolver.resolveBinding(node)));
		return false;
	}
	
	public boolean visit(ImportDeclaration node) {
		//System.out.println(node.resolveBinding().toString());
		String name = node.getName().getFullyQualifiedName();
		StringBuilder importType = new StringBuilder("import");
		if (node.getAST().apiLevel() >= AST.JLS3) {
			if (node.isStatic())
				importType = concat("static",importType);
		}
	
		ownValue = constructDeclarationNode(importType.toString(), values.string(name));
		ownValue = constructAstNode(ownValue, getSourceLocation(node), values.sourceLocation(bindingsResolver.resolveBinding(node)));
		return false;
	}
	
	public boolean visit(InfixExpression node) {
		//System.out.println(node.toString());
		IValue operator = values.string(node.getOperator().toString());
		IValue leftSide = visitChild(node.getLeftOperand());
		IValue rightSide = visitChild(node.getRightOperand());
	
		IValueList extendedOperands = new IValueList(values);
		if (node.hasExtendedOperands()) {
			for (Iterator it = node.extendedOperands().iterator(); it.hasNext();) {
				Expression e = (Expression) it.next();
				extendedOperands.add(visitChild(e));
			}
		}
	
		ownValue = constructExpressionNode("infix", leftSide, operator, rightSide);//, extendedOperands.asList());
		ownValue = constructAstNode(ownValue, getSourceLocation(node), values.sourceLocation(bindingsResolver.resolveBinding(node)));
		return false;
	}
	
	public boolean visit(Initializer node) {
		//System.out.println(node.toString());
		java.util.Map.Entry<IValueList, IValueList> extendedModifiers = parseExtendedModifiers(node);
		IValue body = visitChild(node.getBody());
	
		ownValue = constructDeclarationNode("initializer", extendedModifiers.getKey().asList(), extendedModifiers.getValue().asList(), body);
		ownValue = constructAstNode(ownValue, getSourceLocation(node), values.sourceLocation(bindingsResolver.resolveBinding(node)));
		return false;
	}
	
	public boolean visit(InstanceofExpression node) {
		//System.out.println(node.toString());
		IValue leftSide = visitChild(node.getLeftOperand());
		IValue rightSide = visitChild(node.getRightOperand());
	
		ownValue = constructExpressionNode("instanceof", leftSide, rightSide);
		ownValue = constructAstNode(ownValue, getSourceLocation(node), values.sourceLocation(bindingsResolver.resolveBinding(node)));
		return false;
	}
	
	public boolean visit(Javadoc node) {
	//	ownValue = constructDeclarationNode(node);
		return false;
	}
	
	public boolean visit(LabeledStatement node) {
		//System.out.println(node.toString());
		IValue label = values.string(node.getLabel().getFullyQualifiedName());
		IValue body = visitChild(node.getBody());
	
		ownValue = constructStatementNode("label", label, body);
		ownValue = constructAstNode(ownValue, getSourceLocation(node), values.sourceLocation(bindingsResolver.resolveBinding(node)));
		return false;
	}
	
	public boolean visit(LineComment node) {
	//	ownValue = constructDeclarationNode(node);
		return false;
	}
	
	public boolean visit(MarkerAnnotation node) {
		//System.out.println(node.toString());
		IValue typeName = values.string(node.getTypeName().getFullyQualifiedName());
		ownValue = constructExpressionNode("markerAnnotation", typeName);
		ownValue = constructAstNode(ownValue, getSourceLocation(node), values.sourceLocation(bindingsResolver.resolveBinding(node)));
		return false;
	}
	
	public boolean visit(MemberRef node) {
		return false;
	}
	
	public boolean visit(MemberValuePair node) {
		//System.out.println(node.toString());
		IValue name = values.string(node.getName().getFullyQualifiedName());
		IValue value = visitChild(node.getValue());
	
		ownValue = constructExpressionNode("memberValuePair", name, value);
		ownValue = constructAstNode(ownValue, getSourceLocation(node), values.sourceLocation(bindingsResolver.resolveBinding(node)));
		return false;
	}
	
	public boolean visit(MethodDeclaration node) {
		//System.out.println(node.resolveBinding().toString());
		java.util.Map.Entry<IValueList, IValueList> extendedModifiers = parseExtendedModifiers(node);
		
		IValueList genericTypes = new IValueList(values);
		if (node.getAST().apiLevel() >= AST.JLS3) {
			if (!node.typeParameters().isEmpty()) {
				for (Iterator it = node.typeParameters().iterator(); it.hasNext();) {
					TypeParameter t = (TypeParameter) it.next();
					genericTypes.add(visitChild(t));
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
				// methods really ought to have a return type
				//returnType = Core.CONS_VOID.make(values);
				returnType = constructTypeNode("void");
			}
		}
		
		IValue name = values.string(node.getName().getFullyQualifiedName());
	
		IValueList parameters = new IValueList(values);
		for (Iterator it = node.parameters().iterator(); it.hasNext();) {
			SingleVariableDeclaration v = (SingleVariableDeclaration) it.next();
			parameters.add(visitChild(v));
		}
	
		IValueList possibleExceptions = new IValueList(values);
		if (!node.thrownExceptions().isEmpty()) {
	
			for (Iterator it = node.thrownExceptions().iterator(); it.hasNext();) {
				Name n = (Name) it.next();
				possibleExceptions.add(visitChild(n));
			}
		}
	
		IValue body = node.getBody() == null ? null : visitChild(node.getBody()); 
	
		ownValue = constructDeclarationNode("method", extendedModifiers.getKey().asList(), returnType, name, parameters.asList(), possibleExceptions.asList(), body);
		ownValue = constructAstNode(ownValue, getSourceLocation(node), values.sourceLocation(bindingsResolver.resolveBinding(node)));
		return false;
	}
	
	public boolean visit(MethodInvocation node) {
		//System.out.println(node.toString());
		IValue expression = node.getExpression() == null ? null : visitChild(node.getExpression());
		
		IValueList genericTypes = new IValueList(values);
		if (node.getAST().apiLevel() >= AST.JLS3) {
			if (!node.typeArguments().isEmpty()) {
				for (Iterator it = node.typeArguments().iterator(); it.hasNext();) {
					Type t = (Type) it.next();
					genericTypes.add(visitChild(t));
				}
			}
		}
	
		IValue name = values.string(node.getName().getFullyQualifiedName());
	
		IValueList arguments = new IValueList(values);
		for (Iterator it = node.arguments().iterator(); it.hasNext();) {
			Expression e = (Expression) it.next();
			arguments.add(visitChild(e));
		}		
		
		ownValue = constructExpressionNode("call", expression, genericTypes.asList(), name, arguments.asList());
		ownValue = constructAstNode(ownValue, getSourceLocation(node), values.sourceLocation(bindingsResolver.resolveBinding(node)));
		return false;
	}
	
	public boolean visit(MethodRef node) {
		return false;
	}
	
	public boolean visit(MethodRefParameter node) {
		return false;
	}
	
	public boolean visit(Modifier node) {
		String modifier = node.getKeyword().toString();
		
		Set<org.eclipse.imp.pdb.facts.type.Type> constrs = typeStore.lookupConstructor(DATATYPE_RASCAL_AST_MODIFIER_NODE_TYPE, modifier);
		for (org.eclipse.imp.pdb.facts.type.Type constr: constrs) {
			ownValue = values.constructor(constr);
		}
	
		return false;
	}
	
	public boolean visit(NormalAnnotation node) {
		//System.out.println(node.toString());
		IValue typeName = values.string(node.getTypeName().getFullyQualifiedName());
	
		IValueList memberValuePairs = new IValueList(values);
		for (Iterator it = node.values().iterator(); it.hasNext();) {
			MemberValuePair p = (MemberValuePair) it.next();
			memberValuePairs.add(visitChild(p));
		}
	
		ownValue = constructExpressionNode("normalAnnotation", typeName, memberValuePairs.asList());
		ownValue = constructAstNode(ownValue, getSourceLocation(node), values.sourceLocation(bindingsResolver.resolveBinding(node)));
		return false;
	}
	
	public boolean visit(NullLiteral node) {
		//System.out.println(node.toString());
		ownValue = constructExpressionNode("null");
		ownValue = constructAstNode(ownValue, getSourceLocation(node), values.sourceLocation(bindingsResolver.resolveBinding(node)));
		return false;
	}
	
	public boolean visit(NumberLiteral node) {
		//System.out.println(node.toString());
		IValue number = values.string(node.getToken());
	
		ownValue = constructExpressionNode("number", number);
		ownValue = constructAstNode(ownValue, getSourceLocation(node), values.sourceLocation(bindingsResolver.resolveBinding(node)));
		return false;
	}
	
	public boolean visit(PackageDeclaration node) {
		//System.out.println(node.resolveBinding().toString());
		IValue name = values.string(node.getName().getFullyQualifiedName());
		IPackageBinding b = (IPackageBinding)node.resolveBinding();
		IValueList annotations = new IValueList(values);
		if (node.getAST().apiLevel() >= AST.JLS3) {
			// TODO: annotations have their own AST now
			for (Iterator it = node.annotations().iterator(); it.hasNext();) {
				Annotation p = (Annotation) it.next();
				annotations.add(visitChild(p));
			}
		}
		
		ownValue = constructDeclarationNode("package", name);
		ownValue = constructAstNode(ownValue, getSourceLocation(node), values.sourceLocation(bindingsResolver.resolveBinding(node)));
		return false;
	}
	
	public boolean visit(ParameterizedType node) {
		//System.out.println(node.resolveBinding().toString());
		IValue type = visitChild(node.getType());
	
		IValueList genericTypes = new IValueList(values);
		for (Iterator it = node.typeArguments().iterator(); it.hasNext();) {
			Type t = (Type) it.next();
			genericTypes.add(visitChild(t));
		}
	
		ownValue = constructTypeNode("parameterizedType", type, genericTypes.asList());
		ownValue = constructAstNode(ownValue, getSourceLocation(node), values.sourceLocation(bindingsResolver.resolveBinding(node)));
		return false;
	}
	
	public boolean visit(ParenthesizedExpression node) {
		//System.out.println(node.toString());
		IValue expression = visitChild(node.getExpression());
		ownValue = constructExpressionNode("bracket", expression);
		ownValue = constructAstNode(ownValue, getSourceLocation(node), values.sourceLocation(bindingsResolver.resolveBinding(node)));
		return false;
	}
	
	public boolean visit(PostfixExpression node) {
		//System.out.println(node.toString());
		IValue operand = visitChild(node.getOperand());
		IValue operator = values.string(node.getOperator().toString());
	
		ownValue = constructExpressionNode("postfix", operand, operator);
		ownValue = constructAstNode(ownValue, getSourceLocation(node), values.sourceLocation(bindingsResolver.resolveBinding(node)));
		return false;
	}
	
	public boolean visit(PrefixExpression node) {
		//System.out.println(node.toString());
		IValue operand = visitChild(node.getOperand());
		IValue operator = values.string(node.getOperator().toString());
	
		ownValue = constructExpressionNode("prefix", operator, operand);
		ownValue = constructAstNode(ownValue, getSourceLocation(node), values.sourceLocation(bindingsResolver.resolveBinding(node)));
		return false;
	}
	
	public boolean visit(PrimitiveType node) {
		ownValue = constructTypeNode(node.toString());
		ownValue = constructAstNode(ownValue, getSourceLocation(node), values.sourceLocation(bindingsResolver.resolveBinding(node)));
		return false;
	}
	
	public boolean visit(QualifiedName node) {
		//System.out.println(node.resolveBinding().toString());
		IValue qualifier = visitChild(node.getQualifier());
		//IValue name = values.string((node.getName().getFullyQualifiedName()));
		
		IValue name = visitChild(node.getName());
		
		ownValue = constructExpressionNode("qualifier", qualifier, name);
		ownValue = constructAstNode(ownValue, getSourceLocation(node), values.sourceLocation(bindingsResolver.resolveBinding(node)));
		return false;
	}
	
	public boolean visit(QualifiedType node) {
		//System.out.println(node.resolveBinding().toString());
		IValue qualifier = visitChild(node.getQualifier());
		//IValue name = values.string((node.getName().getFullyQualifiedName()));
		
		IValue name = visitChild(node.getName());
		
		ownValue = constructExpressionNode("qualifier", qualifier, name);
		ownValue = constructAstNode(ownValue, getSourceLocation(node), values.sourceLocation(bindingsResolver.resolveBinding(node)));
		return false;
	}
	
	public boolean visit(ReturnStatement node) {
		//System.out.println(node.toString());
		IValue expression = node.getExpression() == null ? null : visitChild(node.getExpression());
		ownValue = constructStatementNode("return", expression);
		ownValue = constructAstNode(ownValue, getSourceLocation(node), values.sourceLocation(bindingsResolver.resolveBinding(node)));
		return false;
	}
	
	public boolean visit(SimpleName node) {
		//System.out.println(node.resolveBinding().toString());
		IValue value = values.string(node.getFullyQualifiedName());
		
		ownValue = constructExpressionNode("simpleName", value);
		ownValue = constructAstNode(ownValue, getSourceLocation(node), values.sourceLocation(bindingsResolver.resolveBinding(node)));
		return false;
	}
	
	public boolean visit(SimpleType node) {
		//System.out.println(node.resolveBinding().toString());
		String name = node.getName().toString();
		ownValue = constructTypeNode("simpleType", values.string(name));
		ownValue = constructAstNode(ownValue, getSourceLocation(node), values.sourceLocation(bindingsResolver.resolveBinding(node)));
		return false;
	}
	
	public boolean visit(SingleMemberAnnotation node) {
		//System.out.println(node.toString());
		IValue name = values.string(node.getTypeName().getFullyQualifiedName());
		IValue value = visitChild(node.getValue());
	
		ownValue = constructExpressionNode("singleMemberAnnotation", name, value);
		ownValue = constructAstNode(ownValue, getSourceLocation(node), values.sourceLocation(bindingsResolver.resolveBinding(node)));
		return false;
	}
	
	public boolean visit(SingleVariableDeclaration node) {
		//System.out.println(node.resolveBinding().toString());
		IValue name = values.string(node.getName().getFullyQualifiedName());
	
		java.util.Map.Entry<IValueList, IValueList> extendedModifiers;
		if (node.getAST().apiLevel() == AST.JLS2) {
			extendedModifiers = new java.util.AbstractMap.SimpleEntry<IValueList, IValueList>(parseModifiers(node.getModifiers()), new IValueList(values));
		} else {
			extendedModifiers = parseExtendedModifiers(node.modifiers());
			
		}
	
		IValue type = visitChild(node.getType());
		IValue initializer = node.getInitializer() == null ? null : visitChild(node.getInitializer());
	
		IValue isVarags = values.bool(false);
		if (node.getAST().apiLevel() >= AST.JLS3) {
			isVarags = values.bool(node.isVarargs());
		}
	
		ownValue = constructDeclarationNode("parameter", type, name, values.integer(node.getExtraDimensions()), initializer);/*extendedModifiers.getKey().asList(), extendedModifiers.getValue().asList(), 
										type, initializer, isVarags);*/
		
		ownValue = constructAstNode(ownValue, getSourceLocation(node), values.sourceLocation(bindingsResolver.resolveBinding(node)));
		return false;
	}
	
	public boolean visit(StringLiteral node) {
		//System.out.println(node.toString());
		IValue value = values.string(node.getEscapedValue());		
		ownValue = constructExpressionNode("string", value);
		ownValue = constructAstNode(ownValue, getSourceLocation(node), values.sourceLocation(bindingsResolver.resolveBinding(node)));
		return false;
	}
	
	public boolean visit(SuperConstructorInvocation node) {
		//System.out.println(node.toString());
		IValue expression = node.getExpression() == null ? null : visitChild(node.getExpression());
	
		IValueList genericTypes = new IValueList(values);	
		if (node.getAST().apiLevel() >= AST.JLS3) {
			if (!node.typeArguments().isEmpty()) {
				for (Iterator it = node.typeArguments().iterator(); it.hasNext();) {
					Type t = (Type) it.next();
					genericTypes.add(visitChild(t));
				}
			}
		}
	
		IValueList arguments = new IValueList(values);
		for (Iterator it = node.arguments().iterator(); it.hasNext();) {
			Expression e = (Expression) it.next();
			arguments.add(visitChild(e));
		}
	
		ownValue = constructExpressionNode("constructor", expression, genericTypes.asList(), arguments.asList());
		ownValue = constructAstNode(ownValue, getSourceLocation(node), values.sourceLocation(bindingsResolver.resolveBinding(node)));
		return false;
	}
	
	public boolean visit(SuperFieldAccess node) {
		//System.out.println(node.toString());
		IValue qualifier = node.getQualifier() == null ? null : visitChild(node.getQualifier());
		IValue name = values.string((node.getName().getFullyQualifiedName()));
	
		ownValue = constructExpressionNode("fieldAccess", qualifier, name);
		ownValue = constructAstNode(ownValue, getSourceLocation(node), values.sourceLocation(bindingsResolver.resolveBinding(node)));
		return false;
	}
	
	public boolean visit(SuperMethodInvocation node) {
		//System.out.println(node.toString());
		IValue qualifier = node.getQualifier() == null ? null : visitChild(node.getQualifier());
		
		IValueList genericTypes = new IValueList(values);
		if (node.getAST().apiLevel() >= AST.JLS3) {
			if (!node.typeArguments().isEmpty()) {
				for (Iterator it = node.typeArguments().iterator(); it.hasNext();) {
					Type t = (Type) it.next();
					genericTypes.add(visitChild(t));
				}
			}
		}
	
		IValue name = values.string(node.getName().getFullyQualifiedName());
	
		IValueList arguments = new IValueList(values);
		for (Iterator it = node.arguments().iterator(); it.hasNext();) {
			Expression e = (Expression) it.next();
			arguments.add(visitChild(e));
		}
	
		ownValue = constructExpressionNode("call", qualifier, genericTypes.asList(), name, arguments.asList());
		ownValue = constructAstNode(ownValue, getSourceLocation(node), values.sourceLocation(bindingsResolver.resolveBinding(node)));
		return false;
	}
	
	public boolean visit(SwitchCase node) {
		//System.out.println(node.toString());
//		IValue isDefault = values.bool(node.isDefault());
		IValue expression = node.getExpression() == null ? null : visitChild(node.getExpression());
		String constructorName = "case";
		
		if (node.isDefault())
			constructorName = "defaultCase";
		
		ownValue = constructStatementNode(constructorName, expression);			
		ownValue = constructAstNode(ownValue, getSourceLocation(node), values.sourceLocation(bindingsResolver.resolveBinding(node)));
		return false;
	}
	
	public boolean visit(SwitchStatement node) {
		//System.out.println(node.toString());
		IValue expression = visitChild(node.getExpression());
	
		IValueList statements = new IValueList(values);
		for (Iterator it = node.statements().iterator(); it.hasNext();) {
			Statement s = (Statement) it.next();
			statements.add(visitChild(s));
		}
	
		ownValue = constructStatementNode("switch", expression, statements.asList());
		ownValue = constructAstNode(ownValue, getSourceLocation(node), values.sourceLocation(bindingsResolver.resolveBinding(node)));
		return false;
	}
	
	public boolean visit(SynchronizedStatement node) {
		//System.out.println(node.toString());
		IValue expression = visitChild(node.getExpression());
		IValue body = visitChild(node.getBody());
		
		ownValue = constructStatementNode("synchronized", expression, body);
		ownValue = constructAstNode(ownValue, getSourceLocation(node), values.sourceLocation(bindingsResolver.resolveBinding(node)));
		return false;
	}
	
	public boolean visit(TagElement node) {
		// TODO: What to do with JavaDoc?
		return false;
	}
	
	public boolean visit(TextElement node) {
		// TODO: What to do with JavaDoc?
		return false;
	}
	
	public boolean visit(ThisExpression node) {
		//System.out.println(node.toString());
		IValue qualifier = node.getQualifier() == null ? null : visitChild(node.getQualifier());
	
		ownValue = constructExpressionNode("this", qualifier);
		ownValue = constructAstNode(ownValue, getSourceLocation(node), values.sourceLocation(bindingsResolver.resolveBinding(node)));
		return false;
	}
	
	public boolean visit(ThrowStatement node) {
		//System.out.println(node.toString());
		IValue expression = visitChild(node.getExpression());
		
		ownValue = constructStatementNode("throw", expression);
		ownValue = constructAstNode(ownValue, getSourceLocation(node), values.sourceLocation(bindingsResolver.resolveBinding(node)));
		return false;
	}
	
	public boolean visit(TryStatement node) {
		//System.out.println(node.toString());
		IValue body = visitChild(node.getBody());
	
		IValueList catchClauses = new IValueList(values);
		for (Iterator it = node.catchClauses().iterator(); it.hasNext();) {
			CatchClause cc = (CatchClause) it.next();
			catchClauses.add(visitChild(cc));
		}
		
		IValue finallyBlock = node.getFinally() == null ? null : visitChild(node.getFinally()); 
		
		ownValue = constructStatementNode("try", body, catchClauses.asList(), finallyBlock);
		ownValue = constructAstNode(ownValue, getSourceLocation(node), values.sourceLocation(bindingsResolver.resolveBinding(node)));
		return false;
	}
	
	public boolean visit(TypeDeclaration node) {
		//System.out.println(node.resolveBinding().toString());
		java.util.Map.Entry<IValueList, IValueList> extendedModifiers = parseExtendedModifiers(node);
		String objectType = node.isInterface() ? "interface" : "class";
		IValue name = values.string(node.getName().getFullyQualifiedName()); 
		
		IValueList genericTypes = new IValueList(values);
		if (node.getAST().apiLevel() >= AST.JLS3) {
			if (!node.typeParameters().isEmpty()) {			
				for (Iterator it = node.typeParameters().iterator(); it.hasNext();) {
					TypeParameter t = (TypeParameter) it.next();
					genericTypes.add(visitChild(t));			
				}
			}
		}
		
		IValueList extendsClass = new IValueList(values);
		IValueList implementsInterfaces = new IValueList(values);
		
		if (node.getAST().apiLevel() == AST.JLS2) {
			if (node.getSuperclass() != null) {
				extendsClass.add(visitChild(node.getSuperclass()));
			}
			if (!node.superInterfaces().isEmpty()) {
				for (Iterator it = node.superInterfaces().iterator(); it.hasNext();) {
					Name n = (Name) it.next();
					implementsInterfaces.add(visitChild(n));
				}
			}
		} else if (node.getAST().apiLevel() >= AST.JLS3) {
			if (node.getSuperclassType() != null) {
				extendsClass.add(visitChild(node.getSuperclassType()));
			}
			if (!node.superInterfaceTypes().isEmpty()) {
				for (Iterator it = node.superInterfaceTypes().iterator(); it.hasNext();) {
					Type t = (Type) it.next();
					implementsInterfaces.add(visitChild(t));
				}
			}
		}
		
		IValueList bodyDeclarations = new IValueList(values);
		for (Iterator it = node.bodyDeclarations().iterator(); it.hasNext();) {
			BodyDeclaration d = (BodyDeclaration) it.next();
			bodyDeclarations.add(visitChild(d));
		}
		
		ownValue = constructDeclarationNode(objectType, extendedModifiers.getKey().asList(), name, extendsClass.asList(), implementsInterfaces.asList(), bodyDeclarations.asList());
		ownValue = constructAstNode(ownValue, getSourceLocation(node), values.sourceLocation(bindingsResolver.resolveBinding(node)));
		return false;
	}
	
	public boolean visit(TypeDeclarationStatement node) {
		//System.out.println(node.resolveBinding().toString());
		IValue typeDeclaration;
		if (node.getAST().apiLevel() == AST.JLS2) {
			typeDeclaration = visitChild(node.getTypeDeclaration());
		}
		else {
			typeDeclaration = visitChild(node.getDeclaration());
		}
		
		ownValue = constructStatementNode("declaration", typeDeclaration);
		ownValue = constructAstNode(ownValue, getSourceLocation(node), values.sourceLocation(bindingsResolver.resolveBinding(node)));
		return false;
	}
	
	public boolean visit(TypeLiteral node) {
		//System.out.println(node.toString());
		IValue type = visitChild(node.getType());
	
		ownValue = constructExpressionNode("type", type);
		ownValue = constructAstNode(ownValue, getSourceLocation(node), values.sourceLocation(bindingsResolver.resolveBinding(node)));
		return false;
	}
	
	public boolean visit(TypeParameter node) {
		//System.out.println(node.resolveBinding().toString());
		IValue name = values.string(node.getName().getFullyQualifiedName());
		
		IValueList extendsList = new IValueList(values);
		if (!node.typeBounds().isEmpty()) {
			for (Iterator it = node.typeBounds().iterator(); it.hasNext();) {
				Type t = (Type) it.next();
				extendsList.add(visitChild(t));
			}
		}
		
		ownValue = constructDeclarationNode("typeParameter", name, extendsList.asList());
		ownValue = constructAstNode(ownValue, getSourceLocation(node), values.sourceLocation(bindingsResolver.resolveBinding(node)));
		return false;
	}
	
	public boolean visit(UnionType node) {
		//System.out.println(node.resolveBinding().toString());
		IValueList typesValues = new IValueList(values);
		for(Iterator types = node.types().iterator(); types.hasNext();) {
			Type type = (Type) types.next();
			typesValues.add(visitChild(type));
		}
		
		ownValue = constructDeclarationNode("union", typesValues.asList());
		ownValue = constructAstNode(ownValue, getSourceLocation(node), values.sourceLocation(bindingsResolver.resolveBinding(node)));
		return false;
	}
	
	public boolean visit(VariableDeclarationExpression node) {
		//System.out.println(node.toString());
		java.util.Map.Entry<IValueList, IValueList> extendedModifiers;
		if (node.getAST().apiLevel() == AST.JLS2) {
			extendedModifiers = new java.util.AbstractMap.SimpleEntry<IValueList, IValueList>(parseModifiers(node.getModifiers()), new IValueList(values));
		} else {
			extendedModifiers = parseExtendedModifiers(node.modifiers());
		}
		
		IValue type = visitChild(node.getType());
		
		IValueList fragments = new IValueList(values);
		for (Iterator it = node.fragments().iterator(); it.hasNext();) {
			VariableDeclarationFragment f = (VariableDeclarationFragment) it.next();
			fragments.add(visitChild(f));
		}
		
		ownValue = constructDeclarationNode("variables", type, fragments.asList());
		//extendedModifiers.getKey().asList(), extendedModifiers.getValue().asList(),
		ownValue = constructAstNode(ownValue, getSourceLocation(node), values.sourceLocation(bindingsResolver.resolveBinding(node)));
		return false;
	}
	
	public boolean visit(VariableDeclarationFragment node) {
		//System.out.println(node.resolveBinding().toString());
		IValue name = values.string(node.getName().getFullyQualifiedName());
		
		IValue initializer = node.getInitializer() == null ? null : visitChild(node.getInitializer());
		// extra dimensions?
		ownValue = constructDeclarationNode("variable", name, values.integer(node.getExtraDimensions()), initializer);
		//node.resolveBinding().getType().getDimensions() for all dimensions
		ownValue = constructAstNode(ownValue, getSourceLocation(node), values.sourceLocation(bindingsResolver.resolveBinding(node)));
		return false;
	}
	
	public boolean visit(VariableDeclarationStatement node) {
		//System.out.println(node.toString());
		java.util.Map.Entry<IValueList, IValueList> extendedModifiers;
		if (node.getAST().apiLevel() == AST.JLS2) {
			extendedModifiers = new java.util.AbstractMap.SimpleEntry<IValueList, IValueList>(parseModifiers(node.getModifiers()), new IValueList(values));
		} else {		
			extendedModifiers = parseExtendedModifiers(node.modifiers());
		}
		
		IValue type = visitChild(node.getType());
	
		IValueList fragments = new IValueList(values);
		for (Iterator it = node.fragments().iterator(); it.hasNext();) {
			VariableDeclarationFragment f = (VariableDeclarationFragment) it.next();
			fragments.add(visitChild(f));
		}
		
		ownValue = constructDeclarationNode("variables", //extendedModifiers.getKey().asList(), extendedModifiers.getValue().asList(), 
										type, fragments.asList());
		ownValue = constructAstNode(ownValue, getSourceLocation(node), values.sourceLocation(bindingsResolver.resolveBinding(node)));
		return false;
	}
	
	public boolean visit(WhileStatement node) {
		//System.out.println(node.toString());
		IValue expression = visitChild(node.getExpression());
		IValue body = visitChild(node.getBody());
		
		ownValue = constructStatementNode("while", expression, body);
		ownValue = constructAstNode(ownValue, getSourceLocation(node), values.sourceLocation(bindingsResolver.resolveBinding(node)));
		return false;
	}
	
	public boolean visit(WildcardType node) {
		//System.out.println(node.resolveBinding().toString());
		IValue type = null;
		String name = "wildcard";
				
		if (node.getBound() != null) {
			type = visitChild(node.getBound());
			if (node.isUpperBound()) {
				name = "upperbound";
				
			} else {
				name = "lowerbound";
			}
		}
		ownValue = constructTypeNode(name, type);
		ownValue = constructAstNode(ownValue, getSourceLocation(node), values.sourceLocation(bindingsResolver.resolveBinding(node)));
		return false;
	}
	
	private ISourceLocation getSourceLocation(ASTNode node) {
	
		int start = compilUnit.getExtendedStartPosition(node);//node.getStartPosition();
		int end = start + compilUnit.getExtendedLength(node) - 1;//start + node.getLength() - 1;
		
		return values.sourceLocation(loc.getURI(), 
				 start, node.getLength(), 
				 compilUnit.getLineNumber(start), compilUnit.getLineNumber(end), 
				 compilUnit.getColumnNumber(start), compilUnit.getColumnNumber(end));
	}
	
	private StringBuilder concat(String prefix, StringBuilder postfix) {
		postfix = postfix.replace(0, 1, postfix.substring(0,1).toUpperCase());
		postfix = postfix.insert(0, prefix);
		return postfix;
	}
}
