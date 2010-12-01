
package org.rascalmpl.ast;


import org.eclipse.imp.pdb.facts.INode;


public abstract class Symbol extends AbstractAST {
  public Symbol(INode node) {
    super(node);
  }
  

  public boolean hasLhs() {
    return false;
  }

  public org.rascalmpl.ast.Symbol getLhs() {
    throw new UnsupportedOperationException();
  }

  public boolean hasRhs() {
    return false;
  }

  public org.rascalmpl.ast.Symbol getRhs() {
    throw new UnsupportedOperationException();
  }

  public boolean hasSep() {
    return false;
  }

  public org.rascalmpl.ast.StrCon getSep() {
    throw new UnsupportedOperationException();
  }

  public boolean hasSingelQuotedString() {
    return false;
  }

  public org.rascalmpl.ast.SingleQuotedStrCon getSingelQuotedString() {
    throw new UnsupportedOperationException();
  }

  public boolean hasString() {
    return false;
  }

  public org.rascalmpl.ast.StrCon getString() {
    throw new UnsupportedOperationException();
  }

  public boolean hasName() {
    return false;
  }

  public org.rascalmpl.ast.QualifiedName getName() {
    throw new UnsupportedOperationException();
  }

  public boolean hasTail() {
    return false;
  }

  public java.util.List<org.rascalmpl.ast.Symbol> getTail() {
    throw new UnsupportedOperationException();
  }

  public boolean hasSymbol() {
    return false;
  }

  public org.rascalmpl.ast.Symbol getSymbol() {
    throw new UnsupportedOperationException();
  }

  public boolean hasCharClass() {
    return false;
  }

  public org.rascalmpl.ast.CharClass getCharClass() {
    throw new UnsupportedOperationException();
  }

  public boolean hasHead() {
    return false;
  }

  public org.rascalmpl.ast.Symbol getHead() {
    throw new UnsupportedOperationException();
  }


static public class Ambiguity extends Symbol {
  private final java.util.List<org.rascalmpl.ast.Symbol> alternatives;

  public Ambiguity(INode node, java.util.List<org.rascalmpl.ast.Symbol> alternatives) {
    super(node);
    this.alternatives = java.util.Collections.unmodifiableList(alternatives);
  }

  public java.util.List<org.rascalmpl.ast.Symbol> getAlternatives() {
   return alternatives;
  }

  public <T> T accept(IASTVisitor<T> v) {
	return v.visitSymbolAmbiguity(this);
  }
}





  public boolean isSort() {
    return false;
  }
  
static public class Sort extends Symbol {
  // Production: sig("Sort",[arg("org.rascalmpl.ast.QualifiedName","name")])

  
     private final org.rascalmpl.ast.QualifiedName name;
  

  
public Sort(INode node , org.rascalmpl.ast.QualifiedName name) {
  super(node);
  
    this.name = name;
  
}


  @Override
  public boolean isSort() { 
    return true; 
  }

  @Override
  public <T> T accept(IASTVisitor<T> visitor) {
    return visitor.visitSymbolSort(this);
  }
  
  
     @Override
     public org.rascalmpl.ast.QualifiedName getName() {
        return this.name;
     }
     
     @Override
     public boolean hasName() {
        return true;
     }
  	
}


  public boolean isIter() {
    return false;
  }
  
static public class Iter extends Symbol {
  // Production: sig("Iter",[arg("org.rascalmpl.ast.Symbol","symbol")])

  
     private final org.rascalmpl.ast.Symbol symbol;
  

  
public Iter(INode node , org.rascalmpl.ast.Symbol symbol) {
  super(node);
  
    this.symbol = symbol;
  
}


  @Override
  public boolean isIter() { 
    return true; 
  }

  @Override
  public <T> T accept(IASTVisitor<T> visitor) {
    return visitor.visitSymbolIter(this);
  }
  
  
     @Override
     public org.rascalmpl.ast.Symbol getSymbol() {
        return this.symbol;
     }
     
     @Override
     public boolean hasSymbol() {
        return true;
     }
  	
}


  public boolean isEmpty() {
    return false;
  }
  
static public class Empty extends Symbol {
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
    return visitor.visitSymbolEmpty(this);
  }
  
  	
}


  public boolean isSequence() {
    return false;
  }
  
static public class Sequence extends Symbol {
  // Production: sig("Sequence",[arg("org.rascalmpl.ast.Symbol","head"),arg("java.util.List\<org.rascalmpl.ast.Symbol\>","tail")])

  
     private final org.rascalmpl.ast.Symbol head;
  
     private final java.util.List<org.rascalmpl.ast.Symbol> tail;
  

  
public Sequence(INode node , org.rascalmpl.ast.Symbol head,  java.util.List<org.rascalmpl.ast.Symbol> tail) {
  super(node);
  
    this.head = head;
  
    this.tail = tail;
  
}


  @Override
  public boolean isSequence() { 
    return true; 
  }

  @Override
  public <T> T accept(IASTVisitor<T> visitor) {
    return visitor.visitSymbolSequence(this);
  }
  
  
     @Override
     public org.rascalmpl.ast.Symbol getHead() {
        return this.head;
     }
     
     @Override
     public boolean hasHead() {
        return true;
     }
  
     @Override
     public java.util.List<org.rascalmpl.ast.Symbol> getTail() {
        return this.tail;
     }
     
     @Override
     public boolean hasTail() {
        return true;
     }
  	
}


  public boolean isIterStar() {
    return false;
  }
  
static public class IterStar extends Symbol {
  // Production: sig("IterStar",[arg("org.rascalmpl.ast.Symbol","symbol")])

  
     private final org.rascalmpl.ast.Symbol symbol;
  

  
public IterStar(INode node , org.rascalmpl.ast.Symbol symbol) {
  super(node);
  
    this.symbol = symbol;
  
}


  @Override
  public boolean isIterStar() { 
    return true; 
  }

  @Override
  public <T> T accept(IASTVisitor<T> visitor) {
    return visitor.visitSymbolIterStar(this);
  }
  
  
     @Override
     public org.rascalmpl.ast.Symbol getSymbol() {
        return this.symbol;
     }
     
     @Override
     public boolean hasSymbol() {
        return true;
     }
  	
}


  public boolean isLiteral() {
    return false;
  }
  
static public class Literal extends Symbol {
  // Production: sig("Literal",[arg("org.rascalmpl.ast.StrCon","string")])

  
     private final org.rascalmpl.ast.StrCon string;
  

  
public Literal(INode node , org.rascalmpl.ast.StrCon string) {
  super(node);
  
    this.string = string;
  
}


  @Override
  public boolean isLiteral() { 
    return true; 
  }

  @Override
  public <T> T accept(IASTVisitor<T> visitor) {
    return visitor.visitSymbolLiteral(this);
  }
  
  
     @Override
     public org.rascalmpl.ast.StrCon getString() {
        return this.string;
     }
     
     @Override
     public boolean hasString() {
        return true;
     }
  	
}


  public boolean isIterStarSep() {
    return false;
  }
  
static public class IterStarSep extends Symbol {
  // Production: sig("IterStarSep",[arg("org.rascalmpl.ast.Symbol","symbol"),arg("org.rascalmpl.ast.StrCon","sep")])

  
     private final org.rascalmpl.ast.Symbol symbol;
  
     private final org.rascalmpl.ast.StrCon sep;
  

  
public IterStarSep(INode node , org.rascalmpl.ast.Symbol symbol,  org.rascalmpl.ast.StrCon sep) {
  super(node);
  
    this.symbol = symbol;
  
    this.sep = sep;
  
}


  @Override
  public boolean isIterStarSep() { 
    return true; 
  }

  @Override
  public <T> T accept(IASTVisitor<T> visitor) {
    return visitor.visitSymbolIterStarSep(this);
  }
  
  
     @Override
     public org.rascalmpl.ast.Symbol getSymbol() {
        return this.symbol;
     }
     
     @Override
     public boolean hasSymbol() {
        return true;
     }
  
     @Override
     public org.rascalmpl.ast.StrCon getSep() {
        return this.sep;
     }
     
     @Override
     public boolean hasSep() {
        return true;
     }
  	
}


  public boolean isAlternative() {
    return false;
  }
  
static public class Alternative extends Symbol {
  // Production: sig("Alternative",[arg("org.rascalmpl.ast.Symbol","lhs"),arg("org.rascalmpl.ast.Symbol","rhs")])

  
     private final org.rascalmpl.ast.Symbol lhs;
  
     private final org.rascalmpl.ast.Symbol rhs;
  

  
public Alternative(INode node , org.rascalmpl.ast.Symbol lhs,  org.rascalmpl.ast.Symbol rhs) {
  super(node);
  
    this.lhs = lhs;
  
    this.rhs = rhs;
  
}


  @Override
  public boolean isAlternative() { 
    return true; 
  }

  @Override
  public <T> T accept(IASTVisitor<T> visitor) {
    return visitor.visitSymbolAlternative(this);
  }
  
  
     @Override
     public org.rascalmpl.ast.Symbol getLhs() {
        return this.lhs;
     }
     
     @Override
     public boolean hasLhs() {
        return true;
     }
  
     @Override
     public org.rascalmpl.ast.Symbol getRhs() {
        return this.rhs;
     }
     
     @Override
     public boolean hasRhs() {
        return true;
     }
  	
}


  public boolean isCaseInsensitiveLiteral() {
    return false;
  }
  
static public class CaseInsensitiveLiteral extends Symbol {
  // Production: sig("CaseInsensitiveLiteral",[arg("org.rascalmpl.ast.SingleQuotedStrCon","singelQuotedString")])

  
     private final org.rascalmpl.ast.SingleQuotedStrCon singelQuotedString;
  

  
public CaseInsensitiveLiteral(INode node , org.rascalmpl.ast.SingleQuotedStrCon singelQuotedString) {
  super(node);
  
    this.singelQuotedString = singelQuotedString;
  
}


  @Override
  public boolean isCaseInsensitiveLiteral() { 
    return true; 
  }

  @Override
  public <T> T accept(IASTVisitor<T> visitor) {
    return visitor.visitSymbolCaseInsensitiveLiteral(this);
  }
  
  
     @Override
     public org.rascalmpl.ast.SingleQuotedStrCon getSingelQuotedString() {
        return this.singelQuotedString;
     }
     
     @Override
     public boolean hasSingelQuotedString() {
        return true;
     }
  	
}


  public boolean isIterSep() {
    return false;
  }
  
static public class IterSep extends Symbol {
  // Production: sig("IterSep",[arg("org.rascalmpl.ast.Symbol","symbol"),arg("org.rascalmpl.ast.StrCon","sep")])

  
     private final org.rascalmpl.ast.Symbol symbol;
  
     private final org.rascalmpl.ast.StrCon sep;
  

  
public IterSep(INode node , org.rascalmpl.ast.Symbol symbol,  org.rascalmpl.ast.StrCon sep) {
  super(node);
  
    this.symbol = symbol;
  
    this.sep = sep;
  
}


  @Override
  public boolean isIterSep() { 
    return true; 
  }

  @Override
  public <T> T accept(IASTVisitor<T> visitor) {
    return visitor.visitSymbolIterSep(this);
  }
  
  
     @Override
     public org.rascalmpl.ast.Symbol getSymbol() {
        return this.symbol;
     }
     
     @Override
     public boolean hasSymbol() {
        return true;
     }
  
     @Override
     public org.rascalmpl.ast.StrCon getSep() {
        return this.sep;
     }
     
     @Override
     public boolean hasSep() {
        return true;
     }
  	
}


  public boolean isOptional() {
    return false;
  }
  
static public class Optional extends Symbol {
  // Production: sig("Optional",[arg("org.rascalmpl.ast.Symbol","symbol")])

  
     private final org.rascalmpl.ast.Symbol symbol;
  

  
public Optional(INode node , org.rascalmpl.ast.Symbol symbol) {
  super(node);
  
    this.symbol = symbol;
  
}


  @Override
  public boolean isOptional() { 
    return true; 
  }

  @Override
  public <T> T accept(IASTVisitor<T> visitor) {
    return visitor.visitSymbolOptional(this);
  }
  
  
     @Override
     public org.rascalmpl.ast.Symbol getSymbol() {
        return this.symbol;
     }
     
     @Override
     public boolean hasSymbol() {
        return true;
     }
  	
}


  public boolean isCharacterClass() {
    return false;
  }
  
static public class CharacterClass extends Symbol {
  // Production: sig("CharacterClass",[arg("org.rascalmpl.ast.CharClass","charClass")])

  
     private final org.rascalmpl.ast.CharClass charClass;
  

  
public CharacterClass(INode node , org.rascalmpl.ast.CharClass charClass) {
  super(node);
  
    this.charClass = charClass;
  
}


  @Override
  public boolean isCharacterClass() { 
    return true; 
  }

  @Override
  public <T> T accept(IASTVisitor<T> visitor) {
    return visitor.visitSymbolCharacterClass(this);
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



}
