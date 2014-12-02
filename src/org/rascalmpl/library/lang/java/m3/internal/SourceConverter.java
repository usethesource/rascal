package org.rascalmpl.library.lang.java.m3.internal;

import java.io.File;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.ISourceLocation;
import org.eclipse.imp.pdb.facts.type.TypeStore;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.AbstractTypeDeclaration;
import org.eclipse.jdt.core.dom.Annotation;
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

@SuppressWarnings({"rawtypes", "deprecation"})
public class SourceConverter extends M3Converter {
	SourceConverter(TypeStore typeStore) {
		super(typeStore);
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
		if (node instanceof Annotation) {
			insert(annotations, getParent(), resolveBinding(((Annotation) node).getTypeName()));
			return;
		}
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
		IConstructor type = bindingsResolver.resolveType(node.getType().resolveBinding(), true);
	    insert(types, ownValue, type);
	}
	
	public boolean visit(AnonymousClassDeclaration node) {
		insert(containment, getParent(), ownValue);
		// enum constant declaration and classinstancecreation gives types for anonymousclasses
		ASTNode parent = node.getParent();
		if (parent instanceof ClassInstanceCreation) {
			ISourceLocation superclass = resolveBinding(((ClassInstanceCreation) parent).getType());
			insert(typeDependency, ownValue, superclass);
			IConstructor type = bindingsResolver.resolveType(((ClassInstanceCreation) parent).getType().resolveBinding(), false);
		  	insert(types, ownValue, type);
		  	
		  	if (!superclass.getScheme().contains("+interface")) {
		  		insert(extendsRelations, ownValue, superclass);
		  	}
		  	else {
		  		insert(implementsRelations, ownValue, superclass);
		  	}
		}
		else if (parent instanceof EnumConstantDeclaration) {
			insert(typeDependency, ownValue, resolveBinding(((EnumConstantDeclaration) parent).resolveVariable()));
			IConstructor type = bindingsResolver.resolveType(((EnumConstantDeclaration) parent).resolveVariable().getType(), false);
		  	insert(types, ownValue, type);
		}
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
	  	insert(uses, getSourceLocation(node), ownValue);
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
    IConstructor type = bindingsResolver.resolveType(node.resolveBinding(), true);
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
		ASTNode parent = node.getParent();
		if (parent == null) {
			parent = node.getAlternateRoot();
		}
		insert(documentation, resolveBinding(parent), getSourceLocation(node));
		return false;
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
		
		IConstructor type = bindingsResolver.resolveType(node.resolveBinding(), true);
		insert(types, ownValue, type);
	}
	
	private void fillOverrides(IMethodBinding node, ITypeBinding parent) {
		if (node == null || parent == null) {
			insert(messages, values.constructor(DATATYPE_RASCAL_MESSAGE_ERROR_NODE_TYPE,
					values.string("parent or method binding is null, not proceeding with fillOverrides"),
					getSourceLocation(compilUnit.findDeclaringNode(node))));
			return;
		}
		
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
	
	private void generatePackageDecls(ISourceLocation parent, ISourceLocation pkg, ISourceLocation folder) {
	    insert(declarations, pkg, folder);
	  
	    if (!(parent == null)) {
	      insert(containment, parent, pkg);
	      pkg = parent;
	      generatePackageDecls(getParent(pkg), pkg, getParent(folder));
	    }
    }
	
	private ISourceLocation getParent(ISourceLocation sourceLoc) {
		File file = new File(sourceLoc.getPath());
		String parent = file.getParent();
		if (parent != null && !parent.equals("/")) {
			parent = parent.replaceAll(Matcher.quoteReplacement("\\"), "/");
			String authority = null;
			if (sourceLoc.hasAuthority())
				authority = sourceLoc.getAuthority();
			try {
				return values.sourceLocation(sourceLoc.getScheme(), authority, parent);
			} catch (URISyntaxException e) {
				throw new RuntimeException("Should not happen", e);
			}
		}
		return null; // there is no parent;
	}
	
	public boolean visit(PackageDeclaration node) {
		IPackageBinding binding = node.resolveBinding();
		
		if (binding != null) {
		  generatePackageDecls(getParent((ISourceLocation) ownValue), (ISourceLocation) ownValue, getParent(loc));
		  insert(containment, ownValue, getParent());
		} else {
			insert(messages, values.constructor(DATATYPE_RASCAL_MESSAGE_ERROR_NODE_TYPE,
					values.string("Unresolved binding for: " + node),
					values.sourceLocation(loc, 0, 0)));
		}
		
		scopeManager.push((ISourceLocation) ownValue);
		return true;
	}
	
	public void endVisit(PackageDeclaration node) {
		ownValue = scopeManager.pop();
	}
	
	public boolean visit(QualifiedName node) {
		if (((ISourceLocation) ownValue).getScheme().equals("java+field")) {
			insert(fieldAccess, getParent(), ownValue);
		}
		return true;
	}
	
	public boolean visit(SimpleName node) {
		insert(names, values.string(node.getIdentifier()), ownValue);
		
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
			if (((ISourceLocation) ownValue).getScheme().equals("java+field")) {
			  insert(fieldAccess, getParent(), ownValue);
			}
		}
	}

	public boolean visit(SingleVariableDeclaration node) {
		insert(containment, getParent(), ownValue);
		scopeManager.push((ISourceLocation) ownValue);
		return true;
	}
	
	public void endVisit(SingleVariableDeclaration node) {
		ownValue = scopeManager.pop();
	    IConstructor type = bindingsResolver.resolveType(node.getType().resolveBinding(), false);
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
		
		if (node.isInterface()) {
			insert(extendsRelations, ownValue, implementsInterfaces);
		} else {
			insert(extendsRelations, ownValue, extendsClass);
			insert(implementsRelations, ownValue, implementsInterfaces);
		}
		
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
		IVariableBinding binding = node.resolveBinding();
		
		if (binding != null) {
		  IConstructor type = bindingsResolver.resolveType(binding.getType(), false);
		  insert(types, ownValue, type);
		}
		else {
			insert(messages, values.constructor(DATATYPE_RASCAL_MESSAGE_ERROR_NODE_TYPE,
					values.string("No binding for: " + node),
					values.sourceLocation(loc, 0, 0)));
		}
		
		ASTNode parentASTNode = node.getParent();
		if (parentASTNode instanceof FieldDeclaration) {
			FieldDeclaration parent = (FieldDeclaration)parentASTNode;
			parent.getType().accept(this);
			visitListOfModifiers(parent.modifiers());
			if (parent.getJavadoc() != null) {
				parent.getJavadoc().accept(this);
			}
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
		scopeManager.pop();
		return true;
	}

	public boolean visit(VariableDeclarationStatement node) {
		visitFragments(node.fragments());
		return false;
	}
}
