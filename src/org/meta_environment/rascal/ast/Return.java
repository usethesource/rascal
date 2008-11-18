package org.meta_environment.rascal.ast;

import org.eclipse.imp.pdb.facts.ITree;

public abstract class Return extends AbstractAST {
	static public class Ambiguity extends Return {
		private final java.util.List<org.meta_environment.rascal.ast.Return> alternatives;

		public Ambiguity(
				java.util.List<org.meta_environment.rascal.ast.Return> alternatives) {
			this.alternatives = java.util.Collections
					.unmodifiableList(alternatives);
		}

		public java.util.List<org.meta_environment.rascal.ast.Return> getAlternatives() {
			return alternatives;
		}
	}

	static public class NoExpression extends Return {
		/* package */NoExpression(ITree tree) {
			this.tree = tree;
		}

		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitReturnNoExpression(this);
		}

		@Override
		public boolean isNoExpression() {
			return true;
		}
	}

	static public class WithExpression extends Return {
		private org.meta_environment.rascal.ast.Expression expression;

		/* "return" expression:Expression ";" -> Return {cons("WithExpression")} */
		private WithExpression() {
		}

		/* package */WithExpression(ITree tree,
				org.meta_environment.rascal.ast.Expression expression) {
			this.tree = tree;
			this.expression = expression;
		}

		private void $setExpression(org.meta_environment.rascal.ast.Expression x) {
			this.expression = x;
		}

		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitReturnWithExpression(this);
		}

		@Override
		public org.meta_environment.rascal.ast.Expression getExpression() {
			return expression;
		}

		@Override
		public boolean hasExpression() {
			return true;
		}

		@Override
		public boolean isWithExpression() {
			return true;
		}

		public WithExpression setExpression(
				org.meta_environment.rascal.ast.Expression x) {
			WithExpression z = new WithExpression();
			z.$setExpression(x);
			return z;
		}
	}

	public org.meta_environment.rascal.ast.Expression getExpression() {
		throw new UnsupportedOperationException();
	}

	public boolean hasExpression() {
		return false;
	}

	public boolean isNoExpression() {
		return false;
	}

	public boolean isWithExpression() {
		return false;
	}
}
