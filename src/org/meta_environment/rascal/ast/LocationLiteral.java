package org.meta_environment.rascal.ast; 
import org.eclipse.imp.pdb.facts.INode; 
public abstract class LocationLiteral extends AbstractAST { 
public org.meta_environment.rascal.ast.ProtocolPart getProtocolPart() { throw new UnsupportedOperationException(); }
	public org.meta_environment.rascal.ast.PathPart getPathPart() { throw new UnsupportedOperationException(); }
public boolean hasProtocolPart() { return false; }
	public boolean hasPathPart() { return false; }
public boolean isDefault() { return false; }
static public class Default extends LocationLiteral {
/** protocolPart:ProtocolPart pathPart:PathPart -> LocationLiteral {cons("Default")} */
	private Default() {
		super();
	}
	public Default(INode node, org.meta_environment.rascal.ast.ProtocolPart protocolPart, org.meta_environment.rascal.ast.PathPart pathPart) {
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

private org.meta_environment.rascal.ast.ProtocolPart protocolPart;
	public org.meta_environment.rascal.ast.ProtocolPart getProtocolPart() { return protocolPart; }
	private void $setProtocolPart(org.meta_environment.rascal.ast.ProtocolPart x) { this.protocolPart = x; }
	public Default setProtocolPart(org.meta_environment.rascal.ast.ProtocolPart x) { 
		Default z = new Default();
 		z.$setProtocolPart(x);
		return z;
	}
	private org.meta_environment.rascal.ast.PathPart pathPart;
	public org.meta_environment.rascal.ast.PathPart getPathPart() { return pathPart; }
	private void $setPathPart(org.meta_environment.rascal.ast.PathPart x) { this.pathPart = x; }
	public Default setPathPart(org.meta_environment.rascal.ast.PathPart x) { 
		Default z = new Default();
 		z.$setPathPart(x);
		return z;
	}	
}
static public class Ambiguity extends LocationLiteral {
  private final java.util.List<org.meta_environment.rascal.ast.LocationLiteral> alternatives;
  public Ambiguity(INode node, java.util.List<org.meta_environment.rascal.ast.LocationLiteral> alternatives) {
	this.alternatives = java.util.Collections.unmodifiableList(alternatives);
         this.node = node;
  }
  public java.util.List<org.meta_environment.rascal.ast.LocationLiteral> getAlternatives() {
	return alternatives;
  }
  
  public <T> T accept(IASTVisitor<T> v) {
     return v.visitLocationLiteralAmbiguity(this);
  }
}
}