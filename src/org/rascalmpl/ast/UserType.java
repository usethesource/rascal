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

public abstract class UserType extends AbstractAST {
  public UserType(IConstructor node) {
    super();
  }

  
  public boolean hasParameters() {
    return false;
  }

  public java.util.List<org.rascalmpl.ast.Type> getParameters() {
    throw new UnsupportedOperationException();
  }
  public boolean hasName() {
    return false;
  }

  public org.rascalmpl.ast.QualifiedName getName() {
    throw new UnsupportedOperationException();
  }

  static public class Ambiguity extends UserType {
    private final java.util.List<org.rascalmpl.ast.UserType> alternatives;
    private final IConstructor node;
           
    public Ambiguity(IConstructor node, java.util.List<org.rascalmpl.ast.UserType> alternatives) {
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
    
    public java.util.List<org.rascalmpl.ast.UserType> getAlternatives() {
      return alternatives;
    }
    
    public <T> T accept(IASTVisitor<T> v) {
    	return v.visitUserTypeAmbiguity(this);
    }
  }

  

  
  public boolean isName() {
    return false;
  }

  static public class Name extends UserType {
    // Production: sig("Name",[arg("org.rascalmpl.ast.QualifiedName","name")])
  
    
    private final org.rascalmpl.ast.QualifiedName name;
  
    public Name(IConstructor node , org.rascalmpl.ast.QualifiedName name) {
      super(node);
      
      this.name = name;
    }
  
    @Override
    public boolean isName() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitUserTypeName(this);
    }
  
    
    @Override
    public org.rascalmpl.ast.QualifiedName getName() {
      return this.name;
    }
  
    @Override
    public boolean hasName() {
      return true;
    }	
  }
  public boolean isParametric() {
    return false;
  }

  static public class Parametric extends UserType {
    // Production: sig("Parametric",[arg("org.rascalmpl.ast.QualifiedName","name"),arg("java.util.List\<org.rascalmpl.ast.Type\>","parameters")])
  
    
    private final org.rascalmpl.ast.QualifiedName name;
    private final java.util.List<org.rascalmpl.ast.Type> parameters;
  
    public Parametric(IConstructor node , org.rascalmpl.ast.QualifiedName name,  java.util.List<org.rascalmpl.ast.Type> parameters) {
      super(node);
      
      this.name = name;
      this.parameters = parameters;
    }
  
    @Override
    public boolean isParametric() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitUserTypeParametric(this);
    }
  
    
    @Override
    public org.rascalmpl.ast.QualifiedName getName() {
      return this.name;
    }
  
    @Override
    public boolean hasName() {
      return true;
    }
    @Override
    public java.util.List<org.rascalmpl.ast.Type> getParameters() {
      return this.parameters;
    }
  
    @Override
    public boolean hasParameters() {
      return true;
    }	
  }
}