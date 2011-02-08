package org.rascalmpl.semantics.dynamic;

import java.lang.StringBuilder;
import java.util.List;
import org.eclipse.imp.pdb.facts.INode;
import org.eclipse.imp.pdb.facts.IString;
import org.eclipse.imp.pdb.facts.IValue;
import org.rascalmpl.ast.Expression;
import org.rascalmpl.ast.NullASTVisitor;
import org.rascalmpl.ast.PreProtocolChars;
import org.rascalmpl.ast.ProtocolChars;
import org.rascalmpl.ast.ProtocolTail;
import org.rascalmpl.interpreter.Evaluator;
import org.rascalmpl.interpreter.result.Result;

public abstract class ProtocolPart extends org.rascalmpl.ast.ProtocolPart {

	public ProtocolPart(INode __param1) {
		super(__param1);
	}

	static public class NonInterpolated extends org.rascalmpl.ast.ProtocolPart.NonInterpolated {

		public NonInterpolated(INode __param1, ProtocolChars __param2) {
			super(__param1, __param2);
		}


		@Override
		public Result<IValue> interpret(Evaluator __eval) {

			return this.getProtocolChars().interpret(__eval);

		}

	}

	static public class Interpolated extends org.rascalmpl.ast.ProtocolPart.Interpolated {

		public Interpolated(INode __param1, PreProtocolChars __param2, Expression __param3, ProtocolTail __param4) {
			super(__param1, __param2, __param3, __param4);
		}


		@Override
		public Result<IValue> interpret(Evaluator __eval) {

			Result<IValue> pre = this.getPre().interpret(__eval);
			Result<IValue> expr = this.getExpression().interpret(__eval);
			Result<IValue> tail = this.getTail().interpret(__eval);
			StringBuilder result = new StringBuilder();

			result.append(((IString) pre.getValue()).getValue());
			__eval.appendToString(expr.getValue(), result);
			result.append(((IString) tail.getValue()).getValue());

			return org.rascalmpl.interpreter.result.ResultFactory.makeResult(org.rascalmpl.interpreter.Evaluator.__getTf().stringType(), __eval.__getVf().string(result.toString()), __eval);

		}

	}

	static public class Ambiguity extends org.rascalmpl.ast.ProtocolPart.Ambiguity {

		public Ambiguity(INode __param1, List<org.rascalmpl.ast.ProtocolPart> __param2) {
			super(__param1, __param2);
		}


	}
}
