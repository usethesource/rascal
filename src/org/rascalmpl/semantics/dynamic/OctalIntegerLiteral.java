package org.rascalmpl.semantics.dynamic;

import java.math.BigInteger;

import org.eclipse.imp.pdb.facts.ISourceLocation;
import org.eclipse.imp.pdb.facts.IValue;
import org.rascalmpl.interpreter.Evaluator;
import org.rascalmpl.interpreter.result.Result;

public abstract class OctalIntegerLiteral extends
		org.rascalmpl.ast.OctalIntegerLiteral {

	static public class Lexical extends
			org.rascalmpl.ast.OctalIntegerLiteral.Lexical {

		public Lexical(ISourceLocation __param1, String __param2) {
			super(__param1, __param2);
		}

		@Override
		public Result<IValue> interpret(Evaluator __eval) {
			return org.rascalmpl.interpreter.result.ResultFactory
					.makeResult(org.rascalmpl.interpreter.Evaluator.__getTf()
							.integerType(), __eval.__getVf().integer(
							new BigInteger(this.getString(), 8).toString()),
							__eval);
		}

	}

	public OctalIntegerLiteral(ISourceLocation __param1) {
		super(__param1);
	}
}
