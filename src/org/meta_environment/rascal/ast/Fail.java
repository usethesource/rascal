package org.meta_environment.rascal.ast;

import org.eclipse.imp.pdb.facts.INode;

public abstract class Fail extends AbstractAST {
	public org.meta_environment.rascal.ast.Name getLabel() {
		throw new UnsupportedOperationException();
	}

	public boolean hasLabel() {
		return false;
	}

	public boolean isWithLabel() {
		return false;
	}

	static public class WithLabel extends Fail {
		/** &syms -> &sort {&attr*1, cons(&strcon), &attr*2} */
		private WithLabel() {
		}

		/* package */WithLabel(INode node,
				org.meta_environment.rascal.ast.Name label) {
			this.node = node;
			this.label = label;
		}

		@Override
		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitFailWithLabel(this);
		}

		@Override
		public boolean isWithLabel() {
			return true;
		}

		@Override
		public boolean hasLabel() {
			return true;
		}

		private org.meta_environment.rascal.ast.Name label;

		@Override
		public org.meta_environment.rascal.ast.Name getLabel() {
			return label;
		}

		private void $setLabel(org.meta_environment.rascal.ast.Name x) {
			this.label = x;
		}

		public WithLabel setLabel(org.meta_environment.rascal.ast.Name x) {
			WithLabel z = new WithLabel();
			z.$setLabel(x);
			return z;
		}
	}

	static public class Ambiguity extends Fail {
		private final java.util.List<org.meta_environment.rascal.ast.Fail> alternatives;

		public Ambiguity(
				INode node,
				java.util.List<org.meta_environment.rascal.ast.Fail> alternatives) {
			this.alternatives = java.util.Collections
					.unmodifiableList(alternatives);
			this.node = node;
		}

		public java.util.List<org.meta_environment.rascal.ast.Fail> getAlternatives() {
			return alternatives;
		}

		@Override
		public <T> T accept(IASTVisitor<T> v) {
			return v.visitFailAmbiguity(this);
		}
	}

	public boolean isNoLabel() {
		return false;
	}

	static public class NoLabel extends Fail {
		/** &syms -> &sort {&attr*1, cons(&strcon), &attr*2} */
		private NoLabel() {
		}

		/* package */NoLabel(INode node) {
			this.node = node;
		}

		@Override
		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitFailNoLabel(this);
		}

		@Override
		public boolean isNoLabel() {
			return true;
		}
	}

	@Override
	public abstract <T> T accept(IASTVisitor<T> visitor);
}