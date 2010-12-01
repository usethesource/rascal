
package org.rascalmpl.ast;


import org.eclipse.imp.pdb.facts.INode;


public abstract class NoElseMayFollow extends AbstractAST {
  public NoElseMayFollow(INode node) {
    super(node);
  }
  


static public class Ambiguity extends NoElseMayFollow {
  private final java.util.List<org.rascalmpl.ast.NoElseMayFollow> alternatives;

  public Ambiguity(INode node, java.util.List<org.rascalmpl.ast.NoElseMayFollow> alternatives) {
    super(node);
    this.alternatives = java.util.Collections.unmodifiableList(alternatives);
  }

  public java.util.List<org.rascalmpl.ast.NoElseMayFollow> getAlternatives() {
   return alternatives;
  }

  public <T> T accept(IASTVisitor<T> v) {
	return v.visitNoElseMayFollowAmbiguity(this);
  }
}





  public boolean isDefault() {
    return false;
  }
  
static public class Default extends NoElseMayFollow {
  // Production: sig("Default",[])

  

  
public Default(INode node ) {
  super(node);
  
}


  @Override
  public boolean isDefault() { 
    return true; 
  }

  @Override
  public <T> T accept(IASTVisitor<T> visitor) {
    return visitor.visitNoElseMayFollowDefault(this);
  }
  
  	
}



}
