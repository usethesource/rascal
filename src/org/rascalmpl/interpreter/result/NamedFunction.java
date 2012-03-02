/*******************************************************************************
 * Copyright (c) 2009-2011 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI
 *   * Mark Hills - Mark.Hills@cwi.nl (CWI)
*******************************************************************************/
package org.rascalmpl.interpreter.result;

import org.rascalmpl.ast.AbstractAST;
import org.rascalmpl.interpreter.Evaluator;
import org.rascalmpl.interpreter.env.Environment;
import org.rascalmpl.interpreter.types.FunctionType;

abstract public class NamedFunction extends AbstractFunction {
	protected final String name;
	
	public NamedFunction(AbstractAST ast, Evaluator eval, FunctionType functionType, String name,
			boolean varargs, Environment env) {
		super(ast, eval, functionType, varargs, env);
		this.name = name;
	}

	@Override
	public String getName() {
		return name;
	}

}
