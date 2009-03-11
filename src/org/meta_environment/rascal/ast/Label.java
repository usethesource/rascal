package org.meta_environment.rascal.ast;

import org.eclipse.imp.pdb.facts.INode;

public abstract class Label extends AbstractAST {
	public boolean isEmpty() {
		return false;
	}

	static public class Empty extends Label {
		/** &syms -> &sort {&attr*1, cons(&strcon), &attr*2} */
		private Empty() {
		}

		/* package */Empty(INode node) {
			this.node = node;
		}

		@Override
		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitLabelEmpty(this);
		}

		@Override
		public boolean isEmpty() {
			return true;
		}
	}

	static public class Ambiguity extends Label {
		private final java.util.List<org.meta_environment.rascal.ast.Label> alternatives;

		public Ambiguity(
				INode node,
				java.util.List<org.meta_environment.rascal.ast.Label> alternatives) {
			this.alternatives = java.util.Collections
					.unmodifiableList(alternatives);
			this.node = node;
		}

		public java.util.List<org.meta_environment.rascal.ast.Label> getAlternatives() {
			return alternatives;
		}

		@Override
		public <T> T accept(IASTVisitor<T> v) {
			return v.visitLabelAmbiguity(this);
		}
	}

	public org.meta_environment.rascal.ast.Name getName() {
		throw new UnsupportedOperationException();
	}

	public boolean hasName() {
		return false;
	}

	public boolean isDefault() {
		return false;
	}

	static public class Default extends Label {
		/** &syms -> &sort {&attr*1, cons(&strcon), &attr*2} */
		private Default() {
		}

		/* package */Default(INode node,
				org.meta_environment.rascal.ast.Name name) {
			this.node = node;
			this.name = name;
		}

		@Override
		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitLabelDefault(this);
		}

		@Override
		public boolean isDefault() {
			return true;
		}

		@Override
		public boolean hasName() {
			return true;
		}

		private org.meta_environment.rascal.ast.Name name;

		@Override
		public org.meta_environment.rascal.ast.Name getName() {
			return name;
		}

		private void $setName(org.meta_environment.rascal.ast.Name x) {
			this.name = x;
		}

		public Default setName(org.meta_environment.rascal.ast.Name x) {
			Default z = new Default();
			z.$setName(x);
			return z;
		}
	}

	@Override
	public abstract <T> T accept(IASTVisitor<T> visitor);
}