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

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.exceptions.FactParseError;
import org.eclipse.imp.pdb.facts.exceptions.FactTypeUseException;
import org.eclipse.imp.pdb.facts.io.StandardTextReader;
import org.eclipse.imp.pdb.facts.type.Type;
import org.rascalmpl.interpreter.IEvaluator;
import org.rascalmpl.interpreter.asserts.ImplementationError;
import org.rascalmpl.interpreter.env.Environment;
import org.rascalmpl.interpreter.result.Result;
import org.rascalmpl.interpreter.staticErrors.DateTimeSyntax;

public abstract class DateAndTime extends org.rascalmpl.ast.DateAndTime {

	static public class Lexical extends org.rascalmpl.ast.DateAndTime.Lexical {

		public Lexical(IConstructor __param1, String __param2) {
			super(__param1, __param2);
		}

		@Override
		public Result<IValue> interpret(IEvaluator<Result<IValue>> __eval) {
			// Split into date and time components; of the form $<date>T<time>
			String dtPart = this.getString().substring(1);
			String datePart = dtPart.substring(0, dtPart.indexOf("T"));
			String timePart = dtPart.substring(dtPart.indexOf("T") + 1);

			return createVisitedDateTime(__eval, datePart, timePart, this);
		}

		@Override
		public Type typeOf(Environment env) {
			return TF.dateTimeType();
		}
		
		public Result<IValue> createVisitedDateTime(IEvaluator<Result<IValue>> eval, String datePart, String timePart, Lexical x) {
			try {
				StandardTextReader parser = new StandardTextReader();
				IValue result = parser.read(VF, new StringReader("$" + datePart + "T" + timePart));
				return makeResult(TF.dateTimeType(), result, eval);
			} catch (FactTypeUseException e) {
				throw new DateTimeSyntax(e.getMessage(), eval.getCurrentAST().getLocation());
			} catch (FactParseError e) {
				throw new DateTimeSyntax(e.getMessage(), eval.getCurrentAST().getLocation());
			} catch (IOException e) {
				throw new ImplementationError(e.getMessage());
			}
		}

	}

	public DateAndTime(IConstructor __param1) {
		super(__param1);
	}
}
