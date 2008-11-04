package org.meta_environment.rascal.ast; 
import org.eclipse.imp.pdb.facts.ITree; 
public abstract class Body extends AbstractAST { 
public java.util.List<org.meta_environment.rascal.ast.Toplevel> getToplevels() { throw new UnsupportedOperationException(); }
public boolean hasToplevels() { return false; }
public boolean isToplevels() { return false; }
static public class Toplevels extends Body {
/* toplevels:Toplevel* -> Body {cons("Toplevels")} */
	private Toplevels() { }
	/*package*/ Toplevels(ITree tree, java.util.List<org.meta_environment.rascal.ast.Toplevel> toplevels) {
		this.tree = tree;
		this.toplevels = toplevels;
	}
	public IVisitable accept(IASTVisitor visitor) {
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
  public Ambiguity(java.util.List<org.meta_environment.rascal.ast.Body> alternatives) {
	this.alternatives = java.util.Collections.unmodifiableList(alternatives);
  }
  public java.util.List<org.meta_environment.rascal.ast.Body> getAlternatives() {
	return alternatives;
  }
}
}