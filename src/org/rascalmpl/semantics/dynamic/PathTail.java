package org.rascalmpl.semantics.dynamic;

import org.eclipse.imp.pdb.facts.ISourceLocation;
import org.eclipse.imp.pdb.facts.IValue;
import org.rascalmpl.ast.Expression;
import org.rascalmpl.ast.MidPathChars;
import org.rascalmpl.ast.PostPathChars;
import org.rascalmpl.interpreter.Evaluator;
import org.rascalmpl.interpreter.result.Result;

public abstract class PathTail extends org.rascalmpl.ast.PathTail {

	static public class Mid extends org.rascalmpl.ast.PathTail.Mid {

		public Mid(ISourceLocation __param1, MidPathChars __param2, Expression __param3,
				org.rascalmpl.ast.PathTail __param4) {
			super(__param1, __param2, __param3, __param4);
		}

		@Override
		public Result<IValue> interpret(Evaluator __eval) {
			Result<IValue> mid = this.getMid().interpret(__eval);
			Result<IValue> expr = this.getExpression().interpret(__eval);
			Result<IValue> tail = this.getTail().interpret(__eval);

			return mid.add(expr).add(tail);
		}

	}

	static public class Post extends org.rascalmpl.ast.PathTail.Post {

		public Post(ISourceLocation __param1, PostPathChars __param2) {
			super(__param1, __param2);
		}

		@Override
		public Result<IValue> interpret(Evaluator __eval) {
			return this.getPost().interpret(__eval);
		}

	}

	public PathTail(ISourceLocation __param1) {
		super(__param1);
	}
}
