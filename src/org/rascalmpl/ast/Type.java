/*******************************************************************************
 * Copyright (c) 2009-2015 CWI
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


import io.usethesource.vallang.IConstructor;
import io.usethesource.vallang.ISourceLocation;

@SuppressWarnings(value = {"unused"})
public abstract class Type extends AbstractAST {
  public Type(ISourceLocation src, IConstructor node) {
    super(src /* we forget node on purpose */);
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

  

  
  public boolean isBasic() {
    return false;
  }

  static public class Basic extends Type {
    // Production: sig("Basic",[arg("org.rascalmpl.ast.BasicType","basic")],breakable=false)
  
    
    private final org.rascalmpl.ast.BasicType basic;
  
    public Basic(ISourceLocation src, IConstructor node , org.rascalmpl.ast.BasicType basic) {
      super(src, node);
      
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
    protected void addForLineNumber(int $line, java.util.List<AbstractAST> $result) {
      if (getLocation().getBeginLine() == $line) {
        $result.add(this);
      }
      ISourceLocation $l;
      
      $l = basic.getLocation();
      if ($l.hasLineColumn() && $l.getBeginLine() <= $line && $l.getEndLine() >= $line) {
        basic.addForLineNumber($line, $result);
      }
      if ($l.getBeginLine() > $line) {
        return;
      }
      
    }
  
    @Override
    public boolean equals(Object o) {
      if (!(o instanceof Basic)) {
        return false;
      }        
      Basic tmp = (Basic) o;
      return true && tmp.basic.equals(this.basic) ; 
    }
   
    @Override
    public int hashCode() {
      return 641 + 313 * basic.hashCode() ; 
    } 
  
    
    @Override
    public org.rascalmpl.ast.BasicType getBasic() {
      return this.basic;
    }
  
    @Override
    public boolean hasBasic() {
      return true;
    }	
  
    @Override
    public Object clone()  {
      return newInstance(getClass(), src, (IConstructor) null , clone(basic));
    }
            
  }
  public boolean isBracket() {
    return false;
  }

  static public class Bracket extends Type {
    // Production: sig("Bracket",[arg("org.rascalmpl.ast.Type","type")],breakable=false)
  
    
    private final org.rascalmpl.ast.Type type;
  
    public Bracket(ISourceLocation src, IConstructor node , org.rascalmpl.ast.Type type) {
      super(src, node);
      
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
    protected void addForLineNumber(int $line, java.util.List<AbstractAST> $result) {
      if (getLocation().getBeginLine() == $line) {
        $result.add(this);
      }
      ISourceLocation $l;
      
      $l = type.getLocation();
      if ($l.hasLineColumn() && $l.getBeginLine() <= $line && $l.getEndLine() >= $line) {
        type.addForLineNumber($line, $result);
      }
      if ($l.getBeginLine() > $line) {
        return;
      }
      
    }
  
    @Override
    public boolean equals(Object o) {
      if (!(o instanceof Bracket)) {
        return false;
      }        
      Bracket tmp = (Bracket) o;
      return true && tmp.type.equals(this.type) ; 
    }
   
    @Override
    public int hashCode() {
      return 509 + 563 * type.hashCode() ; 
    } 
  
    
    @Override
    public org.rascalmpl.ast.Type getType() {
      return this.type;
    }
  
    @Override
    public boolean hasType() {
      return true;
    }	
  
    @Override
    public Object clone()  {
      return newInstance(getClass(), src, (IConstructor) null , clone(type));
    }
            
  }
  public boolean isFunction() {
    return false;
  }

  static public class Function extends Type {
    // Production: sig("Function",[arg("org.rascalmpl.ast.FunctionType","function")],breakable=false)
  
    
    private final org.rascalmpl.ast.FunctionType function;
  
    public Function(ISourceLocation src, IConstructor node , org.rascalmpl.ast.FunctionType function) {
      super(src, node);
      
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
    protected void addForLineNumber(int $line, java.util.List<AbstractAST> $result) {
      if (getLocation().getBeginLine() == $line) {
        $result.add(this);
      }
      ISourceLocation $l;
      
      $l = function.getLocation();
      if ($l.hasLineColumn() && $l.getBeginLine() <= $line && $l.getEndLine() >= $line) {
        function.addForLineNumber($line, $result);
      }
      if ($l.getBeginLine() > $line) {
        return;
      }
      
    }
  
    @Override
    public boolean equals(Object o) {
      if (!(o instanceof Function)) {
        return false;
      }        
      Function tmp = (Function) o;
      return true && tmp.function.equals(this.function) ; 
    }
   
    @Override
    public int hashCode() {
      return 149 + 431 * function.hashCode() ; 
    } 
  
    
    @Override
    public org.rascalmpl.ast.FunctionType getFunction() {
      return this.function;
    }
  
    @Override
    public boolean hasFunction() {
      return true;
    }	
  
    @Override
    public Object clone()  {
      return newInstance(getClass(), src, (IConstructor) null , clone(function));
    }
            
  }
  public boolean isSelector() {
    return false;
  }

  static public class Selector extends Type {
    // Production: sig("Selector",[arg("org.rascalmpl.ast.DataTypeSelector","selector")],breakable=false)
  
    
    private final org.rascalmpl.ast.DataTypeSelector selector;
  
    public Selector(ISourceLocation src, IConstructor node , org.rascalmpl.ast.DataTypeSelector selector) {
      super(src, node);
      
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
    protected void addForLineNumber(int $line, java.util.List<AbstractAST> $result) {
      if (getLocation().getBeginLine() == $line) {
        $result.add(this);
      }
      ISourceLocation $l;
      
      $l = selector.getLocation();
      if ($l.hasLineColumn() && $l.getBeginLine() <= $line && $l.getEndLine() >= $line) {
        selector.addForLineNumber($line, $result);
      }
      if ($l.getBeginLine() > $line) {
        return;
      }
      
    }
  
    @Override
    public boolean equals(Object o) {
      if (!(o instanceof Selector)) {
        return false;
      }        
      Selector tmp = (Selector) o;
      return true && tmp.selector.equals(this.selector) ; 
    }
   
    @Override
    public int hashCode() {
      return 67 + 19 * selector.hashCode() ; 
    } 
  
    
    @Override
    public org.rascalmpl.ast.DataTypeSelector getSelector() {
      return this.selector;
    }
  
    @Override
    public boolean hasSelector() {
      return true;
    }	
  
    @Override
    public Object clone()  {
      return newInstance(getClass(), src, (IConstructor) null , clone(selector));
    }
            
  }
  public boolean isStructured() {
    return false;
  }

  static public class Structured extends Type {
    // Production: sig("Structured",[arg("org.rascalmpl.ast.StructuredType","structured")],breakable=false)
  
    
    private final org.rascalmpl.ast.StructuredType structured;
  
    public Structured(ISourceLocation src, IConstructor node , org.rascalmpl.ast.StructuredType structured) {
      super(src, node);
      
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
    protected void addForLineNumber(int $line, java.util.List<AbstractAST> $result) {
      if (getLocation().getBeginLine() == $line) {
        $result.add(this);
      }
      ISourceLocation $l;
      
      $l = structured.getLocation();
      if ($l.hasLineColumn() && $l.getBeginLine() <= $line && $l.getEndLine() >= $line) {
        structured.addForLineNumber($line, $result);
      }
      if ($l.getBeginLine() > $line) {
        return;
      }
      
    }
  
    @Override
    public boolean equals(Object o) {
      if (!(o instanceof Structured)) {
        return false;
      }        
      Structured tmp = (Structured) o;
      return true && tmp.structured.equals(this.structured) ; 
    }
   
    @Override
    public int hashCode() {
      return 181 + 599 * structured.hashCode() ; 
    } 
  
    
    @Override
    public org.rascalmpl.ast.StructuredType getStructured() {
      return this.structured;
    }
  
    @Override
    public boolean hasStructured() {
      return true;
    }	
  
    @Override
    public Object clone()  {
      return newInstance(getClass(), src, (IConstructor) null , clone(structured));
    }
            
  }
  public boolean isSymbol() {
    return false;
  }

  static public class Symbol extends Type {
    // Production: sig("Symbol",[arg("org.rascalmpl.ast.Sym","symbol")],breakable=false)
  
    
    private final org.rascalmpl.ast.Sym symbol;
  
    public Symbol(ISourceLocation src, IConstructor node , org.rascalmpl.ast.Sym symbol) {
      super(src, node);
      
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
    protected void addForLineNumber(int $line, java.util.List<AbstractAST> $result) {
      if (getLocation().getBeginLine() == $line) {
        $result.add(this);
      }
      ISourceLocation $l;
      
      $l = symbol.getLocation();
      if ($l.hasLineColumn() && $l.getBeginLine() <= $line && $l.getEndLine() >= $line) {
        symbol.addForLineNumber($line, $result);
      }
      if ($l.getBeginLine() > $line) {
        return;
      }
      
    }
  
    @Override
    public boolean equals(Object o) {
      if (!(o instanceof Symbol)) {
        return false;
      }        
      Symbol tmp = (Symbol) o;
      return true && tmp.symbol.equals(this.symbol) ; 
    }
   
    @Override
    public int hashCode() {
      return 101 + 421 * symbol.hashCode() ; 
    } 
  
    
    @Override
    public org.rascalmpl.ast.Sym getSymbol() {
      return this.symbol;
    }
  
    @Override
    public boolean hasSymbol() {
      return true;
    }	
  
    @Override
    public Object clone()  {
      return newInstance(getClass(), src, (IConstructor) null , clone(symbol));
    }
            
  }
  public boolean isUser() {
    return false;
  }

  static public class User extends Type {
    // Production: sig("User",[arg("org.rascalmpl.ast.UserType","user")],breakable=false)
  
    
    private final org.rascalmpl.ast.UserType user;
  
    public User(ISourceLocation src, IConstructor node , org.rascalmpl.ast.UserType user) {
      super(src, node);
      
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
    protected void addForLineNumber(int $line, java.util.List<AbstractAST> $result) {
      if (getLocation().getBeginLine() == $line) {
        $result.add(this);
      }
      ISourceLocation $l;
      
      $l = user.getLocation();
      if ($l.hasLineColumn() && $l.getBeginLine() <= $line && $l.getEndLine() >= $line) {
        user.addForLineNumber($line, $result);
      }
      if ($l.getBeginLine() > $line) {
        return;
      }
      
    }
  
    @Override
    public boolean equals(Object o) {
      if (!(o instanceof User)) {
        return false;
      }        
      User tmp = (User) o;
      return true && tmp.user.equals(this.user) ; 
    }
   
    @Override
    public int hashCode() {
      return 277 + 487 * user.hashCode() ; 
    } 
  
    
    @Override
    public org.rascalmpl.ast.UserType getUser() {
      return this.user;
    }
  
    @Override
    public boolean hasUser() {
      return true;
    }	
  
    @Override
    public Object clone()  {
      return newInstance(getClass(), src, (IConstructor) null , clone(user));
    }
            
  }
  public boolean isVariable() {
    return false;
  }

  static public class Variable extends Type {
    // Production: sig("Variable",[arg("org.rascalmpl.ast.TypeVar","typeVar")],breakable=false)
  
    
    private final org.rascalmpl.ast.TypeVar typeVar;
  
    public Variable(ISourceLocation src, IConstructor node , org.rascalmpl.ast.TypeVar typeVar) {
      super(src, node);
      
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
    protected void addForLineNumber(int $line, java.util.List<AbstractAST> $result) {
      if (getLocation().getBeginLine() == $line) {
        $result.add(this);
      }
      ISourceLocation $l;
      
      $l = typeVar.getLocation();
      if ($l.hasLineColumn() && $l.getBeginLine() <= $line && $l.getEndLine() >= $line) {
        typeVar.addForLineNumber($line, $result);
      }
      if ($l.getBeginLine() > $line) {
        return;
      }
      
    }
  
    @Override
    public boolean equals(Object o) {
      if (!(o instanceof Variable)) {
        return false;
      }        
      Variable tmp = (Variable) o;
      return true && tmp.typeVar.equals(this.typeVar) ; 
    }
   
    @Override
    public int hashCode() {
      return 797 + 787 * typeVar.hashCode() ; 
    } 
  
    
    @Override
    public org.rascalmpl.ast.TypeVar getTypeVar() {
      return this.typeVar;
    }
  
    @Override
    public boolean hasTypeVar() {
      return true;
    }	
  
    @Override
    public Object clone()  {
      return newInstance(getClass(), src, (IConstructor) null , clone(typeVar));
    }
            
  }
}