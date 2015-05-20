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

import java.util.List;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.ISourceLocation;
import org.eclipse.imp.pdb.facts.IValue;
import org.rascalmpl.ast.Statement;
import org.rascalmpl.interpreter.IEvaluator;
import org.rascalmpl.interpreter.result.Result;

public abstract class FunctionBody extends org.rascalmpl.ast.FunctionBody {

	static public class Default extends org.rascalmpl.ast.FunctionBody.Default {

		public Default(ISourceLocation __param1, IConstructor tree, List<Statement> __param2) {
			super(__param1, tree, __param2);
		}

		@Override
		public Result<IValue> interpret(IEvaluator<Result<IValue>> __eval) {

			Result<IValue> result = org.rascalmpl.interpreter.result.ResultFactory
					.nothing();

			for (Statement statement : this.getStatements()) {
				__eval.setCurrentAST(statement);
				result = statement.interpret(__eval);
			}

			return result;

		}

	}

	public FunctionBody(ISourceLocation __param1, IConstructor tree) {
		super(__param1, tree);
	}

}
