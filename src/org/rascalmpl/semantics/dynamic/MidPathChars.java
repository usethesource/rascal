package org.rascalmpl.semantics.dynamic;

import java.lang.String;
import java.util.List;
import org.eclipse.imp.pdb.facts.INode;
import org.eclipse.imp.pdb.facts.IValue;
import org.rascalmpl.ast.NullASTVisitor;
import org.rascalmpl.interpreter.Evaluator;
import org.rascalmpl.interpreter.result.Result;

public abstract class MidPathChars extends org.rascalmpl.ast.MidPathChars {

	public MidPathChars(INode __param1) {
		super(__param1);
	}

	static public class Lexical extends org.rascalmpl.ast.MidPathChars.Lexical {

		public Lexical(INode __param1, String __param2) {
			super(__param1, __param2);
		}

		@Override
		public <T> T __evaluate(NullASTVisitor<T> __eval) {
			return null;
		}

		@Override
		public Result<IValue> interpret(Evaluator __eval) {

			String s = this.getString();
			s = s.substring(1, s.length() - 1);
			return org.rascalmpl.interpreter.result.ResultFactory.makeResult(org.rascalmpl.interpreter.Evaluator.__getTf().stringType(), __eval.__getVf().string(s), __eval);

		}

	}

	static public class Ambiguity extends org.rascalmpl.ast.MidPathChars.Ambiguity {

		public Ambiguity(INode __param1, List<org.rascalmpl.ast.MidPathChars> __param2) {
			super(__param1, __param2);
		}

		@Override
		public <T> T __evaluate(NullASTVisitor<T> __eval) {
			return null;
		}

	}
}