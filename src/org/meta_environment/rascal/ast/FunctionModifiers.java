package org.meta_environment.rascal.ast;

import org.eclipse.imp.pdb.facts.ITree;

public abstract class FunctionModifiers extends AbstractAST {
	static public class Ambiguity extends FunctionModifiers {
		private final java.util.List<org.meta_environment.rascal.ast.FunctionModifiers> alternatives;

		public Ambiguity(
				java.util.List<org.meta_environment.rascal.ast.FunctionModifiers> alternatives) {
			this.alternatives = java.util.Collections
					.unmodifiableList(alternatives);
		}

		public java.util.List<org.meta_environment.rascal.ast.FunctionModifiers> getAlternatives() {
			return alternatives;
		}
	}

	static public class List extends FunctionModifiers {
		private java.util.List<org.meta_environment.rascal.ast.FunctionModifier> modifiers;

		/* modifiers:FunctionModifier -> FunctionModifiers {cons("List")} */
		private List() {
		}

		/* package */List(
				ITree tree,
				java.util.List<org.meta_environment.rascal.ast.FunctionModifier> modifiers) {
			this.tree = tree;
			this.modifiers = modifiers;
		}

		private void $setModifiers(
				java.util.List<org.meta_environment.rascal.ast.FunctionModifier> x) {
			this.modifiers = x;
		}

		public IVisitable accept(IASTVisitor visitor) {
			return visitor.visitFunctionModifiersList(this);
		}

		@Override
		public java.util.List<org.meta_environment.rascal.ast.FunctionModifier> getModifiers() {
			return modifiers;
		}

		public List setModifiers(
				java.util.List<org.meta_environment.rascal.ast.FunctionModifier> x) {
			List z = new List();
			z.$setModifiers(x);
			return z;
		}
	}

	public java.util.List<org.meta_environment.rascal.ast.FunctionModifier> getModifiers() {
		throw new UnsupportedOperationException();
	}
}
