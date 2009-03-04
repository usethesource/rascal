package org.meta_environment.rascal.ast; 
import org.eclipse.imp.pdb.facts.INode; 
public abstract class Protocol extends AbstractAST { 
public org.meta_environment.rascal.ast.ProtocolLiteral getProtocolLiteral() { throw new UnsupportedOperationException(); }
public boolean hasProtocolLiteral() { return false; }
public boolean isDefault() { return false; }
static public class Default extends Protocol {
/* protocolLiteral:ProtocolLiteral -> Protocol {cons("Default")} */
	private Default() { }
	/*package*/ Default(INode node, org.meta_environment.rascal.ast.ProtocolLiteral protocolLiteral) {
		this.node = node;
		this.protocolLiteral = protocolLiteral;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitProtocolDefault(this);
	}

	public boolean isDefault() { return true; }

	public boolean hasProtocolLiteral() { return true; }

private org.meta_environment.rascal.ast.ProtocolLiteral protocolLiteral;
	public org.meta_environment.rascal.ast.ProtocolLiteral getProtocolLiteral() { return protocolLiteral; }
	private void $setProtocolLiteral(org.meta_environment.rascal.ast.ProtocolLiteral x) { this.protocolLiteral = x; }
	public Default setProtocolLiteral(org.meta_environment.rascal.ast.ProtocolLiteral x) { 
		Default z = new Default();
 		z.$setProtocolLiteral(x);
		return z;
	}	
}
static public class Ambiguity extends Protocol {
  private final java.util.List<org.meta_environment.rascal.ast.Protocol> alternatives;
  public Ambiguity(INode node, java.util.List<org.meta_environment.rascal.ast.Protocol> alternatives) {
	this.alternatives = java.util.Collections.unmodifiableList(alternatives);
         this.node = node;
  }
  public java.util.List<org.meta_environment.rascal.ast.Protocol> getAlternatives() {
	return alternatives;
  }
  
  public <T> T accept(IASTVisitor<T> v) {
     return v.visitProtocolAmbiguity(this);
  }
}
}