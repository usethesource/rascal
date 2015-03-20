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

public abstract class FunctionModifier extends AbstractAST {
  public FunctionModifier(IConstructor node) {
    super();
  }

  

  

  
  public boolean isDefault() {
    return false;
  }

  static public class Default extends FunctionModifier {
    // Production: sig("Default",[])
  
    
  
    public Default(IConstructor node ) {
      super(node);
      
    }
  
    @Override
    public boolean isDefault() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitFunctionModifierDefault(this);
    }
  
    @Override
    public boolean equals(Object o) {
      if (!(o instanceof Default)) {
        return false;
      }        
      Default tmp = (Default) o;
      return true ; 
    }
   
    @Override
    public int hashCode() {
      return 307 ; 
    } 
  
    	
  }
  public boolean isJava() {
    return false;
  }

  static public class Java extends FunctionModifier {
    // Production: sig("Java",[])
  
    
  
    public Java(IConstructor node ) {
      super(node);
      
    }
  
    @Override
    public boolean isJava() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitFunctionModifierJava(this);
    }
  
    @Override
    public boolean equals(Object o) {
      if (!(o instanceof Java)) {
        return false;
      }        
      Java tmp = (Java) o;
      return true ; 
    }
   
    @Override
    public int hashCode() {
      return 131 ; 
    } 
  
    	
  }
  public boolean isTest() {
    return false;
  }

  static public class Test extends FunctionModifier {
    // Production: sig("Test",[])
  
    
  
    public Test(IConstructor node ) {
      super(node);
      
    }
  
    @Override
    public boolean isTest() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitFunctionModifierTest(this);
    }
  
    @Override
    public boolean equals(Object o) {
      if (!(o instanceof Test)) {
        return false;
      }        
      Test tmp = (Test) o;
      return true ; 
    }
   
    @Override
    public int hashCode() {
      return 467 ; 
    } 
  
    	
  }
}