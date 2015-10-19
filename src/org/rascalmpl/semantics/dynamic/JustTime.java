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

import java.io.IOException;
import java.io.StringReader;

import org.rascalmpl.interpreter.IEvaluator;
import org.rascalmpl.interpreter.asserts.ImplementationError;
import org.rascalmpl.interpreter.result.Result;
import org.rascalmpl.interpreter.staticErrors.DateTimeSyntax;
import org.rascalmpl.value.IConstructor;
import org.rascalmpl.value.ISourceLocation;
import org.rascalmpl.value.IValue;
import org.rascalmpl.value.exceptions.FactParseError;
import org.rascalmpl.value.exceptions.FactTypeUseException;
import org.rascalmpl.value.io.StandardTextReader;

public abstract class JustTime extends org.rascalmpl.ast.JustTime {

	static public class Lexical extends org.rascalmpl.ast.JustTime.Lexical {

		public Lexical(ISourceLocation __param1, IConstructor tree, String __param2) {
			super(__param1, tree, __param2);
		}

		@Override
		public Result<IValue> interpret(IEvaluator<Result<IValue>> __eval) {
			// Time is of the form $T<time>
			String timePart = this.getString().substring(2);
			return createVisitedTime(__eval, timePart, this);
		}
		
		private Result<IValue> createVisitedTime(IEvaluator<Result<IValue>> eval, String timePart, org.rascalmpl.ast.JustTime.Lexical x) {
			try {
				timePart.replaceAll(":","");

				StandardTextReader parser = new StandardTextReader();
				IValue result = parser.read(VF, new StringReader("$T" + timePart));
				return makeResult(TF.dateTimeType(), result, eval);
			} catch (FactTypeUseException e) {
				throw new DateTimeSyntax(e.getMessage(), eval.getCurrentAST().getLocation());
			} catch (FactParseError e){
				throw new DateTimeSyntax(e.getMessage(), eval.getCurrentAST().getLocation());
			} catch (IOException e) {
				throw new ImplementationError(e.getMessage());
			}
		}
	}

	public JustTime(ISourceLocation __param1, IConstructor tree) {
		super(__param1, tree);
	}

}
