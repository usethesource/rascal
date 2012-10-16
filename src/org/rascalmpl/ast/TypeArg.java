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

public abstract class TypeArg extends AbstractAST {
  public TypeArg(IConstructor node) {
    super();
  }

  
  public boolean hasName() {
    return false;
  }

  public org.rascalmpl.ast.Name getName() {
    throw new UnsupportedOperationException();
  }
  public boolean hasType() {
    return false;
  }

  public org.rascalmpl.ast.Type getType() {
    throw new UnsupportedOperationException();
  }

  static public class Ambiguity extends TypeArg {
    private final java.util.List<org.rascalmpl.ast.TypeArg> alternatives;
    private final IConstructor node;
           
    public Ambiguity(IConstructor node, java.util.List<org.rascalmpl.ast.TypeArg> alternatives) {
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
    
    public java.util.List<org.rascalmpl.ast.TypeArg> getAlternatives() {
      return alternatives;
    }
    
    public <T> T accept(IASTVisitor<T> v) {
    	return v.visitTypeArgAmbiguity(this);
    }
  }

  

  
  public boolean isDefault() {
    return false;
  }

  static public class Default extends TypeArg {
    // Production: sig("Default",[arg("org.rascalmpl.ast.Type","type")])
  
    
    private final org.rascalmpl.ast.Type type;
  
    public Default(IConstructor node , org.rascalmpl.ast.Type type) {
      super(node);
      
      this.type = type;
    }
  
    @Override
    public boolean isDefault() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitTypeArgDefault(this);
    }
  
    
    @Override
    public org.rascalmpl.ast.Type getType() {
      return this.type;
    }
  
    @Override
    public boolean hasType() {
      return true;
    }	
  }
  public boolean isNamed() {
    return false;
  }

  static public class Named extends TypeArg {
    // Production: sig("Named",[arg("org.rascalmpl.ast.Type","type"),arg("org.rascalmpl.ast.Name","name")])
  
    
    private final org.rascalmpl.ast.Type type;
    private final org.rascalmpl.ast.Name name;
  
    public Named(IConstructor node , org.rascalmpl.ast.Type type,  org.rascalmpl.ast.Name name) {
      super(node);
      
      this.type = type;
      this.name = name;
    }
  
    @Override
    public boolean isNamed() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitTypeArgNamed(this);
    }
  
    
    @Override
    public org.rascalmpl.ast.Type getType() {
      return this.type;
    }
  
    @Override
    public boolean hasType() {
      return true;
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