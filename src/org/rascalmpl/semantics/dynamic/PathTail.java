package org.rascalmpl.semantics.dynamic;

import java.lang.StringBuilder;
import java.util.List;
import org.eclipse.imp.pdb.facts.INode;
import org.eclipse.imp.pdb.facts.IString;
import org.eclipse.imp.pdb.facts.IValue;
import org.rascalmpl.ast.Expression;
import org.rascalmpl.ast.MidPathChars;
import org.rascalmpl.ast.NullASTVisitor;
import org.rascalmpl.ast.PostPathChars;
import org.rascalmpl.interpreter.Evaluator;
import org.rascalmpl.interpreter.result.Result;

public abstract class PathTail extends org.rascalmpl.ast.PathTail {

	public PathTail(INode __param1) {
		super(__param1);
	}

	static public class Mid extends org.rascalmpl.ast.PathTail.Mid {

		public Mid(INode __param1, MidPathChars __param2, Expression __param3, org.rascalmpl.ast.PathTail __param4) {
			super(__param1, __param2, __param3, __param4);
		}

		@Override
		public <T> T __evaluate(NullASTVisitor<T> __eval) {
			return null;
		}

		@Override
		public Result<IValue> __evaluate(Evaluator __eval) {

			Result<IValue> mid = this.getMid().__evaluate(__eval);
			Result<IValue> expr = this.getExpression().__evaluate(__eval);
			Result<IValue> tail = this.getTail().__evaluate(__eval);
			StringBuilder result = new StringBuilder();

			result.append(((IString) mid.getValue()).getValue());
			__eval.appendToString(expr.getValue(), result);
			result.append(((IString) tail.getValue()).getValue());

			return org.rascalmpl.interpreter.result.ResultFactory.makeResult(org.rascalmpl.interpreter.Evaluator.__getTf().stringType(), __eval.__getVf().string(result.toString()), __eval);

		}

	}

	static public class Ambiguity extends org.rascalmpl.ast.PathTail.Ambiguity {

		public Ambiguity(INode __param1, List<org.rascalmpl.ast.PathTail> __param2) {
			super(__param1, __param2);
		}

		@Override
		public <T> T __evaluate(NullASTVisitor<T> __eval) {
			return null;
		}

	}

	static public class Post extends org.rascalmpl.ast.PathTail.Post {

		public Post(INode __param1, PostPathChars __param2) {
			super(__param1, __param2);
		}

		@Override
		public <T> T __evaluate(NullASTVisitor<T> __eval) {
			return null;
		}

		@Override
		public Result<IValue> __evaluate(Evaluator __eval) {

			return this.getPost().__evaluate(__eval);

		}

	}
}