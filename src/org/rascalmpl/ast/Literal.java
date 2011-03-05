
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


public abstract class Literal extends AbstractAST {
  public Literal(INode node) {
    super(node);
  }
  

  public boolean hasRegExpLiteral() {
    return false;
  }

  public org.rascalmpl.ast.RegExpLiteral getRegExpLiteral() {
    throw new UnsupportedOperationException();
  }

  public boolean hasLocationLiteral() {
    return false;
  }

  public org.rascalmpl.ast.LocationLiteral getLocationLiteral() {
    throw new UnsupportedOperationException();
  }

  public boolean hasDateTimeLiteral() {
    return false;
  }

  public org.rascalmpl.ast.DateTimeLiteral getDateTimeLiteral() {
    throw new UnsupportedOperationException();
  }

  public boolean hasIntegerLiteral() {
    return false;
  }

  public org.rascalmpl.ast.IntegerLiteral getIntegerLiteral() {
    throw new UnsupportedOperationException();
  }

  public boolean hasRealLiteral() {
    return false;
  }

  public org.rascalmpl.ast.RealLiteral getRealLiteral() {
    throw new UnsupportedOperationException();
  }

  public boolean hasBooleanLiteral() {
    return false;
  }

  public org.rascalmpl.ast.BooleanLiteral getBooleanLiteral() {
    throw new UnsupportedOperationException();
  }

  public boolean hasStringLiteral() {
    return false;
  }

  public org.rascalmpl.ast.StringLiteral getStringLiteral() {
    throw new UnsupportedOperationException();
  }


static public class Ambiguity extends Literal {
  private final java.util.List<org.rascalmpl.ast.Literal> alternatives;

  public Ambiguity(INode node, java.util.List<org.rascalmpl.ast.Literal> alternatives) {
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
  
  public java.util.List<org.rascalmpl.ast.Literal> getAlternatives() {
   return alternatives;
  }

  public <T> T accept(IASTVisitor<T> v) {
	return v.visitLiteralAmbiguity(this);
  }
}





  public boolean isLocation() {
    return false;
  }
  
static public class Location extends Literal {
  // Production: sig("Location",[arg("org.rascalmpl.ast.LocationLiteral","locationLiteral")])

  
     private final org.rascalmpl.ast.LocationLiteral locationLiteral;
  

  
public Location(INode node , org.rascalmpl.ast.LocationLiteral locationLiteral) {
  super(node);
  
    this.locationLiteral = locationLiteral;
  
}


  @Override
  public boolean isLocation() { 
    return true; 
  }

  @Override
  public <T> T accept(IASTVisitor<T> visitor) {
    return visitor.visitLiteralLocation(this);
  }
  
  
     @Override
     public org.rascalmpl.ast.LocationLiteral getLocationLiteral() {
        return this.locationLiteral;
     }
     
     @Override
     public boolean hasLocationLiteral() {
        return true;
     }
  	
}


  public boolean isString() {
    return false;
  }
  
static public class String extends Literal {
  // Production: sig("String",[arg("org.rascalmpl.ast.StringLiteral","stringLiteral")])

  
     private final org.rascalmpl.ast.StringLiteral stringLiteral;
  

  
public String(INode node , org.rascalmpl.ast.StringLiteral stringLiteral) {
  super(node);
  
    this.stringLiteral = stringLiteral;
  
}


  @Override
  public boolean isString() { 
    return true; 
  }

  @Override
  public <T> T accept(IASTVisitor<T> visitor) {
    return visitor.visitLiteralString(this);
  }
  
  
     @Override
     public org.rascalmpl.ast.StringLiteral getStringLiteral() {
        return this.stringLiteral;
     }
     
     @Override
     public boolean hasStringLiteral() {
        return true;
     }
  	
}


  public boolean isRegExp() {
    return false;
  }
  
static public class RegExp extends Literal {
  // Production: sig("RegExp",[arg("org.rascalmpl.ast.RegExpLiteral","regExpLiteral")])

  
     private final org.rascalmpl.ast.RegExpLiteral regExpLiteral;
  

  
public RegExp(INode node , org.rascalmpl.ast.RegExpLiteral regExpLiteral) {
  super(node);
  
    this.regExpLiteral = regExpLiteral;
  
}


  @Override
  public boolean isRegExp() { 
    return true; 
  }

  @Override
  public <T> T accept(IASTVisitor<T> visitor) {
    return visitor.visitLiteralRegExp(this);
  }
  
  
     @Override
     public org.rascalmpl.ast.RegExpLiteral getRegExpLiteral() {
        return this.regExpLiteral;
     }
     
     @Override
     public boolean hasRegExpLiteral() {
        return true;
     }
  	
}


  public boolean isReal() {
    return false;
  }
  
static public class Real extends Literal {
  // Production: sig("Real",[arg("org.rascalmpl.ast.RealLiteral","realLiteral")])

  
     private final org.rascalmpl.ast.RealLiteral realLiteral;
  

  
public Real(INode node , org.rascalmpl.ast.RealLiteral realLiteral) {
  super(node);
  
    this.realLiteral = realLiteral;
  
}


  @Override
  public boolean isReal() { 
    return true; 
  }

  @Override
  public <T> T accept(IASTVisitor<T> visitor) {
    return visitor.visitLiteralReal(this);
  }
  
  
     @Override
     public org.rascalmpl.ast.RealLiteral getRealLiteral() {
        return this.realLiteral;
     }
     
     @Override
     public boolean hasRealLiteral() {
        return true;
     }
  	
}


  public boolean isBoolean() {
    return false;
  }
  
static public class Boolean extends Literal {
  // Production: sig("Boolean",[arg("org.rascalmpl.ast.BooleanLiteral","booleanLiteral")])

  
     private final org.rascalmpl.ast.BooleanLiteral booleanLiteral;
  

  
public Boolean(INode node , org.rascalmpl.ast.BooleanLiteral booleanLiteral) {
  super(node);
  
    this.booleanLiteral = booleanLiteral;
  
}


  @Override
  public boolean isBoolean() { 
    return true; 
  }

  @Override
  public <T> T accept(IASTVisitor<T> visitor) {
    return visitor.visitLiteralBoolean(this);
  }
  
  
     @Override
     public org.rascalmpl.ast.BooleanLiteral getBooleanLiteral() {
        return this.booleanLiteral;
     }
     
     @Override
     public boolean hasBooleanLiteral() {
        return true;
     }
  	
}


  public boolean isDateTime() {
    return false;
  }
  
static public class DateTime extends Literal {
  // Production: sig("DateTime",[arg("org.rascalmpl.ast.DateTimeLiteral","dateTimeLiteral")])

  
     private final org.rascalmpl.ast.DateTimeLiteral dateTimeLiteral;
  

  
public DateTime(INode node , org.rascalmpl.ast.DateTimeLiteral dateTimeLiteral) {
  super(node);
  
    this.dateTimeLiteral = dateTimeLiteral;
  
}


  @Override
  public boolean isDateTime() { 
    return true; 
  }

  @Override
  public <T> T accept(IASTVisitor<T> visitor) {
    return visitor.visitLiteralDateTime(this);
  }
  
  
     @Override
     public org.rascalmpl.ast.DateTimeLiteral getDateTimeLiteral() {
        return this.dateTimeLiteral;
     }
     
     @Override
     public boolean hasDateTimeLiteral() {
        return true;
     }
  	
}


  public boolean isInteger() {
    return false;
  }
  
static public class Integer extends Literal {
  // Production: sig("Integer",[arg("org.rascalmpl.ast.IntegerLiteral","integerLiteral")])

  
     private final org.rascalmpl.ast.IntegerLiteral integerLiteral;
  

  
public Integer(INode node , org.rascalmpl.ast.IntegerLiteral integerLiteral) {
  super(node);
  
    this.integerLiteral = integerLiteral;
  
}


  @Override
  public boolean isInteger() { 
    return true; 
  }

  @Override
  public <T> T accept(IASTVisitor<T> visitor) {
    return visitor.visitLiteralInteger(this);
  }
  
  
     @Override
     public org.rascalmpl.ast.IntegerLiteral getIntegerLiteral() {
        return this.integerLiteral;
     }
     
     @Override
     public boolean hasIntegerLiteral() {
        return true;
     }
  	
}



}
