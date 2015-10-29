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
import org.rascalmpl.value.type.Type;

public class UnexpectedKeywordArgumentType extends StaticError {
	private static final long serialVersionUID = -9009407553448884728L;
	
	public UnexpectedKeywordArgumentType(String name, Type expected, Type got, AbstractAST ast) {
		super("Expected " + expected + ", but got " + got + " for keyword parameter " + name, ast);
//		printStackTrace();
	}
	
}
