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

public abstract class PatternWithAction extends AbstractAST {
  public PatternWithAction(IConstructor node) {
    super();
  }

  
  public boolean hasPattern() {
    return false;
  }

  public org.rascalmpl.ast.Expression getPattern() {
    throw new UnsupportedOperationException();
  }
  public boolean hasReplacement() {
    return false;
  }

  public org.rascalmpl.ast.Replacement getReplacement() {
    throw new UnsupportedOperationException();
  }
  public boolean hasStatement() {
    return false;
  }

  public org.rascalmpl.ast.Statement getStatement() {
    throw new UnsupportedOperationException();
  }

  static public class Ambiguity extends PatternWithAction {
    private final java.util.List<org.rascalmpl.ast.PatternWithAction> alternatives;
    private final IConstructor node;
           
    public Ambiguity(IConstructor node, java.util.List<org.rascalmpl.ast.PatternWithAction> alternatives) {
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
    
    public java.util.List<org.rascalmpl.ast.PatternWithAction> getAlternatives() {
      return alternatives;
    }
    
    public <T> T accept(IASTVisitor<T> v) {
    	return v.visitPatternWithActionAmbiguity(this);
    }
  }

  

  
  public boolean isArbitrary() {
    return false;
  }

  static public class Arbitrary extends PatternWithAction {
    // Production: sig("Arbitrary",[arg("org.rascalmpl.ast.Expression","pattern"),arg("org.rascalmpl.ast.Statement","statement")])
  
    
    private final org.rascalmpl.ast.Expression pattern;
    private final org.rascalmpl.ast.Statement statement;
  
    public Arbitrary(IConstructor node , org.rascalmpl.ast.Expression pattern,  org.rascalmpl.ast.Statement statement) {
      super(node);
      
      this.pattern = pattern;
      this.statement = statement;
    }
  
    @Override
    public boolean isArbitrary() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitPatternWithActionArbitrary(this);
    }
  
    
    @Override
    public org.rascalmpl.ast.Expression getPattern() {
      return this.pattern;
    }
  
    @Override
    public boolean hasPattern() {
      return true;
    }
    @Override
    public org.rascalmpl.ast.Statement getStatement() {
      return this.statement;
    }
  
    @Override
    public boolean hasStatement() {
      return true;
    }	
  }
  public boolean isReplacing() {
    return false;
  }

  static public class Replacing extends PatternWithAction {
    // Production: sig("Replacing",[arg("org.rascalmpl.ast.Expression","pattern"),arg("org.rascalmpl.ast.Replacement","replacement")])
  
    
    private final org.rascalmpl.ast.Expression pattern;
    private final org.rascalmpl.ast.Replacement replacement;
  
    public Replacing(IConstructor node , org.rascalmpl.ast.Expression pattern,  org.rascalmpl.ast.Replacement replacement) {
      super(node);
      
      this.pattern = pattern;
      this.replacement = replacement;
    }
  
    @Override
    public boolean isReplacing() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitPatternWithActionReplacing(this);
    }
  
    
    @Override
    public org.rascalmpl.ast.Expression getPattern() {
      return this.pattern;
    }
  
    @Override
    public boolean hasPattern() {
      return true;
    }
    @Override
    public org.rascalmpl.ast.Replacement getReplacement() {
      return this.replacement;
    }
  
    @Override
    public boolean hasReplacement() {
      return true;
    }	
  }
}