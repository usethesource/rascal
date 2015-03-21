/*******************************************************************************
 * Copyright (c) 2009-2015 CWI
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

public abstract class Parameters extends AbstractAST {
  public Parameters(IConstructor node) {
    super();
  }

  
  public boolean hasFormals() {
    return false;
  }

  public org.rascalmpl.ast.Formals getFormals() {
    throw new UnsupportedOperationException();
  }
  public boolean hasKeywordFormals() {
    return false;
  }

  public org.rascalmpl.ast.KeywordFormals getKeywordFormals() {
    throw new UnsupportedOperationException();
  }

  

  
  public boolean isDefault() {
    return false;
  }

  static public class Default extends Parameters {
    // Production: sig("Default",[arg("org.rascalmpl.ast.Formals","formals"),arg("org.rascalmpl.ast.KeywordFormals","keywordFormals")])
  
    
    private final org.rascalmpl.ast.Formals formals;
    private final org.rascalmpl.ast.KeywordFormals keywordFormals;
  
    public Default(IConstructor node , org.rascalmpl.ast.Formals formals,  org.rascalmpl.ast.KeywordFormals keywordFormals) {
      super(node);
      
      this.formals = formals;
      this.keywordFormals = keywordFormals;
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
    public boolean equals(Object o) {
      if (!(o instanceof Default)) {
        return false;
      }        
      Default tmp = (Default) o;
      return true && tmp.formals.equals(this.formals) && tmp.keywordFormals.equals(this.keywordFormals) ; 
    }
   
    @Override
    public int hashCode() {
      return 359 + 293 * formals.hashCode() + 523 * keywordFormals.hashCode() ; 
    } 
  
    
    @Override
    public org.rascalmpl.ast.Formals getFormals() {
      return this.formals;
    }
  
    @Override
    public boolean hasFormals() {
      return true;
    }
    @Override
    public org.rascalmpl.ast.KeywordFormals getKeywordFormals() {
      return this.keywordFormals;
    }
  
    @Override
    public boolean hasKeywordFormals() {
      return true;
    }	
  }
  public boolean isVarArgs() {
    return false;
  }

  static public class VarArgs extends Parameters {
    // Production: sig("VarArgs",[arg("org.rascalmpl.ast.Formals","formals"),arg("org.rascalmpl.ast.KeywordFormals","keywordFormals")])
  
    
    private final org.rascalmpl.ast.Formals formals;
    private final org.rascalmpl.ast.KeywordFormals keywordFormals;
  
    public VarArgs(IConstructor node , org.rascalmpl.ast.Formals formals,  org.rascalmpl.ast.KeywordFormals keywordFormals) {
      super(node);
      
      this.formals = formals;
      this.keywordFormals = keywordFormals;
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
    public boolean equals(Object o) {
      if (!(o instanceof VarArgs)) {
        return false;
      }        
      VarArgs tmp = (VarArgs) o;
      return true && tmp.formals.equals(this.formals) && tmp.keywordFormals.equals(this.keywordFormals) ; 
    }
   
    @Override
    public int hashCode() {
      return 433 + 211 * formals.hashCode() + 653 * keywordFormals.hashCode() ; 
    } 
  
    
    @Override
    public org.rascalmpl.ast.Formals getFormals() {
      return this.formals;
    }
  
    @Override
    public boolean hasFormals() {
      return true;
    }
    @Override
    public org.rascalmpl.ast.KeywordFormals getKeywordFormals() {
      return this.keywordFormals;
    }
  
    @Override
    public boolean hasKeywordFormals() {
      return true;
    }	
  }
}