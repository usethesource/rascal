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

public abstract class Tags extends AbstractAST {
  public Tags(IConstructor node) {
    super();
  }

  
  public boolean hasTags() {
    return false;
  }

  public java.util.List<org.rascalmpl.ast.Tag> getTags() {
    throw new UnsupportedOperationException();
  }

  

  
  public boolean isDefault() {
    return false;
  }

  static public class Default extends Tags {
    // Production: sig("Default",[arg("java.util.List\<org.rascalmpl.ast.Tag\>","tags")])
  
    
    private final java.util.List<org.rascalmpl.ast.Tag> tags;
  
    public Default(IConstructor node , java.util.List<org.rascalmpl.ast.Tag> tags) {
      super(node);
      
      this.tags = tags;
    }
  
    @Override
    public boolean isDefault() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitTagsDefault(this);
    }
  
    @Override
    public boolean equals(Object o) {
      if (!(o instanceof Default)) {
        return false;
      }        
      Default tmp = (Default) o;
      return true && tmp.tags.equals(this.tags) ; 
    }
   
    @Override
    public int hashCode() {
      return 97 + 367 * tags.hashCode() ; 
    } 
  
    
    @Override
    public java.util.List<org.rascalmpl.ast.Tag> getTags() {
      return this.tags;
    }
  
    @Override
    public boolean hasTags() {
      return true;
    }	
  }
}