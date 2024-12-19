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
public abstract class TypeArg extends AbstractAST {
  public TypeArg(ISourceLocation src, IConstructor node) {
    super(src /* we forget node on purpose */);
  }

  
  public boolean hasName() {
    return false;
  }

  public org.rascalmpl.ast.Name getName() {
    throw new UnsupportedOperationException();
  }
  public boolean hasType() {
    return false;
  }

  public org.rascalmpl.ast.Type getType() {
    throw new UnsupportedOperationException();
  }

  

  
  public boolean isDefault() {
    return false;
  }

  static public class Default extends TypeArg {
    // Production: sig("Default",[arg("org.rascalmpl.ast.Type","type")],breakable=false)
  
    
    private final org.rascalmpl.ast.Type type;
  
    public Default(ISourceLocation src, IConstructor node , org.rascalmpl.ast.Type type) {
      super(src, node);
      
      this.type = type;
    }
  
    @Override
    public boolean isDefault() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitTypeArgDefault(this);
    }
  
    @Override
    protected void addForLineNumber(int $line, java.util.List<AbstractAST> $result) {
      if (getLocation().getBeginLine() == $line) {
        $result.add(this);
      }
      ISourceLocation $l;
      
      $l = type.getLocation();
      if ($l.hasLineColumn() && $l.getBeginLine() <= $line && $l.getEndLine() >= $line) {
        type.addForLineNumber($line, $result);
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
      return true && tmp.type.equals(this.type) ; 
    }
   
    @Override
    public int hashCode() {
      return 797 + 787 * type.hashCode() ; 
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
    public Object clone()  {
      return newInstance(getClass(), src, (IConstructor) null , clone(type));
    }
            
  }
  public boolean isNamed() {
    return false;
  }

  static public class Named extends TypeArg {
    // Production: sig("Named",[arg("org.rascalmpl.ast.Type","type"),arg("org.rascalmpl.ast.Name","name")],breakable=false)
  
    
    private final org.rascalmpl.ast.Type type;
    private final org.rascalmpl.ast.Name name;
  
    public Named(ISourceLocation src, IConstructor node , org.rascalmpl.ast.Type type,  org.rascalmpl.ast.Name name) {
      super(src, node);
      
      this.type = type;
      this.name = name;
    }
  
    @Override
    public boolean isNamed() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitTypeArgNamed(this);
    }
  
    @Override
    protected void addForLineNumber(int $line, java.util.List<AbstractAST> $result) {
      if (getLocation().getBeginLine() == $line) {
        $result.add(this);
      }
      ISourceLocation $l;
      
      $l = type.getLocation();
      if ($l.hasLineColumn() && $l.getBeginLine() <= $line && $l.getEndLine() >= $line) {
        type.addForLineNumber($line, $result);
      }
      if ($l.getBeginLine() > $line) {
        return;
      }
      
      $l = name.getLocation();
      if ($l.hasLineColumn() && $l.getBeginLine() <= $line && $l.getEndLine() >= $line) {
        name.addForLineNumber($line, $result);
      }
      if ($l.getBeginLine() > $line) {
        return;
      }
      
    }
  
    @Override
    public boolean equals(Object o) {
      if (!(o instanceof Named)) {
        return false;
      }        
      Named tmp = (Named) o;
      return true && tmp.type.equals(this.type) && tmp.name.equals(this.name) ; 
    }
   
    @Override
    public int hashCode() {
      return 397 + 191 * type.hashCode() + 977 * name.hashCode() ; 
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
    public Object clone()  {
      return newInstance(getClass(), src, (IConstructor) null , clone(type), clone(name));
    }
            
  }
}