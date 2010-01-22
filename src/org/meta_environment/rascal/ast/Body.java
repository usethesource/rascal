package org.meta_environment.rascal.ast; 
import org.eclipse.imp.pdb.facts.INode; 
public abstract class Body extends AbstractAST { 
  public org.meta_environment.rascal.ast.Marker getMarker() { throw new UnsupportedOperationException(); }
	public org.meta_environment.rascal.ast.Rest getRest() { throw new UnsupportedOperationException(); }
public boolean hasMarker() { return false; }
	public boolean hasRest() { return false; }
public boolean isAnything() { return false; }
static public class Anything extends Body {
/** marker:Marker rest:Rest -> Body {cons("Anything"), avoid} */
	public Anything(INode node, org.meta_environment.rascal.ast.Marker marker, org.meta_environment.rascal.ast.Rest rest) {
		this.node = node;
		this.marker = marker;
		this.rest = rest;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitBodyAnything(this);
	}

	public boolean isAnything() { return true; }

	public boolean hasMarker() { return true; }
	public boolean hasRest() { return true; }

private final org.meta_environment.rascal.ast.Marker marker;
	public org.meta_environment.rascal.ast.Marker getMarker() { return marker; }
	private final org.meta_environment.rascal.ast.Rest rest;
	public org.meta_environment.rascal.ast.Rest getRest() { return rest; }	
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
public java.util.List<org.meta_environment.rascal.ast.Toplevel> getToplevels() { throw new UnsupportedOperationException(); }
public boolean hasToplevels() { return false; }
public boolean isToplevels() { return false; }
static public class Toplevels extends Body {
/** toplevels:Toplevel* -> Body {cons("Toplevels")} */
	public Toplevels(INode node, java.util.List<org.meta_environment.rascal.ast.Toplevel> toplevels) {
		this.node = node;
		this.toplevels = toplevels;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitBodyToplevels(this);
	}

	public boolean isToplevels() { return true; }

	public boolean hasToplevels() { return true; }

private final java.util.List<org.meta_environment.rascal.ast.Toplevel> toplevels;
	public java.util.List<org.meta_environment.rascal.ast.Toplevel> getToplevels() { return toplevels; }	
}
 public abstract <T> T accept(IASTVisitor<T> visitor);
}