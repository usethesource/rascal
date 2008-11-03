package org.meta_environment.rascal.ast;

import org.eclipse.imp.pdb.facts.ITree;

public abstract class Generator extends AbstractAST {
	static public class Ambiguity extends Generator {
		private final java.util.List<org.meta_environment.rascal.ast.Generator> alternatives;

		public Ambiguity(
				java.util.List<org.meta_environment.rascal.ast.Generator> alternatives) {
			this.alternatives = java.util.Collections
					.unmodifiableList(alternatives);
		}

		public java.util.List<org.meta_environment.rascal.ast.Generator> getAlternatives() {
			return alternatives;
		}
	}

	static public class Expression extends Generator {
		private org.meta_environment.rascal.ast.Expression expression;

		/* expression:Expression -> Generator {cons("Expression")} */
		private Expression() {
		}

		/* package */Expression(ITree tree,
				org.meta_environment.rascal.ast.Expression expression) {
			this.tree = tree;
			this.expression = expression;
		}

		private void $setExpression(org.meta_environment.rascal.ast.Expression x) {
			this.expression = x;
		}

		public IVisitable accept(IASTVisitor visitor) {
			return visitor.visitGeneratorExpression(this);
		}

		@Override
		public org.meta_environment.rascal.ast.Expression getExpression() {
			return expression;
		}

		public Expression setExpression(
				org.meta_environment.rascal.ast.Expression x) {
			Expression z = new Expression();
			z.$setExpression(x);
			return z;
		}
	}

	static public class Producer extends Generator {
		private org.meta_environment.rascal.ast.ValueProducer producer;

		/* producer:ValueProducer -> Generator {cons("Producer")} */
		private Producer() {
		}

		/* package */Producer(ITree tree,
				org.meta_environment.rascal.ast.ValueProducer producer) {
			this.tree = tree;
			this.producer = producer;
		}

		private void $setProducer(
				org.meta_environment.rascal.ast.ValueProducer x) {
			this.producer = x;
		}

		public IVisitable accept(IASTVisitor visitor) {
			return visitor.visitGeneratorProducer(this);
		}

		@Override
		public org.meta_environment.rascal.ast.ValueProducer getProducer() {
			return producer;
		}

		public Producer setProducer(
				org.meta_environment.rascal.ast.ValueProducer x) {
			Producer z = new Producer();
			z.$setProducer(x);
			return z;
		}
	}

	public org.meta_environment.rascal.ast.Expression getExpression() {
		throw new UnsupportedOperationException();
	}

	public org.meta_environment.rascal.ast.ValueProducer getProducer() {
		throw new UnsupportedOperationException();
	}
}
