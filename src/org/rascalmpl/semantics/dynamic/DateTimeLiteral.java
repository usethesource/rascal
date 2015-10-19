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

import org.rascalmpl.ast.DateAndTime;
import org.rascalmpl.ast.JustDate;
import org.rascalmpl.ast.JustTime;
import org.rascalmpl.interpreter.IEvaluator;
import org.rascalmpl.interpreter.result.Result;
import org.rascalmpl.value.IConstructor;
import org.rascalmpl.value.ISourceLocation;
import org.rascalmpl.value.IValue;

public abstract class DateTimeLiteral extends org.rascalmpl.ast.DateTimeLiteral {

	static public class DateAndTimeLiteral extends
			org.rascalmpl.ast.DateTimeLiteral.DateAndTimeLiteral {

		public DateAndTimeLiteral(ISourceLocation __param1, IConstructor tree, DateAndTime __param2) {
			super(__param1, tree, __param2);
		}

		@Override
		public Result<IValue> interpret(IEvaluator<Result<IValue>> __eval) {
			return this.getDateAndTime().interpret(__eval);
		}

	}

	static public class DateLiteral extends
			org.rascalmpl.ast.DateTimeLiteral.DateLiteral {

		public DateLiteral(ISourceLocation __param1, IConstructor tree, JustDate __param2) {
			super(__param1, tree, __param2);
		}

		@Override
		public Result<IValue> interpret(IEvaluator<Result<IValue>> __eval) {
			return this.getDate().interpret(__eval);
		}

	}

	static public class TimeLiteral extends
			org.rascalmpl.ast.DateTimeLiteral.TimeLiteral {

		public TimeLiteral(ISourceLocation __param1, IConstructor tree, JustTime __param2) {
			super(__param1, tree, __param2);
		}

		@Override
		public Result<IValue> interpret(IEvaluator<Result<IValue>> __eval) {
			return this.getTime().interpret(__eval);
		}

	}

	public DateTimeLiteral(ISourceLocation __param1, IConstructor tree) {
		super(__param1, tree);
	}
}
