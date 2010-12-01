
package org.rascalmpl.ast;


import org.eclipse.imp.pdb.facts.INode;


public abstract class CharRange extends AbstractAST {
  public CharRange(INode node) {
    super(node);
  }
  

  public boolean hasCharacter() {
    return false;
  }

  public org.rascalmpl.ast.Character getCharacter() {
    throw new UnsupportedOperationException();
  }

  public boolean hasStart() {
    return false;
  }

  public org.rascalmpl.ast.Character getStart() {
    throw new UnsupportedOperationException();
  }

  public boolean hasEnd() {
    return false;
  }

  public org.rascalmpl.ast.Character getEnd() {
    throw new UnsupportedOperationException();
  }


static public class Ambiguity extends CharRange {
  private final java.util.List<org.rascalmpl.ast.CharRange> alternatives;

  public Ambiguity(INode node, java.util.List<org.rascalmpl.ast.CharRange> alternatives) {
    super(node);
    this.alternatives = java.util.Collections.unmodifiableList(alternatives);
  }

  public java.util.List<org.rascalmpl.ast.CharRange> getAlternatives() {
   return alternatives;
  }

  public <T> T accept(IASTVisitor<T> v) {
	return v.visitCharRangeAmbiguity(this);
  }
}





  public boolean isRange() {
    return false;
  }
  
static public class Range extends CharRange {
  // Production: sig("Range",[arg("org.rascalmpl.ast.Character","start"),arg("org.rascalmpl.ast.Character","end")])

  
     private final org.rascalmpl.ast.Character start;
  
     private final org.rascalmpl.ast.Character end;
  

  
public Range(INode node , org.rascalmpl.ast.Character start,  org.rascalmpl.ast.Character end) {
  super(node);
  
    this.start = start;
  
    this.end = end;
  
}


  @Override
  public boolean isRange() { 
    return true; 
  }

  @Override
  public <T> T accept(IASTVisitor<T> visitor) {
    return visitor.visitCharRangeRange(this);
  }
  
  
     @Override
     public org.rascalmpl.ast.Character getStart() {
        return this.start;
     }
     
     @Override
     public boolean hasStart() {
        return true;
     }
  
     @Override
     public org.rascalmpl.ast.Character getEnd() {
        return this.end;
     }
     
     @Override
     public boolean hasEnd() {
        return true;
     }
  	
}


  public boolean isCharacter() {
    return false;
  }
  
static public class Character extends CharRange {
  // Production: sig("Character",[arg("org.rascalmpl.ast.Character","character")])

  
     private final org.rascalmpl.ast.Character character;
  

  
public Character(INode node , org.rascalmpl.ast.Character character) {
  super(node);
  
    this.character = character;
  
}


  @Override
  public boolean isCharacter() { 
    return true; 
  }

  @Override
  public <T> T accept(IASTVisitor<T> visitor) {
    return visitor.visitCharRangeCharacter(this);
  }
  
  
     @Override
     public org.rascalmpl.ast.Character getCharacter() {
        return this.character;
     }
     
     @Override
     public boolean hasCharacter() {
        return true;
     }
  	
}



}
