package org.meta_environment.rascal.ast;

import org.eclipse.imp.pdb.facts.ITree;

public abstract class FunctionModifier extends AbstractAST {
	static public class Ambiguity extends FunctionModifier {
		private final java.util.List<org.meta_environment.rascal.ast.FunctionModifier> alternatives;

		public Ambiguity(
				ITree tree,
				java.util.List<org.meta_environment.rascal.ast.FunctionModifier> alternatives) {
			this.alternatives = java.util.Collections
					.unmodifiableList(alternatives);
			this.tree = tree;
		}

		@Override
		public <T> T accept(IASTVisitor<T> v) {
			return v.visitFunctionModifierAmbiguity(this);
		}

		public java.util.List<org.meta_environment.rascal.ast.FunctionModifier> getAlternatives() {
			return alternatives;
		}
	}

	static public class Java extends FunctionModifier {
		/* package */Java(ITree tree) {
			this.tree = tree;
		}

		@Override
		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitFunctionModifierJava(this);
		}

		@Override
		public boolean isJava() {
			return true;
		}
	}

	public boolean isJava() {
		return false;
	}
}