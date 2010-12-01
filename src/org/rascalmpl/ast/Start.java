
package org.rascalmpl.ast;


import org.eclipse.imp.pdb.facts.INode;


public abstract class Start extends AbstractAST {
  public Start(INode node) {
    super(node);
  }
  


static public class Ambiguity extends Start {
  private final java.util.List<org.rascalmpl.ast.Start> alternatives;

  public Ambiguity(INode node, java.util.List<org.rascalmpl.ast.Start> alternatives) {
    super(node);
    this.alternatives = java.util.Collections.unmodifiableList(alternatives);
  }

  public java.util.List<org.rascalmpl.ast.Start> getAlternatives() {
   return alternatives;
  }

  public <T> T accept(IASTVisitor<T> v) {
	return v.visitStartAmbiguity(this);
  }
}





  public boolean isAbsent() {
    return false;
  }
  
static public class Absent extends Start {
  // Production: sig("Absent",[])

  

  
public Absent(INode node ) {
  super(node);
  
}


  @Override
  public boolean isAbsent() { 
    return true; 
  }

  @Override
  public <T> T accept(IASTVisitor<T> visitor) {
    return visitor.visitStartAbsent(this);
  }
  
  	
}


  public boolean isPresent() {
    return false;
  }
  
static public class Present extends Start {
  // Production: sig("Present",[])

  

  
public Present(INode node ) {
  super(node);
  
}


  @Override
  public boolean isPresent() { 
    return true; 
  }

  @Override
  public <T> T accept(IASTVisitor<T> visitor) {
    return visitor.visitStartPresent(this);
  }
  
  	
}



}
