package org.meta_environment.rascal.ast;

import org.eclipse.imp.pdb.facts.INode;

public abstract class ClosureAsFunction extends AbstractAST {
	public org.meta_environment.rascal.ast.Expression getExpression() {
		throw new UnsupportedOperationException();
	}

	public boolean hasExpression() {
		return false;
	}

	public boolean isEvaluated() {
		return false;
	}

	static public class Evaluated extends ClosureAsFunction {
		/** &syms -> &sort {&attr*1, cons(&strcon), &attr*2} */
		private Evaluated() {
		}

		/* package */Evaluated(INode node,
				org.meta_environment.rascal.ast.Expression expression) {
			this.node = node;
			this.expression = expression;
		}

		@Override
		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitClosureAsFunctionEvaluated(this);
		}

		@Override
		public boolean isEvaluated() {
			return true;
		}

		@Override
		public boolean hasExpression() {
			return true;
		}

		private org.meta_environment.rascal.ast.Expression expression;

		@Override
		public org.meta_environment.rascal.ast.Expression getExpression() {
			return expression;
		}

		private void $setExpression(org.meta_environment.rascal.ast.Expression x) {
			this.expression = x;
		}

		public Evaluated setExpression(
				org.meta_environment.rascal.ast.Expression x) {
			Evaluated z = new Evaluated();
			z.$setExpression(x);
			return z;
		}
	}

	static public class Ambiguity extends ClosureAsFunction {
		private final java.util.List<org.meta_environment.rascal.ast.ClosureAsFunction> alternatives;

		public Ambiguity(
				INode node,
				java.util.List<org.meta_environment.rascal.ast.ClosureAsFunction> alternatives) {
			this.alternatives = java.util.Collections
					.unmodifiableList(alternatives);
			this.node = node;
		}

		public java.util.List<org.meta_environment.rascal.ast.ClosureAsFunction> getAlternatives() {
			return alternatives;
		}

		@Override
		public <T> T accept(IASTVisitor<T> v) {
			return v.visitClosureAsFunctionAmbiguity(this);
		}
	}
}