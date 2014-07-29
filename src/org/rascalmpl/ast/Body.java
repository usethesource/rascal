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

public abstract class Body extends AbstractAST {
  public Body(IConstructor node) {
    super();
  }

  
  public boolean hasToplevels() {
    return false;
  }

  public java.util.List<org.rascalmpl.ast.Toplevel> getToplevels() {
    throw new UnsupportedOperationException();
  }

  

  
  public boolean isToplevels() {
    return false;
  }

  static public class Toplevels extends Body {
    // Production: sig("Toplevels",[arg("java.util.List\<org.rascalmpl.ast.Toplevel\>","toplevels")])
  
    
    private final java.util.List<org.rascalmpl.ast.Toplevel> toplevels;
  
    public Toplevels(IConstructor node , java.util.List<org.rascalmpl.ast.Toplevel> toplevels) {
      super(node);
      
      this.toplevels = toplevels;
    }
  
    @Override
    public boolean isToplevels() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitBodyToplevels(this);
    }
  
    @Override
    public boolean equals(Object o) {
      if (!(o instanceof Toplevels)) {
        return false;
      }        
      Toplevels tmp = (Toplevels) o;
      return true && tmp.toplevels.equals(this.toplevels) ; 
    }
   
    @Override
    public int hashCode() {
      return 13331 + 67 * toplevels.hashCode() ; 
    } 
  
    
    @Override
    public java.util.List<org.rascalmpl.ast.Toplevel> getToplevels() {
      return this.toplevels;
    }
  
    @Override
    public boolean hasToplevels() {
      return true;
    }	
  }
}