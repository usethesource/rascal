package org.meta_environment.rascal.ast;

import org.eclipse.imp.pdb.facts.ITree;

public abstract class Variant extends AbstractAST {
	static public class Ambiguity extends Variant {
		private final java.util.List<org.meta_environment.rascal.ast.Variant> alternatives;

		public Ambiguity(
				ITree tree,
				java.util.List<org.meta_environment.rascal.ast.Variant> alternatives) {
			this.alternatives = java.util.Collections
					.unmodifiableList(alternatives);
			this.tree = tree;
		}

		@Override
		public <T> T accept(IASTVisitor<T> v) {
			return v.visitVariantAmbiguity(this);
		}

		public java.util.List<org.meta_environment.rascal.ast.Variant> getAlternatives() {
			return alternatives;
		}
	}

	static public class AnonymousConstructor extends Variant {
		private org.meta_environment.rascal.ast.Type type;
		private org.meta_environment.rascal.ast.Name name;

		/* type:Type name:Name -> Variant {cons("AnonymousConstructor")} */
		private AnonymousConstructor() {
		}

		/* package */AnonymousConstructor(ITree tree,
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
			return visitor.visitVariantAnonymousConstructor(this);
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
		public boolean isAnonymousConstructor() {
			return true;
		}

		public AnonymousConstructor setName(
				org.meta_environment.rascal.ast.Name x) {
			final AnonymousConstructor z = new AnonymousConstructor();
			z.$setName(x);
			return z;
		}

		public AnonymousConstructor setType(
				org.meta_environment.rascal.ast.Type x) {
			final AnonymousConstructor z = new AnonymousConstructor();
			z.$setType(x);
			return z;
		}
	}

	static public class NAryConstructor extends Variant {
		private org.meta_environment.rascal.ast.Name name;
		private java.util.List<org.meta_environment.rascal.ast.TypeArg> arguments;

		/*
		 * name:Name "(" arguments:{TypeArg ","} ")" -> Variant
		 * {cons("NAryConstructor")}
		 */
		private NAryConstructor() {
		}

		/* package */NAryConstructor(
				ITree tree,
				org.meta_environment.rascal.ast.Name name,
				java.util.List<org.meta_environment.rascal.ast.TypeArg> arguments) {
			this.tree = tree;
			this.name = name;
			this.arguments = arguments;
		}

		private void $setArguments(
				java.util.List<org.meta_environment.rascal.ast.TypeArg> x) {
			this.arguments = x;
		}

		private void $setName(org.meta_environment.rascal.ast.Name x) {
			this.name = x;
		}

		@Override
		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitVariantNAryConstructor(this);
		}

		@Override
		public java.util.List<org.meta_environment.rascal.ast.TypeArg> getArguments() {
			return arguments;
		}

		@Override
		public org.meta_environment.rascal.ast.Name getName() {
			return name;
		}

		@Override
		public boolean hasArguments() {
			return true;
		}

		@Override
		public boolean hasName() {
			return true;
		}

		@Override
		public boolean isNAryConstructor() {
			return true;
		}

		public NAryConstructor setArguments(
				java.util.List<org.meta_environment.rascal.ast.TypeArg> x) {
			final NAryConstructor z = new NAryConstructor();
			z.$setArguments(x);
			return z;
		}

		public NAryConstructor setName(org.meta_environment.rascal.ast.Name x) {
			final NAryConstructor z = new NAryConstructor();
			z.$setName(x);
			return z;
		}
	}

	static public class NillaryConstructor extends Variant {
		private org.meta_environment.rascal.ast.Name name;

		/* name:Name -> Variant {cons("NillaryConstructor")} */
		private NillaryConstructor() {
		}

		/* package */NillaryConstructor(ITree tree,
				org.meta_environment.rascal.ast.Name name) {
			this.tree = tree;
			this.name = name;
		}

		private void $setName(org.meta_environment.rascal.ast.Name x) {
			this.name = x;
		}

		@Override
		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitVariantNillaryConstructor(this);
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
		public boolean isNillaryConstructor() {
			return true;
		}

		public NillaryConstructor setName(org.meta_environment.rascal.ast.Name x) {
			final NillaryConstructor z = new NillaryConstructor();
			z.$setName(x);
			return z;
		}
	}

	@Override
	public abstract <T> T accept(IASTVisitor<T> visitor);

	public java.util.List<org.meta_environment.rascal.ast.TypeArg> getArguments() {
		throw new UnsupportedOperationException();
	}

	public org.meta_environment.rascal.ast.Name getName() {
		throw new UnsupportedOperationException();
	}

	public org.meta_environment.rascal.ast.Type getType() {
		throw new UnsupportedOperationException();
	}

	public boolean hasArguments() {
		return false;
	}

	public boolean hasName() {
		return false;
	}

	public boolean hasType() {
		return false;
	}

	public boolean isAnonymousConstructor() {
		return false;
	}

	public boolean isNAryConstructor() {
		return false;
	}

	public boolean isNillaryConstructor() {
		return false;
	}
}