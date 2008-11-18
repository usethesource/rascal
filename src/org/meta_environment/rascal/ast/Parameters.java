package org.meta_environment.rascal.ast;

import org.eclipse.imp.pdb.facts.ITree;

public abstract class Parameters extends AbstractAST {
	static public class Ambiguity extends Parameters {
		private final java.util.List<org.meta_environment.rascal.ast.Parameters> alternatives;

		public Ambiguity(
				java.util.List<org.meta_environment.rascal.ast.Parameters> alternatives) {
			this.alternatives = java.util.Collections
					.unmodifiableList(alternatives);
		}

		public java.util.List<org.meta_environment.rascal.ast.Parameters> getAlternatives() {
			return alternatives;
		}
	}

	static public class Default extends Parameters {
		private org.meta_environment.rascal.ast.Formals formals;

		/* "(" formals:Formals ")" -> Parameters {cons("Default")} */
		private Default() {
		}

		/* package */Default(ITree tree,
				org.meta_environment.rascal.ast.Formals formals) {
			this.tree = tree;
			this.formals = formals;
		}

		private void $setFormals(org.meta_environment.rascal.ast.Formals x) {
			this.formals = x;
		}

		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitParametersDefault(this);
		}

		@Override
		public org.meta_environment.rascal.ast.Formals getFormals() {
			return formals;
		}

		@Override
		public boolean hasFormals() {
			return true;
		}

		@Override
		public boolean isDefault() {
			return true;
		}

		public Default setFormals(org.meta_environment.rascal.ast.Formals x) {
			Default z = new Default();
			z.$setFormals(x);
			return z;
		}
	}

	static public class VarArgs extends Parameters {
		private org.meta_environment.rascal.ast.Formals formals;

		/* "(" formals:Formals "..." ")" -> Parameters {cons("VarArgs")} */
		private VarArgs() {
		}

		/* package */VarArgs(ITree tree,
				org.meta_environment.rascal.ast.Formals formals) {
			this.tree = tree;
			this.formals = formals;
		}

		private void $setFormals(org.meta_environment.rascal.ast.Formals x) {
			this.formals = x;
		}

		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitParametersVarArgs(this);
		}

		@Override
		public org.meta_environment.rascal.ast.Formals getFormals() {
			return formals;
		}

		@Override
		public boolean hasFormals() {
			return true;
		}

		@Override
		public boolean isVarArgs() {
			return true;
		}

		public VarArgs setFormals(org.meta_environment.rascal.ast.Formals x) {
			VarArgs z = new VarArgs();
			z.$setFormals(x);
			return z;
		}
	}

	public org.meta_environment.rascal.ast.Formals getFormals() {
		throw new UnsupportedOperationException();
	}

	public boolean hasFormals() {
		return false;
	}

	public boolean isDefault() {
		return false;
	}

	public boolean isVarArgs() {
		return false;
	}
}
