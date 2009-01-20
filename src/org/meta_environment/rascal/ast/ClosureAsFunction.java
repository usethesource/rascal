package org.meta_environment.rascal.ast;

import org.eclipse.imp.pdb.facts.ITree;

public abstract class ClosureAsFunction extends AbstractAST {
	static public class Ambiguity extends ClosureAsFunction {
		private final java.util.List<org.meta_environment.rascal.ast.ClosureAsFunction> alternatives;

		public Ambiguity(
				ITree tree,
				java.util.List<org.meta_environment.rascal.ast.ClosureAsFunction> alternatives) {
			this.alternatives = java.util.Collections
					.unmodifiableList(alternatives);
			this.tree = tree;
		}

		@Override
		public <T> T accept(IASTVisitor<T> v) {
			return v.visitClosureAsFunctionAmbiguity(this);
		}

		public java.util.List<org.meta_environment.rascal.ast.ClosureAsFunction> getAlternatives() {
			return alternatives;
		}
	}

	static public class Evaluated extends ClosureAsFunction {
		private org.meta_environment.rascal.ast.Expression expression;

		/* "#" expression:Expression -> ClosureAsFunction {cons("Evaluated")} */
		private Evaluated() {
		}

		/* package */Evaluated(ITree tree,
				org.meta_environment.rascal.ast.Expression expression) {
			this.tree = tree;
			this.expression = expression;
		}

		private void $setExpression(org.meta_environment.rascal.ast.Expression x) {
			this.expression = x;
		}

		@Override
		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitClosureAsFunctionEvaluated(this);
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
		public boolean isEvaluated() {
			return true;
		}

		public Evaluated setExpression(
				org.meta_environment.rascal.ast.Expression x) {
			final Evaluated z = new Evaluated();
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

	public boolean isEvaluated() {
		return false;
	}
}