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

public class UnsupportedSubscript extends StaticError {
	private static final long serialVersionUID = -315365847166484727L;

	public UnsupportedSubscript(Type receiver, Type subscript, AbstractAST ast) {
		super("Unsupported subscript of type " + subscript + " on type " + receiver, ast);
	}

}
