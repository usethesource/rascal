/*******************************************************************************
 * Copyright (c) 2009-2011 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI
*******************************************************************************/
package org.rascalmpl.interpreter.staticErrors;

import org.rascalmpl.ast.Declaration.Alias;

public class CyclicAliasError extends StaticError {
	private static final long serialVersionUID = 4212805540522552180L;
	private final Alias first;
	private final Alias last;

	public CyclicAliasError(String name, Alias first, Alias last) {
		super("Cyclic alias declaration for " + name, last);
		this.first = first;
		this.last = last;
	}
	
	public Alias getFirstDeclaration() {
		return first;
	}
	
	public Alias getLastDeclaration() {
		return last;
	}

}
