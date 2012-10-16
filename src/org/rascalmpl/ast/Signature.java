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

public abstract class Signature extends AbstractAST {
  public Signature(IConstructor node) {
    super();
  }

  
  public boolean hasExceptions() {
    return false;
  }

  public java.util.List<org.rascalmpl.ast.Type> getExceptions() {
    throw new UnsupportedOperationException();
  }
  public boolean hasModifiers() {
    return false;
  }

  public org.rascalmpl.ast.FunctionModifiers getModifiers() {
    throw new UnsupportedOperationException();
  }
  public boolean hasName() {
    return false;
  }

  public org.rascalmpl.ast.Name getName() {
    throw new UnsupportedOperationException();
  }
  public boolean hasParameters() {
    return false;
  }

  public org.rascalmpl.ast.Parameters getParameters() {
    throw new UnsupportedOperationException();
  }
  public boolean hasType() {
    return false;
  }

  public org.rascalmpl.ast.Type getType() {
    throw new UnsupportedOperationException();
  }

  static public class Ambiguity extends Signature {
    private final java.util.List<org.rascalmpl.ast.Signature> alternatives;
    private final IConstructor node;
           
    public Ambiguity(IConstructor node, java.util.List<org.rascalmpl.ast.Signature> alternatives) {
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
    
    public java.util.List<org.rascalmpl.ast.Signature> getAlternatives() {
      return alternatives;
    }
    
    public <T> T accept(IASTVisitor<T> v) {
    	return v.visitSignatureAmbiguity(this);
    }
  }

  

  
  public boolean isNoThrows() {
    return false;
  }

  static public class NoThrows extends Signature {
    // Production: sig("NoThrows",[arg("org.rascalmpl.ast.FunctionModifiers","modifiers"),arg("org.rascalmpl.ast.Type","type"),arg("org.rascalmpl.ast.Name","name"),arg("org.rascalmpl.ast.Parameters","parameters")])
  
    
    private final org.rascalmpl.ast.FunctionModifiers modifiers;
    private final org.rascalmpl.ast.Type type;
    private final org.rascalmpl.ast.Name name;
    private final org.rascalmpl.ast.Parameters parameters;
  
    public NoThrows(IConstructor node , org.rascalmpl.ast.FunctionModifiers modifiers,  org.rascalmpl.ast.Type type,  org.rascalmpl.ast.Name name,  org.rascalmpl.ast.Parameters parameters) {
      super(node);
      
      this.modifiers = modifiers;
      this.type = type;
      this.name = name;
      this.parameters = parameters;
    }
  
    @Override
    public boolean isNoThrows() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitSignatureNoThrows(this);
    }
  
    
    @Override
    public org.rascalmpl.ast.FunctionModifiers getModifiers() {
      return this.modifiers;
    }
  
    @Override
    public boolean hasModifiers() {
      return true;
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
    @Override
    public org.rascalmpl.ast.Parameters getParameters() {
      return this.parameters;
    }
  
    @Override
    public boolean hasParameters() {
      return true;
    }	
  }
  public boolean isWithThrows() {
    return false;
  }

  static public class WithThrows extends Signature {
    // Production: sig("WithThrows",[arg("org.rascalmpl.ast.FunctionModifiers","modifiers"),arg("org.rascalmpl.ast.Type","type"),arg("org.rascalmpl.ast.Name","name"),arg("org.rascalmpl.ast.Parameters","parameters"),arg("java.util.List\<org.rascalmpl.ast.Type\>","exceptions")])
  
    
    private final org.rascalmpl.ast.FunctionModifiers modifiers;
    private final org.rascalmpl.ast.Type type;
    private final org.rascalmpl.ast.Name name;
    private final org.rascalmpl.ast.Parameters parameters;
    private final java.util.List<org.rascalmpl.ast.Type> exceptions;
  
    public WithThrows(IConstructor node , org.rascalmpl.ast.FunctionModifiers modifiers,  org.rascalmpl.ast.Type type,  org.rascalmpl.ast.Name name,  org.rascalmpl.ast.Parameters parameters,  java.util.List<org.rascalmpl.ast.Type> exceptions) {
      super(node);
      
      this.modifiers = modifiers;
      this.type = type;
      this.name = name;
      this.parameters = parameters;
      this.exceptions = exceptions;
    }
  
    @Override
    public boolean isWithThrows() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitSignatureWithThrows(this);
    }
  
    
    @Override
    public org.rascalmpl.ast.FunctionModifiers getModifiers() {
      return this.modifiers;
    }
  
    @Override
    public boolean hasModifiers() {
      return true;
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
    @Override
    public org.rascalmpl.ast.Parameters getParameters() {
      return this.parameters;
    }
  
    @Override
    public boolean hasParameters() {
      return true;
    }
    @Override
    public java.util.List<org.rascalmpl.ast.Type> getExceptions() {
      return this.exceptions;
    }
  
    @Override
    public boolean hasExceptions() {
      return true;
    }	
  }
}