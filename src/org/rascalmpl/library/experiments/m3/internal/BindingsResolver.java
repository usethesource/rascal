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

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.AnnotationTypeDeclaration;
import org.eclipse.jdt.core.dom.AnnotationTypeMemberDeclaration;
import org.eclipse.jdt.core.dom.AnonymousClassDeclaration;
import org.eclipse.jdt.core.dom.EnumConstantDeclaration;
import org.eclipse.jdt.core.dom.EnumDeclaration;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.IBinding;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.IPackageBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.IVariableBinding;
import org.eclipse.jdt.core.dom.ImportDeclaration;
import org.eclipse.jdt.core.dom.MemberRef;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodRef;
import org.eclipse.jdt.core.dom.PackageDeclaration;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.TypeParameter;
import org.eclipse.jdt.core.dom.VariableDeclaration;

public class BindingsResolver {
	
	private String project;
	private final boolean collectBindings;
	
	BindingsResolver(boolean collectBindings) {
		this.collectBindings = collectBindings;
	}
	
	public void setProject(String project) {
		this.project = project;
	}
	
	public URI resolveBinding(ASTNode node) {
		if (collectBindings) {
			if(node instanceof TypeDeclaration) 
				return resolveBinding(((TypeDeclaration) node).resolveBinding());
			else if(node instanceof EnumDeclaration) 
				return resolveBinding(((EnumDeclaration) node).resolveBinding());
			else if(node instanceof AnnotationTypeDeclaration) 
				return resolveBinding(((AnnotationTypeDeclaration) node).resolveBinding());
			else if(node instanceof AnnotationTypeMemberDeclaration) 
				return resolveBinding(((AnnotationTypeMemberDeclaration) node).resolveBinding());
			else if(node instanceof AnonymousClassDeclaration) 
				return resolveBinding(((AnonymousClassDeclaration) node).resolveBinding());
			else if(node instanceof EnumConstantDeclaration) 
				return resolveBinding(((EnumConstantDeclaration) node).resolveConstructorBinding());
			else if(node instanceof Expression) 
				return resolveBinding(((Expression) node).resolveTypeBinding());
			else if(node instanceof ImportDeclaration) 
				return resolveBinding(((ImportDeclaration) node).resolveBinding());
			else if(node instanceof MemberRef) 
				return resolveBinding(((MemberRef) node).resolveBinding());
			else if(node instanceof MethodDeclaration) 
				return resolveBinding(((MethodDeclaration) node).resolveBinding());
			else if(node instanceof MethodRef) 
				return resolveBinding(((MethodRef) node).resolveBinding());
			else if(node instanceof PackageDeclaration) 
				return resolveBinding(((PackageDeclaration) node).resolveBinding());
			else if(node instanceof Type) 
				return resolveBinding(((Type) node).resolveBinding());
			else if(node instanceof TypeParameter) 
				return resolveBinding(((TypeParameter) node).resolveBinding());
			else if(node instanceof VariableDeclaration) 
				return resolveBinding(((VariableDeclaration) node).resolveBinding());
		}
		return convertBinding("unknown", null, null, null);
	}
	
	private URI resolveBinding(IBinding binding) {
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
		//TODO: there are other bindings in JDT
		return convertBinding("unknown", null, null, null);
	}
	
	private URI resolveBinding(IMethodBinding binding) {
		if (binding == null)
			return convertBinding("unresolved", null, null, null);
		String signature = getPath(resolveBinding(binding.getDeclaringClass()));
		if (!signature.isEmpty())
			signature = signature.concat(".");
		String params = "";
		for (ITypeBinding parameterType: binding.getParameterTypes())
			params = params.concat(getPath(resolveBinding(parameterType)));
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
	
	private URI resolveBinding(ITypeBinding binding) {
		if (binding == null)
			return convertBinding("unresolved", null, null, null);
		String scheme = binding.isInterface() ? "java+interface" : "java+class";
		if (binding.isWildcardType())
			return convertBinding("unknown", null, null, null);
		return convertBinding(scheme, binding.getQualifiedName(), null, null);
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
	
	private URI convertBinding(String scheme, String path, String query, String fragment) {
		URI binding = null;
		if (path == null)
			path = "";
		try {
			binding = new URI(scheme, this.project, "/" + path, query, fragment);
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
