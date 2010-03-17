package org.rascalmpl.ast; 
import org.eclipse.imp.pdb.facts.INode; 
public abstract class ModuleActuals extends AbstractAST { 
public java.util.List<org.rascalmpl.ast.Type> getTypes() { throw new UnsupportedOperationException(); }
public boolean hasTypes() { return false; }
public boolean isDefault() { return false; }
static public class Default extends ModuleActuals {
/** "[" types:{Type ","}+ "]" -> ModuleActuals {cons("Default")} */
	public Default(INode node, java.util.List<org.rascalmpl.ast.Type> types) {
		this.node = node;
		this.types = types;
	}
	@Override
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitModuleActualsDefault(this);
	}

	@Override
	public boolean isDefault() { return true; }

	@Override
	public boolean hasTypes() { return true; }

private final java.util.List<org.rascalmpl.ast.Type> types;
	@Override
	public java.util.List<org.rascalmpl.ast.Type> getTypes() { return types; }	
}
static public class Ambiguity extends ModuleActuals {
  private final java.util.List<org.rascalmpl.ast.ModuleActuals> alternatives;
  public Ambiguity(INode node, java.util.List<org.rascalmpl.ast.ModuleActuals> alternatives) {
	this.alternatives = java.util.Collections.unmodifiableList(alternatives);
         this.node = node;
  }
  public java.util.List<org.rascalmpl.ast.ModuleActuals> getAlternatives() {
	return alternatives;
  }
  
  @Override
public <T> T accept(IASTVisitor<T> v) {
     return v.visitModuleActualsAmbiguity(this);
  }
}
}