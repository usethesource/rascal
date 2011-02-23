package org.rascalmpl.semantics.dynamic;

import java.lang.String;
import java.util.List;
import org.eclipse.imp.pdb.facts.INode;
import org.eclipse.imp.pdb.facts.IValue;
import org.rascalmpl.interpreter.Evaluator;
import org.rascalmpl.interpreter.result.Result;

public abstract class ProtocolChars extends org.rascalmpl.ast.ProtocolChars {

	public ProtocolChars(INode __param1) {
		super(__param1);
	}

	static public class Ambiguity extends org.rascalmpl.ast.ProtocolChars.Ambiguity {

		public Ambiguity(INode __param1, List<org.rascalmpl.ast.ProtocolChars> __param2) {
			super(__param1, __param2);
		}


	}

	static public class Lexical extends org.rascalmpl.ast.ProtocolChars.Lexical {

		public Lexical(INode __param1, String __param2) {
			super(__param1, __param2);
		}

		@Override
		public Result<IValue> interpret(Evaluator __eval) {

			String str = this.getString();
			return org.rascalmpl.interpreter.result.ResultFactory.makeResult(TF.stringType(), VF.string(str.substring(1, str.length() - 3)),
					__eval);

		}


	}
}
