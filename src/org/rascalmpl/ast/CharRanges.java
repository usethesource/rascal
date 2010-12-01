
package org.rascalmpl.ast;


import org.eclipse.imp.pdb.facts.INode;


public abstract class CharRanges extends AbstractAST {
  public CharRanges(INode node) {
    super(node);
  }
  

  public boolean hasRange() {
    return false;
  }

  public org.rascalmpl.ast.CharRange getRange() {
    throw new UnsupportedOperationException();
  }

  public boolean hasRanges() {
    return false;
  }

  public org.rascalmpl.ast.CharRanges getRanges() {
    throw new UnsupportedOperationException();
  }

  public boolean hasRhs() {
    return false;
  }

  public org.rascalmpl.ast.CharRanges getRhs() {
    throw new UnsupportedOperationException();
  }

  public boolean hasLhs() {
    return false;
  }

  public org.rascalmpl.ast.CharRanges getLhs() {
    throw new UnsupportedOperationException();
  }


static public class Ambiguity extends CharRanges {
  private final java.util.List<org.rascalmpl.ast.CharRanges> alternatives;

  public Ambiguity(INode node, java.util.List<org.rascalmpl.ast.CharRanges> alternatives) {
    super(node);
    this.alternatives = java.util.Collections.unmodifiableList(alternatives);
  }

  public java.util.List<org.rascalmpl.ast.CharRanges> getAlternatives() {
   return alternatives;
  }

  public <T> T accept(IASTVisitor<T> v) {
	return v.visitCharRangesAmbiguity(this);
  }
}





  public boolean isRange() {
    return false;
  }
  
static public class Range extends CharRanges {
  // Production: sig("Range",[arg("org.rascalmpl.ast.CharRange","range")])

  
     private final org.rascalmpl.ast.CharRange range;
  

  
public Range(INode node , org.rascalmpl.ast.CharRange range) {
  super(node);
  
    this.range = range;
  
}


  @Override
  public boolean isRange() { 
    return true; 
  }

  @Override
  public <T> T accept(IASTVisitor<T> visitor) {
    return visitor.visitCharRangesRange(this);
  }
  
  
     @Override
     public org.rascalmpl.ast.CharRange getRange() {
        return this.range;
     }
     
     @Override
     public boolean hasRange() {
        return true;
     }
  	
}


  public boolean isBracket() {
    return false;
  }
  
static public class Bracket extends CharRanges {
  // Production: sig("Bracket",[arg("org.rascalmpl.ast.CharRanges","ranges")])

  
     private final org.rascalmpl.ast.CharRanges ranges;
  

  
public Bracket(INode node , org.rascalmpl.ast.CharRanges ranges) {
  super(node);
  
    this.ranges = ranges;
  
}


  @Override
  public boolean isBracket() { 
    return true; 
  }

  @Override
  public <T> T accept(IASTVisitor<T> visitor) {
    return visitor.visitCharRangesBracket(this);
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


  public boolean isConcatenate() {
    return false;
  }
  
static public class Concatenate extends CharRanges {
  // Production: sig("Concatenate",[arg("org.rascalmpl.ast.CharRanges","lhs"),arg("org.rascalmpl.ast.CharRanges","rhs")])

  
     private final org.rascalmpl.ast.CharRanges lhs;
  
     private final org.rascalmpl.ast.CharRanges rhs;
  

  
public Concatenate(INode node , org.rascalmpl.ast.CharRanges lhs,  org.rascalmpl.ast.CharRanges rhs) {
  super(node);
  
    this.lhs = lhs;
  
    this.rhs = rhs;
  
}


  @Override
  public boolean isConcatenate() { 
    return true; 
  }

  @Override
  public <T> T accept(IASTVisitor<T> visitor) {
    return visitor.visitCharRangesConcatenate(this);
  }
  
  
     @Override
     public org.rascalmpl.ast.CharRanges getLhs() {
        return this.lhs;
     }
     
     @Override
     public boolean hasLhs() {
        return true;
     }
  
     @Override
     public org.rascalmpl.ast.CharRanges getRhs() {
        return this.rhs;
     }
     
     @Override
     public boolean hasRhs() {
        return true;
     }
  	
}



}
