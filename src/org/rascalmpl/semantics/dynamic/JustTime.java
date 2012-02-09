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

import java.io.IOException;
import java.io.StringReader;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.exceptions.FactTypeUseException;
import org.eclipse.imp.pdb.facts.io.StandardTextReader;
import org.rascalmpl.interpreter.Evaluator;
import org.rascalmpl.interpreter.asserts.ImplementationError;
import org.rascalmpl.interpreter.result.Result;

public abstract class JustTime extends org.rascalmpl.ast.JustTime {

	static public class Lexical extends org.rascalmpl.ast.JustTime.Lexical {

		public Lexical(IConstructor __param1, String __param2) {
			super(__param1, __param2);
		}

		@Override
		public Result<IValue> interpret(Evaluator __eval) {
			// Time is of the form $T<time>
			String timePart = this.getString().substring(2);
			return createVisitedTime(__eval, timePart, this);
		}
		
		private Result<IValue> createVisitedTime(Evaluator eval, String timePart, org.rascalmpl.ast.JustTime.Lexical x) {
			try {
				timePart.replaceAll(":","");

				StandardTextReader parser = new StandardTextReader();
				IValue result = parser.read(VF, new StringReader("T" + timePart));
				return makeResult(TF.dateTimeType(), result, eval);
			} catch (FactTypeUseException e) {
				throw new ImplementationError(e.getMessage());
			} catch (IOException e) {
				throw new ImplementationError(e.getMessage());
			}
		}
	}

	public JustTime(IConstructor __param1) {
		super(__param1);
	}

}
