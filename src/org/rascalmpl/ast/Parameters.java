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

public abstract class Parameters extends AbstractAST {
  public Parameters(IConstructor node) {
    super(node);
  }

  
  public boolean hasFormals() {
    return false;
  }

  public org.rascalmpl.ast.Formals getFormals() {
    throw new UnsupportedOperationException();
  }

  static public class Ambiguity extends Parameters {
    private final java.util.List<org.rascalmpl.ast.Parameters> alternatives;
  
    public Ambiguity(IConstructor node, java.util.List<org.rascalmpl.ast.Parameters> alternatives) {
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
    
    public java.util.List<org.rascalmpl.ast.Parameters> getAlternatives() {
      return alternatives;
    }
    
    public <T> T accept(IASTVisitor<T> v) {
    	return v.visitParametersAmbiguity(this);
    }
  }

  

  
  public boolean isVarArgs() {
    return false;
  }

  static public class VarArgs extends Parameters {
    // Production: sig("VarArgs",[arg("org.rascalmpl.ast.Formals","formals")])
  
    
    private final org.rascalmpl.ast.Formals formals;
  
    public VarArgs(IConstructor node , org.rascalmpl.ast.Formals formals) {
      super(node);
      
      this.formals = formals;
    }
  
    @Override
    public boolean isVarArgs() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitParametersVarArgs(this);
    }
  
    
    @Override
    public org.rascalmpl.ast.Formals getFormals() {
      return this.formals;
    }
  
    @Override
    public boolean hasFormals() {
      return true;
    }	
  }
  public boolean isDefault() {
    return false;
  }

  static public class Default extends Parameters {
    // Production: sig("Default",[arg("org.rascalmpl.ast.Formals","formals")])
  
    
    private final org.rascalmpl.ast.Formals formals;
  
    public Default(IConstructor node , org.rascalmpl.ast.Formals formals) {
      super(node);
      
      this.formals = formals;
    }
  
    @Override
    public boolean isDefault() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitParametersDefault(this);
    }
  
    
    @Override
    public org.rascalmpl.ast.Formals getFormals() {
      return this.formals;
    }
  
    @Override
    public boolean hasFormals() {
      return true;
    }	
  }
}