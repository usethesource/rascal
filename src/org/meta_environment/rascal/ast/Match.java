package org.meta_environment.rascal.ast;

import org.eclipse.imp.pdb.facts.ITree;

public abstract class Match extends AbstractAST {
	static public class Ambiguity extends Match {
		private final java.util.List<org.meta_environment.rascal.ast.Match> alternatives;

		public Ambiguity(
				java.util.List<org.meta_environment.rascal.ast.Match> alternatives) {
			this.alternatives = java.util.Collections
					.unmodifiableList(alternatives);
		}

		public java.util.List<org.meta_environment.rascal.ast.Match> getAlternatives() {
			return alternatives;
		}
	}

	static public class Arbitrary extends Match {
		private org.meta_environment.rascal.ast.Expression match;
		private org.meta_environment.rascal.ast.Statement statement;

		/* match:Expression ":" statement:Statement -> Match {cons("Arbitrary")} */
		private Arbitrary() {
		}

		/* package */Arbitrary(ITree tree,
				org.meta_environment.rascal.ast.Expression match,
				org.meta_environment.rascal.ast.Statement statement) {
			this.tree = tree;
			this.match = match;
			this.statement = statement;
		}

		private void $setMatch(org.meta_environment.rascal.ast.Expression x) {
			this.match = x;
		}

		private void $setStatement(org.meta_environment.rascal.ast.Statement x) {
			this.statement = x;
		}

		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitMatchArbitrary(this);
		}

		@Override
		public org.meta_environment.rascal.ast.Expression getMatch() {
			return match;
		}

		@Override
		public org.meta_environment.rascal.ast.Statement getStatement() {
			return statement;
		}

		@Override
		public boolean hasMatch() {
			return true;
		}

		@Override
		public boolean hasStatement() {
			return true;
		}

		@Override
		public boolean isArbitrary() {
			return true;
		}

		public Arbitrary setMatch(org.meta_environment.rascal.ast.Expression x) {
			Arbitrary z = new Arbitrary();
			z.$setMatch(x);
			return z;
		}

		public Arbitrary setStatement(
				org.meta_environment.rascal.ast.Statement x) {
			Arbitrary z = new Arbitrary();
			z.$setStatement(x);
			return z;
		}
	}

	static public class Replacing extends Match {
		private org.meta_environment.rascal.ast.Expression match;
		private org.meta_environment.rascal.ast.Expression replacement;

		/*
		 * match:Expression "=>" replacement:Expression -> Match
		 * {cons("Replacing")}
		 */
		private Replacing() {
		}

		/* package */Replacing(ITree tree,
				org.meta_environment.rascal.ast.Expression match,
				org.meta_environment.rascal.ast.Expression replacement) {
			this.tree = tree;
			this.match = match;
			this.replacement = replacement;
		}

		private void $setMatch(org.meta_environment.rascal.ast.Expression x) {
			this.match = x;
		}

		private void $setReplacement(
				org.meta_environment.rascal.ast.Expression x) {
			this.replacement = x;
		}

		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitMatchReplacing(this);
		}

		@Override
		public org.meta_environment.rascal.ast.Expression getMatch() {
			return match;
		}

		@Override
		public org.meta_environment.rascal.ast.Expression getReplacement() {
			return replacement;
		}

		@Override
		public boolean hasMatch() {
			return true;
		}

		@Override
		public boolean hasReplacement() {
			return true;
		}

		@Override
		public boolean isReplacing() {
			return true;
		}

		public Replacing setMatch(org.meta_environment.rascal.ast.Expression x) {
			Replacing z = new Replacing();
			z.$setMatch(x);
			return z;
		}

		public Replacing setReplacement(
				org.meta_environment.rascal.ast.Expression x) {
			Replacing z = new Replacing();
			z.$setReplacement(x);
			return z;
		}
	}

	public org.meta_environment.rascal.ast.Expression getMatch() {
		throw new UnsupportedOperationException();
	}

	public org.meta_environment.rascal.ast.Expression getReplacement() {
		throw new UnsupportedOperationException();
	}

	public org.meta_environment.rascal.ast.Statement getStatement() {
		throw new UnsupportedOperationException();
	}

	public boolean hasMatch() {
		return false;
	}

	public boolean hasReplacement() {
		return false;
	}

	public boolean hasStatement() {
		return false;
	}

	public boolean isArbitrary() {
		return false;
	}

	public boolean isReplacing() {
		return false;
	}
}
