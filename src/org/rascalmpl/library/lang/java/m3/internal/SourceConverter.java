package org.rascalmpl.library.lang.java.m3.internal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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
import org.eclipse.jdt.core.dom.ExportsDirective;
import org.eclipse.jdt.core.dom.FieldAccess;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.IPackageBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.IVariableBinding;
import org.eclipse.jdt.core.dom.Initializer;
import org.eclipse.jdt.core.dom.Javadoc;
import org.eclipse.jdt.core.dom.LambdaExpression;
import org.eclipse.jdt.core.dom.LineComment;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.ModuleDeclaration;
import org.eclipse.jdt.core.dom.Name;
import org.eclipse.jdt.core.dom.OpensDirective;
import org.eclipse.jdt.core.dom.PackageDeclaration;
import org.eclipse.jdt.core.dom.ProvidesDirective;
import org.eclipse.jdt.core.dom.QualifiedName;
import org.eclipse.jdt.core.dom.RequiresDirective;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.SuperConstructorInvocation;
import org.eclipse.jdt.core.dom.SuperFieldAccess;
import org.eclipse.jdt.core.dom.SuperMethodInvocation;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.TypeParameter;
import org.eclipse.jdt.core.dom.UsesDirective;
import org.eclipse.jdt.core.dom.VariableDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationExpression;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;
import org.rascalmpl.uri.URIUtil;

import io.usethesource.vallang.IConstructor;
import io.usethesource.vallang.IListWriter;
import io.usethesource.vallang.ISourceLocation;

@SuppressWarnings("rawtypes")
public class SourceConverter extends M3Converter {
	SourceConverter(LimitedTypeStore typeStore, Map<String, ISourceLocation> cache) {
		super(typeStore, cache);
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
			if (!parent.equals(dependency)) {
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
	
	@Override
	public boolean visit(AnnotationTypeDeclaration node) {
		insert(containment, getParent(), ownValue);
		scopeManager.push((ISourceLocation) ownValue);
		return true;
	}
	
	@Override
	public void endVisit(AnnotationTypeDeclaration node) {
		ownValue = scopeManager.pop();
		computeTypeSymbol(node);
	}
	
	@Override
	public boolean visit(AnnotationTypeMemberDeclaration node) {
		insert(containment, getParent(), ownValue);
		scopeManager.push((ISourceLocation) ownValue);
		return true;
	}
	
	@Override
	public void endVisit(AnnotationTypeMemberDeclaration node) {
		ownValue = scopeManager.pop();
		IConstructor type = bindingsResolver.resolveType(node.getType().resolveBinding(), true);
	    insert(types, ownValue, type);
	}
	
	@Override
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
			IVariableBinding var = ((EnumConstantDeclaration) parent).resolveVariable();
            insert(typeDependency, ownValue, resolveBinding(var));
            
            if (var != null) {
                IConstructor type = bindingsResolver.resolveType(var.getType(), false);
                insert(types, ownValue, type);
            }
		}
		insert(declarations, ownValue, getSourceLocation(node));
		scopeManager.push((ISourceLocation) ownValue);
		return true;
	}
	
	@Override
	public void endVisit(AnonymousClassDeclaration node) {
		ownValue = scopeManager.pop();
	}
	
	@Override
	public boolean visit(BlockComment node) {
		insert(documentation, resolveBinding(node.getAlternateRoot()), getSourceLocation(node));
		return true;
	}
	
	@Override
	public boolean visit(ClassInstanceCreation node) {
		insert(methodInvocation, getParent(), ownValue);
	  	insert(uses, getSourceLocation(node), ownValue);
		return true;
	}
	
	@Override
	public boolean visit(CompilationUnit node) {
		insert(declarations, ownValue, getSourceLocation(node));
		scopeManager.push((ISourceLocation) ownValue);
		return true;
	}
	
	@Override
	public boolean visit(ModuleDeclaration node) {
		insert(declarations, ownValue, getSourceLocation(node));
		insert(names, values.string(node.getName().getFullyQualifiedName()), ownValue);

		scopeManager.push((ISourceLocation) ownValue);
		return true;
	}

	@Override
	public void endVisit(ModuleDeclaration node) {
		scopeManager.pop();
	}

	@Override
	public boolean visit(RequiresDirective node) {
		var parent = scopeManager.peek();
		var name = resolveBinding(node.getName());
		// TODO: encode static and transitive modifiers somehow in the modifiers relation, or in the moduleRequires relation
		insert(moduleRequiresModule, parent, name);
		return true;
	}

	@Override
	public boolean visit(ExportsDirective node) {
		var parent = scopeManager.peek();
		var targets = ((List<?>) node.modules())
			.stream()
			.map(o -> ((ASTNode) o))
			.map(n -> resolveBinding(n)).collect(values.listWriter());
		var name = resolveBinding(node.getName());

		if (targets.isEmpty()) {
			// this encodes unqualified exports to all other modules
			insert(moduleExportsPackage, parent, name, URIUtil.rootLocation("java+module"));
		}
		else {
			for (var target : targets) {
				insert(moduleExportsPackage, parent, name , target);
			}
		}

		return true;
	}

	@Override
	public boolean visit(ProvidesDirective node) {
		var parent = scopeManager.peek();
		var implementations = ((List<?>) node.implementations())
			.stream()
			.map(o -> ((ASTNode) o))
			.map(n -> resolveBinding(n)).collect(values.listWriter());
		var name = resolveBinding(node.getName());

		for (var impl : implementations) {
			insert(moduleProvidesService, parent, name, impl);
		}

		return true;
	}

	@Override
	public boolean visit(UsesDirective node) {
		var parent = scopeManager.peek();
		insert(moduleUsesService, parent, resolveBinding(node.getName()));
		return true;
	}

	@Override
	public boolean visit(OpensDirective node) {
		var parent = scopeManager.peek();
		var name = resolveBinding(node.getName());
		var modules = ((List<?>) node.modules())
			.stream()
			.map(o -> ((ASTNode) o))
			.map(n -> resolveBinding(n)).collect(values.listWriter());
		
		if (modules.isEmpty()) {
			// this encodes unqualified opening to all other modules
			insert(moduleOpensPackage, parent, name, URIUtil.rootLocation("java+module"));
		}
		else {
			for (var module : modules) {
				insert(moduleOpensPackage, parent, name, module);
			}
		}

		return true;
	}

	@Override
	public void endVisit(CompilationUnit node) {
		ownValue = scopeManager.pop();
	}
	
	@Override
	public boolean visit(ConstructorInvocation node) {
		insert(methodInvocation, getParent(), ownValue);
		return true;
	}
	
	@Override
	public boolean visit(EnumConstantDeclaration node) {
		insert(containment, getParent(), ownValue);
		scopeManager.push((ISourceLocation) ownValue);
		return true;
	}
	
	@Override
	public void endVisit(EnumConstantDeclaration node) {
		ownValue = scopeManager.pop();
	}
	
	@Override
	public boolean visit(EnumDeclaration node) {
		// TODO: enums can have methods that override as well
		insert(containment, getParent(), ownValue);
		
		IListWriter implementedInterfaces = values.listWriter();
		if (!node.superInterfaceTypes().isEmpty()) {
			for (Iterator it = node.superInterfaceTypes().iterator(); it.hasNext();) {
				Type t = (Type) it.next();
				implementedInterfaces.append(resolveBinding(t));
			}
		}
		insert(implementsRelations, ownValue, implementedInterfaces.done());
		
		scopeManager.push((ISourceLocation) ownValue);
		return true;
	}
	
	@Override
	public void endVisit(EnumDeclaration node) {
		ownValue = scopeManager.pop();
	  	computeTypeSymbol(node);
	}

	private void computeTypeSymbol(AbstractTypeDeclaration node) {
		IConstructor type = bindingsResolver.resolveType(node.resolveBinding(), true);
		insert(types, ownValue, type);
	}
	
	@Override
	public boolean visit(FieldAccess node) {
		insert(fieldAccess, getParent(), ownValue);
		return true;
	}
	
	@Override
	public boolean visit(FieldDeclaration node) {
		visitFragments(node.fragments());
		return false;
	}
	
	@Override
	public boolean visit(Initializer node) {
		insert(containment, getParent(), ownValue);
		insert(declarations, ownValue, getSourceLocation(node));
		scopeManager.push((ISourceLocation) ownValue);
		return true;
	}
	
	@Override
	public void endVisit(Initializer node) {
		ownValue = scopeManager.pop();
	}
	
	@Override
	public boolean visit(Javadoc node) {
		ASTNode parent = node.getParent();
		if (parent == null) {
			parent = node.getAlternateRoot();
		}
		
		if (parent instanceof FieldDeclaration) {
		    // possibly there are several comma-separated variable declarations
		    // all associated with the same javadoc. 
		    FieldDeclaration decl = (FieldDeclaration) parent;
		    
		    for (Object var : decl.fragments()) {
		        insert(documentation, resolveBinding((VariableDeclaration) var), getSourceLocation(node));
		    }
		}
		else {
		    insert(documentation, resolveBinding(parent), getSourceLocation(node));
		}
		
		return false;
	}
	
	@Override
	public boolean visit(LineComment node) {
		insert(documentation, resolveBinding(node.getAlternateRoot()), getSourceLocation(node));
		return true;
	}
	
	@Override
	public boolean visit(MethodDeclaration node) {
		insert(containment, getParent(), ownValue);
		scopeManager.push((ISourceLocation) ownValue);
		return true;
	}
	
	@Override
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
			insert(messages, values.constructor(DATATYPE_RASCAL_MESSAGE_ERROR_NODE_TYPE2,
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
	
	@Override
	public boolean visit(MethodInvocation node) {
		insert(methodInvocation, getParent(), ownValue);
		//TODO: add to uses as well
		return true;
	}
	
	@Override
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
	    ISourceLocation result = URIUtil.getParentLocation(sourceLoc);
	    if (result.getPath().equals("/")) {
	        return null;
	    }
	    return result;
	}
	
	@Override
	public boolean visit(PackageDeclaration node) {
		IPackageBinding binding = node.resolveBinding();
		
		if (binding != null) {
		  generatePackageDecls(getParent((ISourceLocation) ownValue), (ISourceLocation) ownValue, getParent(loc));
		  insert(containment, ownValue, getParent());
		} 
		else {
			insert(messages, values.constructor(DATATYPE_RASCAL_MESSAGE_ERROR_NODE_TYPE2,
					values.string("Unresolved binding for: " + node),
					values.sourceLocation(loc, 0, 0)));
		}
		
		scopeManager.push((ISourceLocation) ownValue);
		return true;
	}
	
	@Override
	public void endVisit(PackageDeclaration node) {
		ownValue = scopeManager.pop();
	}
	
	@Override
	public boolean visit(QualifiedName node) {
		insert(names, values.string(node.getFullyQualifiedName()), ownValue);

		// TODO: this seems brittle. 
		if (((ISourceLocation) ownValue).getScheme().equals("java+field")) {
			insert(fieldAccess, getParent(), ownValue);
		}
		return true;
	}
	
	@Override
	public boolean visit(SimpleName node) {
		insert(names, values.string(node.getIdentifier()), ownValue);
		insert(names, values.string(node.getFullyQualifiedName()), ownValue);
		
		if (!simpleNameIsConstructorDecl(node)) {
			addTypeDependency(resolveBinding(node.resolveTypeBinding()));
			if (!node.isDeclaration()) {
				addTypeDependency(resolveDeclaringClass(node.resolveBinding()));
			}
		}
		
		return true;
	}
	
	@Override
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

	@Override
	public boolean visit(SingleVariableDeclaration node) {
		insert(containment, getParent(), ownValue);
		scopeManager.push((ISourceLocation) ownValue);
		return true;
	}
	
	@Override
	public void endVisit(SingleVariableDeclaration node) {
		ownValue = scopeManager.pop();
	    IConstructor type = bindingsResolver.resolveType(node.getType().resolveBinding(), false);
        insert(types, ownValue, type);
	}
	
	@Override
	public boolean visit(SuperConstructorInvocation node) {
		insert(methodInvocation, getParent(), ownValue);
		return true;
	}
	
	@Override
	public boolean visit(SuperFieldAccess node) {
		insert(fieldAccess, getParent(), ownValue);
		return true;
	}
	
	@Override
	public boolean visit(SuperMethodInvocation node) {
		insert(methodInvocation, getParent(), ownValue);
		return true;
	}
	
	@Override
	public boolean visit(TypeDeclaration node) {
		insert(containment, getParent(), ownValue);
		
		scopeManager.push((ISourceLocation) ownValue);
		
		IListWriter extendsClass = values.listWriter();
		IListWriter implementsInterfaces = values.listWriter();
		
		if (node.getAST().apiLevel() == AST.JLS2) {
			if (node.getSuperclass() != null) {
				extendsClass.append(resolveBinding(node.getSuperclass()));
			}
			if (!node.superInterfaces().isEmpty()) {
				for (Iterator it = node.superInterfaces().iterator(); it.hasNext();) {
					Name n = (Name) it.next();
					implementsInterfaces.append(resolveBinding(n));
				}
			}
		} 
		else if (node.getAST().apiLevel() >= AST.JLS3) {
			if (node.getSuperclassType() != null) {
				extendsClass.append(resolveBinding(node.getSuperclassType()));
			}
			if (!node.superInterfaceTypes().isEmpty()) {
				for (Iterator it = node.superInterfaceTypes().iterator(); it.hasNext();) {
					Type t = (Type) it.next();
					implementsInterfaces.append(resolveBinding(t));
				}
			}
		}
		
		if (node.isInterface()) {
			insert(extendsRelations, ownValue, implementsInterfaces.done());
		} 
		else {
			insert(extendsRelations, ownValue, extendsClass.done());
			insert(implementsRelations, ownValue, implementsInterfaces.done());
		}
		
		return true;
	}
	
	@Override
	public void endVisit(TypeDeclaration node) {
		ownValue = scopeManager.pop();
		computeTypeSymbol(node);
	}
	
	@Override
	public boolean visit(TypeParameter node) {
		IListWriter extendsList = values.listWriter();
		if (!node.typeBounds().isEmpty()) {
			for (Iterator it = node.typeBounds().iterator(); it.hasNext();) {
				Type t = (Type) it.next();
				extendsList.append(resolveBinding(t));
			}
		}
		
		insert(containment, getParent(), ownValue);
		
		return true;
	}
	
	@Override
	public boolean visit(VariableDeclarationExpression node) {
		visitFragments(node.fragments());
		return false;
	}
	
	@Override
	public boolean visit(VariableDeclarationFragment node) {
		insert(containment, getParent(), ownValue);
		scopeManager.push((ISourceLocation) ownValue);
		IVariableBinding binding = node.resolveBinding();
		
		if (binding != null) {
		  IConstructor type = bindingsResolver.resolveType(binding.getType(), false);
		  insert(types, ownValue, type);
		}
		else {
			insert(messages, values.constructor(DATATYPE_RASCAL_MESSAGE_ERROR_NODE_TYPE2,
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
		else if (parentASTNode instanceof LambdaExpression) {
			// skip, there is nothing to extract in terms of modifiers from lambda's
		}
		else {
			VariableDeclarationStatement parent = (VariableDeclarationStatement)parentASTNode;
			parent.getType().accept(this);
			visitListOfModifiers(parent.modifiers());
		}
		scopeManager.pop();
		return true;
	}

	@Override
	public boolean visit(VariableDeclarationStatement node) {
		visitFragments(node.fragments());
		return false;
	}
}
