package org.meta_environment.rascal.ast;

import org.eclipse.imp.pdb.facts.ITree;

public abstract class Tag extends AbstractAST {
	static public class Ambiguity extends Tag {
		private final java.util.List<org.meta_environment.rascal.ast.Tag> alternatives;

		public Ambiguity(ITree tree,
				java.util.List<org.meta_environment.rascal.ast.Tag> alternatives) {
			this.alternatives = java.util.Collections
					.unmodifiableList(alternatives);
			this.tree = tree;
		}

		@Override
		public <T> T accept(IASTVisitor<T> v) {
			return v.visitTagAmbiguity(this);
		}

		public java.util.List<org.meta_environment.rascal.ast.Tag> getAlternatives() {
			return alternatives;
		}
	}

	static public class Default extends Tag {
		private org.meta_environment.rascal.ast.Name name;
		private org.meta_environment.rascal.ast.TagString contents;

		/* "@" name:Name contents:TagString -> Tag {cons("Default")} */
		private Default() {
		}

		/* package */Default(ITree tree,
				org.meta_environment.rascal.ast.Name name,
				org.meta_environment.rascal.ast.TagString contents) {
			this.tree = tree;
			this.name = name;
			this.contents = contents;
		}

		private void $setContents(org.meta_environment.rascal.ast.TagString x) {
			this.contents = x;
		}

		private void $setName(org.meta_environment.rascal.ast.Name x) {
			this.name = x;
		}

		@Override
		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitTagDefault(this);
		}

		@Override
		public org.meta_environment.rascal.ast.TagString getContents() {
			return contents;
		}

		@Override
		public org.meta_environment.rascal.ast.Name getName() {
			return name;
		}

		@Override
		public boolean hasContents() {
			return true;
		}

		@Override
		public boolean hasName() {
			return true;
		}

		@Override
		public boolean isDefault() {
			return true;
		}

		public Default setContents(org.meta_environment.rascal.ast.TagString x) {
			final Default z = new Default();
			z.$setContents(x);
			return z;
		}

		public Default setName(org.meta_environment.rascal.ast.Name x) {
			final Default z = new Default();
			z.$setName(x);
			return z;
		}
	}

	public org.meta_environment.rascal.ast.TagString getContents() {
		throw new UnsupportedOperationException();
	}

	public org.meta_environment.rascal.ast.Name getName() {
		throw new UnsupportedOperationException();
	}

	public boolean hasContents() {
		return false;
	}

	public boolean hasName() {
		return false;
	}

	public boolean isDefault() {
		return false;
	}
}