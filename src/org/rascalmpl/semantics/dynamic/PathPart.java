package org.rascalmpl.semantics.dynamic;

import java.util.List;

import org.eclipse.imp.pdb.facts.INode;
import org.eclipse.imp.pdb.facts.IValue;
import org.rascalmpl.ast.Expression;
import org.rascalmpl.ast.PathChars;
import org.rascalmpl.ast.PathTail;
import org.rascalmpl.ast.PrePathChars;
import org.rascalmpl.interpreter.Evaluator;
import org.rascalmpl.interpreter.result.Result;

public abstract class PathPart extends org.rascalmpl.ast.PathPart {

	public PathPart(INode __param1) {
		super(__param1);
	}

	static public class Interpolated extends org.rascalmpl.ast.PathPart.Interpolated {

		public Interpolated(INode __param1, PrePathChars __param2, Expression __param3, PathTail __param4) {
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

	static public class NonInterpolated extends org.rascalmpl.ast.PathPart.NonInterpolated {

		public NonInterpolated(INode __param1, PathChars __param2) {
			super(__param1, __param2);
		}

		@Override
		public Result<IValue> interpret(Evaluator __eval) {

			return this.getPathChars().interpret(__eval);

		}


	}

	static public class Ambiguity extends org.rascalmpl.ast.PathPart.Ambiguity {

		public Ambiguity(INode __param1, List<org.rascalmpl.ast.PathPart> __param2) {
			super(__param1, __param2);
		}


	}
}
