/*******************************************************************************
 * Copyright (c) 2009-2011 CWI
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
*******************************************************************************/

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


public abstract class LongLiteral extends AbstractAST {
  public LongLiteral(INode node) {
    super(node);
  }
  

  public boolean hasHexLong() {
    return false;
  }

  public org.rascalmpl.ast.HexLongLiteral getHexLong() {
    throw new UnsupportedOperationException();
  }

  public boolean hasDecimalLong() {
    return false;
  }

  public org.rascalmpl.ast.DecimalLongLiteral getDecimalLong() {
    throw new UnsupportedOperationException();
  }

  public boolean hasOctalLong() {
    return false;
  }

  public org.rascalmpl.ast.OctalLongLiteral getOctalLong() {
    throw new UnsupportedOperationException();
  }


static public class Ambiguity extends LongLiteral {
  private final java.util.List<org.rascalmpl.ast.LongLiteral> alternatives;

  public Ambiguity(INode node, java.util.List<org.rascalmpl.ast.LongLiteral> alternatives) {
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
  
  public java.util.List<org.rascalmpl.ast.LongLiteral> getAlternatives() {
   return alternatives;
  }

  public <T> T accept(IASTVisitor<T> v) {
	return v.visitLongLiteralAmbiguity(this);
  }
}





  public boolean isOctalLongLiteral() {
    return false;
  }
  
static public class OctalLongLiteral extends LongLiteral {
  // Production: sig("OctalLongLiteral",[arg("org.rascalmpl.ast.OctalLongLiteral","octalLong")])

  
     private final org.rascalmpl.ast.OctalLongLiteral octalLong;
  

  
public OctalLongLiteral(INode node , org.rascalmpl.ast.OctalLongLiteral octalLong) {
  super(node);
  
    this.octalLong = octalLong;
  
}


  @Override
  public boolean isOctalLongLiteral() { 
    return true; 
  }

  @Override
  public <T> T accept(IASTVisitor<T> visitor) {
    return visitor.visitLongLiteralOctalLongLiteral(this);
  }
  
  
     @Override
     public org.rascalmpl.ast.OctalLongLiteral getOctalLong() {
        return this.octalLong;
     }
     
     @Override
     public boolean hasOctalLong() {
        return true;
     }
  	
}


  public boolean isDecimalLongLiteral() {
    return false;
  }
  
static public class DecimalLongLiteral extends LongLiteral {
  // Production: sig("DecimalLongLiteral",[arg("org.rascalmpl.ast.DecimalLongLiteral","decimalLong")])

  
     private final org.rascalmpl.ast.DecimalLongLiteral decimalLong;
  

  
public DecimalLongLiteral(INode node , org.rascalmpl.ast.DecimalLongLiteral decimalLong) {
  super(node);
  
    this.decimalLong = decimalLong;
  
}


  @Override
  public boolean isDecimalLongLiteral() { 
    return true; 
  }

  @Override
  public <T> T accept(IASTVisitor<T> visitor) {
    return visitor.visitLongLiteralDecimalLongLiteral(this);
  }
  
  
     @Override
     public org.rascalmpl.ast.DecimalLongLiteral getDecimalLong() {
        return this.decimalLong;
     }
     
     @Override
     public boolean hasDecimalLong() {
        return true;
     }
  	
}


  public boolean isHexLongLiteral() {
    return false;
  }
  
static public class HexLongLiteral extends LongLiteral {
  // Production: sig("HexLongLiteral",[arg("org.rascalmpl.ast.HexLongLiteral","hexLong")])

  
     private final org.rascalmpl.ast.HexLongLiteral hexLong;
  

  
public HexLongLiteral(INode node , org.rascalmpl.ast.HexLongLiteral hexLong) {
  super(node);
  
    this.hexLong = hexLong;
  
}


  @Override
  public boolean isHexLongLiteral() { 
    return true; 
  }

  @Override
  public <T> T accept(IASTVisitor<T> visitor) {
    return visitor.visitLongLiteralHexLongLiteral(this);
  }
  
  
     @Override
     public org.rascalmpl.ast.HexLongLiteral getHexLong() {
        return this.hexLong;
     }
     
     @Override
     public boolean hasHexLong() {
        return true;
     }
  	
}



}
