package org.rascalmpl.semantics.dynamic;

import java.lang.String;
import java.util.List;
import org.eclipse.imp.pdb.facts.INode;
import org.eclipse.imp.pdb.facts.IValue;
import org.rascalmpl.ast.NullASTVisitor;
import org.rascalmpl.interpreter.Evaluator;
import org.rascalmpl.interpreter.result.Result;

public abstract class PostPathChars extends org.rascalmpl.ast.PostPathChars {

	public PostPathChars(INode __param1) {
		super(__param1);
	}

	static public class Lexical extends org.rascalmpl.ast.PostPathChars.Lexical {

		public Lexical(INode __param1, String __param2) {
			super(__param1, __param2);
		}

		@Override
		public Result<IValue> interpret(Evaluator __eval) {

			String str = this.getString();
			return org.rascalmpl.interpreter.result.ResultFactory.makeResult(org.rascalmpl.interpreter.Evaluator.__getTf().stringType(), __eval.__getVf().string(str.substring(1, str.length() - 1)),
					__eval);

		}


	}

	static public class Ambiguity extends org.rascalmpl.ast.PostPathChars.Ambiguity {

		public Ambiguity(INode __param1, List<org.rascalmpl.ast.PostPathChars> __param2) {
			super(__param1, __param2);
		}


	}
}
