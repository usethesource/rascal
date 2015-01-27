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


import org.eclipse.imp.pdb.facts.IConstructor;

public abstract class Import extends AbstractAST {
  public Import(IConstructor node) {
    super();
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
    // Production: sig("Default",[arg("org.rascalmpl.ast.ImportedModule","module")])
  
    
    private final org.rascalmpl.ast.ImportedModule module;
  
    public Default(IConstructor node , org.rascalmpl.ast.ImportedModule module) {
      super(node);
      
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
    public boolean equals(Object o) {
      if (!(o instanceof Default)) {
        return false;
      }        
      Default tmp = (Default) o;
      return true && tmp.module.equals(this.module) ; 
    }
   
    @Override
    public int hashCode() {
      return 461 + 193 * module.hashCode() ; 
    } 
  
    
    @Override
    public org.rascalmpl.ast.ImportedModule getModule() {
      return this.module;
    }
  
    @Override
    public boolean hasModule() {
      return true;
    }	
  }
  public boolean isExtend() {
    return false;
  }

  static public class Extend extends Import {
    // Production: sig("Extend",[arg("org.rascalmpl.ast.ImportedModule","module")])
  
    
    private final org.rascalmpl.ast.ImportedModule module;
  
    public Extend(IConstructor node , org.rascalmpl.ast.ImportedModule module) {
      super(node);
      
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
    public boolean equals(Object o) {
      if (!(o instanceof Extend)) {
        return false;
      }        
      Extend tmp = (Extend) o;
      return true && tmp.module.equals(this.module) ; 
    }
   
    @Override
    public int hashCode() {
      return 13 + 727 * module.hashCode() ; 
    } 
  
    
    @Override
    public org.rascalmpl.ast.ImportedModule getModule() {
      return this.module;
    }
  
    @Override
    public boolean hasModule() {
      return true;
    }	
  }
  public boolean isExternal() {
    return false;
  }

  static public class External extends Import {
    // Production: sig("External",[arg("org.rascalmpl.ast.QualifiedName","name"),arg("org.rascalmpl.ast.LocationLiteral","at")])
  
    
    private final org.rascalmpl.ast.QualifiedName name;
    private final org.rascalmpl.ast.LocationLiteral at;
  
    public External(IConstructor node , org.rascalmpl.ast.QualifiedName name,  org.rascalmpl.ast.LocationLiteral at) {
      super(node);
      
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
    public boolean equals(Object o) {
      if (!(o instanceof External)) {
        return false;
      }        
      External tmp = (External) o;
      return true && tmp.name.equals(this.name) && tmp.at.equals(this.at) ; 
    }
   
    @Override
    public int hashCode() {
      return 433 + 463 * name.hashCode() + 607 * at.hashCode() ; 
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
  }
  public boolean isSyntax() {
    return false;
  }

  static public class Syntax extends Import {
    // Production: sig("Syntax",[arg("org.rascalmpl.ast.SyntaxDefinition","syntax")])
  
    
    private final org.rascalmpl.ast.SyntaxDefinition syntax;
  
    public Syntax(IConstructor node , org.rascalmpl.ast.SyntaxDefinition syntax) {
      super(node);
      
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
    public boolean equals(Object o) {
      if (!(o instanceof Syntax)) {
        return false;
      }        
      Syntax tmp = (Syntax) o;
      return true && tmp.syntax.equals(this.syntax) ; 
    }
   
    @Override
    public int hashCode() {
      return 751 + 139 * syntax.hashCode() ; 
    } 
  
    
    @Override
    public org.rascalmpl.ast.SyntaxDefinition getSyntax() {
      return this.syntax;
    }
  
    @Override
    public boolean hasSyntax() {
      return true;
    }	
  }
}