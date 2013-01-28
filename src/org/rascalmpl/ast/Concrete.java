/*******************************************************************************
 * Copyright (c) 2009-2013 CWI
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

public abstract class Concrete extends AbstractAST {
  public Concrete(IConstructor node) {
    super();
  }

  
  public boolean hasParts() {
    return false;
  }

  public java.util.List<org.rascalmpl.ast.ConcretePart> getParts() {
    throw new UnsupportedOperationException();
  }

  public boolean hasSymbol() {
    return false;
  }

  public Sym getSymbol() {
    throw new UnsupportedOperationException();
  }

  static public class Ambiguity extends Concrete {
    private final java.util.List<org.rascalmpl.ast.Concrete> alternatives;
    private final IConstructor node;
           
    public Ambiguity(IConstructor node, java.util.List<org.rascalmpl.ast.Concrete> alternatives) {
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
    
    public java.util.List<org.rascalmpl.ast.Concrete> getAlternatives() {
      return alternatives;
    }
    
    public <T> T accept(IASTVisitor<T> v) {
    	return v.visitConcreteAmbiguity(this);
    }
  }

  

  
  public boolean isTyped() {
    return false;
  }

  static public class Typed extends Concrete {
    // Production: sig("Typed",[arg("java.util.List\<org.rascalmpl.ast.ConcretePart\>","parts")])
  
    
    private final java.util.List<org.rascalmpl.ast.ConcretePart> parts;
    private final Sym symbol;
  
    public Typed(IConstructor node , Sym symbol, java.util.List<org.rascalmpl.ast.ConcretePart> parts) {
      super(node);
      
      this.parts = parts;
      this.symbol = symbol;
    }
  
    @Override
    public boolean hasSymbol() {
      return true;
    }
    
    @Override
    public Sym getSymbol() {
      return symbol;
    }
    
    @Override
    public boolean isTyped() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitConcreteTyped(this);
    }
  
    
    @Override
    public java.util.List<org.rascalmpl.ast.ConcretePart> getParts() {
      return this.parts;
    }
  
    @Override
    public boolean hasParts() {
      return true;
    }	
  }
}