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

public abstract class Kind extends AbstractAST {
  public Kind(IConstructor node) {
    super();
  }

  

  

  
  public boolean isAlias() {
    return false;
  }

  static public class Alias extends Kind {
    // Production: sig("Alias",[])
  
    
  
    public Alias(IConstructor node ) {
      super(node);
      
    }
  
    @Override
    public boolean isAlias() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitKindAlias(this);
    }
  
    @Override
    public boolean equals(Object o) {
      if (!(o instanceof Alias)) {
        return false;
      }        
      Alias tmp = (Alias) o;
      return true ; 
    }
   
    @Override
    public int hashCode() {
      return 461 ; 
    } 
  
    	
  }
  public boolean isAll() {
    return false;
  }

  static public class All extends Kind {
    // Production: sig("All",[])
  
    
  
    public All(IConstructor node ) {
      super(node);
      
    }
  
    @Override
    public boolean isAll() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitKindAll(this);
    }
  
    @Override
    public boolean equals(Object o) {
      if (!(o instanceof All)) {
        return false;
      }        
      All tmp = (All) o;
      return true ; 
    }
   
    @Override
    public int hashCode() {
      return 2 ; 
    } 
  
    	
  }
  public boolean isAnno() {
    return false;
  }

  static public class Anno extends Kind {
    // Production: sig("Anno",[])
  
    
  
    public Anno(IConstructor node ) {
      super(node);
      
    }
  
    @Override
    public boolean isAnno() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitKindAnno(this);
    }
  
    @Override
    public boolean equals(Object o) {
      if (!(o instanceof Anno)) {
        return false;
      }        
      Anno tmp = (Anno) o;
      return true ; 
    }
   
    @Override
    public int hashCode() {
      return 569 ; 
    } 
  
    	
  }
  public boolean isData() {
    return false;
  }

  static public class Data extends Kind {
    // Production: sig("Data",[])
  
    
  
    public Data(IConstructor node ) {
      super(node);
      
    }
  
    @Override
    public boolean isData() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitKindData(this);
    }
  
    @Override
    public boolean equals(Object o) {
      if (!(o instanceof Data)) {
        return false;
      }        
      Data tmp = (Data) o;
      return true ; 
    }
   
    @Override
    public int hashCode() {
      return 131 ; 
    } 
  
    	
  }
  public boolean isFunction() {
    return false;
  }

  static public class Function extends Kind {
    // Production: sig("Function",[])
  
    
  
    public Function(IConstructor node ) {
      super(node);
      
    }
  
    @Override
    public boolean isFunction() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitKindFunction(this);
    }
  
    @Override
    public boolean equals(Object o) {
      if (!(o instanceof Function)) {
        return false;
      }        
      Function tmp = (Function) o;
      return true ; 
    }
   
    @Override
    public int hashCode() {
      return 109 ; 
    } 
  
    	
  }
  public boolean isModule() {
    return false;
  }

  static public class Module extends Kind {
    // Production: sig("Module",[])
  
    
  
    public Module(IConstructor node ) {
      super(node);
      
    }
  
    @Override
    public boolean isModule() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitKindModule(this);
    }
  
    @Override
    public boolean equals(Object o) {
      if (!(o instanceof Module)) {
        return false;
      }        
      Module tmp = (Module) o;
      return true ; 
    }
   
    @Override
    public int hashCode() {
      return 197 ; 
    } 
  
    	
  }
  public boolean isTag() {
    return false;
  }

  static public class Tag extends Kind {
    // Production: sig("Tag",[])
  
    
  
    public Tag(IConstructor node ) {
      super(node);
      
    }
  
    @Override
    public boolean isTag() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitKindTag(this);
    }
  
    @Override
    public boolean equals(Object o) {
      if (!(o instanceof Tag)) {
        return false;
      }        
      Tag tmp = (Tag) o;
      return true ; 
    }
   
    @Override
    public int hashCode() {
      return 373 ; 
    } 
  
    	
  }
  public boolean isVariable() {
    return false;
  }

  static public class Variable extends Kind {
    // Production: sig("Variable",[])
  
    
  
    public Variable(IConstructor node ) {
      super(node);
      
    }
  
    @Override
    public boolean isVariable() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitKindVariable(this);
    }
  
    @Override
    public boolean equals(Object o) {
      if (!(o instanceof Variable)) {
        return false;
      }        
      Variable tmp = (Variable) o;
      return true ; 
    }
   
    @Override
    public int hashCode() {
      return 67 ; 
    } 
  
    	
  }
  public boolean isView() {
    return false;
  }

  static public class View extends Kind {
    // Production: sig("View",[])
  
    
  
    public View(IConstructor node ) {
      super(node);
      
    }
  
    @Override
    public boolean isView() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitKindView(this);
    }
  
    @Override
    public boolean equals(Object o) {
      if (!(o instanceof View)) {
        return false;
      }        
      View tmp = (View) o;
      return true ; 
    }
   
    @Override
    public int hashCode() {
      return 433 ; 
    } 
  
    	
  }
}