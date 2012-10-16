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

  static public class Ambiguity extends ImportedModule {
    private final java.util.List<org.rascalmpl.ast.ImportedModule> alternatives;
    private final IConstructor node;
           
    public Ambiguity(IConstructor node, java.util.List<org.rascalmpl.ast.ImportedModule> alternatives) {
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
    
    public java.util.List<org.rascalmpl.ast.ImportedModule> getAlternatives() {
      return alternatives;
    }
    
    public <T> T accept(IASTVisitor<T> v) {
    	return v.visitImportedModuleAmbiguity(this);
    }
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