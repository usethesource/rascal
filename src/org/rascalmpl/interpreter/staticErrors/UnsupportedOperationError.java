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

import org.eclipse.imp.pdb.facts.type.Type;
import org.rascalmpl.ast.AbstractAST;

public class UnsupportedOperationError extends StaticError {
	private static final long serialVersionUID = 4097641812252466488L;
	
	public UnsupportedOperationError(String operation, Type on, AbstractAST ast) {
		super(operation + " not supported on " + on + "at " + ast.getLocation(), ast);
	}
	
	public UnsupportedOperationError(String operation, Type on1, Type on2, AbstractAST ast) {
		super(operation + " not supported on " + on1 + " and " + on2, ast);
	}
	
	public UnsupportedOperationError(String message, AbstractAST ast) {
		super(message, ast);
	}
}
