/*******************************************************************************
 * Copyright (c) 2009-2015 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   * Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI
 *   * Tijs van der Storm - Tijs.van.der.Storm@cwi.nl
 *   * Paul Klint - Paul.Klint@cwi.nl - CWI
 *   * Mark Hills - Mark.Hills@cwi.nl (CWI)
 *   * Arnold Lankamp - Arnold.Lankamp@cwi.nl
 *   * Michael Steindorfer - Michael.Steindorfer@cwi.nl - CWI
 *******************************************************************************/
package org.rascalmpl.ast;


import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.ISourceLocation;

public abstract class Sym extends AbstractAST {
  public Sym(ISourceLocation src, IConstructor node) {
    super(src /* we forget node on purpose */);
  }

  
  public boolean hasArguments() {
    return false;
  }

  public java.util.List<org.rascalmpl.ast.Expression> getArguments() {
    throw new UnsupportedOperationException();
  }
  public boolean hasBlock() {
    return false;
  }

  public java.util.List<org.rascalmpl.ast.Statement> getBlock() {
    throw new UnsupportedOperationException();
  }
  public boolean hasAlternatives() {
    return false;
  }

  public java.util.List<org.rascalmpl.ast.Sym> getAlternatives() {
    throw new UnsupportedOperationException();
  }
  public boolean hasParameters() {
    return false;
  }

  public java.util.List<org.rascalmpl.ast.Sym> getParameters() {
    throw new UnsupportedOperationException();
  }
  public boolean hasSequence() {
    return false;
  }

  public java.util.List<org.rascalmpl.ast.Sym> getSequence() {
    throw new UnsupportedOperationException();
  }
  public boolean hasSymbols() {
    return false;
  }

  public java.util.List<org.rascalmpl.ast.Sym> getSymbols() {
    throw new UnsupportedOperationException();
  }
  public boolean hasCistring() {
    return false;
  }

  public org.rascalmpl.ast.CaseInsensitiveStringConstant getCistring() {
    throw new UnsupportedOperationException();
  }
  public boolean hasCharClass() {
    return false;
  }

  public org.rascalmpl.ast.Class getCharClass() {
    throw new UnsupportedOperationException();
  }
  public boolean hasCondition() {
    return false;
  }

  public org.rascalmpl.ast.Expression getCondition() {
    throw new UnsupportedOperationException();
  }
  public boolean hasColumn() {
    return false;
  }

  public org.rascalmpl.ast.IntegerLiteral getColumn() {
    throw new UnsupportedOperationException();
  }
  public boolean hasKeywordArguments() {
    return false;
  }

  public org.rascalmpl.ast.KeywordArguments_Expression getKeywordArguments() {
    throw new UnsupportedOperationException();
  }
  public boolean hasNonterminal() {
    return false;
  }

  public org.rascalmpl.ast.Nonterminal getNonterminal() {
    throw new UnsupportedOperationException();
  }
  public boolean hasLabel() {
    return false;
  }

  public org.rascalmpl.ast.NonterminalLabel getLabel() {
    throw new UnsupportedOperationException();
  }
  public boolean hasString() {
    return false;
  }

  public org.rascalmpl.ast.StringConstant getString() {
    throw new UnsupportedOperationException();
  }
  public boolean hasBody() {
    return false;
  }

  public org.rascalmpl.ast.Sym getBody() {
    throw new UnsupportedOperationException();
  }
  public boolean hasElsePart() {
    return false;
  }

  public org.rascalmpl.ast.Sym getElsePart() {
    throw new UnsupportedOperationException();
  }
  public boolean hasFirst() {
    return false;
  }

  public org.rascalmpl.ast.Sym getFirst() {
    throw new UnsupportedOperationException();
  }
  public boolean hasMatch() {
    return false;
  }

  public org.rascalmpl.ast.Sym getMatch() {
    throw new UnsupportedOperationException();
  }
  public boolean hasSep() {
    return false;
  }

  public org.rascalmpl.ast.Sym getSep() {
    throw new UnsupportedOperationException();
  }
  public boolean hasSymbol() {
    return false;
  }

  public org.rascalmpl.ast.Sym getSymbol() {
    throw new UnsupportedOperationException();
  }
  public boolean hasThenPart() {
    return false;
  }

  public org.rascalmpl.ast.Sym getThenPart() {
    throw new UnsupportedOperationException();
  }

  

  
  public boolean isAlternative() {
    return false;
  }

  static public class Alternative extends Sym {
    // Production: sig("Alternative",[arg("org.rascalmpl.ast.Sym","first"),arg("java.util.List\<org.rascalmpl.ast.Sym\>","alternatives")],breakable=false)
  
    
    private final org.rascalmpl.ast.Sym first;
    private final java.util.List<org.rascalmpl.ast.Sym> alternatives;
  
    public Alternative(ISourceLocation src, IConstructor node , org.rascalmpl.ast.Sym first,  java.util.List<org.rascalmpl.ast.Sym> alternatives) {
      super(src, node);
      
      this.first = first;
      this.alternatives = alternatives;
    }
  
    @Override
    public boolean isAlternative() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitSymAlternative(this);
    }
  
    @Override
    protected void addForLineNumber(int $line, java.util.List<AbstractAST> $result) {
      if (getLocation().getBeginLine() == $line) {
        $result.add(this);
      }
      ISourceLocation $l;
      
      $l = first.getLocation();
      if ($l.hasLineColumn() && $l.getBeginLine() <= $line && $l.getEndLine() >= $line) {
        first.addForLineNumber($line, $result);
      }
      if ($l.getBeginLine() > $line) {
        return;
      }
      
      for (AbstractAST $elem : alternatives) {
        $l = $elem.getLocation();
        if ($l.hasLineColumn() && $l.getBeginLine() <= $line && $l.getEndLine() >= $line) {
          $elem.addForLineNumber($line, $result);
        }
        if ($l.getBeginLine() > $line) {
          return;
        }
  
      }
    }
  
    @Override
    public boolean equals(Object o) {
      if (!(o instanceof Alternative)) {
        return false;
      }        
      Alternative tmp = (Alternative) o;
      return true && tmp.first.equals(this.first) && tmp.alternatives.equals(this.alternatives) ; 
    }
   
    @Override
    public int hashCode() {
      return 151 + 311 * first.hashCode() + 73 * alternatives.hashCode() ; 
    } 
  
    
    @Override
    public org.rascalmpl.ast.Sym getFirst() {
      return this.first;
    }
  
    @Override
    public boolean hasFirst() {
      return true;
    }
    @Override
    public java.util.List<org.rascalmpl.ast.Sym> getAlternatives() {
      return this.alternatives;
    }
  
    @Override
    public boolean hasAlternatives() {
      return true;
    }	
  
    @Override
    public Object clone()  {
      return newInstance(getClass(), src, (IConstructor) null , clone(first), clone(alternatives));
    }
            
  }
  public boolean isBracket() {
    return false;
  }

  static public class Bracket extends Sym {
    // Production: sig("Bracket",[arg("org.rascalmpl.ast.Sym","symbol")],breakable=false)
  
    
    private final org.rascalmpl.ast.Sym symbol;
  
    public Bracket(ISourceLocation src, IConstructor node , org.rascalmpl.ast.Sym symbol) {
      super(src, node);
      
      this.symbol = symbol;
    }
  
    @Override
    public boolean isBracket() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitSymBracket(this);
    }
  
    @Override
    protected void addForLineNumber(int $line, java.util.List<AbstractAST> $result) {
      if (getLocation().getBeginLine() == $line) {
        $result.add(this);
      }
      ISourceLocation $l;
      
      $l = symbol.getLocation();
      if ($l.hasLineColumn() && $l.getBeginLine() <= $line && $l.getEndLine() >= $line) {
        symbol.addForLineNumber($line, $result);
      }
      if ($l.getBeginLine() > $line) {
        return;
      }
      
    }
  
    @Override
    public boolean equals(Object o) {
      if (!(o instanceof Bracket)) {
        return false;
      }        
      Bracket tmp = (Bracket) o;
      return true && tmp.symbol.equals(this.symbol) ; 
    }
   
    @Override
    public int hashCode() {
      return 619 + 5 * symbol.hashCode() ; 
    } 
  
    
    @Override
    public org.rascalmpl.ast.Sym getSymbol() {
      return this.symbol;
    }
  
    @Override
    public boolean hasSymbol() {
      return true;
    }	
  
    @Override
    public Object clone()  {
      return newInstance(getClass(), src, (IConstructor) null , clone(symbol));
    }
            
  }
  public boolean isCaseInsensitiveLiteral() {
    return false;
  }

  static public class CaseInsensitiveLiteral extends Sym {
    // Production: sig("CaseInsensitiveLiteral",[arg("org.rascalmpl.ast.CaseInsensitiveStringConstant","cistring")],breakable=false)
  
    
    private final org.rascalmpl.ast.CaseInsensitiveStringConstant cistring;
  
    public CaseInsensitiveLiteral(ISourceLocation src, IConstructor node , org.rascalmpl.ast.CaseInsensitiveStringConstant cistring) {
      super(src, node);
      
      this.cistring = cistring;
    }
  
    @Override
    public boolean isCaseInsensitiveLiteral() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitSymCaseInsensitiveLiteral(this);
    }
  
    @Override
    protected void addForLineNumber(int $line, java.util.List<AbstractAST> $result) {
      if (getLocation().getBeginLine() == $line) {
        $result.add(this);
      }
      ISourceLocation $l;
      
      $l = cistring.getLocation();
      if ($l.hasLineColumn() && $l.getBeginLine() <= $line && $l.getEndLine() >= $line) {
        cistring.addForLineNumber($line, $result);
      }
      if ($l.getBeginLine() > $line) {
        return;
      }
      
    }
  
    @Override
    public boolean equals(Object o) {
      if (!(o instanceof CaseInsensitiveLiteral)) {
        return false;
      }        
      CaseInsensitiveLiteral tmp = (CaseInsensitiveLiteral) o;
      return true && tmp.cistring.equals(this.cistring) ; 
    }
   
    @Override
    public int hashCode() {
      return 191 + 467 * cistring.hashCode() ; 
    } 
  
    
    @Override
    public org.rascalmpl.ast.CaseInsensitiveStringConstant getCistring() {
      return this.cistring;
    }
  
    @Override
    public boolean hasCistring() {
      return true;
    }	
  
    @Override
    public Object clone()  {
      return newInstance(getClass(), src, (IConstructor) null , clone(cistring));
    }
            
  }
  public boolean isCharacterClass() {
    return false;
  }

  static public class CharacterClass extends Sym {
    // Production: sig("CharacterClass",[arg("org.rascalmpl.ast.Class","charClass")],breakable=false)
  
    
    private final org.rascalmpl.ast.Class charClass;
  
    public CharacterClass(ISourceLocation src, IConstructor node , org.rascalmpl.ast.Class charClass) {
      super(src, node);
      
      this.charClass = charClass;
    }
  
    @Override
    public boolean isCharacterClass() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitSymCharacterClass(this);
    }
  
    @Override
    protected void addForLineNumber(int $line, java.util.List<AbstractAST> $result) {
      if (getLocation().getBeginLine() == $line) {
        $result.add(this);
      }
      ISourceLocation $l;
      
      $l = charClass.getLocation();
      if ($l.hasLineColumn() && $l.getBeginLine() <= $line && $l.getEndLine() >= $line) {
        charClass.addForLineNumber($line, $result);
      }
      if ($l.getBeginLine() > $line) {
        return;
      }
      
    }
  
    @Override
    public boolean equals(Object o) {
      if (!(o instanceof CharacterClass)) {
        return false;
      }        
      CharacterClass tmp = (CharacterClass) o;
      return true && tmp.charClass.equals(this.charClass) ; 
    }
   
    @Override
    public int hashCode() {
      return 149 + 19 * charClass.hashCode() ; 
    } 
  
    
    @Override
    public org.rascalmpl.ast.Class getCharClass() {
      return this.charClass;
    }
  
    @Override
    public boolean hasCharClass() {
      return true;
    }	
  
    @Override
    public Object clone()  {
      return newInstance(getClass(), src, (IConstructor) null , clone(charClass));
    }
            
  }
  public boolean isColumn() {
    return false;
  }

  static public class Column extends Sym {
    // Production: sig("Column",[arg("org.rascalmpl.ast.Sym","symbol"),arg("org.rascalmpl.ast.IntegerLiteral","column")],breakable=false)
  
    
    private final org.rascalmpl.ast.Sym symbol;
    private final org.rascalmpl.ast.IntegerLiteral column;
  
    public Column(ISourceLocation src, IConstructor node , org.rascalmpl.ast.Sym symbol,  org.rascalmpl.ast.IntegerLiteral column) {
      super(src, node);
      
      this.symbol = symbol;
      this.column = column;
    }
  
    @Override
    public boolean isColumn() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitSymColumn(this);
    }
  
    @Override
    protected void addForLineNumber(int $line, java.util.List<AbstractAST> $result) {
      if (getLocation().getBeginLine() == $line) {
        $result.add(this);
      }
      ISourceLocation $l;
      
      $l = symbol.getLocation();
      if ($l.hasLineColumn() && $l.getBeginLine() <= $line && $l.getEndLine() >= $line) {
        symbol.addForLineNumber($line, $result);
      }
      if ($l.getBeginLine() > $line) {
        return;
      }
      
      $l = column.getLocation();
      if ($l.hasLineColumn() && $l.getBeginLine() <= $line && $l.getEndLine() >= $line) {
        column.addForLineNumber($line, $result);
      }
      if ($l.getBeginLine() > $line) {
        return;
      }
      
    }
  
    @Override
    public boolean equals(Object o) {
      if (!(o instanceof Column)) {
        return false;
      }        
      Column tmp = (Column) o;
      return true && tmp.symbol.equals(this.symbol) && tmp.column.equals(this.column) ; 
    }
   
    @Override
    public int hashCode() {
      return 761 + 89 * symbol.hashCode() + 587 * column.hashCode() ; 
    } 
  
    
    @Override
    public org.rascalmpl.ast.Sym getSymbol() {
      return this.symbol;
    }
  
    @Override
    public boolean hasSymbol() {
      return true;
    }
    @Override
    public org.rascalmpl.ast.IntegerLiteral getColumn() {
      return this.column;
    }
  
    @Override
    public boolean hasColumn() {
      return true;
    }	
  
    @Override
    public Object clone()  {
      return newInstance(getClass(), src, (IConstructor) null , clone(symbol), clone(column));
    }
            
  }
  public boolean isDependAlign() {
    return false;
  }

  static public class DependAlign extends Sym {
    // Production: sig("DependAlign",[arg("org.rascalmpl.ast.Sym","symbol")],breakable=false)
  
    
    private final org.rascalmpl.ast.Sym symbol;
  
    public DependAlign(ISourceLocation src, IConstructor node , org.rascalmpl.ast.Sym symbol) {
      super(src, node);
      
      this.symbol = symbol;
    }
  
    @Override
    public boolean isDependAlign() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitSymDependAlign(this);
    }
  
    @Override
    protected void addForLineNumber(int $line, java.util.List<AbstractAST> $result) {
      if (getLocation().getBeginLine() == $line) {
        $result.add(this);
      }
      ISourceLocation $l;
      
      $l = symbol.getLocation();
      if ($l.hasLineColumn() && $l.getBeginLine() <= $line && $l.getEndLine() >= $line) {
        symbol.addForLineNumber($line, $result);
      }
      if ($l.getBeginLine() > $line) {
        return;
      }
      
    }
  
    @Override
    public boolean equals(Object o) {
      if (!(o instanceof DependAlign)) {
        return false;
      }        
      DependAlign tmp = (DependAlign) o;
      return true && tmp.symbol.equals(this.symbol) ; 
    }
   
    @Override
    public int hashCode() {
      return 67 + 191 * symbol.hashCode() ; 
    } 
  
    
    @Override
    public org.rascalmpl.ast.Sym getSymbol() {
      return this.symbol;
    }
  
    @Override
    public boolean hasSymbol() {
      return true;
    }	
  
    @Override
    public Object clone()  {
      return newInstance(getClass(), src, (IConstructor) null , clone(symbol));
    }
            
  }
  public boolean isDependAlternative() {
    return false;
  }

  static public class DependAlternative extends Sym {
    // Production: sig("DependAlternative",[arg("org.rascalmpl.ast.Expression","condition"),arg("org.rascalmpl.ast.Sym","thenPart"),arg("org.rascalmpl.ast.Sym","elsePart")],breakable=false)
  
    
    private final org.rascalmpl.ast.Expression condition;
    private final org.rascalmpl.ast.Sym thenPart;
    private final org.rascalmpl.ast.Sym elsePart;
  
    public DependAlternative(ISourceLocation src, IConstructor node , org.rascalmpl.ast.Expression condition,  org.rascalmpl.ast.Sym thenPart,  org.rascalmpl.ast.Sym elsePart) {
      super(src, node);
      
      this.condition = condition;
      this.thenPart = thenPart;
      this.elsePart = elsePart;
    }
  
    @Override
    public boolean isDependAlternative() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitSymDependAlternative(this);
    }
  
    @Override
    protected void addForLineNumber(int $line, java.util.List<AbstractAST> $result) {
      if (getLocation().getBeginLine() == $line) {
        $result.add(this);
      }
      ISourceLocation $l;
      
      $l = condition.getLocation();
      if ($l.hasLineColumn() && $l.getBeginLine() <= $line && $l.getEndLine() >= $line) {
        condition.addForLineNumber($line, $result);
      }
      if ($l.getBeginLine() > $line) {
        return;
      }
      
      $l = thenPart.getLocation();
      if ($l.hasLineColumn() && $l.getBeginLine() <= $line && $l.getEndLine() >= $line) {
        thenPart.addForLineNumber($line, $result);
      }
      if ($l.getBeginLine() > $line) {
        return;
      }
      
      $l = elsePart.getLocation();
      if ($l.hasLineColumn() && $l.getBeginLine() <= $line && $l.getEndLine() >= $line) {
        elsePart.addForLineNumber($line, $result);
      }
      if ($l.getBeginLine() > $line) {
        return;
      }
      
    }
  
    @Override
    public boolean equals(Object o) {
      if (!(o instanceof DependAlternative)) {
        return false;
      }        
      DependAlternative tmp = (DependAlternative) o;
      return true && tmp.condition.equals(this.condition) && tmp.thenPart.equals(this.thenPart) && tmp.elsePart.equals(this.elsePart) ; 
    }
   
    @Override
    public int hashCode() {
      return 577 + 263 * condition.hashCode() + 647 * thenPart.hashCode() + 991 * elsePart.hashCode() ; 
    } 
  
    
    @Override
    public org.rascalmpl.ast.Expression getCondition() {
      return this.condition;
    }
  
    @Override
    public boolean hasCondition() {
      return true;
    }
    @Override
    public org.rascalmpl.ast.Sym getThenPart() {
      return this.thenPart;
    }
  
    @Override
    public boolean hasThenPart() {
      return true;
    }
    @Override
    public org.rascalmpl.ast.Sym getElsePart() {
      return this.elsePart;
    }
  
    @Override
    public boolean hasElsePart() {
      return true;
    }	
  
    @Override
    public Object clone()  {
      return newInstance(getClass(), src, (IConstructor) null , clone(condition), clone(thenPart), clone(elsePart));
    }
            
  }
  public boolean isDependCode() {
    return false;
  }

  static public class DependCode extends Sym {
    // Production: sig("DependCode",[arg("org.rascalmpl.ast.Sym","symbol"),arg("java.util.List\<org.rascalmpl.ast.Statement\>","block")],breakable=false)
  
    
    private final org.rascalmpl.ast.Sym symbol;
    private final java.util.List<org.rascalmpl.ast.Statement> block;
  
    public DependCode(ISourceLocation src, IConstructor node , org.rascalmpl.ast.Sym symbol,  java.util.List<org.rascalmpl.ast.Statement> block) {
      super(src, node);
      
      this.symbol = symbol;
      this.block = block;
    }
  
    @Override
    public boolean isDependCode() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitSymDependCode(this);
    }
  
    @Override
    protected void addForLineNumber(int $line, java.util.List<AbstractAST> $result) {
      if (getLocation().getBeginLine() == $line) {
        $result.add(this);
      }
      ISourceLocation $l;
      
      $l = symbol.getLocation();
      if ($l.hasLineColumn() && $l.getBeginLine() <= $line && $l.getEndLine() >= $line) {
        symbol.addForLineNumber($line, $result);
      }
      if ($l.getBeginLine() > $line) {
        return;
      }
      
      for (AbstractAST $elem : block) {
        $l = $elem.getLocation();
        if ($l.hasLineColumn() && $l.getBeginLine() <= $line && $l.getEndLine() >= $line) {
          $elem.addForLineNumber($line, $result);
        }
        if ($l.getBeginLine() > $line) {
          return;
        }
  
      }
    }
  
    @Override
    public boolean equals(Object o) {
      if (!(o instanceof DependCode)) {
        return false;
      }        
      DependCode tmp = (DependCode) o;
      return true && tmp.symbol.equals(this.symbol) && tmp.block.equals(this.block) ; 
    }
   
    @Override
    public int hashCode() {
      return 409 + 907 * symbol.hashCode() + 89 * block.hashCode() ; 
    } 
  
    
    @Override
    public org.rascalmpl.ast.Sym getSymbol() {
      return this.symbol;
    }
  
    @Override
    public boolean hasSymbol() {
      return true;
    }
    @Override
    public java.util.List<org.rascalmpl.ast.Statement> getBlock() {
      return this.block;
    }
  
    @Override
    public boolean hasBlock() {
      return true;
    }	
  
    @Override
    public Object clone()  {
      return newInstance(getClass(), src, (IConstructor) null , clone(symbol), clone(block));
    }
            
  }
  public boolean isDependConditionAfter() {
    return false;
  }

  static public class DependConditionAfter extends Sym {
    // Production: sig("DependConditionAfter",[arg("org.rascalmpl.ast.Sym","symbol"),arg("org.rascalmpl.ast.Expression","condition")],breakable=false)
  
    
    private final org.rascalmpl.ast.Sym symbol;
    private final org.rascalmpl.ast.Expression condition;
  
    public DependConditionAfter(ISourceLocation src, IConstructor node , org.rascalmpl.ast.Sym symbol,  org.rascalmpl.ast.Expression condition) {
      super(src, node);
      
      this.symbol = symbol;
      this.condition = condition;
    }
  
    @Override
    public boolean isDependConditionAfter() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitSymDependConditionAfter(this);
    }
  
    @Override
    protected void addForLineNumber(int $line, java.util.List<AbstractAST> $result) {
      if (getLocation().getBeginLine() == $line) {
        $result.add(this);
      }
      ISourceLocation $l;
      
      $l = symbol.getLocation();
      if ($l.hasLineColumn() && $l.getBeginLine() <= $line && $l.getEndLine() >= $line) {
        symbol.addForLineNumber($line, $result);
      }
      if ($l.getBeginLine() > $line) {
        return;
      }
      
      $l = condition.getLocation();
      if ($l.hasLineColumn() && $l.getBeginLine() <= $line && $l.getEndLine() >= $line) {
        condition.addForLineNumber($line, $result);
      }
      if ($l.getBeginLine() > $line) {
        return;
      }
      
    }
  
    @Override
    public boolean equals(Object o) {
      if (!(o instanceof DependConditionAfter)) {
        return false;
      }        
      DependConditionAfter tmp = (DependConditionAfter) o;
      return true && tmp.symbol.equals(this.symbol) && tmp.condition.equals(this.condition) ; 
    }
   
    @Override
    public int hashCode() {
      return 347 + 509 * symbol.hashCode() + 151 * condition.hashCode() ; 
    } 
  
    
    @Override
    public org.rascalmpl.ast.Sym getSymbol() {
      return this.symbol;
    }
  
    @Override
    public boolean hasSymbol() {
      return true;
    }
    @Override
    public org.rascalmpl.ast.Expression getCondition() {
      return this.condition;
    }
  
    @Override
    public boolean hasCondition() {
      return true;
    }	
  
    @Override
    public Object clone()  {
      return newInstance(getClass(), src, (IConstructor) null , clone(symbol), clone(condition));
    }
            
  }
  public boolean isDependConditionBefore() {
    return false;
  }

  static public class DependConditionBefore extends Sym {
    // Production: sig("DependConditionBefore",[arg("org.rascalmpl.ast.Expression","condition"),arg("org.rascalmpl.ast.Sym","thenPart")],breakable=false)
  
    
    private final org.rascalmpl.ast.Expression condition;
    private final org.rascalmpl.ast.Sym thenPart;
  
    public DependConditionBefore(ISourceLocation src, IConstructor node , org.rascalmpl.ast.Expression condition,  org.rascalmpl.ast.Sym thenPart) {
      super(src, node);
      
      this.condition = condition;
      this.thenPart = thenPart;
    }
  
    @Override
    public boolean isDependConditionBefore() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitSymDependConditionBefore(this);
    }
  
    @Override
    protected void addForLineNumber(int $line, java.util.List<AbstractAST> $result) {
      if (getLocation().getBeginLine() == $line) {
        $result.add(this);
      }
      ISourceLocation $l;
      
      $l = condition.getLocation();
      if ($l.hasLineColumn() && $l.getBeginLine() <= $line && $l.getEndLine() >= $line) {
        condition.addForLineNumber($line, $result);
      }
      if ($l.getBeginLine() > $line) {
        return;
      }
      
      $l = thenPart.getLocation();
      if ($l.hasLineColumn() && $l.getBeginLine() <= $line && $l.getEndLine() >= $line) {
        thenPart.addForLineNumber($line, $result);
      }
      if ($l.getBeginLine() > $line) {
        return;
      }
      
    }
  
    @Override
    public boolean equals(Object o) {
      if (!(o instanceof DependConditionBefore)) {
        return false;
      }        
      DependConditionBefore tmp = (DependConditionBefore) o;
      return true && tmp.condition.equals(this.condition) && tmp.thenPart.equals(this.thenPart) ; 
    }
   
    @Override
    public int hashCode() {
      return 599 + 29 * condition.hashCode() + 941 * thenPart.hashCode() ; 
    } 
  
    
    @Override
    public org.rascalmpl.ast.Expression getCondition() {
      return this.condition;
    }
  
    @Override
    public boolean hasCondition() {
      return true;
    }
    @Override
    public org.rascalmpl.ast.Sym getThenPart() {
      return this.thenPart;
    }
  
    @Override
    public boolean hasThenPart() {
      return true;
    }	
  
    @Override
    public Object clone()  {
      return newInstance(getClass(), src, (IConstructor) null , clone(condition), clone(thenPart));
    }
            
  }
  public boolean isDependIgnore() {
    return false;
  }

  static public class DependIgnore extends Sym {
    // Production: sig("DependIgnore",[arg("org.rascalmpl.ast.Sym","symbol")],breakable=false)
  
    
    private final org.rascalmpl.ast.Sym symbol;
  
    public DependIgnore(ISourceLocation src, IConstructor node , org.rascalmpl.ast.Sym symbol) {
      super(src, node);
      
      this.symbol = symbol;
    }
  
    @Override
    public boolean isDependIgnore() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitSymDependIgnore(this);
    }
  
    @Override
    protected void addForLineNumber(int $line, java.util.List<AbstractAST> $result) {
      if (getLocation().getBeginLine() == $line) {
        $result.add(this);
      }
      ISourceLocation $l;
      
      $l = symbol.getLocation();
      if ($l.hasLineColumn() && $l.getBeginLine() <= $line && $l.getEndLine() >= $line) {
        symbol.addForLineNumber($line, $result);
      }
      if ($l.getBeginLine() > $line) {
        return;
      }
      
    }
  
    @Override
    public boolean equals(Object o) {
      if (!(o instanceof DependIgnore)) {
        return false;
      }        
      DependIgnore tmp = (DependIgnore) o;
      return true && tmp.symbol.equals(this.symbol) ; 
    }
   
    @Override
    public int hashCode() {
      return 103 + 67 * symbol.hashCode() ; 
    } 
  
    
    @Override
    public org.rascalmpl.ast.Sym getSymbol() {
      return this.symbol;
    }
  
    @Override
    public boolean hasSymbol() {
      return true;
    }	
  
    @Override
    public Object clone()  {
      return newInstance(getClass(), src, (IConstructor) null , clone(symbol));
    }
            
  }
  public boolean isDependLoop() {
    return false;
  }

  static public class DependLoop extends Sym {
    // Production: sig("DependLoop",[arg("org.rascalmpl.ast.Expression","condition"),arg("org.rascalmpl.ast.Sym","body")],breakable=false)
  
    
    private final org.rascalmpl.ast.Expression condition;
    private final org.rascalmpl.ast.Sym body;
  
    public DependLoop(ISourceLocation src, IConstructor node , org.rascalmpl.ast.Expression condition,  org.rascalmpl.ast.Sym body) {
      super(src, node);
      
      this.condition = condition;
      this.body = body;
    }
  
    @Override
    public boolean isDependLoop() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitSymDependLoop(this);
    }
  
    @Override
    protected void addForLineNumber(int $line, java.util.List<AbstractAST> $result) {
      if (getLocation().getBeginLine() == $line) {
        $result.add(this);
      }
      ISourceLocation $l;
      
      $l = condition.getLocation();
      if ($l.hasLineColumn() && $l.getBeginLine() <= $line && $l.getEndLine() >= $line) {
        condition.addForLineNumber($line, $result);
      }
      if ($l.getBeginLine() > $line) {
        return;
      }
      
      $l = body.getLocation();
      if ($l.hasLineColumn() && $l.getBeginLine() <= $line && $l.getEndLine() >= $line) {
        body.addForLineNumber($line, $result);
      }
      if ($l.getBeginLine() > $line) {
        return;
      }
      
    }
  
    @Override
    public boolean equals(Object o) {
      if (!(o instanceof DependLoop)) {
        return false;
      }        
      DependLoop tmp = (DependLoop) o;
      return true && tmp.condition.equals(this.condition) && tmp.body.equals(this.body) ; 
    }
   
    @Override
    public int hashCode() {
      return 769 + 149 * condition.hashCode() + 541 * body.hashCode() ; 
    } 
  
    
    @Override
    public org.rascalmpl.ast.Expression getCondition() {
      return this.condition;
    }
  
    @Override
    public boolean hasCondition() {
      return true;
    }
    @Override
    public org.rascalmpl.ast.Sym getBody() {
      return this.body;
    }
  
    @Override
    public boolean hasBody() {
      return true;
    }	
  
    @Override
    public Object clone()  {
      return newInstance(getClass(), src, (IConstructor) null , clone(condition), clone(body));
    }
            
  }
  public boolean isDependNonterminal() {
    return false;
  }

  static public class DependNonterminal extends Sym {
    // Production: sig("DependNonterminal",[arg("org.rascalmpl.ast.Nonterminal","nonterminal"),arg("java.util.List\<org.rascalmpl.ast.Expression\>","arguments"),arg("org.rascalmpl.ast.KeywordArguments_Expression","keywordArguments")],breakable=false)
  
    
    private final org.rascalmpl.ast.Nonterminal nonterminal;
    private final java.util.List<org.rascalmpl.ast.Expression> arguments;
    private final org.rascalmpl.ast.KeywordArguments_Expression keywordArguments;
  
    public DependNonterminal(ISourceLocation src, IConstructor node , org.rascalmpl.ast.Nonterminal nonterminal,  java.util.List<org.rascalmpl.ast.Expression> arguments,  org.rascalmpl.ast.KeywordArguments_Expression keywordArguments) {
      super(src, node);
      
      this.nonterminal = nonterminal;
      this.arguments = arguments;
      this.keywordArguments = keywordArguments;
    }
  
    @Override
    public boolean isDependNonterminal() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitSymDependNonterminal(this);
    }
  
    @Override
    protected void addForLineNumber(int $line, java.util.List<AbstractAST> $result) {
      if (getLocation().getBeginLine() == $line) {
        $result.add(this);
      }
      ISourceLocation $l;
      
      $l = nonterminal.getLocation();
      if ($l.hasLineColumn() && $l.getBeginLine() <= $line && $l.getEndLine() >= $line) {
        nonterminal.addForLineNumber($line, $result);
      }
      if ($l.getBeginLine() > $line) {
        return;
      }
      
      for (AbstractAST $elem : arguments) {
        $l = $elem.getLocation();
        if ($l.hasLineColumn() && $l.getBeginLine() <= $line && $l.getEndLine() >= $line) {
          $elem.addForLineNumber($line, $result);
        }
        if ($l.getBeginLine() > $line) {
          return;
        }
  
      }
      $l = keywordArguments.getLocation();
      if ($l.hasLineColumn() && $l.getBeginLine() <= $line && $l.getEndLine() >= $line) {
        keywordArguments.addForLineNumber($line, $result);
      }
      if ($l.getBeginLine() > $line) {
        return;
      }
      
    }
  
    @Override
    public boolean equals(Object o) {
      if (!(o instanceof DependNonterminal)) {
        return false;
      }        
      DependNonterminal tmp = (DependNonterminal) o;
      return true && tmp.nonterminal.equals(this.nonterminal) && tmp.arguments.equals(this.arguments) && tmp.keywordArguments.equals(this.keywordArguments) ; 
    }
   
    @Override
    public int hashCode() {
      return 353 + 23 * nonterminal.hashCode() + 787 * arguments.hashCode() + 863 * keywordArguments.hashCode() ; 
    } 
  
    
    @Override
    public org.rascalmpl.ast.Nonterminal getNonterminal() {
      return this.nonterminal;
    }
  
    @Override
    public boolean hasNonterminal() {
      return true;
    }
    @Override
    public java.util.List<org.rascalmpl.ast.Expression> getArguments() {
      return this.arguments;
    }
  
    @Override
    public boolean hasArguments() {
      return true;
    }
    @Override
    public org.rascalmpl.ast.KeywordArguments_Expression getKeywordArguments() {
      return this.keywordArguments;
    }
  
    @Override
    public boolean hasKeywordArguments() {
      return true;
    }	
  
    @Override
    public Object clone()  {
      return newInstance(getClass(), src, (IConstructor) null , clone(nonterminal), clone(arguments), clone(keywordArguments));
    }
            
  }
  public boolean isDependOffside() {
    return false;
  }

  static public class DependOffside extends Sym {
    // Production: sig("DependOffside",[arg("org.rascalmpl.ast.Sym","symbol")],breakable=false)
  
    
    private final org.rascalmpl.ast.Sym symbol;
  
    public DependOffside(ISourceLocation src, IConstructor node , org.rascalmpl.ast.Sym symbol) {
      super(src, node);
      
      this.symbol = symbol;
    }
  
    @Override
    public boolean isDependOffside() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitSymDependOffside(this);
    }
  
    @Override
    protected void addForLineNumber(int $line, java.util.List<AbstractAST> $result) {
      if (getLocation().getBeginLine() == $line) {
        $result.add(this);
      }
      ISourceLocation $l;
      
      $l = symbol.getLocation();
      if ($l.hasLineColumn() && $l.getBeginLine() <= $line && $l.getEndLine() >= $line) {
        symbol.addForLineNumber($line, $result);
      }
      if ($l.getBeginLine() > $line) {
        return;
      }
      
    }
  
    @Override
    public boolean equals(Object o) {
      if (!(o instanceof DependOffside)) {
        return false;
      }        
      DependOffside tmp = (DependOffside) o;
      return true && tmp.symbol.equals(this.symbol) ; 
    }
   
    @Override
    public int hashCode() {
      return 557 + 61 * symbol.hashCode() ; 
    } 
  
    
    @Override
    public org.rascalmpl.ast.Sym getSymbol() {
      return this.symbol;
    }
  
    @Override
    public boolean hasSymbol() {
      return true;
    }	
  
    @Override
    public Object clone()  {
      return newInstance(getClass(), src, (IConstructor) null , clone(symbol));
    }
            
  }
  public boolean isDependParametrized() {
    return false;
  }

  static public class DependParametrized extends Sym {
    // Production: sig("DependParametrized",[arg("org.rascalmpl.ast.Nonterminal","nonterminal"),arg("java.util.List\<org.rascalmpl.ast.Sym\>","parameters"),arg("java.util.List\<org.rascalmpl.ast.Expression\>","arguments"),arg("org.rascalmpl.ast.KeywordArguments_Expression","keywordArguments")],breakable=false)
  
    
    private final org.rascalmpl.ast.Nonterminal nonterminal;
    private final java.util.List<org.rascalmpl.ast.Sym> parameters;
    private final java.util.List<org.rascalmpl.ast.Expression> arguments;
    private final org.rascalmpl.ast.KeywordArguments_Expression keywordArguments;
  
    public DependParametrized(ISourceLocation src, IConstructor node , org.rascalmpl.ast.Nonterminal nonterminal,  java.util.List<org.rascalmpl.ast.Sym> parameters,  java.util.List<org.rascalmpl.ast.Expression> arguments,  org.rascalmpl.ast.KeywordArguments_Expression keywordArguments) {
      super(src, node);
      
      this.nonterminal = nonterminal;
      this.parameters = parameters;
      this.arguments = arguments;
      this.keywordArguments = keywordArguments;
    }
  
    @Override
    public boolean isDependParametrized() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitSymDependParametrized(this);
    }
  
    @Override
    protected void addForLineNumber(int $line, java.util.List<AbstractAST> $result) {
      if (getLocation().getBeginLine() == $line) {
        $result.add(this);
      }
      ISourceLocation $l;
      
      $l = nonterminal.getLocation();
      if ($l.hasLineColumn() && $l.getBeginLine() <= $line && $l.getEndLine() >= $line) {
        nonterminal.addForLineNumber($line, $result);
      }
      if ($l.getBeginLine() > $line) {
        return;
      }
      
      for (AbstractAST $elem : parameters) {
        $l = $elem.getLocation();
        if ($l.hasLineColumn() && $l.getBeginLine() <= $line && $l.getEndLine() >= $line) {
          $elem.addForLineNumber($line, $result);
        }
        if ($l.getBeginLine() > $line) {
          return;
        }
  
      }
      for (AbstractAST $elem : arguments) {
        $l = $elem.getLocation();
        if ($l.hasLineColumn() && $l.getBeginLine() <= $line && $l.getEndLine() >= $line) {
          $elem.addForLineNumber($line, $result);
        }
        if ($l.getBeginLine() > $line) {
          return;
        }
  
      }
      $l = keywordArguments.getLocation();
      if ($l.hasLineColumn() && $l.getBeginLine() <= $line && $l.getEndLine() >= $line) {
        keywordArguments.addForLineNumber($line, $result);
      }
      if ($l.getBeginLine() > $line) {
        return;
      }
      
    }
  
    @Override
    public boolean equals(Object o) {
      if (!(o instanceof DependParametrized)) {
        return false;
      }        
      DependParametrized tmp = (DependParametrized) o;
      return true && tmp.nonterminal.equals(this.nonterminal) && tmp.parameters.equals(this.parameters) && tmp.arguments.equals(this.arguments) && tmp.keywordArguments.equals(this.keywordArguments) ; 
    }
   
    @Override
    public int hashCode() {
      return 281 + 263 * nonterminal.hashCode() + 19 * parameters.hashCode() + 61 * arguments.hashCode() + 241 * keywordArguments.hashCode() ; 
    } 
  
    
    @Override
    public org.rascalmpl.ast.Nonterminal getNonterminal() {
      return this.nonterminal;
    }
  
    @Override
    public boolean hasNonterminal() {
      return true;
    }
    @Override
    public java.util.List<org.rascalmpl.ast.Sym> getParameters() {
      return this.parameters;
    }
  
    @Override
    public boolean hasParameters() {
      return true;
    }
    @Override
    public java.util.List<org.rascalmpl.ast.Expression> getArguments() {
      return this.arguments;
    }
  
    @Override
    public boolean hasArguments() {
      return true;
    }
    @Override
    public org.rascalmpl.ast.KeywordArguments_Expression getKeywordArguments() {
      return this.keywordArguments;
    }
  
    @Override
    public boolean hasKeywordArguments() {
      return true;
    }	
  
    @Override
    public Object clone()  {
      return newInstance(getClass(), src, (IConstructor) null , clone(nonterminal), clone(parameters), clone(arguments), clone(keywordArguments));
    }
            
  }
  public boolean isDependScope() {
    return false;
  }

  static public class DependScope extends Sym {
    // Production: sig("DependScope",[arg("java.util.List\<org.rascalmpl.ast.Sym\>","symbols")],breakable=false)
  
    
    private final java.util.List<org.rascalmpl.ast.Sym> symbols;
  
    public DependScope(ISourceLocation src, IConstructor node , java.util.List<org.rascalmpl.ast.Sym> symbols) {
      super(src, node);
      
      this.symbols = symbols;
    }
  
    @Override
    public boolean isDependScope() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitSymDependScope(this);
    }
  
    @Override
    protected void addForLineNumber(int $line, java.util.List<AbstractAST> $result) {
      if (getLocation().getBeginLine() == $line) {
        $result.add(this);
      }
      ISourceLocation $l;
      
      for (AbstractAST $elem : symbols) {
        $l = $elem.getLocation();
        if ($l.hasLineColumn() && $l.getBeginLine() <= $line && $l.getEndLine() >= $line) {
          $elem.addForLineNumber($line, $result);
        }
        if ($l.getBeginLine() > $line) {
          return;
        }
  
      }
    }
  
    @Override
    public boolean equals(Object o) {
      if (!(o instanceof DependScope)) {
        return false;
      }        
      DependScope tmp = (DependScope) o;
      return true && tmp.symbols.equals(this.symbols) ; 
    }
   
    @Override
    public int hashCode() {
      return 409 + 443 * symbols.hashCode() ; 
    } 
  
    
    @Override
    public java.util.List<org.rascalmpl.ast.Sym> getSymbols() {
      return this.symbols;
    }
  
    @Override
    public boolean hasSymbols() {
      return true;
    }	
  
    @Override
    public Object clone()  {
      return newInstance(getClass(), src, (IConstructor) null , clone(symbols));
    }
            
  }
  public boolean isEmpty() {
    return false;
  }

  static public class Empty extends Sym {
    // Production: sig("Empty",[],breakable=false)
  
    
  
    public Empty(ISourceLocation src, IConstructor node ) {
      super(src, node);
      
    }
  
    @Override
    public boolean isEmpty() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitSymEmpty(this);
    }
  
    @Override
    protected void addForLineNumber(int $line, java.util.List<AbstractAST> $result) {
      if (getLocation().getBeginLine() == $line) {
        $result.add(this);
      }
      ISourceLocation $l;
      
    }
  
    @Override
    public boolean equals(Object o) {
      if (!(o instanceof Empty)) {
        return false;
      }        
      Empty tmp = (Empty) o;
      return true ; 
    }
   
    @Override
    public int hashCode() {
      return 11 ; 
    } 
  
    	
  
    @Override
    public Object clone()  {
      return newInstance(getClass(), src, (IConstructor) null );
    }
            
  }
  public boolean isEndOfLine() {
    return false;
  }

  static public class EndOfLine extends Sym {
    // Production: sig("EndOfLine",[arg("org.rascalmpl.ast.Sym","symbol")],breakable=false)
  
    
    private final org.rascalmpl.ast.Sym symbol;
  
    public EndOfLine(ISourceLocation src, IConstructor node , org.rascalmpl.ast.Sym symbol) {
      super(src, node);
      
      this.symbol = symbol;
    }
  
    @Override
    public boolean isEndOfLine() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitSymEndOfLine(this);
    }
  
    @Override
    protected void addForLineNumber(int $line, java.util.List<AbstractAST> $result) {
      if (getLocation().getBeginLine() == $line) {
        $result.add(this);
      }
      ISourceLocation $l;
      
      $l = symbol.getLocation();
      if ($l.hasLineColumn() && $l.getBeginLine() <= $line && $l.getEndLine() >= $line) {
        symbol.addForLineNumber($line, $result);
      }
      if ($l.getBeginLine() > $line) {
        return;
      }
      
    }
  
    @Override
    public boolean equals(Object o) {
      if (!(o instanceof EndOfLine)) {
        return false;
      }        
      EndOfLine tmp = (EndOfLine) o;
      return true && tmp.symbol.equals(this.symbol) ; 
    }
   
    @Override
    public int hashCode() {
      return 389 + 229 * symbol.hashCode() ; 
    } 
  
    
    @Override
    public org.rascalmpl.ast.Sym getSymbol() {
      return this.symbol;
    }
  
    @Override
    public boolean hasSymbol() {
      return true;
    }	
  
    @Override
    public Object clone()  {
      return newInstance(getClass(), src, (IConstructor) null , clone(symbol));
    }
            
  }
  public boolean isExcept() {
    return false;
  }

  static public class Except extends Sym {
    // Production: sig("Except",[arg("org.rascalmpl.ast.Sym","symbol"),arg("org.rascalmpl.ast.NonterminalLabel","label")],breakable=false)
  
    
    private final org.rascalmpl.ast.Sym symbol;
    private final org.rascalmpl.ast.NonterminalLabel label;
  
    public Except(ISourceLocation src, IConstructor node , org.rascalmpl.ast.Sym symbol,  org.rascalmpl.ast.NonterminalLabel label) {
      super(src, node);
      
      this.symbol = symbol;
      this.label = label;
    }
  
    @Override
    public boolean isExcept() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitSymExcept(this);
    }
  
    @Override
    protected void addForLineNumber(int $line, java.util.List<AbstractAST> $result) {
      if (getLocation().getBeginLine() == $line) {
        $result.add(this);
      }
      ISourceLocation $l;
      
      $l = symbol.getLocation();
      if ($l.hasLineColumn() && $l.getBeginLine() <= $line && $l.getEndLine() >= $line) {
        symbol.addForLineNumber($line, $result);
      }
      if ($l.getBeginLine() > $line) {
        return;
      }
      
      $l = label.getLocation();
      if ($l.hasLineColumn() && $l.getBeginLine() <= $line && $l.getEndLine() >= $line) {
        label.addForLineNumber($line, $result);
      }
      if ($l.getBeginLine() > $line) {
        return;
      }
      
    }
  
    @Override
    public boolean equals(Object o) {
      if (!(o instanceof Except)) {
        return false;
      }        
      Except tmp = (Except) o;
      return true && tmp.symbol.equals(this.symbol) && tmp.label.equals(this.label) ; 
    }
   
    @Override
    public int hashCode() {
      return 193 + 617 * symbol.hashCode() + 521 * label.hashCode() ; 
    } 
  
    
    @Override
    public org.rascalmpl.ast.Sym getSymbol() {
      return this.symbol;
    }
  
    @Override
    public boolean hasSymbol() {
      return true;
    }
    @Override
    public org.rascalmpl.ast.NonterminalLabel getLabel() {
      return this.label;
    }
  
    @Override
    public boolean hasLabel() {
      return true;
    }	
  
    @Override
    public Object clone()  {
      return newInstance(getClass(), src, (IConstructor) null , clone(symbol), clone(label));
    }
            
  }
  public boolean isFollow() {
    return false;
  }

  static public class Follow extends Sym {
    // Production: sig("Follow",[arg("org.rascalmpl.ast.Sym","symbol"),arg("org.rascalmpl.ast.Sym","match")],breakable=false)
  
    
    private final org.rascalmpl.ast.Sym symbol;
    private final org.rascalmpl.ast.Sym match;
  
    public Follow(ISourceLocation src, IConstructor node , org.rascalmpl.ast.Sym symbol,  org.rascalmpl.ast.Sym match) {
      super(src, node);
      
      this.symbol = symbol;
      this.match = match;
    }
  
    @Override
    public boolean isFollow() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitSymFollow(this);
    }
  
    @Override
    protected void addForLineNumber(int $line, java.util.List<AbstractAST> $result) {
      if (getLocation().getBeginLine() == $line) {
        $result.add(this);
      }
      ISourceLocation $l;
      
      $l = symbol.getLocation();
      if ($l.hasLineColumn() && $l.getBeginLine() <= $line && $l.getEndLine() >= $line) {
        symbol.addForLineNumber($line, $result);
      }
      if ($l.getBeginLine() > $line) {
        return;
      }
      
      $l = match.getLocation();
      if ($l.hasLineColumn() && $l.getBeginLine() <= $line && $l.getEndLine() >= $line) {
        match.addForLineNumber($line, $result);
      }
      if ($l.getBeginLine() > $line) {
        return;
      }
      
    }
  
    @Override
    public boolean equals(Object o) {
      if (!(o instanceof Follow)) {
        return false;
      }        
      Follow tmp = (Follow) o;
      return true && tmp.symbol.equals(this.symbol) && tmp.match.equals(this.match) ; 
    }
   
    @Override
    public int hashCode() {
      return 269 + 839 * symbol.hashCode() + 743 * match.hashCode() ; 
    } 
  
    
    @Override
    public org.rascalmpl.ast.Sym getSymbol() {
      return this.symbol;
    }
  
    @Override
    public boolean hasSymbol() {
      return true;
    }
    @Override
    public org.rascalmpl.ast.Sym getMatch() {
      return this.match;
    }
  
    @Override
    public boolean hasMatch() {
      return true;
    }	
  
    @Override
    public Object clone()  {
      return newInstance(getClass(), src, (IConstructor) null , clone(symbol), clone(match));
    }
            
  }
  public boolean isIter() {
    return false;
  }

  static public class Iter extends Sym {
    // Production: sig("Iter",[arg("org.rascalmpl.ast.Sym","symbol")],breakable=false)
  
    
    private final org.rascalmpl.ast.Sym symbol;
  
    public Iter(ISourceLocation src, IConstructor node , org.rascalmpl.ast.Sym symbol) {
      super(src, node);
      
      this.symbol = symbol;
    }
  
    @Override
    public boolean isIter() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitSymIter(this);
    }
  
    @Override
    protected void addForLineNumber(int $line, java.util.List<AbstractAST> $result) {
      if (getLocation().getBeginLine() == $line) {
        $result.add(this);
      }
      ISourceLocation $l;
      
      $l = symbol.getLocation();
      if ($l.hasLineColumn() && $l.getBeginLine() <= $line && $l.getEndLine() >= $line) {
        symbol.addForLineNumber($line, $result);
      }
      if ($l.getBeginLine() > $line) {
        return;
      }
      
    }
  
    @Override
    public boolean equals(Object o) {
      if (!(o instanceof Iter)) {
        return false;
      }        
      Iter tmp = (Iter) o;
      return true && tmp.symbol.equals(this.symbol) ; 
    }
   
    @Override
    public int hashCode() {
      return 751 + 241 * symbol.hashCode() ; 
    } 
  
    
    @Override
    public org.rascalmpl.ast.Sym getSymbol() {
      return this.symbol;
    }
  
    @Override
    public boolean hasSymbol() {
      return true;
    }	
  
    @Override
    public Object clone()  {
      return newInstance(getClass(), src, (IConstructor) null , clone(symbol));
    }
            
  }
  public boolean isIterSep() {
    return false;
  }

  static public class IterSep extends Sym {
    // Production: sig("IterSep",[arg("org.rascalmpl.ast.Sym","symbol"),arg("org.rascalmpl.ast.Sym","sep")],breakable=false)
  
    
    private final org.rascalmpl.ast.Sym symbol;
    private final org.rascalmpl.ast.Sym sep;
  
    public IterSep(ISourceLocation src, IConstructor node , org.rascalmpl.ast.Sym symbol,  org.rascalmpl.ast.Sym sep) {
      super(src, node);
      
      this.symbol = symbol;
      this.sep = sep;
    }
  
    @Override
    public boolean isIterSep() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitSymIterSep(this);
    }
  
    @Override
    protected void addForLineNumber(int $line, java.util.List<AbstractAST> $result) {
      if (getLocation().getBeginLine() == $line) {
        $result.add(this);
      }
      ISourceLocation $l;
      
      $l = symbol.getLocation();
      if ($l.hasLineColumn() && $l.getBeginLine() <= $line && $l.getEndLine() >= $line) {
        symbol.addForLineNumber($line, $result);
      }
      if ($l.getBeginLine() > $line) {
        return;
      }
      
      $l = sep.getLocation();
      if ($l.hasLineColumn() && $l.getBeginLine() <= $line && $l.getEndLine() >= $line) {
        sep.addForLineNumber($line, $result);
      }
      if ($l.getBeginLine() > $line) {
        return;
      }
      
    }
  
    @Override
    public boolean equals(Object o) {
      if (!(o instanceof IterSep)) {
        return false;
      }        
      IterSep tmp = (IterSep) o;
      return true && tmp.symbol.equals(this.symbol) && tmp.sep.equals(this.sep) ; 
    }
   
    @Override
    public int hashCode() {
      return 911 + 827 * symbol.hashCode() + 29 * sep.hashCode() ; 
    } 
  
    
    @Override
    public org.rascalmpl.ast.Sym getSymbol() {
      return this.symbol;
    }
  
    @Override
    public boolean hasSymbol() {
      return true;
    }
    @Override
    public org.rascalmpl.ast.Sym getSep() {
      return this.sep;
    }
  
    @Override
    public boolean hasSep() {
      return true;
    }	
  
    @Override
    public Object clone()  {
      return newInstance(getClass(), src, (IConstructor) null , clone(symbol), clone(sep));
    }
            
  }
  public boolean isIterStar() {
    return false;
  }

  static public class IterStar extends Sym {
    // Production: sig("IterStar",[arg("org.rascalmpl.ast.Sym","symbol")],breakable=false)
  
    
    private final org.rascalmpl.ast.Sym symbol;
  
    public IterStar(ISourceLocation src, IConstructor node , org.rascalmpl.ast.Sym symbol) {
      super(src, node);
      
      this.symbol = symbol;
    }
  
    @Override
    public boolean isIterStar() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitSymIterStar(this);
    }
  
    @Override
    protected void addForLineNumber(int $line, java.util.List<AbstractAST> $result) {
      if (getLocation().getBeginLine() == $line) {
        $result.add(this);
      }
      ISourceLocation $l;
      
      $l = symbol.getLocation();
      if ($l.hasLineColumn() && $l.getBeginLine() <= $line && $l.getEndLine() >= $line) {
        symbol.addForLineNumber($line, $result);
      }
      if ($l.getBeginLine() > $line) {
        return;
      }
      
    }
  
    @Override
    public boolean equals(Object o) {
      if (!(o instanceof IterStar)) {
        return false;
      }        
      IterStar tmp = (IterStar) o;
      return true && tmp.symbol.equals(this.symbol) ; 
    }
   
    @Override
    public int hashCode() {
      return 947 + 883 * symbol.hashCode() ; 
    } 
  
    
    @Override
    public org.rascalmpl.ast.Sym getSymbol() {
      return this.symbol;
    }
  
    @Override
    public boolean hasSymbol() {
      return true;
    }	
  
    @Override
    public Object clone()  {
      return newInstance(getClass(), src, (IConstructor) null , clone(symbol));
    }
            
  }
  public boolean isIterStarSep() {
    return false;
  }

  static public class IterStarSep extends Sym {
    // Production: sig("IterStarSep",[arg("org.rascalmpl.ast.Sym","symbol"),arg("org.rascalmpl.ast.Sym","sep")],breakable=false)
  
    
    private final org.rascalmpl.ast.Sym symbol;
    private final org.rascalmpl.ast.Sym sep;
  
    public IterStarSep(ISourceLocation src, IConstructor node , org.rascalmpl.ast.Sym symbol,  org.rascalmpl.ast.Sym sep) {
      super(src, node);
      
      this.symbol = symbol;
      this.sep = sep;
    }
  
    @Override
    public boolean isIterStarSep() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitSymIterStarSep(this);
    }
  
    @Override
    protected void addForLineNumber(int $line, java.util.List<AbstractAST> $result) {
      if (getLocation().getBeginLine() == $line) {
        $result.add(this);
      }
      ISourceLocation $l;
      
      $l = symbol.getLocation();
      if ($l.hasLineColumn() && $l.getBeginLine() <= $line && $l.getEndLine() >= $line) {
        symbol.addForLineNumber($line, $result);
      }
      if ($l.getBeginLine() > $line) {
        return;
      }
      
      $l = sep.getLocation();
      if ($l.hasLineColumn() && $l.getBeginLine() <= $line && $l.getEndLine() >= $line) {
        sep.addForLineNumber($line, $result);
      }
      if ($l.getBeginLine() > $line) {
        return;
      }
      
    }
  
    @Override
    public boolean equals(Object o) {
      if (!(o instanceof IterStarSep)) {
        return false;
      }        
      IterStarSep tmp = (IterStarSep) o;
      return true && tmp.symbol.equals(this.symbol) && tmp.sep.equals(this.sep) ; 
    }
   
    @Override
    public int hashCode() {
      return 743 + 313 * symbol.hashCode() + 607 * sep.hashCode() ; 
    } 
  
    
    @Override
    public org.rascalmpl.ast.Sym getSymbol() {
      return this.symbol;
    }
  
    @Override
    public boolean hasSymbol() {
      return true;
    }
    @Override
    public org.rascalmpl.ast.Sym getSep() {
      return this.sep;
    }
  
    @Override
    public boolean hasSep() {
      return true;
    }	
  
    @Override
    public Object clone()  {
      return newInstance(getClass(), src, (IConstructor) null , clone(symbol), clone(sep));
    }
            
  }
  public boolean isLabeled() {
    return false;
  }

  static public class Labeled extends Sym {
    // Production: sig("Labeled",[arg("org.rascalmpl.ast.Sym","symbol"),arg("org.rascalmpl.ast.NonterminalLabel","label")],breakable=false)
  
    
    private final org.rascalmpl.ast.Sym symbol;
    private final org.rascalmpl.ast.NonterminalLabel label;
  
    public Labeled(ISourceLocation src, IConstructor node , org.rascalmpl.ast.Sym symbol,  org.rascalmpl.ast.NonterminalLabel label) {
      super(src, node);
      
      this.symbol = symbol;
      this.label = label;
    }
  
    @Override
    public boolean isLabeled() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitSymLabeled(this);
    }
  
    @Override
    protected void addForLineNumber(int $line, java.util.List<AbstractAST> $result) {
      if (getLocation().getBeginLine() == $line) {
        $result.add(this);
      }
      ISourceLocation $l;
      
      $l = symbol.getLocation();
      if ($l.hasLineColumn() && $l.getBeginLine() <= $line && $l.getEndLine() >= $line) {
        symbol.addForLineNumber($line, $result);
      }
      if ($l.getBeginLine() > $line) {
        return;
      }
      
      $l = label.getLocation();
      if ($l.hasLineColumn() && $l.getBeginLine() <= $line && $l.getEndLine() >= $line) {
        label.addForLineNumber($line, $result);
      }
      if ($l.getBeginLine() > $line) {
        return;
      }
      
    }
  
    @Override
    public boolean equals(Object o) {
      if (!(o instanceof Labeled)) {
        return false;
      }        
      Labeled tmp = (Labeled) o;
      return true && tmp.symbol.equals(this.symbol) && tmp.label.equals(this.label) ; 
    }
   
    @Override
    public int hashCode() {
      return 727 + 883 * symbol.hashCode() + 617 * label.hashCode() ; 
    } 
  
    
    @Override
    public org.rascalmpl.ast.Sym getSymbol() {
      return this.symbol;
    }
  
    @Override
    public boolean hasSymbol() {
      return true;
    }
    @Override
    public org.rascalmpl.ast.NonterminalLabel getLabel() {
      return this.label;
    }
  
    @Override
    public boolean hasLabel() {
      return true;
    }	
  
    @Override
    public Object clone()  {
      return newInstance(getClass(), src, (IConstructor) null , clone(symbol), clone(label));
    }
            
  }
  public boolean isLiteral() {
    return false;
  }

  static public class Literal extends Sym {
    // Production: sig("Literal",[arg("org.rascalmpl.ast.StringConstant","string")],breakable=false)
  
    
    private final org.rascalmpl.ast.StringConstant string;
  
    public Literal(ISourceLocation src, IConstructor node , org.rascalmpl.ast.StringConstant string) {
      super(src, node);
      
      this.string = string;
    }
  
    @Override
    public boolean isLiteral() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitSymLiteral(this);
    }
  
    @Override
    protected void addForLineNumber(int $line, java.util.List<AbstractAST> $result) {
      if (getLocation().getBeginLine() == $line) {
        $result.add(this);
      }
      ISourceLocation $l;
      
      $l = string.getLocation();
      if ($l.hasLineColumn() && $l.getBeginLine() <= $line && $l.getEndLine() >= $line) {
        string.addForLineNumber($line, $result);
      }
      if ($l.getBeginLine() > $line) {
        return;
      }
      
    }
  
    @Override
    public boolean equals(Object o) {
      if (!(o instanceof Literal)) {
        return false;
      }        
      Literal tmp = (Literal) o;
      return true && tmp.string.equals(this.string) ; 
    }
   
    @Override
    public int hashCode() {
      return 233 + 263 * string.hashCode() ; 
    } 
  
    
    @Override
    public org.rascalmpl.ast.StringConstant getString() {
      return this.string;
    }
  
    @Override
    public boolean hasString() {
      return true;
    }	
  
    @Override
    public Object clone()  {
      return newInstance(getClass(), src, (IConstructor) null , clone(string));
    }
            
  }
  public boolean isNonterminal() {
    return false;
  }

  static public class Nonterminal extends Sym {
    // Production: sig("Nonterminal",[arg("org.rascalmpl.ast.Nonterminal","nonterminal")],breakable=false)
  
    
    private final org.rascalmpl.ast.Nonterminal nonterminal;
  
    public Nonterminal(ISourceLocation src, IConstructor node , org.rascalmpl.ast.Nonterminal nonterminal) {
      super(src, node);
      
      this.nonterminal = nonterminal;
    }
  
    @Override
    public boolean isNonterminal() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitSymNonterminal(this);
    }
  
    @Override
    protected void addForLineNumber(int $line, java.util.List<AbstractAST> $result) {
      if (getLocation().getBeginLine() == $line) {
        $result.add(this);
      }
      ISourceLocation $l;
      
      $l = nonterminal.getLocation();
      if ($l.hasLineColumn() && $l.getBeginLine() <= $line && $l.getEndLine() >= $line) {
        nonterminal.addForLineNumber($line, $result);
      }
      if ($l.getBeginLine() > $line) {
        return;
      }
      
    }
  
    @Override
    public boolean equals(Object o) {
      if (!(o instanceof Nonterminal)) {
        return false;
      }        
      Nonterminal tmp = (Nonterminal) o;
      return true && tmp.nonterminal.equals(this.nonterminal) ; 
    }
   
    @Override
    public int hashCode() {
      return 163 + 683 * nonterminal.hashCode() ; 
    } 
  
    
    @Override
    public org.rascalmpl.ast.Nonterminal getNonterminal() {
      return this.nonterminal;
    }
  
    @Override
    public boolean hasNonterminal() {
      return true;
    }	
  
    @Override
    public Object clone()  {
      return newInstance(getClass(), src, (IConstructor) null , clone(nonterminal));
    }
            
  }
  public boolean isNotFollow() {
    return false;
  }

  static public class NotFollow extends Sym {
    // Production: sig("NotFollow",[arg("org.rascalmpl.ast.Sym","symbol"),arg("org.rascalmpl.ast.Sym","match")],breakable=false)
  
    
    private final org.rascalmpl.ast.Sym symbol;
    private final org.rascalmpl.ast.Sym match;
  
    public NotFollow(ISourceLocation src, IConstructor node , org.rascalmpl.ast.Sym symbol,  org.rascalmpl.ast.Sym match) {
      super(src, node);
      
      this.symbol = symbol;
      this.match = match;
    }
  
    @Override
    public boolean isNotFollow() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitSymNotFollow(this);
    }
  
    @Override
    protected void addForLineNumber(int $line, java.util.List<AbstractAST> $result) {
      if (getLocation().getBeginLine() == $line) {
        $result.add(this);
      }
      ISourceLocation $l;
      
      $l = symbol.getLocation();
      if ($l.hasLineColumn() && $l.getBeginLine() <= $line && $l.getEndLine() >= $line) {
        symbol.addForLineNumber($line, $result);
      }
      if ($l.getBeginLine() > $line) {
        return;
      }
      
      $l = match.getLocation();
      if ($l.hasLineColumn() && $l.getBeginLine() <= $line && $l.getEndLine() >= $line) {
        match.addForLineNumber($line, $result);
      }
      if ($l.getBeginLine() > $line) {
        return;
      }
      
    }
  
    @Override
    public boolean equals(Object o) {
      if (!(o instanceof NotFollow)) {
        return false;
      }        
      NotFollow tmp = (NotFollow) o;
      return true && tmp.symbol.equals(this.symbol) && tmp.match.equals(this.match) ; 
    }
   
    @Override
    public int hashCode() {
      return 661 + 499 * symbol.hashCode() + 769 * match.hashCode() ; 
    } 
  
    
    @Override
    public org.rascalmpl.ast.Sym getSymbol() {
      return this.symbol;
    }
  
    @Override
    public boolean hasSymbol() {
      return true;
    }
    @Override
    public org.rascalmpl.ast.Sym getMatch() {
      return this.match;
    }
  
    @Override
    public boolean hasMatch() {
      return true;
    }	
  
    @Override
    public Object clone()  {
      return newInstance(getClass(), src, (IConstructor) null , clone(symbol), clone(match));
    }
            
  }
  public boolean isNotPrecede() {
    return false;
  }

  static public class NotPrecede extends Sym {
    // Production: sig("NotPrecede",[arg("org.rascalmpl.ast.Sym","match"),arg("org.rascalmpl.ast.Sym","symbol")],breakable=false)
  
    
    private final org.rascalmpl.ast.Sym match;
    private final org.rascalmpl.ast.Sym symbol;
  
    public NotPrecede(ISourceLocation src, IConstructor node , org.rascalmpl.ast.Sym match,  org.rascalmpl.ast.Sym symbol) {
      super(src, node);
      
      this.match = match;
      this.symbol = symbol;
    }
  
    @Override
    public boolean isNotPrecede() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitSymNotPrecede(this);
    }
  
    @Override
    protected void addForLineNumber(int $line, java.util.List<AbstractAST> $result) {
      if (getLocation().getBeginLine() == $line) {
        $result.add(this);
      }
      ISourceLocation $l;
      
      $l = match.getLocation();
      if ($l.hasLineColumn() && $l.getBeginLine() <= $line && $l.getEndLine() >= $line) {
        match.addForLineNumber($line, $result);
      }
      if ($l.getBeginLine() > $line) {
        return;
      }
      
      $l = symbol.getLocation();
      if ($l.hasLineColumn() && $l.getBeginLine() <= $line && $l.getEndLine() >= $line) {
        symbol.addForLineNumber($line, $result);
      }
      if ($l.getBeginLine() > $line) {
        return;
      }
      
    }
  
    @Override
    public boolean equals(Object o) {
      if (!(o instanceof NotPrecede)) {
        return false;
      }        
      NotPrecede tmp = (NotPrecede) o;
      return true && tmp.match.equals(this.match) && tmp.symbol.equals(this.symbol) ; 
    }
   
    @Override
    public int hashCode() {
      return 163 + 641 * match.hashCode() + 313 * symbol.hashCode() ; 
    } 
  
    
    @Override
    public org.rascalmpl.ast.Sym getMatch() {
      return this.match;
    }
  
    @Override
    public boolean hasMatch() {
      return true;
    }
    @Override
    public org.rascalmpl.ast.Sym getSymbol() {
      return this.symbol;
    }
  
    @Override
    public boolean hasSymbol() {
      return true;
    }	
  
    @Override
    public Object clone()  {
      return newInstance(getClass(), src, (IConstructor) null , clone(match), clone(symbol));
    }
            
  }
  public boolean isOptional() {
    return false;
  }

  static public class Optional extends Sym {
    // Production: sig("Optional",[arg("org.rascalmpl.ast.Sym","symbol")],breakable=false)
  
    
    private final org.rascalmpl.ast.Sym symbol;
  
    public Optional(ISourceLocation src, IConstructor node , org.rascalmpl.ast.Sym symbol) {
      super(src, node);
      
      this.symbol = symbol;
    }
  
    @Override
    public boolean isOptional() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitSymOptional(this);
    }
  
    @Override
    protected void addForLineNumber(int $line, java.util.List<AbstractAST> $result) {
      if (getLocation().getBeginLine() == $line) {
        $result.add(this);
      }
      ISourceLocation $l;
      
      $l = symbol.getLocation();
      if ($l.hasLineColumn() && $l.getBeginLine() <= $line && $l.getEndLine() >= $line) {
        symbol.addForLineNumber($line, $result);
      }
      if ($l.getBeginLine() > $line) {
        return;
      }
      
    }
  
    @Override
    public boolean equals(Object o) {
      if (!(o instanceof Optional)) {
        return false;
      }        
      Optional tmp = (Optional) o;
      return true && tmp.symbol.equals(this.symbol) ; 
    }
   
    @Override
    public int hashCode() {
      return 509 + 563 * symbol.hashCode() ; 
    } 
  
    
    @Override
    public org.rascalmpl.ast.Sym getSymbol() {
      return this.symbol;
    }
  
    @Override
    public boolean hasSymbol() {
      return true;
    }	
  
    @Override
    public Object clone()  {
      return newInstance(getClass(), src, (IConstructor) null , clone(symbol));
    }
            
  }
  public boolean isParameter() {
    return false;
  }

  static public class Parameter extends Sym {
    // Production: sig("Parameter",[arg("org.rascalmpl.ast.Nonterminal","nonterminal")],breakable=false)
  
    
    private final org.rascalmpl.ast.Nonterminal nonterminal;
  
    public Parameter(ISourceLocation src, IConstructor node , org.rascalmpl.ast.Nonterminal nonterminal) {
      super(src, node);
      
      this.nonterminal = nonterminal;
    }
  
    @Override
    public boolean isParameter() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitSymParameter(this);
    }
  
    @Override
    protected void addForLineNumber(int $line, java.util.List<AbstractAST> $result) {
      if (getLocation().getBeginLine() == $line) {
        $result.add(this);
      }
      ISourceLocation $l;
      
      $l = nonterminal.getLocation();
      if ($l.hasLineColumn() && $l.getBeginLine() <= $line && $l.getEndLine() >= $line) {
        nonterminal.addForLineNumber($line, $result);
      }
      if ($l.getBeginLine() > $line) {
        return;
      }
      
    }
  
    @Override
    public boolean equals(Object o) {
      if (!(o instanceof Parameter)) {
        return false;
      }        
      Parameter tmp = (Parameter) o;
      return true && tmp.nonterminal.equals(this.nonterminal) ; 
    }
   
    @Override
    public int hashCode() {
      return 149 + 431 * nonterminal.hashCode() ; 
    } 
  
    
    @Override
    public org.rascalmpl.ast.Nonterminal getNonterminal() {
      return this.nonterminal;
    }
  
    @Override
    public boolean hasNonterminal() {
      return true;
    }	
  
    @Override
    public Object clone()  {
      return newInstance(getClass(), src, (IConstructor) null , clone(nonterminal));
    }
            
  }
  public boolean isParametrized() {
    return false;
  }

  static public class Parametrized extends Sym {
    // Production: sig("Parametrized",[arg("org.rascalmpl.ast.Nonterminal","nonterminal"),arg("java.util.List\<org.rascalmpl.ast.Sym\>","parameters")],breakable=false)
  
    
    private final org.rascalmpl.ast.Nonterminal nonterminal;
    private final java.util.List<org.rascalmpl.ast.Sym> parameters;
  
    public Parametrized(ISourceLocation src, IConstructor node , org.rascalmpl.ast.Nonterminal nonterminal,  java.util.List<org.rascalmpl.ast.Sym> parameters) {
      super(src, node);
      
      this.nonterminal = nonterminal;
      this.parameters = parameters;
    }
  
    @Override
    public boolean isParametrized() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitSymParametrized(this);
    }
  
    @Override
    protected void addForLineNumber(int $line, java.util.List<AbstractAST> $result) {
      if (getLocation().getBeginLine() == $line) {
        $result.add(this);
      }
      ISourceLocation $l;
      
      $l = nonterminal.getLocation();
      if ($l.hasLineColumn() && $l.getBeginLine() <= $line && $l.getEndLine() >= $line) {
        nonterminal.addForLineNumber($line, $result);
      }
      if ($l.getBeginLine() > $line) {
        return;
      }
      
      for (AbstractAST $elem : parameters) {
        $l = $elem.getLocation();
        if ($l.hasLineColumn() && $l.getBeginLine() <= $line && $l.getEndLine() >= $line) {
          $elem.addForLineNumber($line, $result);
        }
        if ($l.getBeginLine() > $line) {
          return;
        }
  
      }
    }
  
    @Override
    public boolean equals(Object o) {
      if (!(o instanceof Parametrized)) {
        return false;
      }        
      Parametrized tmp = (Parametrized) o;
      return true && tmp.nonterminal.equals(this.nonterminal) && tmp.parameters.equals(this.parameters) ; 
    }
   
    @Override
    public int hashCode() {
      return 67 + 19 * nonterminal.hashCode() + 181 * parameters.hashCode() ; 
    } 
  
    
    @Override
    public org.rascalmpl.ast.Nonterminal getNonterminal() {
      return this.nonterminal;
    }
  
    @Override
    public boolean hasNonterminal() {
      return true;
    }
    @Override
    public java.util.List<org.rascalmpl.ast.Sym> getParameters() {
      return this.parameters;
    }
  
    @Override
    public boolean hasParameters() {
      return true;
    }	
  
    @Override
    public Object clone()  {
      return newInstance(getClass(), src, (IConstructor) null , clone(nonterminal), clone(parameters));
    }
            
  }
  public boolean isPrecede() {
    return false;
  }

  static public class Precede extends Sym {
    // Production: sig("Precede",[arg("org.rascalmpl.ast.Sym","match"),arg("org.rascalmpl.ast.Sym","symbol")],breakable=false)
  
    
    private final org.rascalmpl.ast.Sym match;
    private final org.rascalmpl.ast.Sym symbol;
  
    public Precede(ISourceLocation src, IConstructor node , org.rascalmpl.ast.Sym match,  org.rascalmpl.ast.Sym symbol) {
      super(src, node);
      
      this.match = match;
      this.symbol = symbol;
    }
  
    @Override
    public boolean isPrecede() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitSymPrecede(this);
    }
  
    @Override
    protected void addForLineNumber(int $line, java.util.List<AbstractAST> $result) {
      if (getLocation().getBeginLine() == $line) {
        $result.add(this);
      }
      ISourceLocation $l;
      
      $l = match.getLocation();
      if ($l.hasLineColumn() && $l.getBeginLine() <= $line && $l.getEndLine() >= $line) {
        match.addForLineNumber($line, $result);
      }
      if ($l.getBeginLine() > $line) {
        return;
      }
      
      $l = symbol.getLocation();
      if ($l.hasLineColumn() && $l.getBeginLine() <= $line && $l.getEndLine() >= $line) {
        symbol.addForLineNumber($line, $result);
      }
      if ($l.getBeginLine() > $line) {
        return;
      }
      
    }
  
    @Override
    public boolean equals(Object o) {
      if (!(o instanceof Precede)) {
        return false;
      }        
      Precede tmp = (Precede) o;
      return true && tmp.match.equals(this.match) && tmp.symbol.equals(this.symbol) ; 
    }
   
    @Override
    public int hashCode() {
      return 599 + 101 * match.hashCode() + 421 * symbol.hashCode() ; 
    } 
  
    
    @Override
    public org.rascalmpl.ast.Sym getMatch() {
      return this.match;
    }
  
    @Override
    public boolean hasMatch() {
      return true;
    }
    @Override
    public org.rascalmpl.ast.Sym getSymbol() {
      return this.symbol;
    }
  
    @Override
    public boolean hasSymbol() {
      return true;
    }	
  
    @Override
    public Object clone()  {
      return newInstance(getClass(), src, (IConstructor) null , clone(match), clone(symbol));
    }
            
  }
  public boolean isSequence() {
    return false;
  }

  static public class Sequence extends Sym {
    // Production: sig("Sequence",[arg("org.rascalmpl.ast.Sym","first"),arg("java.util.List\<org.rascalmpl.ast.Sym\>","sequence")],breakable=false)
  
    
    private final org.rascalmpl.ast.Sym first;
    private final java.util.List<org.rascalmpl.ast.Sym> sequence;
  
    public Sequence(ISourceLocation src, IConstructor node , org.rascalmpl.ast.Sym first,  java.util.List<org.rascalmpl.ast.Sym> sequence) {
      super(src, node);
      
      this.first = first;
      this.sequence = sequence;
    }
  
    @Override
    public boolean isSequence() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitSymSequence(this);
    }
  
    @Override
    protected void addForLineNumber(int $line, java.util.List<AbstractAST> $result) {
      if (getLocation().getBeginLine() == $line) {
        $result.add(this);
      }
      ISourceLocation $l;
      
      $l = first.getLocation();
      if ($l.hasLineColumn() && $l.getBeginLine() <= $line && $l.getEndLine() >= $line) {
        first.addForLineNumber($line, $result);
      }
      if ($l.getBeginLine() > $line) {
        return;
      }
      
      for (AbstractAST $elem : sequence) {
        $l = $elem.getLocation();
        if ($l.hasLineColumn() && $l.getBeginLine() <= $line && $l.getEndLine() >= $line) {
          $elem.addForLineNumber($line, $result);
        }
        if ($l.getBeginLine() > $line) {
          return;
        }
  
      }
    }
  
    @Override
    public boolean equals(Object o) {
      if (!(o instanceof Sequence)) {
        return false;
      }        
      Sequence tmp = (Sequence) o;
      return true && tmp.first.equals(this.first) && tmp.sequence.equals(this.sequence) ; 
    }
   
    @Override
    public int hashCode() {
      return 277 + 487 * first.hashCode() + 797 * sequence.hashCode() ; 
    } 
  
    
    @Override
    public org.rascalmpl.ast.Sym getFirst() {
      return this.first;
    }
  
    @Override
    public boolean hasFirst() {
      return true;
    }
    @Override
    public java.util.List<org.rascalmpl.ast.Sym> getSequence() {
      return this.sequence;
    }
  
    @Override
    public boolean hasSequence() {
      return true;
    }	
  
    @Override
    public Object clone()  {
      return newInstance(getClass(), src, (IConstructor) null , clone(first), clone(sequence));
    }
            
  }
  public boolean isStart() {
    return false;
  }

  static public class Start extends Sym {
    // Production: sig("Start",[arg("org.rascalmpl.ast.Nonterminal","nonterminal")],breakable=false)
  
    
    private final org.rascalmpl.ast.Nonterminal nonterminal;
  
    public Start(ISourceLocation src, IConstructor node , org.rascalmpl.ast.Nonterminal nonterminal) {
      super(src, node);
      
      this.nonterminal = nonterminal;
    }
  
    @Override
    public boolean isStart() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitSymStart(this);
    }
  
    @Override
    protected void addForLineNumber(int $line, java.util.List<AbstractAST> $result) {
      if (getLocation().getBeginLine() == $line) {
        $result.add(this);
      }
      ISourceLocation $l;
      
      $l = nonterminal.getLocation();
      if ($l.hasLineColumn() && $l.getBeginLine() <= $line && $l.getEndLine() >= $line) {
        nonterminal.addForLineNumber($line, $result);
      }
      if ($l.getBeginLine() > $line) {
        return;
      }
      
    }
  
    @Override
    public boolean equals(Object o) {
      if (!(o instanceof Start)) {
        return false;
      }        
      Start tmp = (Start) o;
      return true && tmp.nonterminal.equals(this.nonterminal) ; 
    }
   
    @Override
    public int hashCode() {
      return 787 + 397 * nonterminal.hashCode() ; 
    } 
  
    
    @Override
    public org.rascalmpl.ast.Nonterminal getNonterminal() {
      return this.nonterminal;
    }
  
    @Override
    public boolean hasNonterminal() {
      return true;
    }	
  
    @Override
    public Object clone()  {
      return newInstance(getClass(), src, (IConstructor) null , clone(nonterminal));
    }
            
  }
  public boolean isStartOfLine() {
    return false;
  }

  static public class StartOfLine extends Sym {
    // Production: sig("StartOfLine",[arg("org.rascalmpl.ast.Sym","symbol")],breakable=false)
  
    
    private final org.rascalmpl.ast.Sym symbol;
  
    public StartOfLine(ISourceLocation src, IConstructor node , org.rascalmpl.ast.Sym symbol) {
      super(src, node);
      
      this.symbol = symbol;
    }
  
    @Override
    public boolean isStartOfLine() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitSymStartOfLine(this);
    }
  
    @Override
    protected void addForLineNumber(int $line, java.util.List<AbstractAST> $result) {
      if (getLocation().getBeginLine() == $line) {
        $result.add(this);
      }
      ISourceLocation $l;
      
      $l = symbol.getLocation();
      if ($l.hasLineColumn() && $l.getBeginLine() <= $line && $l.getEndLine() >= $line) {
        symbol.addForLineNumber($line, $result);
      }
      if ($l.getBeginLine() > $line) {
        return;
      }
      
    }
  
    @Override
    public boolean equals(Object o) {
      if (!(o instanceof StartOfLine)) {
        return false;
      }        
      StartOfLine tmp = (StartOfLine) o;
      return true && tmp.symbol.equals(this.symbol) ; 
    }
   
    @Override
    public int hashCode() {
      return 191 + 977 * symbol.hashCode() ; 
    } 
  
    
    @Override
    public org.rascalmpl.ast.Sym getSymbol() {
      return this.symbol;
    }
  
    @Override
    public boolean hasSymbol() {
      return true;
    }	
  
    @Override
    public Object clone()  {
      return newInstance(getClass(), src, (IConstructor) null , clone(symbol));
    }
            
  }
  public boolean isUnequal() {
    return false;
  }

  static public class Unequal extends Sym {
    // Production: sig("Unequal",[arg("org.rascalmpl.ast.Sym","symbol"),arg("org.rascalmpl.ast.Sym","match")],breakable=false)
  
    
    private final org.rascalmpl.ast.Sym symbol;
    private final org.rascalmpl.ast.Sym match;
  
    public Unequal(ISourceLocation src, IConstructor node , org.rascalmpl.ast.Sym symbol,  org.rascalmpl.ast.Sym match) {
      super(src, node);
      
      this.symbol = symbol;
      this.match = match;
    }
  
    @Override
    public boolean isUnequal() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitSymUnequal(this);
    }
  
    @Override
    protected void addForLineNumber(int $line, java.util.List<AbstractAST> $result) {
      if (getLocation().getBeginLine() == $line) {
        $result.add(this);
      }
      ISourceLocation $l;
      
      $l = symbol.getLocation();
      if ($l.hasLineColumn() && $l.getBeginLine() <= $line && $l.getEndLine() >= $line) {
        symbol.addForLineNumber($line, $result);
      }
      if ($l.getBeginLine() > $line) {
        return;
      }
      
      $l = match.getLocation();
      if ($l.hasLineColumn() && $l.getBeginLine() <= $line && $l.getEndLine() >= $line) {
        match.addForLineNumber($line, $result);
      }
      if ($l.getBeginLine() > $line) {
        return;
      }
      
    }
  
    @Override
    public boolean equals(Object o) {
      if (!(o instanceof Unequal)) {
        return false;
      }        
      Unequal tmp = (Unequal) o;
      return true && tmp.symbol.equals(this.symbol) && tmp.match.equals(this.match) ; 
    }
   
    @Override
    public int hashCode() {
      return 173 + 541 * symbol.hashCode() + 491 * match.hashCode() ; 
    } 
  
    
    @Override
    public org.rascalmpl.ast.Sym getSymbol() {
      return this.symbol;
    }
  
    @Override
    public boolean hasSymbol() {
      return true;
    }
    @Override
    public org.rascalmpl.ast.Sym getMatch() {
      return this.match;
    }
  
    @Override
    public boolean hasMatch() {
      return true;
    }	
  
    @Override
    public Object clone()  {
      return newInstance(getClass(), src, (IConstructor) null , clone(symbol), clone(match));
    }
            
  }
}