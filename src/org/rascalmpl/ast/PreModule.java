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


import org.eclipse.imp.pdb.facts.IConstructor;
import org.rascalmpl.interpreter.asserts.Ambiguous;
import org.eclipse.imp.pdb.facts.IValue;
import org.rascalmpl.interpreter.Evaluator;
import org.rascalmpl.interpreter.env.Environment;
import org.rascalmpl.interpreter.result.Result;

public abstract class PreModule extends AbstractAST {
  public PreModule(IConstructor node) {
    super(node);
  }

  
  public boolean hasRest() {
    return false;
  }

  public org.rascalmpl.ast.Rest getRest() {
    throw new UnsupportedOperationException();
  }
  public boolean hasHeader() {
    return false;
  }

  public org.rascalmpl.ast.Header getHeader() {
    throw new UnsupportedOperationException();
  }

  static public class Ambiguity extends PreModule {
    private final java.util.List<org.rascalmpl.ast.PreModule> alternatives;
  
    public Ambiguity(IConstructor node, java.util.List<org.rascalmpl.ast.PreModule> alternatives) {
      super(node);
      this.alternatives = java.util.Collections.unmodifiableList(alternatives);
    }
    
    @Override
    public Result<IValue> interpret(Evaluator __eval) {
      throw new Ambiguous(this.getTree());
    }
      
    @Override
    public org.eclipse.imp.pdb.facts.type.Type typeOf(Environment env) {
      throw new Ambiguous(this.getTree());
    }
    
    public java.util.List<org.rascalmpl.ast.PreModule> getAlternatives() {
      return alternatives;
    }
    
    public <T> T accept(IASTVisitor<T> v) {
    	return v.visitPreModuleAmbiguity(this);
    }
  }

  

  
  public boolean isDefault() {
    return false;
  }

  static public class Default extends PreModule {
    // Production: sig("Default",[arg("org.rascalmpl.ast.Header","header"),arg("org.rascalmpl.ast.Rest","rest")])
  
    
    private final org.rascalmpl.ast.Header header;
    private final org.rascalmpl.ast.Rest rest;
  
    public Default(IConstructor node , org.rascalmpl.ast.Header header,  org.rascalmpl.ast.Rest rest) {
      super(node);
      
      this.header = header;
      this.rest = rest;
    }
  
    @Override
    public boolean isDefault() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitPreModuleDefault(this);
    }
  
    
    @Override
    public org.rascalmpl.ast.Header getHeader() {
      return this.header;
    }
  
    @Override
    public boolean hasHeader() {
      return true;
    }
    @Override
    public org.rascalmpl.ast.Rest getRest() {
      return this.rest;
    }
  
    @Override
    public boolean hasRest() {
      return true;
    }	
  }
}