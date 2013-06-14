package org.rascalmpl.library.experiments.m3.internal;

import java.util.Iterator;
import java.util.Set;

import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.IMapWriter;
import org.eclipse.imp.pdb.facts.ISetWriter;
import org.eclipse.imp.pdb.facts.ISourceLocation;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.type.TypeStore;
import org.eclipse.jdt.core.dom.*;

public class M3Converter extends JavaToRascalConverter {
	private static final String DATATYPE_M3_NODE							= "M3";
	private final org.eclipse.imp.pdb.facts.type.Type DATATYPE_M3_NODE_TYPE;
	
	private static final org.eclipse.imp.pdb.facts.type.Type locType 		= TF.sourceLocationType();
	private static final org.eclipse.imp.pdb.facts.type.Type m3TupleType 	= TF.tupleType(locType, locType);
	private static final org.eclipse.imp.pdb.facts.type.Type m3MapType   	= TF.mapType(locType, locType);
	private final org.eclipse.imp.pdb.facts.type.Type m3LOCModifierType;
	private final org.eclipse.imp.pdb.facts.type.Type m3LOCTypeType;
	
	private ISetWriter source;
	private ISetWriter containment;
	private ISetWriter inheritance;
	private ISetWriter access;
	private ISetWriter reference;
	private ISetWriter invocation;
	private ISetWriter imports;
	private IMapWriter types;
	private IMapWriter documentation;
	private ISetWriter modifiers;
	
	M3Converter(final TypeStore typeStore) {
		super(typeStore, true);
		this.DATATYPE_M3_NODE_TYPE = this.typeStore.lookupAbstractDataType(DATATYPE_M3_NODE);
		source = values.relationWriter(m3TupleType);
		containment = values.relationWriter(m3TupleType);
		inheritance = values.relationWriter(m3TupleType);
		access = values.relationWriter(m3TupleType);
		reference = values.relationWriter(m3TupleType);
		invocation = values.relationWriter(m3TupleType);
		imports = values.relationWriter(m3TupleType);
		m3LOCModifierType = TF.tupleType(locType, DATATYPE_RASCAL_AST_MODIFIER_NODE_TYPE);
		modifiers = values.relationWriter(m3LOCModifierType);
		m3LOCTypeType = TF.tupleType(locType, DATATYPE_RASCAL_AST_TYPE_NODE_TYPE);
		types = values.mapWriter(m3LOCTypeType);
		documentation = values.mapWriter(m3MapType);
	}
	
	public IValue getModel() {
		org.eclipse.imp.pdb.facts.type.Type args = TF.tupleType(loc, source.done(), containment.done(), inheritance.done(), 
				access.done(), reference.done(), invocation.done(), imports.done(), types.done(), documentation.done(), modifiers.done());
		org.eclipse.imp.pdb.facts.type.Type constr = typeStore.lookupConstructor(DATATYPE_M3_NODE_TYPE, "java", args);
		return values.constructor(constr, loc, source.done(), containment.done(), inheritance.done(), 
				access.done(), reference.done(), invocation.done(), imports.done(), types.done(), documentation.done(), modifiers.done());
	}
		
	public boolean visit(AnnotationTypeDeclaration node) {
		ISourceLocation thisEntity = super.resolveBinding(node);
		ISourceLocation src = super.getSourceLocation(node);
		
		source.insert(values.tuple(thisEntity, src));
		
		java.util.Map.Entry<IValueList, IValueList> extendedModifiers = parseExtendedModifiers(node.modifiers());
		
		for(IValue modifier: (IList)(extendedModifiers.getKey().asList()))
			modifiers.insert(values.tuple(thisEntity, modifier));
		
		for (Iterator it = node.bodyDeclarations().iterator(); it.hasNext();) {
			BodyDeclaration d = (BodyDeclaration) it.next();
			containment.insert(values.tuple(thisEntity, super.resolveBinding(d)));
			visitChild(d);
		}
		
		return false;
	}
	
	public boolean visit(AnnotationTypeMemberDeclaration node) {
		java.util.Map.Entry<IValueList, IValueList> extendedModifiers = parseExtendedModifiers(node.modifiers());
		IValue typeArgument = visitChild(node.getType());
		
		String name = node.getName().getFullyQualifiedName();
		
		IValue defaultBlock = node.getDefault() == null ? null : visitChild(node.getDefault());

		return false;
	}
	
	public boolean visit(AnonymousClassDeclaration node) {
		IValueList bodyDeclarations = new IValueList(values);
	
		for (Iterator it = node.bodyDeclarations().iterator(); it.hasNext();) {
			BodyDeclaration b = (BodyDeclaration) it.next();
			bodyDeclarations.add(visitChild(b));
		}

		return false;
	}
	
	public boolean visit(ArrayAccess node) {
		IValue array = visitChild(node.getArray());
		IValue index = visitChild(node.getIndex());

		ownValue = null;
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

		return false;
	}
	
	public boolean visit(ArrayInitializer node) {
		IValueList expressions = new IValueList(values);
		for (Iterator it = node.expressions().iterator(); it.hasNext();) {
			Expression e = (Expression) it.next();
			expressions.add(visitChild(e));
		}
		
		return false;
	}
	
	public boolean visit(ArrayType node) {
		IValue type = visitChild(node.getComponentType());

		return false;
	}
	
	public boolean visit(AssertStatement node) {
		IValue expression = visitChild(node.getExpression());
		IValue message = node.getMessage() == null ? null : visitChild(node.getMessage());
		
		
		
		return false;
	}
	
	public boolean visit(Assignment node) {
		IValue leftSide = visitChild(node.getLeftHandSide());
		IValue rightSide = visitChild(node.getRightHandSide());
		
		
		
		return false;
	}
	
	public boolean visit(Block node) {
		IValueList statements = new IValueList(values);
		for (Iterator it = node.statements().iterator(); it.hasNext();) {
			Statement s = (Statement) it.next();
			statements.add(visitChild(s));
		}
		
		
		
		return false;
	}
	
	public boolean visit(BlockComment node) {
	
		return false;
	}
	
	public boolean visit(BooleanLiteral node) {
		IValue booleanValue = values.bool(node.booleanValue());
		
		
		
		
		return false;
	}
	
	public boolean visit(BreakStatement node) {
		IValue label = node.getLabel() == null ? values.string("") : values.string(node.getLabel().getFullyQualifiedName());
		
		
		
		return false;
	}
	
	public boolean visit(CastExpression node) {
		IValue type = visitChild(node.getType());
		IValue expression = visitChild(node.getExpression());
		
		
		
		return false;
	}
	
	public boolean visit(CatchClause node) {
		IValue exception = visitChild(node.getException());
		IValue body = visitChild(node.getBody());
		
		
		
		return false;
	}
	
	public boolean visit(CharacterLiteral node) {
		IValue value = values.string(node.getEscapedValue()); 
		
		
		
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
		
		
		
		
		return false;
	}
	
	public boolean visit(ConditionalExpression node) {
		IValue expression = visitChild(node.getExpression());
		IValue thenBranch = visitChild(node.getThenExpression());
		IValue elseBranch = visitChild(node.getElseExpression());
		
		
		
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
		
		
		
		return false;
	}
	
	public boolean visit(ContinueStatement node) {
		
		IValue label = node.getLabel() == null ? null : values.string(node.getLabel().getFullyQualifiedName());
		
		
		return false;
	}
	
	public boolean visit(DoStatement node) {
		
		IValue body = visitChild(node.getBody());
		IValue whileExpression = visitChild(node.getExpression());
	
		
		
		return false;
	}
	
	public boolean visit(EmptyStatement node) {
		
		
		
		return false;
	}
	
	public boolean visit(EnhancedForStatement node) {
		
		IValue parameter = visitChild(node.getParameter());
		IValue collectionExpression = visitChild(node.getExpression());
		IValue body = visitChild(node.getBody());
	
		
		
		return false;
	}
	
	public boolean visit(EnumConstantDeclaration node) {
		
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
		
		return false;
	}
	
	public boolean visit(EnumDeclaration node) {
		
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
		
		return false;
	}
	
	public boolean visit(ExpressionStatement node) {
		
		IValue expression = visitChild(node.getExpression());
		
		
		return false;
	}
	
	public boolean visit(FieldAccess node) {
		
		IValue expression = visitChild(node.getExpression());
		IValue name = values.string(node.getName().getFullyQualifiedName());
	
		
		
		return false;
	}
	
	public boolean visit(FieldDeclaration node) {
		
		java.util.Map.Entry<IValueList, IValueList> extendedModifiers = parseExtendedModifiers(node);
		IValue type = visitChild(node.getType());
	
		IValueList fragments = new IValueList(values);
		for (Iterator it = node.fragments().iterator(); it.hasNext();) {
			VariableDeclarationFragment f = (VariableDeclarationFragment) it.next();
			fragments.add(visitChild(f));
		}
		
		
		
		return false;
	}
	
	public boolean visit(ForStatement node) {
		
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
	
		
		
		return false;
	}
	
	public boolean visit(IfStatement node) {
		
		IValue booleanExpression = visitChild(node.getExpression());
		IValue thenStatement = visitChild(node.getThenStatement());
		IValue elseStatement = node.getElseStatement() == null ? null : visitChild(node.getElseStatement());
	
		
		
		return false;
	}
	
	public boolean visit(ImportDeclaration node) {
		
		String name = node.getName().getFullyQualifiedName();
		String importType = new String("import");
		if (node.getAST().apiLevel() >= AST.JLS3) {
			if (node.isStatic())
				importType = "static".concat(importType);
		}
	
		return false;
	}
	
	public boolean visit(InfixExpression node) {
		
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
	
		
		
		return false;
	}
	
	public boolean visit(Initializer node) {
		
		java.util.Map.Entry<IValueList, IValueList> extendedModifiers = parseExtendedModifiers(node);
		IValue body = visitChild(node.getBody());
	
		
		
		return false;
	}
	
	public boolean visit(InstanceofExpression node) {
		
		IValue leftSide = visitChild(node.getLeftOperand());
		IValue rightSide = visitChild(node.getRightOperand());
	
		
		
		return false;
	}
	
	public boolean visit(Javadoc node) {
	
		return false;
	}
	
	public boolean visit(LabeledStatement node) {
		
		IValue label = values.string(node.getLabel().getFullyQualifiedName());
		IValue body = visitChild(node.getBody());
	
		
		
		return false;
	}
	
	public boolean visit(LineComment node) {
	
		return false;
	}
	
	public boolean visit(MarkerAnnotation node) {
		
		IValue typeName = values.string(node.getTypeName().getFullyQualifiedName());
		
		
		return false;
	}
	
	public boolean visit(MemberRef node) {
		return false;
	}
	
	public boolean visit(MemberValuePair node) {
		
		IValue name = values.string(node.getName().getFullyQualifiedName());
		IValue value = visitChild(node.getValue());
	
		
		
		return false;
	}
	
	public boolean visit(MethodDeclaration node) {
		ISourceLocation thisEntity = super.resolveBinding(node);
		java.util.Map.Entry<IValueList, IValueList> extendedModifiers = parseExtendedModifiers(node);
		
		for(IValue modifier: (IList)(extendedModifiers.getKey().asList()))
			modifiers.insert(values.tuple(thisEntity, modifier));
		
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
	
		
		
		return false;
	}
	
	public boolean visit(MethodInvocation node) {
		
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
		
		IValue typeName = values.string(node.getTypeName().getFullyQualifiedName());
	
		IValueList memberValuePairs = new IValueList(values);
		for (Iterator it = node.values().iterator(); it.hasNext();) {
			MemberValuePair p = (MemberValuePair) it.next();
			memberValuePairs.add(visitChild(p));
		}
	
		
		
		return false;
	}
	
	public boolean visit(NullLiteral node) {
		
		
		
		return false;
	}
	
	public boolean visit(NumberLiteral node) {
		
		IValue number = values.string(node.getToken());
	
		
		
		return false;
	}
	
	public boolean visit(PackageDeclaration node) {
		
		IValue name = values.string(node.getName().getFullyQualifiedName());
		IPackageBinding b = (IPackageBinding)node.resolveBinding();
		IValueList annotations = new IValueList(values);
		if (node.getAST().apiLevel() >= AST.JLS3) {
			
			for (Iterator it = node.annotations().iterator(); it.hasNext();) {
				Annotation p = (Annotation) it.next();
				annotations.add(visitChild(p));
			}
		}
		
		
		
		return false;
	}
	
	public boolean visit(ParameterizedType node) {
		
		IValue type = visitChild(node.getType());
	
		IValueList genericTypes = new IValueList(values);
		for (Iterator it = node.typeArguments().iterator(); it.hasNext();) {
			Type t = (Type) it.next();
			genericTypes.add(visitChild(t));
		}
	
		
		
		return false;
	}
	
	public boolean visit(ParenthesizedExpression node) {
		
		IValue expression = visitChild(node.getExpression());
		
		
		return false;
	}
	
	public boolean visit(PostfixExpression node) {
		
		IValue operand = visitChild(node.getOperand());
		IValue operator = values.string(node.getOperator().toString());
	
		
		
		return false;
	}
	
	public boolean visit(PrefixExpression node) {
		
		IValue operand = visitChild(node.getOperand());
		IValue operator = values.string(node.getOperator().toString());
	
		
		
		return false;
	}
	
	public boolean visit(PrimitiveType node) {
		
		
		return false;
	}
	
	public boolean visit(QualifiedName node) {
		
		IValue qualifier = visitChild(node.getQualifier());
		
		
		IValue name = visitChild(node.getName());
		
		
		
		return false;
	}
	
	public boolean visit(QualifiedType node) {
		
		IValue qualifier = visitChild(node.getQualifier());
		
		
		IValue name = visitChild(node.getName());
		
		
		
		return false;
	}
	
	public boolean visit(ReturnStatement node) {
		
		IValue expression = node.getExpression() == null ? null : visitChild(node.getExpression());
		
		
		return false;
	}
	
	public boolean visit(SimpleName node) {
		
		IValue value = values.string(node.getFullyQualifiedName());
		
		
		
		return false;
	}
	
	public boolean visit(SimpleType node) {
		
		String name = node.getName().toString();
		
		
		return false;
	}
	
	public boolean visit(SingleMemberAnnotation node) {
		
		IValue name = values.string(node.getTypeName().getFullyQualifiedName());
		IValue value = visitChild(node.getValue());
	
		
		
		return false;
	}
	
	public boolean visit(SingleVariableDeclaration node) {
		
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
	
		/*extendedModifiers.getKey().asList(), extendedModifiers.getValue().asList(), 
										type, initializer, isVarags);*/
		
		
		return false;
	}
	
	public boolean visit(StringLiteral node) {
		
		IValue value = values.string(node.getEscapedValue());		
		
		
		return false;
	}
	
	public boolean visit(SuperConstructorInvocation node) {
		
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
	
		
		
		return false;
	}
	
	public boolean visit(SuperFieldAccess node) {
		
		IValue qualifier = node.getQualifier() == null ? null : visitChild(node.getQualifier());
		IValue name = values.string((node.getName().getFullyQualifiedName()));
	
		
		
		return false;
	}
	
	public boolean visit(SuperMethodInvocation node) {
		
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
	
		
		
		return false;
	}
	
	public boolean visit(SwitchCase node) {
		

		IValue expression = node.getExpression() == null ? null : visitChild(node.getExpression());
		String constructorName = "case";
		
		if (node.isDefault())
			constructorName = "defaultCase";
		
					
		
		return false;
	}
	
	public boolean visit(SwitchStatement node) {
		
		IValue expression = visitChild(node.getExpression());
	
		IValueList statements = new IValueList(values);
		for (Iterator it = node.statements().iterator(); it.hasNext();) {
			Statement s = (Statement) it.next();
			statements.add(visitChild(s));
		}
	
		
		
		return false;
	}
	
	public boolean visit(SynchronizedStatement node) {
		
		IValue expression = visitChild(node.getExpression());
		IValue body = visitChild(node.getBody());
		
		
		
		return false;
	}
	
	public boolean visit(TagElement node) {
		
		return false;
	}
	
	public boolean visit(TextElement node) {
		
		return false;
	}
	
	public boolean visit(ThisExpression node) {
		
		IValue qualifier = node.getQualifier() == null ? null : visitChild(node.getQualifier());
	
		
		
		return false;
	}
	
	public boolean visit(ThrowStatement node) {
		
		IValue expression = visitChild(node.getExpression());
		
		
		
		return false;
	}
	
	public boolean visit(TryStatement node) {
		
		IValue body = visitChild(node.getBody());
	
		IValueList catchClauses = new IValueList(values);
		for (Iterator it = node.catchClauses().iterator(); it.hasNext();) {
			CatchClause cc = (CatchClause) it.next();
			catchClauses.add(visitChild(cc));
		}
		
		IValue finallyBlock = node.getFinally() == null ? null : visitChild(node.getFinally()); 
		
		
		
		return false;
	}
	
	public boolean visit(TypeDeclaration node) {
		
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
		
		
		
		return false;
	}
	
	public boolean visit(TypeDeclarationStatement node) {
		
		IValue typeDeclaration;
		if (node.getAST().apiLevel() == AST.JLS2) {
			typeDeclaration = visitChild(node.getTypeDeclaration());
		}
		else {
			typeDeclaration = visitChild(node.getDeclaration());
		}
		
		
		
		return false;
	}
	
	public boolean visit(TypeLiteral node) {
		
		IValue type = visitChild(node.getType());
	
		
		
		return false;
	}
	
	public boolean visit(TypeParameter node) {
		
		IValue name = values.string(node.getName().getFullyQualifiedName());
		
		IValueList extendsList = new IValueList(values);
		if (!node.typeBounds().isEmpty()) {
			for (Iterator it = node.typeBounds().iterator(); it.hasNext();) {
				Type t = (Type) it.next();
				extendsList.add(visitChild(t));
			}
		}
		
		
		
		return false;
	}
	
	public boolean visit(UnionType node) {
		
		IValueList typesValues = new IValueList(values);
		for(Iterator types = node.types().iterator(); types.hasNext();) {
			Type type = (Type) types.next();
			typesValues.add(visitChild(type));
		}
		
		
		
		return false;
	}
	
	public boolean visit(VariableDeclarationExpression node) {
		
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
		
		
		
		
		return false;
	}
	
	public boolean visit(VariableDeclarationFragment node) {
		
		IValue name = values.string(node.getName().getFullyQualifiedName());
		
		IValue initializer = node.getInitializer() == null ? null : visitChild(node.getInitializer());
		
		
		
		
		return false;
	}
	
	public boolean visit(VariableDeclarationStatement node) {
		
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
		
		return false;
	}
	
	public boolean visit(WhileStatement node) {
		
		IValue expression = visitChild(node.getExpression());
		IValue body = visitChild(node.getBody());
		
		
		
		return false;
	}
	
	public boolean visit(WildcardType node) {
		
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
		
		
		return false;
	}
}

