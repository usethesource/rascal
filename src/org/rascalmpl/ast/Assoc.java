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

public abstract class Assoc extends AbstractAST {
  public Assoc(IConstructor node) {
    super();
  }

  

  static public class Ambiguity extends Assoc {
    private final java.util.List<org.rascalmpl.ast.Assoc> alternatives;
    private final IConstructor node;
           
    public Ambiguity(IConstructor node, java.util.List<org.rascalmpl.ast.Assoc> alternatives) {
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
    
    public java.util.List<org.rascalmpl.ast.Assoc> getAlternatives() {
      return alternatives;
    }
    
    public <T> T accept(IASTVisitor<T> v) {
    	return v.visitAssocAmbiguity(this);
    }
  }

  

  
  public boolean isAssociative() {
    return false;
  }

  static public class Associative extends Assoc {
    // Production: sig("Associative",[])
  
    
  
    public Associative(IConstructor node ) {
      super(node);
      
    }
  
    @Override
    public boolean isAssociative() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitAssocAssociative(this);
    }
  
    	
  }
  public boolean isLeft() {
    return false;
  }

  static public class Left extends Assoc {
    // Production: sig("Left",[])
  
    
  
    public Left(IConstructor node ) {
      super(node);
      
    }
  
    @Override
    public boolean isLeft() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitAssocLeft(this);
    }
  
    	
  }
  public boolean isNonAssociative() {
    return false;
  }

  static public class NonAssociative extends Assoc {
    // Production: sig("NonAssociative",[])
  
    
  
    public NonAssociative(IConstructor node ) {
      super(node);
      
    }
  
    @Override
    public boolean isNonAssociative() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitAssocNonAssociative(this);
    }
  
    	
  }
  public boolean isRight() {
    return false;
  }

  static public class Right extends Assoc {
    // Production: sig("Right",[])
  
    
  
    public Right(IConstructor node ) {
      super(node);
      
    }
  
    @Override
    public boolean isRight() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitAssocRight(this);
    }
  
    	
  }
}