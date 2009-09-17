package org.meta_environment.rascal.ast; 
import org.eclipse.imp.pdb.facts.INode; 
public abstract class Renamings extends AbstractAST { 
public java.util.List<org.meta_environment.rascal.ast.Renaming> getRenamings() { throw new UnsupportedOperationException(); }
public boolean hasRenamings() { return false; }
public boolean isDefault() { return false; }
static public class Default extends Renamings {
/** "renaming" renamings:{Renaming ","}+ -> Renamings {cons("Default")} */
	public Default(INode node, java.util.List<org.meta_environment.rascal.ast.Renaming> renamings) {
		this.node = node;
		this.renamings = renamings;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitRenamingsDefault(this);
	}

	public boolean isDefault() { return true; }

	public boolean hasRenamings() { return true; }

private final java.util.List<org.meta_environment.rascal.ast.Renaming> renamings;
	public java.util.List<org.meta_environment.rascal.ast.Renaming> getRenamings() { return renamings; }	
}
static public class Ambiguity extends Renamings {
  private final java.util.List<org.meta_environment.rascal.ast.Renamings> alternatives;
  public Ambiguity(INode node, java.util.List<org.meta_environment.rascal.ast.Renamings> alternatives) {
	this.alternatives = java.util.Collections.unmodifiableList(alternatives);
         this.node = node;
  }
  public java.util.List<org.meta_environment.rascal.ast.Renamings> getAlternatives() {
	return alternatives;
  }
  
  public <T> T accept(IASTVisitor<T> v) {
     return v.visitRenamingsAmbiguity(this);
  }
}
}