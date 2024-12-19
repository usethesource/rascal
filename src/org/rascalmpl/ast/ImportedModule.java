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
public abstract class ImportedModule extends AbstractAST {
  public ImportedModule(ISourceLocation src, IConstructor node) {
    super(src /* we forget node on purpose */);
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
    // Production: sig("Actuals",[arg("org.rascalmpl.ast.QualifiedName","name"),arg("org.rascalmpl.ast.ModuleActuals","actuals")],breakable=false)
  
    
    private final org.rascalmpl.ast.QualifiedName name;
    private final org.rascalmpl.ast.ModuleActuals actuals;
  
    public Actuals(ISourceLocation src, IConstructor node , org.rascalmpl.ast.QualifiedName name,  org.rascalmpl.ast.ModuleActuals actuals) {
      super(src, node);
      
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
      
      $l = actuals.getLocation();
      if ($l.hasLineColumn() && $l.getBeginLine() <= $line && $l.getEndLine() >= $line) {
        actuals.addForLineNumber($line, $result);
      }
      if ($l.getBeginLine() > $line) {
        return;
      }
      
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
      return 811 + 709 * name.hashCode() + 167 * actuals.hashCode() ; 
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
    public Object clone()  {
      return newInstance(getClass(), src, (IConstructor) null , clone(name), clone(actuals));
    }
            
  }
  public boolean isActualsRenaming() {
    return false;
  }

  static public class ActualsRenaming extends ImportedModule {
    // Production: sig("ActualsRenaming",[arg("org.rascalmpl.ast.QualifiedName","name"),arg("org.rascalmpl.ast.ModuleActuals","actuals"),arg("org.rascalmpl.ast.Renamings","renamings")],breakable=false)
  
    
    private final org.rascalmpl.ast.QualifiedName name;
    private final org.rascalmpl.ast.ModuleActuals actuals;
    private final org.rascalmpl.ast.Renamings renamings;
  
    public ActualsRenaming(ISourceLocation src, IConstructor node , org.rascalmpl.ast.QualifiedName name,  org.rascalmpl.ast.ModuleActuals actuals,  org.rascalmpl.ast.Renamings renamings) {
      super(src, node);
      
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
      
      $l = actuals.getLocation();
      if ($l.hasLineColumn() && $l.getBeginLine() <= $line && $l.getEndLine() >= $line) {
        actuals.addForLineNumber($line, $result);
      }
      if ($l.getBeginLine() > $line) {
        return;
      }
      
      $l = renamings.getLocation();
      if ($l.hasLineColumn() && $l.getBeginLine() <= $line && $l.getEndLine() >= $line) {
        renamings.addForLineNumber($line, $result);
      }
      if ($l.getBeginLine() > $line) {
        return;
      }
      
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
      return 53 + 463 * name.hashCode() + 127 * actuals.hashCode() + 67 * renamings.hashCode() ; 
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
  
    @Override
    public Object clone()  {
      return newInstance(getClass(), src, (IConstructor) null , clone(name), clone(actuals), clone(renamings));
    }
            
  }
  public boolean isDefault() {
    return false;
  }

  static public class Default extends ImportedModule {
    // Production: sig("Default",[arg("org.rascalmpl.ast.QualifiedName","name")],breakable=false)
  
    
    private final org.rascalmpl.ast.QualifiedName name;
  
    public Default(ISourceLocation src, IConstructor node , org.rascalmpl.ast.QualifiedName name) {
      super(src, node);
      
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
      return 643 + 229 * name.hashCode() ; 
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
    public Object clone()  {
      return newInstance(getClass(), src, (IConstructor) null , clone(name));
    }
            
  }
  public boolean isRenamings() {
    return false;
  }

  static public class Renamings extends ImportedModule {
    // Production: sig("Renamings",[arg("org.rascalmpl.ast.QualifiedName","name"),arg("org.rascalmpl.ast.Renamings","renamings")],breakable=false)
  
    
    private final org.rascalmpl.ast.QualifiedName name;
    private final org.rascalmpl.ast.Renamings renamings;
  
    public Renamings(ISourceLocation src, IConstructor node , org.rascalmpl.ast.QualifiedName name,  org.rascalmpl.ast.Renamings renamings) {
      super(src, node);
      
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
      
      $l = renamings.getLocation();
      if ($l.hasLineColumn() && $l.getBeginLine() <= $line && $l.getEndLine() >= $line) {
        renamings.addForLineNumber($line, $result);
      }
      if ($l.getBeginLine() > $line) {
        return;
      }
      
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
      return 271 + 331 * name.hashCode() + 17 * renamings.hashCode() ; 
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
  
    @Override
    public Object clone()  {
      return newInstance(getClass(), src, (IConstructor) null , clone(name), clone(renamings));
    }
            
  }
}