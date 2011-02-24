package org.rascalmpl.semantics.dynamic;

import org.eclipse.imp.pdb.facts.ISourceLocation;
import org.eclipse.imp.pdb.facts.IValue;
import org.rascalmpl.interpreter.Evaluator;
import org.rascalmpl.interpreter.result.Result;

public abstract class PostProtocolChars extends
		org.rascalmpl.ast.PostProtocolChars {

	static public class Lexical extends
			org.rascalmpl.ast.PostProtocolChars.Lexical {

		public Lexical(ISourceLocation __param1, String __param2) {
			super(__param1, __param2);
		}

		@Override
		public Result<IValue> interpret(Evaluator __eval) {

			String str = this.getString();
			return org.rascalmpl.interpreter.result.ResultFactory
					.makeResult(org.rascalmpl.interpreter.Evaluator.__getTf()
							.stringType(), __eval.__getVf().string(
							str.substring(1, str.length() - 3)), __eval);

		}

	}

	public PostProtocolChars(ISourceLocation __param1) {
		super(__param1);
	}
}
