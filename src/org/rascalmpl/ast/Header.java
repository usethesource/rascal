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

public abstract class Header extends AbstractAST {
  public Header(IConstructor node) {
    super();
  }

  
  public boolean hasImports() {
    return false;
  }

  public java.util.List<org.rascalmpl.ast.Import> getImports() {
    throw new UnsupportedOperationException();
  }
  public boolean hasParams() {
    return false;
  }

  public org.rascalmpl.ast.ModuleParameters getParams() {
    throw new UnsupportedOperationException();
  }
  public boolean hasName() {
    return false;
  }

  public org.rascalmpl.ast.QualifiedName getName() {
    throw new UnsupportedOperationException();
  }
  public boolean hasTags() {
    return false;
  }

  public org.rascalmpl.ast.Tags getTags() {
    throw new UnsupportedOperationException();
  }

  

  
  public boolean isDefault() {
    return false;
  }

  static public class Default extends Header {
    // Production: sig("Default",[arg("org.rascalmpl.ast.Tags","tags"),arg("org.rascalmpl.ast.QualifiedName","name"),arg("java.util.List\<org.rascalmpl.ast.Import\>","imports")])
  
    
    private final org.rascalmpl.ast.Tags tags;
    private final org.rascalmpl.ast.QualifiedName name;
    private final java.util.List<org.rascalmpl.ast.Import> imports;
  
    public Default(IConstructor node , org.rascalmpl.ast.Tags tags,  org.rascalmpl.ast.QualifiedName name,  java.util.List<org.rascalmpl.ast.Import> imports) {
      super(node);
      
      this.tags = tags;
      this.name = name;
      this.imports = imports;
    }
  
    @Override
    public boolean isDefault() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitHeaderDefault(this);
    }
  
    @Override
    public boolean equals(Object o) {
      if (!(o instanceof Default)) {
        return false;
      }        
      Default tmp = (Default) o;
      return true && tmp.tags.equals(this.tags) && tmp.name.equals(this.name) && tmp.imports.equals(this.imports) ; 
    }
   
    @Override
    public int hashCode() {
      return 457 + 563 * tags.hashCode() + 47 * name.hashCode() + 907 * imports.hashCode() ; 
    } 
  
    
    @Override
    public org.rascalmpl.ast.Tags getTags() {
      return this.tags;
    }
  
    @Override
    public boolean hasTags() {
      return true;
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
    public java.util.List<org.rascalmpl.ast.Import> getImports() {
      return this.imports;
    }
  
    @Override
    public boolean hasImports() {
      return true;
    }	
  }
  public boolean isParameters() {
    return false;
  }

  static public class Parameters extends Header {
    // Production: sig("Parameters",[arg("org.rascalmpl.ast.Tags","tags"),arg("org.rascalmpl.ast.QualifiedName","name"),arg("org.rascalmpl.ast.ModuleParameters","params"),arg("java.util.List\<org.rascalmpl.ast.Import\>","imports")])
  
    
    private final org.rascalmpl.ast.Tags tags;
    private final org.rascalmpl.ast.QualifiedName name;
    private final org.rascalmpl.ast.ModuleParameters params;
    private final java.util.List<org.rascalmpl.ast.Import> imports;
  
    public Parameters(IConstructor node , org.rascalmpl.ast.Tags tags,  org.rascalmpl.ast.QualifiedName name,  org.rascalmpl.ast.ModuleParameters params,  java.util.List<org.rascalmpl.ast.Import> imports) {
      super(node);
      
      this.tags = tags;
      this.name = name;
      this.params = params;
      this.imports = imports;
    }
  
    @Override
    public boolean isParameters() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitHeaderParameters(this);
    }
  
    @Override
    public boolean equals(Object o) {
      if (!(o instanceof Parameters)) {
        return false;
      }        
      Parameters tmp = (Parameters) o;
      return true && tmp.tags.equals(this.tags) && tmp.name.equals(this.name) && tmp.params.equals(this.params) && tmp.imports.equals(this.imports) ; 
    }
   
    @Override
    public int hashCode() {
      return 503 + 419 * tags.hashCode() + 991 * name.hashCode() + 487 * params.hashCode() + 337 * imports.hashCode() ; 
    } 
  
    
    @Override
    public org.rascalmpl.ast.Tags getTags() {
      return this.tags;
    }
  
    @Override
    public boolean hasTags() {
      return true;
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
    public org.rascalmpl.ast.ModuleParameters getParams() {
      return this.params;
    }
  
    @Override
    public boolean hasParams() {
      return true;
    }
    @Override
    public java.util.List<org.rascalmpl.ast.Import> getImports() {
      return this.imports;
    }
  
    @Override
    public boolean hasImports() {
      return true;
    }	
  }
}