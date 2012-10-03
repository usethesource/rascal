/*******************************************************************************
 * Copyright (c) 2009-2011 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Anastasia Izmaylova - A.Izmaylova@cwi.nl - CWI
*******************************************************************************/
package org.rascalmpl.interpreter.staticErrors;

import org.eclipse.imp.pdb.facts.ISourceLocation;
import org.rascalmpl.ast.AbstractAST;

public class UninitializedPatternMatchError extends StaticError {
	private static final long serialVersionUID = -1128833651256940542L;

	public UninitializedPatternMatchError(String message, AbstractAST ast) {
		super(message, ast);
	}
	
	public UninitializedPatternMatchError(String message, ISourceLocation loc) {
		super(message, loc);
	}
}
