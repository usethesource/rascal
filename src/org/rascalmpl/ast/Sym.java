
package org.rascalmpl.ast;


import org.eclipse.imp.pdb.facts.INode;

import org.rascalmpl.interpreter.asserts.Ambiguous;

import org.eclipse.imp.pdb.facts.IConstructor;

import org.eclipse.imp.pdb.facts.IValue;

import org.rascalmpl.interpreter.BooleanEvaluator;

import org.rascalmpl.interpreter.Evaluator;

import org.rascalmpl.interpreter.PatternEvaluator;

import org.rascalmpl.interpreter.asserts.Ambiguous;

import org.rascalmpl.interpreter.env.Environment;

import org.rascalmpl.interpreter.matching.IBooleanResult;

import org.rascalmpl.interpreter.matching.IMatchingResult;

import org.rascalmpl.interpreter.result.Result;


public abstract class Sym extends AbstractAST {
  public Sym(INode node) {
    super(node);
  }
  

  public boolean hasCharClass() {
    return false;
  }

  public org.rascalmpl.ast.Class getCharClass() {
    throw new UnsupportedOperationException();
  }

  public boolean hasColumn() {
    return false;
  }

  public org.rascalmpl.ast.IntegerLiteral getColumn() {
    throw new UnsupportedOperationException();
  }

  public boolean hasCistring() {
    return false;
  }

  public org.rascalmpl.ast.CaseInsensitiveStringConstant getCistring() {
    throw new UnsupportedOperationException();
  }

  public boolean hasSymbol() {
    return false;
  }

  public org.rascalmpl.ast.Sym getSymbol() {
    throw new UnsupportedOperationException();
  }

  public boolean hasSep() {
    return false;
  }

  public org.rascalmpl.ast.StringConstant getSep() {
    throw new UnsupportedOperationException();
  }

  public boolean hasPnonterminal() {
    return false;
  }

  public org.rascalmpl.ast.ParameterizedNonterminal getPnonterminal() {
    throw new UnsupportedOperationException();
  }

  public boolean hasString() {
    return false;
  }

  public org.rascalmpl.ast.StringConstant getString() {
    throw new UnsupportedOperationException();
  }

  public boolean hasParameters() {
    return false;
  }

  public java.util.List<org.rascalmpl.ast.Sym> getParameters() {
    throw new UnsupportedOperationException();
  }

  public boolean hasLabel() {
    return false;
  }

  public org.rascalmpl.ast.NonterminalLabel getLabel() {
    throw new UnsupportedOperationException();
  }

  public boolean hasNonterminal() {
    return false;
  }

  public org.rascalmpl.ast.Nonterminal getNonterminal() {
    throw new UnsupportedOperationException();
  }


static public class Ambiguity extends Sym {
  private final java.util.List<org.rascalmpl.ast.Sym> alternatives;

  public Ambiguity(INode node, java.util.List<org.rascalmpl.ast.Sym> alternatives) {
    super(node);
    this.alternatives = java.util.Collections.unmodifiableList(alternatives);
  }

  @Override
  public Result<IValue> interpret(Evaluator __eval) {
    throw new Ambiguous((IConstructor) this.getTree());
  }
  
  @Override
  public org.eclipse.imp.pdb.facts.type.Type typeOf(Environment env) {
    throw new Ambiguous((IConstructor) this.getTree());
  }
  
  @Override
  public IBooleanResult buildBooleanBacktracker(BooleanEvaluator __eval) {
    throw new Ambiguous((IConstructor) this.getTree());
  }

  @Override
  public IMatchingResult buildMatcher(PatternEvaluator __eval) {
    throw new Ambiguous((IConstructor) this.getTree());
  }
  
  public java.util.List<org.rascalmpl.ast.Sym> getAlternatives() {
   return alternatives;
  }

  public <T> T accept(IASTVisitor<T> v) {
	return v.visitSymAmbiguity(this);
  }
}





  public boolean isStartOfLine() {
    return false;
  }
  
static public class StartOfLine extends Sym {
  // Production: sig("StartOfLine",[])

  

  
public StartOfLine(INode node ) {
  super(node);
  
}


  @Override
  public boolean isStartOfLine() { 
    return true; 
  }

  @Override
  public <T> T accept(IASTVisitor<T> visitor) {
    return visitor.visitSymStartOfLine(this);
  }
  
  	
}


  public boolean isNonterminal() {
    return false;
  }
  
static public class Nonterminal extends Sym {
  // Production: sig("Nonterminal",[arg("org.rascalmpl.ast.Nonterminal","nonterminal")])

  
     private final org.rascalmpl.ast.Nonterminal nonterminal;
  

  
public Nonterminal(INode node , org.rascalmpl.ast.Nonterminal nonterminal) {
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
     public org.rascalmpl.ast.Nonterminal getNonterminal() {
        return this.nonterminal;
     }
     
     @Override
     public boolean hasNonterminal() {
        return true;
     }
  	
}


  public boolean isOptional() {
    return false;
  }
  
static public class Optional extends Sym {
  // Production: sig("Optional",[arg("org.rascalmpl.ast.Sym","symbol")])

  
     private final org.rascalmpl.ast.Sym symbol;
  

  
public Optional(INode node , org.rascalmpl.ast.Sym symbol) {
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
  

  
public Parameter(INode node , org.rascalmpl.ast.Nonterminal nonterminal) {
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
     public org.rascalmpl.ast.Nonterminal getNonterminal() {
        return this.nonterminal;
     }
     
     @Override
     public boolean hasNonterminal() {
        return true;
     }
  	
}


  public boolean isCaseInsensitiveLiteral() {
    return false;
  }
  
static public class CaseInsensitiveLiteral extends Sym {
  // Production: sig("CaseInsensitiveLiteral",[arg("org.rascalmpl.ast.CaseInsensitiveStringConstant","cistring")])

  
     private final org.rascalmpl.ast.CaseInsensitiveStringConstant cistring;
  

  
public CaseInsensitiveLiteral(INode node , org.rascalmpl.ast.CaseInsensitiveStringConstant cistring) {
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
  

  
public CharacterClass(INode node , org.rascalmpl.ast.Class charClass) {
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
     public org.rascalmpl.ast.Class getCharClass() {
        return this.charClass;
     }
     
     @Override
     public boolean hasCharClass() {
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
  

  
public Labeled(INode node , org.rascalmpl.ast.Sym symbol,  org.rascalmpl.ast.NonterminalLabel label) {
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


  public boolean isIterStar() {
    return false;
  }
  
static public class IterStar extends Sym {
  // Production: sig("IterStar",[arg("org.rascalmpl.ast.Sym","symbol")])

  
     private final org.rascalmpl.ast.Sym symbol;
  

  
public IterStar(INode node , org.rascalmpl.ast.Sym symbol) {
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
     public org.rascalmpl.ast.Sym getSymbol() {
        return this.symbol;
     }
     
     @Override
     public boolean hasSymbol() {
        return true;
     }
  	
}


  public boolean isParametrized() {
    return false;
  }
  
static public class Parametrized extends Sym {
  // Production: sig("Parametrized",[arg("org.rascalmpl.ast.ParameterizedNonterminal","pnonterminal"),arg("java.util.List\<org.rascalmpl.ast.Sym\>","parameters")])

  
     private final org.rascalmpl.ast.ParameterizedNonterminal pnonterminal;
  
     private final java.util.List<org.rascalmpl.ast.Sym> parameters;
  

  
public Parametrized(INode node , org.rascalmpl.ast.ParameterizedNonterminal pnonterminal,  java.util.List<org.rascalmpl.ast.Sym> parameters) {
  super(node);
  
    this.pnonterminal = pnonterminal;
  
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
     public org.rascalmpl.ast.ParameterizedNonterminal getPnonterminal() {
        return this.pnonterminal;
     }
     
     @Override
     public boolean hasPnonterminal() {
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


  public boolean isIter() {
    return false;
  }
  
static public class Iter extends Sym {
  // Production: sig("Iter",[arg("org.rascalmpl.ast.Sym","symbol")])

  
     private final org.rascalmpl.ast.Sym symbol;
  

  
public Iter(INode node , org.rascalmpl.ast.Sym symbol) {
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
     public org.rascalmpl.ast.Sym getSymbol() {
        return this.symbol;
     }
     
     @Override
     public boolean hasSymbol() {
        return true;
     }
  	
}


  public boolean isEndOfLine() {
    return false;
  }
  
static public class EndOfLine extends Sym {
  // Production: sig("EndOfLine",[])

  

  
public EndOfLine(INode node ) {
  super(node);
  
}


  @Override
  public boolean isEndOfLine() { 
    return true; 
  }

  @Override
  public <T> T accept(IASTVisitor<T> visitor) {
    return visitor.visitSymEndOfLine(this);
  }
  
  	
}


  public boolean isIterStarSep() {
    return false;
  }
  
static public class IterStarSep extends Sym {
  // Production: sig("IterStarSep",[arg("org.rascalmpl.ast.Sym","symbol"),arg("org.rascalmpl.ast.StringConstant","sep")])

  
     private final org.rascalmpl.ast.Sym symbol;
  
     private final org.rascalmpl.ast.StringConstant sep;
  

  
public IterStarSep(INode node , org.rascalmpl.ast.Sym symbol,  org.rascalmpl.ast.StringConstant sep) {
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
     public org.rascalmpl.ast.Sym getSymbol() {
        return this.symbol;
     }
     
     @Override
     public boolean hasSymbol() {
        return true;
     }
  
     @Override
     public org.rascalmpl.ast.StringConstant getSep() {
        return this.sep;
     }
     
     @Override
     public boolean hasSep() {
        return true;
     }
  	
}


  public boolean isLiteral() {
    return false;
  }
  
static public class Literal extends Sym {
  // Production: sig("Literal",[arg("org.rascalmpl.ast.StringConstant","string")])

  
     private final org.rascalmpl.ast.StringConstant string;
  

  
public Literal(INode node , org.rascalmpl.ast.StringConstant string) {
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
     public org.rascalmpl.ast.StringConstant getString() {
        return this.string;
     }
     
     @Override
     public boolean hasString() {
        return true;
     }
  	
}


  public boolean isColumn() {
    return false;
  }
  
static public class Column extends Sym {
  // Production: sig("Column",[arg("org.rascalmpl.ast.IntegerLiteral","column")])

  
     private final org.rascalmpl.ast.IntegerLiteral column;
  

  
public Column(INode node , org.rascalmpl.ast.IntegerLiteral column) {
  super(node);
  
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
     public org.rascalmpl.ast.IntegerLiteral getColumn() {
        return this.column;
     }
     
     @Override
     public boolean hasColumn() {
        return true;
     }
  	
}


  public boolean isIterSep() {
    return false;
  }
  
static public class IterSep extends Sym {
  // Production: sig("IterSep",[arg("org.rascalmpl.ast.Sym","symbol"),arg("org.rascalmpl.ast.StringConstant","sep")])

  
     private final org.rascalmpl.ast.Sym symbol;
  
     private final org.rascalmpl.ast.StringConstant sep;
  

  
public IterSep(INode node , org.rascalmpl.ast.Sym symbol,  org.rascalmpl.ast.StringConstant sep) {
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
     public org.rascalmpl.ast.Sym getSymbol() {
        return this.symbol;
     }
     
     @Override
     public boolean hasSymbol() {
        return true;
     }
  
     @Override
     public org.rascalmpl.ast.StringConstant getSep() {
        return this.sep;
     }
     
     @Override
     public boolean hasSep() {
        return true;
     }
  	
}



}
