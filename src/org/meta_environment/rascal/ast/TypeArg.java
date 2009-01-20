package org.meta_environment.rascal.ast;

import org.eclipse.imp.pdb.facts.ITree;

public abstract class TypeArg extends AbstractAST {
	static public class Ambiguity extends TypeArg {
		private final java.util.List<org.meta_environment.rascal.ast.TypeArg> alternatives;

		public Ambiguity(
				ITree tree,
				java.util.List<org.meta_environment.rascal.ast.TypeArg> alternatives) {
			this.alternatives = java.util.Collections
					.unmodifiableList(alternatives);
			this.tree = tree;
		}

		@Override
		public <T> T accept(IASTVisitor<T> v) {
			return v.visitTypeArgAmbiguity(this);
		}

		public java.util.List<org.meta_environment.rascal.ast.TypeArg> getAlternatives() {
			return alternatives;
		}
	}

	static public class Default extends TypeArg {
		private org.meta_environment.rascal.ast.Type type;

		/* type:Type -> TypeArg {cons("Default")} */
		private Default() {
		}

		/* package */Default(ITree tree,
				org.meta_environment.rascal.ast.Type type) {
			this.tree = tree;
			this.type = type;
		}

		private void $setType(org.meta_environment.rascal.ast.Type x) {
			this.type = x;
		}

		@Override
		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitTypeArgDefault(this);
		}

		@Override
		public org.meta_environment.rascal.ast.Type getType() {
			return type;
		}

		@Override
		public boolean hasType() {
			return true;
		}

		@Override
		public boolean isDefault() {
			return true;
		}

		public Default setType(org.meta_environment.rascal.ast.Type x) {
			final Default z = new Default();
			z.$setType(x);
			return z;
		}
	}

	static public class Named extends TypeArg {
		private org.meta_environment.rascal.ast.Type type;
		private org.meta_environment.rascal.ast.Name name;

		/* type:Type name:Name -> TypeArg {cons("Named")} */
		private Named() {
		}

		/* package */Named(ITree tree,
				org.meta_environment.rascal.ast.Type type,
				org.meta_environment.rascal.ast.Name name) {
			this.tree = tree;
			this.type = type;
			this.name = name;
		}

		private void $setName(org.meta_environment.rascal.ast.Name x) {
			this.name = x;
		}

		private void $setType(org.meta_environment.rascal.ast.Type x) {
			this.type = x;
		}

		@Override
		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitTypeArgNamed(this);
		}

		@Override
		public org.meta_environment.rascal.ast.Name getName() {
			return name;
		}

		@Override
		public org.meta_environment.rascal.ast.Type getType() {
			return type;
		}

		@Override
		public boolean hasName() {
			return true;
		}

		@Override
		public boolean hasType() {
			return true;
		}

		@Override
		public boolean isNamed() {
			return true;
		}

		public Named setName(org.meta_environment.rascal.ast.Name x) {
			final Named z = new Named();
			z.$setName(x);
			return z;
		}

		public Named setType(org.meta_environment.rascal.ast.Type x) {
			final Named z = new Named();
			z.$setType(x);
			return z;
		}
	}

	@Override
	public abstract <T> T accept(IASTVisitor<T> visitor);

	public org.meta_environment.rascal.ast.Name getName() {
		throw new UnsupportedOperationException();
	}

	public org.meta_environment.rascal.ast.Type getType() {
		throw new UnsupportedOperationException();
	}

	public boolean hasName() {
		return false;
	}

	public boolean hasType() {
		return false;
	}

	public boolean isDefault() {
		return false;
	}

	public boolean isNamed() {
		return false;
	}
}