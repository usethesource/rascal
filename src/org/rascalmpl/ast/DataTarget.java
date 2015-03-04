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

public abstract class DataTarget extends AbstractAST {
  public DataTarget(IConstructor node) {
    super();
  }

  
  public boolean hasLabel() {
    return false;
  }

  public org.rascalmpl.ast.Name getLabel() {
    throw new UnsupportedOperationException();
  }

  

  
  public boolean isEmpty() {
    return false;
  }

  static public class Empty extends DataTarget {
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
      return visitor.visitDataTargetEmpty(this);
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
      return 821 ; 
    } 
  
    	
  }
  public boolean isLabeled() {
    return false;
  }

  static public class Labeled extends DataTarget {
    // Production: sig("Labeled",[arg("org.rascalmpl.ast.Name","label")])
  
    
    private final org.rascalmpl.ast.Name label;
  
    public Labeled(IConstructor node , org.rascalmpl.ast.Name label) {
      super(node);
      
      this.label = label;
    }
  
    @Override
    public boolean isLabeled() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitDataTargetLabeled(this);
    }
  
    @Override
    public boolean equals(Object o) {
      if (!(o instanceof Labeled)) {
        return false;
      }        
      Labeled tmp = (Labeled) o;
      return true && tmp.label.equals(this.label) ; 
    }
   
    @Override
    public int hashCode() {
      return 271 + 29 * label.hashCode() ; 
    } 
  
    
    @Override
    public org.rascalmpl.ast.Name getLabel() {
      return this.label;
    }
  
    @Override
    public boolean hasLabel() {
      return true;
    }	
  }
}