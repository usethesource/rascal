package org.meta_environment.rascal.ast;

import org.eclipse.imp.pdb.facts.ITree;

public abstract class LocalVariableDeclaration extends AbstractAST {
	static public class Ambiguity extends LocalVariableDeclaration {
		private final java.util.List<org.meta_environment.rascal.ast.LocalVariableDeclaration> alternatives;

		public Ambiguity(
				java.util.List<org.meta_environment.rascal.ast.LocalVariableDeclaration> alternatives) {
			this.alternatives = java.util.Collections
					.unmodifiableList(alternatives);
		}

		public java.util.List<org.meta_environment.rascal.ast.LocalVariableDeclaration> getAlternatives() {
			return alternatives;
		}
	}

	static public class Default extends LocalVariableDeclaration {
		private org.meta_environment.rascal.ast.Declarator declarator;

		/* declarator:Declarator -> LocalVariableDeclaration {cons("Default")} */
		private Default() {
		}

		/* package */Default(ITree tree,
				org.meta_environment.rascal.ast.Declarator declarator) {
			this.tree = tree;
			this.declarator = declarator;
		}

		private void $setDeclarator(org.meta_environment.rascal.ast.Declarator x) {
			this.declarator = x;
		}

		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitLocalVariableDeclarationDefault(this);
		}

		@Override
		public org.meta_environment.rascal.ast.Declarator getDeclarator() {
			return declarator;
		}

		@Override
		public boolean hasDeclarator() {
			return true;
		}

		@Override
		public boolean isDefault() {
			return true;
		}

		public Default setDeclarator(
				org.meta_environment.rascal.ast.Declarator x) {
			Default z = new Default();
			z.$setDeclarator(x);
			return z;
		}
	}

	static public class Dynamic extends LocalVariableDeclaration {
		private org.meta_environment.rascal.ast.Declarator declarator;

		/*
		 * "dynamic" declarator:Declarator -> LocalVariableDeclaration
		 * {cons("Dynamic")}
		 */
		private Dynamic() {
		}

		/* package */Dynamic(ITree tree,
				org.meta_environment.rascal.ast.Declarator declarator) {
			this.tree = tree;
			this.declarator = declarator;
		}

		private void $setDeclarator(org.meta_environment.rascal.ast.Declarator x) {
			this.declarator = x;
		}

		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitLocalVariableDeclarationDynamic(this);
		}

		@Override
		public org.meta_environment.rascal.ast.Declarator getDeclarator() {
			return declarator;
		}

		@Override
		public boolean hasDeclarator() {
			return true;
		}

		@Override
		public boolean isDynamic() {
			return true;
		}

		public Dynamic setDeclarator(
				org.meta_environment.rascal.ast.Declarator x) {
			Dynamic z = new Dynamic();
			z.$setDeclarator(x);
			return z;
		}
	}

	public org.meta_environment.rascal.ast.Declarator getDeclarator() {
		throw new UnsupportedOperationException();
	}

	public boolean hasDeclarator() {
		return false;
	}

	public boolean isDefault() {
		return false;
	}

	public boolean isDynamic() {
		return false;
	}
}
