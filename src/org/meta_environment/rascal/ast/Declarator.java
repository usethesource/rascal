package org.meta_environment.rascal.ast;

import org.eclipse.imp.pdb.facts.ITree;

public abstract class Declarator extends AbstractAST {
	static public class Ambiguity extends Declarator {
		private final java.util.List<org.meta_environment.rascal.ast.Declarator> alternatives;

		public Ambiguity(
				ITree tree,
				java.util.List<org.meta_environment.rascal.ast.Declarator> alternatives) {
			this.alternatives = java.util.Collections
					.unmodifiableList(alternatives);
			this.tree = tree;
		}

		@Override
		public <T> T accept(IASTVisitor<T> v) {
			return v.visitDeclaratorAmbiguity(this);
		}

		public java.util.List<org.meta_environment.rascal.ast.Declarator> getAlternatives() {
			return alternatives;
		}
	}

	static public class Default extends Declarator {
		private org.meta_environment.rascal.ast.Type type;
		private java.util.List<org.meta_environment.rascal.ast.Variable> variables;

		/* type:Type variables:{Variable ","}+ -> Declarator {cons("Default")} */
		private Default() {
		}

		/* package */Default(
				ITree tree,
				org.meta_environment.rascal.ast.Type type,
				java.util.List<org.meta_environment.rascal.ast.Variable> variables) {
			this.tree = tree;
			this.type = type;
			this.variables = variables;
		}

		private void $setType(org.meta_environment.rascal.ast.Type x) {
			this.type = x;
		}

		private void $setVariables(
				java.util.List<org.meta_environment.rascal.ast.Variable> x) {
			this.variables = x;
		}

		@Override
		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitDeclaratorDefault(this);
		}

		@Override
		public org.meta_environment.rascal.ast.Type getType() {
			return type;
		}

		@Override
		public java.util.List<org.meta_environment.rascal.ast.Variable> getVariables() {
			return variables;
		}

		@Override
		public boolean hasType() {
			return true;
		}

		@Override
		public boolean hasVariables() {
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

		public Default setVariables(
				java.util.List<org.meta_environment.rascal.ast.Variable> x) {
			final Default z = new Default();
			z.$setVariables(x);
			return z;
		}
	}

	public org.meta_environment.rascal.ast.Type getType() {
		throw new UnsupportedOperationException();
	}

	public java.util.List<org.meta_environment.rascal.ast.Variable> getVariables() {
		throw new UnsupportedOperationException();
	}

	public boolean hasType() {
		return false;
	}

	public boolean hasVariables() {
		return false;
	}

	public boolean isDefault() {
		return false;
	}
}