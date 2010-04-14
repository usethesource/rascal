package org.rascalmpl.ast; 
import org.eclipse.imp.pdb.facts.INode; 
public abstract class Body extends AbstractAST { 
  public org.rascalmpl.ast.Marker getMarker() { throw new UnsupportedOperationException(); }
	public org.rascalmpl.ast.Rest getRest() { throw new UnsupportedOperationException(); }
public boolean hasMarker() { return false; }
	public boolean hasRest() { return false; }
public boolean isAnything() { return false; }
static public class Anything extends Body {
/** marker:Marker rest:Rest -> Body {cons("Anything"), avoid} */
	public Anything(INode node, org.rascalmpl.ast.Marker marker, org.rascalmpl.ast.Rest rest) {
		this.node = node;
		this.marker = marker;
		this.rest = rest;
	}
	@Override
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitBodyAnything(this);
	}

	@Override
	public boolean isAnything() { return true; }

	@Override
	public boolean hasMarker() { return true; }
	@Override
	public boolean hasRest() { return true; }

private final org.rascalmpl.ast.Marker marker;
	@Override
	public org.rascalmpl.ast.Marker getMarker() { return marker; }
	private final org.rascalmpl.ast.Rest rest;
	@Override
	public org.rascalmpl.ast.Rest getRest() { return rest; }	
}
static public class Ambiguity extends Body {
  private final java.util.List<org.rascalmpl.ast.Body> alternatives;
  public Ambiguity(INode node, java.util.List<org.rascalmpl.ast.Body> alternatives) {
	this.alternatives = java.util.Collections.unmodifiableList(alternatives);
         this.node = node;
  }
  public java.util.List<org.rascalmpl.ast.Body> getAlternatives() {
	return alternatives;
  }
  
  @Override
public <T> T accept(IASTVisitor<T> v) {
     return v.visitBodyAmbiguity(this);
  }
} 
public java.util.List<org.rascalmpl.ast.Toplevel> getToplevels() { throw new UnsupportedOperationException(); }
public boolean hasToplevels() { return false; }
public boolean isToplevels() { return false; }
static public class Toplevels extends Body {
/** toplevels:Toplevel* -> Body {cons("Toplevels")} */
	public Toplevels(INode node, java.util.List<org.rascalmpl.ast.Toplevel> toplevels) {
		this.node = node;
		this.toplevels = toplevels;
	}
	@Override
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitBodyToplevels(this);
	}

	@Override
	public boolean isToplevels() { return true; }

	@Override
	public boolean hasToplevels() { return true; }

private final java.util.List<org.rascalmpl.ast.Toplevel> toplevels;
	@Override
	public java.util.List<org.rascalmpl.ast.Toplevel> getToplevels() { return toplevels; }	
}
 @Override
public abstract <T> T accept(IASTVisitor<T> visitor);
}