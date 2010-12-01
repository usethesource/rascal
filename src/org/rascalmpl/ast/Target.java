
package org.rascalmpl.ast;


import org.eclipse.imp.pdb.facts.INode;


public abstract class Target extends AbstractAST {
  public Target(INode node) {
    super(node);
  }
  

  public boolean hasName() {
    return false;
  }

  public org.rascalmpl.ast.Name getName() {
    throw new UnsupportedOperationException();
  }


static public class Ambiguity extends Target {
  private final java.util.List<org.rascalmpl.ast.Target> alternatives;

  public Ambiguity(INode node, java.util.List<org.rascalmpl.ast.Target> alternatives) {
    super(node);
    this.alternatives = java.util.Collections.unmodifiableList(alternatives);
  }

  public java.util.List<org.rascalmpl.ast.Target> getAlternatives() {
   return alternatives;
  }

  public <T> T accept(IASTVisitor<T> v) {
	return v.visitTargetAmbiguity(this);
  }
}





  public boolean isEmpty() {
    return false;
  }
  
static public class Empty extends Target {
  // Production: sig("Empty",[])

  

  
public Empty(INode node ) {
  super(node);
  
}


  @Override
  public boolean isEmpty() { 
    return true; 
  }

  @Override
  public <T> T accept(IASTVisitor<T> visitor) {
    return visitor.visitTargetEmpty(this);
  }
  
  	
}


  public boolean isLabeled() {
    return false;
  }
  
static public class Labeled extends Target {
  // Production: sig("Labeled",[arg("org.rascalmpl.ast.Name","name")])

  
     private final org.rascalmpl.ast.Name name;
  

  
public Labeled(INode node , org.rascalmpl.ast.Name name) {
  super(node);
  
    this.name = name;
  
}


  @Override
  public boolean isLabeled() { 
    return true; 
  }

  @Override
  public <T> T accept(IASTVisitor<T> visitor) {
    return visitor.visitTargetLabeled(this);
  }
  
  
     @Override
     public org.rascalmpl.ast.Name getName() {
        return this.name;
     }
     
     @Override
     public boolean hasName() {
        return true;
     }
  	
}



}
