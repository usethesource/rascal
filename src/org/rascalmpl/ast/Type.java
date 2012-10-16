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

public abstract class Type extends AbstractAST {
  public Type(IConstructor node) {
    super();
  }

  
  public boolean hasBasic() {
    return false;
  }

  public org.rascalmpl.ast.BasicType getBasic() {
    throw new UnsupportedOperationException();
  }
  public boolean hasSelector() {
    return false;
  }

  public org.rascalmpl.ast.DataTypeSelector getSelector() {
    throw new UnsupportedOperationException();
  }
  public boolean hasFunction() {
    return false;
  }

  public org.rascalmpl.ast.FunctionType getFunction() {
    throw new UnsupportedOperationException();
  }
  public boolean hasStructured() {
    return false;
  }

  public org.rascalmpl.ast.StructuredType getStructured() {
    throw new UnsupportedOperationException();
  }
  public boolean hasSymbol() {
    return false;
  }

  public org.rascalmpl.ast.Sym getSymbol() {
    throw new UnsupportedOperationException();
  }
  public boolean hasType() {
    return false;
  }

  public org.rascalmpl.ast.Type getType() {
    throw new UnsupportedOperationException();
  }
  public boolean hasTypeVar() {
    return false;
  }

  public org.rascalmpl.ast.TypeVar getTypeVar() {
    throw new UnsupportedOperationException();
  }
  public boolean hasUser() {
    return false;
  }

  public org.rascalmpl.ast.UserType getUser() {
    throw new UnsupportedOperationException();
  }

  static public class Ambiguity extends Type {
    private final java.util.List<org.rascalmpl.ast.Type> alternatives;
    private final IConstructor node;
           
    public Ambiguity(IConstructor node, java.util.List<org.rascalmpl.ast.Type> alternatives) {
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
    
    public java.util.List<org.rascalmpl.ast.Type> getAlternatives() {
      return alternatives;
    }
    
    public <T> T accept(IASTVisitor<T> v) {
    	return v.visitTypeAmbiguity(this);
    }
  }

  

  
  public boolean isBasic() {
    return false;
  }

  static public class Basic extends Type {
    // Production: sig("Basic",[arg("org.rascalmpl.ast.BasicType","basic")])
  
    
    private final org.rascalmpl.ast.BasicType basic;
  
    public Basic(IConstructor node , org.rascalmpl.ast.BasicType basic) {
      super(node);
      
      this.basic = basic;
    }
  
    @Override
    public boolean isBasic() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitTypeBasic(this);
    }
  
    
    @Override
    public org.rascalmpl.ast.BasicType getBasic() {
      return this.basic;
    }
  
    @Override
    public boolean hasBasic() {
      return true;
    }	
  }
  public boolean isBracket() {
    return false;
  }

  static public class Bracket extends Type {
    // Production: sig("Bracket",[arg("org.rascalmpl.ast.Type","type")])
  
    
    private final org.rascalmpl.ast.Type type;
  
    public Bracket(IConstructor node , org.rascalmpl.ast.Type type) {
      super(node);
      
      this.type = type;
    }
  
    @Override
    public boolean isBracket() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitTypeBracket(this);
    }
  
    
    @Override
    public org.rascalmpl.ast.Type getType() {
      return this.type;
    }
  
    @Override
    public boolean hasType() {
      return true;
    }	
  }
  public boolean isFunction() {
    return false;
  }

  static public class Function extends Type {
    // Production: sig("Function",[arg("org.rascalmpl.ast.FunctionType","function")])
  
    
    private final org.rascalmpl.ast.FunctionType function;
  
    public Function(IConstructor node , org.rascalmpl.ast.FunctionType function) {
      super(node);
      
      this.function = function;
    }
  
    @Override
    public boolean isFunction() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitTypeFunction(this);
    }
  
    
    @Override
    public org.rascalmpl.ast.FunctionType getFunction() {
      return this.function;
    }
  
    @Override
    public boolean hasFunction() {
      return true;
    }	
  }
  public boolean isSelector() {
    return false;
  }

  static public class Selector extends Type {
    // Production: sig("Selector",[arg("org.rascalmpl.ast.DataTypeSelector","selector")])
  
    
    private final org.rascalmpl.ast.DataTypeSelector selector;
  
    public Selector(IConstructor node , org.rascalmpl.ast.DataTypeSelector selector) {
      super(node);
      
      this.selector = selector;
    }
  
    @Override
    public boolean isSelector() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitTypeSelector(this);
    }
  
    
    @Override
    public org.rascalmpl.ast.DataTypeSelector getSelector() {
      return this.selector;
    }
  
    @Override
    public boolean hasSelector() {
      return true;
    }	
  }
  public boolean isStructured() {
    return false;
  }

  static public class Structured extends Type {
    // Production: sig("Structured",[arg("org.rascalmpl.ast.StructuredType","structured")])
  
    
    private final org.rascalmpl.ast.StructuredType structured;
  
    public Structured(IConstructor node , org.rascalmpl.ast.StructuredType structured) {
      super(node);
      
      this.structured = structured;
    }
  
    @Override
    public boolean isStructured() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitTypeStructured(this);
    }
  
    
    @Override
    public org.rascalmpl.ast.StructuredType getStructured() {
      return this.structured;
    }
  
    @Override
    public boolean hasStructured() {
      return true;
    }	
  }
  public boolean isSymbol() {
    return false;
  }

  static public class Symbol extends Type {
    // Production: sig("Symbol",[arg("org.rascalmpl.ast.Sym","symbol")])
  
    
    private final org.rascalmpl.ast.Sym symbol;
  
    public Symbol(IConstructor node , org.rascalmpl.ast.Sym symbol) {
      super(node);
      
      this.symbol = symbol;
    }
  
    @Override
    public boolean isSymbol() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitTypeSymbol(this);
    }
  
    
    @Override
    public org.rascalmpl.ast.Sym getSymbol() {
      return this.symbol;
    }
  
    @Override
    public boolean hasSymbol() {
      return true;
    }	
  }
  public boolean isUser() {
    return false;
  }

  static public class User extends Type {
    // Production: sig("User",[arg("org.rascalmpl.ast.UserType","user")])
  
    
    private final org.rascalmpl.ast.UserType user;
  
    public User(IConstructor node , org.rascalmpl.ast.UserType user) {
      super(node);
      
      this.user = user;
    }
  
    @Override
    public boolean isUser() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitTypeUser(this);
    }
  
    
    @Override
    public org.rascalmpl.ast.UserType getUser() {
      return this.user;
    }
  
    @Override
    public boolean hasUser() {
      return true;
    }	
  }
  public boolean isVariable() {
    return false;
  }

  static public class Variable extends Type {
    // Production: sig("Variable",[arg("org.rascalmpl.ast.TypeVar","typeVar")])
  
    
    private final org.rascalmpl.ast.TypeVar typeVar;
  
    public Variable(IConstructor node , org.rascalmpl.ast.TypeVar typeVar) {
      super(node);
      
      this.typeVar = typeVar;
    }
  
    @Override
    public boolean isVariable() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitTypeVariable(this);
    }
  
    
    @Override
    public org.rascalmpl.ast.TypeVar getTypeVar() {
      return this.typeVar;
    }
  
    @Override
    public boolean hasTypeVar() {
      return true;
    }	
  }
}