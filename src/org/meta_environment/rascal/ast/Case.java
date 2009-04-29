package org.meta_environment.rascal.ast;

import org.eclipse.imp.pdb.facts.INode;

public abstract class Case extends AbstractAST {
	public org.meta_environment.rascal.ast.PatternWithAction getPatternWithAction() {
		throw new UnsupportedOperationException();
	}

	public boolean hasPatternWithAction() {
		return false;
	}

	public boolean isPatternWithAction() {
		return false;
	}

	static public class PatternWithAction extends Case {
		/*
		 * "case" patternWithAction:PatternWithAction -> Case
		 * {cons("PatternWithAction")}
		 */
		private PatternWithAction() {
		}

		/* package */PatternWithAction(
				INode node,
				org.meta_environment.rascal.ast.PatternWithAction patternWithAction) {
			this.node = node;
			this.patternWithAction = patternWithAction;
		}

		@Override
		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitCasePatternWithAction(this);
		}

		@Override
		public boolean isPatternWithAction() {
			return true;
		}

		@Override
		public boolean hasPatternWithAction() {
			return true;
		}

		private org.meta_environment.rascal.ast.PatternWithAction patternWithAction;

		@Override
		public org.meta_environment.rascal.ast.PatternWithAction getPatternWithAction() {
			return patternWithAction;
		}

		private void $setPatternWithAction(
				org.meta_environment.rascal.ast.PatternWithAction x) {
			this.patternWithAction = x;
		}

		public PatternWithAction setPatternWithAction(
				org.meta_environment.rascal.ast.PatternWithAction x) {
			PatternWithAction z = new PatternWithAction();
			z.$setPatternWithAction(x);
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