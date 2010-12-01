
package org.rascalmpl.ast;


import org.eclipse.imp.pdb.facts.INode;


public abstract class OptCharRanges extends AbstractAST {
  public OptCharRanges(INode node) {
    super(node);
  }
  

  public boolean hasRanges() {
    return false;
  }

  public org.rascalmpl.ast.CharRanges getRanges() {
    throw new UnsupportedOperationException();
  }


static public class Ambiguity extends OptCharRanges {
  private final java.util.List<org.rascalmpl.ast.OptCharRanges> alternatives;

  public Ambiguity(INode node, java.util.List<org.rascalmpl.ast.OptCharRanges> alternatives) {
    super(node);
    this.alternatives = java.util.Collections.unmodifiableList(alternatives);
  }

  public java.util.List<org.rascalmpl.ast.OptCharRanges> getAlternatives() {
   return alternatives;
  }

  public <T> T accept(IASTVisitor<T> v) {
	return v.visitOptCharRangesAmbiguity(this);
  }
}





  public boolean isAbsent() {
    return false;
  }
  
static public class Absent extends OptCharRanges {
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
    return visitor.visitOptCharRangesAbsent(this);
  }
  
  	
}


  public boolean isPresent() {
    return false;
  }
  
static public class Present extends OptCharRanges {
  // Production: sig("Present",[arg("org.rascalmpl.ast.CharRanges","ranges")])

  
     private final org.rascalmpl.ast.CharRanges ranges;
  

  
public Present(INode node , org.rascalmpl.ast.CharRanges ranges) {
  super(node);
  
    this.ranges = ranges;
  
}


  @Override
  public boolean isPresent() { 
    return true; 
  }

  @Override
  public <T> T accept(IASTVisitor<T> visitor) {
    return visitor.visitOptCharRangesPresent(this);
  }
  
  
     @Override
     public org.rascalmpl.ast.CharRanges getRanges() {
        return this.ranges;
     }
     
     @Override
     public boolean hasRanges() {
        return true;
     }
  	
}



}
