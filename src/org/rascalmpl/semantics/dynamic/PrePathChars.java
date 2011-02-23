package org.rascalmpl.semantics.dynamic;

import org.eclipse.imp.pdb.facts.INode;
import org.eclipse.imp.pdb.facts.IValue;
import org.rascalmpl.interpreter.Evaluator;
import org.rascalmpl.interpreter.result.Result;

public abstract class PrePathChars extends org.rascalmpl.ast.PrePathChars {

	static public class Lexical extends org.rascalmpl.ast.PrePathChars.Lexical {

		public Lexical(INode __param1, String __param2) {
			super(__param1, __param2);
		}

		@Override
		public Result<IValue> interpret(Evaluator __eval) {

			String str = this.getString();
			return org.rascalmpl.interpreter.result.ResultFactory
					.makeResult(org.rascalmpl.interpreter.Evaluator.__getTf()
							.stringType(), __eval.__getVf().string(
							str.substring(0, str.length() - 1)), __eval);

		}

	}

	public PrePathChars(INode __param1) {
		super(__param1);
	}
}
