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

public abstract class Module extends AbstractAST {
  public Module(IConstructor node) {
    super();
  }

  
  public boolean hasBody() {
    return false;
  }

  public org.rascalmpl.ast.Body getBody() {
    throw new UnsupportedOperationException();
  }
  public boolean hasHeader() {
    return false;
  }

  public org.rascalmpl.ast.Header getHeader() {
    throw new UnsupportedOperationException();
  }

  

  
  public boolean isDefault() {
    return false;
  }

  static public class Default extends Module {
    // Production: sig("Default",[arg("org.rascalmpl.ast.Header","header"),arg("org.rascalmpl.ast.Body","body")])
  
    
    private final org.rascalmpl.ast.Header header;
    private final org.rascalmpl.ast.Body body;
  
    public Default(IConstructor node , org.rascalmpl.ast.Header header,  org.rascalmpl.ast.Body body) {
      super(node);
      
      this.header = header;
      this.body = body;
    }
  
    @Override
    public boolean isDefault() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitModuleDefault(this);
    }
  
    @Override
    public boolean equals(Object o) {
      if (!(o instanceof Default)) {
        return false;
      }        
      Default tmp = (Default) o;
      return true && tmp.header.equals(this.header) && tmp.body.equals(this.body) ; 
    }
   
    @Override
    public int hashCode() {
      return 419 + 739 * header.hashCode() + 53 * body.hashCode() ; 
    } 
  
    
    @Override
    public org.rascalmpl.ast.Header getHeader() {
      return this.header;
    }
  
    @Override
    public boolean hasHeader() {
      return true;
    }
    @Override
    public org.rascalmpl.ast.Body getBody() {
      return this.body;
    }
  
    @Override
    public boolean hasBody() {
      return true;
    }	
  }
}