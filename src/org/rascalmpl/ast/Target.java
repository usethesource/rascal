/*******************************************************************************
 * Copyright (c) 2009-2012 CWI
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
import org.eclipse.imp.pdb.facts.IValue;
import org.rascalmpl.interpreter.IEvaluator;
import org.rascalmpl.interpreter.asserts.Ambiguous;
import org.rascalmpl.interpreter.env.Environment;
import org.rascalmpl.interpreter.result.Result;

public abstract class Target extends AbstractAST {
  public Target(IConstructor node) {
    super();
  }

  
  public boolean hasName() {
    return false;
  }

  public org.rascalmpl.ast.Name getName() {
    throw new UnsupportedOperationException();
  }

  static public class Ambiguity extends Target {
    private final java.util.List<org.rascalmpl.ast.Target> alternatives;
    private final IConstructor node;
           
    public Ambiguity(IConstructor node, java.util.List<org.rascalmpl.ast.Target> alternatives) {
      super(node);
      this.node = node;
      this.alternatives = java.util.Collections.unmodifiableList(alternatives);
    }
    
    @Override
    public IConstructor getTree() {
      return node;
    }
  
    @Override
    public AbstractAST findNode(int offset) {
      return null;
    }
  
    @Override
    public Result<IValue> interpret(IEvaluator<Result<IValue>> __eval) {
      throw new Ambiguous(src);
    }
      
    @Override
    public org.eclipse.imp.pdb.facts.type.Type typeOf(Environment env) {
      throw new Ambiguous(src);
    }
    
    public java.util.List<org.rascalmpl.ast.Target> getAlternatives() {
      return alternatives;
    }
    
    public <T> T accept(IASTVisitor<T> v) {
    	return v.visitTargetAmbiguity(this);
    }
  }

  

  
  public boolean isEmpty() {
    return false;
  }

  static public class Empty extends Target {
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
      return visitor.visitTargetEmpty(this);
    }
  
    	
  }
  public boolean isLabeled() {
    return false;
  }

  static public class Labeled extends Target {
    // Production: sig("Labeled",[arg("org.rascalmpl.ast.Name","name")])
  
    
    private final org.rascalmpl.ast.Name name;
  
    public Labeled(IConstructor node , org.rascalmpl.ast.Name name) {
      super(node);
      
      this.name = name;
    }
  
    @Override
    public boolean isLabeled() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitTargetLabeled(this);
    }
  
    
    @Override
    public org.rascalmpl.ast.Name getName() {
      return this.name;
    }
  
    @Override
    public boolean hasName() {
      return true;
    }	
  }
}