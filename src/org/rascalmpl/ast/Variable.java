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
public abstract class Variable extends AbstractAST {
  public Variable(ISourceLocation src, IConstructor node) {
    super(src /* we forget node on purpose */);
  }

  
  public boolean hasInitial() {
    return false;
  }

  public org.rascalmpl.ast.Expression getInitial() {
    throw new UnsupportedOperationException();
  }
  public boolean hasName() {
    return false;
  }

  public org.rascalmpl.ast.Name getName() {
    throw new UnsupportedOperationException();
  }

  

  
  public boolean isInitialized() {
    return false;
  }

  static public class Initialized extends Variable {
    // Production: sig("Initialized",[arg("org.rascalmpl.ast.Name","name"),arg("org.rascalmpl.ast.Expression","initial")],breakable=false)
  
    
    private final org.rascalmpl.ast.Name name;
    private final org.rascalmpl.ast.Expression initial;
  
    public Initialized(ISourceLocation src, IConstructor node , org.rascalmpl.ast.Name name,  org.rascalmpl.ast.Expression initial) {
      super(src, node);
      
      this.name = name;
      this.initial = initial;
    }
  
    @Override
    public boolean isInitialized() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitVariableInitialized(this);
    }
  
    @Override
    protected void addForLineNumber(int $line, java.util.List<AbstractAST> $result) {
      if (getLocation().getBeginLine() == $line) {
        $result.add(this);
      }
      ISourceLocation $l;
      
      $l = name.getLocation();
      if ($l.hasLineColumn() && $l.getBeginLine() <= $line && $l.getEndLine() >= $line) {
        name.addForLineNumber($line, $result);
      }
      if ($l.getBeginLine() > $line) {
        return;
      }
      
      $l = initial.getLocation();
      if ($l.hasLineColumn() && $l.getBeginLine() <= $line && $l.getEndLine() >= $line) {
        initial.addForLineNumber($line, $result);
      }
      if ($l.getBeginLine() > $line) {
        return;
      }
      
    }
  
    @Override
    public boolean equals(Object o) {
      if (!(o instanceof Initialized)) {
        return false;
      }        
      Initialized tmp = (Initialized) o;
      return true && tmp.name.equals(this.name) && tmp.initial.equals(this.initial) ; 
    }
   
    @Override
    public int hashCode() {
      return 467 + 691 * name.hashCode() + 701 * initial.hashCode() ; 
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
    public org.rascalmpl.ast.Expression getInitial() {
      return this.initial;
    }
  
    @Override
    public boolean hasInitial() {
      return true;
    }	
  
    @Override
    public Object clone()  {
      return newInstance(getClass(), src, (IConstructor) null , clone(name), clone(initial));
    }
            
  }
  public boolean isUnInitialized() {
    return false;
  }

  static public class UnInitialized extends Variable {
    // Production: sig("UnInitialized",[arg("org.rascalmpl.ast.Name","name")],breakable=false)
  
    
    private final org.rascalmpl.ast.Name name;
  
    public UnInitialized(ISourceLocation src, IConstructor node , org.rascalmpl.ast.Name name) {
      super(src, node);
      
      this.name = name;
    }
  
    @Override
    public boolean isUnInitialized() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitVariableUnInitialized(this);
    }
  
    @Override
    protected void addForLineNumber(int $line, java.util.List<AbstractAST> $result) {
      if (getLocation().getBeginLine() == $line) {
        $result.add(this);
      }
      ISourceLocation $l;
      
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
      if (!(o instanceof UnInitialized)) {
        return false;
      }        
      UnInitialized tmp = (UnInitialized) o;
      return true && tmp.name.equals(this.name) ; 
    }
   
    @Override
    public int hashCode() {
      return 19 + 137 * name.hashCode() ; 
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
      return newInstance(getClass(), src, (IConstructor) null , clone(name));
    }
            
  }
}