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

import io.usethesource.vallang.ISourceLocation;

public class SyntaxError extends StaticError {
	private static final long serialVersionUID = 333331541118811177L;
	 
	public SyntaxError(String inWhat, ISourceLocation loc) {
		super("Parse error in " + inWhat, loc);
	}
}
