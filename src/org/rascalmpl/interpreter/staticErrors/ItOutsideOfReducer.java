/*******************************************************************************
 * Copyright (c) 2009-2011 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI
 *   * Tijs van der Storm - Tijs.van.der.Storm@cwi.nl
*******************************************************************************/
package org.rascalmpl.interpreter.staticErrors;

import org.rascalmpl.ast.AbstractAST;

public class ItOutsideOfReducer extends StaticError {
	private static final long serialVersionUID = -6837835628108765920L;
	
	public ItOutsideOfReducer(AbstractAST ast) {
		super("Use of the 'it' special variable is only allowed within reducers and closures.", ast);
	}

	
}
