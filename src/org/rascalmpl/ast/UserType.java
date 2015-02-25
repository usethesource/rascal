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

public abstract class UserType extends AbstractAST {
  public UserType(IConstructor node) {
    super();
  }

  
  public boolean hasParameters() {
    return false;
  }

  public java.util.List<org.rascalmpl.ast.Type> getParameters() {
    throw new UnsupportedOperationException();
  }
  public boolean hasName() {
    return false;
  }

  public org.rascalmpl.ast.QualifiedName getName() {
    throw new UnsupportedOperationException();
  }

  

  
  public boolean isName() {
    return false;
  }

  static public class Name extends UserType {
    // Production: sig("Name",[arg("org.rascalmpl.ast.QualifiedName","name")])
  
    
    private final org.rascalmpl.ast.QualifiedName name;
  
    public Name(IConstructor node , org.rascalmpl.ast.QualifiedName name) {
      super(node);
      
      this.name = name;
    }
  
    @Override
    public boolean isName() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitUserTypeName(this);
    }
  
    @Override
    public boolean equals(Object o) {
      if (!(o instanceof Name)) {
        return false;
      }        
      Name tmp = (Name) o;
      return true && tmp.name.equals(this.name) ; 
    }
   
    @Override
    public int hashCode() {
      return 13 + 353 * name.hashCode() ; 
    } 
  
    
    @Override
    public org.rascalmpl.ast.QualifiedName getName() {
      return this.name;
    }
  
    @Override
    public boolean hasName() {
      return true;
    }	
  }
  public boolean isParametric() {
    return false;
  }

  static public class Parametric extends UserType {
    // Production: sig("Parametric",[arg("org.rascalmpl.ast.QualifiedName","name"),arg("java.util.List\<org.rascalmpl.ast.Type\>","parameters")])
  
    
    private final org.rascalmpl.ast.QualifiedName name;
    private final java.util.List<org.rascalmpl.ast.Type> parameters;
  
    public Parametric(IConstructor node , org.rascalmpl.ast.QualifiedName name,  java.util.List<org.rascalmpl.ast.Type> parameters) {
      super(node);
      
      this.name = name;
      this.parameters = parameters;
    }
  
    @Override
    public boolean isParametric() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitUserTypeParametric(this);
    }
  
    @Override
    public boolean equals(Object o) {
      if (!(o instanceof Parametric)) {
        return false;
      }        
      Parametric tmp = (Parametric) o;
      return true && tmp.name.equals(this.name) && tmp.parameters.equals(this.parameters) ; 
    }
   
    @Override
    public int hashCode() {
      return 179 + 727 * name.hashCode() + 17 * parameters.hashCode() ; 
    } 
  
    
    @Override
    public org.rascalmpl.ast.QualifiedName getName() {
      return this.name;
    }
  
    @Override
    public boolean hasName() {
      return true;
    }
    @Override
    public java.util.List<org.rascalmpl.ast.Type> getParameters() {
      return this.parameters;
    }
  
    @Override
    public boolean hasParameters() {
      return true;
    }	
  }
}