
package org.rascalmpl.ast;


import org.eclipse.imp.pdb.facts.INode;


public abstract class Body extends AbstractAST {
  public Body(INode node) {
    super(node);
  }
  

  public boolean hasToplevels() {
    return false;
  }

  public java.util.List<org.rascalmpl.ast.Toplevel> getToplevels() {
    throw new UnsupportedOperationException();
  }


static public class Ambiguity extends Body {
  private final java.util.List<org.rascalmpl.ast.Body> alternatives;

  public Ambiguity(INode node, java.util.List<org.rascalmpl.ast.Body> alternatives) {
    super(node);
    this.alternatives = java.util.Collections.unmodifiableList(alternatives);
  }

  public java.util.List<org.rascalmpl.ast.Body> getAlternatives() {
   return alternatives;
  }

  public <T> T accept(IASTVisitor<T> v) {
	return v.visitBodyAmbiguity(this);
  }
}





  public boolean isToplevels() {
    return false;
  }
  
static public class Toplevels extends Body {
  // Production: sig("Toplevels",[arg("java.util.List\<org.rascalmpl.ast.Toplevel\>","toplevels")])

  
     private final java.util.List<org.rascalmpl.ast.Toplevel> toplevels;
  

  
public Toplevels(INode node , java.util.List<org.rascalmpl.ast.Toplevel> toplevels) {
  super(node);
  
    this.toplevels = toplevels;
  
}


  @Override
  public boolean isToplevels() { 
    return true; 
  }

  @Override
  public <T> T accept(IASTVisitor<T> visitor) {
    return visitor.visitBodyToplevels(this);
  }
  
  
     @Override
     public java.util.List<org.rascalmpl.ast.Toplevel> getToplevels() {
        return this.toplevels;
     }
     
     @Override
     public boolean hasToplevels() {
        return true;
     }
  	
}



}
