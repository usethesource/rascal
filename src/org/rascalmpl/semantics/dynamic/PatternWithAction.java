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

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.type.Type;
import org.rascalmpl.ast.Expression;
import org.rascalmpl.ast.Replacement;
import org.rascalmpl.ast.Statement;
import org.rascalmpl.interpreter.Evaluator;
import org.rascalmpl.interpreter.matching.IMatchingResult;
import org.rascalmpl.interpreter.matching.NodePattern;
import org.rascalmpl.interpreter.result.Result;
import org.rascalmpl.interpreter.staticErrors.UnexpectedTypeError;
import org.rascalmpl.interpreter.types.NonTerminalType;
import org.rascalmpl.values.uptr.Factory;

public abstract class PatternWithAction extends
		org.rascalmpl.ast.PatternWithAction {

	static public class Arbitrary extends
			org.rascalmpl.ast.PatternWithAction.Arbitrary {

		public Arbitrary(IConstructor __param1, Expression __param2, Statement __param3) {
			super(__param1, __param2, __param3);
		}

		@Override
		public Result<IValue> interpret(Evaluator __eval) {

			IMatchingResult pv = this.getPattern().getMatcher(__eval);

			Type pt = pv.getType(__eval.getCurrentEnvt(), null);

			if (pv instanceof NodePattern) {
				pt = ((NodePattern) pv).getConstructorType(__eval
						.getCurrentEnvt());
			}

			// TODO store rules for concrete syntax on production rule and
			// create Lambda's for production rules to speed up matching and
			// rewrite rule look up
			if (pt instanceof NonTerminalType) {
				pt = Factory.Tree_Appl;
			}

			if (!(pt.isAbstractDataType() || pt.isConstructorType() || pt
					.isNodeType())) {
				throw new UnexpectedTypeError(
						org.rascalmpl.interpreter.Evaluator.__getTf()
								.nodeType(), pt, this);
			}

			__eval.__getHeap().storeRule(pt, this,
					__eval.getCurrentModuleEnvironment());
			return org.rascalmpl.interpreter.result.ResultFactory.nothing();

		}

	}

	static public class Replacing extends
			org.rascalmpl.ast.PatternWithAction.Replacing {

		public Replacing(IConstructor __param1, Expression __param2,
				Replacement __param3) {
			super(__param1, __param2, __param3);
		}

		@Override
		public Result<IValue> interpret(Evaluator __eval) {

			IMatchingResult pv = this.getPattern().getMatcher(__eval);
			Type pt = pv.getType(__eval.getCurrentEnvt(), null);

			if (pv instanceof NodePattern) {
				pt = ((NodePattern) pv).getConstructorType(__eval
						.getCurrentEnvt());
			}

			if (pt instanceof NonTerminalType) {
				pt = Factory.Tree_Appl;
			}

			if (!(pt.isAbstractDataType() || pt.isConstructorType() || pt
					.isNodeType())) {
				throw new UnexpectedTypeError(
						org.rascalmpl.interpreter.Evaluator.__getTf()
								.nodeType(), pt, this);
			}

			__eval.__getHeap().storeRule(pt, this,
					__eval.getCurrentModuleEnvironment());
			return org.rascalmpl.interpreter.result.ResultFactory.nothing();

		}

	}

	public PatternWithAction(IConstructor __param1) {
		super(__param1);
	}
}
