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

public abstract class ProdModifier extends AbstractAST {
  public ProdModifier(IConstructor node) {
    super();
  }

  
  public boolean hasAssociativity() {
    return false;
  }

  public org.rascalmpl.ast.Assoc getAssociativity() {
    throw new UnsupportedOperationException();
  }
  public boolean hasTag() {
    return false;
  }

  public org.rascalmpl.ast.Tag getTag() {
    throw new UnsupportedOperationException();
  }

  static public class Ambiguity extends ProdModifier {
    private final java.util.List<org.rascalmpl.ast.ProdModifier> alternatives;
    private final IConstructor node;
           
    public Ambiguity(IConstructor node, java.util.List<org.rascalmpl.ast.ProdModifier> alternatives) {
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
    
    public java.util.List<org.rascalmpl.ast.ProdModifier> getAlternatives() {
      return alternatives;
    }
    
    public <T> T accept(IASTVisitor<T> v) {
    	return v.visitProdModifierAmbiguity(this);
    }
  }

  

  
  public boolean isAssociativity() {
    return false;
  }

  static public class Associativity extends ProdModifier {
    // Production: sig("Associativity",[arg("org.rascalmpl.ast.Assoc","associativity")])
  
    
    private final org.rascalmpl.ast.Assoc associativity;
  
    public Associativity(IConstructor node , org.rascalmpl.ast.Assoc associativity) {
      super(node);
      
      this.associativity = associativity;
    }
  
    @Override
    public boolean isAssociativity() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitProdModifierAssociativity(this);
    }
  
    
    @Override
    public org.rascalmpl.ast.Assoc getAssociativity() {
      return this.associativity;
    }
  
    @Override
    public boolean hasAssociativity() {
      return true;
    }	
  }
  public boolean isBracket() {
    return false;
  }

  static public class Bracket extends ProdModifier {
    // Production: sig("Bracket",[])
  
    
  
    public Bracket(IConstructor node ) {
      super(node);
      
    }
  
    @Override
    public boolean isBracket() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitProdModifierBracket(this);
    }
  
    	
  }
  public boolean isTag() {
    return false;
  }

  static public class Tag extends ProdModifier {
    // Production: sig("Tag",[arg("org.rascalmpl.ast.Tag","tag")])
  
    
    private final org.rascalmpl.ast.Tag tag;
  
    public Tag(IConstructor node , org.rascalmpl.ast.Tag tag) {
      super(node);
      
      this.tag = tag;
    }
  
    @Override
    public boolean isTag() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitProdModifierTag(this);
    }
  
    
    @Override
    public org.rascalmpl.ast.Tag getTag() {
      return this.tag;
    }
  
    @Override
    public boolean hasTag() {
      return true;
    }	
  }
}