package org.meta_environment.rascal.ast;

import org.eclipse.imp.pdb.facts.INode;

public abstract class Renamings extends AbstractAST {
	public java.util.List<org.meta_environment.rascal.ast.Renaming> getRenamings() {
		throw new UnsupportedOperationException();
	}

	public boolean hasRenamings() {
		return false;
	}

	public boolean isDefault() {
		return false;
	}

	static public class Default extends Renamings {
		/** &syms -> &sort {&attr*1, cons(&strcon), &attr*2} */
		private Default() {
		}

		/* package */Default(
				INode node,
				java.util.List<org.meta_environment.rascal.ast.Renaming> renamings) {
			this.node = node;
			this.renamings = renamings;
		}

		@Override
		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitRenamingsDefault(this);
		}

		@Override
		public boolean isDefault() {
			return true;
		}

		@Override
		public boolean hasRenamings() {
			return true;
		}

		private java.util.List<org.meta_environment.rascal.ast.Renaming> renamings;

		@Override
		public java.util.List<org.meta_environment.rascal.ast.Renaming> getRenamings() {
			return renamings;
		}

		private void $setRenamings(
				java.util.List<org.meta_environment.rascal.ast.Renaming> x) {
			this.renamings = x;
		}

		public Default setRenamings(
				java.util.List<org.meta_environment.rascal.ast.Renaming> x) {
			Default z = new Default();
			z.$setRenamings(x);
			return z;
		}
	}

	static public class Ambiguity extends Renamings {
		private final java.util.List<org.meta_environment.rascal.ast.Renamings> alternatives;

		public Ambiguity(
				INode node,
				java.util.List<org.meta_environment.rascal.ast.Renamings> alternatives) {
			this.alternatives = java.util.Collections
					.unmodifiableList(alternatives);
			this.node = node;
		}

		public java.util.List<org.meta_environment.rascal.ast.Renamings> getAlternatives() {
			return alternatives;
		}

		@Override
		public <T> T accept(IASTVisitor<T> v) {
			return v.visitRenamingsAmbiguity(this);
		}
	}
}