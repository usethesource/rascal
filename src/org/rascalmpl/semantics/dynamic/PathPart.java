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
import org.rascalmpl.ast.PathChars;
import org.rascalmpl.ast.PathTail;
import org.rascalmpl.ast.PrePathChars;
import org.rascalmpl.exceptions.RuntimeExceptionFactory;
import org.rascalmpl.interpreter.IEvaluator;
import org.rascalmpl.interpreter.result.Result;
import org.rascalmpl.interpreter.result.ResultFactory;
import org.rascalmpl.uri.URIUtil;
import io.usethesource.vallang.IConstructor;
import io.usethesource.vallang.ISourceLocation;
import io.usethesource.vallang.IString;
import io.usethesource.vallang.IValue;

public abstract class PathPart extends org.rascalmpl.ast.PathPart {

	static public class Interpolated extends
			org.rascalmpl.ast.PathPart.Interpolated {

		public Interpolated(ISourceLocation __param1, IConstructor tree, PrePathChars __param2,
				Expression __param3, PathTail __param4) {
			super(__param1, tree, __param2, __param3, __param4);
		}

		@Override
		public Result<IValue> interpret(IEvaluator<Result<IValue>> __eval) {

			Result<IValue> pre = this.getPre().interpret(__eval);
			Result<IValue> expr = this.getExpression().interpret(__eval);
			// here we have to encode the string for us in the path part of a uri
			// the trick is to use new File which does the encoding for us, in 
			// a way that conforms to the current OS where we are running. So if someone
			// is splicing a windows path here, with the slashes the other way around,
			// they are first interpreted as path separators and not encoded as %slash
			// However, first we need to map the expression result to string by using
			// the `add` semantics of strings:
			IString path = (IString) ResultFactory.makeResult(TF.stringType(), VF.string(""), __eval)
				.add(expr).getValue();
			
			try {
				// reuse our URI encoders here on the unencoded expression part 
				URI tmp = URIUtil.create("x", "", "/" + path.getValue());
				// but get the path out directly anyway, unencoded!
				path = VF.string(tmp.getRawPath());

				// connect the pre, middle and end pieces
				Result<IValue> tail = this.getTail().interpret(__eval);
				return pre.add(ResultFactory.makeResult(TF.stringType(), path, __eval)).add(tail);
			}
			catch (URISyntaxException e) {
				throw RuntimeExceptionFactory.malformedURI(path.getValue(), getExpression(), __eval.getStackTrace());
			}
		}

	}

	static public class NonInterpolated extends
			org.rascalmpl.ast.PathPart.NonInterpolated {

		public NonInterpolated(ISourceLocation __param1, IConstructor tree, PathChars __param2) {
			super(__param1, tree, __param2);
		}

		@Override
		public Result<IValue> interpret(IEvaluator<Result<IValue>> __eval) {

			return this.getPathChars().interpret(__eval);

		}

	}

	public PathPart(ISourceLocation __param1, IConstructor tree) {
		super(__param1, tree);
	}

}
