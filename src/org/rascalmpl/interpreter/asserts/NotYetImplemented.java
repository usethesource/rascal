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
package org.rascalmpl.interpreter.asserts;

import org.rascalmpl.ast.AbstractAST;


public final class NotYetImplemented extends AssertionError {
	private static final long serialVersionUID = -8740312542969306482L;

	public NotYetImplemented(String message) {
		super("Not yet implemented " + message);
	}
	
	public NotYetImplemented(AbstractAST ast) {
		super("Operation not yet implemented on " + ast.getClass() + " at " + ast.getLocation());
	}
}
