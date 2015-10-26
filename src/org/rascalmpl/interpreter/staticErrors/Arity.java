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
import org.rascalmpl.value.type.Type;

public class Arity extends StaticError {
	private static final long serialVersionUID = -8995239033315812561L;
	
	public Arity(Type off, int arity, AbstractAST ast) {
		super("Arity " + off + " unequal to " + arity, ast);
	}
	
	public Arity(int expected, int got, AbstractAST ast) {
		super("Expected arity : " + expected + ", unequal to " + got, ast);
	}
}
