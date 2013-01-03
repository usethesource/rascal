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

public abstract class DataTypeSelector extends AbstractAST {
  public DataTypeSelector(IConstructor node) {
    super();
  }

  
  public boolean hasProduction() {
    return false;
  }

  public org.rascalmpl.ast.Name getProduction() {
    throw new UnsupportedOperationException();
  }
  public boolean hasSort() {
    return false;
  }

  public org.rascalmpl.ast.QualifiedName getSort() {
    throw new UnsupportedOperationException();
  }

  static public class Ambiguity extends DataTypeSelector {
    private final java.util.List<org.rascalmpl.ast.DataTypeSelector> alternatives;
    private final IConstructor node;
           
    public Ambiguity(IConstructor node, java.util.List<org.rascalmpl.ast.DataTypeSelector> alternatives) {
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
    
    public java.util.List<org.rascalmpl.ast.DataTypeSelector> getAlternatives() {
      return alternatives;
    }
    
    public <T> T accept(IASTVisitor<T> v) {
    	return v.visitDataTypeSelectorAmbiguity(this);
    }
  }

  

  
  public boolean isSelector() {
    return false;
  }

  static public class Selector extends DataTypeSelector {
    // Production: sig("Selector",[arg("org.rascalmpl.ast.QualifiedName","sort"),arg("org.rascalmpl.ast.Name","production")])
  
    
    private final org.rascalmpl.ast.QualifiedName sort;
    private final org.rascalmpl.ast.Name production;
  
    public Selector(IConstructor node , org.rascalmpl.ast.QualifiedName sort,  org.rascalmpl.ast.Name production) {
      super(node);
      
      this.sort = sort;
      this.production = production;
    }
  
    @Override
    public boolean isSelector() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitDataTypeSelectorSelector(this);
    }
  
    
    @Override
    public org.rascalmpl.ast.QualifiedName getSort() {
      return this.sort;
    }
  
    @Override
    public boolean hasSort() {
      return true;
    }
    @Override
    public org.rascalmpl.ast.Name getProduction() {
      return this.production;
    }
  
    @Override
    public boolean hasProduction() {
      return true;
    }	
  }
}