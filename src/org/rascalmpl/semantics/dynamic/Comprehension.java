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

import java.util.ArrayList;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.ISourceLocation;
import org.eclipse.imp.pdb.facts.IValue;
import org.rascalmpl.ast.Expression;
import org.rascalmpl.interpreter.IEvaluator;
import org.rascalmpl.interpreter.control_exceptions.InterruptException;
import org.rascalmpl.interpreter.env.Environment;
import org.rascalmpl.interpreter.matching.IBooleanResult;
import org.rascalmpl.interpreter.result.Result;

public abstract class Comprehension extends org.rascalmpl.ast.Comprehension {

	static public class List extends org.rascalmpl.ast.Comprehension.List {

		public List(ISourceLocation __param1, IConstructor tree, java.util.List<Expression> __param2,
				java.util.List<Expression> __param3) {
			super(__param1, tree, __param2, __param3);
		}

		@Override
		public Result<IValue> interpret(IEvaluator<Result<IValue>> __eval) {
			return evalComprehension(__eval, this.getGenerators(),
					new ListComprehensionWriter(this.getResults(), __eval));
		}

	}

	static public class Map extends org.rascalmpl.ast.Comprehension.Map {

		public Map(ISourceLocation __param1, IConstructor tree, Expression __param2, Expression __param3,
				java.util.List<Expression> __param4) {
			super(__param1, tree, __param2, __param3, __param4);
		}

		@Override
		public Result<IValue> interpret(IEvaluator<Result<IValue>> __eval) {
			java.util.List<Expression> resultExprs = new ArrayList<Expression>();
			resultExprs.add(this.getFrom());
			resultExprs.add(this.getTo());
			return evalComprehension(__eval, this.getGenerators(),
					new MapComprehensionWriter(resultExprs, __eval));

		}
	}

	static public class Set extends org.rascalmpl.ast.Comprehension.Set {
		public Set(ISourceLocation __param1, IConstructor tree, java.util.List<Expression> __param2,
				java.util.List<Expression> __param3) {
			super(__param1, tree, __param2, __param3);
		}

		@Override
		public Result<IValue> interpret(IEvaluator<Result<IValue>> __eval) {
			return evalComprehension(__eval, this.getGenerators(),
					new SetComprehensionWriter(this.getResults(), __eval));
		}
	}

	public static Result<IValue> evalComprehension(IEvaluator<Result<IValue>> eval,
			java.util.List<Expression> generators, ComprehensionWriter w) {
		int size = generators.size();
		IBooleanResult[] gens = new IBooleanResult[size];
		Environment[] olds = new Environment[size];
		Environment old = eval.getCurrentEnvt();
		int i = 0;

		try {
			olds[0] = eval.getCurrentEnvt();
			eval.pushEnv();
			gens[0] = generators.get(0).getBacktracker(eval);
			gens[0].init();

			while (i >= 0 && i < size) {
				if (eval.isInterrupted())
					throw new InterruptException(eval.getStackTrace(), eval.getCurrentAST().getLocation());
				if (gens[i].hasNext() && gens[i].next()) {
					if (i == size - 1) {
						w.append();
					} else {
						i++;
						olds[i] = eval.getCurrentEnvt();
						eval.pushEnv();
						gens[i] = generators.get(i).getBacktracker(eval);
						gens[i].init();
					}
				} else {
					eval.unwind(olds[i]);
					i--;
				}
			}
		} finally {
			eval.unwind(old);
		}
		return w.done();
	}

	public Comprehension(ISourceLocation __param1, IConstructor tree) {
		super(__param1, tree);
	}
}
