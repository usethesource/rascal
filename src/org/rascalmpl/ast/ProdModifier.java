/*******************************************************************************
 * Copyright (c) 2009-2014 CWI
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

public abstract class ProdModifier extends AbstractAST {
  public ProdModifier(IConstructor node) {
    super();
  }

  
  public boolean hasAssociativity() {
    return false;
  }

  public org.rascalmpl.ast.Assoc getAssociativity() {
    throw new UnsupportedOperationException();
  }
  public boolean hasTag() {
    return false;
  }

  public org.rascalmpl.ast.Tag getTag() {
    throw new UnsupportedOperationException();
  }

  

  
  public boolean isAssociativity() {
    return false;
  }

  static public class Associativity extends ProdModifier {
    // Production: sig("Associativity",[arg("org.rascalmpl.ast.Assoc","associativity")])
  
    
    private final org.rascalmpl.ast.Assoc associativity;
  
    public Associativity(IConstructor node , org.rascalmpl.ast.Assoc associativity) {
      super(node);
      
      this.associativity = associativity;
    }
  
    @Override
    public boolean isAssociativity() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitProdModifierAssociativity(this);
    }
  
    @Override
    public boolean equals(Object o) {
      if (!(o instanceof Associativity)) {
        return false;
      }        
      Associativity tmp = (Associativity) o;
      return true && tmp.associativity.equals(this.associativity) ; 
    }
   
    @Override
    public int hashCode() {
      return 13331 + 41 * associativity.hashCode() ; 
    } 
  
    
    @Override
    public org.rascalmpl.ast.Assoc getAssociativity() {
      return this.associativity;
    }
  
    @Override
    public boolean hasAssociativity() {
      return true;
    }	
  }
  public boolean isBracket() {
    return false;
  }

  static public class Bracket extends ProdModifier {
    // Production: sig("Bracket",[])
  
    
  
    public Bracket(IConstructor node ) {
      super(node);
      
    }
  
    @Override
    public boolean isBracket() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitProdModifierBracket(this);
    }
  
    @Override
    public boolean equals(Object o) {
      if (!(o instanceof Bracket)) {
        return false;
      }        
      Bracket tmp = (Bracket) o;
      return true ; 
    }
   
    @Override
    public int hashCode() {
      return 13331 ; 
    } 
  
    	
  }
  public boolean isTag() {
    return false;
  }

  static public class Tag extends ProdModifier {
    // Production: sig("Tag",[arg("org.rascalmpl.ast.Tag","tag")])
  
    
    private final org.rascalmpl.ast.Tag tag;
  
    public Tag(IConstructor node , org.rascalmpl.ast.Tag tag) {
      super(node);
      
      this.tag = tag;
    }
  
    @Override
    public boolean isTag() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitProdModifierTag(this);
    }
  
    @Override
    public boolean equals(Object o) {
      if (!(o instanceof Tag)) {
        return false;
      }        
      Tag tmp = (Tag) o;
      return true && tmp.tag.equals(this.tag) ; 
    }
   
    @Override
    public int hashCode() {
      return 13331 + 23 * tag.hashCode() ; 
    } 
  
    
    @Override
    public org.rascalmpl.ast.Tag getTag() {
      return this.tag;
    }
  
    @Override
    public boolean hasTag() {
      return true;
    }	
  }
}