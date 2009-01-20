package org.meta_environment.rascal.ast;

import org.eclipse.imp.pdb.facts.ITree;

public abstract class Label extends AbstractAST {
	static public class Ambiguity extends Label {
		private final java.util.List<org.meta_environment.rascal.ast.Label> alternatives;

		public Ambiguity(
				ITree tree,
				java.util.List<org.meta_environment.rascal.ast.Label> alternatives) {
			this.alternatives = java.util.Collections
					.unmodifiableList(alternatives);
			this.tree = tree;
		}

		@Override
		public <T> T accept(IASTVisitor<T> v) {
			return v.visitLabelAmbiguity(this);
		}

		public java.util.List<org.meta_environment.rascal.ast.Label> getAlternatives() {
			return alternatives;
		}
	}

	static public class Default extends Label {
		private org.meta_environment.rascal.ast.Name name;

		/* name:Name ":" -> Label {cons("Default")} */
		private Default() {
		}

		/* package */Default(ITree tree,
				org.meta_environment.rascal.ast.Name name) {
			this.tree = tree;
			this.name = name;
		}

		private void $setName(org.meta_environment.rascal.ast.Name x) {
			this.name = x;
		}

		@Override
		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitLabelDefault(this);
		}

		@Override
		public org.meta_environment.rascal.ast.Name getName() {
			return name;
		}

		@Override
		public boolean hasName() {
			return true;
		}

		@Override
		public boolean isDefault() {
			return true;
		}

		public Default setName(org.meta_environment.rascal.ast.Name x) {
			final Default z = new Default();
			z.$setName(x);
			return z;
		}
	}

	static public class Empty extends Label {
		/* package */Empty(ITree tree) {
			this.tree = tree;
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

	@Override
	public abstract <T> T accept(IASTVisitor<T> visitor);

	public org.meta_environment.rascal.ast.Name getName() {
		throw new UnsupportedOperationException();
	}

	public boolean hasName() {
		return false;
	}

	public boolean isDefault() {
		return false;
	}

	public boolean isEmpty() {
		return false;
	}
}