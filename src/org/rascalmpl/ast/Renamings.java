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

public abstract class Renamings extends AbstractAST {
  public Renamings(IConstructor node) {
    super();
  }

  
  public boolean hasRenamings() {
    return false;
  }

  public java.util.List<org.rascalmpl.ast.Renaming> getRenamings() {
    throw new UnsupportedOperationException();
  }

  

  
  public boolean isDefault() {
    return false;
  }

  static public class Default extends Renamings {
    // Production: sig("Default",[arg("java.util.List\<org.rascalmpl.ast.Renaming\>","renamings")])
  
    
    private final java.util.List<org.rascalmpl.ast.Renaming> renamings;
  
    public Default(IConstructor node , java.util.List<org.rascalmpl.ast.Renaming> renamings) {
      super(node);
      
      this.renamings = renamings;
    }
  
    @Override
    public boolean isDefault() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitRenamingsDefault(this);
    }
  
    @Override
    public boolean equals(Object o) {
      if (!(o instanceof Default)) {
        return false;
      }        
      Default tmp = (Default) o;
      return true && tmp.renamings.equals(this.renamings) ; 
    }
   
    @Override
    public int hashCode() {
      return 607 + 227 * renamings.hashCode() ; 
    } 
  
    
    @Override
    public java.util.List<org.rascalmpl.ast.Renaming> getRenamings() {
      return this.renamings;
    }
  
    @Override
    public boolean hasRenamings() {
      return true;
    }	
  }
}