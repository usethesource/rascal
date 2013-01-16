/*******************************************************************************
 * Copyright (c) 2009-2013 CWI
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

public abstract class KeywordFormals extends AbstractAST {
  public KeywordFormals(IConstructor node) {
    super();
  }

  
  public boolean hasKeywordFormalList() {
    return false;
  }

  public java.util.List<org.rascalmpl.ast.KeywordFormal> getKeywordFormalList() {
    throw new UnsupportedOperationException();
  }
  public boolean hasOptionalComma() {
    return false;
  }

  public org.rascalmpl.ast.OptionalComma getOptionalComma() {
    throw new UnsupportedOperationException();
  }

  static public class Ambiguity extends KeywordFormals {
    private final java.util.List<org.rascalmpl.ast.KeywordFormals> alternatives;
    private final IConstructor node;
           
    public Ambiguity(IConstructor node, java.util.List<org.rascalmpl.ast.KeywordFormals> alternatives) {
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
    
    public java.util.List<org.rascalmpl.ast.KeywordFormals> getAlternatives() {
      return alternatives;
    }
    
    public <T> T accept(IASTVisitor<T> v) {
    	return v.visitKeywordFormalsAmbiguity(this);
    }
  }

  

  
  public boolean isDefault() {
    return false;
  }

  static public class Default extends KeywordFormals {
    // Production: sig("Default",[arg("org.rascalmpl.ast.OptionalComma","optionalComma"),arg("java.util.List\<org.rascalmpl.ast.KeywordFormal\>","keywordFormalList")])
  
    
    private final org.rascalmpl.ast.OptionalComma optionalComma;
    private final java.util.List<org.rascalmpl.ast.KeywordFormal> keywordFormalList;
  
    public Default(IConstructor node , org.rascalmpl.ast.OptionalComma optionalComma,  java.util.List<org.rascalmpl.ast.KeywordFormal> keywordFormalList) {
      super(node);
      
      this.optionalComma = optionalComma;
      this.keywordFormalList = keywordFormalList;
    }
  
    @Override
    public boolean isDefault() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitKeywordFormalsDefault(this);
    }
  
    
    @Override
    public org.rascalmpl.ast.OptionalComma getOptionalComma() {
      return this.optionalComma;
    }
  
    @Override
    public boolean hasOptionalComma() {
      return true;
    }
    @Override
    public java.util.List<org.rascalmpl.ast.KeywordFormal> getKeywordFormalList() {
      return this.keywordFormalList;
    }
  
    @Override
    public boolean hasKeywordFormalList() {
      return true;
    }	
  }
  public boolean isNone() {
    return false;
  }

  static public class None extends KeywordFormals {
    // Production: sig("None",[])
  
    
  
    public None(IConstructor node ) {
      super(node);
      
    }
  
    @Override
    public boolean isNone() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitKeywordFormalsNone(this);
    }
  
    	
  }
}