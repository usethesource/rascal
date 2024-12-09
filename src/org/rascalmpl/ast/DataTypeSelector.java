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
public abstract class DataTypeSelector extends AbstractAST {
  public DataTypeSelector(ISourceLocation src, IConstructor node) {
    super(src /* we forget node on purpose */);
  }

  
  public boolean hasProduction() {
    return false;
  }

  public org.rascalmpl.ast.Name getProduction() {
    throw new UnsupportedOperationException();
  }
  public boolean hasSort() {
    return false;
  }

  public org.rascalmpl.ast.QualifiedName getSort() {
    throw new UnsupportedOperationException();
  }

  

  
  public boolean isSelector() {
    return false;
  }

  static public class Selector extends DataTypeSelector {
    // Production: sig("Selector",[arg("org.rascalmpl.ast.QualifiedName","sort"),arg("org.rascalmpl.ast.Name","production")],breakable=false)
  
    
    private final org.rascalmpl.ast.QualifiedName sort;
    private final org.rascalmpl.ast.Name production;
  
    public Selector(ISourceLocation src, IConstructor node , org.rascalmpl.ast.QualifiedName sort,  org.rascalmpl.ast.Name production) {
      super(src, node);
      
      this.sort = sort;
      this.production = production;
    }
  
    @Override
    public boolean isSelector() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitDataTypeSelectorSelector(this);
    }
  
    @Override
    protected void addForLineNumber(int $line, java.util.List<AbstractAST> $result) {
      if (getLocation().getBeginLine() == $line) {
        $result.add(this);
      }
      ISourceLocation $l;
      
      $l = sort.getLocation();
      if ($l.hasLineColumn() && $l.getBeginLine() <= $line && $l.getEndLine() >= $line) {
        sort.addForLineNumber($line, $result);
      }
      if ($l.getBeginLine() > $line) {
        return;
      }
      
      $l = production.getLocation();
      if ($l.hasLineColumn() && $l.getBeginLine() <= $line && $l.getEndLine() >= $line) {
        production.addForLineNumber($line, $result);
      }
      if ($l.getBeginLine() > $line) {
        return;
      }
      
    }
  
    @Override
    public boolean equals(Object o) {
      if (!(o instanceof Selector)) {
        return false;
      }        
      Selector tmp = (Selector) o;
      return true && tmp.sort.equals(this.sort) && tmp.production.equals(this.production) ; 
    }
   
    @Override
    public int hashCode() {
      return 41 + 761 * sort.hashCode() + 971 * production.hashCode() ; 
    } 
  
    
    @Override
    public org.rascalmpl.ast.QualifiedName getSort() {
      return this.sort;
    }
  
    @Override
    public boolean hasSort() {
      return true;
    }
    @Override
    public org.rascalmpl.ast.Name getProduction() {
      return this.production;
    }
  
    @Override
    public boolean hasProduction() {
      return true;
    }	
  
    @Override
    public Object clone()  {
      return newInstance(getClass(), src, (IConstructor) null , clone(sort), clone(production));
    }
            
  }
}