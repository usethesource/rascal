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

public abstract class Signature extends AbstractAST {
  public Signature(IConstructor node) {
    super();
  }

  
  public boolean hasExceptions() {
    return false;
  }

  public java.util.List<org.rascalmpl.ast.Type> getExceptions() {
    throw new UnsupportedOperationException();
  }
  public boolean hasModifiers() {
    return false;
  }

  public org.rascalmpl.ast.FunctionModifiers getModifiers() {
    throw new UnsupportedOperationException();
  }
  public boolean hasName() {
    return false;
  }

  public org.rascalmpl.ast.Name getName() {
    throw new UnsupportedOperationException();
  }
  public boolean hasParameters() {
    return false;
  }

  public org.rascalmpl.ast.Parameters getParameters() {
    throw new UnsupportedOperationException();
  }
  public boolean hasType() {
    return false;
  }

  public org.rascalmpl.ast.Type getType() {
    throw new UnsupportedOperationException();
  }

  

  
  public boolean isNoThrows() {
    return false;
  }

  static public class NoThrows extends Signature {
    // Production: sig("NoThrows",[arg("org.rascalmpl.ast.FunctionModifiers","modifiers"),arg("org.rascalmpl.ast.Type","type"),arg("org.rascalmpl.ast.Name","name"),arg("org.rascalmpl.ast.Parameters","parameters")])
  
    
    private final org.rascalmpl.ast.FunctionModifiers modifiers;
    private final org.rascalmpl.ast.Type type;
    private final org.rascalmpl.ast.Name name;
    private final org.rascalmpl.ast.Parameters parameters;
  
    public NoThrows(IConstructor node , org.rascalmpl.ast.FunctionModifiers modifiers,  org.rascalmpl.ast.Type type,  org.rascalmpl.ast.Name name,  org.rascalmpl.ast.Parameters parameters) {
      super(node);
      
      this.modifiers = modifiers;
      this.type = type;
      this.name = name;
      this.parameters = parameters;
    }
  
    @Override
    public boolean isNoThrows() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitSignatureNoThrows(this);
    }
  
    @Override
    public boolean equals(Object o) {
      if (!(o instanceof NoThrows)) {
        return false;
      }        
      NoThrows tmp = (NoThrows) o;
      return true && tmp.modifiers.equals(this.modifiers) && tmp.type.equals(this.type) && tmp.name.equals(this.name) && tmp.parameters.equals(this.parameters) ; 
    }
   
    @Override
    public int hashCode() {
      return 3 + 271 * modifiers.hashCode() + 823 * type.hashCode() + 59 * name.hashCode() + 661 * parameters.hashCode() ; 
    } 
  
    
    @Override
    public org.rascalmpl.ast.FunctionModifiers getModifiers() {
      return this.modifiers;
    }
  
    @Override
    public boolean hasModifiers() {
      return true;
    }
    @Override
    public org.rascalmpl.ast.Type getType() {
      return this.type;
    }
  
    @Override
    public boolean hasType() {
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
    @Override
    public org.rascalmpl.ast.Parameters getParameters() {
      return this.parameters;
    }
  
    @Override
    public boolean hasParameters() {
      return true;
    }	
  }
  public boolean isWithThrows() {
    return false;
  }

  static public class WithThrows extends Signature {
    // Production: sig("WithThrows",[arg("org.rascalmpl.ast.FunctionModifiers","modifiers"),arg("org.rascalmpl.ast.Type","type"),arg("org.rascalmpl.ast.Name","name"),arg("org.rascalmpl.ast.Parameters","parameters"),arg("java.util.List\<org.rascalmpl.ast.Type\>","exceptions")])
  
    
    private final org.rascalmpl.ast.FunctionModifiers modifiers;
    private final org.rascalmpl.ast.Type type;
    private final org.rascalmpl.ast.Name name;
    private final org.rascalmpl.ast.Parameters parameters;
    private final java.util.List<org.rascalmpl.ast.Type> exceptions;
  
    public WithThrows(IConstructor node , org.rascalmpl.ast.FunctionModifiers modifiers,  org.rascalmpl.ast.Type type,  org.rascalmpl.ast.Name name,  org.rascalmpl.ast.Parameters parameters,  java.util.List<org.rascalmpl.ast.Type> exceptions) {
      super(node);
      
      this.modifiers = modifiers;
      this.type = type;
      this.name = name;
      this.parameters = parameters;
      this.exceptions = exceptions;
    }
  
    @Override
    public boolean isWithThrows() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitSignatureWithThrows(this);
    }
  
    @Override
    public boolean equals(Object o) {
      if (!(o instanceof WithThrows)) {
        return false;
      }        
      WithThrows tmp = (WithThrows) o;
      return true && tmp.modifiers.equals(this.modifiers) && tmp.type.equals(this.type) && tmp.name.equals(this.name) && tmp.parameters.equals(this.parameters) && tmp.exceptions.equals(this.exceptions) ; 
    }
   
    @Override
    public int hashCode() {
      return 281 + 983 * modifiers.hashCode() + 709 * type.hashCode() + 229 * name.hashCode() + 521 * parameters.hashCode() + 947 * exceptions.hashCode() ; 
    } 
  
    
    @Override
    public org.rascalmpl.ast.FunctionModifiers getModifiers() {
      return this.modifiers;
    }
  
    @Override
    public boolean hasModifiers() {
      return true;
    }
    @Override
    public org.rascalmpl.ast.Type getType() {
      return this.type;
    }
  
    @Override
    public boolean hasType() {
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
    @Override
    public org.rascalmpl.ast.Parameters getParameters() {
      return this.parameters;
    }
  
    @Override
    public boolean hasParameters() {
      return true;
    }
    @Override
    public java.util.List<org.rascalmpl.ast.Type> getExceptions() {
      return this.exceptions;
    }
  
    @Override
    public boolean hasExceptions() {
      return true;
    }	
  }
}