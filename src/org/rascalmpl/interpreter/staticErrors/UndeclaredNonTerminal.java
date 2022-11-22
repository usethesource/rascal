/*******************************************************************************
 * Copyright (c) 2009-2013 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI
 *   * Tijs van der Storm - Tijs.van.der.Storm@cwi.nl
 *   * Paul Klint - Paul.Klint@cwi.nl - CWI
*******************************************************************************/
package org.rascalmpl.interpreter.staticErrors;

import org.rascalmpl.ast.AbstractAST;

import io.usethesource.vallang.ISourceLocation;

public class UndeclaredNonTerminal extends StaticError {
	private static final long serialVersionUID = -5617996489458337612L;
	private final String name;

	public UndeclaredNonTerminal(String name, String module, AbstractAST ast) {
		super("Undeclared non-terminal: " + name + " in module " + module, ast);
		this.name = name;
	}

	public UndeclaredNonTerminal(String name, String module, ISourceLocation ast) {
		super("Undeclared non-terminal: " + name + " in module " + module, ast);
		this.name = name;
	}
	
	public String getName() {
		return name;
	}

}
