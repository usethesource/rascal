/*******************************************************************************
 * Copyright (c) 2009-2013 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI
 *   * Paul Klint - Paul.Klint@cwi.nl - CWI
 *   * Arnold Lankamp - Arnold.Lankamp@cwi.nl
*******************************************************************************/
package org.rascalmpl.interpreter.staticErrors;

import org.rascalmpl.ast.AbstractAST;
import org.rascalmpl.value.type.Type;


public class UndeclaredAnnotation extends StaticError {
	private static final long serialVersionUID = -7406667412199993333L;
	
	/**
	 * Note that for constructors this is a static error, but there is also
	 * a dynamic exception related to non-existence of an annotation.
	 */
	public UndeclaredAnnotation(String name, Type on, AbstractAST node) {
		super("Undeclared annotation: " + name + " on " + on, node);
	}
	
}
