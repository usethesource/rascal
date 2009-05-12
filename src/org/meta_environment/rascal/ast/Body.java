package org.meta_environment.rascal.ast; 
import org.eclipse.imp.pdb.facts.INode; 
public abstract class Body extends AbstractAST { 
public java.util.List<org.meta_environment.rascal.ast.Toplevel> getToplevels() { throw new UnsupportedOperationException(); }
public boolean hasToplevels() { return false; }
public boolean isToplevels() { return false; }
static public class Toplevels extends Body {
/* toplevels:Toplevel* -> Body {cons("Toplevels")} */
	private Toplevels() {
		super();
	}
	/*package*/ Toplevels(INode node, java.util.List<org.meta_environment.rascal.ast.Toplevel> toplevels) {
		this.node = node;
		this.toplevels = toplevels;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitBodyToplevels(this);
	}

	public boolean isToplevels() { return true; }

	public boolean hasToplevels() { return true; }

private java.util.List<org.meta_environment.rascal.ast.Toplevel> toplevels;
	public java.util.List<org.meta_environment.rascal.ast.Toplevel> getToplevels() { return toplevels; }
	private void $setToplevels(java.util.List<org.meta_environment.rascal.ast.Toplevel> x) { this.toplevels = x; }
	public Toplevels setToplevels(java.util.List<org.meta_environment.rascal.ast.Toplevel> x) { 
		Toplevels z = new Toplevels();
 		z.$setToplevels(x);
		return z;
	}	
}
static public class Ambiguity extends Body {
  private final java.util.List<org.meta_environment.rascal.ast.Body> alternatives;
  public Ambiguity(INode node, java.util.List<org.meta_environment.rascal.ast.Body> alternatives) {
	this.alternatives = java.util.Collections.unmodifiableList(alternatives);
         this.node = node;
  }
  public java.util.List<org.meta_environment.rascal.ast.Body> getAlternatives() {
	return alternatives;
  }
  
  public <T> T accept(IASTVisitor<T> v) {
     return v.visitBodyAmbiguity(this);
  }
}
}