/*******************************************************************************
 * Copyright (c) 2009-2013 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI
 *   * Anya Helene Bagge - anya@ii.uib.no (Univ. Bergen)
 *   * Paul Klint - Paul.Klint@cwi.nl - CWI
 *   * Arnold Lankamp - Arnold.Lankamp@cwi.nl
*******************************************************************************/
package org.rascalmpl.interpreter.staticErrors;

import org.rascalmpl.ast.AbstractAST;
import io.usethesource.vallang.ISourceLocation;

/**
 * A static error represents all errors that are detected by the interpreter
 * in 'static type check' mode. These errors can NOT be caught in Rascal code.
 * In the future, they may be thrown by an actual type checker.
 */
public abstract class StaticError extends RuntimeException {
	private static final long serialVersionUID = 2730379050952564623L;
	private ISourceLocation loc;
	
	public StaticError(String message, ISourceLocation loc) {
		super(message);
		if(loc != null)
			addStackTraceElement(loc);
		this.loc = loc;
	}
	
	public StaticError(String message, ISourceLocation loc, Throwable cause) {
		super(message, cause);
		if(loc != null)
			addStackTraceElement(loc);
		this.loc = loc;
	}

	public StaticError(String message, AbstractAST ast) {
		this(message, ast != null ? ast.getLocation() : null);
	}
	
	public StaticError(String message, AbstractAST ast, Throwable cause) {
		this(message, ast != null ? ast.getLocation() : null, cause);
	}
	
	public ISourceLocation getLocation() {
		return loc;
	}
	
	public void setLocation(ISourceLocation loc) {
		String mod = loc.getPath().replaceAll("^.*/", "").replaceAll("\\..*$", "");
		getStackTrace()[0] = new StackTraceElement(mod, "?", loc.getPath(), loc.getBeginLine());

		this.loc = loc;
	}

	private void addStackTraceElement(ISourceLocation loc) {
		StackTraceElement[] oldStackTrace = getStackTrace();
		StackTraceElement[] stackTrace = new StackTraceElement[oldStackTrace.length+1];
		int i = 0;
		
		String mod;
		if(loc.getScheme().equals("rascal"))
			mod = loc.getAuthority();
		else
			mod = loc.getPath().replaceAll("^.*/", "").replaceAll("\\..*$", "");
		if(mod == null)
			mod = "<empty>";
		
		if (loc.hasLineColumn()) {
			stackTrace[i++] = new StackTraceElement(mod, "?", loc.getPath(), loc.getBeginLine());
		}
		else {
			stackTrace[i++] = new StackTraceElement(mod, "?", loc.getPath(), 1);
		}

		for (StackTraceElement elt : oldStackTrace) {
			stackTrace[i++] = elt;
		}
		setStackTrace(stackTrace);
	}
	
	public String getAdvice(){
		String prefix = "https://www.rascal-mpl.org/docs/Rascal/Errors/CompileTimeErrors/";
		String cn = getClass().getSimpleName();
		return "Advice: |" + prefix + cn + "|";
	}
	
	@Override
	public String getMessage(){
		return super.getMessage() + "\n" + getAdvice();
	}
}
