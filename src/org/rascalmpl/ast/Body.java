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
public abstract class Body extends AbstractAST {
  public Body(ISourceLocation src, IConstructor node) {
    super(src /* we forget node on purpose */);
  }

  
  public boolean hasToplevels() {
    return false;
  }

  public java.util.List<org.rascalmpl.ast.Toplevel> getToplevels() {
    throw new UnsupportedOperationException();
  }

  

  
  public boolean isToplevels() {
    return false;
  }

  static public class Toplevels extends Body {
    // Production: sig("Toplevels",[arg("java.util.List\<org.rascalmpl.ast.Toplevel\>","toplevels")],breakable=false)
  
    
    private final java.util.List<org.rascalmpl.ast.Toplevel> toplevels;
  
    public Toplevels(ISourceLocation src, IConstructor node , java.util.List<org.rascalmpl.ast.Toplevel> toplevels) {
      super(src, node);
      
      this.toplevels = toplevels;
    }
  
    @Override
    public boolean isToplevels() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitBodyToplevels(this);
    }
  
    @Override
    protected void addForLineNumber(int $line, java.util.List<AbstractAST> $result) {
      if (getLocation().getBeginLine() == $line) {
        $result.add(this);
      }
      ISourceLocation $l;
      
      for (AbstractAST $elem : toplevels) {
        $l = $elem.getLocation();
        if ($l.hasLineColumn() && $l.getBeginLine() <= $line && $l.getEndLine() >= $line) {
          $elem.addForLineNumber($line, $result);
        }
        if ($l.getBeginLine() > $line) {
          return;
        }
  
      }
    }
  
    @Override
    public boolean equals(Object o) {
      if (!(o instanceof Toplevels)) {
        return false;
      }        
      Toplevels tmp = (Toplevels) o;
      return true && tmp.toplevels.equals(this.toplevels) ; 
    }
   
    @Override
    public int hashCode() {
      return 743 + 137 * toplevels.hashCode() ; 
    } 
  
    
    @Override
    public java.util.List<org.rascalmpl.ast.Toplevel> getToplevels() {
      return this.toplevels;
    }
  
    @Override
    public boolean hasToplevels() {
      return true;
    }	
  
    @Override
    public Object clone()  {
      return newInstance(getClass(), src, (IConstructor) null , clone(toplevels));
    }
            
  }
}