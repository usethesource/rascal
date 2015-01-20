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

public abstract class Visit extends AbstractAST {
  public Visit(IConstructor node) {
    super();
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
    // Production: sig("DefaultStrategy",[arg("org.rascalmpl.ast.Expression","subject"),arg("java.util.List\<org.rascalmpl.ast.Case\>","cases")])
  
    
    private final org.rascalmpl.ast.Expression subject;
    private final java.util.List<org.rascalmpl.ast.Case> cases;
  
    public DefaultStrategy(IConstructor node , org.rascalmpl.ast.Expression subject,  java.util.List<org.rascalmpl.ast.Case> cases) {
      super(node);
      
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
    public boolean equals(Object o) {
      if (!(o instanceof DefaultStrategy)) {
        return false;
      }        
      DefaultStrategy tmp = (DefaultStrategy) o;
      return true && tmp.subject.equals(this.subject) && tmp.cases.equals(this.cases) ; 
    }
   
    @Override
    public int hashCode() {
      return 359 + 769 * subject.hashCode() + 47 * cases.hashCode() ; 
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
  }
  public boolean isGivenStrategy() {
    return false;
  }

  static public class GivenStrategy extends Visit {
    // Production: sig("GivenStrategy",[arg("org.rascalmpl.ast.Strategy","strategy"),arg("org.rascalmpl.ast.Expression","subject"),arg("java.util.List\<org.rascalmpl.ast.Case\>","cases")])
  
    
    private final org.rascalmpl.ast.Strategy strategy;
    private final org.rascalmpl.ast.Expression subject;
    private final java.util.List<org.rascalmpl.ast.Case> cases;
  
    public GivenStrategy(IConstructor node , org.rascalmpl.ast.Strategy strategy,  org.rascalmpl.ast.Expression subject,  java.util.List<org.rascalmpl.ast.Case> cases) {
      super(node);
      
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
    public boolean equals(Object o) {
      if (!(o instanceof GivenStrategy)) {
        return false;
      }        
      GivenStrategy tmp = (GivenStrategy) o;
      return true && tmp.strategy.equals(this.strategy) && tmp.subject.equals(this.subject) && tmp.cases.equals(this.cases) ; 
    }
   
    @Override
    public int hashCode() {
      return 569 + 3 * strategy.hashCode() + 643 * subject.hashCode() + 661 * cases.hashCode() ; 
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
  }
}