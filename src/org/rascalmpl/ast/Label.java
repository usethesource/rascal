/*******************************************************************************
 * Copyright (c) 2009-2014 CWI
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

public abstract class Label extends AbstractAST {
  public Label(IConstructor node) {
    super();
  }

  
  public boolean hasName() {
    return false;
  }

  public org.rascalmpl.ast.Name getName() {
    throw new UnsupportedOperationException();
  }

  

  
  public boolean isDefault() {
    return false;
  }

  static public class Default extends Label {
    // Production: sig("Default",[arg("org.rascalmpl.ast.Name","name")])
  
    
    private final org.rascalmpl.ast.Name name;
  
    public Default(IConstructor node , org.rascalmpl.ast.Name name) {
      super(node);
      
      this.name = name;
    }
  
    @Override
    public boolean isDefault() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitLabelDefault(this);
    }
  
    @Override
    public boolean equals(Object o) {
      if (!(o instanceof Default)) {
        return false;
      }        
      Default tmp = (Default) o;
      return true && tmp.name.equals(this.name) ; 
    }
   
    @Override
    public int hashCode() {
      return 397 + 167 * name.hashCode() ; 
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
  public boolean isEmpty() {
    return false;
  }

  static public class Empty extends Label {
    // Production: sig("Empty",[])
  
    
  
    public Empty(IConstructor node ) {
      super(node);
      
    }
  
    @Override
    public boolean isEmpty() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitLabelEmpty(this);
    }
  
    @Override
    public boolean equals(Object o) {
      if (!(o instanceof Empty)) {
        return false;
      }        
      Empty tmp = (Empty) o;
      return true ; 
    }
   
    @Override
    public int hashCode() {
      return 853 ; 
    } 
  
    	
  }
}