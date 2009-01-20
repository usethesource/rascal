package org.meta_environment.rascal.ast;

import org.eclipse.imp.pdb.facts.ITree;

public abstract class QualifiedName extends AbstractAST {
	static public class Ambiguity extends QualifiedName {
		private final java.util.List<org.meta_environment.rascal.ast.QualifiedName> alternatives;

		public Ambiguity(
				ITree tree,
				java.util.List<org.meta_environment.rascal.ast.QualifiedName> alternatives) {
			this.alternatives = java.util.Collections
					.unmodifiableList(alternatives);
			this.tree = tree;
		}

		@Override
		public <T> T accept(IASTVisitor<T> v) {
			return v.visitQualifiedNameAmbiguity(this);
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

		@Override
		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitQualifiedNameDefault(this);
		}

		@Override
		public java.util.List<org.meta_environment.rascal.ast.Name> getNames() {
			return names;
		}

		@Override
		public boolean hasNames() {
			return true;
		}

		@Override
		public boolean isDefault() {
			return true;
		}

		public Default setNames(
				java.util.List<org.meta_environment.rascal.ast.Name> x) {
			final Default z = new Default();
			z.$setNames(x);
			return z;
		}
	}

	public java.util.List<org.meta_environment.rascal.ast.Name> getNames() {
		throw new UnsupportedOperationException();
	}

	public boolean hasNames() {
		return false;
	}

	public boolean isDefault() {
		return false;
	}
}