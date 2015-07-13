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

public abstract class LeftSym extends AbstractAST {
  public LeftSym(IConstructor node) {
    super();
  }

  
  public boolean hasParameters() {
    return false;
  }

  public java.util.List<org.rascalmpl.ast.Sym> getParameters() {
    throw new UnsupportedOperationException();
  }
  public boolean hasNonterminal() {
    return false;
  }

  public org.rascalmpl.ast.Nonterminal getNonterminal() {
    throw new UnsupportedOperationException();
  }
  public boolean hasFormals() {
    return false;
  }

  public org.rascalmpl.ast.Parameters getFormals() {
    throw new UnsupportedOperationException();
  }
  public boolean hasSym() {
    return false;
  }

  public org.rascalmpl.ast.Sym getSym() {
    throw new UnsupportedOperationException();
  }
  public boolean hasTyp() {
    return false;
  }

  public org.rascalmpl.ast.Type getTyp() {
    throw new UnsupportedOperationException();
  }

  

  
  public boolean isDefault() {
    return false;
  }

  static public class Default extends LeftSym {
    // Production: sig("Default",[arg("org.rascalmpl.ast.Sym","sym")])
  
    
    private final org.rascalmpl.ast.Sym sym;
  
    public Default(IConstructor node , org.rascalmpl.ast.Sym sym) {
      super(node);
      
      this.sym = sym;
    }
  
    @Override
    public boolean isDefault() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitLeftSymDefault(this);
    }
  
    @Override
    public boolean equals(Object o) {
      if (!(o instanceof Default)) {
        return false;
      }        
      Default tmp = (Default) o;
      return true && tmp.sym.equals(this.sym) ; 
    }
   
    @Override
    public int hashCode() {
      return 101 + 607 * sym.hashCode() ; 
    } 
  
    
    @Override
    public org.rascalmpl.ast.Sym getSym() {
      return this.sym;
    }
  
    @Override
    public boolean hasSym() {
      return true;
    }	
  }
  public boolean isDependFormals() {
    return false;
  }

  static public class DependFormals extends LeftSym {
    // Production: sig("DependFormals",[arg("org.rascalmpl.ast.Nonterminal","nonterminal"),arg("org.rascalmpl.ast.Type","typ"),arg("org.rascalmpl.ast.Parameters","formals")])
  
    
    private final org.rascalmpl.ast.Nonterminal nonterminal;
    private final org.rascalmpl.ast.Type typ;
    private final org.rascalmpl.ast.Parameters formals;
  
    public DependFormals(IConstructor node , org.rascalmpl.ast.Nonterminal nonterminal,  org.rascalmpl.ast.Type typ,  org.rascalmpl.ast.Parameters formals) {
      super(node);
      
      this.nonterminal = nonterminal;
      this.typ = typ;
      this.formals = formals;
    }
  
    @Override
    public boolean isDependFormals() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitLeftSymDependFormals(this);
    }
  
    @Override
    public boolean equals(Object o) {
      if (!(o instanceof DependFormals)) {
        return false;
      }        
      DependFormals tmp = (DependFormals) o;
      return true && tmp.nonterminal.equals(this.nonterminal) && tmp.typ.equals(this.typ) && tmp.formals.equals(this.formals) ; 
    }
   
    @Override
    public int hashCode() {
      return 67 + 853 * nonterminal.hashCode() + 991 * typ.hashCode() + 991 * formals.hashCode() ; 
    } 
  
    
    @Override
    public org.rascalmpl.ast.Nonterminal getNonterminal() {
      return this.nonterminal;
    }
  
    @Override
    public boolean hasNonterminal() {
      return true;
    }
    @Override
    public org.rascalmpl.ast.Type getTyp() {
      return this.typ;
    }
  
    @Override
    public boolean hasTyp() {
      return true;
    }
    @Override
    public org.rascalmpl.ast.Parameters getFormals() {
      return this.formals;
    }
  
    @Override
    public boolean hasFormals() {
      return true;
    }	
  }
  public boolean isDependFormalsParametrized() {
    return false;
  }

  static public class DependFormalsParametrized extends LeftSym {
    // Production: sig("DependFormalsParametrized",[arg("org.rascalmpl.ast.Nonterminal","nonterminal"),arg("java.util.List\<org.rascalmpl.ast.Sym\>","parameters"),arg("org.rascalmpl.ast.Type","typ"),arg("org.rascalmpl.ast.Parameters","formals")])
  
    
    private final org.rascalmpl.ast.Nonterminal nonterminal;
    private final java.util.List<org.rascalmpl.ast.Sym> parameters;
    private final org.rascalmpl.ast.Type typ;
    private final org.rascalmpl.ast.Parameters formals;
  
    public DependFormalsParametrized(IConstructor node , org.rascalmpl.ast.Nonterminal nonterminal,  java.util.List<org.rascalmpl.ast.Sym> parameters,  org.rascalmpl.ast.Type typ,  org.rascalmpl.ast.Parameters formals) {
      super(node);
      
      this.nonterminal = nonterminal;
      this.parameters = parameters;
      this.typ = typ;
      this.formals = formals;
    }
  
    @Override
    public boolean isDependFormalsParametrized() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitLeftSymDependFormalsParametrized(this);
    }
  
    @Override
    public boolean equals(Object o) {
      if (!(o instanceof DependFormalsParametrized)) {
        return false;
      }        
      DependFormalsParametrized tmp = (DependFormalsParametrized) o;
      return true && tmp.nonterminal.equals(this.nonterminal) && tmp.parameters.equals(this.parameters) && tmp.typ.equals(this.typ) && tmp.formals.equals(this.formals) ; 
    }
   
    @Override
    public int hashCode() {
      return 761 + 523 * nonterminal.hashCode() + 829 * parameters.hashCode() + 349 * typ.hashCode() + 37 * formals.hashCode() ; 
    } 
  
    
    @Override
    public org.rascalmpl.ast.Nonterminal getNonterminal() {
      return this.nonterminal;
    }
  
    @Override
    public boolean hasNonterminal() {
      return true;
    }
    @Override
    public java.util.List<org.rascalmpl.ast.Sym> getParameters() {
      return this.parameters;
    }
  
    @Override
    public boolean hasParameters() {
      return true;
    }
    @Override
    public org.rascalmpl.ast.Type getTyp() {
      return this.typ;
    }
  
    @Override
    public boolean hasTyp() {
      return true;
    }
    @Override
    public org.rascalmpl.ast.Parameters getFormals() {
      return this.formals;
    }
  
    @Override
    public boolean hasFormals() {
      return true;
    }	
  }
  public boolean isDependVoidFormals() {
    return false;
  }

  static public class DependVoidFormals extends LeftSym {
    // Production: sig("DependVoidFormals",[arg("org.rascalmpl.ast.Nonterminal","nonterminal"),arg("org.rascalmpl.ast.Parameters","formals")])
  
    
    private final org.rascalmpl.ast.Nonterminal nonterminal;
    private final org.rascalmpl.ast.Parameters formals;
  
    public DependVoidFormals(IConstructor node , org.rascalmpl.ast.Nonterminal nonterminal,  org.rascalmpl.ast.Parameters formals) {
      super(node);
      
      this.nonterminal = nonterminal;
      this.formals = formals;
    }
  
    @Override
    public boolean isDependVoidFormals() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitLeftSymDependVoidFormals(this);
    }
  
    @Override
    public boolean equals(Object o) {
      if (!(o instanceof DependVoidFormals)) {
        return false;
      }        
      DependVoidFormals tmp = (DependVoidFormals) o;
      return true && tmp.nonterminal.equals(this.nonterminal) && tmp.formals.equals(this.formals) ; 
    }
   
    @Override
    public int hashCode() {
      return 787 + 769 * nonterminal.hashCode() + 853 * formals.hashCode() ; 
    } 
  
    
    @Override
    public org.rascalmpl.ast.Nonterminal getNonterminal() {
      return this.nonterminal;
    }
  
    @Override
    public boolean hasNonterminal() {
      return true;
    }
    @Override
    public org.rascalmpl.ast.Parameters getFormals() {
      return this.formals;
    }
  
    @Override
    public boolean hasFormals() {
      return true;
    }	
  }
  public boolean isDependVoidFormalsParametrized() {
    return false;
  }

  static public class DependVoidFormalsParametrized extends LeftSym {
    // Production: sig("DependVoidFormalsParametrized",[arg("org.rascalmpl.ast.Nonterminal","nonterminal"),arg("java.util.List\<org.rascalmpl.ast.Sym\>","parameters"),arg("org.rascalmpl.ast.Parameters","formals")])
  
    
    private final org.rascalmpl.ast.Nonterminal nonterminal;
    private final java.util.List<org.rascalmpl.ast.Sym> parameters;
    private final org.rascalmpl.ast.Parameters formals;
  
    public DependVoidFormalsParametrized(IConstructor node , org.rascalmpl.ast.Nonterminal nonterminal,  java.util.List<org.rascalmpl.ast.Sym> parameters,  org.rascalmpl.ast.Parameters formals) {
      super(node);
      
      this.nonterminal = nonterminal;
      this.parameters = parameters;
      this.formals = formals;
    }
  
    @Override
    public boolean isDependVoidFormalsParametrized() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitLeftSymDependVoidFormalsParametrized(this);
    }
  
    @Override
    public boolean equals(Object o) {
      if (!(o instanceof DependVoidFormalsParametrized)) {
        return false;
      }        
      DependVoidFormalsParametrized tmp = (DependVoidFormalsParametrized) o;
      return true && tmp.nonterminal.equals(this.nonterminal) && tmp.parameters.equals(this.parameters) && tmp.formals.equals(this.formals) ; 
    }
   
    @Override
    public int hashCode() {
      return 179 + 773 * nonterminal.hashCode() + 311 * parameters.hashCode() + 829 * formals.hashCode() ; 
    } 
  
    
    @Override
    public org.rascalmpl.ast.Nonterminal getNonterminal() {
      return this.nonterminal;
    }
  
    @Override
    public boolean hasNonterminal() {
      return true;
    }
    @Override
    public java.util.List<org.rascalmpl.ast.Sym> getParameters() {
      return this.parameters;
    }
  
    @Override
    public boolean hasParameters() {
      return true;
    }
    @Override
    public org.rascalmpl.ast.Parameters getFormals() {
      return this.formals;
    }
  
    @Override
    public boolean hasFormals() {
      return true;
    }	
  }
}