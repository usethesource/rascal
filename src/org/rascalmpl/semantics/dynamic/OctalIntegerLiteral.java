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

import java.math.BigInteger;

import org.eclipse.imp.pdb.facts.INode;
import org.eclipse.imp.pdb.facts.IValue;
import org.rascalmpl.interpreter.Evaluator;
import org.rascalmpl.interpreter.result.Result;

public abstract class OctalIntegerLiteral extends
		org.rascalmpl.ast.OctalIntegerLiteral {

	static public class Lexical extends
			org.rascalmpl.ast.OctalIntegerLiteral.Lexical {

		public Lexical(INode __param1, String __param2) {
			super(__param1, __param2);
		}

		@Override
		public Result<IValue> interpret(Evaluator __eval) {
			return org.rascalmpl.interpreter.result.ResultFactory
					.makeResult(org.rascalmpl.interpreter.Evaluator.__getTf()
							.integerType(), __eval.__getVf().integer(
							new BigInteger(this.getString(), 8).toString()),
							__eval);
		}

	}

	public OctalIntegerLiteral(INode __param1) {
		super(__param1);
	}
}
