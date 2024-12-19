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
public abstract class Import extends AbstractAST {
  public Import(ISourceLocation src, IConstructor node) {
    super(src /* we forget node on purpose */);
  }

  
  public boolean hasModule() {
    return false;
  }

  public org.rascalmpl.ast.ImportedModule getModule() {
    throw new UnsupportedOperationException();
  }
  public boolean hasAt() {
    return false;
  }

  public org.rascalmpl.ast.LocationLiteral getAt() {
    throw new UnsupportedOperationException();
  }
  public boolean hasName() {
    return false;
  }

  public org.rascalmpl.ast.QualifiedName getName() {
    throw new UnsupportedOperationException();
  }
  public boolean hasSyntax() {
    return false;
  }

  public org.rascalmpl.ast.SyntaxDefinition getSyntax() {
    throw new UnsupportedOperationException();
  }

  

  
  public boolean isDefault() {
    return false;
  }

  static public class Default extends Import {
    // Production: sig("Default",[arg("org.rascalmpl.ast.ImportedModule","module")],breakable=false)
  
    
    private final org.rascalmpl.ast.ImportedModule module;
  
    public Default(ISourceLocation src, IConstructor node , org.rascalmpl.ast.ImportedModule module) {
      super(src, node);
      
      this.module = module;
    }
  
    @Override
    public boolean isDefault() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitImportDefault(this);
    }
  
    @Override
    protected void addForLineNumber(int $line, java.util.List<AbstractAST> $result) {
      if (getLocation().getBeginLine() == $line) {
        $result.add(this);
      }
      ISourceLocation $l;
      
      $l = module.getLocation();
      if ($l.hasLineColumn() && $l.getBeginLine() <= $line && $l.getEndLine() >= $line) {
        module.addForLineNumber($line, $result);
      }
      if ($l.getBeginLine() > $line) {
        return;
      }
      
    }
  
    @Override
    public boolean equals(Object o) {
      if (!(o instanceof Default)) {
        return false;
      }        
      Default tmp = (Default) o;
      return true && tmp.module.equals(this.module) ; 
    }
   
    @Override
    public int hashCode() {
      return 937 + 521 * module.hashCode() ; 
    } 
  
    
    @Override
    public org.rascalmpl.ast.ImportedModule getModule() {
      return this.module;
    }
  
    @Override
    public boolean hasModule() {
      return true;
    }	
  
    @Override
    public Object clone()  {
      return newInstance(getClass(), src, (IConstructor) null , clone(module));
    }
            
  }
  public boolean isExtend() {
    return false;
  }

  static public class Extend extends Import {
    // Production: sig("Extend",[arg("org.rascalmpl.ast.ImportedModule","module")],breakable=false)
  
    
    private final org.rascalmpl.ast.ImportedModule module;
  
    public Extend(ISourceLocation src, IConstructor node , org.rascalmpl.ast.ImportedModule module) {
      super(src, node);
      
      this.module = module;
    }
  
    @Override
    public boolean isExtend() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitImportExtend(this);
    }
  
    @Override
    protected void addForLineNumber(int $line, java.util.List<AbstractAST> $result) {
      if (getLocation().getBeginLine() == $line) {
        $result.add(this);
      }
      ISourceLocation $l;
      
      $l = module.getLocation();
      if ($l.hasLineColumn() && $l.getBeginLine() <= $line && $l.getEndLine() >= $line) {
        module.addForLineNumber($line, $result);
      }
      if ($l.getBeginLine() > $line) {
        return;
      }
      
    }
  
    @Override
    public boolean equals(Object o) {
      if (!(o instanceof Extend)) {
        return false;
      }        
      Extend tmp = (Extend) o;
      return true && tmp.module.equals(this.module) ; 
    }
   
    @Override
    public int hashCode() {
      return 719 + 773 * module.hashCode() ; 
    } 
  
    
    @Override
    public org.rascalmpl.ast.ImportedModule getModule() {
      return this.module;
    }
  
    @Override
    public boolean hasModule() {
      return true;
    }	
  
    @Override
    public Object clone()  {
      return newInstance(getClass(), src, (IConstructor) null , clone(module));
    }
            
  }
  public boolean isExternal() {
    return false;
  }

  static public class External extends Import {
    // Production: sig("External",[arg("org.rascalmpl.ast.QualifiedName","name"),arg("org.rascalmpl.ast.LocationLiteral","at")],breakable=false)
  
    
    private final org.rascalmpl.ast.QualifiedName name;
    private final org.rascalmpl.ast.LocationLiteral at;
  
    public External(ISourceLocation src, IConstructor node , org.rascalmpl.ast.QualifiedName name,  org.rascalmpl.ast.LocationLiteral at) {
      super(src, node);
      
      this.name = name;
      this.at = at;
    }
  
    @Override
    public boolean isExternal() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitImportExternal(this);
    }
  
    @Override
    protected void addForLineNumber(int $line, java.util.List<AbstractAST> $result) {
      if (getLocation().getBeginLine() == $line) {
        $result.add(this);
      }
      ISourceLocation $l;
      
      $l = name.getLocation();
      if ($l.hasLineColumn() && $l.getBeginLine() <= $line && $l.getEndLine() >= $line) {
        name.addForLineNumber($line, $result);
      }
      if ($l.getBeginLine() > $line) {
        return;
      }
      
      $l = at.getLocation();
      if ($l.hasLineColumn() && $l.getBeginLine() <= $line && $l.getEndLine() >= $line) {
        at.addForLineNumber($line, $result);
      }
      if ($l.getBeginLine() > $line) {
        return;
      }
      
    }
  
    @Override
    public boolean equals(Object o) {
      if (!(o instanceof External)) {
        return false;
      }        
      External tmp = (External) o;
      return true && tmp.name.equals(this.name) && tmp.at.equals(this.at) ; 
    }
   
    @Override
    public int hashCode() {
      return 277 + 239 * name.hashCode() + 151 * at.hashCode() ; 
    } 
  
    
    @Override
    public org.rascalmpl.ast.QualifiedName getName() {
      return this.name;
    }
  
    @Override
    public boolean hasName() {
      return true;
    }
    @Override
    public org.rascalmpl.ast.LocationLiteral getAt() {
      return this.at;
    }
  
    @Override
    public boolean hasAt() {
      return true;
    }	
  
    @Override
    public Object clone()  {
      return newInstance(getClass(), src, (IConstructor) null , clone(name), clone(at));
    }
            
  }
  public boolean isSyntax() {
    return false;
  }

  static public class Syntax extends Import {
    // Production: sig("Syntax",[arg("org.rascalmpl.ast.SyntaxDefinition","syntax")],breakable=false)
  
    
    private final org.rascalmpl.ast.SyntaxDefinition syntax;
  
    public Syntax(ISourceLocation src, IConstructor node , org.rascalmpl.ast.SyntaxDefinition syntax) {
      super(src, node);
      
      this.syntax = syntax;
    }
  
    @Override
    public boolean isSyntax() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitImportSyntax(this);
    }
  
    @Override
    protected void addForLineNumber(int $line, java.util.List<AbstractAST> $result) {
      if (getLocation().getBeginLine() == $line) {
        $result.add(this);
      }
      ISourceLocation $l;
      
      $l = syntax.getLocation();
      if ($l.hasLineColumn() && $l.getBeginLine() <= $line && $l.getEndLine() >= $line) {
        syntax.addForLineNumber($line, $result);
      }
      if ($l.getBeginLine() > $line) {
        return;
      }
      
    }
  
    @Override
    public boolean equals(Object o) {
      if (!(o instanceof Syntax)) {
        return false;
      }        
      Syntax tmp = (Syntax) o;
      return true && tmp.syntax.equals(this.syntax) ; 
    }
   
    @Override
    public int hashCode() {
      return 397 + 137 * syntax.hashCode() ; 
    } 
  
    
    @Override
    public org.rascalmpl.ast.SyntaxDefinition getSyntax() {
      return this.syntax;
    }
  
    @Override
    public boolean hasSyntax() {
      return true;
    }	
  
    @Override
    public Object clone()  {
      return newInstance(getClass(), src, (IConstructor) null , clone(syntax));
    }
            
  }
}