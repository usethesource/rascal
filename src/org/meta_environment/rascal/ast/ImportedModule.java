package org.meta_environment.rascal.ast; 
import org.eclipse.imp.pdb.facts.INode;
public abstract class ImportedModule extends AbstractAST { 
  public org.meta_environment.rascal.ast.QualifiedName getName() { throw new UnsupportedOperationException(); } public org.meta_environment.rascal.ast.ModuleActuals getActuals() { throw new UnsupportedOperationException(); } public org.meta_environment.rascal.ast.Renamings getRenamings() { throw new UnsupportedOperationException(); } public boolean hasName() { return false; } public boolean hasActuals() { return false; } public boolean hasRenamings() { return false; } public boolean isActualsRenaming() { return false; }
static public class ActualsRenaming extends ImportedModule {
/** name:QualifiedName actuals:ModuleActuals renamings:Renamings -> ImportedModule {cons("ActualsRenaming")} */
	private ActualsRenaming() {
		super();
	}
	public ActualsRenaming(INode node, org.meta_environment.rascal.ast.QualifiedName name, org.meta_environment.rascal.ast.ModuleActuals actuals, org.meta_environment.rascal.ast.Renamings renamings) {
		this.node = node;
		this.name = name;
		this.actuals = actuals;
		this.renamings = renamings;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitImportedModuleActualsRenaming(this);
	}

	public boolean isActualsRenaming() { return true; }

	public boolean hasName() { return true; }
	public boolean hasActuals() { return true; }
	public boolean hasRenamings() { return true; }

private org.meta_environment.rascal.ast.QualifiedName name;
	public org.meta_environment.rascal.ast.QualifiedName getName() { return name; }
	private void $setName(org.meta_environment.rascal.ast.QualifiedName x) { this.name = x; }
	public ActualsRenaming setName(org.meta_environment.rascal.ast.QualifiedName x) { 
		ActualsRenaming z = new ActualsRenaming();
 		z.$setName(x);
		return z;
	}
	private org.meta_environment.rascal.ast.ModuleActuals actuals;
	public org.meta_environment.rascal.ast.ModuleActuals getActuals() { return actuals; }
	private void $setActuals(org.meta_environment.rascal.ast.ModuleActuals x) { this.actuals = x; }
	public ActualsRenaming setActuals(org.meta_environment.rascal.ast.ModuleActuals x) { 
		ActualsRenaming z = new ActualsRenaming();
 		z.$setActuals(x);
		return z;
	}
	private org.meta_environment.rascal.ast.Renamings renamings;
	public org.meta_environment.rascal.ast.Renamings getRenamings() { return renamings; }
	private void $setRenamings(org.meta_environment.rascal.ast.Renamings x) { this.renamings = x; }
	public ActualsRenaming setRenamings(org.meta_environment.rascal.ast.Renamings x) { 
		ActualsRenaming z = new ActualsRenaming();
 		z.$setRenamings(x);
		return z;
	}	
}
static public class Ambiguity extends ImportedModule {
  private final java.util.List<org.meta_environment.rascal.ast.ImportedModule> alternatives;
  public Ambiguity(INode node, java.util.List<org.meta_environment.rascal.ast.ImportedModule> alternatives) {
	this.alternatives = java.util.Collections.unmodifiableList(alternatives);
         this.node = node;
  }
  public java.util.List<org.meta_environment.rascal.ast.ImportedModule> getAlternatives() {
	return alternatives;
  }
  
  public <T> T accept(IASTVisitor<T> v) {
     return v.visitImportedModuleAmbiguity(this);
  }
} public boolean isActuals() { return false; }
static public class Actuals extends ImportedModule {
/** name:QualifiedName actuals:ModuleActuals -> ImportedModule {cons("Actuals")} */
	private Actuals() {
		super();
	}
	public Actuals(INode node, org.meta_environment.rascal.ast.QualifiedName name, org.meta_environment.rascal.ast.ModuleActuals actuals) {
		this.node = node;
		this.name = name;
		this.actuals = actuals;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitImportedModuleActuals(this);
	}

	public boolean isActuals() { return true; }

	public boolean hasName() { return true; }
	public boolean hasActuals() { return true; }

private org.meta_environment.rascal.ast.QualifiedName name;
	public org.meta_environment.rascal.ast.QualifiedName getName() { return name; }
	private void $setName(org.meta_environment.rascal.ast.QualifiedName x) { this.name = x; }
	public Actuals setName(org.meta_environment.rascal.ast.QualifiedName x) { 
		Actuals z = new Actuals();
 		z.$setName(x);
		return z;
	}
	private org.meta_environment.rascal.ast.ModuleActuals actuals;
	public org.meta_environment.rascal.ast.ModuleActuals getActuals() { return actuals; }
	private void $setActuals(org.meta_environment.rascal.ast.ModuleActuals x) { this.actuals = x; }
	public Actuals setActuals(org.meta_environment.rascal.ast.ModuleActuals x) { 
		Actuals z = new Actuals();
 		z.$setActuals(x);
		return z;
	}	
} public abstract <T> T accept(IASTVisitor<T> visitor); public boolean isRenamings() { return false; }
static public class Renamings extends ImportedModule {
/** name:QualifiedName renamings:Renamings -> ImportedModule {cons("Renamings")} */
	private Renamings() {
		super();
	}
	public Renamings(INode node, org.meta_environment.rascal.ast.QualifiedName name, org.meta_environment.rascal.ast.Renamings renamings) {
		this.node = node;
		this.name = name;
		this.renamings = renamings;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitImportedModuleRenamings(this);
	}

	public boolean isRenamings() { return true; }

	public boolean hasName() { return true; }
	public boolean hasRenamings() { return true; }

private org.meta_environment.rascal.ast.QualifiedName name;
	public org.meta_environment.rascal.ast.QualifiedName getName() { return name; }
	private void $setName(org.meta_environment.rascal.ast.QualifiedName x) { this.name = x; }
	public Renamings setName(org.meta_environment.rascal.ast.QualifiedName x) { 
		Renamings z = new Renamings();
 		z.$setName(x);
		return z;
	}
	private org.meta_environment.rascal.ast.Renamings renamings;
	public org.meta_environment.rascal.ast.Renamings getRenamings() { return renamings; }
	private void $setRenamings(org.meta_environment.rascal.ast.Renamings x) { this.renamings = x; }
	public Renamings setRenamings(org.meta_environment.rascal.ast.Renamings x) { 
		Renamings z = new Renamings();
 		z.$setRenamings(x);
		return z;
	}	
} public boolean isDefault() { return false; }
static public class Default extends ImportedModule {
/** name:QualifiedName -> ImportedModule {cons("Default")} */
	private Default() {
		super();
	}
	public Default(INode node, org.meta_environment.rascal.ast.QualifiedName name) {
		this.node = node;
		this.name = name;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitImportedModuleDefault(this);
	}

	public boolean isDefault() { return true; }

	public boolean hasName() { return true; }

private org.meta_environment.rascal.ast.QualifiedName name;
	public org.meta_environment.rascal.ast.QualifiedName getName() { return name; }
	private void $setName(org.meta_environment.rascal.ast.QualifiedName x) { this.name = x; }
	public Default setName(org.meta_environment.rascal.ast.QualifiedName x) { 
		Default z = new Default();
 		z.$setName(x);
		return z;
	}	
}
}