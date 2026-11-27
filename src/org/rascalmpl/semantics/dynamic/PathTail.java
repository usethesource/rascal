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

import java.net.URI;
import java.net.URISyntaxException;

import org.rascalmpl.ast.Expression;
import org.rascalmpl.ast.MidPathChars;
import org.rascalmpl.ast.PostPathChars;
import org.rascalmpl.exceptions.RuntimeExceptionFactory;
import org.rascalmpl.interpreter.IEvaluator;
import org.rascalmpl.interpreter.result.Result;
import org.rascalmpl.interpreter.result.ResultFactory;
import org.rascalmpl.uri.URIUtil;

import io.usethesource.vallang.IConstructor;
import io.usethesource.vallang.ISourceLocation;
import io.usethesource.vallang.IString;
import io.usethesource.vallang.IValue;

public abstract class PathTail extends org.rascalmpl.ast.PathTail {

	static public class Mid extends org.rascalmpl.ast.PathTail.Mid {

		public Mid(ISourceLocation __param1, IConstructor tree, MidPathChars __param2, Expression __param3,
				org.rascalmpl.ast.PathTail __param4) {
			super(__param1, tree, __param2, __param3, __param4);
		}

		@Override
		public Result<IValue> interpret(IEvaluator<Result<IValue>> __eval) {
			Result<IValue> mid = this.getMid().interpret(__eval);
			Result<IValue> expr = this.getExpression().interpret(__eval);

			// The semantics of .add is used here to coerce different kinds of values
			// to path strings (e.g. parse trees, string constants, type names)
			IString path = (IString) ResultFactory.makeResult(TF.stringType(), VF.string(""), __eval)
				.add(expr).getValue();
			
			try {
				// reuse our URI encoders here on the unencoded expression part 
				URI tmp = URIUtil.create("x", "", "/" + path.getValue());
				// but get the path out directly anyway, unencoded!
				path = VF.string(tmp.getRawPath());

				// connect the pre, middle and end pieces
				Result<IValue> tail = this.getTail().interpret(__eval);
				return mid.add(ResultFactory.makeResult(TF.stringType(), path, __eval)).add(tail);
			}
			catch (URISyntaxException e) {
				throw RuntimeExceptionFactory.malformedURI(path.getValue(), getExpression(), __eval.getStackTrace());
			}
		}

	}

	static public class Post extends org.rascalmpl.ast.PathTail.Post {

		public Post(ISourceLocation __param1, IConstructor tree, PostPathChars __param2) {
			super(__param1, tree, __param2);
		}

		@Override
		public Result<IValue> interpret(IEvaluator<Result<IValue>> __eval) {
			return this.getPost().interpret(__eval);
		}

	}

	public PathTail(ISourceLocation __param1, IConstructor tree) {
		super(__param1, tree);
	}
}
