/*******************************************************************************
 * Copyright (c) 2009-2011 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI
 *   * Paul Klint - Paul.Klint@cwi.nl - CWI
*******************************************************************************/
package org.rascalmpl.interpreter.staticErrors;

import org.rascalmpl.ast.FunctionDeclaration;

public class UndeclaredJavaMethod extends StaticError {
	private static final long serialVersionUID = -3645474482816345282L;

	public UndeclaredJavaMethod(String name, FunctionDeclaration func) {
		super("No such Java method: " + name, func);
	}
}
