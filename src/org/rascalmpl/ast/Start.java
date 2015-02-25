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

public abstract class Start extends AbstractAST {
  public Start(IConstructor node) {
    super();
  }

  

  

  
  public boolean isAbsent() {
    return false;
  }

  static public class Absent extends Start {
    // Production: sig("Absent",[])
  
    
  
    public Absent(IConstructor node ) {
      super(node);
      
    }
  
    @Override
    public boolean isAbsent() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitStartAbsent(this);
    }
  
    @Override
    public boolean equals(Object o) {
      if (!(o instanceof Absent)) {
        return false;
      }        
      Absent tmp = (Absent) o;
      return true ; 
    }
   
    @Override
    public int hashCode() {
      return 691 ; 
    } 
  
    	
  }
  public boolean isPresent() {
    return false;
  }

  static public class Present extends Start {
    // Production: sig("Present",[])
  
    
  
    public Present(IConstructor node ) {
      super(node);
      
    }
  
    @Override
    public boolean isPresent() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitStartPresent(this);
    }
  
    @Override
    public boolean equals(Object o) {
      if (!(o instanceof Present)) {
        return false;
      }        
      Present tmp = (Present) o;
      return true ; 
    }
   
    @Override
    public int hashCode() {
      return 79 ; 
    } 
  
    	
  }
}