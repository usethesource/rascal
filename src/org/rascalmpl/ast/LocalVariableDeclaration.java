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
public abstract class LocalVariableDeclaration extends AbstractAST {
  public LocalVariableDeclaration(ISourceLocation src, IConstructor node) {
    super(src /* we forget node on purpose */);
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
    // Production: sig("Default",[arg("org.rascalmpl.ast.Declarator","declarator")],breakable=false)
  
    
    private final org.rascalmpl.ast.Declarator declarator;
  
    public Default(ISourceLocation src, IConstructor node , org.rascalmpl.ast.Declarator declarator) {
      super(src, node);
      
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
    protected void addForLineNumber(int $line, java.util.List<AbstractAST> $result) {
      if (getLocation().getBeginLine() == $line) {
        $result.add(this);
      }
      ISourceLocation $l;
      
      $l = declarator.getLocation();
      if ($l.hasLineColumn() && $l.getBeginLine() <= $line && $l.getEndLine() >= $line) {
        declarator.addForLineNumber($line, $result);
      }
      if ($l.getBeginLine() > $line) {
        return;
      }
      
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
      return 463 + 691 * declarator.hashCode() ; 
    } 
  
    
    @Override
    public org.rascalmpl.ast.Declarator getDeclarator() {
      return this.declarator;
    }
  
    @Override
    public boolean hasDeclarator() {
      return true;
    }	
  
    @Override
    public Object clone()  {
      return newInstance(getClass(), src, (IConstructor) null , clone(declarator));
    }
            
  }
  public boolean isDynamic() {
    return false;
  }

  static public class Dynamic extends LocalVariableDeclaration {
    // Production: sig("Dynamic",[arg("org.rascalmpl.ast.Declarator","declarator")],breakable=false)
  
    
    private final org.rascalmpl.ast.Declarator declarator;
  
    public Dynamic(ISourceLocation src, IConstructor node , org.rascalmpl.ast.Declarator declarator) {
      super(src, node);
      
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
    protected void addForLineNumber(int $line, java.util.List<AbstractAST> $result) {
      if (getLocation().getBeginLine() == $line) {
        $result.add(this);
      }
      ISourceLocation $l;
      
      $l = declarator.getLocation();
      if ($l.hasLineColumn() && $l.getBeginLine() <= $line && $l.getEndLine() >= $line) {
        declarator.addForLineNumber($line, $result);
      }
      if ($l.getBeginLine() > $line) {
        return;
      }
      
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
      return 479 + 89 * declarator.hashCode() ; 
    } 
  
    
    @Override
    public org.rascalmpl.ast.Declarator getDeclarator() {
      return this.declarator;
    }
  
    @Override
    public boolean hasDeclarator() {
      return true;
    }	
  
    @Override
    public Object clone()  {
      return newInstance(getClass(), src, (IConstructor) null , clone(declarator));
    }
            
  }
}