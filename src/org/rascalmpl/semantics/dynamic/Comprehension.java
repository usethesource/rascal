package org.rascalmpl.semantics.dynamic;

import java.util.ArrayList;
import org.eclipse.imp.pdb.facts.INode;
import org.eclipse.imp.pdb.facts.IValue;
import org.rascalmpl.ast.Expression;
import org.rascalmpl.ast.NullASTVisitor;
import org.rascalmpl.interpreter.Evaluator;
import org.rascalmpl.interpreter.result.Result;

public abstract class Comprehension extends org.rascalmpl.ast.Comprehension {

	public Comprehension(INode __param1) {
		super(__param1);
	}

	static public class List extends org.rascalmpl.ast.Comprehension.List {

		public List(INode __param1, java.util.List<Expression> __param2, java.util.List<Expression> __param3) {
			super(__param1, __param2, __param3);
		}

		@Override
		public Result<IValue> interpret(Evaluator __eval) {

			return __eval.evalComprehension(this.getGenerators(), __eval.new ListComprehensionWriter(this.getResults(), __eval));

		}

		@Override
		public <T> T __evaluate(NullASTVisitor<T> __eval) {
			return null;
		}

	}

	static public class Ambiguity extends org.rascalmpl.ast.Comprehension.Ambiguity {

		public Ambiguity(INode __param1, java.util.List<org.rascalmpl.ast.Comprehension> __param2) {
			super(__param1, __param2);
		}

		@Override
		public <T> T __evaluate(NullASTVisitor<T> __eval) {
			return null;
		}

	}

	static public class Map extends org.rascalmpl.ast.Comprehension.Map {

		public Map(INode __param1, Expression __param2, Expression __param3, java.util.List<Expression> __param4) {
			super(__param1, __param2, __param3, __param4);
		}

		@Override
		public <T> T __evaluate(NullASTVisitor<T> __eval) {
			return null;
		}

		@Override
		public Result<IValue> interpret(Evaluator __eval) {

			java.util.List<Expression> resultExprs = new ArrayList<Expression>();
			resultExprs.add(this.getFrom());
			resultExprs.add(this.getTo());
			return __eval.evalComprehension(this.getGenerators(), __eval.new MapComprehensionWriter(resultExprs, __eval));

		}

	}

	static public class Set extends org.rascalmpl.ast.Comprehension.Set {

		public Set(INode __param1, java.util.List<Expression> __param2, java.util.List<Expression> __param3) {
			super(__param1, __param2, __param3);
		}

		@Override
		public <T> T __evaluate(NullASTVisitor<T> __eval) {
			return null;
		}

		@Override
		public Result<IValue> interpret(Evaluator __eval) {

			return __eval.evalComprehension(this.getGenerators(), __eval.new SetComprehensionWriter(this.getResults(), __eval));

		}

	}
}