package org.rascalmpl.ast; 
import org.eclipse.imp.pdb.facts.INode; 
public abstract class ModuleParameters extends AbstractAST { 
public java.util.List<org.rascalmpl.ast.TypeVar> getParameters() { throw new UnsupportedOperationException(); }
public boolean hasParameters() { return false; }
public boolean isDefault() { return false; }
static public class Default extends ModuleParameters {
/** "[" parameters:{TypeVar ","}+ "]" -> ModuleParameters {cons("Default")} */
	protected Default(INode node, java.util.List<org.rascalmpl.ast.TypeVar> parameters) {
		this.node = node;
		this.parameters = parameters;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitModuleParametersDefault(this);
	}

	public boolean isDefault() { return true; }

	public boolean hasParameters() { return true; }

private final java.util.List<org.rascalmpl.ast.TypeVar> parameters;
	public java.util.List<org.rascalmpl.ast.TypeVar> getParameters() { return parameters; }	
}
static public class Ambiguity extends ModuleParameters {
  private final java.util.List<org.rascalmpl.ast.ModuleParameters> alternatives;
  protected Ambiguity(INode node, java.util.List<org.rascalmpl.ast.ModuleParameters> alternatives) {
	this.alternatives = java.util.Collections.unmodifiableList(alternatives);
         this.node = node;
  }
  public java.util.List<org.rascalmpl.ast.ModuleParameters> getAlternatives() {
	return alternatives;
  }
  
  public <T> T accept(IASTVisitor<T> v) {
     return v.visitModuleParametersAmbiguity(this);
  }
}
}