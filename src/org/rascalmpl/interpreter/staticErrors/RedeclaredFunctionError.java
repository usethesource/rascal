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

public class RedeclaredFunctionError extends StaticError {
	private static final long serialVersionUID = 8306385560142947662L;

	public RedeclaredFunctionError(String header, String header2,
			AbstractAST ast) {
		super("Redeclared function " + header + " overlaps with " + header2, ast);
	}

}
