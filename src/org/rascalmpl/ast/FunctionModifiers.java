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

public abstract class FunctionModifiers extends AbstractAST {
  public FunctionModifiers(IConstructor node) {
    super();
  }

  
  public boolean hasModifiers() {
    return false;
  }

  public java.util.List<org.rascalmpl.ast.FunctionModifier> getModifiers() {
    throw new UnsupportedOperationException();
  }

  

  
  public boolean isModifierlist() {
    return false;
  }

  static public class Modifierlist extends FunctionModifiers {
    // Production: sig("Modifierlist",[arg("java.util.List\<org.rascalmpl.ast.FunctionModifier\>","modifiers")])
  
    
    private final java.util.List<org.rascalmpl.ast.FunctionModifier> modifiers;
  
    public Modifierlist(IConstructor node , java.util.List<org.rascalmpl.ast.FunctionModifier> modifiers) {
      super(node);
      
      this.modifiers = modifiers;
    }
  
    @Override
    public boolean isModifierlist() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitFunctionModifiersModifierlist(this);
    }
  
    @Override
    public boolean equals(Object o) {
      if (!(o instanceof Modifierlist)) {
        return false;
      }        
      Modifierlist tmp = (Modifierlist) o;
      return true && tmp.modifiers.equals(this.modifiers) ; 
    }
   
    @Override
    public int hashCode() {
      return 547 + 631 * modifiers.hashCode() ; 
    } 
  
    
    @Override
    public java.util.List<org.rascalmpl.ast.FunctionModifier> getModifiers() {
      return this.modifiers;
    }
  
    @Override
    public boolean hasModifiers() {
      return true;
    }	
  }
}