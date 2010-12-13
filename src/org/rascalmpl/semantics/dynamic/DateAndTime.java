package org.rascalmpl.semantics.dynamic;

import java.lang.String;
import java.util.List;
import org.eclipse.imp.pdb.facts.INode;
import org.eclipse.imp.pdb.facts.IValue;
import org.rascalmpl.ast.NullASTVisitor;
import org.rascalmpl.interpreter.Evaluator;
import org.rascalmpl.interpreter.result.Result;

public abstract class DateAndTime extends org.rascalmpl.ast.DateAndTime {

	public DateAndTime(INode __param1) {
		super(__param1);
	}

	static public class Ambiguity extends org.rascalmpl.ast.DateAndTime.Ambiguity {

		public Ambiguity(INode __param1, List<org.rascalmpl.ast.DateAndTime> __param2) {
			super(__param1, __param2);
		}

		@Override
		public <T> T __evaluate(NullASTVisitor<T> __eval) {
			return null;
		}

	}

	static public class Lexical extends org.rascalmpl.ast.DateAndTime.Lexical {

		public Lexical(INode __param1, String __param2) {
			super(__param1, __param2);
		}

		@Override
		public <T> T __evaluate(NullASTVisitor<T> __eval) {
			return null;
		}

		@Override
		public Result<IValue> __evaluate(Evaluator __eval) {

			// Split into date and time components; of the form $<date>T<time>
			String dtPart = this.getString().substring(1);
			String datePart = dtPart.substring(0, dtPart.indexOf("T"));
			String timePart = dtPart.substring(dtPart.indexOf("T") + 1);

			return __eval.createVisitedDateTime(datePart, timePart, this);

		}

	}
}