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

public abstract class Visibility extends AbstractAST {
  public Visibility(IConstructor node) {
    super();
  }

  

  

  
  public boolean isDefault() {
    return false;
  }

  static public class Default extends Visibility {
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
      return visitor.visitVisibilityDefault(this);
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
      return 317 ; 
    } 
  
    	
  }
  public boolean isPrivate() {
    return false;
  }

  static public class Private extends Visibility {
    // Production: sig("Private",[])
  
    
  
    public Private(IConstructor node ) {
      super(node);
      
    }
  
    @Override
    public boolean isPrivate() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitVisibilityPrivate(this);
    }
  
    @Override
    public boolean equals(Object o) {
      if (!(o instanceof Private)) {
        return false;
      }        
      Private tmp = (Private) o;
      return true ; 
    }
   
    @Override
    public int hashCode() {
      return 769 ; 
    } 
  
    	
  }
  public boolean isPublic() {
    return false;
  }

  static public class Public extends Visibility {
    // Production: sig("Public",[])
  
    
  
    public Public(IConstructor node ) {
      super(node);
      
    }
  
    @Override
    public boolean isPublic() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitVisibilityPublic(this);
    }
  
    @Override
    public boolean equals(Object o) {
      if (!(o instanceof Public)) {
        return false;
      }        
      Public tmp = (Public) o;
      return true ; 
    }
   
    @Override
    public int hashCode() {
      return 953 ; 
    } 
  
    	
  }
}