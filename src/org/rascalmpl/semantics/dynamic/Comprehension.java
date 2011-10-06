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

import java.util.ArrayList;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IValue;
import org.rascalmpl.ast.Expression;
import org.rascalmpl.interpreter.Evaluator;
import org.rascalmpl.interpreter.control_exceptions.InterruptException;
import org.rascalmpl.interpreter.env.Environment;
import org.rascalmpl.interpreter.matching.IBooleanResult;
import org.rascalmpl.interpreter.result.Result;

public abstract class Comprehension extends org.rascalmpl.ast.Comprehension {

	static public class List extends org.rascalmpl.ast.Comprehension.List {

		public List(IConstructor __param1, java.util.List<Expression> __param2,
				java.util.List<Expression> __param3) {
			super(__param1, __param2, __param3);
		}

		@Override
		public Result<IValue> interpret(Evaluator __eval) {
			return evalComprehension(__eval, this.getGenerators(),
					new ListComprehensionWriter(this.getResults(), __eval));
		}

	}

	static public class Map extends org.rascalmpl.ast.Comprehension.Map {

		public Map(IConstructor __param1, Expression __param2, Expression __param3,
				java.util.List<Expression> __param4) {
			super(__param1, __param2, __param3, __param4);
		}

		@Override
		public Result<IValue> interpret(Evaluator __eval) {
			java.util.List<Expression> resultExprs = new ArrayList<Expression>();
			resultExprs.add(this.getFrom());
			resultExprs.add(this.getTo());
			return evalComprehension(__eval, this.getGenerators(),
					new MapComprehensionWriter(resultExprs, __eval));

		}
	}

	static public class Set extends org.rascalmpl.ast.Comprehension.Set {
		public Set(IConstructor __param1, java.util.List<Expression> __param2,
				java.util.List<Expression> __param3) {
			super(__param1, __param2, __param3);
		}

		@Override
		public Result<IValue> interpret(Evaluator __eval) {
			return evalComprehension(__eval, this.getGenerators(),
					new SetComprehensionWriter(this.getResults(), __eval));
		}
	}

	public static Result<IValue> evalComprehension(Evaluator eval,
			java.util.List<Expression> generators, ComprehensionWriter w) {
		int size = generators.size();
		IBooleanResult[] gens = new IBooleanResult[size];
		Environment[] olds = new Environment[size];
		Environment old = eval.getCurrentEnvt();
		int i = 0;

		try {
			gens[0] = generators.get(0).getBacktracker(eval);
			gens[0].init();
			olds[0] = eval.getCurrentEnvt();
			eval.pushEnv();

			while (i >= 0 && i < size) {
				if (eval.__getInterrupt())
					throw new InterruptException(eval.getStackTrace());
				if (gens[i].hasNext() && gens[i].next()) {
					if (i == size - 1) {
						w.append();
						eval.unwind(olds[i]);
						eval.pushEnv();
					} else {
						i++;
						gens[i] = generators.get(i).getBacktracker(eval);
						gens[i].init();
						olds[i] = eval.getCurrentEnvt();
						eval.pushEnv();
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

	public Comprehension(IConstructor __param1) {
		super(__param1);
	}
}
