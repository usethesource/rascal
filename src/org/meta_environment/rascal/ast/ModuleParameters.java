package org.meta_environment.rascal.ast;

import org.eclipse.imp.pdb.facts.ITree;

public abstract class ModuleParameters extends AbstractAST {
	static public class Ambiguity extends ModuleParameters {
		private final java.util.List<org.meta_environment.rascal.ast.ModuleParameters> alternatives;

		public Ambiguity(
				java.util.List<org.meta_environment.rascal.ast.ModuleParameters> alternatives) {
			this.alternatives = java.util.Collections
					.unmodifiableList(alternatives);
		}

		public java.util.List<org.meta_environment.rascal.ast.ModuleParameters> getAlternatives() {
			return alternatives;
		}
	}

	static public class Default extends ModuleParameters {
		private java.util.List<org.meta_environment.rascal.ast.TypeVar> parameters;

		/*
		 * "[" parameters:{TypeVar ","}+ "]" -> ModuleParameters
		 * {cons("Default")}
		 */
		private Default() {
		}

		/* package */Default(
				ITree tree,
				java.util.List<org.meta_environment.rascal.ast.TypeVar> parameters) {
			this.tree = tree;
			this.parameters = parameters;
		}

		private void $setParameters(
				java.util.List<org.meta_environment.rascal.ast.TypeVar> x) {
			this.parameters = x;
		}

		public IVisitable accept(IASTVisitor visitor) {
			return visitor.visitModuleParametersDefault(this);
		}

		@Override
		public java.util.List<org.meta_environment.rascal.ast.TypeVar> getParameters() {
			return parameters;
		}

		public Default setParameters(
				java.util.List<org.meta_environment.rascal.ast.TypeVar> x) {
			Default z = new Default();
			z.$setParameters(x);
			return z;
		}
	}

	public java.util.List<org.meta_environment.rascal.ast.TypeVar> getParameters() {
		throw new UnsupportedOperationException();
	}
}
