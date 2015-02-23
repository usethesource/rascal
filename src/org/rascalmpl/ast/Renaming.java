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

public abstract class Renaming extends AbstractAST {
  public Renaming(IConstructor node) {
    super();
  }

  
  public boolean hasFrom() {
    return false;
  }

  public org.rascalmpl.ast.Name getFrom() {
    throw new UnsupportedOperationException();
  }
  public boolean hasTo() {
    return false;
  }

  public org.rascalmpl.ast.Name getTo() {
    throw new UnsupportedOperationException();
  }

  

  
  public boolean isDefault() {
    return false;
  }

  static public class Default extends Renaming {
    // Production: sig("Default",[arg("org.rascalmpl.ast.Name","from"),arg("org.rascalmpl.ast.Name","to")])
  
    
    private final org.rascalmpl.ast.Name from;
    private final org.rascalmpl.ast.Name to;
  
    public Default(IConstructor node , org.rascalmpl.ast.Name from,  org.rascalmpl.ast.Name to) {
      super(node);
      
      this.from = from;
      this.to = to;
    }
  
    @Override
    public boolean isDefault() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitRenamingDefault(this);
    }
  
    @Override
    public boolean equals(Object o) {
      if (!(o instanceof Default)) {
        return false;
      }        
      Default tmp = (Default) o;
      return true && tmp.from.equals(this.from) && tmp.to.equals(this.to) ; 
    }
   
    @Override
    public int hashCode() {
      return 607 + 227 * from.hashCode() + 733 * to.hashCode() ; 
    } 
  
    
    @Override
    public org.rascalmpl.ast.Name getFrom() {
      return this.from;
    }
  
    @Override
    public boolean hasFrom() {
      return true;
    }
    @Override
    public org.rascalmpl.ast.Name getTo() {
      return this.to;
    }
  
    @Override
    public boolean hasTo() {
      return true;
    }	
  }
}