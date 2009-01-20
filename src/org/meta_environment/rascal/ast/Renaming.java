package org.meta_environment.rascal.ast; 
import org.eclipse.imp.pdb.facts.ITree; 
public abstract class Renaming extends AbstractAST { 
public org.meta_environment.rascal.ast.Name getFrom() { throw new UnsupportedOperationException(); }
	public org.meta_environment.rascal.ast.Name getTo() { throw new UnsupportedOperationException(); }
public boolean hasFrom() { return false; }
	public boolean hasTo() { return false; }
public boolean isDefault() { return false; }
static public class Default extends Renaming {
/* from:Name "=>" to:Name -> Renaming {cons("Default")} */
	private Default() { }
	/*package*/ Default(ITree tree, org.meta_environment.rascal.ast.Name from, org.meta_environment.rascal.ast.Name to) {
		this.tree = tree;
		this.from = from;
		this.to = to;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitRenamingDefault(this);
	}

	public boolean isDefault() { return true; }

	public boolean hasFrom() { return true; }
	public boolean hasTo() { return true; }

private org.meta_environment.rascal.ast.Name from;
	public org.meta_environment.rascal.ast.Name getFrom() { return from; }
	private void $setFrom(org.meta_environment.rascal.ast.Name x) { this.from = x; }
	public Default setFrom(org.meta_environment.rascal.ast.Name x) { 
		Default z = new Default();
 		z.$setFrom(x);
		return z;
	}
	private org.meta_environment.rascal.ast.Name to;
	public org.meta_environment.rascal.ast.Name getTo() { return to; }
	private void $setTo(org.meta_environment.rascal.ast.Name x) { this.to = x; }
	public Default setTo(org.meta_environment.rascal.ast.Name x) { 
		Default z = new Default();
 		z.$setTo(x);
		return z;
	}	
}
static public class Ambiguity extends Renaming {
  private final java.util.List<org.meta_environment.rascal.ast.Renaming> alternatives;
  public Ambiguity(ITree tree, java.util.List<org.meta_environment.rascal.ast.Renaming> alternatives) {
	this.alternatives = java.util.Collections.unmodifiableList(alternatives);
         this.tree = tree;
  }
  public java.util.List<org.meta_environment.rascal.ast.Renaming> getAlternatives() {
	return alternatives;
  }
  
  public <T> T accept(IASTVisitor<T> v) {
     return v.visitRenamingAmbiguity(this);
  }
}
}