package org.rascalmpl.semantics.dynamic;

import org.eclipse.imp.pdb.facts.ISourceLocation;
import org.eclipse.imp.pdb.facts.IValue;
import org.rascalmpl.ast.Expression;
import org.rascalmpl.ast.PreProtocolChars;
import org.rascalmpl.ast.ProtocolChars;
import org.rascalmpl.ast.ProtocolTail;
import org.rascalmpl.interpreter.Evaluator;
import org.rascalmpl.interpreter.result.Result;

public abstract class ProtocolPart extends org.rascalmpl.ast.ProtocolPart {

	static public class Interpolated extends
			org.rascalmpl.ast.ProtocolPart.Interpolated {

		public Interpolated(ISourceLocation __param1, PreProtocolChars __param2,
				Expression __param3, ProtocolTail __param4) {
			super(__param1, __param2, __param3, __param4);
		}

		@Override
		public Result<IValue> interpret(Evaluator __eval) {
			Result<IValue> pre = this.getPre().interpret(__eval);
			Result<IValue> expr = this.getExpression().interpret(__eval);
			Result<IValue> tail = this.getTail().interpret(__eval);

			return pre.add(expr).add(tail);
		}

	}

	static public class NonInterpolated extends
			org.rascalmpl.ast.ProtocolPart.NonInterpolated {

		public NonInterpolated(ISourceLocation __param1, ProtocolChars __param2) {
			super(__param1, __param2);
		}

		@Override
		public Result<IValue> interpret(Evaluator __eval) {
			return this.getProtocolChars().interpret(__eval);
		}

	}

	public ProtocolPart(ISourceLocation __param1) {
		super(__param1);
	}

}
