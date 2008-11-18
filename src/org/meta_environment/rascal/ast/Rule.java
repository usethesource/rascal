package org.meta_environment.rascal.ast;

import org.eclipse.imp.pdb.facts.ITree;

public abstract class Rule extends AbstractAST {
	static public class Ambiguity extends Rule {
		private final java.util.List<org.meta_environment.rascal.ast.Rule> alternatives;

		public Ambiguity(
				java.util.List<org.meta_environment.rascal.ast.Rule> alternatives) {
			this.alternatives = java.util.Collections
					.unmodifiableList(alternatives);
		}

		public java.util.List<org.meta_environment.rascal.ast.Rule> getAlternatives() {
			return alternatives;
		}
	}

	static public class NoGuard extends Rule {
		private org.meta_environment.rascal.ast.Match match;

		/* match:Match -> Rule {cons("NoGuard")} */
		private NoGuard() {
		}

		/* package */NoGuard(ITree tree,
				org.meta_environment.rascal.ast.Match match) {
			this.tree = tree;
			this.match = match;
		}

		private void $setMatch(org.meta_environment.rascal.ast.Match x) {
			this.match = x;
		}

		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitRuleNoGuard(this);
		}

		@Override
		public org.meta_environment.rascal.ast.Match getMatch() {
			return match;
		}

		@Override
		public boolean hasMatch() {
			return true;
		}

		@Override
		public boolean isNoGuard() {
			return true;
		}

		public NoGuard setMatch(org.meta_environment.rascal.ast.Match x) {
			NoGuard z = new NoGuard();
			z.$setMatch(x);
			return z;
		}
	}

	static public class WithGuard extends Rule {
		private org.meta_environment.rascal.ast.Match match;
		private org.meta_environment.rascal.ast.Type type;

		/* "[" type:Type "]" match:Match -> Rule {cons("WithGuard")} */
		private WithGuard() {
		}

		/* package */WithGuard(ITree tree,
				org.meta_environment.rascal.ast.Type type,
				org.meta_environment.rascal.ast.Match match) {
			this.tree = tree;
			this.type = type;
			this.match = match;
		}

		private void $setMatch(org.meta_environment.rascal.ast.Match x) {
			this.match = x;
		}

		private void $setType(org.meta_environment.rascal.ast.Type x) {
			this.type = x;
		}

		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitRuleWithGuard(this);
		}

		@Override
		public org.meta_environment.rascal.ast.Match getMatch() {
			return match;
		}

		@Override
		public org.meta_environment.rascal.ast.Type getType() {
			return type;
		}

		@Override
		public boolean hasMatch() {
			return true;
		}

		@Override
		public boolean hasType() {
			return true;
		}

		@Override
		public boolean isWithGuard() {
			return true;
		}

		public WithGuard setMatch(org.meta_environment.rascal.ast.Match x) {
			WithGuard z = new WithGuard();
			z.$setMatch(x);
			return z;
		}

		public WithGuard setType(org.meta_environment.rascal.ast.Type x) {
			WithGuard z = new WithGuard();
			z.$setType(x);
			return z;
		}
	}

	public org.meta_environment.rascal.ast.Match getMatch() {
		throw new UnsupportedOperationException();
	}

	public org.meta_environment.rascal.ast.Type getType() {
		throw new UnsupportedOperationException();
	}

	public boolean hasMatch() {
		return false;
	}

	public boolean hasType() {
		return false;
	}

	public boolean isNoGuard() {
		return false;
	}

	public boolean isWithGuard() {
		return false;
	}
}
