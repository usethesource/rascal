
package org.rascalmpl.ast;


import org.eclipse.imp.pdb.facts.INode;


public abstract class LocationLiteral extends AbstractAST {
  public LocationLiteral(INode node) {
    super(node);
  }
  

  public boolean hasProtocolPart() {
    return false;
  }

  public org.rascalmpl.ast.ProtocolPart getProtocolPart() {
    throw new UnsupportedOperationException();
  }

  public boolean hasPathPart() {
    return false;
  }

  public org.rascalmpl.ast.PathPart getPathPart() {
    throw new UnsupportedOperationException();
  }


static public class Ambiguity extends LocationLiteral {
  private final java.util.List<org.rascalmpl.ast.LocationLiteral> alternatives;

  public Ambiguity(INode node, java.util.List<org.rascalmpl.ast.LocationLiteral> alternatives) {
    super(node);
    this.alternatives = java.util.Collections.unmodifiableList(alternatives);
  }

  public java.util.List<org.rascalmpl.ast.LocationLiteral> getAlternatives() {
   return alternatives;
  }

  public <T> T accept(IASTVisitor<T> v) {
	return v.visitLocationLiteralAmbiguity(this);
  }
}





  public boolean isDefault() {
    return false;
  }
  
static public class Default extends LocationLiteral {
  // Production: sig("Default",[arg("org.rascalmpl.ast.ProtocolPart","protocolPart"),arg("org.rascalmpl.ast.PathPart","pathPart")])

  
     private final org.rascalmpl.ast.ProtocolPart protocolPart;
  
     private final org.rascalmpl.ast.PathPart pathPart;
  

  
public Default(INode node , org.rascalmpl.ast.ProtocolPart protocolPart,  org.rascalmpl.ast.PathPart pathPart) {
  super(node);
  
    this.protocolPart = protocolPart;
  
    this.pathPart = pathPart;
  
}


  @Override
  public boolean isDefault() { 
    return true; 
  }

  @Override
  public <T> T accept(IASTVisitor<T> visitor) {
    return visitor.visitLocationLiteralDefault(this);
  }
  
  
     @Override
     public org.rascalmpl.ast.ProtocolPart getProtocolPart() {
        return this.protocolPart;
     }
     
     @Override
     public boolean hasProtocolPart() {
        return true;
     }
  
     @Override
     public org.rascalmpl.ast.PathPart getPathPart() {
        return this.pathPart;
     }
     
     @Override
     public boolean hasPathPart() {
        return true;
     }
  	
}



}
