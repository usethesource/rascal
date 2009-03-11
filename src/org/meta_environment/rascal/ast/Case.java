package org.meta_environment.rascal.ast;

import org.eclipse.imp.pdb.facts.INode;

public abstract class Case extends AbstractAST {
	public org.meta_environment.rascal.ast.Rule getRule() {
		throw new UnsupportedOperationException();
	}

	public boolean hasRule() {
		return false;
	}

	public boolean isRule() {
		return false;
	}

	static public class Rule extends Case {
		/** &syms -> &sort {&attr*1, cons(&strcon), &attr*2} */
		private Rule() {
		}

		/* package */Rule(INode node, org.meta_environment.rascal.ast.Rule rule) {
			this.node = node;
			this.rule = rule;
		}

		@Override
		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitCaseRule(this);
		}

		@Override
		public boolean isRule() {
			return true;
		}

		@Override
		public boolean hasRule() {
			return true;
		}

		private org.meta_environment.rascal.ast.Rule rule;

		@Override
		public org.meta_environment.rascal.ast.Rule getRule() {
			return rule;
		}

		private void $setRule(org.meta_environment.rascal.ast.Rule x) {
			this.rule = x;
		}

		public Rule setRule(org.meta_environment.rascal.ast.Rule x) {
			Rule z = new Rule();
			z.$setRule(x);
			return z;
		}
	}

	static public class Ambiguity extends Case {
		private final java.util.List<org.meta_environment.rascal.ast.Case> alternatives;

		public Ambiguity(
				INode node,
				java.util.List<org.meta_environment.rascal.ast.Case> alternatives) {
			this.alternatives = java.util.Collections
					.unmodifiableList(alternatives);
			this.node = node;
		}

		public java.util.List<org.meta_environment.rascal.ast.Case> getAlternatives() {
			return alternatives;
		}

		@Override
		public <T> T accept(IASTVisitor<T> v) {
			return v.visitCaseAmbiguity(this);
		}
	}

	public org.meta_environment.rascal.ast.Statement getStatement() {
		throw new UnsupportedOperationException();
	}

	public boolean hasStatement() {
		return false;
	}

	public boolean isDefault() {
		return false;
	}

	static public class Default extends Case {
		/** &syms -> &sort {&attr*1, cons(&strcon), &attr*2} */
		private Default() {
		}

		/* package */Default(INode node,
				org.meta_environment.rascal.ast.Statement statement) {
			this.node = node;
			this.statement = statement;
		}

		@Override
		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitCaseDefault(this);
		}

		@Override
		public boolean isDefault() {
			return true;
		}

		@Override
		public boolean hasStatement() {
			return true;
		}

		private org.meta_environment.rascal.ast.Statement statement;

		@Override
		public org.meta_environment.rascal.ast.Statement getStatement() {
			return statement;
		}

		private void $setStatement(org.meta_environment.rascal.ast.Statement x) {
			this.statement = x;
		}

		public Default setStatement(org.meta_environment.rascal.ast.Statement x) {
			Default z = new Default();
			z.$setStatement(x);
			return z;
		}
	}

	@Override
	public abstract <T> T accept(IASTVisitor<T> visitor);
}