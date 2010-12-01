
package org.rascalmpl.ast;


import org.eclipse.imp.pdb.facts.INode;


public abstract class CharClass extends AbstractAST {
  public CharClass(INode node) {
    super(node);
  }
  

  public boolean hasLhs() {
    return false;
  }

  public org.rascalmpl.ast.CharClass getLhs() {
    throw new UnsupportedOperationException();
  }

  public boolean hasRhs() {
    return false;
  }

  public org.rascalmpl.ast.CharClass getRhs() {
    throw new UnsupportedOperationException();
  }

  public boolean hasOptionalCharRanges() {
    return false;
  }

  public org.rascalmpl.ast.OptCharRanges getOptionalCharRanges() {
    throw new UnsupportedOperationException();
  }

  public boolean hasCharClass() {
    return false;
  }

  public org.rascalmpl.ast.CharClass getCharClass() {
    throw new UnsupportedOperationException();
  }


static public class Ambiguity extends CharClass {
  private final java.util.List<org.rascalmpl.ast.CharClass> alternatives;

  public Ambiguity(INode node, java.util.List<org.rascalmpl.ast.CharClass> alternatives) {
    super(node);
    this.alternatives = java.util.Collections.unmodifiableList(alternatives);
  }

  public java.util.List<org.rascalmpl.ast.CharClass> getAlternatives() {
   return alternatives;
  }

  public <T> T accept(IASTVisitor<T> v) {
	return v.visitCharClassAmbiguity(this);
  }
}





  public boolean isComplement() {
    return false;
  }
  
static public class Complement extends CharClass {
  // Production: sig("Complement",[arg("org.rascalmpl.ast.CharClass","charClass")])

  
     private final org.rascalmpl.ast.CharClass charClass;
  

  
public Complement(INode node , org.rascalmpl.ast.CharClass charClass) {
  super(node);
  
    this.charClass = charClass;
  
}


  @Override
  public boolean isComplement() { 
    return true; 
  }

  @Override
  public <T> T accept(IASTVisitor<T> visitor) {
    return visitor.visitCharClassComplement(this);
  }
  
  
     @Override
     public org.rascalmpl.ast.CharClass getCharClass() {
        return this.charClass;
     }
     
     @Override
     public boolean hasCharClass() {
        return true;
     }
  	
}


  public boolean isIntersection() {
    return false;
  }
  
static public class Intersection extends CharClass {
  // Production: sig("Intersection",[arg("org.rascalmpl.ast.CharClass","lhs"),arg("org.rascalmpl.ast.CharClass","rhs")])

  
     private final org.rascalmpl.ast.CharClass lhs;
  
     private final org.rascalmpl.ast.CharClass rhs;
  

  
public Intersection(INode node , org.rascalmpl.ast.CharClass lhs,  org.rascalmpl.ast.CharClass rhs) {
  super(node);
  
    this.lhs = lhs;
  
    this.rhs = rhs;
  
}


  @Override
  public boolean isIntersection() { 
    return true; 
  }

  @Override
  public <T> T accept(IASTVisitor<T> visitor) {
    return visitor.visitCharClassIntersection(this);
  }
  
  
     @Override
     public org.rascalmpl.ast.CharClass getLhs() {
        return this.lhs;
     }
     
     @Override
     public boolean hasLhs() {
        return true;
     }
  
     @Override
     public org.rascalmpl.ast.CharClass getRhs() {
        return this.rhs;
     }
     
     @Override
     public boolean hasRhs() {
        return true;
     }
  	
}


  public boolean isBracket() {
    return false;
  }
  
static public class Bracket extends CharClass {
  // Production: sig("Bracket",[arg("org.rascalmpl.ast.CharClass","charClass")])

  
     private final org.rascalmpl.ast.CharClass charClass;
  

  
public Bracket(INode node , org.rascalmpl.ast.CharClass charClass) {
  super(node);
  
    this.charClass = charClass;
  
}


  @Override
  public boolean isBracket() { 
    return true; 
  }

  @Override
  public <T> T accept(IASTVisitor<T> visitor) {
    return visitor.visitCharClassBracket(this);
  }
  
  
     @Override
     public org.rascalmpl.ast.CharClass getCharClass() {
        return this.charClass;
     }
     
     @Override
     public boolean hasCharClass() {
        return true;
     }
  	
}


  public boolean isDifference() {
    return false;
  }
  
static public class Difference extends CharClass {
  // Production: sig("Difference",[arg("org.rascalmpl.ast.CharClass","lhs"),arg("org.rascalmpl.ast.CharClass","rhs")])

  
     private final org.rascalmpl.ast.CharClass lhs;
  
     private final org.rascalmpl.ast.CharClass rhs;
  

  
public Difference(INode node , org.rascalmpl.ast.CharClass lhs,  org.rascalmpl.ast.CharClass rhs) {
  super(node);
  
    this.lhs = lhs;
  
    this.rhs = rhs;
  
}


  @Override
  public boolean isDifference() { 
    return true; 
  }

  @Override
  public <T> T accept(IASTVisitor<T> visitor) {
    return visitor.visitCharClassDifference(this);
  }
  
  
     @Override
     public org.rascalmpl.ast.CharClass getLhs() {
        return this.lhs;
     }
     
     @Override
     public boolean hasLhs() {
        return true;
     }
  
     @Override
     public org.rascalmpl.ast.CharClass getRhs() {
        return this.rhs;
     }
     
     @Override
     public boolean hasRhs() {
        return true;
     }
  	
}


  public boolean isSimpleCharclass() {
    return false;
  }
  
static public class SimpleCharclass extends CharClass {
  // Production: sig("SimpleCharclass",[arg("org.rascalmpl.ast.OptCharRanges","optionalCharRanges")])

  
     private final org.rascalmpl.ast.OptCharRanges optionalCharRanges;
  

  
public SimpleCharclass(INode node , org.rascalmpl.ast.OptCharRanges optionalCharRanges) {
  super(node);
  
    this.optionalCharRanges = optionalCharRanges;
  
}


  @Override
  public boolean isSimpleCharclass() { 
    return true; 
  }

  @Override
  public <T> T accept(IASTVisitor<T> visitor) {
    return visitor.visitCharClassSimpleCharclass(this);
  }
  
  
     @Override
     public org.rascalmpl.ast.OptCharRanges getOptionalCharRanges() {
        return this.optionalCharRanges;
     }
     
     @Override
     public boolean hasOptionalCharRanges() {
        return true;
     }
  	
}


  public boolean isUnion() {
    return false;
  }
  
static public class Union extends CharClass {
  // Production: sig("Union",[arg("org.rascalmpl.ast.CharClass","lhs"),arg("org.rascalmpl.ast.CharClass","rhs")])

  
     private final org.rascalmpl.ast.CharClass lhs;
  
     private final org.rascalmpl.ast.CharClass rhs;
  

  
public Union(INode node , org.rascalmpl.ast.CharClass lhs,  org.rascalmpl.ast.CharClass rhs) {
  super(node);
  
    this.lhs = lhs;
  
    this.rhs = rhs;
  
}


  @Override
  public boolean isUnion() { 
    return true; 
  }

  @Override
  public <T> T accept(IASTVisitor<T> visitor) {
    return visitor.visitCharClassUnion(this);
  }
  
  
     @Override
     public org.rascalmpl.ast.CharClass getLhs() {
        return this.lhs;
     }
     
     @Override
     public boolean hasLhs() {
        return true;
     }
  
     @Override
     public org.rascalmpl.ast.CharClass getRhs() {
        return this.rhs;
     }
     
     @Override
     public boolean hasRhs() {
        return true;
     }
  	
}



}
