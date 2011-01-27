package org.rascalmpl.semantics.dynamic;

import java.lang.String;
import java.math.BigInteger;
import java.util.List;
import org.eclipse.imp.pdb.facts.INode;
import org.eclipse.imp.pdb.facts.IValue;
import org.rascalmpl.ast.NullASTVisitor;
import org.rascalmpl.interpreter.Evaluator;
import org.rascalmpl.interpreter.result.Result;

public abstract class HexIntegerLiteral extends org.rascalmpl.ast.HexIntegerLiteral {

	public HexIntegerLiteral(INode __param1) {
		super(__param1);
	}

	static public class Ambiguity extends org.rascalmpl.ast.HexIntegerLiteral.Ambiguity {

		public Ambiguity(INode __param1, List<org.rascalmpl.ast.HexIntegerLiteral> __param2) {
			super(__param1, __param2);
		}

		@Override
		public <T> T __evaluate(NullASTVisitor<T> __eval) {
			return null;
		}

	}

	static public class Lexical extends org.rascalmpl.ast.HexIntegerLiteral.Lexical {

		public Lexical(INode __param1, String __param2) {
			super(__param1, __param2);
		}

		@Override
		public Result<IValue> interpret(Evaluator __eval) {

			String chars = this.getString();
			String hex = chars.substring(2, chars.length());
			return org.rascalmpl.interpreter.result.ResultFactory.makeResult(org.rascalmpl.interpreter.Evaluator.__getTf().integerType(), __eval.__getVf().integer(new BigInteger(hex, 16).toString()),
					__eval);

		}

		@Override
		public <T> T __evaluate(NullASTVisitor<T> __eval) {
			return null;
		}

	}
}