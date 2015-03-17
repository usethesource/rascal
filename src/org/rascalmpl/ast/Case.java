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

public abstract class Case extends AbstractAST {
  public Case(IConstructor node) {
    super();
  }

  
  public boolean hasPatternWithAction() {
    return false;
  }

  public org.rascalmpl.ast.PatternWithAction getPatternWithAction() {
    throw new UnsupportedOperationException();
  }
  public boolean hasStatement() {
    return false;
  }

  public org.rascalmpl.ast.Statement getStatement() {
    throw new UnsupportedOperationException();
  }

  

  
  public boolean isDefault() {
    return false;
  }

  static public class Default extends Case {
    // Production: sig("Default",[arg("org.rascalmpl.ast.Statement","statement")])
  
    
    private final org.rascalmpl.ast.Statement statement;
  
    public Default(IConstructor node , org.rascalmpl.ast.Statement statement) {
      super(node);
      
      this.statement = statement;
    }
  
    @Override
    public boolean isDefault() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitCaseDefault(this);
    }
  
    @Override
    public boolean equals(Object o) {
      if (!(o instanceof Default)) {
        return false;
      }        
      Default tmp = (Default) o;
      return true && tmp.statement.equals(this.statement) ; 
    }
   
    @Override
    public int hashCode() {
      return 907 + 613 * statement.hashCode() ; 
    } 
  
    
    @Override
    public org.rascalmpl.ast.Statement getStatement() {
      return this.statement;
    }
  
    @Override
    public boolean hasStatement() {
      return true;
    }	
  }
  public boolean isPatternWithAction() {
    return false;
  }

  static public class PatternWithAction extends Case {
    // Production: sig("PatternWithAction",[arg("org.rascalmpl.ast.PatternWithAction","patternWithAction")])
  
    
    private final org.rascalmpl.ast.PatternWithAction patternWithAction;
  
    public PatternWithAction(IConstructor node , org.rascalmpl.ast.PatternWithAction patternWithAction) {
      super(node);
      
      this.patternWithAction = patternWithAction;
    }
  
    @Override
    public boolean isPatternWithAction() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitCasePatternWithAction(this);
    }
  
    @Override
    public boolean equals(Object o) {
      if (!(o instanceof PatternWithAction)) {
        return false;
      }        
      PatternWithAction tmp = (PatternWithAction) o;
      return true && tmp.patternWithAction.equals(this.patternWithAction) ; 
    }
   
    @Override
    public int hashCode() {
      return 11 + 17 * patternWithAction.hashCode() ; 
    } 
  
    
    @Override
    public org.rascalmpl.ast.PatternWithAction getPatternWithAction() {
      return this.patternWithAction;
    }
  
    @Override
    public boolean hasPatternWithAction() {
      return true;
    }	
  }
}