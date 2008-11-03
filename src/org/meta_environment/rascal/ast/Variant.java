package org.meta_environment.rascal.ast;

import org.eclipse.imp.pdb.facts.ITree;

public abstract class Variant extends AbstractAST {
	static public class Ambiguity extends Variant {
		private final java.util.List<org.meta_environment.rascal.ast.Variant> alternatives;

		public Ambiguity(
				java.util.List<org.meta_environment.rascal.ast.Variant> alternatives) {
			this.alternatives = java.util.Collections
					.unmodifiableList(alternatives);
		}

		public java.util.List<org.meta_environment.rascal.ast.Variant> getAlternatives() {
			return alternatives;
		}
	}

	static public class AnonymousConstructor extends Variant {
		private org.meta_environment.rascal.ast.Name name;
		private org.meta_environment.rascal.ast.Type type;

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

		public IVisitable accept(IASTVisitor visitor) {
			return visitor.visitVariantAnonymousConstructor(this);
		}

		public org.meta_environment.rascal.ast.Name getName() {
			return name;
		}

		public org.meta_environment.rascal.ast.Type getType() {
			return type;
		}

		public AnonymousConstructor setName(
				org.meta_environment.rascal.ast.Name x) {
			AnonymousConstructor z = new AnonymousConstructor();
			z.$setName(x);
			return z;
		}

		public AnonymousConstructor setType(
				org.meta_environment.rascal.ast.Type x) {
			AnonymousConstructor z = new AnonymousConstructor();
			z.$setType(x);
			return z;
		}
	}

	static public class NAryConstructor extends Variant {
		private java.util.List<org.meta_environment.rascal.ast.TypeArg> arguments;
		private org.meta_environment.rascal.ast.Name name;

		/*
		 * name:Name "(" arguments:{TypeArg ","}+ ")" -> Variant
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

		public IVisitable accept(IASTVisitor visitor) {
			return visitor.visitVariantNAryConstructor(this);
		}

		public java.util.List<org.meta_environment.rascal.ast.TypeArg> getArguments() {
			return arguments;
		}

		public org.meta_environment.rascal.ast.Name getName() {
			return name;
		}

		public NAryConstructor setArguments(
				java.util.List<org.meta_environment.rascal.ast.TypeArg> x) {
			NAryConstructor z = new NAryConstructor();
			z.$setArguments(x);
			return z;
		}

		public NAryConstructor setName(org.meta_environment.rascal.ast.Name x) {
			NAryConstructor z = new NAryConstructor();
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

		public IVisitable accept(IASTVisitor visitor) {
			return visitor.visitVariantNillaryConstructor(this);
		}

		public org.meta_environment.rascal.ast.Name getName() {
			return name;
		}

		public NillaryConstructor setName(org.meta_environment.rascal.ast.Name x) {
			NillaryConstructor z = new NillaryConstructor();
			z.$setName(x);
			return z;
		}
	}
}
