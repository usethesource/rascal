package org.rascalmpl.semantics.dynamic;

import org.eclipse.imp.pdb.facts.INode;
import org.eclipse.imp.pdb.facts.IValue;
import org.rascalmpl.interpreter.Evaluator;
import org.rascalmpl.interpreter.result.Result;

public abstract class JustTime extends org.rascalmpl.ast.JustTime {

	static public class Lexical extends org.rascalmpl.ast.JustTime.Lexical {

		public Lexical(INode __param1, String __param2) {
			super(__param1, __param2);
		}

		@Override
		public Result<IValue> interpret(Evaluator __eval) {

			// Time is of the form $T<time>
			String timePart = this.getString().substring(2);
			return __eval.createVisitedTime(timePart, this);

		}

	}

	public JustTime(INode __param1) {
		super(__param1);
	}

}
