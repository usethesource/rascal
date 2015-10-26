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

import java.math.BigInteger;

import org.rascalmpl.interpreter.IEvaluator;
import org.rascalmpl.interpreter.result.Result;
import org.rascalmpl.value.IConstructor;
import org.rascalmpl.value.ISourceLocation;
import org.rascalmpl.value.IValue;

public abstract class HexIntegerLiteral extends
		org.rascalmpl.ast.HexIntegerLiteral {

	static public class Lexical extends
			org.rascalmpl.ast.HexIntegerLiteral.Lexical {

		public Lexical(ISourceLocation __param1, IConstructor tree, String __param2) {
			super(__param1, tree, __param2);
		}

		@Override
		public Result<IValue> interpret(IEvaluator<Result<IValue>> __eval) {

			String chars = this.getString();
			String hex = chars.substring(2, chars.length());
			return org.rascalmpl.interpreter.result.ResultFactory
					.makeResult(org.rascalmpl.interpreter.Evaluator.__getTf()
							.integerType(), __eval.__getVf().integer(
							new BigInteger(hex, 16).toString()), __eval);

		}

	}

	public HexIntegerLiteral(ISourceLocation __param1, IConstructor tree) {
		super(__param1, tree);
	}
}
