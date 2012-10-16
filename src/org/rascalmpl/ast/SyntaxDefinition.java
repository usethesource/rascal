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

public abstract class SyntaxDefinition extends AbstractAST {
  public SyntaxDefinition(IConstructor node) {
    super();
  }

  
  public boolean hasProduction() {
    return false;
  }

  public org.rascalmpl.ast.Prod getProduction() {
    throw new UnsupportedOperationException();
  }
  public boolean hasStart() {
    return false;
  }

  public org.rascalmpl.ast.Start getStart() {
    throw new UnsupportedOperationException();
  }
  public boolean hasDefined() {
    return false;
  }

  public org.rascalmpl.ast.Sym getDefined() {
    throw new UnsupportedOperationException();
  }
  public boolean hasVis() {
    return false;
  }

  public org.rascalmpl.ast.Visibility getVis() {
    throw new UnsupportedOperationException();
  }

  static public class Ambiguity extends SyntaxDefinition {
    private final java.util.List<org.rascalmpl.ast.SyntaxDefinition> alternatives;
    private final IConstructor node;
           
    public Ambiguity(IConstructor node, java.util.List<org.rascalmpl.ast.SyntaxDefinition> alternatives) {
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
    
    public java.util.List<org.rascalmpl.ast.SyntaxDefinition> getAlternatives() {
      return alternatives;
    }
    
    public <T> T accept(IASTVisitor<T> v) {
    	return v.visitSyntaxDefinitionAmbiguity(this);
    }
  }

  

  
  public boolean isKeyword() {
    return false;
  }

  static public class Keyword extends SyntaxDefinition {
    // Production: sig("Keyword",[arg("org.rascalmpl.ast.Sym","defined"),arg("org.rascalmpl.ast.Prod","production")])
  
    
    private final org.rascalmpl.ast.Sym defined;
    private final org.rascalmpl.ast.Prod production;
  
    public Keyword(IConstructor node , org.rascalmpl.ast.Sym defined,  org.rascalmpl.ast.Prod production) {
      super(node);
      
      this.defined = defined;
      this.production = production;
    }
  
    @Override
    public boolean isKeyword() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitSyntaxDefinitionKeyword(this);
    }
  
    
    @Override
    public org.rascalmpl.ast.Sym getDefined() {
      return this.defined;
    }
  
    @Override
    public boolean hasDefined() {
      return true;
    }
    @Override
    public org.rascalmpl.ast.Prod getProduction() {
      return this.production;
    }
  
    @Override
    public boolean hasProduction() {
      return true;
    }	
  }
  public boolean isLanguage() {
    return false;
  }

  static public class Language extends SyntaxDefinition {
    // Production: sig("Language",[arg("org.rascalmpl.ast.Start","start"),arg("org.rascalmpl.ast.Sym","defined"),arg("org.rascalmpl.ast.Prod","production")])
  
    
    private final org.rascalmpl.ast.Start start;
    private final org.rascalmpl.ast.Sym defined;
    private final org.rascalmpl.ast.Prod production;
  
    public Language(IConstructor node , org.rascalmpl.ast.Start start,  org.rascalmpl.ast.Sym defined,  org.rascalmpl.ast.Prod production) {
      super(node);
      
      this.start = start;
      this.defined = defined;
      this.production = production;
    }
  
    @Override
    public boolean isLanguage() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitSyntaxDefinitionLanguage(this);
    }
  
    
    @Override
    public org.rascalmpl.ast.Start getStart() {
      return this.start;
    }
  
    @Override
    public boolean hasStart() {
      return true;
    }
    @Override
    public org.rascalmpl.ast.Sym getDefined() {
      return this.defined;
    }
  
    @Override
    public boolean hasDefined() {
      return true;
    }
    @Override
    public org.rascalmpl.ast.Prod getProduction() {
      return this.production;
    }
  
    @Override
    public boolean hasProduction() {
      return true;
    }	
  }
  public boolean isLayout() {
    return false;
  }

  static public class Layout extends SyntaxDefinition {
    // Production: sig("Layout",[arg("org.rascalmpl.ast.Visibility","vis"),arg("org.rascalmpl.ast.Sym","defined"),arg("org.rascalmpl.ast.Prod","production")])
  
    
    private final org.rascalmpl.ast.Visibility vis;
    private final org.rascalmpl.ast.Sym defined;
    private final org.rascalmpl.ast.Prod production;
  
    public Layout(IConstructor node , org.rascalmpl.ast.Visibility vis,  org.rascalmpl.ast.Sym defined,  org.rascalmpl.ast.Prod production) {
      super(node);
      
      this.vis = vis;
      this.defined = defined;
      this.production = production;
    }
  
    @Override
    public boolean isLayout() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitSyntaxDefinitionLayout(this);
    }
  
    
    @Override
    public org.rascalmpl.ast.Visibility getVis() {
      return this.vis;
    }
  
    @Override
    public boolean hasVis() {
      return true;
    }
    @Override
    public org.rascalmpl.ast.Sym getDefined() {
      return this.defined;
    }
  
    @Override
    public boolean hasDefined() {
      return true;
    }
    @Override
    public org.rascalmpl.ast.Prod getProduction() {
      return this.production;
    }
  
    @Override
    public boolean hasProduction() {
      return true;
    }	
  }
  public boolean isLexical() {
    return false;
  }

  static public class Lexical extends SyntaxDefinition {
    // Production: sig("Lexical",[arg("org.rascalmpl.ast.Sym","defined"),arg("org.rascalmpl.ast.Prod","production")])
  
    
    private final org.rascalmpl.ast.Sym defined;
    private final org.rascalmpl.ast.Prod production;
  
    public Lexical(IConstructor node , org.rascalmpl.ast.Sym defined,  org.rascalmpl.ast.Prod production) {
      super(node);
      
      this.defined = defined;
      this.production = production;
    }
  
    @Override
    public boolean isLexical() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitSyntaxDefinitionLexical(this);
    }
  
    
    @Override
    public org.rascalmpl.ast.Sym getDefined() {
      return this.defined;
    }
  
    @Override
    public boolean hasDefined() {
      return true;
    }
    @Override
    public org.rascalmpl.ast.Prod getProduction() {
      return this.production;
    }
  
    @Override
    public boolean hasProduction() {
      return true;
    }	
  }
}