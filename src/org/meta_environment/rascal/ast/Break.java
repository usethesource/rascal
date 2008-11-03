package org.meta_environment.rascal.ast;

import org.eclipse.imp.pdb.facts.ITree;

public abstract class Break extends AbstractAST {
	static public class Ambiguity extends Break {
		private final java.util.List<org.meta_environment.rascal.ast.Break> alternatives;

		public Ambiguity(
				java.util.List<org.meta_environment.rascal.ast.Break> alternatives) {
			this.alternatives = java.util.Collections
					.unmodifiableList(alternatives);
		}

		public java.util.List<org.meta_environment.rascal.ast.Break> getAlternatives() {
			return alternatives;
		}
	}

	static public class NoLabel extends Break {
		/* package */NoLabel(ITree tree) {
			this.tree = tree;
		}

		public IVisitable accept(IASTVisitor visitor) {
			return visitor.visitBreakNoLabel(this);
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

		public IVisitable accept(IASTVisitor visitor) {
			return visitor.visitBreakWithLabel(this);
		}

		@Override
		public org.meta_environment.rascal.ast.Name getLabel() {
			return label;
		}

		public WithLabel setLabel(org.meta_environment.rascal.ast.Name x) {
			WithLabel z = new WithLabel();
			z.$setLabel(x);
			return z;
		}
	}

	public org.meta_environment.rascal.ast.Name getLabel() {
		throw new UnsupportedOperationException();
	}
}
