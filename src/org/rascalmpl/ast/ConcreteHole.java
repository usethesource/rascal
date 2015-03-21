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

public abstract class ConcreteHole extends AbstractAST {
  public ConcreteHole(IConstructor node) {
    super();
  }

  
  public boolean hasName() {
    return false;
  }

  public org.rascalmpl.ast.Name getName() {
    throw new UnsupportedOperationException();
  }
  public boolean hasSymbol() {
    return false;
  }

  public org.rascalmpl.ast.Sym getSymbol() {
    throw new UnsupportedOperationException();
  }

  

  
  public boolean isOne() {
    return false;
  }

  static public class One extends ConcreteHole {
    // Production: sig("One",[arg("org.rascalmpl.ast.Sym","symbol"),arg("org.rascalmpl.ast.Name","name")])
  
    
    private final org.rascalmpl.ast.Sym symbol;
    private final org.rascalmpl.ast.Name name;
  
    public One(IConstructor node , org.rascalmpl.ast.Sym symbol,  org.rascalmpl.ast.Name name) {
      super(node);
      
      this.symbol = symbol;
      this.name = name;
    }
  
    @Override
    public boolean isOne() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitConcreteHoleOne(this);
    }
  
    @Override
    public boolean equals(Object o) {
      if (!(o instanceof One)) {
        return false;
      }        
      One tmp = (One) o;
      return true && tmp.symbol.equals(this.symbol) && tmp.name.equals(this.name) ; 
    }
   
    @Override
    public int hashCode() {
      return 479 + 163 * symbol.hashCode() + 257 * name.hashCode() ; 
    } 
  
    
    @Override
    public org.rascalmpl.ast.Sym getSymbol() {
      return this.symbol;
    }
  
    @Override
    public boolean hasSymbol() {
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