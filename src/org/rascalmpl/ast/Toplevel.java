
package org.rascalmpl.ast;


import org.eclipse.imp.pdb.facts.INode;


public abstract class Toplevel extends AbstractAST {
  public Toplevel(INode node) {
    super(node);
  }
  

  public boolean hasDeclaration() {
    return false;
  }

  public org.rascalmpl.ast.Declaration getDeclaration() {
    throw new UnsupportedOperationException();
  }


static public class Ambiguity extends Toplevel {
  private final java.util.List<org.rascalmpl.ast.Toplevel> alternatives;

  public Ambiguity(INode node, java.util.List<org.rascalmpl.ast.Toplevel> alternatives) {
    super(node);
    this.alternatives = java.util.Collections.unmodifiableList(alternatives);
  }

  public java.util.List<org.rascalmpl.ast.Toplevel> getAlternatives() {
   return alternatives;
  }

  public <T> T accept(IASTVisitor<T> v) {
	return v.visitToplevelAmbiguity(this);
  }
}





  public boolean isGivenVisibility() {
    return false;
  }
  
static public class GivenVisibility extends Toplevel {
  // Production: sig("GivenVisibility",[arg("org.rascalmpl.ast.Declaration","declaration")])

  
     private final org.rascalmpl.ast.Declaration declaration;
  

  
public GivenVisibility(INode node , org.rascalmpl.ast.Declaration declaration) {
  super(node);
  
    this.declaration = declaration;
  
}


  @Override
  public boolean isGivenVisibility() { 
    return true; 
  }

  @Override
  public <T> T accept(IASTVisitor<T> visitor) {
    return visitor.visitToplevelGivenVisibility(this);
  }
  
  
     @Override
     public org.rascalmpl.ast.Declaration getDeclaration() {
        return this.declaration;
     }
     
     @Override
     public boolean hasDeclaration() {
        return true;
     }
  	
}



}
