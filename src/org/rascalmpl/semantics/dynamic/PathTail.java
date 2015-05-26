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

import java.net.URISyntaxException;
import java.net.URLDecoder;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.ISourceLocation;
import org.eclipse.imp.pdb.facts.IString;
import org.eclipse.imp.pdb.facts.IValue;
import org.rascalmpl.ast.Expression;
import org.rascalmpl.ast.MidPathChars;
import org.rascalmpl.ast.PostPathChars;
import org.rascalmpl.interpreter.IEvaluator;
import org.rascalmpl.interpreter.result.Result;
import org.rascalmpl.interpreter.result.ResultFactory;
import org.rascalmpl.interpreter.utils.RuntimeExceptionFactory;
import org.rascalmpl.uri.URIUtil;

public abstract class PathTail extends org.rascalmpl.ast.PathTail {

	static public class Mid extends org.rascalmpl.ast.PathTail.Mid {

		public Mid(ISourceLocation __param1, IConstructor tree, MidPathChars __param2, Expression __param3,
				org.rascalmpl.ast.PathTail __param4) {
			super(__param1, tree, __param2, __param3, __param4);
		}

		@Override
		public Result<IValue> interpret(IEvaluator<Result<IValue>> __eval) {
			Result<IValue> mid = this.getMid().interpret(__eval);
			String expr = ((IString) this.getExpression().interpret(__eval).getValue()).getValue();
			String workExpr;
			
			try {
				workExpr = expr.startsWith("/") ? expr : ("/" + expr);
				workExpr = URIUtil.create("tmp", "", workExpr).getRawPath();
				workExpr = expr.startsWith("/") ? workExpr : workExpr.substring(1);
				Result<IValue> tail = this.getTail().interpret(__eval);
				Result<IValue> tmp = ResultFactory.makeResult(TF.stringType(), VF.string(workExpr), __eval);
				return mid.add(tmp).add(tail);
			} catch (URISyntaxException e) {
				throw RuntimeExceptionFactory.malformedURI(expr, this, __eval.getStackTrace());
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
