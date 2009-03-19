package org.meta_environment.rascal.ast;

import org.eclipse.imp.pdb.facts.INode;

public abstract class TypeArg extends AbstractAST {
	public org.meta_environment.rascal.ast.Type getType() {
		throw new UnsupportedOperationException();
	}

	public boolean hasType() {
		return false;
	}

	public boolean isDefault() {
		return false;
	}

	static public class Default extends TypeArg {
		/* type:Type -> TypeArg {cons("Default")} */
		private Default() {
		}

		/* package */Default(INode node,
				org.meta_environment.rascal.ast.Type type) {
			this.node = node;
			this.type = type;
		}

		@Override
		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitTypeArgDefault(this);
		}

		@Override
		public boolean isDefault() {
			return true;
		}

		@Override
		public boolean hasType() {
			return true;
		}

		private org.meta_environment.rascal.ast.Type type;

		@Override
		public org.meta_environment.rascal.ast.Type getType() {
			return type;
		}

		private void $setType(org.meta_environment.rascal.ast.Type x) {
			this.type = x;
		}

		public Default setType(org.meta_environment.rascal.ast.Type x) {
			Default z = new Default();
			z.$setType(x);
			return z;
		}
	}

	static public class Ambiguity extends TypeArg {
		private final java.util.List<org.meta_environment.rascal.ast.TypeArg> alternatives;

		public Ambiguity(
				INode node,
				java.util.List<org.meta_environment.rascal.ast.TypeArg> alternatives) {
			this.alternatives = java.util.Collections
					.unmodifiableList(alternatives);
			this.node = node;
		}

		public java.util.List<org.meta_environment.rascal.ast.TypeArg> getAlternatives() {
			return alternatives;
		}

		@Override
		public <T> T accept(IASTVisitor<T> v) {
			return v.visitTypeArgAmbiguity(this);
		}
	}

	public org.meta_environment.rascal.ast.Name getName() {
		throw new UnsupportedOperationException();
	}

	public boolean hasName() {
		return false;
	}

	public boolean isNamed() {
		return false;
	}

	static public class Named extends TypeArg {
		/* type:Type name:Name -> TypeArg {cons("Named")} */
		private Named() {
		}

		/* package */Named(INode node,
				org.meta_environment.rascal.ast.Type type,
				org.meta_environment.rascal.ast.Name name) {
			this.node = node;
			this.type = type;
			this.name = name;
		}

		@Override
		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitTypeArgNamed(this);
		}

		@Override
		public boolean isNamed() {
			return true;
		}

		@Override
		public boolean hasType() {
			return true;
		}

		@Override
		public boolean hasName() {
			return true;
		}

		private org.meta_environment.rascal.ast.Type type;

		@Override
		public org.meta_environment.rascal.ast.Type getType() {
			return type;
		}

		private void $setType(org.meta_environment.rascal.ast.Type x) {
			this.type = x;
		}

		public Named setType(org.meta_environment.rascal.ast.Type x) {
			Named z = new Named();
			z.$setType(x);
			return z;
		}

		private org.meta_environment.rascal.ast.Name name;

		@Override
		public org.meta_environment.rascal.ast.Name getName() {
			return name;
		}

		private void $setName(org.meta_environment.rascal.ast.Name x) {
			this.name = x;
		}

		public Named setName(org.meta_environment.rascal.ast.Name x) {
			Named z = new Named();
			z.$setName(x);
			return z;
		}
	}

	@Override
	public abstract <T> T accept(IASTVisitor<T> visitor);
}