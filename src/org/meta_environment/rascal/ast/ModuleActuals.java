package org.meta_environment.rascal.ast;

import org.eclipse.imp.pdb.facts.ITree;

public abstract class ModuleActuals extends AbstractAST {
	static public class Ambiguity extends ModuleActuals {
		private final java.util.List<org.meta_environment.rascal.ast.ModuleActuals> alternatives;

		public Ambiguity(
				java.util.List<org.meta_environment.rascal.ast.ModuleActuals> alternatives) {
			this.alternatives = java.util.Collections
					.unmodifiableList(alternatives);
		}

		public java.util.List<org.meta_environment.rascal.ast.ModuleActuals> getAlternatives() {
			return alternatives;
		}
	}

	static public class Default extends ModuleActuals {
		private java.util.List<org.meta_environment.rascal.ast.Type> types;

		/* "[" types:{Type ","}+ "]" -> ModuleActuals {cons("Default")} */
		private Default() {
		}

		/* package */Default(ITree tree,
				java.util.List<org.meta_environment.rascal.ast.Type> types) {
			this.tree = tree;
			this.types = types;
		}

		private void $setTypes(
				java.util.List<org.meta_environment.rascal.ast.Type> x) {
			this.types = x;
		}

		public IVisitable accept(IASTVisitor visitor) {
			return visitor.visitModuleActualsDefault(this);
		}

		public java.util.List<org.meta_environment.rascal.ast.Type> getTypes() {
			return types;
		}

		public Default setTypes(
				java.util.List<org.meta_environment.rascal.ast.Type> x) {
			Default z = new Default();
			z.$setTypes(x);
			return z;
		}
	}
}
