package org.rascalmpl.library.lang.java.m3.internal;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.ISetWriter;
import org.eclipse.imp.pdb.facts.ISourceLocation;
import org.eclipse.imp.pdb.facts.IString;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.type.TypeFactory;
import org.eclipse.imp.pdb.facts.type.TypeStore;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.AbstractTypeDeclaration;
import org.eclipse.jdt.core.dom.AnnotationTypeDeclaration;
import org.eclipse.jdt.core.dom.AnnotationTypeMemberDeclaration;
import org.eclipse.jdt.core.dom.AnonymousClassDeclaration;
import org.eclipse.jdt.core.dom.BlockComment;
import org.eclipse.jdt.core.dom.ClassInstanceCreation;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.ConstructorInvocation;
import org.eclipse.jdt.core.dom.EnumConstantDeclaration;
import org.eclipse.jdt.core.dom.EnumDeclaration;
import org.eclipse.jdt.core.dom.FieldAccess;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.IPackageBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.IVariableBinding;
import org.eclipse.jdt.core.dom.Initializer;
import org.eclipse.jdt.core.dom.Javadoc;
import org.eclipse.jdt.core.dom.LineComment;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.Name;
import org.eclipse.jdt.core.dom.PackageDeclaration;
import org.eclipse.jdt.core.dom.QualifiedName;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.SuperConstructorInvocation;
import org.eclipse.jdt.core.dom.SuperFieldAccess;
import org.eclipse.jdt.core.dom.SuperMethodInvocation;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.TypeParameter;
import org.eclipse.jdt.core.dom.VariableDeclarationExpression;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;
import org.rascalmpl.uri.URIUtil;

@SuppressWarnings({"rawtypes", "deprecation"})
public class M3Converter extends JavaToRascalConverter {
	private static final String DATATYPE_M3_NODE							= "M3";
	private final org.eclipse.imp.pdb.facts.type.Type DATATYPE_M3_NODE_TYPE;
	private final org.eclipse.imp.pdb.facts.type.Type DATATYPE_TYPESYMBOL;
	
	private static final org.eclipse.imp.pdb.facts.type.Type locType 		= TF.sourceLocationType();
	private static final org.eclipse.imp.pdb.facts.type.Type m3TupleType 	= TF.tupleType(locType, locType);
	private final org.eclipse.imp.pdb.facts.type.Type m3LOCModifierType;
	private final org.eclipse.imp.pdb.facts.type.Type m3LOCTypeType;
	
	private final Stack<ISourceLocation> scopeManager = new Stack<ISourceLocation>();
	
	private ISetWriter uses;
	private ISetWriter declarations;
	private ISetWriter containment;
	private ISetWriter extendsRelations;
	private ISetWriter implementsRelations;
	private ISetWriter fieldAccess;
	private ISetWriter methodInvocation;
	private ISetWriter typeDependency;
	private ISetWriter documentation;
	private ISetWriter modifiers;
	private ISetWriter names;
	private ISetWriter methodOverrides;
  private ISetWriter types;
  private final org.eclipse.imp.pdb.facts.type.Type CONSTRUCTOR_M3;
	
	M3Converter(final TypeStore typeStore) {
		super(typeStore, true);
		this.DATATYPE_M3_NODE_TYPE = this.typeStore.lookupAbstractDataType(DATATYPE_M3_NODE);
		TypeFactory tf = TypeFactory.getInstance();
    this.CONSTRUCTOR_M3= this.typeStore.lookupConstructor(DATATYPE_M3_NODE_TYPE, "m3", tf.tupleType(tf.sourceLocationType()));
		this.DATATYPE_TYPESYMBOL = this.typeStore.lookupAbstractDataType("TypeSymbol");
		uses = values.relationWriter(m3TupleType);
		declarations = values.relationWriter(m3TupleType);
		containment = values.relationWriter(m3TupleType);
		extendsRelations = values.relationWriter(m3TupleType);
		implementsRelations = values.relationWriter(m3TupleType);
		fieldAccess = values.relationWriter(m3TupleType);
		methodInvocation = values.relationWriter(m3TupleType);
		m3LOCModifierType = TF.tupleType(locType, DATATYPE_RASCAL_AST_MODIFIER_NODE_TYPE);
		modifiers = values.relationWriter(m3LOCModifierType);
		m3LOCTypeType = TF.tupleType(locType, locType);
		typeDependency = values.relationWriter(m3LOCTypeType);
		documentation = values.relationWriter(m3TupleType);
		names = values.relationWriter(TF.tupleType(TF.stringType(), locType));
		methodOverrides = values.relationWriter(TF.tupleType(locType, locType));
		types = values.relationWriter(TF.tupleType(locType, DATATYPE_TYPESYMBOL));
	}
	
	public IValue getModel() {
		ownValue = values.constructor(CONSTRUCTOR_M3, loc);
		setAnnotation("declarations", declarations.done());
		setAnnotation("uses", uses.done());
		setAnnotation("containment", containment.done());
		setAnnotation("extends", extendsRelations.done());
		setAnnotation("implements", implementsRelations.done());
		setAnnotation("methodInvocation", methodInvocation.done());
		setAnnotation("modifiers", modifiers.done());
		setAnnotation("typeDependency", typeDependency.done());
		setAnnotation("documentation", documentation.done());
		setAnnotation("fieldAccess", fieldAccess.done());
		setAnnotation("names", names.done());
		setAnnotation("methodOverrides", methodOverrides.done());
		setAnnotation("types", types.done());
		insertCompilationUnitMessages();
		return ownValue;
	}
	
	public ISourceLocation getParent() {
		return scopeManager.peek();
	}
	
	public void insert(ISetWriter relW, IValue lhs, IValue rhs) {
		if ((isValid((ISourceLocation) lhs) && isValid((ISourceLocation) rhs))) {
			relW.insert(values.tuple(lhs, rhs));
		}
	}

	public void insert(ISetWriter relW, IValue lhs, IValueList rhs) {
		for (IValue oneRHS: (IList)rhs.asList())
			if (lhs.getType().isString() || (isValid((ISourceLocation) lhs) && isValid((ISourceLocation) oneRHS)))
				insert(relW, lhs, oneRHS);
	}
	
	public void insert(ISetWriter relW, IString lhs, IValue rhs) {
		if (isValid((ISourceLocation) rhs)) {
			relW.insert(values.tuple(lhs, rhs));
		}
	}
	
	public void insert(ISetWriter relW, IValue lhs, IConstructor rhs) {
		relW.insert(values.tuple(lhs, rhs));
	}
	
	private boolean isValid(ISourceLocation binding) {
		return binding != null && !(binding.getURI().getScheme().equals("unknown") || binding.getURI().getScheme().equals("unresolved"));
	}
	
	private void visitListOfModifiers(List modif) {
		for (Iterator it = modif.iterator(); it.hasNext(); ) {
			((ASTNode)it.next()).accept(this);
		}
	}
	
	private void visitFragments(List fragments) {
		for (Iterator it = fragments.iterator(); it.hasNext(); ) {
			((VariableDeclarationFragment) it.next()).accept(this);
		}
	}
	
	private boolean simpleNameIsConstructorDecl(SimpleName node) {
		ASTNode parent = node.getParent();
		if (parent instanceof MethodDeclaration) {
			if (((MethodDeclaration) parent).isConstructor() && ((MethodDeclaration) parent).getName() == node) {
				return true;
			}
		}
		return false;
	}
	
	private void addTypeDependency(ISourceLocation dependency) {
	  if (!scopeManager.isEmpty()) {
	    ISourceLocation parent = getParent();
	    if (!parent.isEqual(dependency)) {
	      insert(typeDependency, parent, dependency);
	    }
	  }
	}
	
	public void preVisit(ASTNode node) {
		ownValue = resolveBinding(node);
	}
	
	public boolean visit(AnnotationTypeDeclaration node) {
		insert(containment, getParent(), ownValue);
		scopeManager.push((ISourceLocation) ownValue);
		return true;
	}
	
	public void endVisit(AnnotationTypeDeclaration node) {
		ownValue = scopeManager.pop();
		computeTypeSymbol(node);
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
		if (parent instanceof ClassInstanceCreation) {
			insert(typeDependency, ownValue, resolveBinding(((ClassInstanceCreation) parent).getType()));
		}
		else if (parent instanceof EnumConstantDeclaration) {
			insert(typeDependency, ownValue, resolveBinding(((EnumConstantDeclaration) parent).resolveVariable()));
		}
		insert(declarations, ownValue, getSourceLocation(node));
		scopeManager.push((ISourceLocation) ownValue);
	  IConstructor type = bindingsResolver.computeTypeSymbol(node.resolveBinding(), false);
    insert(types, ownValue, type);
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
	  insert(declarations, ownValue, getSourceLocation(node));
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
		insert(implementsRelations, ownValue, implementedInterfaces);
		
		scopeManager.push((ISourceLocation) ownValue);
		return true;
	}
	
	public void endVisit(EnumDeclaration node) {
		ownValue = scopeManager.pop();
	  computeTypeSymbol(node);
	}

  private void computeTypeSymbol(AbstractTypeDeclaration node) {
    IConstructor type = bindingsResolver.computeTypeSymbol(node.resolveBinding(), true);
    insert(types, ownValue, type);
  }
	
	public boolean visit(FieldAccess node) {
		insert(fieldAccess, getParent(), ownValue);
		return true;
	}
	
	public boolean visit(FieldDeclaration node) {
		visitFragments(node.fragments());
		return false;
	}
	
	public boolean visit(Initializer node) {
		insert(containment, getParent(), ownValue);
		insert(declarations, ownValue, getSourceLocation(node));
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
		ASTNode parent = node.getParent();
		if (parent instanceof TypeDeclaration) {
		  // TODO: why can this node.resolveBinding sometimes be null?
			fillOverrides(node.resolveBinding(), ((TypeDeclaration)parent).resolveBinding());
		}
		else if (parent instanceof AnonymousClassDeclaration) {
			fillOverrides(node.resolveBinding(), ((AnonymousClassDeclaration)parent).resolveBinding());
		}
		
		IConstructor type = bindingsResolver.computeMethodTypeSymbol(node.resolveBinding(), true);
		insert(types, ownValue, type);
	}
	
	private void fillOverrides(IMethodBinding node, ITypeBinding parent) {
		List<ITypeBinding> parentClass = new ArrayList<ITypeBinding>();
		parentClass.addAll(Arrays.asList(parent.getInterfaces()));
		parentClass.add(parent.getSuperclass());
		
		for (ITypeBinding parentBinding: parentClass) {
			if (parentBinding == null) {
				return;
			}
			for (IMethodBinding parentMethod: parentBinding.getDeclaredMethods()) {
				if (node.overrides(parentMethod) && node.isSubsignature(parentMethod)) {
					insert(methodOverrides, resolveBinding(node), resolveBinding(parentMethod));
				}
			}
			fillOverrides(node, parentBinding);
		}
	}
	
	public boolean visit(MethodInvocation node) {
		insert(methodInvocation, getParent(), ownValue);
		//TODO: add to uses as well
		return true;
	}
	
	public boolean visit(Modifier node) {
		String modifier = node.getKeyword().toString();
		insert(modifiers, getParent(), constructModifierNode(modifier));
		return true;
	}
	
	private void generatePackageDecls(URI child, URI pkg, URI folder) {
	  if (pkg == null) {
	    return;
	  }
	  insert(containment, values.sourceLocation(pkg), values.sourceLocation(child));
    insert(names, values.string(pkg.getPath()), values.sourceLocation(pkg));
    insert(declarations, values.sourceLocation(pkg), values.sourceLocation(folder));
  
    generatePackageDecls(pkg, URIUtil.getParentURI(pkg), URIUtil.getParentURI(folder));
	}
	
	public boolean visit(PackageDeclaration node) {
		IPackageBinding binding = node.resolveBinding();
		
		if (binding == null) {
		  // TODO
		  System.err.println("Unresolved binding for: "+ node);
		  return true;
		}
		
		generatePackageDecls(getParent().getURI(), resolveBinding(binding).getURI(), URIUtil.getParentURI(loc.getURI()));
	
		insert(containment, ownValue, getParent());
		
		scopeManager.push((ISourceLocation) ownValue);
		return true;
	}
	
	public void endVisit(PackageDeclaration node) {
		ownValue = scopeManager.pop();
	}
	
	public boolean visit(QualifiedName node) {
		if (((ISourceLocation) ownValue).getURI().getScheme().equals("java+field")) {
			insert(fieldAccess, getParent(), ownValue);
		}
		return true;
	}
	
	public boolean visit(SimpleName node) {
		insert(names, values.string(node.getIdentifier()), values.sourceLocation(((ISourceLocation) ownValue).getURI()));
		
		if (((ISourceLocation)ownValue).getURI().getScheme().equals("java+field")) {
			if (!getParent().isEqual((ISourceLocation) ownValue)) {
				insert(fieldAccess, getParent(), ownValue);
			}
		}
		
		if (!simpleNameIsConstructorDecl(node)) {
			addTypeDependency(resolveBinding(node.resolveTypeBinding()));
			if (!node.isDeclaration()) {
				addTypeDependency(resolveDeclaringClass(node.resolveBinding()));
			}
		}
		
		return true;
	}
	
	public void endVisit(SimpleName node) {
		if ((node.isDeclaration() || simpleNameIsConstructorDecl(node))) {
			insert(declarations, ownValue, getSourceLocation(compilUnit.findDeclaringNode(node.resolveBinding())));
		}
		else {
			insert(uses, getSourceLocation(node), ownValue);
		}
	}

	public boolean visit(SingleVariableDeclaration node) {
		insert(containment, getParent(), ownValue);
		scopeManager.push((ISourceLocation) ownValue);
		return true;
	}
	
	public void endVisit(SingleVariableDeclaration node) {
		ownValue = scopeManager.pop();
	  IConstructor type = bindingsResolver.computeTypeSymbol(node.getType().resolveBinding(), false);
    insert(types, ownValue, type);
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
		} 
		else if (node.getAST().apiLevel() >= AST.JLS3) {
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
		
		insert(extendsRelations, ownValue, extendsClass);
		insert(implementsRelations, ownValue, implementsInterfaces);
		
		return true;
	}
	
	
	public void endVisit(TypeDeclaration node) {
		ownValue = scopeManager.pop();
		computeTypeSymbol(node);
	}
	
	public boolean visit(TypeParameter node) {
		IValueList extendsList = new IValueList(values);
		if (!node.typeBounds().isEmpty()) {
			for (Iterator it = node.typeBounds().iterator(); it.hasNext();) {
				Type t = (Type) it.next();
				extendsList.add(resolveBinding(t));
			}
		}
		
		insert(containment, getParent(), ownValue);
		
		return true;
	}
	
	public boolean visit(VariableDeclarationExpression node) {
		visitFragments(node.fragments());
		return false;
	}
	
	public boolean visit(VariableDeclarationFragment node) {
		insert(containment, getParent(), ownValue);
		
		scopeManager.push((ISourceLocation) ownValue);
		ASTNode parentASTNode = node.getParent();
		if (parentASTNode instanceof FieldDeclaration) {
			FieldDeclaration parent = (FieldDeclaration)parentASTNode;
			parent.getType().accept(this);
			visitListOfModifiers(parent.modifiers());
		} 
		else if (parentASTNode instanceof VariableDeclarationExpression) {
			VariableDeclarationExpression parent = (VariableDeclarationExpression)parentASTNode;
			parent.getType().accept(this);
			visitListOfModifiers(parent.modifiers());
		} 
		else {
			VariableDeclarationStatement parent = (VariableDeclarationStatement)parentASTNode;
			parent.getType().accept(this);
			visitListOfModifiers(parent.modifiers());
		}
		
		return true;
	}
	
	public void endVisit(VariableDeclarationFragment node) {
	  ownValue = scopeManager.pop();
		IVariableBinding binding = node.resolveBinding();
		
		if (binding != null) {
		  IConstructor type = bindingsResolver.computeTypeSymbol(binding.getType(), false);
		  insert(types, ownValue, type);
		}
		else {
		  System.err.println("no binding for " + node);
		}
	}

	public boolean visit(VariableDeclarationStatement node) {
		visitFragments(node.fragments());
		return false;
	}
}
