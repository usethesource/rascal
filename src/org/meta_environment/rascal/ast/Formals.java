package org.meta_environment.rascal.ast;

import org.eclipse.imp.pdb.facts.INode;

public abstract class Formals extends AbstractAST {
	public java.util.List<org.meta_environment.rascal.ast.Formal> getFormals() {
		throw new UnsupportedOperationException();
	}

	public boolean hasFormals() {
		return false;
	}

	public boolean isDefault() {
		return false;
	}

	static public class Default extends Formals {
		/** &syms -> &sort {&attr*1, cons(&strcon), &attr*2} */
		private Default() {
		}

		/* package */Default(INode node,
				java.util.List<org.meta_environment.rascal.ast.Formal> formals) {
			this.node = node;
			this.formals = formals;
		}

		@Override
		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitFormalsDefault(this);
		}

		@Override
		public boolean isDefault() {
			return true;
		}

		@Override
		public boolean hasFormals() {
			return true;
		}

		private java.util.List<org.meta_environment.rascal.ast.Formal> formals;

		@Override
		public java.util.List<org.meta_environment.rascal.ast.Formal> getFormals() {
			return formals;
		}

		private void $setFormals(
				java.util.List<org.meta_environment.rascal.ast.Formal> x) {
			this.formals = x;
		}

		public Default setFormals(
				java.util.List<org.meta_environment.rascal.ast.Formal> x) {
			Default z = new Default();
			z.$setFormals(x);
			return z;
		}
	}

	static public class Ambiguity extends Formals {
		private final java.util.List<org.meta_environment.rascal.ast.Formals> alternatives;

		public Ambiguity(
				INode node,
				java.util.List<org.meta_environment.rascal.ast.Formals> alternatives) {
			this.alternatives = java.util.Collections
					.unmodifiableList(alternatives);
			this.node = node;
		}

		public java.util.List<org.meta_environment.rascal.ast.Formals> getAlternatives() {
			return alternatives;
		}

		@Override
		public <T> T accept(IASTVisitor<T> v) {
			return v.visitFormalsAmbiguity(this);
		}
	}
}