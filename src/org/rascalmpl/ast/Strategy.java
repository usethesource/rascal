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
public abstract class Strategy extends AbstractAST {
  public Strategy(ISourceLocation src, IConstructor node) {
    super(src /* we forget node on purpose */);
  }

  

  

  
  public boolean isBottomUp() {
    return false;
  }

  static public class BottomUp extends Strategy {
    // Production: sig("BottomUp",[],breakable=false)
  
    
  
    public BottomUp(ISourceLocation src, IConstructor node ) {
      super(src, node);
      
    }
  
    @Override
    public boolean isBottomUp() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitStrategyBottomUp(this);
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
      if (!(o instanceof BottomUp)) {
        return false;
      }        
      BottomUp tmp = (BottomUp) o;
      return true ; 
    }
   
    @Override
    public int hashCode() {
      return 53 ; 
    } 
  
    	
  
    @Override
    public Object clone()  {
      return newInstance(getClass(), src, (IConstructor) null );
    }
            
  }
  public boolean isBottomUpBreak() {
    return false;
  }

  static public class BottomUpBreak extends Strategy {
    // Production: sig("BottomUpBreak",[],breakable=false)
  
    
  
    public BottomUpBreak(ISourceLocation src, IConstructor node ) {
      super(src, node);
      
    }
  
    @Override
    public boolean isBottomUpBreak() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitStrategyBottomUpBreak(this);
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
      if (!(o instanceof BottomUpBreak)) {
        return false;
      }        
      BottomUpBreak tmp = (BottomUpBreak) o;
      return true ; 
    }
   
    @Override
    public int hashCode() {
      return 839 ; 
    } 
  
    	
  
    @Override
    public Object clone()  {
      return newInstance(getClass(), src, (IConstructor) null );
    }
            
  }
  public boolean isInnermost() {
    return false;
  }

  static public class Innermost extends Strategy {
    // Production: sig("Innermost",[],breakable=false)
  
    
  
    public Innermost(ISourceLocation src, IConstructor node ) {
      super(src, node);
      
    }
  
    @Override
    public boolean isInnermost() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitStrategyInnermost(this);
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
      if (!(o instanceof Innermost)) {
        return false;
      }        
      Innermost tmp = (Innermost) o;
      return true ; 
    }
   
    @Override
    public int hashCode() {
      return 389 ; 
    } 
  
    	
  
    @Override
    public Object clone()  {
      return newInstance(getClass(), src, (IConstructor) null );
    }
            
  }
  public boolean isOutermost() {
    return false;
  }

  static public class Outermost extends Strategy {
    // Production: sig("Outermost",[],breakable=false)
  
    
  
    public Outermost(ISourceLocation src, IConstructor node ) {
      super(src, node);
      
    }
  
    @Override
    public boolean isOutermost() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitStrategyOutermost(this);
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
      if (!(o instanceof Outermost)) {
        return false;
      }        
      Outermost tmp = (Outermost) o;
      return true ; 
    }
   
    @Override
    public int hashCode() {
      return 229 ; 
    } 
  
    	
  
    @Override
    public Object clone()  {
      return newInstance(getClass(), src, (IConstructor) null );
    }
            
  }
  public boolean isTopDown() {
    return false;
  }

  static public class TopDown extends Strategy {
    // Production: sig("TopDown",[],breakable=false)
  
    
  
    public TopDown(ISourceLocation src, IConstructor node ) {
      super(src, node);
      
    }
  
    @Override
    public boolean isTopDown() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitStrategyTopDown(this);
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
      if (!(o instanceof TopDown)) {
        return false;
      }        
      TopDown tmp = (TopDown) o;
      return true ; 
    }
   
    @Override
    public int hashCode() {
      return 157 ; 
    } 
  
    	
  
    @Override
    public Object clone()  {
      return newInstance(getClass(), src, (IConstructor) null );
    }
            
  }
  public boolean isTopDownBreak() {
    return false;
  }

  static public class TopDownBreak extends Strategy {
    // Production: sig("TopDownBreak",[],breakable=false)
  
    
  
    public TopDownBreak(ISourceLocation src, IConstructor node ) {
      super(src, node);
      
    }
  
    @Override
    public boolean isTopDownBreak() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitStrategyTopDownBreak(this);
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
      if (!(o instanceof TopDownBreak)) {
        return false;
      }        
      TopDownBreak tmp = (TopDownBreak) o;
      return true ; 
    }
   
    @Override
    public int hashCode() {
      return 283 ; 
    } 
  
    	
  
    @Override
    public Object clone()  {
      return newInstance(getClass(), src, (IConstructor) null );
    }
            
  }
}