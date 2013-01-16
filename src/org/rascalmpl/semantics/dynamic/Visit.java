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

import java.util.List;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.type.Type;
import org.rascalmpl.ast.Case;
import org.rascalmpl.ast.Expression;
import org.rascalmpl.ast.Strategy;
import org.rascalmpl.interpreter.IEvaluator;
import org.rascalmpl.interpreter.TraversalEvaluator;
import org.rascalmpl.interpreter.TraversalEvaluator.CaseBlockList;
import org.rascalmpl.interpreter.TraversalEvaluator.DIRECTION;
import org.rascalmpl.interpreter.TraversalEvaluator.FIXEDPOINT;
import org.rascalmpl.interpreter.TraversalEvaluator.PROGRESS;
import org.rascalmpl.interpreter.asserts.ImplementationError;
import org.rascalmpl.interpreter.result.Result;
import org.rascalmpl.interpreter.utils.Cases;

public abstract class Visit extends org.rascalmpl.ast.Visit {

	static public class DefaultStrategy extends
			org.rascalmpl.ast.Visit.DefaultStrategy {
		private final CaseBlockList blocks;

		public DefaultStrategy(IConstructor __param1, Expression __param2,
				List<Case> __param3) {
			super(__param1, __param2, __param3);
			blocks = new CaseBlockList(Cases.precompute(getCases()));
		}

		@Override
		public Result<IValue> interpret(IEvaluator<Result<IValue>> __eval) {
			Result<IValue> subject = this.getSubject().interpret(__eval);
			TraversalEvaluator te = new TraversalEvaluator(__eval);

			IValue val = te.traverse(subject.getValue(),
					blocks, DIRECTION.BottomUp,
					PROGRESS.Continuing, FIXEDPOINT.No);
			Type t = val.getType();
			return org.rascalmpl.interpreter.result.ResultFactory.makeResult(t,
					val, __eval);

		}

	}

	static public class GivenStrategy extends
			org.rascalmpl.ast.Visit.GivenStrategy {
		private final CaseBlockList blocks;

		public GivenStrategy(IConstructor __param1, Strategy __param2,
				Expression __param3, List<Case> __param4) {
			super(__param1, __param2, __param3, __param4);
			blocks = new CaseBlockList(Cases.precompute(getCases()));
		}

		@Override
		public Result<IValue> interpret(IEvaluator<Result<IValue>> __eval) {

			Result<IValue> subject = this.getSubject().interpret(__eval);

			// TODO: warning switched to static type here, but not sure if
			// that's correct...
			Type subjectType = subject.getType();

			if (subjectType.isConstructorType()) {
				subjectType = subjectType.getAbstractDataType();
			}

			Strategy s = this.getStrategy();

			DIRECTION direction = DIRECTION.BottomUp;
			PROGRESS progress = PROGRESS.Continuing;
			FIXEDPOINT fixedpoint = FIXEDPOINT.No;

			if (s.isBottomUp()) {
				direction = DIRECTION.BottomUp;
			} else if (s.isBottomUpBreak()) {
				direction = DIRECTION.BottomUp;
				progress = PROGRESS.Breaking;
			} else if (s.isInnermost()) {
				direction = DIRECTION.BottomUp;
				fixedpoint = FIXEDPOINT.Yes;
			} else if (s.isTopDown()) {
				direction = DIRECTION.TopDown;
			} else if (s.isTopDownBreak()) {
				direction = DIRECTION.TopDown;
				progress = PROGRESS.Breaking;
			} else if (s.isOutermost()) {
				direction = DIRECTION.TopDown;
				fixedpoint = FIXEDPOINT.Yes;
			} else {
				throw new ImplementationError("Unknown strategy " + s);
			}

			TraversalEvaluator te = new TraversalEvaluator(__eval);
			IValue val = te
					.traverse(subject.getValue(), blocks,direction, progress, fixedpoint);
			Type t = val.getType();
			return org.rascalmpl.interpreter.result.ResultFactory.makeResult(t,
					val, __eval);

		}

	}

	public Visit(IConstructor __param1) {
		super(__param1);
	}
}
