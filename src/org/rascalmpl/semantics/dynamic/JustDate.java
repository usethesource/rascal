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
import org.eclipse.imp.pdb.facts.ISourceLocation;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.exceptions.FactParseError;
import org.eclipse.imp.pdb.facts.exceptions.FactTypeUseException;
import org.eclipse.imp.pdb.facts.io.StandardTextReader;
import org.rascalmpl.interpreter.IEvaluator;
import org.rascalmpl.interpreter.asserts.ImplementationError;
import org.rascalmpl.interpreter.result.Result;
import org.rascalmpl.interpreter.staticErrors.DateTimeSyntax;

public abstract class JustDate extends org.rascalmpl.ast.JustDate {

	static public class Lexical extends org.rascalmpl.ast.JustDate.Lexical {

		public Lexical(ISourceLocation __param1, IConstructor tree, String __param2) {
			super(__param1, tree, __param2);
		}

		@Override
		public Result<IValue> interpret(IEvaluator<Result<IValue>> __eval) {
			// Date is of the form $<date>
			String datePart = this.getString().substring(1);
			return createVisitedDate(__eval, datePart, this);
		}
		
		private Result<IValue> createVisitedDate(IEvaluator<Result<IValue>> eval, String datePart, org.rascalmpl.ast.JustDate.Lexical x) {
			try {
				datePart.replaceAll("-", "");

				StandardTextReader parser = new StandardTextReader();
				IValue result = parser.read(VF, new StringReader("$" + datePart));
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

	public JustDate(ISourceLocation __param1, IConstructor tree) {
		super(__param1, tree);
	}

}
