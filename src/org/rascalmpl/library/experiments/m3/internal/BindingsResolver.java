/*******************************************************************************
 * Copyright (c) 2009-2013 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   * Anastasia Izmaylova - A.Izmaylova@cwi.nl - CWI
*******************************************************************************/
package org.rascalmpl.library.experiments.m3.internal;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.jdt.core.dom.*;

public class BindingsResolver {
	
	private String project;
	private final boolean collectBindings;
	
	private final Map<URI, Integer> anonymousClassCounter = new HashMap<URI, Integer>();
	private final Map<URI, Integer> initializerCounter = new HashMap<URI, Integer>();
	
	BindingsResolver(boolean collectBindings) {
		this.collectBindings = collectBindings;
	}
	
	public void setProject(String project) {
		this.project = project;
	}
	
	public URI resolveBinding(ASTNode node) {
		if (collectBindings) {
			if (node instanceof TypeDeclaration) 
				return resolveBinding(((TypeDeclaration) node).resolveBinding());
			else if (node instanceof EnumDeclaration) 
				return resolveBinding(((EnumDeclaration) node).resolveBinding());
			else if (node instanceof AnnotationTypeDeclaration) 
				return resolveBinding(((AnnotationTypeDeclaration) node).resolveBinding());
			else if (node instanceof AnnotationTypeMemberDeclaration) 
				return resolveBinding(((AnnotationTypeMemberDeclaration) node).resolveBinding());
			else if (node instanceof AnonymousClassDeclaration) {
				int anonCounter = 1;
				ITypeBinding nodeBinding = ((AnonymousClassDeclaration) node).resolveBinding();
				URI parent = getParent(nodeBinding);
				if (anonymousClassCounter.containsKey(parent))
					anonCounter = anonymousClassCounter.get(parent) + 1;
				anonymousClassCounter.put(parent, anonCounter);
				return resolveBinding(nodeBinding);
			}
			else if (node instanceof EnumConstantDeclaration) 
				return resolveBinding(((EnumConstantDeclaration) node).resolveConstructorBinding());
			else if (node instanceof ClassInstanceCreation)
				return resolveBinding(((ClassInstanceCreation) node).resolveConstructorBinding());
			else if (node instanceof FieldAccess)
				return resolveBinding(((FieldAccess) node).resolveFieldBinding());
			else if (node instanceof MethodInvocation)
				return resolveBinding(((MethodInvocation) node).resolveMethodBinding());
			else if (node instanceof Name)
				return resolveBinding(((Name) node).resolveBinding());
			else if (node instanceof SuperFieldAccess)
				return resolveBinding(((SuperFieldAccess) node).resolveFieldBinding());
			else if (node instanceof SuperMethodInvocation)
				return resolveBinding(((SuperMethodInvocation) node).resolveMethodBinding());
			else if (node instanceof Expression)
				return resolveBinding(((Expression) node).resolveTypeBinding());
			else if (node instanceof ImportDeclaration) 
				return resolveBinding(((ImportDeclaration) node).resolveBinding());
			else if (node instanceof MemberRef) 
				return resolveBinding(((MemberRef) node).resolveBinding());
			else if (node instanceof MethodDeclaration)
				return resolveBinding(((MethodDeclaration) node).resolveBinding());
			else if (node instanceof MethodRef) 
				return resolveBinding(((MethodRef) node).resolveBinding());
			else if (node instanceof PackageDeclaration) 
				return resolveBinding(((PackageDeclaration) node).resolveBinding());
			else if (node instanceof Type) 
				return resolveBinding(((Type) node).resolveBinding());
			else if (node instanceof TypeParameter) 
				return resolveBinding(((TypeParameter) node).resolveBinding());
			else if (node instanceof VariableDeclaration) 
				return resolveBinding(((VariableDeclaration) node).resolveBinding());
			else if (node instanceof ConstructorInvocation)
				return resolveBinding(((ConstructorInvocation) node).resolveConstructorBinding());
			else if (node instanceof SuperConstructorInvocation)
				return resolveBinding(((SuperConstructorInvocation) node).resolveConstructorBinding());
			else if (node instanceof TypeDeclarationStatement)
				return resolveBinding(((TypeDeclarationStatement) node).resolveBinding());
			else if (node instanceof Initializer)
				return resolveInitializer((Initializer) node);
		}
		return convertBinding("unknown", null, null, null);
	}
	
	private URI resolveInitializer(Initializer node) {
		int initCounter = 1;
		URI parent = resolveBinding(node.getParent());
		if (initializerCounter.containsKey(parent))
			initCounter = initializerCounter.get(parent) + 1;
		initializerCounter.put(parent, initCounter);
		
		return convertBinding("java+initializer", parent.getPath() + "$initializer" + initCounter, null, null);
	}
	
	public URI resolveBinding(IBinding binding) {
		if (binding == null)
			return convertBinding("unresolved", null, null, null);
		if (binding instanceof ITypeBinding)
			return resolveBinding((ITypeBinding) binding);
		else if (binding instanceof IMethodBinding)
			return resolveBinding((IMethodBinding) binding);
		else if (binding instanceof IPackageBinding)
			return resolveBinding((IPackageBinding) binding);
		else if (binding instanceof IVariableBinding)
			return resolveBinding((IVariableBinding) binding);
		return convertBinding("unknown", null, null, null);
	}
	
	private URI resolveBinding(IMethodBinding binding) {
		if (binding == null)
			return convertBinding("unresolved", null, null, null);
		String signature = getPath(resolveBinding(binding.getDeclaringClass()));
		if (!signature.isEmpty())
			signature = signature.concat(".");
		String params = "";
		for (ITypeBinding parameterType: binding.getParameterTypes()) {
			if (!params.isEmpty())
				params = params.concat(",");
			params = params.concat(getPath(resolveBinding(parameterType)));
		}
		signature = signature.concat(binding.getName() + "(" + params + ")");
		String scheme = "unknown";
		if (binding.isConstructor())
			scheme = "java+constructor";
		else 
			scheme = "java+method";
		
		return convertBinding(scheme, signature, null, null);
	}
	
	private URI resolveBinding(IPackageBinding binding) {
		if (binding == null)
			return convertBinding("unresolved", null, null, null);
		return convertBinding("java+package", binding.getName(), null, null);
	}
	
	private URI getParent(ITypeBinding binding) {
		URI parent = resolveBinding(binding.getDeclaringMethod());
		if (parent.getPath().isEmpty() || parent.getPath().equals("/"))
			parent = resolveBinding(binding.getDeclaringClass());
		return parent;
	}
	
	private URI resolveBinding(ITypeBinding binding) {
		if (binding == null)
			return convertBinding("unresolved", null, null, null);
		String scheme = binding.isInterface() ? "java+interface" : "java+class";
		String qualifiedName = binding.getQualifiedName();
		
		URI parent = getParent(binding);
		
		if (qualifiedName.isEmpty()) {
			qualifiedName = parent.getPath();
			if (!(binding.getName().isEmpty()))
			qualifiedName += "." + binding.getName();
		}
		
		if (binding.isWildcardType())
			return convertBinding("unknown", null, null, null);
		
		if (binding.isAnonymous()) {
			String anonCounter = "";
			if (anonymousClassCounter.containsKey(parent))
				anonCounter = anonymousClassCounter.get(parent).toString();
			qualifiedName += "$anonymous" + anonCounter;
			scheme = "java+anonymousClass";
		}
		
		return convertBinding(scheme, qualifiedName, null, null);
	}
	
	private URI resolveBinding(IVariableBinding binding) {
		if (binding == null)
			return convertBinding("unresolved", null, null, null);
		String qualifiedName = "";

		ITypeBinding declaringClass = binding.getDeclaringClass();
		if (declaringClass != null) {
			qualifiedName = getPath(resolveBinding(declaringClass));
		} else {
			IMethodBinding declaringMethod = binding.getDeclaringMethod();
			if (declaringMethod != null) {
				qualifiedName = getPath(resolveBinding(declaringMethod));
			}
		}
		
		if (!qualifiedName.isEmpty())
			qualifiedName = qualifiedName.concat(".");
		
		String scheme = "java+variable";
		if (binding.isField())
			scheme = "java+field";
		else if (binding.isParameter())
			scheme = "java+parameter";
		else if (binding.isEnumConstant())
			scheme = "java+enumconstant";
		
		return convertBinding(scheme, qualifiedName.concat(binding.getName()), null, null);
	}
	
	public URI convertBinding(String scheme, String path, String query, String fragment) {
		URI binding = null;
		if (path == null)
			path = "";
		
		try {
			binding = new URI(scheme, this.project, !(path.startsWith("/")) ? "/" + path : path, query, fragment);
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		
		return binding;
	}
	
	private String getPath(URI uri) {
		String path = uri.getPath();
		return path.substring(1, path.length());
	}
}
