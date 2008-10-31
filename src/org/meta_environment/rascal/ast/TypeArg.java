package org.meta_environment.rascal.ast;

import org.eclipse.imp.pdb.facts.ITree;

public abstract class TypeArg extends AbstractAST {
	static public class Ambiguity extends TypeArg {
		private final java.util.List<org.meta_environment.rascal.ast.TypeArg> alternatives;

		public Ambiguity(
				java.util.List<org.meta_environment.rascal.ast.TypeArg> alternatives) {
			this.alternatives = java.util.Collections
					.unmodifiableList(alternatives);
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

		public IVisitable accept(IASTVisitor visitor) {
			return visitor.visitTypeArgDefault(this);
		}

		public org.meta_environment.rascal.ast.Type getType() {
			return type;
		}

		public Default setType(org.meta_environment.rascal.ast.Type x) {
			Default z = new Default();
			z.$setType(x);
			return z;
		}
	}

	static public class Named extends TypeArg {
		private org.meta_environment.rascal.ast.Name name;
		private org.meta_environment.rascal.ast.Type type;

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

		public IVisitable accept(IASTVisitor visitor) {
			return visitor.visitTypeArgNamed(this);
		}

		public org.meta_environment.rascal.ast.Name getName() {
			return name;
		}

		public org.meta_environment.rascal.ast.Type getType() {
			return type;
		}

		public Named setName(org.meta_environment.rascal.ast.Name x) {
			Named z = new Named();
			z.$setName(x);
			return z;
		}

		public Named setType(org.meta_environment.rascal.ast.Type x) {
			Named z = new Named();
			z.$setType(x);
			return z;
		}
	}
}
