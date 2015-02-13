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

public abstract class LocalVariableDeclaration extends AbstractAST {
  public LocalVariableDeclaration(IConstructor node) {
    super();
  }

  
  public boolean hasDeclarator() {
    return false;
  }

  public org.rascalmpl.ast.Declarator getDeclarator() {
    throw new UnsupportedOperationException();
  }

  

  
  public boolean isDefault() {
    return false;
  }

  static public class Default extends LocalVariableDeclaration {
    // Production: sig("Default",[arg("org.rascalmpl.ast.Declarator","declarator")])
  
    
    private final org.rascalmpl.ast.Declarator declarator;
  
    public Default(IConstructor node , org.rascalmpl.ast.Declarator declarator) {
      super(node);
      
      this.declarator = declarator;
    }
  
    @Override
    public boolean isDefault() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitLocalVariableDeclarationDefault(this);
    }
  
    @Override
    public boolean equals(Object o) {
      if (!(o instanceof Default)) {
        return false;
      }        
      Default tmp = (Default) o;
      return true && tmp.declarator.equals(this.declarator) ; 
    }
   
    @Override
    public int hashCode() {
      return 179 + 17 * declarator.hashCode() ; 
    } 
  
    
    @Override
    public org.rascalmpl.ast.Declarator getDeclarator() {
      return this.declarator;
    }
  
    @Override
    public boolean hasDeclarator() {
      return true;
    }	
  }
  public boolean isDynamic() {
    return false;
  }

  static public class Dynamic extends LocalVariableDeclaration {
    // Production: sig("Dynamic",[arg("org.rascalmpl.ast.Declarator","declarator")])
  
    
    private final org.rascalmpl.ast.Declarator declarator;
  
    public Dynamic(IConstructor node , org.rascalmpl.ast.Declarator declarator) {
      super(node);
      
      this.declarator = declarator;
    }
  
    @Override
    public boolean isDynamic() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitLocalVariableDeclarationDynamic(this);
    }
  
    @Override
    public boolean equals(Object o) {
      if (!(o instanceof Dynamic)) {
        return false;
      }        
      Dynamic tmp = (Dynamic) o;
      return true && tmp.declarator.equals(this.declarator) ; 
    }
   
    @Override
    public int hashCode() {
      return 577 + 809 * declarator.hashCode() ; 
    } 
  
    
    @Override
    public org.rascalmpl.ast.Declarator getDeclarator() {
      return this.declarator;
    }
  
    @Override
    public boolean hasDeclarator() {
      return true;
    }	
  }
}