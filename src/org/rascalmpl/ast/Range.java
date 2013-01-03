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

public abstract class Range extends AbstractAST {
  public Range(IConstructor node) {
    super();
  }

  
  public boolean hasCharacter() {
    return false;
  }

  public org.rascalmpl.ast.Char getCharacter() {
    throw new UnsupportedOperationException();
  }
  public boolean hasEnd() {
    return false;
  }

  public org.rascalmpl.ast.Char getEnd() {
    throw new UnsupportedOperationException();
  }
  public boolean hasStart() {
    return false;
  }

  public org.rascalmpl.ast.Char getStart() {
    throw new UnsupportedOperationException();
  }

  static public class Ambiguity extends Range {
    private final java.util.List<org.rascalmpl.ast.Range> alternatives;
    private final IConstructor node;
           
    public Ambiguity(IConstructor node, java.util.List<org.rascalmpl.ast.Range> alternatives) {
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
    
    public java.util.List<org.rascalmpl.ast.Range> getAlternatives() {
      return alternatives;
    }
    
    public <T> T accept(IASTVisitor<T> v) {
    	return v.visitRangeAmbiguity(this);
    }
  }

  

  
  public boolean isCharacter() {
    return false;
  }

  static public class Character extends Range {
    // Production: sig("Character",[arg("org.rascalmpl.ast.Char","character")])
  
    
    private final org.rascalmpl.ast.Char character;
  
    public Character(IConstructor node , org.rascalmpl.ast.Char character) {
      super(node);
      
      this.character = character;
    }
  
    @Override
    public boolean isCharacter() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitRangeCharacter(this);
    }
  
    
    @Override
    public org.rascalmpl.ast.Char getCharacter() {
      return this.character;
    }
  
    @Override
    public boolean hasCharacter() {
      return true;
    }	
  }
  public boolean isFromTo() {
    return false;
  }

  static public class FromTo extends Range {
    // Production: sig("FromTo",[arg("org.rascalmpl.ast.Char","start"),arg("org.rascalmpl.ast.Char","end")])
  
    
    private final org.rascalmpl.ast.Char start;
    private final org.rascalmpl.ast.Char end;
  
    public FromTo(IConstructor node , org.rascalmpl.ast.Char start,  org.rascalmpl.ast.Char end) {
      super(node);
      
      this.start = start;
      this.end = end;
    }
  
    @Override
    public boolean isFromTo() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitRangeFromTo(this);
    }
  
    
    @Override
    public org.rascalmpl.ast.Char getStart() {
      return this.start;
    }
  
    @Override
    public boolean hasStart() {
      return true;
    }
    @Override
    public org.rascalmpl.ast.Char getEnd() {
      return this.end;
    }
  
    @Override
    public boolean hasEnd() {
      return true;
    }	
  }
}