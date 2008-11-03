package org.meta_environment.rascal.ast;

import org.eclipse.imp.pdb.facts.ITree;

public abstract class QualifiedName extends AbstractAST {
	static public class Ambiguity extends QualifiedName {
		private final java.util.List<org.meta_environment.rascal.ast.QualifiedName> alternatives;

		public Ambiguity(
				java.util.List<org.meta_environment.rascal.ast.QualifiedName> alternatives) {
			this.alternatives = java.util.Collections
					.unmodifiableList(alternatives);
		}

		public java.util.List<org.meta_environment.rascal.ast.QualifiedName> getAlternatives() {
			return alternatives;
		}
	}

	static public class Default extends QualifiedName {
		private java.util.List<org.meta_environment.rascal.ast.Name> names;

		/* names:{Name "::"}+ -> QualifiedName {cons("Default")} */
		private Default() {
		}

		/* package */Default(ITree tree,
				java.util.List<org.meta_environment.rascal.ast.Name> names) {
			this.tree = tree;
			this.names = names;
		}

		private void $setNames(
				java.util.List<org.meta_environment.rascal.ast.Name> x) {
			this.names = x;
		}

		public IVisitable accept(IASTVisitor visitor) {
			return visitor.visitQualifiedNameDefault(this);
		}

		public java.util.List<org.meta_environment.rascal.ast.Name> getNames() {
			return names;
		}

		public Default setNames(
				java.util.List<org.meta_environment.rascal.ast.Name> x) {
			Default z = new Default();
			z.$setNames(x);
			return z;
		}
	}
}
