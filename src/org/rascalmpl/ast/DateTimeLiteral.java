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


public abstract class DateTimeLiteral extends AbstractAST {
  public DateTimeLiteral(INode node) {
    super(node);
  }
  

  public boolean hasDateAndTime() {
    return false;
  }

  public org.rascalmpl.ast.DateAndTime getDateAndTime() {
    throw new UnsupportedOperationException();
  }

  public boolean hasTime() {
    return false;
  }

  public org.rascalmpl.ast.JustTime getTime() {
    throw new UnsupportedOperationException();
  }

  public boolean hasDate() {
    return false;
  }

  public org.rascalmpl.ast.JustDate getDate() {
    throw new UnsupportedOperationException();
  }


static public class Ambiguity extends DateTimeLiteral {
  private final java.util.List<org.rascalmpl.ast.DateTimeLiteral> alternatives;

  public Ambiguity(INode node, java.util.List<org.rascalmpl.ast.DateTimeLiteral> alternatives) {
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
  
  public java.util.List<org.rascalmpl.ast.DateTimeLiteral> getAlternatives() {
   return alternatives;
  }

  public <T> T accept(IASTVisitor<T> v) {
	return v.visitDateTimeLiteralAmbiguity(this);
  }
}





  public boolean isDateAndTimeLiteral() {
    return false;
  }
  
static public class DateAndTimeLiteral extends DateTimeLiteral {
  // Production: sig("DateAndTimeLiteral",[arg("org.rascalmpl.ast.DateAndTime","dateAndTime")])

  
     private final org.rascalmpl.ast.DateAndTime dateAndTime;
  

  
public DateAndTimeLiteral(INode node , org.rascalmpl.ast.DateAndTime dateAndTime) {
  super(node);
  
    this.dateAndTime = dateAndTime;
  
}


  @Override
  public boolean isDateAndTimeLiteral() { 
    return true; 
  }

  @Override
  public <T> T accept(IASTVisitor<T> visitor) {
    return visitor.visitDateTimeLiteralDateAndTimeLiteral(this);
  }
  
  
     @Override
     public org.rascalmpl.ast.DateAndTime getDateAndTime() {
        return this.dateAndTime;
     }
     
     @Override
     public boolean hasDateAndTime() {
        return true;
     }
  	
}


  public boolean isTimeLiteral() {
    return false;
  }
  
static public class TimeLiteral extends DateTimeLiteral {
  // Production: sig("TimeLiteral",[arg("org.rascalmpl.ast.JustTime","time")])

  
     private final org.rascalmpl.ast.JustTime time;
  

  
public TimeLiteral(INode node , org.rascalmpl.ast.JustTime time) {
  super(node);
  
    this.time = time;
  
}


  @Override
  public boolean isTimeLiteral() { 
    return true; 
  }

  @Override
  public <T> T accept(IASTVisitor<T> visitor) {
    return visitor.visitDateTimeLiteralTimeLiteral(this);
  }
  
  
     @Override
     public org.rascalmpl.ast.JustTime getTime() {
        return this.time;
     }
     
     @Override
     public boolean hasTime() {
        return true;
     }
  	
}


  public boolean isDateLiteral() {
    return false;
  }
  
static public class DateLiteral extends DateTimeLiteral {
  // Production: sig("DateLiteral",[arg("org.rascalmpl.ast.JustDate","date")])

  
     private final org.rascalmpl.ast.JustDate date;
  

  
public DateLiteral(INode node , org.rascalmpl.ast.JustDate date) {
  super(node);
  
    this.date = date;
  
}


  @Override
  public boolean isDateLiteral() { 
    return true; 
  }

  @Override
  public <T> T accept(IASTVisitor<T> visitor) {
    return visitor.visitDateTimeLiteralDateLiteral(this);
  }
  
  
     @Override
     public org.rascalmpl.ast.JustDate getDate() {
        return this.date;
     }
     
     @Override
     public boolean hasDate() {
        return true;
     }
  	
}



}
