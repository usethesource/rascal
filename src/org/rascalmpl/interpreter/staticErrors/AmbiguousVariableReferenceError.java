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

public class AmbiguousVariableReferenceError extends StaticError {
	private static final long serialVersionUID = -6398515695309309263L;

	public AmbiguousVariableReferenceError(String name, AbstractAST ast) {
		super("Ambiguous unqualified variable name " + name, ast);
	}
}
