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


import io.usethesource.vallang.IConstructor;
import io.usethesource.vallang.ISourceLocation;

@SuppressWarnings(value = {"unused"})
public abstract class ProdModifier extends AbstractAST {
  public ProdModifier(ISourceLocation src, IConstructor node) {
    super(src /* we forget node on purpose */);
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
    // Production: sig("Associativity",[arg("org.rascalmpl.ast.Assoc","associativity")],breakable=false)
  
    
    private final org.rascalmpl.ast.Assoc associativity;
  
    public Associativity(ISourceLocation src, IConstructor node , org.rascalmpl.ast.Assoc associativity) {
      super(src, node);
      
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
    protected void addForLineNumber(int $line, java.util.List<AbstractAST> $result) {
      if (getLocation().getBeginLine() == $line) {
        $result.add(this);
      }
      ISourceLocation $l;
      
      $l = associativity.getLocation();
      if ($l.hasLineColumn() && $l.getBeginLine() <= $line && $l.getEndLine() >= $line) {
        associativity.addForLineNumber($line, $result);
      }
      if ($l.getBeginLine() > $line) {
        return;
      }
      
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
      return 157 + 139 * associativity.hashCode() ; 
    } 
  
    
    @Override
    public org.rascalmpl.ast.Assoc getAssociativity() {
      return this.associativity;
    }
  
    @Override
    public boolean hasAssociativity() {
      return true;
    }	
  
    @Override
    public Object clone()  {
      return newInstance(getClass(), src, (IConstructor) null , clone(associativity));
    }
            
  }
  public boolean isBracket() {
    return false;
  }

  static public class Bracket extends ProdModifier {
    // Production: sig("Bracket",[],breakable=false)
  
    
  
    public Bracket(ISourceLocation src, IConstructor node ) {
      super(src, node);
      
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
    protected void addForLineNumber(int $line, java.util.List<AbstractAST> $result) {
      if (getLocation().getBeginLine() == $line) {
        $result.add(this);
      }
      ISourceLocation $l;
      
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
      return 193 ; 
    } 
  
    	
  
    @Override
    public Object clone()  {
      return newInstance(getClass(), src, (IConstructor) null );
    }
            
  }
  public boolean isTag() {
    return false;
  }

  static public class Tag extends ProdModifier {
    // Production: sig("Tag",[arg("org.rascalmpl.ast.Tag","tag")],breakable=false)
  
    
    private final org.rascalmpl.ast.Tag tag;
  
    public Tag(ISourceLocation src, IConstructor node , org.rascalmpl.ast.Tag tag) {
      super(src, node);
      
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
    protected void addForLineNumber(int $line, java.util.List<AbstractAST> $result) {
      if (getLocation().getBeginLine() == $line) {
        $result.add(this);
      }
      ISourceLocation $l;
      
      $l = tag.getLocation();
      if ($l.hasLineColumn() && $l.getBeginLine() <= $line && $l.getEndLine() >= $line) {
        tag.addForLineNumber($line, $result);
      }
      if ($l.getBeginLine() > $line) {
        return;
      }
      
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
      return 859 + 3 * tag.hashCode() ; 
    } 
  
    
    @Override
    public org.rascalmpl.ast.Tag getTag() {
      return this.tag;
    }
  
    @Override
    public boolean hasTag() {
      return true;
    }	
  
    @Override
    public Object clone()  {
      return newInstance(getClass(), src, (IConstructor) null , clone(tag));
    }
            
  }
}