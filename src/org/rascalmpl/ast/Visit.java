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
public abstract class Visit extends AbstractAST {
  public Visit(ISourceLocation src, IConstructor node) {
    super(src /* we forget node on purpose */);
  }

  
  public boolean hasCases() {
    return false;
  }

  public java.util.List<org.rascalmpl.ast.Case> getCases() {
    throw new UnsupportedOperationException();
  }
  public boolean hasSubject() {
    return false;
  }

  public org.rascalmpl.ast.Expression getSubject() {
    throw new UnsupportedOperationException();
  }
  public boolean hasStrategy() {
    return false;
  }

  public org.rascalmpl.ast.Strategy getStrategy() {
    throw new UnsupportedOperationException();
  }

  

  
  public boolean isDefaultStrategy() {
    return false;
  }

  static public class DefaultStrategy extends Visit {
    // Production: sig("DefaultStrategy",[arg("org.rascalmpl.ast.Expression","subject"),arg("java.util.List\<org.rascalmpl.ast.Case\>","cases")],breakable=false)
  
    
    private final org.rascalmpl.ast.Expression subject;
    private final java.util.List<org.rascalmpl.ast.Case> cases;
  
    public DefaultStrategy(ISourceLocation src, IConstructor node , org.rascalmpl.ast.Expression subject,  java.util.List<org.rascalmpl.ast.Case> cases) {
      super(src, node);
      
      this.subject = subject;
      this.cases = cases;
    }
  
    @Override
    public boolean isDefaultStrategy() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitVisitDefaultStrategy(this);
    }
  
    @Override
    protected void addForLineNumber(int $line, java.util.List<AbstractAST> $result) {
      if (getLocation().getBeginLine() == $line) {
        $result.add(this);
      }
      ISourceLocation $l;
      
      $l = subject.getLocation();
      if ($l.hasLineColumn() && $l.getBeginLine() <= $line && $l.getEndLine() >= $line) {
        subject.addForLineNumber($line, $result);
      }
      if ($l.getBeginLine() > $line) {
        return;
      }
      
      for (AbstractAST $elem : cases) {
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
      if (!(o instanceof DefaultStrategy)) {
        return false;
      }        
      DefaultStrategy tmp = (DefaultStrategy) o;
      return true && tmp.subject.equals(this.subject) && tmp.cases.equals(this.cases) ; 
    }
   
    @Override
    public int hashCode() {
      return 797 + 149 * subject.hashCode() + 877 * cases.hashCode() ; 
    } 
  
    
    @Override
    public org.rascalmpl.ast.Expression getSubject() {
      return this.subject;
    }
  
    @Override
    public boolean hasSubject() {
      return true;
    }
    @Override
    public java.util.List<org.rascalmpl.ast.Case> getCases() {
      return this.cases;
    }
  
    @Override
    public boolean hasCases() {
      return true;
    }	
  
    @Override
    public Object clone()  {
      return newInstance(getClass(), src, (IConstructor) null , clone(subject), clone(cases));
    }
            
  }
  public boolean isGivenStrategy() {
    return false;
  }

  static public class GivenStrategy extends Visit {
    // Production: sig("GivenStrategy",[arg("org.rascalmpl.ast.Strategy","strategy"),arg("org.rascalmpl.ast.Expression","subject"),arg("java.util.List\<org.rascalmpl.ast.Case\>","cases")],breakable=false)
  
    
    private final org.rascalmpl.ast.Strategy strategy;
    private final org.rascalmpl.ast.Expression subject;
    private final java.util.List<org.rascalmpl.ast.Case> cases;
  
    public GivenStrategy(ISourceLocation src, IConstructor node , org.rascalmpl.ast.Strategy strategy,  org.rascalmpl.ast.Expression subject,  java.util.List<org.rascalmpl.ast.Case> cases) {
      super(src, node);
      
      this.strategy = strategy;
      this.subject = subject;
      this.cases = cases;
    }
  
    @Override
    public boolean isGivenStrategy() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitVisitGivenStrategy(this);
    }
  
    @Override
    protected void addForLineNumber(int $line, java.util.List<AbstractAST> $result) {
      if (getLocation().getBeginLine() == $line) {
        $result.add(this);
      }
      ISourceLocation $l;
      
      $l = strategy.getLocation();
      if ($l.hasLineColumn() && $l.getBeginLine() <= $line && $l.getEndLine() >= $line) {
        strategy.addForLineNumber($line, $result);
      }
      if ($l.getBeginLine() > $line) {
        return;
      }
      
      $l = subject.getLocation();
      if ($l.hasLineColumn() && $l.getBeginLine() <= $line && $l.getEndLine() >= $line) {
        subject.addForLineNumber($line, $result);
      }
      if ($l.getBeginLine() > $line) {
        return;
      }
      
      for (AbstractAST $elem : cases) {
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
      if (!(o instanceof GivenStrategy)) {
        return false;
      }        
      GivenStrategy tmp = (GivenStrategy) o;
      return true && tmp.strategy.equals(this.strategy) && tmp.subject.equals(this.subject) && tmp.cases.equals(this.cases) ; 
    }
   
    @Override
    public int hashCode() {
      return 401 + 787 * strategy.hashCode() + 239 * subject.hashCode() + 997 * cases.hashCode() ; 
    } 
  
    
    @Override
    public org.rascalmpl.ast.Strategy getStrategy() {
      return this.strategy;
    }
  
    @Override
    public boolean hasStrategy() {
      return true;
    }
    @Override
    public org.rascalmpl.ast.Expression getSubject() {
      return this.subject;
    }
  
    @Override
    public boolean hasSubject() {
      return true;
    }
    @Override
    public java.util.List<org.rascalmpl.ast.Case> getCases() {
      return this.cases;
    }
  
    @Override
    public boolean hasCases() {
      return true;
    }	
  
    @Override
    public Object clone()  {
      return newInstance(getClass(), src, (IConstructor) null , clone(strategy), clone(subject), clone(cases));
    }
            
  }
}