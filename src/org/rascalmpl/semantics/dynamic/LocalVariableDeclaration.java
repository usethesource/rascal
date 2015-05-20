/*******************************************************************************
 * Copyright (c) 2009-2013 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI
 *   * Mark Hills - Mark.Hills@cwi.nl (CWI)
 *   * Arnold Lankamp - Arnold.Lankamp@cwi.nl
*******************************************************************************/
package org.rascalmpl.semantics.dynamic;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.ISourceLocation;
import org.eclipse.imp.pdb.facts.IValue;
import org.rascalmpl.ast.Declarator;
import org.rascalmpl.interpreter.IEvaluator;
import org.rascalmpl.interpreter.result.Result;

public abstract class LocalVariableDeclaration extends
		org.rascalmpl.ast.LocalVariableDeclaration {

	static public class Default extends
			org.rascalmpl.ast.LocalVariableDeclaration.Default {

		public Default(ISourceLocation __param1, IConstructor tree, Declarator __param2) {
			super(__param1, tree, __param2);
		}

		@Override
		public Result<IValue> interpret(IEvaluator<Result<IValue>> __eval) {
			// TODO deal with dynamic variables
			return this.getDeclarator().interpret(__eval);
		}

	}

	public LocalVariableDeclaration(ISourceLocation __param1, IConstructor tree) {
		super(__param1, tree);
	}

}
