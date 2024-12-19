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
public abstract class Field extends AbstractAST {
  public Field(ISourceLocation src, IConstructor node) {
    super(src /* we forget node on purpose */);
  }

  
  public boolean hasFieldIndex() {
    return false;
  }

  public org.rascalmpl.ast.IntegerLiteral getFieldIndex() {
    throw new UnsupportedOperationException();
  }
  public boolean hasFieldName() {
    return false;
  }

  public org.rascalmpl.ast.Name getFieldName() {
    throw new UnsupportedOperationException();
  }

  

  
  public boolean isIndex() {
    return false;
  }

  static public class Index extends Field {
    // Production: sig("Index",[arg("org.rascalmpl.ast.IntegerLiteral","fieldIndex")],breakable=false)
  
    
    private final org.rascalmpl.ast.IntegerLiteral fieldIndex;
  
    public Index(ISourceLocation src, IConstructor node , org.rascalmpl.ast.IntegerLiteral fieldIndex) {
      super(src, node);
      
      this.fieldIndex = fieldIndex;
    }
  
    @Override
    public boolean isIndex() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitFieldIndex(this);
    }
  
    @Override
    protected void addForLineNumber(int $line, java.util.List<AbstractAST> $result) {
      if (getLocation().getBeginLine() == $line) {
        $result.add(this);
      }
      ISourceLocation $l;
      
      $l = fieldIndex.getLocation();
      if ($l.hasLineColumn() && $l.getBeginLine() <= $line && $l.getEndLine() >= $line) {
        fieldIndex.addForLineNumber($line, $result);
      }
      if ($l.getBeginLine() > $line) {
        return;
      }
      
    }
  
    @Override
    public boolean equals(Object o) {
      if (!(o instanceof Index)) {
        return false;
      }        
      Index tmp = (Index) o;
      return true && tmp.fieldIndex.equals(this.fieldIndex) ; 
    }
   
    @Override
    public int hashCode() {
      return 89 + 727 * fieldIndex.hashCode() ; 
    } 
  
    
    @Override
    public org.rascalmpl.ast.IntegerLiteral getFieldIndex() {
      return this.fieldIndex;
    }
  
    @Override
    public boolean hasFieldIndex() {
      return true;
    }	
  
    @Override
    public Object clone()  {
      return newInstance(getClass(), src, (IConstructor) null , clone(fieldIndex));
    }
            
  }
  public boolean isName() {
    return false;
  }

  static public class Name extends Field {
    // Production: sig("Name",[arg("org.rascalmpl.ast.Name","fieldName")],breakable=false)
  
    
    private final org.rascalmpl.ast.Name fieldName;
  
    public Name(ISourceLocation src, IConstructor node , org.rascalmpl.ast.Name fieldName) {
      super(src, node);
      
      this.fieldName = fieldName;
    }
  
    @Override
    public boolean isName() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitFieldName(this);
    }
  
    @Override
    protected void addForLineNumber(int $line, java.util.List<AbstractAST> $result) {
      if (getLocation().getBeginLine() == $line) {
        $result.add(this);
      }
      ISourceLocation $l;
      
      $l = fieldName.getLocation();
      if ($l.hasLineColumn() && $l.getBeginLine() <= $line && $l.getEndLine() >= $line) {
        fieldName.addForLineNumber($line, $result);
      }
      if ($l.getBeginLine() > $line) {
        return;
      }
      
    }
  
    @Override
    public boolean equals(Object o) {
      if (!(o instanceof Name)) {
        return false;
      }        
      Name tmp = (Name) o;
      return true && tmp.fieldName.equals(this.fieldName) ; 
    }
   
    @Override
    public int hashCode() {
      return 673 + 743 * fieldName.hashCode() ; 
    } 
  
    
    @Override
    public org.rascalmpl.ast.Name getFieldName() {
      return this.fieldName;
    }
  
    @Override
    public boolean hasFieldName() {
      return true;
    }	
  
    @Override
    public Object clone()  {
      return newInstance(getClass(), src, (IConstructor) null , clone(fieldName));
    }
            
  }
}