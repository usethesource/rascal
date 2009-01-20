package org.meta_environment.rascal.ast;

import org.eclipse.imp.pdb.facts.ITree;

public abstract class ValueProducer extends AbstractAST {
	static public class Ambiguity extends ValueProducer {
		private final java.util.List<org.meta_environment.rascal.ast.ValueProducer> alternatives;

		public Ambiguity(
				ITree tree,
				java.util.List<org.meta_environment.rascal.ast.ValueProducer> alternatives) {
			this.alternatives = java.util.Collections
					.unmodifiableList(alternatives);
			this.tree = tree;
		}

		@Override
		public <T> T accept(IASTVisitor<T> v) {
			return v.visitValueProducerAmbiguity(this);
		}

		public java.util.List<org.meta_environment.rascal.ast.ValueProducer> getAlternatives() {
			return alternatives;
		}
	}

	static public class DefaultStrategy extends ValueProducer {
		private org.meta_environment.rascal.ast.Expression pattern;
		private org.meta_environment.rascal.ast.Expression expression;

		/*
		 * pattern:Expression ":" expression:Expression -> ValueProducer
		 * {cons("DefaultStrategy")}
		 */
		private DefaultStrategy() {
		}

		/* package */DefaultStrategy(ITree tree,
				org.meta_environment.rascal.ast.Expression pattern,
				org.meta_environment.rascal.ast.Expression expression) {
			this.tree = tree;
			this.pattern = pattern;
			this.expression = expression;
		}

		private void $setExpression(org.meta_environment.rascal.ast.Expression x) {
			this.expression = x;
		}

		private void $setPattern(org.meta_environment.rascal.ast.Expression x) {
			this.pattern = x;
		}

		@Override
		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitValueProducerDefaultStrategy(this);
		}

		@Override
		public org.meta_environment.rascal.ast.Expression getExpression() {
			return expression;
		}

		@Override
		public org.meta_environment.rascal.ast.Expression getPattern() {
			return pattern;
		}

		@Override
		public boolean hasExpression() {
			return true;
		}

		@Override
		public boolean hasPattern() {
			return true;
		}

		@Override
		public boolean isDefaultStrategy() {
			return true;
		}

		public DefaultStrategy setExpression(
				org.meta_environment.rascal.ast.Expression x) {
			final DefaultStrategy z = new DefaultStrategy();
			z.$setExpression(x);
			return z;
		}

		public DefaultStrategy setPattern(
				org.meta_environment.rascal.ast.Expression x) {
			final DefaultStrategy z = new DefaultStrategy();
			z.$setPattern(x);
			return z;
		}
	}

	static public class GivenStrategy extends ValueProducer {
		private org.meta_environment.rascal.ast.Strategy strategy;
		private org.meta_environment.rascal.ast.Expression pattern;
		private org.meta_environment.rascal.ast.Expression expression;

		/*
		 * strategy:Strategy pattern:Expression ":" expression:Expression ->
		 * ValueProducer {cons("GivenStrategy")}
		 */
		private GivenStrategy() {
		}

		/* package */GivenStrategy(ITree tree,
				org.meta_environment.rascal.ast.Strategy strategy,
				org.meta_environment.rascal.ast.Expression pattern,
				org.meta_environment.rascal.ast.Expression expression) {
			this.tree = tree;
			this.strategy = strategy;
			this.pattern = pattern;
			this.expression = expression;
		}

		private void $setExpression(org.meta_environment.rascal.ast.Expression x) {
			this.expression = x;
		}

		private void $setPattern(org.meta_environment.rascal.ast.Expression x) {
			this.pattern = x;
		}

		private void $setStrategy(org.meta_environment.rascal.ast.Strategy x) {
			this.strategy = x;
		}

		@Override
		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitValueProducerGivenStrategy(this);
		}

		@Override
		public org.meta_environment.rascal.ast.Expression getExpression() {
			return expression;
		}

		@Override
		public org.meta_environment.rascal.ast.Expression getPattern() {
			return pattern;
		}

		@Override
		public org.meta_environment.rascal.ast.Strategy getStrategy() {
			return strategy;
		}

		@Override
		public boolean hasExpression() {
			return true;
		}

		@Override
		public boolean hasPattern() {
			return true;
		}

		@Override
		public boolean hasStrategy() {
			return true;
		}

		@Override
		public boolean isGivenStrategy() {
			return true;
		}

		public GivenStrategy setExpression(
				org.meta_environment.rascal.ast.Expression x) {
			final GivenStrategy z = new GivenStrategy();
			z.$setExpression(x);
			return z;
		}

		public GivenStrategy setPattern(
				org.meta_environment.rascal.ast.Expression x) {
			final GivenStrategy z = new GivenStrategy();
			z.$setPattern(x);
			return z;
		}

		public GivenStrategy setStrategy(
				org.meta_environment.rascal.ast.Strategy x) {
			final GivenStrategy z = new GivenStrategy();
			z.$setStrategy(x);
			return z;
		}
	}

	@Override
	public abstract <T> T accept(IASTVisitor<T> visitor);

	public org.meta_environment.rascal.ast.Expression getExpression() {
		throw new UnsupportedOperationException();
	}

	public org.meta_environment.rascal.ast.Expression getPattern() {
		throw new UnsupportedOperationException();
	}

	public org.meta_environment.rascal.ast.Strategy getStrategy() {
		throw new UnsupportedOperationException();
	}

	public boolean hasExpression() {
		return false;
	}

	public boolean hasPattern() {
		return false;
	}

	public boolean hasStrategy() {
		return false;
	}

	public boolean isDefaultStrategy() {
		return false;
	}

	public boolean isGivenStrategy() {
		return false;
	}
}