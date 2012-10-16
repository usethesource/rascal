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

public abstract class FunctionModifiers extends AbstractAST {
  public FunctionModifiers(IConstructor node) {
    super();
  }

  
  public boolean hasModifiers() {
    return false;
  }

  public java.util.List<org.rascalmpl.ast.FunctionModifier> getModifiers() {
    throw new UnsupportedOperationException();
  }

  static public class Ambiguity extends FunctionModifiers {
    private final java.util.List<org.rascalmpl.ast.FunctionModifiers> alternatives;
    private final IConstructor node;
           
    public Ambiguity(IConstructor node, java.util.List<org.rascalmpl.ast.FunctionModifiers> alternatives) {
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
    
    public java.util.List<org.rascalmpl.ast.FunctionModifiers> getAlternatives() {
      return alternatives;
    }
    
    public <T> T accept(IASTVisitor<T> v) {
    	return v.visitFunctionModifiersAmbiguity(this);
    }
  }

  

  
  public boolean isList() {
    return false;
  }

  static public class List extends FunctionModifiers {
    // Production: sig("List",[arg("java.util.List\<org.rascalmpl.ast.FunctionModifier\>","modifiers")])
  
    
    private final java.util.List<org.rascalmpl.ast.FunctionModifier> modifiers;
  
    public List(IConstructor node , java.util.List<org.rascalmpl.ast.FunctionModifier> modifiers) {
      super(node);
      
      this.modifiers = modifiers;
    }
  
    @Override
    public boolean isList() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitFunctionModifiersList(this);
    }
  
    
    @Override
    public java.util.List<org.rascalmpl.ast.FunctionModifier> getModifiers() {
      return this.modifiers;
    }
  
    @Override
    public boolean hasModifiers() {
      return true;
    }	
  }
}