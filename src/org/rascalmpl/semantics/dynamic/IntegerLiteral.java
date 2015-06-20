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
import org.rascalmpl.ast.DecimalIntegerLiteral.Lexical;
import org.rascalmpl.interpreter.IEvaluator;
import org.rascalmpl.interpreter.result.Result;

public abstract class IntegerLiteral extends org.rascalmpl.ast.IntegerLiteral {

	static public class DecimalIntegerLiteral extends
			org.rascalmpl.ast.IntegerLiteral.DecimalIntegerLiteral {

		public DecimalIntegerLiteral(ISourceLocation __param1, IConstructor tree,
				org.rascalmpl.ast.DecimalIntegerLiteral __param2) {
			super(__param1, tree, __param2);
		}

		@Override
		public Result<IValue> interpret(IEvaluator<Result<IValue>> __eval) {
			String str = ((Lexical) this.getDecimal()).getString();
			return org.rascalmpl.interpreter.result.ResultFactory
					.makeResult(org.rascalmpl.interpreter.Evaluator.__getTf()
							.integerType(), __eval.__getVf().integer(str),
							__eval);
		}

	}

	static public class HexIntegerLiteral extends
			org.rascalmpl.ast.IntegerLiteral.HexIntegerLiteral {

		public HexIntegerLiteral(ISourceLocation __param1, IConstructor tree,
				org.rascalmpl.ast.HexIntegerLiteral __param2) {
			super(__param1, tree, __param2);
		}

		@Override
		public Result<IValue> interpret(IEvaluator<Result<IValue>> __eval) {
			return this.getHex().interpret(__eval);
		}

	}

	static public class OctalIntegerLiteral extends
			org.rascalmpl.ast.IntegerLiteral.OctalIntegerLiteral {

		public OctalIntegerLiteral(ISourceLocation __param1, IConstructor tree,
				org.rascalmpl.ast.OctalIntegerLiteral __param2) {
			super(__param1, tree, __param2);
		}

		@Override
		public Result<IValue> interpret(IEvaluator<Result<IValue>> __eval) {
			return this.getOctal().interpret(__eval);
		}

	}

	public IntegerLiteral(ISourceLocation __param1, IConstructor tree) {
		super(__param1, tree);
	}
}
