
package org.rascalmpl.ast;


import org.eclipse.imp.pdb.facts.IConstructor;

import org.eclipse.imp.pdb.facts.IConstructor;

import org.eclipse.imp.pdb.facts.IValue;

import org.rascalmpl.interpreter.Evaluator;

import org.rascalmpl.interpreter.asserts.Ambiguous;

import org.rascalmpl.interpreter.env.Environment;

import org.rascalmpl.interpreter.matching.IBooleanResult;

import org.rascalmpl.interpreter.matching.IMatchingResult;

import org.rascalmpl.interpreter.result.Result;


public abstract class IntegerLiteral extends AbstractAST {
  public IntegerLiteral(IConstructor node) {
    super(node);
  }
  

  public boolean hasOctal() {
    return false;
  }

  public org.rascalmpl.ast.OctalIntegerLiteral getOctal() {
    throw new UnsupportedOperationException();
  }

  public boolean hasHex() {
    return false;
  }

  public org.rascalmpl.ast.HexIntegerLiteral getHex() {
    throw new UnsupportedOperationException();
  }

  public boolean hasDecimal() {
    return false;
  }

  public org.rascalmpl.ast.DecimalIntegerLiteral getDecimal() {
    throw new UnsupportedOperationException();
  }


static public class Ambiguity extends IntegerLiteral {
  private final java.util.List<org.rascalmpl.ast.IntegerLiteral> alternatives;

  public Ambiguity(IConstructor node, java.util.List<org.rascalmpl.ast.IntegerLiteral> alternatives) {
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
  
  public java.util.List<org.rascalmpl.ast.IntegerLiteral> getAlternatives() {
   return alternatives;
  }

  public <T> T accept(IASTVisitor<T> v) {
	return v.visitIntegerLiteralAmbiguity(this);
  }
}





  public boolean isOctalIntegerLiteral() {
    return false;
  }
  
static public class OctalIntegerLiteral extends IntegerLiteral {
  // Production: sig("OctalIntegerLiteral",[arg("org.rascalmpl.ast.OctalIntegerLiteral","octal")])

  
     private final org.rascalmpl.ast.OctalIntegerLiteral octal;
  

  
public OctalIntegerLiteral(IConstructor node , org.rascalmpl.ast.OctalIntegerLiteral octal) {
  super(node);
  
    this.octal = octal;
  
}


  @Override
  public boolean isOctalIntegerLiteral() { 
    return true; 
  }

  @Override
  public <T> T accept(IASTVisitor<T> visitor) {
    return visitor.visitIntegerLiteralOctalIntegerLiteral(this);
  }
  
  
     @Override
     public org.rascalmpl.ast.OctalIntegerLiteral getOctal() {
        return this.octal;
     }
     
     @Override
     public boolean hasOctal() {
        return true;
     }
  	
}


  public boolean isHexIntegerLiteral() {
    return false;
  }
  
static public class HexIntegerLiteral extends IntegerLiteral {
  // Production: sig("HexIntegerLiteral",[arg("org.rascalmpl.ast.HexIntegerLiteral","hex")])

  
     private final org.rascalmpl.ast.HexIntegerLiteral hex;
  

  
public HexIntegerLiteral(IConstructor node , org.rascalmpl.ast.HexIntegerLiteral hex) {
  super(node);
  
    this.hex = hex;
  
}


  @Override
  public boolean isHexIntegerLiteral() { 
    return true; 
  }

  @Override
  public <T> T accept(IASTVisitor<T> visitor) {
    return visitor.visitIntegerLiteralHexIntegerLiteral(this);
  }
  
  
     @Override
     public org.rascalmpl.ast.HexIntegerLiteral getHex() {
        return this.hex;
     }
     
     @Override
     public boolean hasHex() {
        return true;
     }
  	
}


  public boolean isDecimalIntegerLiteral() {
    return false;
  }
  
static public class DecimalIntegerLiteral extends IntegerLiteral {
  // Production: sig("DecimalIntegerLiteral",[arg("org.rascalmpl.ast.DecimalIntegerLiteral","decimal")])

  
     private final org.rascalmpl.ast.DecimalIntegerLiteral decimal;
  

  
public DecimalIntegerLiteral(IConstructor node , org.rascalmpl.ast.DecimalIntegerLiteral decimal) {
  super(node);
  
    this.decimal = decimal;
  
}


  @Override
  public boolean isDecimalIntegerLiteral() { 
    return true; 
  }

  @Override
  public <T> T accept(IASTVisitor<T> visitor) {
    return visitor.visitIntegerLiteralDecimalIntegerLiteral(this);
  }
  
  
     @Override
     public org.rascalmpl.ast.DecimalIntegerLiteral getDecimal() {
        return this.decimal;
     }
     
     @Override
     public boolean hasDecimal() {
        return true;
     }
  	
}



}
