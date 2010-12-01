
package org.rascalmpl.ast;


import org.eclipse.imp.pdb.facts.INode;


public abstract class Character extends AbstractAST {
  public Character(INode node) {
    super(node);
  }
  

  public boolean hasNumChar() {
    return false;
  }

  public org.rascalmpl.ast.NumChar getNumChar() {
    throw new UnsupportedOperationException();
  }

  public boolean hasShortChar() {
    return false;
  }

  public org.rascalmpl.ast.ShortChar getShortChar() {
    throw new UnsupportedOperationException();
  }


static public class Ambiguity extends Character {
  private final java.util.List<org.rascalmpl.ast.Character> alternatives;

  public Ambiguity(INode node, java.util.List<org.rascalmpl.ast.Character> alternatives) {
    super(node);
    this.alternatives = java.util.Collections.unmodifiableList(alternatives);
  }

  public java.util.List<org.rascalmpl.ast.Character> getAlternatives() {
   return alternatives;
  }

  public <T> T accept(IASTVisitor<T> v) {
	return v.visitCharacterAmbiguity(this);
  }
}





  public boolean isEOF() {
    return false;
  }
  
static public class EOF extends Character {
  // Production: sig("EOF",[])

  

  
public EOF(INode node ) {
  super(node);
  
}


  @Override
  public boolean isEOF() { 
    return true; 
  }

  @Override
  public <T> T accept(IASTVisitor<T> visitor) {
    return visitor.visitCharacterEOF(this);
  }
  
  	
}


  public boolean isShort() {
    return false;
  }
  
static public class Short extends Character {
  // Production: sig("Short",[arg("org.rascalmpl.ast.ShortChar","shortChar")])

  
     private final org.rascalmpl.ast.ShortChar shortChar;
  

  
public Short(INode node , org.rascalmpl.ast.ShortChar shortChar) {
  super(node);
  
    this.shortChar = shortChar;
  
}


  @Override
  public boolean isShort() { 
    return true; 
  }

  @Override
  public <T> T accept(IASTVisitor<T> visitor) {
    return visitor.visitCharacterShort(this);
  }
  
  
     @Override
     public org.rascalmpl.ast.ShortChar getShortChar() {
        return this.shortChar;
     }
     
     @Override
     public boolean hasShortChar() {
        return true;
     }
  	
}


  public boolean isBottom() {
    return false;
  }
  
static public class Bottom extends Character {
  // Production: sig("Bottom",[])

  

  
public Bottom(INode node ) {
  super(node);
  
}


  @Override
  public boolean isBottom() { 
    return true; 
  }

  @Override
  public <T> T accept(IASTVisitor<T> visitor) {
    return visitor.visitCharacterBottom(this);
  }
  
  	
}


  public boolean isNumeric() {
    return false;
  }
  
static public class Numeric extends Character {
  // Production: sig("Numeric",[arg("org.rascalmpl.ast.NumChar","numChar")])

  
     private final org.rascalmpl.ast.NumChar numChar;
  

  
public Numeric(INode node , org.rascalmpl.ast.NumChar numChar) {
  super(node);
  
    this.numChar = numChar;
  
}


  @Override
  public boolean isNumeric() { 
    return true; 
  }

  @Override
  public <T> T accept(IASTVisitor<T> visitor) {
    return visitor.visitCharacterNumeric(this);
  }
  
  
     @Override
     public org.rascalmpl.ast.NumChar getNumChar() {
        return this.numChar;
     }
     
     @Override
     public boolean hasNumChar() {
        return true;
     }
  	
}


  public boolean isTop() {
    return false;
  }
  
static public class Top extends Character {
  // Production: sig("Top",[])

  

  
public Top(INode node ) {
  super(node);
  
}


  @Override
  public boolean isTop() { 
    return true; 
  }

  @Override
  public <T> T accept(IASTVisitor<T> visitor) {
    return visitor.visitCharacterTop(this);
  }
  
  	
}



}
