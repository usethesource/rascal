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

public abstract class Strategy extends AbstractAST {
  public Strategy(IConstructor node) {
    super();
  }

  

  static public class Ambiguity extends Strategy {
    private final java.util.List<org.rascalmpl.ast.Strategy> alternatives;
    private final IConstructor node;
           
    public Ambiguity(IConstructor node, java.util.List<org.rascalmpl.ast.Strategy> alternatives) {
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
    
    public java.util.List<org.rascalmpl.ast.Strategy> getAlternatives() {
      return alternatives;
    }
    
    public <T> T accept(IASTVisitor<T> v) {
    	return v.visitStrategyAmbiguity(this);
    }
  }

  

  
  public boolean isBottomUp() {
    return false;
  }

  static public class BottomUp extends Strategy {
    // Production: sig("BottomUp",[])
  
    
  
    public BottomUp(IConstructor node ) {
      super(node);
      
    }
  
    @Override
    public boolean isBottomUp() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitStrategyBottomUp(this);
    }
  
    	
  }
  public boolean isBottomUpBreak() {
    return false;
  }

  static public class BottomUpBreak extends Strategy {
    // Production: sig("BottomUpBreak",[])
  
    
  
    public BottomUpBreak(IConstructor node ) {
      super(node);
      
    }
  
    @Override
    public boolean isBottomUpBreak() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitStrategyBottomUpBreak(this);
    }
  
    	
  }
  public boolean isInnermost() {
    return false;
  }

  static public class Innermost extends Strategy {
    // Production: sig("Innermost",[])
  
    
  
    public Innermost(IConstructor node ) {
      super(node);
      
    }
  
    @Override
    public boolean isInnermost() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitStrategyInnermost(this);
    }
  
    	
  }
  public boolean isOutermost() {
    return false;
  }

  static public class Outermost extends Strategy {
    // Production: sig("Outermost",[])
  
    
  
    public Outermost(IConstructor node ) {
      super(node);
      
    }
  
    @Override
    public boolean isOutermost() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitStrategyOutermost(this);
    }
  
    	
  }
  public boolean isTopDown() {
    return false;
  }

  static public class TopDown extends Strategy {
    // Production: sig("TopDown",[])
  
    
  
    public TopDown(IConstructor node ) {
      super(node);
      
    }
  
    @Override
    public boolean isTopDown() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitStrategyTopDown(this);
    }
  
    	
  }
  public boolean isTopDownBreak() {
    return false;
  }

  static public class TopDownBreak extends Strategy {
    // Production: sig("TopDownBreak",[])
  
    
  
    public TopDownBreak(IConstructor node ) {
      super(node);
      
    }
  
    @Override
    public boolean isTopDownBreak() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitStrategyTopDownBreak(this);
    }
  
    	
  }
}