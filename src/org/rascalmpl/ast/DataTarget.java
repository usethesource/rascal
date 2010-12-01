
package org.rascalmpl.ast;


import org.eclipse.imp.pdb.facts.INode;


public abstract class DataTarget extends AbstractAST {
  public DataTarget(INode node) {
    super(node);
  }
  

  public boolean hasLabel() {
    return false;
  }

  public org.rascalmpl.ast.Name getLabel() {
    throw new UnsupportedOperationException();
  }


static public class Ambiguity extends DataTarget {
  private final java.util.List<org.rascalmpl.ast.DataTarget> alternatives;

  public Ambiguity(INode node, java.util.List<org.rascalmpl.ast.DataTarget> alternatives) {
    super(node);
    this.alternatives = java.util.Collections.unmodifiableList(alternatives);
  }

  public java.util.List<org.rascalmpl.ast.DataTarget> getAlternatives() {
   return alternatives;
  }

  public <T> T accept(IASTVisitor<T> v) {
	return v.visitDataTargetAmbiguity(this);
  }
}





  public boolean isEmpty() {
    return false;
  }
  
static public class Empty extends DataTarget {
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
    return visitor.visitDataTargetEmpty(this);
  }
  
  	
}


  public boolean isLabeled() {
    return false;
  }
  
static public class Labeled extends DataTarget {
  // Production: sig("Labeled",[arg("org.rascalmpl.ast.Name","label")])

  
     private final org.rascalmpl.ast.Name label;
  

  
public Labeled(INode node , org.rascalmpl.ast.Name label) {
  super(node);
  
    this.label = label;
  
}


  @Override
  public boolean isLabeled() { 
    return true; 
  }

  @Override
  public <T> T accept(IASTVisitor<T> visitor) {
    return visitor.visitDataTargetLabeled(this);
  }
  
  
     @Override
     public org.rascalmpl.ast.Name getLabel() {
        return this.label;
     }
     
     @Override
     public boolean hasLabel() {
        return true;
     }
  	
}



}
