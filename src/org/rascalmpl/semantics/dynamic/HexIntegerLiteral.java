package org.rascalmpl.semantics.dynamic;

import java.math.BigInteger;

import org.eclipse.imp.pdb.facts.ISourceLocation;
import org.eclipse.imp.pdb.facts.IValue;
import org.rascalmpl.interpreter.Evaluator;
import org.rascalmpl.interpreter.result.Result;

public abstract class HexIntegerLiteral extends
		org.rascalmpl.ast.HexIntegerLiteral {

	static public class Lexical extends
			org.rascalmpl.ast.HexIntegerLiteral.Lexical {

		public Lexical(ISourceLocation __param1, String __param2) {
			super(__param1, __param2);
		}

		@Override
		public Result<IValue> interpret(Evaluator __eval) {

			String chars = this.getString();
			String hex = chars.substring(2, chars.length());
			return org.rascalmpl.interpreter.result.ResultFactory
					.makeResult(org.rascalmpl.interpreter.Evaluator.__getTf()
							.integerType(), __eval.__getVf().integer(
							new BigInteger(hex, 16).toString()), __eval);

		}

	}

	public HexIntegerLiteral(ISourceLocation __param1) {
		super(__param1);
	}
}
