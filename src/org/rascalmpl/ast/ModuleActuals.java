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

public abstract class ModuleActuals extends AbstractAST {
  public ModuleActuals(IConstructor node) {
    super();
  }

  
  public boolean hasTypes() {
    return false;
  }

  public java.util.List<org.rascalmpl.ast.Type> getTypes() {
    throw new UnsupportedOperationException();
  }

  static public class Ambiguity extends ModuleActuals {
    private final java.util.List<org.rascalmpl.ast.ModuleActuals> alternatives;
    private final IConstructor node;
           
    public Ambiguity(IConstructor node, java.util.List<org.rascalmpl.ast.ModuleActuals> alternatives) {
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
    
    public java.util.List<org.rascalmpl.ast.ModuleActuals> getAlternatives() {
      return alternatives;
    }
    
    public <T> T accept(IASTVisitor<T> v) {
    	return v.visitModuleActualsAmbiguity(this);
    }
  }

  

  
  public boolean isDefault() {
    return false;
  }

  static public class Default extends ModuleActuals {
    // Production: sig("Default",[arg("java.util.List\<org.rascalmpl.ast.Type\>","types")])
  
    
    private final java.util.List<org.rascalmpl.ast.Type> types;
  
    public Default(IConstructor node , java.util.List<org.rascalmpl.ast.Type> types) {
      super(node);
      
      this.types = types;
    }
  
    @Override
    public boolean isDefault() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitModuleActualsDefault(this);
    }
  
    
    @Override
    public java.util.List<org.rascalmpl.ast.Type> getTypes() {
      return this.types;
    }
  
    @Override
    public boolean hasTypes() {
      return true;
    }	
  }
}