package org.rascalmpl.semantics.dynamic;

import java.lang.String;
import java.util.List;
import org.eclipse.imp.pdb.facts.INode;
import org.eclipse.imp.pdb.facts.IValue;
import org.rascalmpl.ast.NullASTVisitor;
import org.rascalmpl.interpreter.Evaluator;
import org.rascalmpl.interpreter.result.Result;

public abstract class JustDate extends org.rascalmpl.ast.JustDate {

	public JustDate(INode __param1) {
		super(__param1);
	}

	static public class Lexical extends org.rascalmpl.ast.JustDate.Lexical {

		public Lexical(INode __param1, String __param2) {
			super(__param1, __param2);
		}

		@Override
		public Result<IValue> __evaluate(Evaluator __eval) {

			// Date is of the form $<date>
			String datePart = this.getString().substring(1);
			return __eval.createVisitedDate(datePart, this);

		}

		@Override
		public <T> T __evaluate(NullASTVisitor<T> __eval) {
			return null;
		}

	}

	static public class Ambiguity extends org.rascalmpl.ast.JustDate.Ambiguity {

		public Ambiguity(INode __param1, List<org.rascalmpl.ast.JustDate> __param2) {
			super(__param1, __param2);
		}

		@Override
		public <T> T __evaluate(NullASTVisitor<T> __eval) {
			return null;
		}

	}
}