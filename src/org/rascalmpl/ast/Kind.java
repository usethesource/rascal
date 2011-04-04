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


public abstract class Kind extends AbstractAST {
  public Kind(INode node) {
    super(node);
  }
  


static public class Ambiguity extends Kind {
  private final java.util.List<org.rascalmpl.ast.Kind> alternatives;

  public Ambiguity(INode node, java.util.List<org.rascalmpl.ast.Kind> alternatives) {
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
  
  public java.util.List<org.rascalmpl.ast.Kind> getAlternatives() {
   return alternatives;
  }

  public <T> T accept(IASTVisitor<T> v) {
	return v.visitKindAmbiguity(this);
  }
}





  public boolean isModule() {
    return false;
  }
  
static public class Module extends Kind {
  // Production: sig("Module",[])

  

  
public Module(INode node ) {
  super(node);
  
}


  @Override
  public boolean isModule() { 
    return true; 
  }

  @Override
  public <T> T accept(IASTVisitor<T> visitor) {
    return visitor.visitKindModule(this);
  }
  
  	
}


  public boolean isRule() {
    return false;
  }
  
static public class Rule extends Kind {
  // Production: sig("Rule",[])

  

  
public Rule(INode node ) {
  super(node);
  
}


  @Override
  public boolean isRule() { 
    return true; 
  }

  @Override
  public <T> T accept(IASTVisitor<T> visitor) {
    return visitor.visitKindRule(this);
  }
  
  	
}


  public boolean isVariable() {
    return false;
  }
  
static public class Variable extends Kind {
  // Production: sig("Variable",[])

  

  
public Variable(INode node ) {
  super(node);
  
}


  @Override
  public boolean isVariable() { 
    return true; 
  }

  @Override
  public <T> T accept(IASTVisitor<T> visitor) {
    return visitor.visitKindVariable(this);
  }
  
  	
}


  public boolean isAnno() {
    return false;
  }
  
static public class Anno extends Kind {
  // Production: sig("Anno",[])

  

  
public Anno(INode node ) {
  super(node);
  
}


  @Override
  public boolean isAnno() { 
    return true; 
  }

  @Override
  public <T> T accept(IASTVisitor<T> visitor) {
    return visitor.visitKindAnno(this);
  }
  
  	
}


  public boolean isFunction() {
    return false;
  }
  
static public class Function extends Kind {
  // Production: sig("Function",[])

  

  
public Function(INode node ) {
  super(node);
  
}


  @Override
  public boolean isFunction() { 
    return true; 
  }

  @Override
  public <T> T accept(IASTVisitor<T> visitor) {
    return visitor.visitKindFunction(this);
  }
  
  	
}


  public boolean isData() {
    return false;
  }
  
static public class Data extends Kind {
  // Production: sig("Data",[])

  

  
public Data(INode node ) {
  super(node);
  
}


  @Override
  public boolean isData() { 
    return true; 
  }

  @Override
  public <T> T accept(IASTVisitor<T> visitor) {
    return visitor.visitKindData(this);
  }
  
  	
}


  public boolean isTag() {
    return false;
  }
  
static public class Tag extends Kind {
  // Production: sig("Tag",[])

  

  
public Tag(INode node ) {
  super(node);
  
}


  @Override
  public boolean isTag() { 
    return true; 
  }

  @Override
  public <T> T accept(IASTVisitor<T> visitor) {
    return visitor.visitKindTag(this);
  }
  
  	
}


  public boolean isView() {
    return false;
  }
  
static public class View extends Kind {
  // Production: sig("View",[])

  

  
public View(INode node ) {
  super(node);
  
}


  @Override
  public boolean isView() { 
    return true; 
  }

  @Override
  public <T> T accept(IASTVisitor<T> visitor) {
    return visitor.visitKindView(this);
  }
  
  	
}


  public boolean isAlias() {
    return false;
  }
  
static public class Alias extends Kind {
  // Production: sig("Alias",[])

  

  
public Alias(INode node ) {
  super(node);
  
}


  @Override
  public boolean isAlias() { 
    return true; 
  }

  @Override
  public <T> T accept(IASTVisitor<T> visitor) {
    return visitor.visitKindAlias(this);
  }
  
  	
}


  public boolean isAll() {
    return false;
  }
  
static public class All extends Kind {
  // Production: sig("All",[])

  

  
public All(INode node ) {
  super(node);
  
}


  @Override
  public boolean isAll() { 
    return true; 
  }

  @Override
  public <T> T accept(IASTVisitor<T> visitor) {
    return visitor.visitKindAll(this);
  }
  
  	
}



}
