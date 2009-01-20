package org.meta_environment.rascal.ast;

import org.eclipse.imp.pdb.facts.ITree;

public abstract class Bound extends AbstractAST {
	static public class Ambiguity extends Bound {
		private final java.util.List<org.meta_environment.rascal.ast.Bound> alternatives;

		public Ambiguity(
				ITree tree,
				java.util.List<org.meta_environment.rascal.ast.Bound> alternatives) {
			this.alternatives = java.util.Collections
					.unmodifiableList(alternatives);
			this.tree = tree;
		}

		@Override
		public <T> T accept(IASTVisitor<T> v) {
			return v.visitBoundAmbiguity(this);
		}

		public java.util.List<org.meta_environment.rascal.ast.Bound> getAlternatives() {
			return alternatives;
		}
	}

	static public class Default extends Bound {
		private org.meta_environment.rascal.ast.Expression expression;

		/* "(" expression:Expression ")" -> Bound {cons("Default")} */
		private Default() {
		}

		/* package */Default(ITree tree,
				org.meta_environment.rascal.ast.Expression expression) {
			this.tree = tree;
			this.expression = expression;
		}

		private void $setExpression(org.meta_environment.rascal.ast.Expression x) {
			this.expression = x;
		}

		@Override
		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitBoundDefault(this);
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
		public boolean isDefault() {
			return true;
		}

		public Default setExpression(
				org.meta_environment.rascal.ast.Expression x) {
			final Default z = new Default();
			z.$setExpression(x);
			return z;
		}
	}

	static public class Empty extends Bound {
		/* package */Empty(ITree tree) {
			this.tree = tree;
		}

		@Override
		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitBoundEmpty(this);
		}

		@Override
		public boolean isEmpty() {
			return true;
		}
	}

	@Override
	public abstract <T> T accept(IASTVisitor<T> visitor);

	public org.meta_environment.rascal.ast.Expression getExpression() {
		throw new UnsupportedOperationException();
	}

	public boolean hasExpression() {
		return false;
	}

	public boolean isDefault() {
		return false;
	}

	public boolean isEmpty() {
		return false;
	}
}