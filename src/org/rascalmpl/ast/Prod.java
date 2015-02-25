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

public abstract class Prod extends AbstractAST {
  public Prod(IConstructor node) {
    super();
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
    // Production: sig("All",[arg("org.rascalmpl.ast.Prod","lhs"),arg("org.rascalmpl.ast.Prod","rhs")])
  
    
    private final org.rascalmpl.ast.Prod lhs;
    private final org.rascalmpl.ast.Prod rhs;
  
    public All(IConstructor node , org.rascalmpl.ast.Prod lhs,  org.rascalmpl.ast.Prod rhs) {
      super(node);
      
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
    public boolean equals(Object o) {
      if (!(o instanceof All)) {
        return false;
      }        
      All tmp = (All) o;
      return true && tmp.lhs.equals(this.lhs) && tmp.rhs.equals(this.rhs) ; 
    }
   
    @Override
    public int hashCode() {
      return 347 + 853 * lhs.hashCode() + 431 * rhs.hashCode() ; 
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
  }
  public boolean isAssociativityGroup() {
    return false;
  }

  static public class AssociativityGroup extends Prod {
    // Production: sig("AssociativityGroup",[arg("org.rascalmpl.ast.Assoc","associativity"),arg("org.rascalmpl.ast.Prod","group")])
  
    
    private final org.rascalmpl.ast.Assoc associativity;
    private final org.rascalmpl.ast.Prod group;
  
    public AssociativityGroup(IConstructor node , org.rascalmpl.ast.Assoc associativity,  org.rascalmpl.ast.Prod group) {
      super(node);
      
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
    public boolean equals(Object o) {
      if (!(o instanceof AssociativityGroup)) {
        return false;
      }        
      AssociativityGroup tmp = (AssociativityGroup) o;
      return true && tmp.associativity.equals(this.associativity) && tmp.group.equals(this.group) ; 
    }
   
    @Override
    public int hashCode() {
      return 43 + 241 * associativity.hashCode() + 829 * group.hashCode() ; 
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
  }
  public boolean isFirst() {
    return false;
  }

  static public class First extends Prod {
    // Production: sig("First",[arg("org.rascalmpl.ast.Prod","lhs"),arg("org.rascalmpl.ast.Prod","rhs")])
  
    
    private final org.rascalmpl.ast.Prod lhs;
    private final org.rascalmpl.ast.Prod rhs;
  
    public First(IConstructor node , org.rascalmpl.ast.Prod lhs,  org.rascalmpl.ast.Prod rhs) {
      super(node);
      
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
    public boolean equals(Object o) {
      if (!(o instanceof First)) {
        return false;
      }        
      First tmp = (First) o;
      return true && tmp.lhs.equals(this.lhs) && tmp.rhs.equals(this.rhs) ; 
    }
   
    @Override
    public int hashCode() {
      return 547 + 503 * lhs.hashCode() + 149 * rhs.hashCode() ; 
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
  }
  public boolean isLabeled() {
    return false;
  }

  static public class Labeled extends Prod {
    // Production: sig("Labeled",[arg("java.util.List\<org.rascalmpl.ast.ProdModifier\>","modifiers"),arg("org.rascalmpl.ast.Name","name"),arg("java.util.List\<org.rascalmpl.ast.Sym\>","syms")])
  
    
    private final java.util.List<org.rascalmpl.ast.ProdModifier> modifiers;
    private final org.rascalmpl.ast.Name name;
    private final java.util.List<org.rascalmpl.ast.Sym> syms;
  
    public Labeled(IConstructor node , java.util.List<org.rascalmpl.ast.ProdModifier> modifiers,  org.rascalmpl.ast.Name name,  java.util.List<org.rascalmpl.ast.Sym> syms) {
      super(node);
      
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
    public boolean equals(Object o) {
      if (!(o instanceof Labeled)) {
        return false;
      }        
      Labeled tmp = (Labeled) o;
      return true && tmp.modifiers.equals(this.modifiers) && tmp.name.equals(this.name) && tmp.syms.equals(this.syms) ; 
    }
   
    @Override
    public int hashCode() {
      return 131 + 419 * modifiers.hashCode() + 827 * name.hashCode() + 757 * syms.hashCode() ; 
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
  }
  public boolean isOthers() {
    return false;
  }

  static public class Others extends Prod {
    // Production: sig("Others",[])
  
    
  
    public Others(IConstructor node ) {
      super(node);
      
    }
  
    @Override
    public boolean isOthers() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitProdOthers(this);
    }
  
    @Override
    public boolean equals(Object o) {
      if (!(o instanceof Others)) {
        return false;
      }        
      Others tmp = (Others) o;
      return true ; 
    }
   
    @Override
    public int hashCode() {
      return 31 ; 
    } 
  
    	
  }
  public boolean isReference() {
    return false;
  }

  static public class Reference extends Prod {
    // Production: sig("Reference",[arg("org.rascalmpl.ast.Name","referenced")])
  
    
    private final org.rascalmpl.ast.Name referenced;
  
    public Reference(IConstructor node , org.rascalmpl.ast.Name referenced) {
      super(node);
      
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
    public boolean equals(Object o) {
      if (!(o instanceof Reference)) {
        return false;
      }        
      Reference tmp = (Reference) o;
      return true && tmp.referenced.equals(this.referenced) ; 
    }
   
    @Override
    public int hashCode() {
      return 503 + 41 * referenced.hashCode() ; 
    } 
  
    
    @Override
    public org.rascalmpl.ast.Name getReferenced() {
      return this.referenced;
    }
  
    @Override
    public boolean hasReferenced() {
      return true;
    }	
  }
  public boolean isUnlabeled() {
    return false;
  }

  static public class Unlabeled extends Prod {
    // Production: sig("Unlabeled",[arg("java.util.List\<org.rascalmpl.ast.ProdModifier\>","modifiers"),arg("java.util.List\<org.rascalmpl.ast.Sym\>","syms")])
  
    
    private final java.util.List<org.rascalmpl.ast.ProdModifier> modifiers;
    private final java.util.List<org.rascalmpl.ast.Sym> syms;
  
    public Unlabeled(IConstructor node , java.util.List<org.rascalmpl.ast.ProdModifier> modifiers,  java.util.List<org.rascalmpl.ast.Sym> syms) {
      super(node);
      
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
    public boolean equals(Object o) {
      if (!(o instanceof Unlabeled)) {
        return false;
      }        
      Unlabeled tmp = (Unlabeled) o;
      return true && tmp.modifiers.equals(this.modifiers) && tmp.syms.equals(this.syms) ; 
    }
   
    @Override
    public int hashCode() {
      return 443 + 557 * modifiers.hashCode() + 499 * syms.hashCode() ; 
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
  }
}