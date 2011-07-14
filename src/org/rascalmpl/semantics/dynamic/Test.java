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
import org.rascalmpl.ast.Expression;
import org.rascalmpl.ast.Name;
import org.rascalmpl.ast.Tags;
import org.rascalmpl.interpreter.Evaluator;
import org.rascalmpl.interpreter.result.Result;

public abstract class Test extends org.rascalmpl.ast.Test {

//	static public class Labeled extends org.rascalmpl.ast.Test.Labeled {
//
//		
//
//		public Labeled(IConstructor node, Tags tags, Name label,
//				Expression expression) {
//			super(node, tags, label, expression);
//			// TODO Auto-generated constructor stub
//		}
//
//		@Override
//		public Result<IValue> interpret(Evaluator __eval) {
//
//			__eval.getCurrentModuleEnvironment().addTest(this);
//			return org.rascalmpl.interpreter.result.ResultFactory.nothing();
//
//		}
//
//	}

	static public class Unlabeled extends org.rascalmpl.ast.Test.Unlabeled {

		public Unlabeled(IConstructor __param1, Tags __param2, Expression __param3) {
			super(__param1, __param2, __param3);
		}

		@Override
		public Result<IValue> interpret(Evaluator __eval) {

			__eval.getCurrentModuleEnvironment().addTest(this);
			return org.rascalmpl.interpreter.result.ResultFactory.nothing();

		}

	}

	public Test(IConstructor __param1) {
		super(__param1);
	}
}
