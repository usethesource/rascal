/*******************************************************************************
 * Copyright (c) 2009-2013 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Anastasia Izmaylova - A.Izmaylova@cwi.nl - CWI
*******************************************************************************/
package org.rascalmpl.interpreter.staticErrors;

import org.rascalmpl.ast.AbstractAST;
import org.rascalmpl.value.ISourceLocation;

public class UninitializedPatternMatch extends StaticError {
	private static final long serialVersionUID = -1128833651256940542L;

	public UninitializedPatternMatch(String message, AbstractAST ast) {
		super(message, ast);
	}
	
	public UninitializedPatternMatch(String message, ISourceLocation loc) {
		super(message, loc);
	}
}
