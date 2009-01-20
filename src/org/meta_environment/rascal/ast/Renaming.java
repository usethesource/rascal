package org.meta_environment.rascal.ast;

import org.eclipse.imp.pdb.facts.ITree;

public abstract class Renaming extends AbstractAST {
	static public class Ambiguity extends Renaming {
		private final java.util.List<org.meta_environment.rascal.ast.Renaming> alternatives;

		public Ambiguity(
				ITree tree,
				java.util.List<org.meta_environment.rascal.ast.Renaming> alternatives) {
			this.alternatives = java.util.Collections
					.unmodifiableList(alternatives);
			this.tree = tree;
		}

		@Override
		public <T> T accept(IASTVisitor<T> v) {
			return v.visitRenamingAmbiguity(this);
		}

		public java.util.List<org.meta_environment.rascal.ast.Renaming> getAlternatives() {
			return alternatives;
		}
	}

	static public class Default extends Renaming {
		private org.meta_environment.rascal.ast.Name from;
		private org.meta_environment.rascal.ast.Name to;

		/* from:Name "=>" to:Name -> Renaming {cons("Default")} */
		private Default() {
		}

		/* package */Default(ITree tree,
				org.meta_environment.rascal.ast.Name from,
				org.meta_environment.rascal.ast.Name to) {
			this.tree = tree;
			this.from = from;
			this.to = to;
		}

		private void $setFrom(org.meta_environment.rascal.ast.Name x) {
			this.from = x;
		}

		private void $setTo(org.meta_environment.rascal.ast.Name x) {
			this.to = x;
		}

		@Override
		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitRenamingDefault(this);
		}

		@Override
		public org.meta_environment.rascal.ast.Name getFrom() {
			return from;
		}

		@Override
		public org.meta_environment.rascal.ast.Name getTo() {
			return to;
		}

		@Override
		public boolean hasFrom() {
			return true;
		}

		@Override
		public boolean hasTo() {
			return true;
		}

		@Override
		public boolean isDefault() {
			return true;
		}

		public Default setFrom(org.meta_environment.rascal.ast.Name x) {
			final Default z = new Default();
			z.$setFrom(x);
			return z;
		}

		public Default setTo(org.meta_environment.rascal.ast.Name x) {
			final Default z = new Default();
			z.$setTo(x);
			return z;
		}
	}

	public org.meta_environment.rascal.ast.Name getFrom() {
		throw new UnsupportedOperationException();
	}

	public org.meta_environment.rascal.ast.Name getTo() {
		throw new UnsupportedOperationException();
	}

	public boolean hasFrom() {
		return false;
	}

	public boolean hasTo() {
		return false;
	}

	public boolean isDefault() {
		return false;
	}
}