package org.meta_environment.rascal.ast; 
import org.eclipse.imp.pdb.facts.INode;
public abstract class Import extends AbstractAST { 
  public org.meta_environment.rascal.ast.ImportedModule getModule() { throw new UnsupportedOperationException(); } public boolean hasModule() { return false; } public boolean isDefault() { return false; }
static public class Default extends Import {
/** "import" module:ImportedModule ";" -> Import {cons("Default")} */
	private Default() {
		super();
	}
	public Default(INode node, org.meta_environment.rascal.ast.ImportedModule module) {
		this.node = node;
		this.module = module;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitImportDefault(this);
	}

	public boolean isDefault() { return true; }

	public boolean hasModule() { return true; }

private org.meta_environment.rascal.ast.ImportedModule module;
	public org.meta_environment.rascal.ast.ImportedModule getModule() { return module; }
	private void $setModule(org.meta_environment.rascal.ast.ImportedModule x) { this.module = x; }
	public Default setModule(org.meta_environment.rascal.ast.ImportedModule x) { 
		Default z = new Default();
 		z.$setModule(x);
		return z;
	}	
}
static public class Ambiguity extends Import {
  private final java.util.List<org.meta_environment.rascal.ast.Import> alternatives;
  public Ambiguity(INode node, java.util.List<org.meta_environment.rascal.ast.Import> alternatives) {
	this.alternatives = java.util.Collections.unmodifiableList(alternatives);
         this.node = node;
  }
  public java.util.List<org.meta_environment.rascal.ast.Import> getAlternatives() {
	return alternatives;
  }
  
  public <T> T accept(IASTVisitor<T> v) {
     return v.visitImportAmbiguity(this);
  }
} public boolean isExtend() { return false; }
static public class Extend extends Import {
/** "extend" module:ImportedModule ";" -> Import {cons("Extend")} */
	private Extend() {
		super();
	}
	public Extend(INode node, org.meta_environment.rascal.ast.ImportedModule module) {
		this.node = node;
		this.module = module;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitImportExtend(this);
	}

	public boolean isExtend() { return true; }

	public boolean hasModule() { return true; }

private org.meta_environment.rascal.ast.ImportedModule module;
	public org.meta_environment.rascal.ast.ImportedModule getModule() { return module; }
	private void $setModule(org.meta_environment.rascal.ast.ImportedModule x) { this.module = x; }
	public Extend setModule(org.meta_environment.rascal.ast.ImportedModule x) { 
		Extend z = new Extend();
 		z.$setModule(x);
		return z;
	}	
}
 public abstract <T> T accept(IASTVisitor<T> visitor);
}