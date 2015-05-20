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
import org.rascalmpl.ast.PathChars;
import org.rascalmpl.ast.PathTail;
import org.rascalmpl.ast.PrePathChars;
import org.rascalmpl.interpreter.IEvaluator;
import org.rascalmpl.interpreter.result.Result;

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
			Result<IValue> tail = this.getTail().interpret(__eval);

			return pre.add(expr).add(tail);
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
