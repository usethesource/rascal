package org.meta_environment.rascal.ast;

import org.eclipse.imp.pdb.facts.ITree;

public abstract class Tags extends AbstractAST {
	static public class Ambiguity extends Tags {
		private final java.util.List<org.meta_environment.rascal.ast.Tags> alternatives;

		public Ambiguity(
				ITree tree,
				java.util.List<org.meta_environment.rascal.ast.Tags> alternatives) {
			this.alternatives = java.util.Collections
					.unmodifiableList(alternatives);
			this.tree = tree;
		}

		@Override
		public <T> T accept(IASTVisitor<T> v) {
			return v.visitTagsAmbiguity(this);
		}

		public java.util.List<org.meta_environment.rascal.ast.Tags> getAlternatives() {
			return alternatives;
		}
	}

	static public class Default extends Tags {
		private java.util.List<org.meta_environment.rascal.ast.Tag> annotations;

		/* annotations:Tag -> Tags {cons("Default")} */
		private Default() {
		}

		/* package */Default(ITree tree,
				java.util.List<org.meta_environment.rascal.ast.Tag> annotations) {
			this.tree = tree;
			this.annotations = annotations;
		}

		private void $setAnnotations(
				java.util.List<org.meta_environment.rascal.ast.Tag> x) {
			this.annotations = x;
		}

		@Override
		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitTagsDefault(this);
		}

		@Override
		public java.util.List<org.meta_environment.rascal.ast.Tag> getAnnotations() {
			return annotations;
		}

		@Override
		public boolean hasAnnotations() {
			return true;
		}

		@Override
		public boolean isDefault() {
			return true;
		}

		public Default setAnnotations(
				java.util.List<org.meta_environment.rascal.ast.Tag> x) {
			final Default z = new Default();
			z.$setAnnotations(x);
			return z;
		}
	}

	public java.util.List<org.meta_environment.rascal.ast.Tag> getAnnotations() {
		throw new UnsupportedOperationException();
	}

	public boolean hasAnnotations() {
		return false;
	}

	public boolean isDefault() {
		return false;
	}
}