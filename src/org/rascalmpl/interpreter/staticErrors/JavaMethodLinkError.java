/*******************************************************************************
 * Copyright (c) 2009-2011 CWI
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

public class JavaMethodLinkError extends StaticError {
	private static final long serialVersionUID = 3867556518416718308L;

	public JavaMethodLinkError(String name, String message, AbstractAST ast, Throwable cause) {
		super("Cannot link method " + name + " because: " + message, ast, cause);
	}
}
