package org.rascalmpl.semantics.dynamic;

import org.eclipse.imp.pdb.facts.INode;
import org.eclipse.imp.pdb.facts.IValue;
import org.rascalmpl.interpreter.Evaluator;
import org.rascalmpl.interpreter.result.Result;

public abstract class JustDate extends org.rascalmpl.ast.JustDate {

	static public class Lexical extends org.rascalmpl.ast.JustDate.Lexical {

		public Lexical(INode __param1, String __param2) {
			super(__param1, __param2);
		}

		@Override
		public Result<IValue> interpret(Evaluator __eval) {

			// Date is of the form $<date>
			String datePart = this.getString().substring(1);
			return __eval.createVisitedDate(datePart, this);

		}

	}

	public JustDate(INode __param1) {
		super(__param1);
	}

}
