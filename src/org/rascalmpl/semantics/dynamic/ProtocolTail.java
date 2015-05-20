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
*******************************************************************************/
package org.rascalmpl.semantics.dynamic;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.ISourceLocation;
import org.eclipse.imp.pdb.facts.IValue;
import org.rascalmpl.ast.Expression;
import org.rascalmpl.ast.MidProtocolChars;
import org.rascalmpl.ast.PostProtocolChars;
import org.rascalmpl.interpreter.IEvaluator;
import org.rascalmpl.interpreter.result.Result;

public abstract class ProtocolTail extends org.rascalmpl.ast.ProtocolTail {

	static public class Mid extends org.rascalmpl.ast.ProtocolTail.Mid {

		public Mid(ISourceLocation __param1, IConstructor tree, MidProtocolChars __param2,
				Expression __param3, org.rascalmpl.ast.ProtocolTail __param4) {
			super(__param1, tree, __param2, __param3, __param4);
		}

		@Override
		public Result<IValue> interpret(IEvaluator<Result<IValue>> __eval) {
			Result<IValue> pre = this.getMid().interpret(__eval);
			Result<IValue> expr = this.getExpression().interpret(__eval);
			Result<IValue> tail = this.getTail().interpret(__eval);

			return pre.add(expr).add(tail);
		}

	}

	static public class Post extends org.rascalmpl.ast.ProtocolTail.Post {

		public Post(ISourceLocation __param1, IConstructor tree, PostProtocolChars __param2) {
			super(__param1, tree, __param2);
		}

		@Override
		public Result<IValue> interpret(IEvaluator<Result<IValue>> __eval) {
			return this.getPost().interpret(__eval);
		}

	}

	public ProtocolTail(ISourceLocation __param1, IConstructor tree) {
		super(__param1, tree);
	}

}
