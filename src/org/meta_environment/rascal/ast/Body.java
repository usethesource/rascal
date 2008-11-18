package org.meta_environment.rascal.ast;

import org.eclipse.imp.pdb.facts.ITree;

public abstract class Body extends AbstractAST {
	static public class Ambiguity extends Body {
		private final java.util.List<org.meta_environment.rascal.ast.Body> alternatives;

		public Ambiguity(
				java.util.List<org.meta_environment.rascal.ast.Body> alternatives) {
			this.alternatives = java.util.Collections
					.unmodifiableList(alternatives);
		}

		public java.util.List<org.meta_environment.rascal.ast.Body> getAlternatives() {
			return alternatives;
		}
	}

	static public class Toplevels extends Body {
		private java.util.List<org.meta_environment.rascal.ast.Toplevel> toplevels;

		/* toplevels:Toplevel -> Body {cons("Toplevels")} */
		private Toplevels() {
		}

		/* package */Toplevels(
				ITree tree,
				java.util.List<org.meta_environment.rascal.ast.Toplevel> toplevels) {
			this.tree = tree;
			this.toplevels = toplevels;
		}

		private void $setToplevels(
				java.util.List<org.meta_environment.rascal.ast.Toplevel> x) {
			this.toplevels = x;
		}

		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitBodyToplevels(this);
		}

		@Override
		public java.util.List<org.meta_environment.rascal.ast.Toplevel> getToplevels() {
			return toplevels;
		}

		@Override
		public boolean hasToplevels() {
			return true;
		}

		@Override
		public boolean isToplevels() {
			return true;
		}

		public Toplevels setToplevels(
				java.util.List<org.meta_environment.rascal.ast.Toplevel> x) {
			Toplevels z = new Toplevels();
			z.$setToplevels(x);
			return z;
		}
	}

	public java.util.List<org.meta_environment.rascal.ast.Toplevel> getToplevels() {
		throw new UnsupportedOperationException();
	}

	public boolean hasToplevels() {
		return false;
	}

	public boolean isToplevels() {
		return false;
	}
}
