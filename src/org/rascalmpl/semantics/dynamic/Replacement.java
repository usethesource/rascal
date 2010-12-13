package org.rascalmpl.semantics.dynamic;

import java.util.List;
import org.eclipse.imp.pdb.facts.INode;
import org.rascalmpl.ast.Expression;
import org.rascalmpl.ast.NullASTVisitor;

public abstract class Replacement extends org.rascalmpl.ast.Replacement {

	public Replacement(INode __param1) {
		super(__param1);
	}

	static public class Conditional extends org.rascalmpl.ast.Replacement.Conditional {

		public Conditional(INode __param1, Expression __param2, List<Expression> __param3) {
			super(__param1, __param2, __param3);
		}

		@Override
		public <T> T __evaluate(NullASTVisitor<T> __eval) {
			return null;
		}

	}

	static public class Unconditional extends org.rascalmpl.ast.Replacement.Unconditional {

		public Unconditional(INode __param1, Expression __param2) {
			super(__param1, __param2);
		}

		@Override
		public <T> T __evaluate(NullASTVisitor<T> __eval) {
			return null;
		}

	}

	static public class Ambiguity extends org.rascalmpl.ast.Replacement.Ambiguity {

		public Ambiguity(INode __param1, List<org.rascalmpl.ast.Replacement> __param2) {
			super(__param1, __param2);
		}

		@Override
		public <T> T __evaluate(NullASTVisitor<T> __eval) {
			return null;
		}

	}
}