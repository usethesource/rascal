package org.meta_environment.rascal.ast;

import org.eclipse.imp.pdb.facts.INode;

public abstract class Case extends AbstractAST {
	public org.meta_environment.rascal.ast.PatternAction getPatternAction() {
		throw new UnsupportedOperationException();
	}

	public boolean hasPatternAction() {
		return false;
	}

	public boolean isPatternAction() {
		return false;
	}

	static public class PatternAction extends Case {
		/* "case" patternAction:PatternAction -> Case {cons("PatternAction")} */
		private PatternAction() {
		}

		/* package */PatternAction(INode node,
				org.meta_environment.rascal.ast.PatternAction patternAction) {
			this.node = node;
			this.patternAction = patternAction;
		}

		@Override
		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitCasePatternAction(this);
		}

		@Override
		public boolean isPatternAction() {
			return true;
		}

		@Override
		public boolean hasPatternAction() {
			return true;
		}

		private org.meta_environment.rascal.ast.PatternAction patternAction;

		@Override
		public org.meta_environment.rascal.ast.PatternAction getPatternAction() {
			return patternAction;
		}

		private void $setPatternAction(
				org.meta_environment.rascal.ast.PatternAction x) {
			this.patternAction = x;
		}

		public PatternAction setPatternAction(
				org.meta_environment.rascal.ast.PatternAction x) {
			PatternAction z = new PatternAction();
			z.$setPatternAction(x);
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
		/* "default" ":" statement:Statement -> Case {cons("Default")} */
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