/*******************************************************************************
 * Copyright (c) 2009-2011 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   * Anastasia Izmaylova - A.Izmaylova@cwi.nl - CWI
*******************************************************************************/
package org.rascalmpl.library.lang.java.m3.internal;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.AnnotationTypeDeclaration;
import org.eclipse.jdt.core.dom.AnnotationTypeMemberDeclaration;
import org.eclipse.jdt.core.dom.AnonymousClassDeclaration;
import org.eclipse.jdt.core.dom.ClassInstanceCreation;
import org.eclipse.jdt.core.dom.ConstructorInvocation;
import org.eclipse.jdt.core.dom.EnumConstantDeclaration;
import org.eclipse.jdt.core.dom.EnumDeclaration;
import org.eclipse.jdt.core.dom.FieldAccess;
import org.eclipse.jdt.core.dom.ImportDeclaration;
import org.eclipse.jdt.core.dom.MemberRef;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.MethodRef;
import org.eclipse.jdt.core.dom.ModuleDeclaration;
import org.eclipse.jdt.core.dom.Name;
import org.eclipse.jdt.core.dom.PackageDeclaration;
import org.eclipse.jdt.core.dom.SuperConstructorInvocation;
import org.eclipse.jdt.core.dom.SuperFieldAccess;
import org.eclipse.jdt.core.dom.SuperMethodInvocation;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.TypeParameter;
import org.eclipse.jdt.core.dom.VariableDeclaration;
import io.usethesource.vallang.ISourceLocation;

public interface IBindingsResolver {
	public ISourceLocation resolveBinding(TypeDeclaration node); // declaration
	public ISourceLocation resolveBinding(EnumDeclaration node); // declaration
	public ISourceLocation resolveBinding(AnnotationTypeDeclaration node); // declaration
	public ISourceLocation resolveBinding(AnnotationTypeMemberDeclaration node); // declaration
	public ISourceLocation resolveBinding(AnonymousClassDeclaration node); // declaration
	public ISourceLocation resolveBinding(ImportDeclaration node); // declaration
	public ISourceLocation resolveBinding(MemberRef node); // Java doc
	public ISourceLocation resolveBinding(MethodDeclaration node);
	public ISourceLocation resolveBinding(MethodRef node); // Java doc
	public ISourceLocation resolveBinding(Name node); // Expression
	public ISourceLocation resolveBinding(PackageDeclaration node); 
	public ISourceLocation resolveBinding(ModuleDeclaration node); 
	public ISourceLocation resolveBinding(Type node); // type
	public ISourceLocation resolveBinding(TypeParameter node); // type
	public ISourceLocation resolveBinding(VariableDeclaration node); // declaration
	public ISourceLocation resolveBinding(ClassInstanceCreation node); // Expression
	public ISourceLocation resolveBinding(ConstructorInvocation node); // Statement
	public ISourceLocation resolveBinding(EnumConstantDeclaration node); // declaration
	public ISourceLocation resolveBinding(SuperConstructorInvocation node); // Statement
	public ISourceLocation resolveBinding(FieldAccess node); // Expression
	public ISourceLocation resolveBinding(SuperFieldAccess node); // Expression
	public ISourceLocation resolveBinding(MethodInvocation node); // Expression
	public ISourceLocation resolveBinding(SuperMethodInvocation node); // Expression
	public ISourceLocation resolveBinding(ASTNode node);
}
