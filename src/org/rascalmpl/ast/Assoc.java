
package org.rascalmpl.ast;


import org.eclipse.imp.pdb.facts.INode;


public abstract class Assoc extends AbstractAST {
  public Assoc(INode node) {
    super(node);
  }
  


static public class Ambiguity extends Assoc {
  private final java.util.List<org.rascalmpl.ast.Assoc> alternatives;

  public Ambiguity(INode node, java.util.List<org.rascalmpl.ast.Assoc> alternatives) {
    super(node);
    this.alternatives = java.util.Collections.unmodifiableList(alternatives);
  }

  public java.util.List<org.rascalmpl.ast.Assoc> getAlternatives() {
   return alternatives;
  }

  public <T> T accept(IASTVisitor<T> v) {
	return v.visitAssocAmbiguity(this);
  }
}





  public boolean isRight() {
    return false;
  }
  
static public class Right extends Assoc {
  // Production: sig("Right",[])

  

  
public Right(INode node ) {
  super(node);
  
}


  @Override
  public boolean isRight() { 
    return true; 
  }

  @Override
  public <T> T accept(IASTVisitor<T> visitor) {
    return visitor.visitAssocRight(this);
  }
  
  	
}


  public boolean isNonAssociative() {
    return false;
  }
  
static public class NonAssociative extends Assoc {
  // Production: sig("NonAssociative",[])

  

  
public NonAssociative(INode node ) {
  super(node);
  
}


  @Override
  public boolean isNonAssociative() { 
    return true; 
  }

  @Override
  public <T> T accept(IASTVisitor<T> visitor) {
    return visitor.visitAssocNonAssociative(this);
  }
  
  	
}


  public boolean isLeft() {
    return false;
  }
  
static public class Left extends Assoc {
  // Production: sig("Left",[])

  

  
public Left(INode node ) {
  super(node);
  
}


  @Override
  public boolean isLeft() { 
    return true; 
  }

  @Override
  public <T> T accept(IASTVisitor<T> visitor) {
    return visitor.visitAssocLeft(this);
  }
  
  	
}


  public boolean isAssociative() {
    return false;
  }
  
static public class Associative extends Assoc {
  // Production: sig("Associative",[])

  

  
public Associative(INode node ) {
  super(node);
  
}


  @Override
  public boolean isAssociative() { 
    return true; 
  }

  @Override
  public <T> T accept(IASTVisitor<T> visitor) {
    return visitor.visitAssocAssociative(this);
  }
  
  	
}



}
