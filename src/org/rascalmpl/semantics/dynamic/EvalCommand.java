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
 *   * Arnold Lankamp - Arnold.Lankamp@cwi.nl
*******************************************************************************/
package org.rascalmpl.semantics.dynamic;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IValue;
import org.rascalmpl.interpreter.IEvaluator;
import org.rascalmpl.interpreter.result.Result;

public abstract class EvalCommand extends org.rascalmpl.ast.EvalCommand {

	static public class Declaration extends
			org.rascalmpl.ast.EvalCommand.Declaration {

		public Declaration(IConstructor __param1,
				org.rascalmpl.ast.Declaration __param2) {
			super(__param1, __param2);
		}

		@Override
		public Result<IValue> interpret(IEvaluator<Result<IValue>> __eval) {

			__eval.setCurrentAST(this);
			return this.getDeclaration().interpret(__eval);

		}

	}

	static public class Import extends org.rascalmpl.ast.EvalCommand.Import {

		public Import(IConstructor __param1, org.rascalmpl.ast.Import __param2) {
			super(__param1, __param2);
		}

		@Override
		public Result<IValue> interpret(IEvaluator<Result<IValue>> __eval) {
			__eval.setCurrentAST(this);
			Result<IValue> res = this.getImported().interpret(__eval);

			// If we import a module from the command line, notify any
			// expressions caching
			// results that could be invalidated by a module load that we have
			// loaded.
			__eval.notifyConstructorDeclaredListeners();

			return res;

		}

	}

	static public class Statement extends org.rascalmpl.ast.EvalCommand.Statement {

		public Statement(IConstructor __param1, org.rascalmpl.ast.Statement __param2) {
			super(__param1, __param2);
		}

		@Override
		public Result<IValue> interpret(IEvaluator<Result<IValue>> __eval) {

			__eval.setCurrentAST(this.getStatement());
			return __eval.eval(this.getStatement());

		}

	}

	public EvalCommand(IConstructor __param1) {
		super(__param1);
	}
}
