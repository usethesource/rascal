/*******************************************************************************
 * Copyright (c) 2009-2013 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI
 *   * Arnold Lankamp - Arnold.Lankamp@cwi.nl
*******************************************************************************/
package org.rascalmpl.interpreter.staticErrors;

import org.rascalmpl.ast.AbstractAST;
import org.rascalmpl.value.ISourceLocation;

public class ModuleNameMismatch extends StaticError {
	private static final long serialVersionUID = 6984933453355489423L;

	public ModuleNameMismatch(String name, String file, AbstractAST ast) {
		super("Module name " + name + " does not match " + file, ast);
	}

	public ModuleNameMismatch(String name, String file,
			ISourceLocation sourceLocation) {
		super("Module name " + name + " does not match " + file, sourceLocation);
	}
}
