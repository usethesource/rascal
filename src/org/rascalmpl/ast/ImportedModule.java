package org.rascalmpl.ast; 
import org.eclipse.imp.pdb.facts.INode; 
public abstract class ImportedModule extends AbstractAST { 
  public org.rascalmpl.ast.QualifiedName getName() { throw new UnsupportedOperationException(); } public org.rascalmpl.ast.ModuleActuals getActuals() { throw new UnsupportedOperationException(); } public org.rascalmpl.ast.Renamings getRenamings() { throw new UnsupportedOperationException(); } public boolean hasName() { return false; } public boolean hasActuals() { return false; } public boolean hasRenamings() { return false; } public boolean isActualsRenaming() { return false; }
static public class ActualsRenaming extends ImportedModule {
/** name:QualifiedName actuals:ModuleActuals renamings:Renamings -> ImportedModule {cons("ActualsRenaming")} */
	public ActualsRenaming(INode node, org.rascalmpl.ast.QualifiedName name, org.rascalmpl.ast.ModuleActuals actuals, org.rascalmpl.ast.Renamings renamings) {
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

private final org.rascalmpl.ast.QualifiedName name;
	public org.rascalmpl.ast.QualifiedName getName() { return name; }
	private final org.rascalmpl.ast.ModuleActuals actuals;
	public org.rascalmpl.ast.ModuleActuals getActuals() { return actuals; }
	private final org.rascalmpl.ast.Renamings renamings;
	public org.rascalmpl.ast.Renamings getRenamings() { return renamings; }	
}
static public class Ambiguity extends ImportedModule {
  private final java.util.List<org.rascalmpl.ast.ImportedModule> alternatives;
  public Ambiguity(INode node, java.util.List<org.rascalmpl.ast.ImportedModule> alternatives) {
	this.alternatives = java.util.Collections.unmodifiableList(alternatives);
         this.node = node;
  }
  public java.util.List<org.rascalmpl.ast.ImportedModule> getAlternatives() {
	return alternatives;
  }
  
  public <T> T accept(IASTVisitor<T> v) {
     return v.visitImportedModuleAmbiguity(this);
  }
} public boolean isActuals() { return false; }
static public class Actuals extends ImportedModule {
/** name:QualifiedName actuals:ModuleActuals -> ImportedModule {cons("Actuals")} */
	public Actuals(INode node, org.rascalmpl.ast.QualifiedName name, org.rascalmpl.ast.ModuleActuals actuals) {
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

private final org.rascalmpl.ast.QualifiedName name;
	public org.rascalmpl.ast.QualifiedName getName() { return name; }
	private final org.rascalmpl.ast.ModuleActuals actuals;
	public org.rascalmpl.ast.ModuleActuals getActuals() { return actuals; }	
} public abstract <T> T accept(IASTVisitor<T> visitor); public boolean isRenamings() { return false; }
static public class Renamings extends ImportedModule {
/** name:QualifiedName renamings:Renamings -> ImportedModule {cons("Renamings")} */
	public Renamings(INode node, org.rascalmpl.ast.QualifiedName name, org.rascalmpl.ast.Renamings renamings) {
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

private final org.rascalmpl.ast.QualifiedName name;
	public org.rascalmpl.ast.QualifiedName getName() { return name; }
	private final org.rascalmpl.ast.Renamings renamings;
	public org.rascalmpl.ast.Renamings getRenamings() { return renamings; }	
} public boolean isDefault() { return false; }
static public class Default extends ImportedModule {
/** name:QualifiedName -> ImportedModule {cons("Default")} */
	public Default(INode node, org.rascalmpl.ast.QualifiedName name) {
		this.node = node;
		this.name = name;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitImportedModuleDefault(this);
	}

	public boolean isDefault() { return true; }

	public boolean hasName() { return true; }

private final org.rascalmpl.ast.QualifiedName name;
	public org.rascalmpl.ast.QualifiedName getName() { return name; }	
}
}