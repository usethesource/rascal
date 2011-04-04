/*******************************************************************************
 * Copyright (c) 2009-2011 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Anya Helene Bagge - A.H.S.Bagge@cwi.nl (Univ. Bergen)
*******************************************************************************/
package org.rascalmpl.interpreter.staticErrors;

import org.eclipse.imp.pdb.facts.ISourceLocation;

public class UndeclaredNonTerminalError extends StaticError {
	private static final long serialVersionUID = 4249563329694796160L;

	public UndeclaredNonTerminalError(String name, ISourceLocation loc) {
		super("Undeclared non-terminal: " + name, loc);
	}

	public UndeclaredNonTerminalError(String name, ISourceLocation loc, Throwable cause) {
		super("Undeclared non-terminal: " + name, loc, cause);
	}
}
