/*******************************************************************************
 * Copyright (c) 2012-2013 CWI
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

public class UndeclaredModuleProvider extends StaticError {
	private static final long serialVersionUID = 7649069585736694101L;

	public UndeclaredModuleProvider(String scheme, AbstractAST node) {
		super("There was no module provider registered for scheme:" + scheme, node);
	}
}
