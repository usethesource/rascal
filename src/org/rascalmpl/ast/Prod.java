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
public abstract class Prod extends AbstractAST {
  public Prod(ISourceLocation src, IConstructor node) {
    super(src /* we forget node on purpose */);
  }

  
  public boolean hasModifiers() {
    return false;
  }

  public java.util.List<org.rascalmpl.ast.ProdModifier> getModifiers() {
    throw new UnsupportedOperationException();
  }
  public boolean hasSyms() {
    return false;
  }

  public java.util.List<org.rascalmpl.ast.Sym> getSyms() {
    throw new UnsupportedOperationException();
  }
  public boolean hasAssociativity() {
    return false;
  }

  public org.rascalmpl.ast.Assoc getAssociativity() {
    throw new UnsupportedOperationException();
  }
  public boolean hasName() {
    return false;
  }

  public org.rascalmpl.ast.Name getName() {
    throw new UnsupportedOperationException();
  }
  public boolean hasReferenced() {
    return false;
  }

  public org.rascalmpl.ast.Name getReferenced() {
    throw new UnsupportedOperationException();
  }
  public boolean hasGroup() {
    return false;
  }

  public org.rascalmpl.ast.Prod getGroup() {
    throw new UnsupportedOperationException();
  }
  public boolean hasLhs() {
    return false;
  }

  public org.rascalmpl.ast.Prod getLhs() {
    throw new UnsupportedOperationException();
  }
  public boolean hasRhs() {
    return false;
  }

  public org.rascalmpl.ast.Prod getRhs() {
    throw new UnsupportedOperationException();
  }

  

  
  public boolean isAll() {
    return false;
  }

  static public class All extends Prod {
    // Production: sig("All",[arg("org.rascalmpl.ast.Prod","lhs"),arg("org.rascalmpl.ast.Prod","rhs")],breakable=false)
  
    
    private final org.rascalmpl.ast.Prod lhs;
    private final org.rascalmpl.ast.Prod rhs;
  
    public All(ISourceLocation src, IConstructor node , org.rascalmpl.ast.Prod lhs,  org.rascalmpl.ast.Prod rhs) {
      super(src, node);
      
      this.lhs = lhs;
      this.rhs = rhs;
    }
  
    @Override
    public boolean isAll() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitProdAll(this);
    }
  
    @Override
    protected void addForLineNumber(int $line, java.util.List<AbstractAST> $result) {
      if (getLocation().getBeginLine() == $line) {
        $result.add(this);
      }
      ISourceLocation $l;
      
      $l = lhs.getLocation();
      if ($l.hasLineColumn() && $l.getBeginLine() <= $line && $l.getEndLine() >= $line) {
        lhs.addForLineNumber($line, $result);
      }
      if ($l.getBeginLine() > $line) {
        return;
      }
      
      $l = rhs.getLocation();
      if ($l.hasLineColumn() && $l.getBeginLine() <= $line && $l.getEndLine() >= $line) {
        rhs.addForLineNumber($line, $result);
      }
      if ($l.getBeginLine() > $line) {
        return;
      }
      
    }
  
    @Override
    public boolean equals(Object o) {
      if (!(o instanceof All)) {
        return false;
      }        
      All tmp = (All) o;
      return true && tmp.lhs.equals(this.lhs) && tmp.rhs.equals(this.rhs) ; 
    }
   
    @Override
    public int hashCode() {
      return 757 + 449 * lhs.hashCode() + 167 * rhs.hashCode() ; 
    } 
  
    
    @Override
    public org.rascalmpl.ast.Prod getLhs() {
      return this.lhs;
    }
  
    @Override
    public boolean hasLhs() {
      return true;
    }
    @Override
    public org.rascalmpl.ast.Prod getRhs() {
      return this.rhs;
    }
  
    @Override
    public boolean hasRhs() {
      return true;
    }	
  
    @Override
    public Object clone()  {
      return newInstance(getClass(), src, (IConstructor) null , clone(lhs), clone(rhs));
    }
            
  }
  public boolean isAssociativityGroup() {
    return false;
  }

  static public class AssociativityGroup extends Prod {
    // Production: sig("AssociativityGroup",[arg("org.rascalmpl.ast.Assoc","associativity"),arg("org.rascalmpl.ast.Prod","group")],breakable=false)
  
    
    private final org.rascalmpl.ast.Assoc associativity;
    private final org.rascalmpl.ast.Prod group;
  
    public AssociativityGroup(ISourceLocation src, IConstructor node , org.rascalmpl.ast.Assoc associativity,  org.rascalmpl.ast.Prod group) {
      super(src, node);
      
      this.associativity = associativity;
      this.group = group;
    }
  
    @Override
    public boolean isAssociativityGroup() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitProdAssociativityGroup(this);
    }
  
    @Override
    protected void addForLineNumber(int $line, java.util.List<AbstractAST> $result) {
      if (getLocation().getBeginLine() == $line) {
        $result.add(this);
      }
      ISourceLocation $l;
      
      $l = associativity.getLocation();
      if ($l.hasLineColumn() && $l.getBeginLine() <= $line && $l.getEndLine() >= $line) {
        associativity.addForLineNumber($line, $result);
      }
      if ($l.getBeginLine() > $line) {
        return;
      }
      
      $l = group.getLocation();
      if ($l.hasLineColumn() && $l.getBeginLine() <= $line && $l.getEndLine() >= $line) {
        group.addForLineNumber($line, $result);
      }
      if ($l.getBeginLine() > $line) {
        return;
      }
      
    }
  
    @Override
    public boolean equals(Object o) {
      if (!(o instanceof AssociativityGroup)) {
        return false;
      }        
      AssociativityGroup tmp = (AssociativityGroup) o;
      return true && tmp.associativity.equals(this.associativity) && tmp.group.equals(this.group) ; 
    }
   
    @Override
    public int hashCode() {
      return 191 + 191 * associativity.hashCode() + 409 * group.hashCode() ; 
    } 
  
    
    @Override
    public org.rascalmpl.ast.Assoc getAssociativity() {
      return this.associativity;
    }
  
    @Override
    public boolean hasAssociativity() {
      return true;
    }
    @Override
    public org.rascalmpl.ast.Prod getGroup() {
      return this.group;
    }
  
    @Override
    public boolean hasGroup() {
      return true;
    }	
  
    @Override
    public Object clone()  {
      return newInstance(getClass(), src, (IConstructor) null , clone(associativity), clone(group));
    }
            
  }
  public boolean isFirst() {
    return false;
  }

  static public class First extends Prod {
    // Production: sig("First",[arg("org.rascalmpl.ast.Prod","lhs"),arg("org.rascalmpl.ast.Prod","rhs")],breakable=false)
  
    
    private final org.rascalmpl.ast.Prod lhs;
    private final org.rascalmpl.ast.Prod rhs;
  
    public First(ISourceLocation src, IConstructor node , org.rascalmpl.ast.Prod lhs,  org.rascalmpl.ast.Prod rhs) {
      super(src, node);
      
      this.lhs = lhs;
      this.rhs = rhs;
    }
  
    @Override
    public boolean isFirst() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitProdFirst(this);
    }
  
    @Override
    protected void addForLineNumber(int $line, java.util.List<AbstractAST> $result) {
      if (getLocation().getBeginLine() == $line) {
        $result.add(this);
      }
      ISourceLocation $l;
      
      $l = lhs.getLocation();
      if ($l.hasLineColumn() && $l.getBeginLine() <= $line && $l.getEndLine() >= $line) {
        lhs.addForLineNumber($line, $result);
      }
      if ($l.getBeginLine() > $line) {
        return;
      }
      
      $l = rhs.getLocation();
      if ($l.hasLineColumn() && $l.getBeginLine() <= $line && $l.getEndLine() >= $line) {
        rhs.addForLineNumber($line, $result);
      }
      if ($l.getBeginLine() > $line) {
        return;
      }
      
    }
  
    @Override
    public boolean equals(Object o) {
      if (!(o instanceof First)) {
        return false;
      }        
      First tmp = (First) o;
      return true && tmp.lhs.equals(this.lhs) && tmp.rhs.equals(this.rhs) ; 
    }
   
    @Override
    public int hashCode() {
      return 487 + 29 * lhs.hashCode() + 23 * rhs.hashCode() ; 
    } 
  
    
    @Override
    public org.rascalmpl.ast.Prod getLhs() {
      return this.lhs;
    }
  
    @Override
    public boolean hasLhs() {
      return true;
    }
    @Override
    public org.rascalmpl.ast.Prod getRhs() {
      return this.rhs;
    }
  
    @Override
    public boolean hasRhs() {
      return true;
    }	
  
    @Override
    public Object clone()  {
      return newInstance(getClass(), src, (IConstructor) null , clone(lhs), clone(rhs));
    }
            
  }
  public boolean isLabeled() {
    return false;
  }

  static public class Labeled extends Prod {
    // Production: sig("Labeled",[arg("java.util.List\<org.rascalmpl.ast.ProdModifier\>","modifiers"),arg("org.rascalmpl.ast.Name","name"),arg("java.util.List\<org.rascalmpl.ast.Sym\>","syms")],breakable=false)
  
    
    private final java.util.List<org.rascalmpl.ast.ProdModifier> modifiers;
    private final org.rascalmpl.ast.Name name;
    private final java.util.List<org.rascalmpl.ast.Sym> syms;
  
    public Labeled(ISourceLocation src, IConstructor node , java.util.List<org.rascalmpl.ast.ProdModifier> modifiers,  org.rascalmpl.ast.Name name,  java.util.List<org.rascalmpl.ast.Sym> syms) {
      super(src, node);
      
      this.modifiers = modifiers;
      this.name = name;
      this.syms = syms;
    }
  
    @Override
    public boolean isLabeled() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitProdLabeled(this);
    }
  
    @Override
    protected void addForLineNumber(int $line, java.util.List<AbstractAST> $result) {
      if (getLocation().getBeginLine() == $line) {
        $result.add(this);
      }
      ISourceLocation $l;
      
      for (AbstractAST $elem : modifiers) {
        $l = $elem.getLocation();
        if ($l.hasLineColumn() && $l.getBeginLine() <= $line && $l.getEndLine() >= $line) {
          $elem.addForLineNumber($line, $result);
        }
        if ($l.getBeginLine() > $line) {
          return;
        }
  
      }
      $l = name.getLocation();
      if ($l.hasLineColumn() && $l.getBeginLine() <= $line && $l.getEndLine() >= $line) {
        name.addForLineNumber($line, $result);
      }
      if ($l.getBeginLine() > $line) {
        return;
      }
      
      for (AbstractAST $elem : syms) {
        $l = $elem.getLocation();
        if ($l.hasLineColumn() && $l.getBeginLine() <= $line && $l.getEndLine() >= $line) {
          $elem.addForLineNumber($line, $result);
        }
        if ($l.getBeginLine() > $line) {
          return;
        }
  
      }
    }
  
    @Override
    public boolean equals(Object o) {
      if (!(o instanceof Labeled)) {
        return false;
      }        
      Labeled tmp = (Labeled) o;
      return true && tmp.modifiers.equals(this.modifiers) && tmp.name.equals(this.name) && tmp.syms.equals(this.syms) ; 
    }
   
    @Override
    public int hashCode() {
      return 23 + 491 * modifiers.hashCode() + 617 * name.hashCode() + 761 * syms.hashCode() ; 
    } 
  
    
    @Override
    public java.util.List<org.rascalmpl.ast.ProdModifier> getModifiers() {
      return this.modifiers;
    }
  
    @Override
    public boolean hasModifiers() {
      return true;
    }
    @Override
    public org.rascalmpl.ast.Name getName() {
      return this.name;
    }
  
    @Override
    public boolean hasName() {
      return true;
    }
    @Override
    public java.util.List<org.rascalmpl.ast.Sym> getSyms() {
      return this.syms;
    }
  
    @Override
    public boolean hasSyms() {
      return true;
    }	
  
    @Override
    public Object clone()  {
      return newInstance(getClass(), src, (IConstructor) null , clone(modifiers), clone(name), clone(syms));
    }
            
  }
  public boolean isReference() {
    return false;
  }

  static public class Reference extends Prod {
    // Production: sig("Reference",[arg("org.rascalmpl.ast.Name","referenced")],breakable=false)
  
    
    private final org.rascalmpl.ast.Name referenced;
  
    public Reference(ISourceLocation src, IConstructor node , org.rascalmpl.ast.Name referenced) {
      super(src, node);
      
      this.referenced = referenced;
    }
  
    @Override
    public boolean isReference() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitProdReference(this);
    }
  
    @Override
    protected void addForLineNumber(int $line, java.util.List<AbstractAST> $result) {
      if (getLocation().getBeginLine() == $line) {
        $result.add(this);
      }
      ISourceLocation $l;
      
      $l = referenced.getLocation();
      if ($l.hasLineColumn() && $l.getBeginLine() <= $line && $l.getEndLine() >= $line) {
        referenced.addForLineNumber($line, $result);
      }
      if ($l.getBeginLine() > $line) {
        return;
      }
      
    }
  
    @Override
    public boolean equals(Object o) {
      if (!(o instanceof Reference)) {
        return false;
      }        
      Reference tmp = (Reference) o;
      return true && tmp.referenced.equals(this.referenced) ; 
    }
   
    @Override
    public int hashCode() {
      return 197 + 5 * referenced.hashCode() ; 
    } 
  
    
    @Override
    public org.rascalmpl.ast.Name getReferenced() {
      return this.referenced;
    }
  
    @Override
    public boolean hasReferenced() {
      return true;
    }	
  
    @Override
    public Object clone()  {
      return newInstance(getClass(), src, (IConstructor) null , clone(referenced));
    }
            
  }
  public boolean isUnlabeled() {
    return false;
  }

  static public class Unlabeled extends Prod {
    // Production: sig("Unlabeled",[arg("java.util.List\<org.rascalmpl.ast.ProdModifier\>","modifiers"),arg("java.util.List\<org.rascalmpl.ast.Sym\>","syms")],breakable=false)
  
    
    private final java.util.List<org.rascalmpl.ast.ProdModifier> modifiers;
    private final java.util.List<org.rascalmpl.ast.Sym> syms;
  
    public Unlabeled(ISourceLocation src, IConstructor node , java.util.List<org.rascalmpl.ast.ProdModifier> modifiers,  java.util.List<org.rascalmpl.ast.Sym> syms) {
      super(src, node);
      
      this.modifiers = modifiers;
      this.syms = syms;
    }
  
    @Override
    public boolean isUnlabeled() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitProdUnlabeled(this);
    }
  
    @Override
    protected void addForLineNumber(int $line, java.util.List<AbstractAST> $result) {
      if (getLocation().getBeginLine() == $line) {
        $result.add(this);
      }
      ISourceLocation $l;
      
      for (AbstractAST $elem : modifiers) {
        $l = $elem.getLocation();
        if ($l.hasLineColumn() && $l.getBeginLine() <= $line && $l.getEndLine() >= $line) {
          $elem.addForLineNumber($line, $result);
        }
        if ($l.getBeginLine() > $line) {
          return;
        }
  
      }
      for (AbstractAST $elem : syms) {
        $l = $elem.getLocation();
        if ($l.hasLineColumn() && $l.getBeginLine() <= $line && $l.getEndLine() >= $line) {
          $elem.addForLineNumber($line, $result);
        }
        if ($l.getBeginLine() > $line) {
          return;
        }
  
      }
    }
  
    @Override
    public boolean equals(Object o) {
      if (!(o instanceof Unlabeled)) {
        return false;
      }        
      Unlabeled tmp = (Unlabeled) o;
      return true && tmp.modifiers.equals(this.modifiers) && tmp.syms.equals(this.syms) ; 
    }
   
    @Override
    public int hashCode() {
      return 19 + 19 * modifiers.hashCode() + 19 * syms.hashCode() ; 
    } 
  
    
    @Override
    public java.util.List<org.rascalmpl.ast.ProdModifier> getModifiers() {
      return this.modifiers;
    }
  
    @Override
    public boolean hasModifiers() {
      return true;
    }
    @Override
    public java.util.List<org.rascalmpl.ast.Sym> getSyms() {
      return this.syms;
    }
  
    @Override
    public boolean hasSyms() {
      return true;
    }	
  
    @Override
    public Object clone()  {
      return newInstance(getClass(), src, (IConstructor) null , clone(modifiers), clone(syms));
    }
            
  }
}