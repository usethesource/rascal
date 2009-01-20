package org.meta_environment.rascal.ast; 
import org.eclipse.imp.pdb.facts.ITree; 
public abstract class Mapping extends AbstractAST { 
public org.meta_environment.rascal.ast.Expression getFrom() { throw new UnsupportedOperationException(); }
	public org.meta_environment.rascal.ast.Expression getTo() { throw new UnsupportedOperationException(); }
public boolean hasFrom() { return false; }
	public boolean hasTo() { return false; }
public boolean isDefault() { return false; }
static public class Default extends Mapping {
/* from:Expression ":" to:Expression -> Mapping {cons("Default")} */
	private Default() { }
	/*package*/ Default(ITree tree, org.meta_environment.rascal.ast.Expression from, org.meta_environment.rascal.ast.Expression to) {
		this.tree = tree;
		this.from = from;
		this.to = to;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitMappingDefault(this);
	}

	public boolean isDefault() { return true; }

	public boolean hasFrom() { return true; }
	public boolean hasTo() { return true; }

private org.meta_environment.rascal.ast.Expression from;
	public org.meta_environment.rascal.ast.Expression getFrom() { return from; }
	private void $setFrom(org.meta_environment.rascal.ast.Expression x) { this.from = x; }
	public Default setFrom(org.meta_environment.rascal.ast.Expression x) { 
		Default z = new Default();
 		z.$setFrom(x);
		return z;
	}
	private org.meta_environment.rascal.ast.Expression to;
	public org.meta_environment.rascal.ast.Expression getTo() { return to; }
	private void $setTo(org.meta_environment.rascal.ast.Expression x) { this.to = x; }
	public Default setTo(org.meta_environment.rascal.ast.Expression x) { 
		Default z = new Default();
 		z.$setTo(x);
		return z;
	}	
}
static public class Ambiguity extends Mapping {
  private final java.util.List<org.meta_environment.rascal.ast.Mapping> alternatives;
  public Ambiguity(ITree tree, java.util.List<org.meta_environment.rascal.ast.Mapping> alternatives) {
	this.alternatives = java.util.Collections.unmodifiableList(alternatives);
         this.tree = tree;
  }
  public java.util.List<org.meta_environment.rascal.ast.Mapping> getAlternatives() {
	return alternatives;
  }
  
  public <T> T accept(IASTVisitor<T> v) {
     return v.visitMappingAmbiguity(this);
  }
}
}