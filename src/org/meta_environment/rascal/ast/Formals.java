package org.meta_environment.rascal.ast; 
import org.eclipse.imp.pdb.facts.INode; 
public abstract class Formals extends AbstractAST { 
public java.util.List<org.meta_environment.rascal.ast.Formal> getFormals() { throw new UnsupportedOperationException(); }
public boolean hasFormals() { return false; }
public boolean isDefault() { return false; }
static public class Default extends Formals {
/** formals:{Formal ","}* -> Formals {cons("Default")} */
	public Default(INode node, java.util.List<org.meta_environment.rascal.ast.Formal> formals) {
		this.node = node;
		this.formals = formals;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitFormalsDefault(this);
	}

	public boolean isDefault() { return true; }

	public boolean hasFormals() { return true; }

private final java.util.List<org.meta_environment.rascal.ast.Formal> formals;
	public java.util.List<org.meta_environment.rascal.ast.Formal> getFormals() { return formals; }	
}
static public class Ambiguity extends Formals {
  private final java.util.List<org.meta_environment.rascal.ast.Formals> alternatives;
  public Ambiguity(INode node, java.util.List<org.meta_environment.rascal.ast.Formals> alternatives) {
	this.alternatives = java.util.Collections.unmodifiableList(alternatives);
         this.node = node;
  }
  public java.util.List<org.meta_environment.rascal.ast.Formals> getAlternatives() {
	return alternatives;
  }
  
  public <T> T accept(IASTVisitor<T> v) {
     return v.visitFormalsAmbiguity(this);
  }
}
}