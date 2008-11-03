package org.meta_environment.rascal.ast;

import org.eclipse.imp.pdb.facts.ITree;

public abstract class Fail extends AbstractAST {
	static public class Ambiguity extends Fail {
		private final java.util.List<org.meta_environment.rascal.ast.Fail> alternatives;

		public Ambiguity(
				java.util.List<org.meta_environment.rascal.ast.Fail> alternatives) {
			this.alternatives = java.util.Collections
					.unmodifiableList(alternatives);
		}

		public java.util.List<org.meta_environment.rascal.ast.Fail> getAlternatives() {
			return alternatives;
		}
	}

	static public class NoLabel extends Fail {
		/* package */NoLabel(ITree tree) {
			this.tree = tree;
		}

		public IVisitable accept(IASTVisitor visitor) {
			return visitor.visitFailNoLabel(this);
		}
	}

	static public class WithLabel extends Fail {
		private org.meta_environment.rascal.ast.Name label;

		/* "fail" label:Name ";" -> Fail {cons("WithLabel")} */
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
			return visitor.visitFailWithLabel(this);
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
