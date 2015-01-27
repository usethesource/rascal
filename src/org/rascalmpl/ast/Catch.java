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

public abstract class Catch extends AbstractAST {
  public Catch(IConstructor node) {
    super();
  }

  
  public boolean hasPattern() {
    return false;
  }

  public org.rascalmpl.ast.Expression getPattern() {
    throw new UnsupportedOperationException();
  }
  public boolean hasBody() {
    return false;
  }

  public org.rascalmpl.ast.Statement getBody() {
    throw new UnsupportedOperationException();
  }

  

  
  public boolean isBinding() {
    return false;
  }

  static public class Binding extends Catch {
    // Production: sig("Binding",[arg("org.rascalmpl.ast.Expression","pattern"),arg("org.rascalmpl.ast.Statement","body")])
  
    
    private final org.rascalmpl.ast.Expression pattern;
    private final org.rascalmpl.ast.Statement body;
  
    public Binding(IConstructor node , org.rascalmpl.ast.Expression pattern,  org.rascalmpl.ast.Statement body) {
      super(node);
      
      this.pattern = pattern;
      this.body = body;
    }
  
    @Override
    public boolean isBinding() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitCatchBinding(this);
    }
  
    @Override
    public boolean equals(Object o) {
      if (!(o instanceof Binding)) {
        return false;
      }        
      Binding tmp = (Binding) o;
      return true && tmp.pattern.equals(this.pattern) && tmp.body.equals(this.body) ; 
    }
   
    @Override
    public int hashCode() {
      return 113 + 859 * pattern.hashCode() + 461 * body.hashCode() ; 
    } 
  
    
    @Override
    public org.rascalmpl.ast.Expression getPattern() {
      return this.pattern;
    }
  
    @Override
    public boolean hasPattern() {
      return true;
    }
    @Override
    public org.rascalmpl.ast.Statement getBody() {
      return this.body;
    }
  
    @Override
    public boolean hasBody() {
      return true;
    }	
  }
  public boolean isDefault() {
    return false;
  }

  static public class Default extends Catch {
    // Production: sig("Default",[arg("org.rascalmpl.ast.Statement","body")])
  
    
    private final org.rascalmpl.ast.Statement body;
  
    public Default(IConstructor node , org.rascalmpl.ast.Statement body) {
      super(node);
      
      this.body = body;
    }
  
    @Override
    public boolean isDefault() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitCatchDefault(this);
    }
  
    @Override
    public boolean equals(Object o) {
      if (!(o instanceof Default)) {
        return false;
      }        
      Default tmp = (Default) o;
      return true && tmp.body.equals(this.body) ; 
    }
   
    @Override
    public int hashCode() {
      return 263 + 409 * body.hashCode() ; 
    } 
  
    
    @Override
    public org.rascalmpl.ast.Statement getBody() {
      return this.body;
    }
  
    @Override
    public boolean hasBody() {
      return true;
    }	
  }
}