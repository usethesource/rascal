package org.meta_environment.rascal.ast;

import org.eclipse.imp.pdb.facts.ITree;

public abstract class Case extends AbstractAST {
	static public class Ambiguity extends Case {
		private final java.util.List<org.meta_environment.rascal.ast.Case> alternatives;

		public Ambiguity(
				java.util.List<org.meta_environment.rascal.ast.Case> alternatives) {
			this.alternatives = java.util.Collections
					.unmodifiableList(alternatives);
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

		public IVisitable accept(IASTVisitor visitor) {
			return visitor.visitCaseDefault(this);
		}

		@Override
		public org.meta_environment.rascal.ast.Statement getStatement() {
			return statement;
		}

		public Default setStatement(org.meta_environment.rascal.ast.Statement x) {
			Default z = new Default();
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

		public IVisitable accept(IASTVisitor visitor) {
			return visitor.visitCaseRule(this);
		}

		@Override
		public org.meta_environment.rascal.ast.Rule getRule() {
			return rule;
		}

		public Rule setRule(org.meta_environment.rascal.ast.Rule x) {
			Rule z = new Rule();
			z.$setRule(x);
			return z;
		}
	}

	public org.meta_environment.rascal.ast.Rule getRule() {
		throw new UnsupportedOperationException();
	}

	public org.meta_environment.rascal.ast.Statement getStatement() {
		throw new UnsupportedOperationException();
	}
}
