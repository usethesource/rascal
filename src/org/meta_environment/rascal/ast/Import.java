package org.meta_environment.rascal.ast;

import org.eclipse.imp.pdb.facts.ITree;

public abstract class Import extends AbstractAST {
	static public class Ambiguity extends Import {
		private final java.util.List<org.meta_environment.rascal.ast.Import> alternatives;

		public Ambiguity(
				java.util.List<org.meta_environment.rascal.ast.Import> alternatives) {
			this.alternatives = java.util.Collections
					.unmodifiableList(alternatives);
		}

		public java.util.List<org.meta_environment.rascal.ast.Import> getAlternatives() {
			return alternatives;
		}
	}

	static public class Default extends Import {
		private org.meta_environment.rascal.ast.ImportedModule module;

		/* "import" module:ImportedModule ";" -> Import {cons("Default")} */
		private Default() {
		}

		/* package */Default(ITree tree,
				org.meta_environment.rascal.ast.ImportedModule module) {
			this.tree = tree;
			this.module = module;
		}

		private void $setModule(org.meta_environment.rascal.ast.ImportedModule x) {
			this.module = x;
		}

		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitImportDefault(this);
		}

		@Override
		public org.meta_environment.rascal.ast.ImportedModule getModule() {
			return module;
		}

		@Override
		public boolean hasModule() {
			return true;
		}

		@Override
		public boolean isDefault() {
			return true;
		}

		public Default setModule(
				org.meta_environment.rascal.ast.ImportedModule x) {
			Default z = new Default();
			z.$setModule(x);
			return z;
		}
	}

	static public class Extend extends Import {
		private org.meta_environment.rascal.ast.ImportedModule module;

		/* "extend" module:ImportedModule ";" -> Import {cons("Extend")} */
		private Extend() {
		}

		/* package */Extend(ITree tree,
				org.meta_environment.rascal.ast.ImportedModule module) {
			this.tree = tree;
			this.module = module;
		}

		private void $setModule(org.meta_environment.rascal.ast.ImportedModule x) {
			this.module = x;
		}

		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitImportExtend(this);
		}

		@Override
		public org.meta_environment.rascal.ast.ImportedModule getModule() {
			return module;
		}

		@Override
		public boolean hasModule() {
			return true;
		}

		@Override
		public boolean isExtend() {
			return true;
		}

		public Extend setModule(org.meta_environment.rascal.ast.ImportedModule x) {
			Extend z = new Extend();
			z.$setModule(x);
			return z;
		}
	}

	public org.meta_environment.rascal.ast.ImportedModule getModule() {
		throw new UnsupportedOperationException();
	}

	public boolean hasModule() {
		return false;
	}

	public boolean isDefault() {
		return false;
	}

	public boolean isExtend() {
		return false;
	}
}
