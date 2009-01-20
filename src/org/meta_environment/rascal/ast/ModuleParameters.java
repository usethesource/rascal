package org.meta_environment.rascal.ast;

import org.eclipse.imp.pdb.facts.ITree;

public abstract class ModuleParameters extends AbstractAST {
	static public class Ambiguity extends ModuleParameters {
		private final java.util.List<org.meta_environment.rascal.ast.ModuleParameters> alternatives;

		public Ambiguity(
				ITree tree,
				java.util.List<org.meta_environment.rascal.ast.ModuleParameters> alternatives) {
			this.alternatives = java.util.Collections
					.unmodifiableList(alternatives);
			this.tree = tree;
		}

		@Override
		public <T> T accept(IASTVisitor<T> v) {
			return v.visitModuleParametersAmbiguity(this);
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

		@Override
		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitModuleParametersDefault(this);
		}

		@Override
		public java.util.List<org.meta_environment.rascal.ast.TypeVar> getParameters() {
			return parameters;
		}

		@Override
		public boolean hasParameters() {
			return true;
		}

		@Override
		public boolean isDefault() {
			return true;
		}

		public Default setParameters(
				java.util.List<org.meta_environment.rascal.ast.TypeVar> x) {
			final Default z = new Default();
			z.$setParameters(x);
			return z;
		}
	}

	public java.util.List<org.meta_environment.rascal.ast.TypeVar> getParameters() {
		throw new UnsupportedOperationException();
	}

	public boolean hasParameters() {
		return false;
	}

	public boolean isDefault() {
		return false;
	}
}