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

public abstract class Sym extends AbstractAST {
  public Sym(IConstructor node) {
    super();
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
  public boolean hasFormals() {
    return false;
  }

  public org.rascalmpl.ast.Parameters getFormals() {
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
  public boolean hasTyp() {
    return false;
  }

  public org.rascalmpl.ast.Type getTyp() {
    throw new UnsupportedOperationException();
  }

  

  
  public boolean isAlternative() {
    return false;
  }

  static public class Alternative extends Sym {
    // Production: sig("Alternative",[arg("org.rascalmpl.ast.Sym","first"),arg("java.util.List\<org.rascalmpl.ast.Sym\>","alternatives")])
  
    
    private final org.rascalmpl.ast.Sym first;
    private final java.util.List<org.rascalmpl.ast.Sym> alternatives;
  
    public Alternative(IConstructor node , org.rascalmpl.ast.Sym first,  java.util.List<org.rascalmpl.ast.Sym> alternatives) {
      super(node);
      
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
    public boolean equals(Object o) {
      if (!(o instanceof Alternative)) {
        return false;
      }        
      Alternative tmp = (Alternative) o;
      return true && tmp.first.equals(this.first) && tmp.alternatives.equals(this.alternatives) ; 
    }
   
    @Override
    public int hashCode() {
      return 757 + 467 * first.hashCode() + 607 * alternatives.hashCode() ; 
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
  }
  public boolean isBracket() {
    return false;
  }

  static public class Bracket extends Sym {
    // Production: sig("Bracket",[arg("org.rascalmpl.ast.Sym","symbol")])
  
    
    private final org.rascalmpl.ast.Sym symbol;
  
    public Bracket(IConstructor node , org.rascalmpl.ast.Sym symbol) {
      super(node);
      
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
    public boolean equals(Object o) {
      if (!(o instanceof Bracket)) {
        return false;
      }        
      Bracket tmp = (Bracket) o;
      return true && tmp.symbol.equals(this.symbol) ; 
    }
   
    @Override
    public int hashCode() {
      return 421 + 373 * symbol.hashCode() ; 
    } 
  
    
    @Override
    public org.rascalmpl.ast.Sym getSymbol() {
      return this.symbol;
    }
  
    @Override
    public boolean hasSymbol() {
      return true;
    }	
  }
  public boolean isCaseInsensitiveLiteral() {
    return false;
  }

  static public class CaseInsensitiveLiteral extends Sym {
    // Production: sig("CaseInsensitiveLiteral",[arg("org.rascalmpl.ast.CaseInsensitiveStringConstant","cistring")])
  
    
    private final org.rascalmpl.ast.CaseInsensitiveStringConstant cistring;
  
    public CaseInsensitiveLiteral(IConstructor node , org.rascalmpl.ast.CaseInsensitiveStringConstant cistring) {
      super(node);
      
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
    public boolean equals(Object o) {
      if (!(o instanceof CaseInsensitiveLiteral)) {
        return false;
      }        
      CaseInsensitiveLiteral tmp = (CaseInsensitiveLiteral) o;
      return true && tmp.cistring.equals(this.cistring) ; 
    }
   
    @Override
    public int hashCode() {
      return 73 + 167 * cistring.hashCode() ; 
    } 
  
    
    @Override
    public org.rascalmpl.ast.CaseInsensitiveStringConstant getCistring() {
      return this.cistring;
    }
  
    @Override
    public boolean hasCistring() {
      return true;
    }	
  }
  public boolean isCharacterClass() {
    return false;
  }

  static public class CharacterClass extends Sym {
    // Production: sig("CharacterClass",[arg("org.rascalmpl.ast.Class","charClass")])
  
    
    private final org.rascalmpl.ast.Class charClass;
  
    public CharacterClass(IConstructor node , org.rascalmpl.ast.Class charClass) {
      super(node);
      
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
    public boolean equals(Object o) {
      if (!(o instanceof CharacterClass)) {
        return false;
      }        
      CharacterClass tmp = (CharacterClass) o;
      return true && tmp.charClass.equals(this.charClass) ; 
    }
   
    @Override
    public int hashCode() {
      return 41 + 461 * charClass.hashCode() ; 
    } 
  
    
    @Override
    public org.rascalmpl.ast.Class getCharClass() {
      return this.charClass;
    }
  
    @Override
    public boolean hasCharClass() {
      return true;
    }	
  }
  public boolean isColumn() {
    return false;
  }

  static public class Column extends Sym {
    // Production: sig("Column",[arg("org.rascalmpl.ast.Sym","symbol"),arg("org.rascalmpl.ast.IntegerLiteral","column")])
  
    
    private final org.rascalmpl.ast.Sym symbol;
    private final org.rascalmpl.ast.IntegerLiteral column;
  
    public Column(IConstructor node , org.rascalmpl.ast.Sym symbol,  org.rascalmpl.ast.IntegerLiteral column) {
      super(node);
      
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
    public boolean equals(Object o) {
      if (!(o instanceof Column)) {
        return false;
      }        
      Column tmp = (Column) o;
      return true && tmp.symbol.equals(this.symbol) && tmp.column.equals(this.column) ; 
    }
   
    @Override
    public int hashCode() {
      return 521 + 311 * symbol.hashCode() + 523 * column.hashCode() ; 
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
  }
  public boolean isDependAlign() {
    return false;
  }

  static public class DependAlign extends Sym {
    // Production: sig("DependAlign",[arg("org.rascalmpl.ast.Sym","symbol")])
  
    
    private final org.rascalmpl.ast.Sym symbol;
  
    public DependAlign(IConstructor node , org.rascalmpl.ast.Sym symbol) {
      super(node);
      
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
    public boolean equals(Object o) {
      if (!(o instanceof DependAlign)) {
        return false;
      }        
      DependAlign tmp = (DependAlign) o;
      return true && tmp.symbol.equals(this.symbol) ; 
    }
   
    @Override
    public int hashCode() {
      return 101 + 383 * symbol.hashCode() ; 
    } 
  
    
    @Override
    public org.rascalmpl.ast.Sym getSymbol() {
      return this.symbol;
    }
  
    @Override
    public boolean hasSymbol() {
      return true;
    }	
  }
  public boolean isDependAlternative() {
    return false;
  }

  static public class DependAlternative extends Sym {
    // Production: sig("DependAlternative",[arg("org.rascalmpl.ast.Expression","condition"),arg("org.rascalmpl.ast.Sym","thenPart"),arg("org.rascalmpl.ast.Sym","elsePart")])
  
    
    private final org.rascalmpl.ast.Expression condition;
    private final org.rascalmpl.ast.Sym thenPart;
    private final org.rascalmpl.ast.Sym elsePart;
  
    public DependAlternative(IConstructor node , org.rascalmpl.ast.Expression condition,  org.rascalmpl.ast.Sym thenPart,  org.rascalmpl.ast.Sym elsePart) {
      super(node);
      
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
    public boolean equals(Object o) {
      if (!(o instanceof DependAlternative)) {
        return false;
      }        
      DependAlternative tmp = (DependAlternative) o;
      return true && tmp.condition.equals(this.condition) && tmp.thenPart.equals(this.thenPart) && tmp.elsePart.equals(this.elsePart) ; 
    }
   
    @Override
    public int hashCode() {
      return 293 + 641 * condition.hashCode() + 751 * thenPart.hashCode() + 43 * elsePart.hashCode() ; 
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
  }
  public boolean isDependCode() {
    return false;
  }

  static public class DependCode extends Sym {
    // Production: sig("DependCode",[arg("org.rascalmpl.ast.Sym","symbol"),arg("java.util.List\<org.rascalmpl.ast.Statement\>","block")])
  
    
    private final org.rascalmpl.ast.Sym symbol;
    private final java.util.List<org.rascalmpl.ast.Statement> block;
  
    public DependCode(IConstructor node , org.rascalmpl.ast.Sym symbol,  java.util.List<org.rascalmpl.ast.Statement> block) {
      super(node);
      
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
    public boolean equals(Object o) {
      if (!(o instanceof DependCode)) {
        return false;
      }        
      DependCode tmp = (DependCode) o;
      return true && tmp.symbol.equals(this.symbol) && tmp.block.equals(this.block) ; 
    }
   
    @Override
    public int hashCode() {
      return 769 + 5 * symbol.hashCode() + 659 * block.hashCode() ; 
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
  }
  public boolean isDependConditionAfter() {
    return false;
  }

  static public class DependConditionAfter extends Sym {
    // Production: sig("DependConditionAfter",[arg("org.rascalmpl.ast.Sym","symbol"),arg("org.rascalmpl.ast.Expression","condition")])
  
    
    private final org.rascalmpl.ast.Sym symbol;
    private final org.rascalmpl.ast.Expression condition;
  
    public DependConditionAfter(IConstructor node , org.rascalmpl.ast.Sym symbol,  org.rascalmpl.ast.Expression condition) {
      super(node);
      
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
    public boolean equals(Object o) {
      if (!(o instanceof DependConditionAfter)) {
        return false;
      }        
      DependConditionAfter tmp = (DependConditionAfter) o;
      return true && tmp.symbol.equals(this.symbol) && tmp.condition.equals(this.condition) ; 
    }
   
    @Override
    public int hashCode() {
      return 127 + 911 * symbol.hashCode() + 881 * condition.hashCode() ; 
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
  }
  public boolean isDependConditionBefore() {
    return false;
  }

  static public class DependConditionBefore extends Sym {
    // Production: sig("DependConditionBefore",[arg("org.rascalmpl.ast.Expression","condition"),arg("org.rascalmpl.ast.Sym","thenPart")])
  
    
    private final org.rascalmpl.ast.Expression condition;
    private final org.rascalmpl.ast.Sym thenPart;
  
    public DependConditionBefore(IConstructor node , org.rascalmpl.ast.Expression condition,  org.rascalmpl.ast.Sym thenPart) {
      super(node);
      
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
    public boolean equals(Object o) {
      if (!(o instanceof DependConditionBefore)) {
        return false;
      }        
      DependConditionBefore tmp = (DependConditionBefore) o;
      return true && tmp.condition.equals(this.condition) && tmp.thenPart.equals(this.thenPart) ; 
    }
   
    @Override
    public int hashCode() {
      return 601 + 797 * condition.hashCode() + 929 * thenPart.hashCode() ; 
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
  }
  public boolean isDependFormals() {
    return false;
  }

  static public class DependFormals extends Sym {
    // Production: sig("DependFormals",[arg("org.rascalmpl.ast.Nonterminal","nonterminal"),arg("org.rascalmpl.ast.Type","typ"),arg("org.rascalmpl.ast.Parameters","formals")])
  
    
    private final org.rascalmpl.ast.Nonterminal nonterminal;
    private final org.rascalmpl.ast.Type typ;
    private final org.rascalmpl.ast.Parameters formals;
  
    public DependFormals(IConstructor node , org.rascalmpl.ast.Nonterminal nonterminal,  org.rascalmpl.ast.Type typ,  org.rascalmpl.ast.Parameters formals) {
      super(node);
      
      this.nonterminal = nonterminal;
      this.typ = typ;
      this.formals = formals;
    }
  
    @Override
    public boolean isDependFormals() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitSymDependFormals(this);
    }
  
    @Override
    public boolean equals(Object o) {
      if (!(o instanceof DependFormals)) {
        return false;
      }        
      DependFormals tmp = (DependFormals) o;
      return true && tmp.nonterminal.equals(this.nonterminal) && tmp.typ.equals(this.typ) && tmp.formals.equals(this.formals) ; 
    }
   
    @Override
    public int hashCode() {
      return 521 + 431 * nonterminal.hashCode() + 733 * typ.hashCode() + 107 * formals.hashCode() ; 
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
    public org.rascalmpl.ast.Type getTyp() {
      return this.typ;
    }
  
    @Override
    public boolean hasTyp() {
      return true;
    }
    @Override
    public org.rascalmpl.ast.Parameters getFormals() {
      return this.formals;
    }
  
    @Override
    public boolean hasFormals() {
      return true;
    }	
  }
  public boolean isDependFormalsParametrized() {
    return false;
  }

  static public class DependFormalsParametrized extends Sym {
    // Production: sig("DependFormalsParametrized",[arg("org.rascalmpl.ast.Nonterminal","nonterminal"),arg("java.util.List\<org.rascalmpl.ast.Sym\>","parameters"),arg("org.rascalmpl.ast.Type","typ"),arg("org.rascalmpl.ast.Parameters","formals")])
  
    
    private final org.rascalmpl.ast.Nonterminal nonterminal;
    private final java.util.List<org.rascalmpl.ast.Sym> parameters;
    private final org.rascalmpl.ast.Type typ;
    private final org.rascalmpl.ast.Parameters formals;
  
    public DependFormalsParametrized(IConstructor node , org.rascalmpl.ast.Nonterminal nonterminal,  java.util.List<org.rascalmpl.ast.Sym> parameters,  org.rascalmpl.ast.Type typ,  org.rascalmpl.ast.Parameters formals) {
      super(node);
      
      this.nonterminal = nonterminal;
      this.parameters = parameters;
      this.typ = typ;
      this.formals = formals;
    }
  
    @Override
    public boolean isDependFormalsParametrized() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitSymDependFormalsParametrized(this);
    }
  
    @Override
    public boolean equals(Object o) {
      if (!(o instanceof DependFormalsParametrized)) {
        return false;
      }        
      DependFormalsParametrized tmp = (DependFormalsParametrized) o;
      return true && tmp.nonterminal.equals(this.nonterminal) && tmp.parameters.equals(this.parameters) && tmp.typ.equals(this.typ) && tmp.formals.equals(this.formals) ; 
    }
   
    @Override
    public int hashCode() {
      return 137 + 443 * nonterminal.hashCode() + 181 * parameters.hashCode() + 97 * typ.hashCode() + 619 * formals.hashCode() ; 
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
    public org.rascalmpl.ast.Type getTyp() {
      return this.typ;
    }
  
    @Override
    public boolean hasTyp() {
      return true;
    }
    @Override
    public org.rascalmpl.ast.Parameters getFormals() {
      return this.formals;
    }
  
    @Override
    public boolean hasFormals() {
      return true;
    }	
  }
  public boolean isDependLoop() {
    return false;
  }

  static public class DependLoop extends Sym {
    // Production: sig("DependLoop",[arg("org.rascalmpl.ast.Expression","condition"),arg("org.rascalmpl.ast.Sym","body")])
  
    
    private final org.rascalmpl.ast.Expression condition;
    private final org.rascalmpl.ast.Sym body;
  
    public DependLoop(IConstructor node , org.rascalmpl.ast.Expression condition,  org.rascalmpl.ast.Sym body) {
      super(node);
      
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
    public boolean equals(Object o) {
      if (!(o instanceof DependLoop)) {
        return false;
      }        
      DependLoop tmp = (DependLoop) o;
      return true && tmp.condition.equals(this.condition) && tmp.body.equals(this.body) ; 
    }
   
    @Override
    public int hashCode() {
      return 29 + 311 * condition.hashCode() + 107 * body.hashCode() ; 
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
  }
  public boolean isDependNonterminal() {
    return false;
  }

  static public class DependNonterminal extends Sym {
    // Production: sig("DependNonterminal",[arg("org.rascalmpl.ast.Nonterminal","nonterminal"),arg("java.util.List\<org.rascalmpl.ast.Expression\>","arguments"),arg("org.rascalmpl.ast.KeywordArguments_Expression","keywordArguments")])
  
    
    private final org.rascalmpl.ast.Nonterminal nonterminal;
    private final java.util.List<org.rascalmpl.ast.Expression> arguments;
    private final org.rascalmpl.ast.KeywordArguments_Expression keywordArguments;
  
    public DependNonterminal(IConstructor node , org.rascalmpl.ast.Nonterminal nonterminal,  java.util.List<org.rascalmpl.ast.Expression> arguments,  org.rascalmpl.ast.KeywordArguments_Expression keywordArguments) {
      super(node);
      
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
    public boolean equals(Object o) {
      if (!(o instanceof DependNonterminal)) {
        return false;
      }        
      DependNonterminal tmp = (DependNonterminal) o;
      return true && tmp.nonterminal.equals(this.nonterminal) && tmp.arguments.equals(this.arguments) && tmp.keywordArguments.equals(this.keywordArguments) ; 
    }
   
    @Override
    public int hashCode() {
      return 7 + 67 * nonterminal.hashCode() + 479 * arguments.hashCode() + 743 * keywordArguments.hashCode() ; 
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
  }
  public boolean isDependOffside() {
    return false;
  }

  static public class DependOffside extends Sym {
    // Production: sig("DependOffside",[arg("org.rascalmpl.ast.Sym","symbol")])
  
    
    private final org.rascalmpl.ast.Sym symbol;
  
    public DependOffside(IConstructor node , org.rascalmpl.ast.Sym symbol) {
      super(node);
      
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
    public boolean equals(Object o) {
      if (!(o instanceof DependOffside)) {
        return false;
      }        
      DependOffside tmp = (DependOffside) o;
      return true && tmp.symbol.equals(this.symbol) ; 
    }
   
    @Override
    public int hashCode() {
      return 503 + 3 * symbol.hashCode() ; 
    } 
  
    
    @Override
    public org.rascalmpl.ast.Sym getSymbol() {
      return this.symbol;
    }
  
    @Override
    public boolean hasSymbol() {
      return true;
    }	
  }
  public boolean isDependParametrized() {
    return false;
  }

  static public class DependParametrized extends Sym {
    // Production: sig("DependParametrized",[arg("org.rascalmpl.ast.Nonterminal","nonterminal"),arg("java.util.List\<org.rascalmpl.ast.Sym\>","parameters"),arg("java.util.List\<org.rascalmpl.ast.Expression\>","arguments"),arg("org.rascalmpl.ast.KeywordArguments_Expression","keywordArguments")])
  
    
    private final org.rascalmpl.ast.Nonterminal nonterminal;
    private final java.util.List<org.rascalmpl.ast.Sym> parameters;
    private final java.util.List<org.rascalmpl.ast.Expression> arguments;
    private final org.rascalmpl.ast.KeywordArguments_Expression keywordArguments;
  
    public DependParametrized(IConstructor node , org.rascalmpl.ast.Nonterminal nonterminal,  java.util.List<org.rascalmpl.ast.Sym> parameters,  java.util.List<org.rascalmpl.ast.Expression> arguments,  org.rascalmpl.ast.KeywordArguments_Expression keywordArguments) {
      super(node);
      
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
    public boolean equals(Object o) {
      if (!(o instanceof DependParametrized)) {
        return false;
      }        
      DependParametrized tmp = (DependParametrized) o;
      return true && tmp.nonterminal.equals(this.nonterminal) && tmp.parameters.equals(this.parameters) && tmp.arguments.equals(this.arguments) && tmp.keywordArguments.equals(this.keywordArguments) ; 
    }
   
    @Override
    public int hashCode() {
      return 673 + 577 * nonterminal.hashCode() + 751 * parameters.hashCode() + 41 * arguments.hashCode() + 919 * keywordArguments.hashCode() ; 
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
  }
  public boolean isDependScope() {
    return false;
  }

  static public class DependScope extends Sym {
    // Production: sig("DependScope",[arg("java.util.List\<org.rascalmpl.ast.Sym\>","symbols")])
  
    
    private final java.util.List<org.rascalmpl.ast.Sym> symbols;
  
    public DependScope(IConstructor node , java.util.List<org.rascalmpl.ast.Sym> symbols) {
      super(node);
      
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
    public boolean equals(Object o) {
      if (!(o instanceof DependScope)) {
        return false;
      }        
      DependScope tmp = (DependScope) o;
      return true && tmp.symbols.equals(this.symbols) ; 
    }
   
    @Override
    public int hashCode() {
      return 677 + 227 * symbols.hashCode() ; 
    } 
  
    
    @Override
    public java.util.List<org.rascalmpl.ast.Sym> getSymbols() {
      return this.symbols;
    }
  
    @Override
    public boolean hasSymbols() {
      return true;
    }	
  }
  public boolean isDependVoidFormals() {
    return false;
  }

  static public class DependVoidFormals extends Sym {
    // Production: sig("DependVoidFormals",[arg("org.rascalmpl.ast.Nonterminal","nonterminal"),arg("org.rascalmpl.ast.Parameters","formals")])
  
    
    private final org.rascalmpl.ast.Nonterminal nonterminal;
    private final org.rascalmpl.ast.Parameters formals;
  
    public DependVoidFormals(IConstructor node , org.rascalmpl.ast.Nonterminal nonterminal,  org.rascalmpl.ast.Parameters formals) {
      super(node);
      
      this.nonterminal = nonterminal;
      this.formals = formals;
    }
  
    @Override
    public boolean isDependVoidFormals() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitSymDependVoidFormals(this);
    }
  
    @Override
    public boolean equals(Object o) {
      if (!(o instanceof DependVoidFormals)) {
        return false;
      }        
      DependVoidFormals tmp = (DependVoidFormals) o;
      return true && tmp.nonterminal.equals(this.nonterminal) && tmp.formals.equals(this.formals) ; 
    }
   
    @Override
    public int hashCode() {
      return 929 + 13 * nonterminal.hashCode() + 661 * formals.hashCode() ; 
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
    public org.rascalmpl.ast.Parameters getFormals() {
      return this.formals;
    }
  
    @Override
    public boolean hasFormals() {
      return true;
    }	
  }
  public boolean isDependVoidFormalsParametrized() {
    return false;
  }

  static public class DependVoidFormalsParametrized extends Sym {
    // Production: sig("DependVoidFormalsParametrized",[arg("org.rascalmpl.ast.Nonterminal","nonterminal"),arg("java.util.List\<org.rascalmpl.ast.Sym\>","parameters"),arg("org.rascalmpl.ast.Parameters","formals")])
  
    
    private final org.rascalmpl.ast.Nonterminal nonterminal;
    private final java.util.List<org.rascalmpl.ast.Sym> parameters;
    private final org.rascalmpl.ast.Parameters formals;
  
    public DependVoidFormalsParametrized(IConstructor node , org.rascalmpl.ast.Nonterminal nonterminal,  java.util.List<org.rascalmpl.ast.Sym> parameters,  org.rascalmpl.ast.Parameters formals) {
      super(node);
      
      this.nonterminal = nonterminal;
      this.parameters = parameters;
      this.formals = formals;
    }
  
    @Override
    public boolean isDependVoidFormalsParametrized() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitSymDependVoidFormalsParametrized(this);
    }
  
    @Override
    public boolean equals(Object o) {
      if (!(o instanceof DependVoidFormalsParametrized)) {
        return false;
      }        
      DependVoidFormalsParametrized tmp = (DependVoidFormalsParametrized) o;
      return true && tmp.nonterminal.equals(this.nonterminal) && tmp.parameters.equals(this.parameters) && tmp.formals.equals(this.formals) ; 
    }
   
    @Override
    public int hashCode() {
      return 67 + 251 * nonterminal.hashCode() + 101 * parameters.hashCode() + 197 * formals.hashCode() ; 
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
    public org.rascalmpl.ast.Parameters getFormals() {
      return this.formals;
    }
  
    @Override
    public boolean hasFormals() {
      return true;
    }	
  }
  public boolean isEmpty() {
    return false;
  }

  static public class Empty extends Sym {
    // Production: sig("Empty",[])
  
    
  
    public Empty(IConstructor node ) {
      super(node);
      
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
    public boolean equals(Object o) {
      if (!(o instanceof Empty)) {
        return false;
      }        
      Empty tmp = (Empty) o;
      return true ; 
    }
   
    @Override
    public int hashCode() {
      return 599 ; 
    } 
  
    	
  }
  public boolean isEndOfLine() {
    return false;
  }

  static public class EndOfLine extends Sym {
    // Production: sig("EndOfLine",[arg("org.rascalmpl.ast.Sym","symbol")])
  
    
    private final org.rascalmpl.ast.Sym symbol;
  
    public EndOfLine(IConstructor node , org.rascalmpl.ast.Sym symbol) {
      super(node);
      
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
    public boolean equals(Object o) {
      if (!(o instanceof EndOfLine)) {
        return false;
      }        
      EndOfLine tmp = (EndOfLine) o;
      return true && tmp.symbol.equals(this.symbol) ; 
    }
   
    @Override
    public int hashCode() {
      return 43 + 179 * symbol.hashCode() ; 
    } 
  
    
    @Override
    public org.rascalmpl.ast.Sym getSymbol() {
      return this.symbol;
    }
  
    @Override
    public boolean hasSymbol() {
      return true;
    }	
  }
  public boolean isExcept() {
    return false;
  }

  static public class Except extends Sym {
    // Production: sig("Except",[arg("org.rascalmpl.ast.Sym","symbol"),arg("org.rascalmpl.ast.NonterminalLabel","label")])
  
    
    private final org.rascalmpl.ast.Sym symbol;
    private final org.rascalmpl.ast.NonterminalLabel label;
  
    public Except(IConstructor node , org.rascalmpl.ast.Sym symbol,  org.rascalmpl.ast.NonterminalLabel label) {
      super(node);
      
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
    public boolean equals(Object o) {
      if (!(o instanceof Except)) {
        return false;
      }        
      Except tmp = (Except) o;
      return true && tmp.symbol.equals(this.symbol) && tmp.label.equals(this.label) ; 
    }
   
    @Override
    public int hashCode() {
      return 631 + 881 * symbol.hashCode() + 619 * label.hashCode() ; 
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
  }
  public boolean isFarFollow() {
    return false;
  }

  static public class FarFollow extends Sym {
    // Production: sig("FarFollow",[arg("org.rascalmpl.ast.Sym","symbol"),arg("org.rascalmpl.ast.Sym","match")])
  
    
    private final org.rascalmpl.ast.Sym symbol;
    private final org.rascalmpl.ast.Sym match;
  
    public FarFollow(IConstructor node , org.rascalmpl.ast.Sym symbol,  org.rascalmpl.ast.Sym match) {
      super(node);
      
      this.symbol = symbol;
      this.match = match;
    }
  
    @Override
    public boolean isFarFollow() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitSymFarFollow(this);
    }
  
    @Override
    public boolean equals(Object o) {
      if (!(o instanceof FarFollow)) {
        return false;
      }        
      FarFollow tmp = (FarFollow) o;
      return true && tmp.symbol.equals(this.symbol) && tmp.match.equals(this.match) ; 
    }
   
    @Override
    public int hashCode() {
      return 379 + 829 * symbol.hashCode() + 607 * match.hashCode() ; 
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
  }
  public boolean isFarNotFollow() {
    return false;
  }

  static public class FarNotFollow extends Sym {
    // Production: sig("FarNotFollow",[arg("org.rascalmpl.ast.Sym","symbol"),arg("org.rascalmpl.ast.Sym","match")])
  
    
    private final org.rascalmpl.ast.Sym symbol;
    private final org.rascalmpl.ast.Sym match;
  
    public FarNotFollow(IConstructor node , org.rascalmpl.ast.Sym symbol,  org.rascalmpl.ast.Sym match) {
      super(node);
      
      this.symbol = symbol;
      this.match = match;
    }
  
    @Override
    public boolean isFarNotFollow() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitSymFarNotFollow(this);
    }
  
    @Override
    public boolean equals(Object o) {
      if (!(o instanceof FarNotFollow)) {
        return false;
      }        
      FarNotFollow tmp = (FarNotFollow) o;
      return true && tmp.symbol.equals(this.symbol) && tmp.match.equals(this.match) ; 
    }
   
    @Override
    public int hashCode() {
      return 421 + 283 * symbol.hashCode() + 859 * match.hashCode() ; 
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
  }
  public boolean isFarNotPrecede() {
    return false;
  }

  static public class FarNotPrecede extends Sym {
    // Production: sig("FarNotPrecede",[arg("org.rascalmpl.ast.Sym","match"),arg("org.rascalmpl.ast.Sym","symbol")])
  
    
    private final org.rascalmpl.ast.Sym match;
    private final org.rascalmpl.ast.Sym symbol;
  
    public FarNotPrecede(IConstructor node , org.rascalmpl.ast.Sym match,  org.rascalmpl.ast.Sym symbol) {
      super(node);
      
      this.match = match;
      this.symbol = symbol;
    }
  
    @Override
    public boolean isFarNotPrecede() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitSymFarNotPrecede(this);
    }
  
    @Override
    public boolean equals(Object o) {
      if (!(o instanceof FarNotPrecede)) {
        return false;
      }        
      FarNotPrecede tmp = (FarNotPrecede) o;
      return true && tmp.match.equals(this.match) && tmp.symbol.equals(this.symbol) ; 
    }
   
    @Override
    public int hashCode() {
      return 701 + 761 * match.hashCode() + 773 * symbol.hashCode() ; 
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
  }
  public boolean isFarPrecede() {
    return false;
  }

  static public class FarPrecede extends Sym {
    // Production: sig("FarPrecede",[arg("org.rascalmpl.ast.Sym","match"),arg("org.rascalmpl.ast.Sym","symbol")])
  
    
    private final org.rascalmpl.ast.Sym match;
    private final org.rascalmpl.ast.Sym symbol;
  
    public FarPrecede(IConstructor node , org.rascalmpl.ast.Sym match,  org.rascalmpl.ast.Sym symbol) {
      super(node);
      
      this.match = match;
      this.symbol = symbol;
    }
  
    @Override
    public boolean isFarPrecede() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitSymFarPrecede(this);
    }
  
    @Override
    public boolean equals(Object o) {
      if (!(o instanceof FarPrecede)) {
        return false;
      }        
      FarPrecede tmp = (FarPrecede) o;
      return true && tmp.match.equals(this.match) && tmp.symbol.equals(this.symbol) ; 
    }
   
    @Override
    public int hashCode() {
      return 677 + 431 * match.hashCode() + 163 * symbol.hashCode() ; 
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
  }
  public boolean isFollow() {
    return false;
  }

  static public class Follow extends Sym {
    // Production: sig("Follow",[arg("org.rascalmpl.ast.Sym","symbol"),arg("org.rascalmpl.ast.Sym","match")])
  
    
    private final org.rascalmpl.ast.Sym symbol;
    private final org.rascalmpl.ast.Sym match;
  
    public Follow(IConstructor node , org.rascalmpl.ast.Sym symbol,  org.rascalmpl.ast.Sym match) {
      super(node);
      
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
    public boolean equals(Object o) {
      if (!(o instanceof Follow)) {
        return false;
      }        
      Follow tmp = (Follow) o;
      return true && tmp.symbol.equals(this.symbol) && tmp.match.equals(this.match) ; 
    }
   
    @Override
    public int hashCode() {
      return 433 + 769 * symbol.hashCode() + 29 * match.hashCode() ; 
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
  }
  public boolean isIter() {
    return false;
  }

  static public class Iter extends Sym {
    // Production: sig("Iter",[arg("org.rascalmpl.ast.Sym","symbol")])
  
    
    private final org.rascalmpl.ast.Sym symbol;
  
    public Iter(IConstructor node , org.rascalmpl.ast.Sym symbol) {
      super(node);
      
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
    public boolean equals(Object o) {
      if (!(o instanceof Iter)) {
        return false;
      }        
      Iter tmp = (Iter) o;
      return true && tmp.symbol.equals(this.symbol) ; 
    }
   
    @Override
    public int hashCode() {
      return 103 + 223 * symbol.hashCode() ; 
    } 
  
    
    @Override
    public org.rascalmpl.ast.Sym getSymbol() {
      return this.symbol;
    }
  
    @Override
    public boolean hasSymbol() {
      return true;
    }	
  }
  public boolean isIterSep() {
    return false;
  }

  static public class IterSep extends Sym {
    // Production: sig("IterSep",[arg("org.rascalmpl.ast.Sym","symbol"),arg("org.rascalmpl.ast.Sym","sep")])
  
    
    private final org.rascalmpl.ast.Sym symbol;
    private final org.rascalmpl.ast.Sym sep;
  
    public IterSep(IConstructor node , org.rascalmpl.ast.Sym symbol,  org.rascalmpl.ast.Sym sep) {
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
      return visitor.visitSymIterSep(this);
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
      return 929 + 601 * symbol.hashCode() + 251 * sep.hashCode() ; 
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
  }
  public boolean isIterStar() {
    return false;
  }

  static public class IterStar extends Sym {
    // Production: sig("IterStar",[arg("org.rascalmpl.ast.Sym","symbol")])
  
    
    private final org.rascalmpl.ast.Sym symbol;
  
    public IterStar(IConstructor node , org.rascalmpl.ast.Sym symbol) {
      super(node);
      
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
    public boolean equals(Object o) {
      if (!(o instanceof IterStar)) {
        return false;
      }        
      IterStar tmp = (IterStar) o;
      return true && tmp.symbol.equals(this.symbol) ; 
    }
   
    @Override
    public int hashCode() {
      return 887 + 857 * symbol.hashCode() ; 
    } 
  
    
    @Override
    public org.rascalmpl.ast.Sym getSymbol() {
      return this.symbol;
    }
  
    @Override
    public boolean hasSymbol() {
      return true;
    }	
  }
  public boolean isIterStarSep() {
    return false;
  }

  static public class IterStarSep extends Sym {
    // Production: sig("IterStarSep",[arg("org.rascalmpl.ast.Sym","symbol"),arg("org.rascalmpl.ast.Sym","sep")])
  
    
    private final org.rascalmpl.ast.Sym symbol;
    private final org.rascalmpl.ast.Sym sep;
  
    public IterStarSep(IConstructor node , org.rascalmpl.ast.Sym symbol,  org.rascalmpl.ast.Sym sep) {
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
      return visitor.visitSymIterStarSep(this);
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
      return 907 + 103 * symbol.hashCode() + 163 * sep.hashCode() ; 
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
  }
  public boolean isLabeled() {
    return false;
  }

  static public class Labeled extends Sym {
    // Production: sig("Labeled",[arg("org.rascalmpl.ast.Sym","symbol"),arg("org.rascalmpl.ast.NonterminalLabel","label")])
  
    
    private final org.rascalmpl.ast.Sym symbol;
    private final org.rascalmpl.ast.NonterminalLabel label;
  
    public Labeled(IConstructor node , org.rascalmpl.ast.Sym symbol,  org.rascalmpl.ast.NonterminalLabel label) {
      super(node);
      
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
    public boolean equals(Object o) {
      if (!(o instanceof Labeled)) {
        return false;
      }        
      Labeled tmp = (Labeled) o;
      return true && tmp.symbol.equals(this.symbol) && tmp.label.equals(this.label) ; 
    }
   
    @Override
    public int hashCode() {
      return 311 + 137 * symbol.hashCode() + 19 * label.hashCode() ; 
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
  }
  public boolean isLiteral() {
    return false;
  }

  static public class Literal extends Sym {
    // Production: sig("Literal",[arg("org.rascalmpl.ast.StringConstant","string")])
  
    
    private final org.rascalmpl.ast.StringConstant string;
  
    public Literal(IConstructor node , org.rascalmpl.ast.StringConstant string) {
      super(node);
      
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
    public boolean equals(Object o) {
      if (!(o instanceof Literal)) {
        return false;
      }        
      Literal tmp = (Literal) o;
      return true && tmp.string.equals(this.string) ; 
    }
   
    @Override
    public int hashCode() {
      return 601 + 641 * string.hashCode() ; 
    } 
  
    
    @Override
    public org.rascalmpl.ast.StringConstant getString() {
      return this.string;
    }
  
    @Override
    public boolean hasString() {
      return true;
    }	
  }
  public boolean isNonterminal() {
    return false;
  }

  static public class Nonterminal extends Sym {
    // Production: sig("Nonterminal",[arg("org.rascalmpl.ast.Nonterminal","nonterminal")])
  
    
    private final org.rascalmpl.ast.Nonterminal nonterminal;
  
    public Nonterminal(IConstructor node , org.rascalmpl.ast.Nonterminal nonterminal) {
      super(node);
      
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
    public boolean equals(Object o) {
      if (!(o instanceof Nonterminal)) {
        return false;
      }        
      Nonterminal tmp = (Nonterminal) o;
      return true && tmp.nonterminal.equals(this.nonterminal) ; 
    }
   
    @Override
    public int hashCode() {
      return 967 + 457 * nonterminal.hashCode() ; 
    } 
  
    
    @Override
    public org.rascalmpl.ast.Nonterminal getNonterminal() {
      return this.nonterminal;
    }
  
    @Override
    public boolean hasNonterminal() {
      return true;
    }	
  }
  public boolean isNotFollow() {
    return false;
  }

  static public class NotFollow extends Sym {
    // Production: sig("NotFollow",[arg("org.rascalmpl.ast.Sym","symbol"),arg("org.rascalmpl.ast.Sym","match")])
  
    
    private final org.rascalmpl.ast.Sym symbol;
    private final org.rascalmpl.ast.Sym match;
  
    public NotFollow(IConstructor node , org.rascalmpl.ast.Sym symbol,  org.rascalmpl.ast.Sym match) {
      super(node);
      
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
    public boolean equals(Object o) {
      if (!(o instanceof NotFollow)) {
        return false;
      }        
      NotFollow tmp = (NotFollow) o;
      return true && tmp.symbol.equals(this.symbol) && tmp.match.equals(this.match) ; 
    }
   
    @Override
    public int hashCode() {
      return 59 + 233 * symbol.hashCode() + 863 * match.hashCode() ; 
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
  }
  public boolean isNotPrecede() {
    return false;
  }

  static public class NotPrecede extends Sym {
    // Production: sig("NotPrecede",[arg("org.rascalmpl.ast.Sym","match"),arg("org.rascalmpl.ast.Sym","symbol")])
  
    
    private final org.rascalmpl.ast.Sym match;
    private final org.rascalmpl.ast.Sym symbol;
  
    public NotPrecede(IConstructor node , org.rascalmpl.ast.Sym match,  org.rascalmpl.ast.Sym symbol) {
      super(node);
      
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
    public boolean equals(Object o) {
      if (!(o instanceof NotPrecede)) {
        return false;
      }        
      NotPrecede tmp = (NotPrecede) o;
      return true && tmp.match.equals(this.match) && tmp.symbol.equals(this.symbol) ; 
    }
   
    @Override
    public int hashCode() {
      return 79 + 19 * match.hashCode() + 113 * symbol.hashCode() ; 
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
  }
  public boolean isOptional() {
    return false;
  }

  static public class Optional extends Sym {
    // Production: sig("Optional",[arg("org.rascalmpl.ast.Sym","symbol")])
  
    
    private final org.rascalmpl.ast.Sym symbol;
  
    public Optional(IConstructor node , org.rascalmpl.ast.Sym symbol) {
      super(node);
      
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
    public boolean equals(Object o) {
      if (!(o instanceof Optional)) {
        return false;
      }        
      Optional tmp = (Optional) o;
      return true && tmp.symbol.equals(this.symbol) ; 
    }
   
    @Override
    public int hashCode() {
      return 157 + 509 * symbol.hashCode() ; 
    } 
  
    
    @Override
    public org.rascalmpl.ast.Sym getSymbol() {
      return this.symbol;
    }
  
    @Override
    public boolean hasSymbol() {
      return true;
    }	
  }
  public boolean isParameter() {
    return false;
  }

  static public class Parameter extends Sym {
    // Production: sig("Parameter",[arg("org.rascalmpl.ast.Nonterminal","nonterminal")])
  
    
    private final org.rascalmpl.ast.Nonterminal nonterminal;
  
    public Parameter(IConstructor node , org.rascalmpl.ast.Nonterminal nonterminal) {
      super(node);
      
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
    public boolean equals(Object o) {
      if (!(o instanceof Parameter)) {
        return false;
      }        
      Parameter tmp = (Parameter) o;
      return true && tmp.nonterminal.equals(this.nonterminal) ; 
    }
   
    @Override
    public int hashCode() {
      return 859 + 457 * nonterminal.hashCode() ; 
    } 
  
    
    @Override
    public org.rascalmpl.ast.Nonterminal getNonterminal() {
      return this.nonterminal;
    }
  
    @Override
    public boolean hasNonterminal() {
      return true;
    }	
  }
  public boolean isParametrized() {
    return false;
  }

  static public class Parametrized extends Sym {
    // Production: sig("Parametrized",[arg("org.rascalmpl.ast.Nonterminal","nonterminal"),arg("java.util.List\<org.rascalmpl.ast.Sym\>","parameters")])
  
    
    private final org.rascalmpl.ast.Nonterminal nonterminal;
    private final java.util.List<org.rascalmpl.ast.Sym> parameters;
  
    public Parametrized(IConstructor node , org.rascalmpl.ast.Nonterminal nonterminal,  java.util.List<org.rascalmpl.ast.Sym> parameters) {
      super(node);
      
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
    public boolean equals(Object o) {
      if (!(o instanceof Parametrized)) {
        return false;
      }        
      Parametrized tmp = (Parametrized) o;
      return true && tmp.nonterminal.equals(this.nonterminal) && tmp.parameters.equals(this.parameters) ; 
    }
   
    @Override
    public int hashCode() {
      return 617 + 103 * nonterminal.hashCode() + 383 * parameters.hashCode() ; 
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
  }
  public boolean isPrecede() {
    return false;
  }

  static public class Precede extends Sym {
    // Production: sig("Precede",[arg("org.rascalmpl.ast.Sym","match"),arg("org.rascalmpl.ast.Sym","symbol")])
  
    
    private final org.rascalmpl.ast.Sym match;
    private final org.rascalmpl.ast.Sym symbol;
  
    public Precede(IConstructor node , org.rascalmpl.ast.Sym match,  org.rascalmpl.ast.Sym symbol) {
      super(node);
      
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
    public boolean equals(Object o) {
      if (!(o instanceof Precede)) {
        return false;
      }        
      Precede tmp = (Precede) o;
      return true && tmp.match.equals(this.match) && tmp.symbol.equals(this.symbol) ; 
    }
   
    @Override
    public int hashCode() {
      return 787 + 691 * match.hashCode() + 919 * symbol.hashCode() ; 
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
  }
  public boolean isSequence() {
    return false;
  }

  static public class Sequence extends Sym {
    // Production: sig("Sequence",[arg("org.rascalmpl.ast.Sym","first"),arg("java.util.List\<org.rascalmpl.ast.Sym\>","sequence")])
  
    
    private final org.rascalmpl.ast.Sym first;
    private final java.util.List<org.rascalmpl.ast.Sym> sequence;
  
    public Sequence(IConstructor node , org.rascalmpl.ast.Sym first,  java.util.List<org.rascalmpl.ast.Sym> sequence) {
      super(node);
      
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
    public boolean equals(Object o) {
      if (!(o instanceof Sequence)) {
        return false;
      }        
      Sequence tmp = (Sequence) o;
      return true && tmp.first.equals(this.first) && tmp.sequence.equals(this.sequence) ; 
    }
   
    @Override
    public int hashCode() {
      return 191 + 67 * first.hashCode() + 607 * sequence.hashCode() ; 
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
  }
  public boolean isStart() {
    return false;
  }

  static public class Start extends Sym {
    // Production: sig("Start",[arg("org.rascalmpl.ast.Nonterminal","nonterminal")])
  
    
    private final org.rascalmpl.ast.Nonterminal nonterminal;
  
    public Start(IConstructor node , org.rascalmpl.ast.Nonterminal nonterminal) {
      super(node);
      
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
    public boolean equals(Object o) {
      if (!(o instanceof Start)) {
        return false;
      }        
      Start tmp = (Start) o;
      return true && tmp.nonterminal.equals(this.nonterminal) ; 
    }
   
    @Override
    public int hashCode() {
      return 853 + 79 * nonterminal.hashCode() ; 
    } 
  
    
    @Override
    public org.rascalmpl.ast.Nonterminal getNonterminal() {
      return this.nonterminal;
    }
  
    @Override
    public boolean hasNonterminal() {
      return true;
    }	
  }
  public boolean isStartOfLine() {
    return false;
  }

  static public class StartOfLine extends Sym {
    // Production: sig("StartOfLine",[arg("org.rascalmpl.ast.Sym","symbol")])
  
    
    private final org.rascalmpl.ast.Sym symbol;
  
    public StartOfLine(IConstructor node , org.rascalmpl.ast.Sym symbol) {
      super(node);
      
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
    public boolean equals(Object o) {
      if (!(o instanceof StartOfLine)) {
        return false;
      }        
      StartOfLine tmp = (StartOfLine) o;
      return true && tmp.symbol.equals(this.symbol) ; 
    }
   
    @Override
    public int hashCode() {
      return 997 + 587 * symbol.hashCode() ; 
    } 
  
    
    @Override
    public org.rascalmpl.ast.Sym getSymbol() {
      return this.symbol;
    }
  
    @Override
    public boolean hasSymbol() {
      return true;
    }	
  }
  public boolean isUnequal() {
    return false;
  }

  static public class Unequal extends Sym {
    // Production: sig("Unequal",[arg("org.rascalmpl.ast.Sym","symbol"),arg("org.rascalmpl.ast.Sym","match")])
  
    
    private final org.rascalmpl.ast.Sym symbol;
    private final org.rascalmpl.ast.Sym match;
  
    public Unequal(IConstructor node , org.rascalmpl.ast.Sym symbol,  org.rascalmpl.ast.Sym match) {
      super(node);
      
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
    public boolean equals(Object o) {
      if (!(o instanceof Unequal)) {
        return false;
      }        
      Unequal tmp = (Unequal) o;
      return true && tmp.symbol.equals(this.symbol) && tmp.match.equals(this.match) ; 
    }
   
    @Override
    public int hashCode() {
      return 937 + 191 * symbol.hashCode() + 479 * match.hashCode() ; 
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
  }
}