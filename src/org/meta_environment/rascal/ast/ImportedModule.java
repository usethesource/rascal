package org.meta_environment.rascal.ast;

import org.eclipse.imp.pdb.facts.ITree;

public abstract class ImportedModule extends AbstractAST {
	static public class Actuals extends ImportedModule {
		private org.meta_environment.rascal.ast.ModuleActuals actuals;
		private org.meta_environment.rascal.ast.ModuleName name;

		/*
		 * name:ModuleName actuals:ModuleActuals -> ImportedModule
		 * {cons("Actuals")}
		 */
		private Actuals() {
		}

		/* package */Actuals(ITree tree,
				org.meta_environment.rascal.ast.ModuleName name,
				org.meta_environment.rascal.ast.ModuleActuals actuals) {
			this.tree = tree;
			this.name = name;
			this.actuals = actuals;
		}

		private void $setActuals(org.meta_environment.rascal.ast.ModuleActuals x) {
			this.actuals = x;
		}

		private void $setName(org.meta_environment.rascal.ast.ModuleName x) {
			this.name = x;
		}

		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitImportedModuleActuals(this);
		}

		@Override
		public org.meta_environment.rascal.ast.ModuleActuals getActuals() {
			return actuals;
		}

		@Override
		public org.meta_environment.rascal.ast.ModuleName getName() {
			return name;
		}

		@Override
		public boolean hasActuals() {
			return true;
		}

		@Override
		public boolean hasName() {
			return true;
		}

		@Override
		public boolean isActuals() {
			return true;
		}

		public Actuals setActuals(
				org.meta_environment.rascal.ast.ModuleActuals x) {
			Actuals z = new Actuals();
			z.$setActuals(x);
			return z;
		}

		public Actuals setName(org.meta_environment.rascal.ast.ModuleName x) {
			Actuals z = new Actuals();
			z.$setName(x);
			return z;
		}
	}

	static public class ActualsRenaming extends ImportedModule {
		private org.meta_environment.rascal.ast.ModuleActuals actuals;
		private org.meta_environment.rascal.ast.ModuleName name;
		private org.meta_environment.rascal.ast.Renamings renamings;

		/*
		 * name:ModuleName actuals:ModuleActuals renamings:Renamings ->
		 * ImportedModule {cons("ActualsRenaming")}
		 */
		private ActualsRenaming() {
		}

		/* package */ActualsRenaming(ITree tree,
				org.meta_environment.rascal.ast.ModuleName name,
				org.meta_environment.rascal.ast.ModuleActuals actuals,
				org.meta_environment.rascal.ast.Renamings renamings) {
			this.tree = tree;
			this.name = name;
			this.actuals = actuals;
			this.renamings = renamings;
		}

		private void $setActuals(org.meta_environment.rascal.ast.ModuleActuals x) {
			this.actuals = x;
		}

		private void $setName(org.meta_environment.rascal.ast.ModuleName x) {
			this.name = x;
		}

		private void $setRenamings(org.meta_environment.rascal.ast.Renamings x) {
			this.renamings = x;
		}

		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitImportedModuleActualsRenaming(this);
		}

		@Override
		public org.meta_environment.rascal.ast.ModuleActuals getActuals() {
			return actuals;
		}

		@Override
		public org.meta_environment.rascal.ast.ModuleName getName() {
			return name;
		}

		@Override
		public org.meta_environment.rascal.ast.Renamings getRenamings() {
			return renamings;
		}

		@Override
		public boolean hasActuals() {
			return true;
		}

		@Override
		public boolean hasName() {
			return true;
		}

		@Override
		public boolean hasRenamings() {
			return true;
		}

		@Override
		public boolean isActualsRenaming() {
			return true;
		}

		public ActualsRenaming setActuals(
				org.meta_environment.rascal.ast.ModuleActuals x) {
			ActualsRenaming z = new ActualsRenaming();
			z.$setActuals(x);
			return z;
		}

		public ActualsRenaming setName(
				org.meta_environment.rascal.ast.ModuleName x) {
			ActualsRenaming z = new ActualsRenaming();
			z.$setName(x);
			return z;
		}

		public ActualsRenaming setRenamings(
				org.meta_environment.rascal.ast.Renamings x) {
			ActualsRenaming z = new ActualsRenaming();
			z.$setRenamings(x);
			return z;
		}
	}

	static public class Ambiguity extends ImportedModule {
		private final java.util.List<org.meta_environment.rascal.ast.ImportedModule> alternatives;

		public Ambiguity(
				java.util.List<org.meta_environment.rascal.ast.ImportedModule> alternatives) {
			this.alternatives = java.util.Collections
					.unmodifiableList(alternatives);
		}

		public java.util.List<org.meta_environment.rascal.ast.ImportedModule> getAlternatives() {
			return alternatives;
		}
	}

	static public class Default extends ImportedModule {
		private org.meta_environment.rascal.ast.ModuleName name;

		/* name:ModuleName -> ImportedModule {cons("Default")} */
		private Default() {
		}

		/* package */Default(ITree tree,
				org.meta_environment.rascal.ast.ModuleName name) {
			this.tree = tree;
			this.name = name;
		}

		private void $setName(org.meta_environment.rascal.ast.ModuleName x) {
			this.name = x;
		}

		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitImportedModuleDefault(this);
		}

		@Override
		public org.meta_environment.rascal.ast.ModuleName getName() {
			return name;
		}

		@Override
		public boolean hasName() {
			return true;
		}

		@Override
		public boolean isDefault() {
			return true;
		}

		public Default setName(org.meta_environment.rascal.ast.ModuleName x) {
			Default z = new Default();
			z.$setName(x);
			return z;
		}
	}

	static public class Renamings extends ImportedModule {
		private org.meta_environment.rascal.ast.ModuleName name;
		private org.meta_environment.rascal.ast.Renamings renamings;

		/*
		 * name:ModuleName renamings:Renamings -> ImportedModule
		 * {cons("Renamings")}
		 */
		private Renamings() {
		}

		/* package */Renamings(ITree tree,
				org.meta_environment.rascal.ast.ModuleName name,
				org.meta_environment.rascal.ast.Renamings renamings) {
			this.tree = tree;
			this.name = name;
			this.renamings = renamings;
		}

		private void $setName(org.meta_environment.rascal.ast.ModuleName x) {
			this.name = x;
		}

		private void $setRenamings(org.meta_environment.rascal.ast.Renamings x) {
			this.renamings = x;
		}

		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitImportedModuleRenamings(this);
		}

		@Override
		public org.meta_environment.rascal.ast.ModuleName getName() {
			return name;
		}

		@Override
		public org.meta_environment.rascal.ast.Renamings getRenamings() {
			return renamings;
		}

		@Override
		public boolean hasName() {
			return true;
		}

		@Override
		public boolean hasRenamings() {
			return true;
		}

		@Override
		public boolean isRenamings() {
			return true;
		}

		public Renamings setName(org.meta_environment.rascal.ast.ModuleName x) {
			Renamings z = new Renamings();
			z.$setName(x);
			return z;
		}

		public Renamings setRenamings(
				org.meta_environment.rascal.ast.Renamings x) {
			Renamings z = new Renamings();
			z.$setRenamings(x);
			return z;
		}
	}

	public org.meta_environment.rascal.ast.ModuleActuals getActuals() {
		throw new UnsupportedOperationException();
	}

	public org.meta_environment.rascal.ast.ModuleName getName() {
		throw new UnsupportedOperationException();
	}

	public org.meta_environment.rascal.ast.Renamings getRenamings() {
		throw new UnsupportedOperationException();
	}

	public boolean hasActuals() {
		return false;
	}

	public boolean hasName() {
		return false;
	}

	public boolean hasRenamings() {
		return false;
	}

	public boolean isActuals() {
		return false;
	}

	public boolean isActualsRenaming() {
		return false;
	}

	public boolean isDefault() {
		return false;
	}

	public boolean isRenamings() {
		return false;
	}
}
