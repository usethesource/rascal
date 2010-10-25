package org.rascalmpl.ast; 
import org.eclipse.imp.pdb.facts.INode; 
public abstract class LocationLiteral extends AbstractAST { 
public org.rascalmpl.ast.ProtocolPart getProtocolPart() { throw new UnsupportedOperationException(); }
	public org.rascalmpl.ast.PathPart getPathPart() { throw new UnsupportedOperationException(); }
public boolean hasProtocolPart() { return false; }
	public boolean hasPathPart() { return false; }
public boolean isDefault() { return false; }
static public class Default extends LocationLiteral {
/** protocolPart:ProtocolPart pathPart:PathPart -> LocationLiteral {cons("Default")} */
	protected Default(INode node, org.rascalmpl.ast.ProtocolPart protocolPart, org.rascalmpl.ast.PathPart pathPart) {
		this.node = node;
		this.protocolPart = protocolPart;
		this.pathPart = pathPart;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitLocationLiteralDefault(this);
	}

	public boolean isDefault() { return true; }

	public boolean hasProtocolPart() { return true; }
	public boolean hasPathPart() { return true; }

private final org.rascalmpl.ast.ProtocolPart protocolPart;
	public org.rascalmpl.ast.ProtocolPart getProtocolPart() { return protocolPart; }
	private final org.rascalmpl.ast.PathPart pathPart;
	public org.rascalmpl.ast.PathPart getPathPart() { return pathPart; }	
}
static public class Ambiguity extends LocationLiteral {
  private final java.util.List<org.rascalmpl.ast.LocationLiteral> alternatives;
  protected Ambiguity(INode node, java.util.List<org.rascalmpl.ast.LocationLiteral> alternatives) {
	this.alternatives = java.util.Collections.unmodifiableList(alternatives);
         this.node = node;
  }
  public java.util.List<org.rascalmpl.ast.LocationLiteral> getAlternatives() {
	return alternatives;
  }
  
  public <T> T accept(IASTVisitor<T> v) {
     return v.visitLocationLiteralAmbiguity(this);
  }
}
}