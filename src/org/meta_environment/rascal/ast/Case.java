package org.meta_environment.rascal.ast;

import org.eclipse.imp.pdb.facts.ITree;

public abstract class Case extends AbstractAST {
	static public class Ambiguity extends Case {
		private final java.util.List<org.meta_environment.rascal.ast.Case> alternatives;

		public Ambiguity(
				ITree tree,
				java.util.List<org.meta_environment.rascal.ast.Case> alternatives) {
			this.alternatives = java.util.Collections
					.unmodifiableList(alternatives);
			this.tree = tree;
		}

		@Override
		public <T> T accept(IASTVisitor<T> v) {
			return v.visitCaseAmbiguity(this);
		}

		public java.util.List<org.meta_environment.rascal.ast.Case> getAlternatives() {
			return alternatives;
		}
	}

	static public class Default extends Case {
		private org.meta_environment.rascal.ast.Statement statement;

		/* "default" ":" statement:Statement -> Case {cons("Default")} */
		private Default() {
		}

		/* package */Default(ITree tree,
				org.meta_environment.rascal.ast.Statement statement) {
			this.tree = tree;
			this.statement = statement;
		}

		private void $setStatement(org.meta_environment.rascal.ast.Statement x) {
			this.statement = x;
		}

		@Override
		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitCaseDefault(this);
		}

		@Override
		public org.meta_environment.rascal.ast.Statement getStatement() {
			return statement;
		}

		@Override
		public boolean hasStatement() {
			return true;
		}

		@Override
		public boolean isDefault() {
			return true;
		}

		public Default setStatement(org.meta_environment.rascal.ast.Statement x) {
			final Default z = new Default();
			z.$setStatement(x);
			return z;
		}
	}

	static public class Rule extends Case {
		private org.meta_environment.rascal.ast.Rule rule;

		/* "case" rule:Rule -> Case {cons("Rule")} */
		private Rule() {
		}

		/* package */Rule(ITree tree, org.meta_environment.rascal.ast.Rule rule) {
			this.tree = tree;
			this.rule = rule;
		}

		private void $setRule(org.meta_environment.rascal.ast.Rule x) {
			this.rule = x;
		}

		@Override
		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitCaseRule(this);
		}

		@Override
		public org.meta_environment.rascal.ast.Rule getRule() {
			return rule;
		}

		@Override
		public boolean hasRule() {
			return true;
		}

		@Override
		public boolean isRule() {
			return true;
		}

		public Rule setRule(org.meta_environment.rascal.ast.Rule x) {
			final Rule z = new Rule();
			z.$setRule(x);
			return z;
		}
	}

	@Override
	public abstract <T> T accept(IASTVisitor<T> visitor);

	public org.meta_environment.rascal.ast.Rule getRule() {
		throw new UnsupportedOperationException();
	}

	public org.meta_environment.rascal.ast.Statement getStatement() {
		throw new UnsupportedOperationException();
	}

	public boolean hasRule() {
		return false;
	}

	public boolean hasStatement() {
		return false;
	}

	public boolean isDefault() {
		return false;
	}

	public boolean isRule() {
		return false;
	}
}