/*******************************************************************************
 * Copyright (c) 2009-2012 CWI
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
import org.eclipse.imp.pdb.facts.IValue;
import org.rascalmpl.interpreter.IEvaluator;
import org.rascalmpl.interpreter.asserts.Ambiguous;
import org.rascalmpl.interpreter.env.Environment;
import org.rascalmpl.interpreter.result.Result;

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

  static public class Ambiguity extends Visit {
    private final java.util.List<org.rascalmpl.ast.Visit> alternatives;
    private final IConstructor node;
           
    public Ambiguity(IConstructor node, java.util.List<org.rascalmpl.ast.Visit> alternatives) {
      super(node);
      this.node = node;
      this.alternatives = java.util.Collections.unmodifiableList(alternatives);
    }
    
    @Override
    public IConstructor getTree() {
      return node;
    }
  
    @Override
    public AbstractAST findNode(int offset) {
      return null;
    }
  
    @Override
    public Result<IValue> interpret(IEvaluator<Result<IValue>> __eval) {
      throw new Ambiguous(src);
    }
      
    @Override
    public org.eclipse.imp.pdb.facts.type.Type typeOf(Environment env) {
      throw new Ambiguous(src);
    }
    
    public java.util.List<org.rascalmpl.ast.Visit> getAlternatives() {
      return alternatives;
    }
    
    public <T> T accept(IASTVisitor<T> v) {
    	return v.visitVisitAmbiguity(this);
    }
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