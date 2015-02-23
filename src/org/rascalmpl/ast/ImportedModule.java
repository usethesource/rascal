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

public abstract class ImportedModule extends AbstractAST {
  public ImportedModule(IConstructor node) {
    super();
  }

  
  public boolean hasActuals() {
    return false;
  }

  public org.rascalmpl.ast.ModuleActuals getActuals() {
    throw new UnsupportedOperationException();
  }
  public boolean hasName() {
    return false;
  }

  public org.rascalmpl.ast.QualifiedName getName() {
    throw new UnsupportedOperationException();
  }
  public boolean hasRenamings() {
    return false;
  }

  public org.rascalmpl.ast.Renamings getRenamings() {
    throw new UnsupportedOperationException();
  }

  

  
  public boolean isActuals() {
    return false;
  }

  static public class Actuals extends ImportedModule {
    // Production: sig("Actuals",[arg("org.rascalmpl.ast.QualifiedName","name"),arg("org.rascalmpl.ast.ModuleActuals","actuals")])
  
    
    private final org.rascalmpl.ast.QualifiedName name;
    private final org.rascalmpl.ast.ModuleActuals actuals;
  
    public Actuals(IConstructor node , org.rascalmpl.ast.QualifiedName name,  org.rascalmpl.ast.ModuleActuals actuals) {
      super(node);
      
      this.name = name;
      this.actuals = actuals;
    }
  
    @Override
    public boolean isActuals() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitImportedModuleActuals(this);
    }
  
    @Override
    public boolean equals(Object o) {
      if (!(o instanceof Actuals)) {
        return false;
      }        
      Actuals tmp = (Actuals) o;
      return true && tmp.name.equals(this.name) && tmp.actuals.equals(this.actuals) ; 
    }
   
    @Override
    public int hashCode() {
      return 709 + 433 * name.hashCode() + 739 * actuals.hashCode() ; 
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
    public org.rascalmpl.ast.ModuleActuals getActuals() {
      return this.actuals;
    }
  
    @Override
    public boolean hasActuals() {
      return true;
    }	
  }
  public boolean isActualsRenaming() {
    return false;
  }

  static public class ActualsRenaming extends ImportedModule {
    // Production: sig("ActualsRenaming",[arg("org.rascalmpl.ast.QualifiedName","name"),arg("org.rascalmpl.ast.ModuleActuals","actuals"),arg("org.rascalmpl.ast.Renamings","renamings")])
  
    
    private final org.rascalmpl.ast.QualifiedName name;
    private final org.rascalmpl.ast.ModuleActuals actuals;
    private final org.rascalmpl.ast.Renamings renamings;
  
    public ActualsRenaming(IConstructor node , org.rascalmpl.ast.QualifiedName name,  org.rascalmpl.ast.ModuleActuals actuals,  org.rascalmpl.ast.Renamings renamings) {
      super(node);
      
      this.name = name;
      this.actuals = actuals;
      this.renamings = renamings;
    }
  
    @Override
    public boolean isActualsRenaming() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitImportedModuleActualsRenaming(this);
    }
  
    @Override
    public boolean equals(Object o) {
      if (!(o instanceof ActualsRenaming)) {
        return false;
      }        
      ActualsRenaming tmp = (ActualsRenaming) o;
      return true && tmp.name.equals(this.name) && tmp.actuals.equals(this.actuals) && tmp.renamings.equals(this.renamings) ; 
    }
   
    @Override
    public int hashCode() {
      return 739 + 653 * name.hashCode() + 59 * actuals.hashCode() + 347 * renamings.hashCode() ; 
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
    public org.rascalmpl.ast.ModuleActuals getActuals() {
      return this.actuals;
    }
  
    @Override
    public boolean hasActuals() {
      return true;
    }
    @Override
    public org.rascalmpl.ast.Renamings getRenamings() {
      return this.renamings;
    }
  
    @Override
    public boolean hasRenamings() {
      return true;
    }	
  }
  public boolean isDefault() {
    return false;
  }

  static public class Default extends ImportedModule {
    // Production: sig("Default",[arg("org.rascalmpl.ast.QualifiedName","name")])
  
    
    private final org.rascalmpl.ast.QualifiedName name;
  
    public Default(IConstructor node , org.rascalmpl.ast.QualifiedName name) {
      super(node);
      
      this.name = name;
    }
  
    @Override
    public boolean isDefault() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitImportedModuleDefault(this);
    }
  
    @Override
    public boolean equals(Object o) {
      if (!(o instanceof Default)) {
        return false;
      }        
      Default tmp = (Default) o;
      return true && tmp.name.equals(this.name) ; 
    }
   
    @Override
    public int hashCode() {
      return 761 + 151 * name.hashCode() ; 
    } 
  
    
    @Override
    public org.rascalmpl.ast.QualifiedName getName() {
      return this.name;
    }
  
    @Override
    public boolean hasName() {
      return true;
    }	
  }
  public boolean isRenamings() {
    return false;
  }

  static public class Renamings extends ImportedModule {
    // Production: sig("Renamings",[arg("org.rascalmpl.ast.QualifiedName","name"),arg("org.rascalmpl.ast.Renamings","renamings")])
  
    
    private final org.rascalmpl.ast.QualifiedName name;
    private final org.rascalmpl.ast.Renamings renamings;
  
    public Renamings(IConstructor node , org.rascalmpl.ast.QualifiedName name,  org.rascalmpl.ast.Renamings renamings) {
      super(node);
      
      this.name = name;
      this.renamings = renamings;
    }
  
    @Override
    public boolean isRenamings() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitImportedModuleRenamings(this);
    }
  
    @Override
    public boolean equals(Object o) {
      if (!(o instanceof Renamings)) {
        return false;
      }        
      Renamings tmp = (Renamings) o;
      return true && tmp.name.equals(this.name) && tmp.renamings.equals(this.renamings) ; 
    }
   
    @Override
    public int hashCode() {
      return 757 + 449 * name.hashCode() + 251 * renamings.hashCode() ; 
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
    public org.rascalmpl.ast.Renamings getRenamings() {
      return this.renamings;
    }
  
    @Override
    public boolean hasRenamings() {
      return true;
    }	
  }
}