package org.meta_environment.rascal.ast;

import org.eclipse.imp.pdb.facts.INode;

public abstract class Toplevel extends AbstractAST {
	public org.meta_environment.rascal.ast.Visibility getVisibility() {
		throw new UnsupportedOperationException();
	}

	public org.meta_environment.rascal.ast.Declaration getDeclaration() {
		throw new UnsupportedOperationException();
	}

	public boolean hasVisibility() {
		return false;
	}

	public boolean hasDeclaration() {
		return false;
	}

	public boolean isGivenVisibility() {
		return false;
	}

	static public class GivenVisibility extends Toplevel {
		/*
		 * visibility:Visibility declaration:Declaration -> Toplevel
		 * {cons("GivenVisibility")}
		 */
		private GivenVisibility() {
			super();
		}

		public GivenVisibility(INode node,
				org.meta_environment.rascal.ast.Visibility visibility,
				org.meta_environment.rascal.ast.Declaration declaration) {
			this.node = node;
			this.visibility = visibility;
			this.declaration = declaration;
		}

		@Override
		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitToplevelGivenVisibility(this);
		}

		@Override
		public boolean isGivenVisibility() {
			return true;
		}

		@Override
		public boolean hasVisibility() {
			return true;
		}

		@Override
		public boolean hasDeclaration() {
			return true;
		}

		private org.meta_environment.rascal.ast.Visibility visibility;

		@Override
		public org.meta_environment.rascal.ast.Visibility getVisibility() {
			return visibility;
		}

		private void $setVisibility(org.meta_environment.rascal.ast.Visibility x) {
			this.visibility = x;
		}

		public GivenVisibility setVisibility(
				org.meta_environment.rascal.ast.Visibility x) {
			GivenVisibility z = new GivenVisibility();
			z.$setVisibility(x);
			return z;
		}

		private org.meta_environment.rascal.ast.Declaration declaration;

		@Override
		public org.meta_environment.rascal.ast.Declaration getDeclaration() {
			return declaration;
		}

		private void $setDeclaration(
				org.meta_environment.rascal.ast.Declaration x) {
			this.declaration = x;
		}

		public GivenVisibility setDeclaration(
				org.meta_environment.rascal.ast.Declaration x) {
			GivenVisibility z = new GivenVisibility();
			z.$setDeclaration(x);
			return z;
		}
	}

	static public class Ambiguity extends Toplevel {
		private final java.util.List<org.meta_environment.rascal.ast.Toplevel> alternatives;

		public Ambiguity(
				INode node,
				java.util.List<org.meta_environment.rascal.ast.Toplevel> alternatives) {
			this.alternatives = java.util.Collections
					.unmodifiableList(alternatives);
			this.node = node;
		}

		public java.util.List<org.meta_environment.rascal.ast.Toplevel> getAlternatives() {
			return alternatives;
		}

		@Override
		public <T> T accept(IASTVisitor<T> v) {
			return v.visitToplevelAmbiguity(this);
		}
	}

	public boolean isDefaultVisibility() {
		return false;
	}

	static public class DefaultVisibility extends Toplevel {
		/* declaration:Declaration -> Toplevel {cons("DefaultVisibility")} */
		private DefaultVisibility() {
			super();
		}

		public DefaultVisibility(INode node,
				org.meta_environment.rascal.ast.Declaration declaration) {
			this.node = node;
			this.declaration = declaration;
		}

		@Override
		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitToplevelDefaultVisibility(this);
		}

		@Override
		public boolean isDefaultVisibility() {
			return true;
		}

		@Override
		public boolean hasDeclaration() {
			return true;
		}

		private org.meta_environment.rascal.ast.Declaration declaration;

		@Override
		public org.meta_environment.rascal.ast.Declaration getDeclaration() {
			return declaration;
		}

		private void $setDeclaration(
				org.meta_environment.rascal.ast.Declaration x) {
			this.declaration = x;
		}

		public DefaultVisibility setDeclaration(
				org.meta_environment.rascal.ast.Declaration x) {
			DefaultVisibility z = new DefaultVisibility();
			z.$setDeclaration(x);
			return z;
		}
	}

	@Override
	public abstract <T> T accept(IASTVisitor<T> visitor);
}