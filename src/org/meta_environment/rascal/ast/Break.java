package org.meta_environment.rascal.ast;

import org.eclipse.imp.pdb.facts.ITree;

public abstract class Break extends AbstractAST {
	static public class Ambiguity extends Break {
		private final java.util.List<org.meta_environment.rascal.ast.Break> alternatives;

		public Ambiguity(
				ITree tree,
				java.util.List<org.meta_environment.rascal.ast.Break> alternatives) {
			this.alternatives = java.util.Collections
					.unmodifiableList(alternatives);
			this.tree = tree;
		}

		@Override
		public <T> T accept(IASTVisitor<T> v) {
			return v.visitBreakAmbiguity(this);
		}

		public java.util.List<org.meta_environment.rascal.ast.Break> getAlternatives() {
			return alternatives;
		}
	}

	static public class NoLabel extends Break {
		/* package */NoLabel(ITree tree) {
			this.tree = tree;
		}

		@Override
		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitBreakNoLabel(this);
		}

		@Override
		public boolean isNoLabel() {
			return true;
		}
	}

	static public class WithLabel extends Break {
		private org.meta_environment.rascal.ast.Name label;

		/* "break" label:Name ";" -> Break {cons("WithLabel")} */
		private WithLabel() {
		}

		/* package */WithLabel(ITree tree,
				org.meta_environment.rascal.ast.Name label) {
			this.tree = tree;
			this.label = label;
		}

		private void $setLabel(org.meta_environment.rascal.ast.Name x) {
			this.label = x;
		}

		@Override
		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitBreakWithLabel(this);
		}

		@Override
		public org.meta_environment.rascal.ast.Name getLabel() {
			return label;
		}

		@Override
		public boolean hasLabel() {
			return true;
		}

		@Override
		public boolean isWithLabel() {
			return true;
		}

		public WithLabel setLabel(org.meta_environment.rascal.ast.Name x) {
			final WithLabel z = new WithLabel();
			z.$setLabel(x);
			return z;
		}
	}

	@Override
	public abstract <T> T accept(IASTVisitor<T> visitor);

	public org.meta_environment.rascal.ast.Name getLabel() {
		throw new UnsupportedOperationException();
	}

	public boolean hasLabel() {
		return false;
	}

	public boolean isNoLabel() {
		return false;
	}

	public boolean isWithLabel() {
		return false;
	}
}