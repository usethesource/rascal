package org.rascalmpl.library.lang.java.m3.internal;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.ISetWriter;
import org.eclipse.imp.pdb.facts.ISourceLocation;
import org.eclipse.imp.pdb.facts.IString;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.type.TypeStore;
import org.eclipse.jdt.core.dom.*;
import org.rascalmpl.uri.URIUtil;

@SuppressWarnings({"rawtypes", "deprecation"})
public class M3Converter extends JavaToRascalConverter {
	private static final String DATATYPE_M3_NODE							= "M3";
	private final org.eclipse.imp.pdb.facts.type.Type DATATYPE_M3_NODE_TYPE;
	
	private static final org.eclipse.imp.pdb.facts.type.Type locType 		= TF.sourceLocationType();
	private static final org.eclipse.imp.pdb.facts.type.Type m3TupleType 	= TF.tupleType(locType, locType);
//	private static final org.eclipse.imp.pdb.facts.type.Type m3MapType   	= TF.mapType(locType, locType);
	private final org.eclipse.imp.pdb.facts.type.Type m3LOCModifierType;
	private final org.eclipse.imp.pdb.facts.type.Type m3LOCTypeType;
	
	private final Stack<ISourceLocation> scopeManager = new Stack<ISourceLocation>();
	
	private ISetWriter uses;
	private ISetWriter declarations;
	private ISetWriter containment;
	private ISetWriter typeInheritance;
	private ISetWriter fieldAccess;
	private ISetWriter methodInvocation;
	private ISetWriter typeDependency;
	private ISetWriter documentation;
	private ISetWriter modifiers;
	private ISetWriter names;
	
	M3Converter(final TypeStore typeStore) {
		super(typeStore, true);
		this.DATATYPE_M3_NODE_TYPE = this.typeStore.lookupAbstractDataType(DATATYPE_M3_NODE);
		uses = values.relationWriter(m3TupleType);
		declarations = values.relationWriter(m3TupleType);
		containment = values.relationWriter(m3TupleType);
		typeInheritance = values.relationWriter(m3TupleType);
		fieldAccess = values.relationWriter(m3TupleType);
		methodInvocation = values.relationWriter(m3TupleType);
		m3LOCModifierType = TF.tupleType(locType, DATATYPE_RASCAL_AST_MODIFIER_NODE_TYPE);
		modifiers = values.relationWriter(m3LOCModifierType);
		m3LOCTypeType = TF.tupleType(locType, locType);
		typeDependency = values.relationWriter(m3LOCTypeType);
		documentation = values.relationWriter(m3TupleType);
		names = values.relationWriter(TF.tupleType(TF.stringType(), locType)); 
	}
	
	public IValue getModel() {
		ownValue = values.constructor(DATATYPE_M3_NODE_TYPE);
		setAnnotation("declarations", declarations.done());
		setAnnotation("uses", uses.done());
		setAnnotation("containment", containment.done());
		setAnnotation("typeInheritance", typeInheritance.done());
		setAnnotation("methodInvocation", methodInvocation.done());
		setAnnotation("modifiers", modifiers.done());
		setAnnotation("typeDependency", typeDependency.done());
		setAnnotation("documentation", documentation.done());
		setAnnotation("fieldAccess", fieldAccess.done());
		setAnnotation("names", names.done());
		insertCompilationUnitMessages();
		return ownValue;
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
		if ((isValid((ISourceLocation) lhs) && isValid((ISourceLocation) rhs)))
			relW.insert(values.tuple(lhs, rhs));
	}

	public void insert(ISetWriter relW, IValue lhs, IValueList rhs) {
		for (IValue oneRHS: (IList)rhs.asList())
			if (lhs.getType().isString() || (isValid((ISourceLocation) lhs) && isValid((ISourceLocation) oneRHS)))
				insert(relW, lhs, oneRHS);
	}
	
	public void insert(ISetWriter relW, IString lhs, IValue rhs) {
		relW.insert(values.tuple(lhs, rhs));
	}
	
	public void insert(ISetWriter relW, IValue lhs, IConstructor rhs) {
		relW.insert(values.tuple(lhs, rhs));
	}
	
	private boolean isValid(ISourceLocation binding) {
		return !(binding.getURI().getScheme().equals("unknown") || binding.getURI().getScheme().equals("unresolved"));
	}
	
	public void preVisit(ASTNode node) {
		ownValue = resolveBinding(node);
	}
	
	public boolean visit(Annotation node) {
		insert(typeDependency, getParent(), ownValue);
		return true; //??
	}
	
	public boolean visit(AnnotationTypeDeclaration node) {
		insert(containment, getParent(), ownValue);
		scopeManager.push((ISourceLocation) ownValue);
		return true;
	}
	
	public void endVisit(AnnotationTypeDeclaration node) {
		ownValue = scopeManager.pop();
	}
	
	public boolean visit(AnnotationTypeMemberDeclaration node) {
		insert(containment, getParent(), ownValue);
		scopeManager.push((ISourceLocation) ownValue);
		return true;
	}
	
	public void endVisit(AnnotationTypeMemberDeclaration node) {
		ownValue = scopeManager.pop();
	}
	
	public boolean visit(AnonymousClassDeclaration node) {
		insert(containment, getParent(), ownValue);
		// enum constant declaration and classinstancecreation gives types for anonymousclasses
		ASTNode parent = node.getParent();
		if (parent instanceof ClassInstanceCreation)
			insert(typeDependency, ownValue, resolveBinding(((ClassInstanceCreation) parent).getType()));
		else if (parent instanceof EnumConstantDeclaration)
			insert(typeDependency, ownValue, resolveBinding(((EnumConstantDeclaration) parent).resolveVariable()));
		insert(declarations, ownValue, getSourceLocation(node));
		scopeManager.push((ISourceLocation) ownValue);
		return true;
	}
	
	public void endVisit(AnonymousClassDeclaration node) {
		ownValue = scopeManager.pop();
	}
	
	public boolean visit(BlockComment node) {
		insert(documentation, resolveBinding(node.getAlternateRoot()), getSourceLocation(node));
		return true;
	}
	
	public boolean visit(ClassInstanceCreation node) {
		insert(methodInvocation, getParent(), ownValue);
		return true;
	}
	
	public boolean visit(CompilationUnit node) {
		scopeManager.push((ISourceLocation) ownValue);
		return true;
	}
	
	public void endVisit(CompilationUnit node) {
		ownValue = scopeManager.pop();
	}
	
	public boolean visit(ConstructorInvocation node) {
		insert(methodInvocation, getParent(), ownValue);
		return true;
	}
	
	public boolean visit(EnumConstantDeclaration node) {
		insert(containment, getParent(), ownValue);
		scopeManager.push((ISourceLocation) ownValue);
		return true;
	}
	
	public void endVisit(EnumConstantDeclaration node) {
		ownValue = scopeManager.pop();
	}
	
	public boolean visit(EnumDeclaration node) {
		insert(containment, getParent(), ownValue);
		
		IValueList implementedInterfaces = new IValueList(values);
		if (!node.superInterfaceTypes().isEmpty()) {
			for (Iterator it = node.superInterfaceTypes().iterator(); it.hasNext();) {
				Type t = (Type) it.next();
				implementedInterfaces.add(resolveBinding(t));
			}
		}
		insert(typeInheritance, ownValue, implementedInterfaces);
		
		scopeManager.push((ISourceLocation) ownValue);
		return true;
	}
	
	public void endVisit(EnumDeclaration node) {
		ownValue = scopeManager.pop();
	}
	
	public boolean visit(FieldAccess node) {
		insert(fieldAccess, getParent(), ownValue);
		return true;
	}
	
	public boolean visit(Initializer node) {
		insert(containment, getParent(), ownValue);
		scopeManager.push((ISourceLocation) ownValue);
		return true;
	}
	
	public void endVisit(Initializer node) {
		ownValue = scopeManager.pop();
	}
	
	public boolean visit(Javadoc node) {
		insert(documentation, resolveBinding(node.getAlternateRoot()), getSourceLocation(node));
		return true;
	}
	
	public boolean visit(LineComment node) {
		insert(documentation, resolveBinding(node.getAlternateRoot()), getSourceLocation(node));
		return true;
	}
	
	public boolean visit(MethodDeclaration node) {
		insert(containment, getParent(), ownValue);
		scopeManager.push((ISourceLocation) ownValue);
		return true;
	}
	
	public void endVisit(MethodDeclaration node) {
		ownValue = scopeManager.pop();
	}
	
	public boolean visit(MethodInvocation node) {
		insert(methodInvocation, getParent(), ownValue);
		return true;
	}
	
	public boolean visit(Modifier node) {
		String modifier = node.getKeyword().toString();
		insert(modifiers, getParent(), constructModifierNode(modifier));
		return true;
	}
	
	public boolean visit(PackageDeclaration node) {
		String parent = "";
		for (String component: node.resolveBinding().getNameComponents()) {
			if (!parent.isEmpty()) {
				insert(containment, resolveBinding(parent), resolveBinding(parent+"/"+component));
				insert(names, values.string(component), resolveBinding(parent+"/"+component));
				parent += ".";
			}
			parent += component;
		}
		
		insert(containment, ownValue, getParent());
		
		scopeManager.push((ISourceLocation) ownValue);
		return true;
	}
	
	public void endVisit(PackageDeclaration node) {
		ownValue = scopeManager.pop();
	}
	
	public boolean visit(QualifiedName node) {
		if (((ISourceLocation) ownValue).getURI().getScheme().equals("java+field"))
			insert(fieldAccess, getParent(), ownValue);
		return true;
	}
	
	public boolean visit(SimpleName node) {
		URI uri = ((ISourceLocation) ownValue).getURI();
		try {
			insert(names, values.string(node.getIdentifier()), values.sourceLocation(URIUtil.changePath(uri, uri.getPath().replaceAll("/", "."))));
		} catch (URISyntaxException e) {
			// should not happen
		}
		
		if (((ISourceLocation)ownValue).getURI().getScheme().equals("java+field")) {
			if (!getParent().isEqual((ISourceLocation) ownValue))
				insert(fieldAccess, getParent(), ownValue);
		}
		
		if (!node.isDeclaration()) {
			insert(typeDependency, getParent(), resolveBinding(node.resolveTypeBinding()));//???
			
			ISourceLocation declaringClass = resolveDeclaringClass(node.resolveBinding());
			if (!getParent().isEqual(declaringClass))
				insert(typeDependency, getParent(), declaringClass);
		}
		
		return true;
	}
	
	public void endVisit(SimpleName node) {
		if (node.isDeclaration())
			insert(declarations, ownValue, getSourceLocation(compilUnit.findDeclaringNode(node.resolveBinding())));
		else
			insert(uses, getSourceLocation(node), ownValue);
	}
	
	public boolean visit(SingleVariableDeclaration node) {
		insert(containment, getParent(), ownValue);
		scopeManager.push((ISourceLocation) ownValue);
		return true;
	}
	
	public void endVisit(SingleVariableDeclaration node) {
		ownValue = scopeManager.pop();
	}
	
	public boolean visit(SuperConstructorInvocation node) {
		insert(methodInvocation, getParent(), ownValue);
		return true;
	}
	
	public boolean visit(SuperFieldAccess node) {
		insert(fieldAccess, getParent(), ownValue);
		return true;
	}
	
	public boolean visit(SuperMethodInvocation node) {
		insert(methodInvocation, getParent(), ownValue);
		return true;
	}
	
	public boolean visit(TypeDeclaration node) {
		insert(containment, getParent(), ownValue);
		
		scopeManager.push((ISourceLocation) ownValue);
		
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
		
		insert(typeInheritance, ownValue, extendsClass);
		insert(typeInheritance, ownValue, implementsInterfaces);
		
		return true;
	}
	
	
	public void endVisit(TypeDeclaration node) {
		ownValue = scopeManager.pop();
	}
	
	public boolean visit(TypeParameter node) {
		// ???
		IValueList extendsList = new IValueList(values);
		if (!node.typeBounds().isEmpty()) {
			for (Iterator it = node.typeBounds().iterator(); it.hasNext();) {
				Type t = (Type) it.next();
				extendsList.add(resolveBinding(t));
			}
		}
		
		insert(typeInheritance, ownValue, extendsList);
		
		return true;
	}
	
	private void visitListOfModifiers(List modif) {
		for (Iterator it = modif.iterator(); it.hasNext(); ) {
			ASTNode next = (ASTNode)it.next();
			if (next instanceof Modifier)
				visit((Modifier) next);
			else if (next instanceof Annotation)
				visit((Annotation) next);
		}
	}
	
	public boolean visit(VariableDeclarationFragment node) {
		insert(containment, getParent(), ownValue);
		IValue type;
		
		scopeManager.push((ISourceLocation) ownValue);
		ASTNode parentASTNode = node.getParent();
		if (parentASTNode instanceof FieldDeclaration) {
			FieldDeclaration parent = (FieldDeclaration)parentASTNode;
			type = resolveBinding(parent.getType());
			visitListOfModifiers(parent.modifiers());
		} else if (parentASTNode instanceof VariableDeclarationExpression) {
			VariableDeclarationExpression parent = (VariableDeclarationExpression)parentASTNode;
			type = resolveBinding(parent.getType());
			visitListOfModifiers(parent.modifiers());
		} else {
			VariableDeclarationStatement parent = (VariableDeclarationStatement)parentASTNode;
			type = resolveBinding(parent.getType());
			visitListOfModifiers(parent.modifiers());
		}
		
		insert(typeDependency, ownValue, type);
		return true;
	}
	
	public void endVisit(VariableDeclarationFragment node) {
		ownValue = scopeManager.pop();
	}
}
