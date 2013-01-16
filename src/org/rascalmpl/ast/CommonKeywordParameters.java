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

public abstract class CommonKeywordParameters extends AbstractAST {
  public CommonKeywordParameters(IConstructor node) {
    super();
  }

  
  public boolean hasKeywordFormalList() {
    return false;
  }

  public java.util.List<org.rascalmpl.ast.KeywordFormal> getKeywordFormalList() {
    throw new UnsupportedOperationException();
  }

  static public class Ambiguity extends CommonKeywordParameters {
    private final java.util.List<org.rascalmpl.ast.CommonKeywordParameters> alternatives;
    private final IConstructor node;
           
    public Ambiguity(IConstructor node, java.util.List<org.rascalmpl.ast.CommonKeywordParameters> alternatives) {
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
    
    public java.util.List<org.rascalmpl.ast.CommonKeywordParameters> getAlternatives() {
      return alternatives;
    }
    
    public <T> T accept(IASTVisitor<T> v) {
    	return v.visitCommonKeywordParametersAmbiguity(this);
    }
  }

  

  
  public boolean isAbsent() {
    return false;
  }

  static public class Absent extends CommonKeywordParameters {
    // Production: sig("Absent",[])
  
    
  
    public Absent(IConstructor node ) {
      super(node);
      
    }
  
    @Override
    public boolean isAbsent() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitCommonKeywordParametersAbsent(this);
    }
  
    	
  }
  public boolean isPresent() {
    return false;
  }

  static public class Present extends CommonKeywordParameters {
    // Production: sig("Present",[arg("java.util.List\<org.rascalmpl.ast.KeywordFormal\>","keywordFormalList")])
  
    
    private final java.util.List<org.rascalmpl.ast.KeywordFormal> keywordFormalList;
  
    public Present(IConstructor node , java.util.List<org.rascalmpl.ast.KeywordFormal> keywordFormalList) {
      super(node);
      
      this.keywordFormalList = keywordFormalList;
    }
  
    @Override
    public boolean isPresent() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitCommonKeywordParametersPresent(this);
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
}