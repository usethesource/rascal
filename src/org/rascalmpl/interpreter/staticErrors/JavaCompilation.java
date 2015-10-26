/*******************************************************************************
 * Copyright (c) 2009-2013 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI
*******************************************************************************/
package org.rascalmpl.interpreter.staticErrors;

import org.rascalmpl.ast.AbstractAST;
import org.rascalmpl.value.ISourceLocation;

public class JavaCompilation extends StaticError {
	private static final long serialVersionUID = 3200356264732532487L;

	public JavaCompilation(String message, AbstractAST ast) {
		super("Java compilation failed due to " + message, ast);
	}
	
	public JavaCompilation(String message, ISourceLocation loc) {
		super("Java compilation failed due to " + message, loc);
	}

}
