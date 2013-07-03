package org.rascalmpl.library.experiments.m3.internal;

import java.util.Iterator;
import java.util.Stack;

import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.IMapWriter;
import org.eclipse.imp.pdb.facts.ISetWriter;
import org.eclipse.imp.pdb.facts.ISourceLocation;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.type.TypeStore;
import org.eclipse.jdt.core.dom.*;

@SuppressWarnings({"rawtypes", "deprecation"})
public class M3Converter extends JavaToRascalConverter {
	private static final String DATATYPE_M3_NODE							= "M3";
	private final org.eclipse.imp.pdb.facts.type.Type DATATYPE_M3_NODE_TYPE;
	
	private static final org.eclipse.imp.pdb.facts.type.Type locType 		= TF.sourceLocationType();
	private static final org.eclipse.imp.pdb.facts.type.Type m3TupleType 	= TF.tupleType(locType, locType);
	private static final org.eclipse.imp.pdb.facts.type.Type m3MapType   	= TF.mapType(locType, locType);
	private final org.eclipse.imp.pdb.facts.type.Type m3LOCModifierType;
	private final org.eclipse.imp.pdb.facts.type.Type m3LOCTypeType;
	
	private final Stack<ISourceLocation> scopeManager = new Stack<ISourceLocation>();
	
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
		ownValue = values.constructor(DATATYPE_M3_NODE_TYPE);
		setAnnotation("source", source.done());
		setAnnotation("containment", containment.done());
		setAnnotation("inheritance", inheritance.done());
		setAnnotation("reference", reference.done());
		setAnnotation("invocation", invocation.done());
		setAnnotation("imports", imports.done());
		setAnnotation("modifiers", modifiers.done());
		setAnnotation("types", types.done());
		setAnnotation("documentation", documentation.done());
		setAnnotation("access", access.done());
		return ownValue;
	}
	
	@Override
	protected ISourceLocation resolveBinding(ASTNode node) {
		ISourceLocation result = super.resolveBinding(node);
		String scheme = result.getURI().getScheme();
		
		if (scheme.equals("unknown") || scheme.equals("unresolved"))
			result = null;
		
		return result;
	}
	
	@Override
	protected IValue visitChild(ASTNode node) {
		if (node == null)
			return null;
		return super.visitChild(node);
	}
	
	public ISourceLocation getParent() {
		return scopeManager.peek();
	}
	
	public void insert(ISetWriter relW, IValue lhs, IValue rhs) {
		relW.insert(values.tuple(lhs, rhs));
	}
	
	public void insert(IMapWriter mapW, IValue lhs, IValue rhs) {
		mapW.insert(values.tuple(lhs, rhs));
	}

	public void insert(ISetWriter relW, IValue lhs, IValueList rhs) {
		for (IValue oneRHS: (IList)rhs.asList())
			insert(relW, ownValue, oneRHS);
	}
	
	public void preVisit(ASTNode node) {
		ownValue = resolveBinding(node);
	}
	
	public void postVisit(ASTNode node) {
		if (ownValue != null) {
			insert(source, ownValue, getSourceLocation(node));
		}
	}
	
	public boolean visit(AnnotationTypeDeclaration node) {
		scopeManager.push((ISourceLocation) ownValue);
		
		IValueList extendedModifiers = parseExtendedModifiers(node.modifiers());
		
		for (Iterator it = node.bodyDeclarations().iterator(); it.hasNext();) {
			BodyDeclaration d = (BodyDeclaration) it.next();
			visitChild(d);
		}
		
		ownValue = scopeManager.pop();
		insert(modifiers, ownValue, extendedModifiers);
		insert(containment, getParent(), ownValue);
		return false;
	}
	
	public boolean visit(AnnotationTypeMemberDeclaration node) {
		scopeManager.push((ISourceLocation) ownValue);
		
		IValueList extendedModifiers = parseExtendedModifiers(node.modifiers());
		IValue typeArgument = visitChild(node.getType());
		
		visitChild(node.getDefault());
		
		ownValue = scopeManager.pop();
		
		insert(types, ownValue, typeArgument);
		insert(modifiers, ownValue, extendedModifiers);
		insert(containment, getParent(), ownValue);
		
		return false;
	}
	
	public boolean visit(AnonymousClassDeclaration node) {
		scopeManager.push((ISourceLocation) ownValue);
		
		for (Iterator it = node.bodyDeclarations().iterator(); it.hasNext();) {
			BodyDeclaration b = (BodyDeclaration) it.next();
			visitChild(b);
		}
		
		ownValue = scopeManager.pop();
		insert(containment, getParent(), ownValue);
		
		return false;
	}
	
	public boolean visit(ArrayAccess node) {
		IValue array = visitChild(node.getArray());
//		IValue index = visitChild(node.getIndex());
		
		insert(access, getParent(), array);
	
		return false;
	}
	
	public boolean visit(ArrayCreation node) {
		visitChild(node.getType().getElementType());
	
		for (Iterator it = node.dimensions().iterator(); it.hasNext();) {
			Expression e = (Expression) it.next();
			visitChild(e);
		}
	
		visitChild(node.getInitializer());
		
		ownValue = resolveBinding(node);
		
		return false;
	}
	
	public boolean visit(ArrayInitializer node) {
		for (Iterator it = node.expressions().iterator(); it.hasNext();) {
			Expression e = (Expression) it.next();
			visitChild(e);
		}
		
		ownValue = resolveBinding(node);
		
		return false;
	}
	
	public boolean visit(ArrayType node) {
		IValue type = visitChild(node.getComponentType());
		
		ownValue = constructTypeNode("arrayType", type);
		setAnnotation("binding", super.resolveBinding(node));
		return false;
	}
	
	public boolean visit(AssertStatement node) {
		visitChild(node.getExpression());
		visitChild(node.getMessage());
		
		ownValue = resolveBinding(node);
		
		return false;
	}
	
	public boolean visit(Assignment node) {
		visitChild(node.getLeftHandSide());
		visitChild(node.getRightHandSide());
		
		ownValue = resolveBinding(node);
		
		return false;
	}
	
	public boolean visit(Block node) {
		for (Iterator it = node.statements().iterator(); it.hasNext();) {
			Statement s = (Statement) it.next();
			visitChild(s);
		}
		
		ownValue = resolveBinding(node);
		
		return false;
	}
	
	public boolean visit(BlockComment node) {
		insert(documentation, resolveBinding(node.getAlternateRoot()), getSourceLocation(node));
		return false;
	}
	
	public boolean visit(BooleanLiteral node) {
		ownValue = resolveBinding(node);
		
		return false;
	}
	
	public boolean visit(BreakStatement node) {
		ownValue = resolveBinding(node);
		
		return false;
	}
	
	public boolean visit(CastExpression node) {
		ownValue = resolveBinding(node);
		
		return false;
	}
	
	public boolean visit(CatchClause node) {
		visitChild(node.getException());
		visitChild(node.getBody());
		
		ownValue = resolveBinding(node);
		
		return false;
	}
	
	public boolean visit(CharacterLiteral node) {
		
		ownValue = resolveBinding(node);
		
		return false;
	}
	
	public boolean visit(ClassInstanceCreation node) {
		visitChild(node.getExpression());
	
		IValueList genericTypes = new IValueList(values);
		if (node.getAST().apiLevel() == AST.JLS2) {
			visitChild(node.getName());
		} 
		else {
			visitChild(node.getType()); 
	
			if (!node.typeArguments().isEmpty()) {
				for (Iterator it = node.typeArguments().iterator(); it.hasNext();) {
					Type t = (Type) it.next();
					genericTypes.add(visitChild(t));
				}
			}
		}

		for (Iterator it = node.arguments().iterator(); it.hasNext();) {
			Expression e = (Expression) it.next();
			visitChild(e);
		}
	
		visitChild(node.getAnonymousClassDeclaration());
		
		ownValue = resolveBinding(node);
		insert(invocation, getParent(), ownValue);
		
		return false;
	}
	
	public boolean visit(CompilationUnit node) {
		scopeManager.push((ISourceLocation) ownValue);
		
		visitChild(node.getPackage());
		
		for (Iterator it = node.imports().iterator(); it.hasNext();) {
			ImportDeclaration d = (ImportDeclaration) it.next();
			visitChild(d);
		}
	
		for (Iterator it = node.types().iterator(); it.hasNext();) {
			AbstractTypeDeclaration d = (AbstractTypeDeclaration) it.next();
			visitChild(d);
		}
		
		ownValue = scopeManager.pop();
			
		return false;
	}
	
	public boolean visit(ConditionalExpression node) {
		visitChild(node.getExpression());
		visitChild(node.getThenExpression());
		visitChild(node.getElseExpression());
		
		ownValue = resolveBinding(node);
		
		return false;
	}
	
	public boolean visit(ConstructorInvocation node) {
		if (node.getAST().apiLevel() >= AST.JLS3) {
			if (!node.typeArguments().isEmpty()) {
	
				for (Iterator it = node.typeArguments().iterator(); it.hasNext();) {
					Type t = (Type) it.next();
					visitChild(t);
				}
			}
		}

		for (Iterator it = node.arguments().iterator(); it.hasNext();) {
			Expression e = (Expression) it.next();
			visitChild(e);
		}
		
		ownValue = resolveBinding(node);
		insert(invocation, getParent(), ownValue);
		
		return false;
	}
	
	public boolean visit(ContinueStatement node) {
		ownValue = resolveBinding(node);
		
		return false;
	}
	
	public boolean visit(DoStatement node) {
		
		visitChild(node.getBody());
		visitChild(node.getExpression());
	
		ownValue = resolveBinding(node);
		
		return false;
	}
	
	public boolean visit(EmptyStatement node) {
		
		ownValue = resolveBinding(node);
		
		return false;
	}
	
	public boolean visit(EnhancedForStatement node) {
		
		visitChild(node.getParameter());
		visitChild(node.getExpression());
		visitChild(node.getBody());
	
		ownValue = resolveBinding(node);
		
		return false;
	}
	
	public boolean visit(EnumConstantDeclaration node) {
		scopeManager.push((ISourceLocation) ownValue);
		IValueList extendedModifiers = parseExtendedModifiers(node.modifiers());
	
		if (!node.arguments().isEmpty()) {
			for (Iterator it = node.arguments().iterator(); it.hasNext();) {
				Expression e = (Expression) it.next();
				visitChild(e);
			}
		}
	
		visitChild(node.getAnonymousClassDeclaration());
		ownValue = scopeManager.pop();
		
		insert(modifiers, ownValue, extendedModifiers);
		insert(containment, getParent(), ownValue);
		
		return false;
	}
	
	public boolean visit(EnumDeclaration node) {
		scopeManager.push((ISourceLocation) ownValue);
		IValueList extendedModifiers = parseExtendedModifiers(node.modifiers());
	
		IValueList implementedInterfaces = new IValueList(values);
		if (!node.superInterfaceTypes().isEmpty()) {
			for (Iterator it = node.superInterfaceTypes().iterator(); it.hasNext();) {
				Type t = (Type) it.next();
				implementedInterfaces.add(visitChild(t));
			}
		}
	
		for (Iterator it = node.enumConstants().iterator(); it.hasNext();) {
			EnumConstantDeclaration d = (EnumConstantDeclaration) it.next();
			visitChild(d);
		}
	
		if (!node.bodyDeclarations().isEmpty()) {
			for (Iterator it = node.bodyDeclarations().iterator(); it.hasNext();) {
				BodyDeclaration d = (BodyDeclaration) it.next();
				visitChild(d);
			}
		}
		
		ownValue = scopeManager.pop();
		insert(modifiers, ownValue, extendedModifiers);
		insert(containment, getParent(), ownValue);
		insert(inheritance, ownValue, implementedInterfaces);
		return false;
	}
	
	public boolean visit(ExpressionStatement node) {
		
		visitChild(node.getExpression());
		ownValue = resolveBinding(node);
		
		return false;
	}
	
	public boolean visit(FieldAccess node) {
		
		visitChild(node.getExpression());
	
		ownValue = resolveBinding(node);
		insert(access, getParent(), ownValue);
		
		return false;
	}
	
	public boolean visit(FieldDeclaration node) {
		
		IValueList extendedModifiers = parseExtendedModifiers(node);
		IValue type = visitChild(node.getType());
	
		IValueList fragments = new IValueList(values);
		for (Iterator it = node.fragments().iterator(); it.hasNext();) {
			VariableDeclarationFragment f = (VariableDeclarationFragment) it.next();
			fragments.add(visitChild(f));
		}
		
		for (IValue fragment: (IList)fragments.asList()) {
			insert(modifiers, fragment, extendedModifiers);
			insert(containment, getParent(), ownValue);
			insert(types, fragment, type);
		}
		
		return false;
	}
	
	public boolean visit(ForStatement node) {
		for (Iterator it = node.initializers().iterator(); it.hasNext();) {
			Expression e = (Expression) it.next();
			visitChild(e);
		}
	
		visitChild(node.getExpression());
	
		for (Iterator it = node.updaters().iterator(); it.hasNext();) {
			Expression e = (Expression) it.next();
			visitChild(e);
		}
	
		visitChild(node.getBody());
	
		ownValue = resolveBinding(node);
		
		return false;
	}
	
	public boolean visit(IfStatement node) {
		
		visitChild(node.getExpression());
		visitChild(node.getThenStatement());
		visitChild(node.getElseStatement());
	
		ownValue = resolveBinding(node);
		
		return false;
	}
	
	public boolean visit(ImportDeclaration node) {
		ownValue = resolveBinding(node);
		insert(imports, getParent(), ownValue);
		return false;
	}
	
	public boolean visit(InfixExpression node) {
		
		visitChild(node.getLeftOperand());
		visitChild(node.getRightOperand());
	
		if (node.hasExtendedOperands()) {
			for (Iterator it = node.extendedOperands().iterator(); it.hasNext();) {
				Expression e = (Expression) it.next();
				visitChild(e);
			}
		}
	
		ownValue = resolveBinding(node);
		
		return false;
	}
	
	public boolean visit(Initializer node) {
		
//		IValueList extendedModifiers = parseExtendedModifiers(node);
		visitChild(node.getBody());
	
		ownValue = resolveBinding(node);
		
		return false;
	}
	
	public boolean visit(InstanceofExpression node) {
		
		visitChild(node.getLeftOperand());
		visitChild(node.getRightOperand());
	
		ownValue = resolveBinding(node);
		
		return false;
	}
	
	public boolean visit(Javadoc node) {
	
		return false;
	}
	
	public boolean visit(LabeledStatement node) {
		ownValue = resolveBinding(node);
		
		return false;
	}
	
	public boolean visit(LineComment node) {
		insert(documentation, resolveBinding(node.getAlternateRoot()), getSourceLocation(node));
		return false;
	}
	
	public boolean visit(MarkerAnnotation node) {
		ownValue = resolveBinding(node);
		
		return false;
	}
	
	public boolean visit(MemberRef node) {
		return false;
	}
	
	public boolean visit(MemberValuePair node) {
		ownValue = resolveBinding(node);
		
		return false;
	}
	
	public boolean visit(MethodDeclaration node) {
		scopeManager.push((ISourceLocation) ownValue);
		IValueList extendedModifiers = parseExtendedModifiers(node);
		
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
				
				
				returnType = constructTypeNode("void");
				setAnnotation("binding", super.resolveBinding(node));
			}
		}
	
		for (Iterator it = node.parameters().iterator(); it.hasNext();) {
			SingleVariableDeclaration v = (SingleVariableDeclaration) it.next();
			visitChild(v);
		}
	
		/*IValueList possibleExceptions = new IValueList(values);
		if (!node.thrownExceptions().isEmpty()) {
	
			for (Iterator it = node.thrownExceptions().iterator(); it.hasNext();) {
				Name n = (Name) it.next();
				possibleExceptions.add(visitChild(n));
			}
		}*/
	
		visitChild(node.getBody()); 
	
		ownValue = scopeManager.pop();
		
		insert(modifiers, ownValue, extendedModifiers);
		if (returnType != null)
			insert(types, ownValue, returnType);
		insert(containment, getParent(), ownValue);
		return false;
	}
	
	public boolean visit(MethodInvocation node) {
		
		visitChild(node.getExpression());
		
		IValueList genericTypes = new IValueList(values);
		if (node.getAST().apiLevel() >= AST.JLS3) {
			if (!node.typeArguments().isEmpty()) {
				for (Iterator it = node.typeArguments().iterator(); it.hasNext();) {
					Type t = (Type) it.next();
					genericTypes.add(visitChild(t));
				}
			}
		}
	
		for (Iterator it = node.arguments().iterator(); it.hasNext();) {
			Expression e = (Expression) it.next();
			visitChild(e);
		}		
		
		ownValue = resolveBinding(node);
		insert(invocation, getParent(), ownValue);
		
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
		ownValue = constructModifierNode(modifier);
			
		return false;
	}
	
	public boolean visit(NormalAnnotation node) {
		ownValue = resolveBinding(node);
		
		return false;
	}
	
	public boolean visit(NullLiteral node) {
		ownValue = resolveBinding(node);
		
		return false;
	}
	
	public boolean visit(NumberLiteral node) {
		ownValue = resolveBinding(node);
		
		return false;
	}
	
	public boolean visit(PackageDeclaration node) {
		scopeManager.push((ISourceLocation) ownValue);
		if (node.getAST().apiLevel() >= AST.JLS3) {
			
			for (Iterator it = node.annotations().iterator(); it.hasNext();) {
				Annotation p = (Annotation) it.next();
				visitChild(p);
			}
		}
		
		ownValue = scopeManager.pop();
		insert(containment, getParent(), ownValue);
		
		return false;
	}
	
	public boolean visit(ParameterizedType node) {
		
		IValue type = visitChild(node.getType());
	
		IValueList genericTypes = new IValueList(values);
		for (Iterator it = node.typeArguments().iterator(); it.hasNext();) {
			Type t = (Type) it.next();
			genericTypes.add(visitChild(t));
		}
	
		ownValue = constructTypeNode("parameterizedType", type);
		setAnnotation("typeParameters", genericTypes.asList());
		setAnnotation("binding", super.resolveBinding(node));
		return false;
	}
	
	public boolean visit(ParenthesizedExpression node) {
		visitChild(node.getExpression());
		ownValue = resolveBinding(node);
		
		return false;
	}
	
	public boolean visit(PostfixExpression node) {
		ownValue = resolveBinding(node);
		
		return false;
	}
	
	public boolean visit(PrefixExpression node) {
		ownValue = resolveBinding(node);
		
		return false;
	}
	
	public boolean visit(PrimitiveType node) {
		ownValue = constructTypeNode(node.toString());

		setAnnotation("binding", super.resolveBinding(node));
		return false;
	}
	
	public boolean visit(QualifiedName node) {
		
		IValue qualifier = visitChild(node.getQualifier());
		
		
		IValue name = visitChild(node.getName());
		
		ownValue = constructTypeNode("qualifier", qualifier, name);

		setAnnotation("binding", super.resolveBinding(node));
		return false;
	}
	
	public boolean visit(QualifiedType node) {
		
		IValue qualifier = visitChild(node.getQualifier());
		
		IValue name = visitChild(node.getName());
		
		ownValue = constructTypeNode("qualifier", qualifier, name);

		setAnnotation("binding", super.resolveBinding(node));
		return false;
	}
	
	public boolean visit(ReturnStatement node) {
		
		visitChild(node.getExpression());
		ownValue = resolveBinding(node);
		
		return false;
	}
	
	public boolean visit(SimpleName node) {
		
		String name = node.getFullyQualifiedName();
		ownValue = constructTypeNode("simpleType", values.string(name));

		setAnnotation("binding", super.resolveBinding(node));
		return false;
	}
	
	public boolean visit(SimpleType node) {
		
		String name = node.getName().getFullyQualifiedName();
		ownValue = constructTypeNode("simpleType", values.string(name));

		setAnnotation("binding", super.resolveBinding(node));
		return false;
	}
	
	public boolean visit(SingleMemberAnnotation node) {
		ownValue = resolveBinding(node);
		
		return false;
	}
	
	public boolean visit(SingleVariableDeclaration node) {
		scopeManager.push((ISourceLocation) ownValue);
		IValueList extendedModifiers = parseExtendedModifiers(node.modifiers());
	
		IValue type = visitChild(node.getType());
		visitChild(node.getInitializer());
	
		ownValue = scopeManager.pop();
		
		insert(types, ownValue, type);
		insert(modifiers, ownValue, extendedModifiers);
		insert(containment, getParent(), ownValue);
		return false;
	}
	
	public boolean visit(StringLiteral node) {
		
		ownValue = resolveBinding(node);
		
		return false;
	}
	
	public boolean visit(SuperConstructorInvocation node) {
		
		visitChild(node.getExpression());
	
		IValueList genericTypes = new IValueList(values);	
		if (node.getAST().apiLevel() >= AST.JLS3) {
			if (!node.typeArguments().isEmpty()) {
				for (Iterator it = node.typeArguments().iterator(); it.hasNext();) {
					Type t = (Type) it.next();
					genericTypes.add(visitChild(t));
				}
			}
		}
	
		for (Iterator it = node.arguments().iterator(); it.hasNext();) {
			Expression e = (Expression) it.next();
			visitChild(e);
		}
	
		ownValue = resolveBinding(node);
		insert(invocation, getParent(), ownValue);
		return false;
	}
	
	public boolean visit(SuperFieldAccess node) {
		
		visitChild(node.getQualifier());
	
		ownValue = resolveBinding(node);
		insert(access, getParent(), ownValue);
		
		return false;
	}
	
	public boolean visit(SuperMethodInvocation node) {
		
		visitChild(node.getQualifier());
		
		IValueList genericTypes = new IValueList(values);
		if (node.getAST().apiLevel() >= AST.JLS3) {
			if (!node.typeArguments().isEmpty()) {
				for (Iterator it = node.typeArguments().iterator(); it.hasNext();) {
					Type t = (Type) it.next();
					genericTypes.add(visitChild(t));
				}
			}
		}

		for (Iterator it = node.arguments().iterator(); it.hasNext();) {
			Expression e = (Expression) it.next();
			visitChild(e);
		}
		
		ownValue = resolveBinding(node);
		insert(invocation, getParent(), ownValue);
		
		return false;
	}
	
	public boolean visit(SwitchCase node) {
		
		visitChild(node.getExpression());
		
		ownValue = resolveBinding(node);			
		
		return false;
	}
	
	public boolean visit(SwitchStatement node) {
		
		visitChild(node.getExpression());
	
		for (Iterator it = node.statements().iterator(); it.hasNext();) {
			Statement s = (Statement) it.next();
			visitChild(s);
		}
	
		ownValue = resolveBinding(node);
		
		return false;
	}
	
	public boolean visit(SynchronizedStatement node) {
		visitChild(node.getExpression());
		visitChild(node.getBody());
		
		ownValue = resolveBinding(node);
		
		return false;
	}
	
	public boolean visit(TagElement node) {
		
		return false;
	}
	
	public boolean visit(TextElement node) {
		
		return false;
	}
	
	public boolean visit(ThisExpression node) {
		ownValue = resolveBinding(node);
		
		return false;
	}
	
	public boolean visit(ThrowStatement node) {
		
		visitChild(node.getExpression());
		
		ownValue = resolveBinding(node);
		
		return false;
	}
	
	public boolean visit(TryStatement node) {
		
		visitChild(node.getBody());
	
		for (Iterator it = node.catchClauses().iterator(); it.hasNext();) {
			CatchClause cc = (CatchClause) it.next();
			visitChild(cc);
		}
		
		visitChild(node.getFinally()); 
		
		ownValue = resolveBinding(node);
		
		return false;
	}
	
	public boolean visit(TypeDeclaration node) {
		scopeManager.push((ISourceLocation) ownValue);
		IValueList extendedModifiers = parseExtendedModifiers(node);
		
		if (node.getAST().apiLevel() >= AST.JLS3) {
			if (!node.typeParameters().isEmpty()) {			
				for (Iterator it = node.typeParameters().iterator(); it.hasNext();) {
					TypeParameter t = (TypeParameter) it.next();
					visitChild(t);			
				}
			}
		}
		
		IValueList extendsClass = new IValueList(values);
		IValueList implementsInterfaces = new IValueList(values);
		
		if (node.getAST().apiLevel() == AST.JLS2) {
			if (node.getSuperclass() != null) {
				extendsClass.add(resolveBinding(node.getSuperclass()));
			}
			if (!node.superInterfaces().isEmpty()) {
				for (Iterator it = node.superInterfaces().iterator(); it.hasNext();) {
					Name n = (Name) it.next();
					implementsInterfaces.add(resolveBinding(n));
				}
			}
		} else if (node.getAST().apiLevel() >= AST.JLS3) {
			if (node.getSuperclassType() != null) {
				extendsClass.add(resolveBinding(node.getSuperclassType()));
			}
			if (!node.superInterfaceTypes().isEmpty()) {
				for (Iterator it = node.superInterfaceTypes().iterator(); it.hasNext();) {
					Type t = (Type) it.next();
					implementsInterfaces.add(resolveBinding(t));
				}
			}
		}
		
		for (Iterator it = node.bodyDeclarations().iterator(); it.hasNext();) {
			BodyDeclaration d = (BodyDeclaration) it.next();
			visitChild(d);
		}
		
		ownValue = scopeManager.pop();
		insert(modifiers, ownValue, extendedModifiers);
		insert(inheritance, ownValue, extendsClass);
		insert(inheritance, ownValue, implementsInterfaces);
		insert(containment, getParent(), ownValue);
		
		return false;
	}
	
	public boolean visit(TypeDeclarationStatement node) {
		if (node.getAST().apiLevel() == AST.JLS2) {
			visitChild(node.getTypeDeclaration());
		}
		else {
			visitChild(node.getDeclaration());
		}
		
		ownValue = resolveBinding(node);
		
		return false;
	}
	
	public boolean visit(TypeLiteral node) {
		visitChild(node.getType());
		ownValue = resolveBinding(node);
		
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
		
		ownValue = constructDeclarationNode("typeParameter", name, extendsList.asList());
		
		return false;
	}
	
	public boolean visit(UnionType node) {
		
		IValueList typesValues = new IValueList(values);
		for(Iterator types = node.types().iterator(); types.hasNext();) {
			Type type = (Type) types.next();
			typesValues.add(visitChild(type));
		}
		
		ownValue = constructTypeNode("unionType", typesValues.asList());

		setAnnotation("binding", super.resolveBinding(node));
		return false;
	}
	
	public boolean visit(VariableDeclarationExpression node) {
		
		IValueList extendedModifiers = parseExtendedModifiers(node.modifiers());
		
		IValue type = visitChild(node.getType());
		
		IValueList fragments = new IValueList(values);
		for (Iterator it = node.fragments().iterator(); it.hasNext();) {
			VariableDeclarationFragment f = (VariableDeclarationFragment) it.next();
			fragments.add(visitChild(f));
		}
		
		for (IValue fragment: (IList)fragments.asList()) {
			insert(modifiers, fragment, extendedModifiers);
			insert(containment, getParent(), ownValue);
			insert(types, fragment, type);
		}
		
		return false;
	}
	
	public boolean visit(VariableDeclarationFragment node) {
		
		ASTNode initializer = node.getInitializer();
		
		visitChild(initializer);
		
		ownValue = resolveBinding(node);
		
		
		return false;
	}
	
	public boolean visit(VariableDeclarationStatement node) {
		
		IValueList extendedModifiers = parseExtendedModifiers(node.modifiers());
		
		IValue type = visitChild(node.getType());
	
		IValueList fragments = new IValueList(values);
		for (Iterator it = node.fragments().iterator(); it.hasNext();) {
			VariableDeclarationFragment f = (VariableDeclarationFragment) it.next();
			fragments.add(visitChild(f));
		}
		
		for (IValue fragment: (IList)fragments.asList()) {
			insert(modifiers, fragment, extendedModifiers);
			insert(containment, getParent(), ownValue);
			insert(types, fragment, type);
		}
		
		return false;
	}
	
	public boolean visit(WhileStatement node) {
		visitChild(node.getExpression());
		visitChild(node.getBody());
		
		ownValue = resolveBinding(node);
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
		ownValue = constructTypeNode(name, type);

		setAnnotation("binding", super.resolveBinding(node));
		return false;
	}
}
