package org.meta_environment.rascal.ast;

import org.eclipse.imp.pdb.facts.ITree;

public abstract class Bound extends AbstractAST {
	static public class Ambiguity extends Bound {
		private final java.util.List<org.meta_environment.rascal.ast.Bound> alternatives;

		public Ambiguity(
				java.util.List<org.meta_environment.rascal.ast.Bound> alternatives) {
			this.alternatives = java.util.Collections
					.unmodifiableList(alternatives);
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

		public IVisitable accept(IASTVisitor visitor) {
			return visitor.visitBoundDefault(this);
		}

		public org.meta_environment.rascal.ast.Expression getExpression() {
			return expression;
		}

		public Default setExpression(
				org.meta_environment.rascal.ast.Expression x) {
			Default z = new Default();
			z.$setExpression(x);
			return z;
		}
	}

	static public class Empty extends Bound {
		/* package */Empty(ITree tree) {
			this.tree = tree;
		}

		public IVisitable accept(IASTVisitor visitor) {
			return visitor.visitBoundEmpty(this);
		}
	}
}
