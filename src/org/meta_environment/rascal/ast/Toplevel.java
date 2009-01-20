package org.meta_environment.rascal.ast;

import org.eclipse.imp.pdb.facts.ITree;

public abstract class Toplevel extends AbstractAST {
	static public class Ambiguity extends Toplevel {
		private final java.util.List<org.meta_environment.rascal.ast.Toplevel> alternatives;

		public Ambiguity(
				ITree tree,
				java.util.List<org.meta_environment.rascal.ast.Toplevel> alternatives) {
			this.alternatives = java.util.Collections
					.unmodifiableList(alternatives);
			this.tree = tree;
		}

		@Override
		public <T> T accept(IASTVisitor<T> v) {
			return v.visitToplevelAmbiguity(this);
		}

		public java.util.List<org.meta_environment.rascal.ast.Toplevel> getAlternatives() {
			return alternatives;
		}
	}

	static public class DefaultVisibility extends Toplevel {
		private org.meta_environment.rascal.ast.Declaration declaration;

		/* declaration:Declaration -> Toplevel {cons("DefaultVisibility")} */
		private DefaultVisibility() {
		}

		/* package */DefaultVisibility(ITree tree,
				org.meta_environment.rascal.ast.Declaration declaration) {
			this.tree = tree;
			this.declaration = declaration;
		}

		private void $setDeclaration(
				org.meta_environment.rascal.ast.Declaration x) {
			this.declaration = x;
		}

		@Override
		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitToplevelDefaultVisibility(this);
		}

		@Override
		public org.meta_environment.rascal.ast.Declaration getDeclaration() {
			return declaration;
		}

		@Override
		public boolean hasDeclaration() {
			return true;
		}

		@Override
		public boolean isDefaultVisibility() {
			return true;
		}

		public DefaultVisibility setDeclaration(
				org.meta_environment.rascal.ast.Declaration x) {
			final DefaultVisibility z = new DefaultVisibility();
			z.$setDeclaration(x);
			return z;
		}
	}

	static public class GivenVisibility extends Toplevel {
		private org.meta_environment.rascal.ast.Visibility visibility;
		private org.meta_environment.rascal.ast.Declaration declaration;

		/*
		 * visibility:Visibility declaration:Declaration -> Toplevel
		 * {cons("GivenVisibility")}
		 */
		private GivenVisibility() {
		}

		/* package */GivenVisibility(ITree tree,
				org.meta_environment.rascal.ast.Visibility visibility,
				org.meta_environment.rascal.ast.Declaration declaration) {
			this.tree = tree;
			this.visibility = visibility;
			this.declaration = declaration;
		}

		private void $setDeclaration(
				org.meta_environment.rascal.ast.Declaration x) {
			this.declaration = x;
		}

		private void $setVisibility(org.meta_environment.rascal.ast.Visibility x) {
			this.visibility = x;
		}

		@Override
		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitToplevelGivenVisibility(this);
		}

		@Override
		public org.meta_environment.rascal.ast.Declaration getDeclaration() {
			return declaration;
		}

		@Override
		public org.meta_environment.rascal.ast.Visibility getVisibility() {
			return visibility;
		}

		@Override
		public boolean hasDeclaration() {
			return true;
		}

		@Override
		public boolean hasVisibility() {
			return true;
		}

		@Override
		public boolean isGivenVisibility() {
			return true;
		}

		public GivenVisibility setDeclaration(
				org.meta_environment.rascal.ast.Declaration x) {
			final GivenVisibility z = new GivenVisibility();
			z.$setDeclaration(x);
			return z;
		}

		public GivenVisibility setVisibility(
				org.meta_environment.rascal.ast.Visibility x) {
			final GivenVisibility z = new GivenVisibility();
			z.$setVisibility(x);
			return z;
		}
	}

	@Override
	public abstract <T> T accept(IASTVisitor<T> visitor);

	public org.meta_environment.rascal.ast.Declaration getDeclaration() {
		throw new UnsupportedOperationException();
	}

	public org.meta_environment.rascal.ast.Visibility getVisibility() {
		throw new UnsupportedOperationException();
	}

	public boolean hasDeclaration() {
		return false;
	}

	public boolean hasVisibility() {
		return false;
	}

	public boolean isDefaultVisibility() {
		return false;
	}

	public boolean isGivenVisibility() {
		return false;
	}
}