package org.meta_environment.rascal.ast;

import org.eclipse.imp.pdb.facts.INode;

public abstract class Visit extends AbstractAST {
	public org.meta_environment.rascal.ast.Expression getSubject() {
		throw new UnsupportedOperationException();
	}

	public java.util.List<org.meta_environment.rascal.ast.Case> getCases() {
		throw new UnsupportedOperationException();
	}

	public boolean hasSubject() {
		return false;
	}

	public boolean hasCases() {
		return false;
	}

	public boolean isDefaultStrategy() {
		return false;
	}

	static public class DefaultStrategy extends Visit {
		/*
		 * "visit" "(" subject:Expression ")" "{" cases:Case+ "}" -> Visit
		 * {cons("DefaultStrategy")}
		 */
		private DefaultStrategy() {
			super();
		}

		public DefaultStrategy(INode node,
				org.meta_environment.rascal.ast.Expression subject,
				java.util.List<org.meta_environment.rascal.ast.Case> cases) {
			this.node = node;
			this.subject = subject;
			this.cases = cases;
		}

		@Override
		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitVisitDefaultStrategy(this);
		}

		@Override
		public boolean isDefaultStrategy() {
			return true;
		}

		@Override
		public boolean hasSubject() {
			return true;
		}

		@Override
		public boolean hasCases() {
			return true;
		}

		private org.meta_environment.rascal.ast.Expression subject;

		@Override
		public org.meta_environment.rascal.ast.Expression getSubject() {
			return subject;
		}

		private void $setSubject(org.meta_environment.rascal.ast.Expression x) {
			this.subject = x;
		}

		public DefaultStrategy setSubject(
				org.meta_environment.rascal.ast.Expression x) {
			DefaultStrategy z = new DefaultStrategy();
			z.$setSubject(x);
			return z;
		}

		private java.util.List<org.meta_environment.rascal.ast.Case> cases;

		@Override
		public java.util.List<org.meta_environment.rascal.ast.Case> getCases() {
			return cases;
		}

		private void $setCases(
				java.util.List<org.meta_environment.rascal.ast.Case> x) {
			this.cases = x;
		}

		public DefaultStrategy setCases(
				java.util.List<org.meta_environment.rascal.ast.Case> x) {
			DefaultStrategy z = new DefaultStrategy();
			z.$setCases(x);
			return z;
		}
	}

	static public class Ambiguity extends Visit {
		private final java.util.List<org.meta_environment.rascal.ast.Visit> alternatives;

		public Ambiguity(
				INode node,
				java.util.List<org.meta_environment.rascal.ast.Visit> alternatives) {
			this.alternatives = java.util.Collections
					.unmodifiableList(alternatives);
			this.node = node;
		}

		public java.util.List<org.meta_environment.rascal.ast.Visit> getAlternatives() {
			return alternatives;
		}

		@Override
		public <T> T accept(IASTVisitor<T> v) {
			return v.visitVisitAmbiguity(this);
		}
	}

	public org.meta_environment.rascal.ast.Strategy getStrategy() {
		throw new UnsupportedOperationException();
	}

	public boolean hasStrategy() {
		return false;
	}

	public boolean isGivenStrategy() {
		return false;
	}

	static public class GivenStrategy extends Visit {
		/*
		 * strategy:Strategy "visit" "(" subject:Expression ")" "{" cases:Case+
		 * "}" -> Visit {cons("GivenStrategy")}
		 */
		private GivenStrategy() {
			super();
		}

		public GivenStrategy(INode node,
				org.meta_environment.rascal.ast.Strategy strategy,
				org.meta_environment.rascal.ast.Expression subject,
				java.util.List<org.meta_environment.rascal.ast.Case> cases) {
			this.node = node;
			this.strategy = strategy;
			this.subject = subject;
			this.cases = cases;
		}

		@Override
		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitVisitGivenStrategy(this);
		}

		@Override
		public boolean isGivenStrategy() {
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
		public boolean hasCases() {
			return true;
		}

		private org.meta_environment.rascal.ast.Strategy strategy;

		@Override
		public org.meta_environment.rascal.ast.Strategy getStrategy() {
			return strategy;
		}

		private void $setStrategy(org.meta_environment.rascal.ast.Strategy x) {
			this.strategy = x;
		}

		public GivenStrategy setStrategy(
				org.meta_environment.rascal.ast.Strategy x) {
			GivenStrategy z = new GivenStrategy();
			z.$setStrategy(x);
			return z;
		}

		private org.meta_environment.rascal.ast.Expression subject;

		@Override
		public org.meta_environment.rascal.ast.Expression getSubject() {
			return subject;
		}

		private void $setSubject(org.meta_environment.rascal.ast.Expression x) {
			this.subject = x;
		}

		public GivenStrategy setSubject(
				org.meta_environment.rascal.ast.Expression x) {
			GivenStrategy z = new GivenStrategy();
			z.$setSubject(x);
			return z;
		}

		private java.util.List<org.meta_environment.rascal.ast.Case> cases;

		@Override
		public java.util.List<org.meta_environment.rascal.ast.Case> getCases() {
			return cases;
		}

		private void $setCases(
				java.util.List<org.meta_environment.rascal.ast.Case> x) {
			this.cases = x;
		}

		public GivenStrategy setCases(
				java.util.List<org.meta_environment.rascal.ast.Case> x) {
			GivenStrategy z = new GivenStrategy();
			z.$setCases(x);
			return z;
		}
	}

	@Override
	public abstract <T> T accept(IASTVisitor<T> visitor);
}