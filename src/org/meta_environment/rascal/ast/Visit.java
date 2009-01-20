package org.meta_environment.rascal.ast;

import org.eclipse.imp.pdb.facts.ITree;

public abstract class Visit extends AbstractAST {
	static public class Ambiguity extends Visit {
		private final java.util.List<org.meta_environment.rascal.ast.Visit> alternatives;

		public Ambiguity(
				ITree tree,
				java.util.List<org.meta_environment.rascal.ast.Visit> alternatives) {
			this.alternatives = java.util.Collections
					.unmodifiableList(alternatives);
			this.tree = tree;
		}

		@Override
		public <T> T accept(IASTVisitor<T> v) {
			return v.visitVisitAmbiguity(this);
		}

		public java.util.List<org.meta_environment.rascal.ast.Visit> getAlternatives() {
			return alternatives;
		}
	}

	static public class DefaultStrategy extends Visit {
		private org.meta_environment.rascal.ast.Expression subject;
		private java.util.List<org.meta_environment.rascal.ast.Case> cases;

		/*
		 * "visit" "(" subject:Expression ")" "{" cases:Case+ "}" -> Visit
		 * {cons("DefaultStrategy")}
		 */
		private DefaultStrategy() {
		}

		/* package */DefaultStrategy(ITree tree,
				org.meta_environment.rascal.ast.Expression subject,
				java.util.List<org.meta_environment.rascal.ast.Case> cases) {
			this.tree = tree;
			this.subject = subject;
			this.cases = cases;
		}

		private void $setCases(
				java.util.List<org.meta_environment.rascal.ast.Case> x) {
			this.cases = x;
		}

		private void $setSubject(org.meta_environment.rascal.ast.Expression x) {
			this.subject = x;
		}

		@Override
		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitVisitDefaultStrategy(this);
		}

		@Override
		public java.util.List<org.meta_environment.rascal.ast.Case> getCases() {
			return cases;
		}

		@Override
		public org.meta_environment.rascal.ast.Expression getSubject() {
			return subject;
		}

		@Override
		public boolean hasCases() {
			return true;
		}

		@Override
		public boolean hasSubject() {
			return true;
		}

		@Override
		public boolean isDefaultStrategy() {
			return true;
		}

		public DefaultStrategy setCases(
				java.util.List<org.meta_environment.rascal.ast.Case> x) {
			final DefaultStrategy z = new DefaultStrategy();
			z.$setCases(x);
			return z;
		}

		public DefaultStrategy setSubject(
				org.meta_environment.rascal.ast.Expression x) {
			final DefaultStrategy z = new DefaultStrategy();
			z.$setSubject(x);
			return z;
		}
	}

	static public class GivenStrategy extends Visit {
		private org.meta_environment.rascal.ast.Strategy strategy;
		private org.meta_environment.rascal.ast.Expression subject;
		private java.util.List<org.meta_environment.rascal.ast.Case> cases;

		/*
		 * strategy:Strategy "visit" "(" subject:Expression ")" "{" cases:Case+
		 * "}" -> Visit {cons("GivenStrategy")}
		 */
		private GivenStrategy() {
		}

		/* package */GivenStrategy(ITree tree,
				org.meta_environment.rascal.ast.Strategy strategy,
				org.meta_environment.rascal.ast.Expression subject,
				java.util.List<org.meta_environment.rascal.ast.Case> cases) {
			this.tree = tree;
			this.strategy = strategy;
			this.subject = subject;
			this.cases = cases;
		}

		private void $setCases(
				java.util.List<org.meta_environment.rascal.ast.Case> x) {
			this.cases = x;
		}

		private void $setStrategy(org.meta_environment.rascal.ast.Strategy x) {
			this.strategy = x;
		}

		private void $setSubject(org.meta_environment.rascal.ast.Expression x) {
			this.subject = x;
		}

		@Override
		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitVisitGivenStrategy(this);
		}

		@Override
		public java.util.List<org.meta_environment.rascal.ast.Case> getCases() {
			return cases;
		}

		@Override
		public org.meta_environment.rascal.ast.Strategy getStrategy() {
			return strategy;
		}

		@Override
		public org.meta_environment.rascal.ast.Expression getSubject() {
			return subject;
		}

		@Override
		public boolean hasCases() {
			return true;
		}

		@Override
		public boolean hasStrategy() {
			return true;
		}

		@Override
		public boolean hasSubject() {
			return true;
		}

		@Override
		public boolean isGivenStrategy() {
			return true;
		}

		public GivenStrategy setCases(
				java.util.List<org.meta_environment.rascal.ast.Case> x) {
			final GivenStrategy z = new GivenStrategy();
			z.$setCases(x);
			return z;
		}

		public GivenStrategy setStrategy(
				org.meta_environment.rascal.ast.Strategy x) {
			final GivenStrategy z = new GivenStrategy();
			z.$setStrategy(x);
			return z;
		}

		public GivenStrategy setSubject(
				org.meta_environment.rascal.ast.Expression x) {
			final GivenStrategy z = new GivenStrategy();
			z.$setSubject(x);
			return z;
		}
	}

	@Override
	public abstract <T> T accept(IASTVisitor<T> visitor);

	public java.util.List<org.meta_environment.rascal.ast.Case> getCases() {
		throw new UnsupportedOperationException();
	}

	public org.meta_environment.rascal.ast.Strategy getStrategy() {
		throw new UnsupportedOperationException();
	}

	public org.meta_environment.rascal.ast.Expression getSubject() {
		throw new UnsupportedOperationException();
	}

	public boolean hasCases() {
		return false;
	}

	public boolean hasStrategy() {
		return false;
	}

	public boolean hasSubject() {
		return false;
	}

	public boolean isDefaultStrategy() {
		return false;
	}

	public boolean isGivenStrategy() {
		return false;
	}
}