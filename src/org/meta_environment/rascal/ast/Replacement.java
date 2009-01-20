package org.meta_environment.rascal.ast;

import org.eclipse.imp.pdb.facts.ITree;

public abstract class Replacement extends AbstractAST {
	static public class Ambiguity extends Replacement {
		private final java.util.List<org.meta_environment.rascal.ast.Replacement> alternatives;

		public Ambiguity(
				ITree tree,
				java.util.List<org.meta_environment.rascal.ast.Replacement> alternatives) {
			this.alternatives = java.util.Collections
					.unmodifiableList(alternatives);
			this.tree = tree;
		}

		@Override
		public <T> T accept(IASTVisitor<T> v) {
			return v.visitReplacementAmbiguity(this);
		}

		public java.util.List<org.meta_environment.rascal.ast.Replacement> getAlternatives() {
			return alternatives;
		}
	}

	static public class Conditional extends Replacement {
		private org.meta_environment.rascal.ast.Expression replacementExpression;
		private java.util.List<org.meta_environment.rascal.ast.Expression> conditions;

		/*
		 * replacementExpression:Expression "when" conditions:{Expression ","}+
		 * -> Replacement {cons("Conditional")}
		 */
		private Conditional() {
		}

		/* package */Conditional(
				ITree tree,
				org.meta_environment.rascal.ast.Expression replacementExpression,
				java.util.List<org.meta_environment.rascal.ast.Expression> conditions) {
			this.tree = tree;
			this.replacementExpression = replacementExpression;
			this.conditions = conditions;
		}

		private void $setConditions(
				java.util.List<org.meta_environment.rascal.ast.Expression> x) {
			this.conditions = x;
		}

		private void $setReplacementExpression(
				org.meta_environment.rascal.ast.Expression x) {
			this.replacementExpression = x;
		}

		@Override
		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitReplacementConditional(this);
		}

		@Override
		public java.util.List<org.meta_environment.rascal.ast.Expression> getConditions() {
			return conditions;
		}

		@Override
		public org.meta_environment.rascal.ast.Expression getReplacementExpression() {
			return replacementExpression;
		}

		@Override
		public boolean hasConditions() {
			return true;
		}

		@Override
		public boolean hasReplacementExpression() {
			return true;
		}

		@Override
		public boolean isConditional() {
			return true;
		}

		public Conditional setConditions(
				java.util.List<org.meta_environment.rascal.ast.Expression> x) {
			final Conditional z = new Conditional();
			z.$setConditions(x);
			return z;
		}

		public Conditional setReplacementExpression(
				org.meta_environment.rascal.ast.Expression x) {
			final Conditional z = new Conditional();
			z.$setReplacementExpression(x);
			return z;
		}
	}

	static public class Unconditional extends Replacement {
		private org.meta_environment.rascal.ast.Expression replacementExpression;

		/*
		 * replacementExpression:Expression -> Replacement
		 * {cons("Unconditional")}
		 */
		private Unconditional() {
		}

		/* package */Unconditional(ITree tree,
				org.meta_environment.rascal.ast.Expression replacementExpression) {
			this.tree = tree;
			this.replacementExpression = replacementExpression;
		}

		private void $setReplacementExpression(
				org.meta_environment.rascal.ast.Expression x) {
			this.replacementExpression = x;
		}

		@Override
		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitReplacementUnconditional(this);
		}

		@Override
		public org.meta_environment.rascal.ast.Expression getReplacementExpression() {
			return replacementExpression;
		}

		@Override
		public boolean hasReplacementExpression() {
			return true;
		}

		@Override
		public boolean isUnconditional() {
			return true;
		}

		public Unconditional setReplacementExpression(
				org.meta_environment.rascal.ast.Expression x) {
			final Unconditional z = new Unconditional();
			z.$setReplacementExpression(x);
			return z;
		}
	}

	@Override
	public abstract <T> T accept(IASTVisitor<T> visitor);

	public java.util.List<org.meta_environment.rascal.ast.Expression> getConditions() {
		throw new UnsupportedOperationException();
	}

	public org.meta_environment.rascal.ast.Expression getReplacementExpression() {
		throw new UnsupportedOperationException();
	}

	public boolean hasConditions() {
		return false;
	}

	public boolean hasReplacementExpression() {
		return false;
	}

	public boolean isConditional() {
		return false;
	}

	public boolean isUnconditional() {
		return false;
	}
}