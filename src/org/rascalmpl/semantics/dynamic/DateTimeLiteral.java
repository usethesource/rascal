package org.rascalmpl.semantics.dynamic;

import org.eclipse.imp.pdb.facts.ISourceLocation;
import org.eclipse.imp.pdb.facts.IValue;
import org.rascalmpl.ast.DateAndTime;
import org.rascalmpl.ast.JustDate;
import org.rascalmpl.ast.JustTime;
import org.rascalmpl.interpreter.Evaluator;
import org.rascalmpl.interpreter.result.Result;

public abstract class DateTimeLiteral extends org.rascalmpl.ast.DateTimeLiteral {

	static public class DateAndTimeLiteral extends
			org.rascalmpl.ast.DateTimeLiteral.DateAndTimeLiteral {

		public DateAndTimeLiteral(ISourceLocation __param1, DateAndTime __param2) {
			super(__param1, __param2);
		}

		@Override
		public Result<IValue> interpret(Evaluator __eval) {
			return this.getDateAndTime().interpret(__eval);
		}

	}

	static public class DateLiteral extends
			org.rascalmpl.ast.DateTimeLiteral.DateLiteral {

		public DateLiteral(ISourceLocation __param1, JustDate __param2) {
			super(__param1, __param2);
		}

		@Override
		public Result<IValue> interpret(Evaluator __eval) {
			return this.getDate().interpret(__eval);
		}

	}

	static public class TimeLiteral extends
			org.rascalmpl.ast.DateTimeLiteral.TimeLiteral {

		public TimeLiteral(ISourceLocation __param1, JustTime __param2) {
			super(__param1, __param2);
		}

		@Override
		public Result<IValue> interpret(Evaluator __eval) {
			return this.getTime().interpret(__eval);
		}

	}

	public DateTimeLiteral(ISourceLocation __param1) {
		super(__param1);
	}
}
