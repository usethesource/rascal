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
import org.eclipse.imp.pdb.facts.ISourceLocation;

public abstract class DefinedSym extends AbstractAST {
  public DefinedSym(ISourceLocation src, IConstructor node) {
    super(src /* we forget node on purpose */);
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

  static public class Default extends DefinedSym {
    // Production: sig("Default",[arg("org.rascalmpl.ast.Sym","sym")],breakable=false)
  
    
    private final org.rascalmpl.ast.Sym sym;
  
    public Default(ISourceLocation src, IConstructor node , org.rascalmpl.ast.Sym sym) {
      super(src, node);
      
      this.sym = sym;
    }
  
    @Override
    public boolean isDefault() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitDefinedSymDefault(this);
    }
  
    @Override
    protected void addForLineNumber(int $line, java.util.List<AbstractAST> $result) {
      if (getLocation().getBeginLine() == $line) {
        $result.add(this);
      }
      ISourceLocation $l;
      
      $l = sym.getLocation();
      if ($l.hasLineColumn() && $l.getBeginLine() <= $line && $l.getEndLine() >= $line) {
        sym.addForLineNumber($line, $result);
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
      return true && tmp.sym.equals(this.sym) ; 
    }
   
    @Override
    public int hashCode() {
      return 397 + 641 * sym.hashCode() ; 
    } 
  
    
    @Override
    public org.rascalmpl.ast.Sym getSym() {
      return this.sym;
    }
  
    @Override
    public boolean hasSym() {
      return true;
    }	
  
    @Override
    public Object clone()  {
      return newInstance(getClass(), src, (IConstructor) null , clone(sym));
    }
            
  }
  public boolean isDependFormals() {
    return false;
  }

  static public class DependFormals extends DefinedSym {
    // Production: sig("DependFormals",[arg("org.rascalmpl.ast.Nonterminal","nonterminal"),arg("org.rascalmpl.ast.Type","typ"),arg("org.rascalmpl.ast.Parameters","formals")],breakable=false)
  
    
    private final org.rascalmpl.ast.Nonterminal nonterminal;
    private final org.rascalmpl.ast.Type typ;
    private final org.rascalmpl.ast.Parameters formals;
  
    public DependFormals(ISourceLocation src, IConstructor node , org.rascalmpl.ast.Nonterminal nonterminal,  org.rascalmpl.ast.Type typ,  org.rascalmpl.ast.Parameters formals) {
      super(src, node);
      
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
      return visitor.visitDefinedSymDependFormals(this);
    }
  
    @Override
    protected void addForLineNumber(int $line, java.util.List<AbstractAST> $result) {
      if (getLocation().getBeginLine() == $line) {
        $result.add(this);
      }
      ISourceLocation $l;
      
      $l = nonterminal.getLocation();
      if ($l.hasLineColumn() && $l.getBeginLine() <= $line && $l.getEndLine() >= $line) {
        nonterminal.addForLineNumber($line, $result);
      }
      if ($l.getBeginLine() > $line) {
        return;
      }
      
      $l = typ.getLocation();
      if ($l.hasLineColumn() && $l.getBeginLine() <= $line && $l.getEndLine() >= $line) {
        typ.addForLineNumber($line, $result);
      }
      if ($l.getBeginLine() > $line) {
        return;
      }
      
      $l = formals.getLocation();
      if ($l.hasLineColumn() && $l.getBeginLine() <= $line && $l.getEndLine() >= $line) {
        formals.addForLineNumber($line, $result);
      }
      if ($l.getBeginLine() > $line) {
        return;
      }
      
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
      return 197 + 233 * nonterminal.hashCode() + 647 * typ.hashCode() + 157 * formals.hashCode() ; 
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
  
    @Override
    public Object clone()  {
      return newInstance(getClass(), src, (IConstructor) null , clone(nonterminal), clone(typ), clone(formals));
    }
            
  }
  public boolean isDependFormalsParametrized() {
    return false;
  }

  static public class DependFormalsParametrized extends DefinedSym {
    // Production: sig("DependFormalsParametrized",[arg("org.rascalmpl.ast.Nonterminal","nonterminal"),arg("java.util.List\<org.rascalmpl.ast.Sym\>","parameters"),arg("org.rascalmpl.ast.Type","typ"),arg("org.rascalmpl.ast.Parameters","formals")],breakable=false)
  
    
    private final org.rascalmpl.ast.Nonterminal nonterminal;
    private final java.util.List<org.rascalmpl.ast.Sym> parameters;
    private final org.rascalmpl.ast.Type typ;
    private final org.rascalmpl.ast.Parameters formals;
  
    public DependFormalsParametrized(ISourceLocation src, IConstructor node , org.rascalmpl.ast.Nonterminal nonterminal,  java.util.List<org.rascalmpl.ast.Sym> parameters,  org.rascalmpl.ast.Type typ,  org.rascalmpl.ast.Parameters formals) {
      super(src, node);
      
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
      return visitor.visitDefinedSymDependFormalsParametrized(this);
    }
  
    @Override
    protected void addForLineNumber(int $line, java.util.List<AbstractAST> $result) {
      if (getLocation().getBeginLine() == $line) {
        $result.add(this);
      }
      ISourceLocation $l;
      
      $l = nonterminal.getLocation();
      if ($l.hasLineColumn() && $l.getBeginLine() <= $line && $l.getEndLine() >= $line) {
        nonterminal.addForLineNumber($line, $result);
      }
      if ($l.getBeginLine() > $line) {
        return;
      }
      
      for (AbstractAST $elem : parameters) {
        $l = $elem.getLocation();
        if ($l.hasLineColumn() && $l.getBeginLine() <= $line && $l.getEndLine() >= $line) {
          $elem.addForLineNumber($line, $result);
        }
        if ($l.getBeginLine() > $line) {
          return;
        }
  
      }
      $l = typ.getLocation();
      if ($l.hasLineColumn() && $l.getBeginLine() <= $line && $l.getEndLine() >= $line) {
        typ.addForLineNumber($line, $result);
      }
      if ($l.getBeginLine() > $line) {
        return;
      }
      
      $l = formals.getLocation();
      if ($l.hasLineColumn() && $l.getBeginLine() <= $line && $l.getEndLine() >= $line) {
        formals.addForLineNumber($line, $result);
      }
      if ($l.getBeginLine() > $line) {
        return;
      }
      
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
      return 83 + 271 * nonterminal.hashCode() + 41 * parameters.hashCode() + 919 * typ.hashCode() + 673 * formals.hashCode() ; 
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
  
    @Override
    public Object clone()  {
      return newInstance(getClass(), src, (IConstructor) null , clone(nonterminal), clone(parameters), clone(typ), clone(formals));
    }
            
  }
  public boolean isDependVoidFormals() {
    return false;
  }

  static public class DependVoidFormals extends DefinedSym {
    // Production: sig("DependVoidFormals",[arg("org.rascalmpl.ast.Nonterminal","nonterminal"),arg("org.rascalmpl.ast.Parameters","formals")],breakable=false)
  
    
    private final org.rascalmpl.ast.Nonterminal nonterminal;
    private final org.rascalmpl.ast.Parameters formals;
  
    public DependVoidFormals(ISourceLocation src, IConstructor node , org.rascalmpl.ast.Nonterminal nonterminal,  org.rascalmpl.ast.Parameters formals) {
      super(src, node);
      
      this.nonterminal = nonterminal;
      this.formals = formals;
    }
  
    @Override
    public boolean isDependVoidFormals() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitDefinedSymDependVoidFormals(this);
    }
  
    @Override
    protected void addForLineNumber(int $line, java.util.List<AbstractAST> $result) {
      if (getLocation().getBeginLine() == $line) {
        $result.add(this);
      }
      ISourceLocation $l;
      
      $l = nonterminal.getLocation();
      if ($l.hasLineColumn() && $l.getBeginLine() <= $line && $l.getEndLine() >= $line) {
        nonterminal.addForLineNumber($line, $result);
      }
      if ($l.getBeginLine() > $line) {
        return;
      }
      
      $l = formals.getLocation();
      if ($l.hasLineColumn() && $l.getBeginLine() <= $line && $l.getEndLine() >= $line) {
        formals.addForLineNumber($line, $result);
      }
      if ($l.getBeginLine() > $line) {
        return;
      }
      
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
      return 499 + 937 * nonterminal.hashCode() + 443 * formals.hashCode() ; 
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
  
    @Override
    public Object clone()  {
      return newInstance(getClass(), src, (IConstructor) null , clone(nonterminal), clone(formals));
    }
            
  }
  public boolean isDependVoidFormalsParametrized() {
    return false;
  }

  static public class DependVoidFormalsParametrized extends DefinedSym {
    // Production: sig("DependVoidFormalsParametrized",[arg("org.rascalmpl.ast.Nonterminal","nonterminal"),arg("java.util.List\<org.rascalmpl.ast.Sym\>","parameters"),arg("org.rascalmpl.ast.Parameters","formals")],breakable=false)
  
    
    private final org.rascalmpl.ast.Nonterminal nonterminal;
    private final java.util.List<org.rascalmpl.ast.Sym> parameters;
    private final org.rascalmpl.ast.Parameters formals;
  
    public DependVoidFormalsParametrized(ISourceLocation src, IConstructor node , org.rascalmpl.ast.Nonterminal nonterminal,  java.util.List<org.rascalmpl.ast.Sym> parameters,  org.rascalmpl.ast.Parameters formals) {
      super(src, node);
      
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
      return visitor.visitDefinedSymDependVoidFormalsParametrized(this);
    }
  
    @Override
    protected void addForLineNumber(int $line, java.util.List<AbstractAST> $result) {
      if (getLocation().getBeginLine() == $line) {
        $result.add(this);
      }
      ISourceLocation $l;
      
      $l = nonterminal.getLocation();
      if ($l.hasLineColumn() && $l.getBeginLine() <= $line && $l.getEndLine() >= $line) {
        nonterminal.addForLineNumber($line, $result);
      }
      if ($l.getBeginLine() > $line) {
        return;
      }
      
      for (AbstractAST $elem : parameters) {
        $l = $elem.getLocation();
        if ($l.hasLineColumn() && $l.getBeginLine() <= $line && $l.getEndLine() >= $line) {
          $elem.addForLineNumber($line, $result);
        }
        if ($l.getBeginLine() > $line) {
          return;
        }
  
      }
      $l = formals.getLocation();
      if ($l.hasLineColumn() && $l.getBeginLine() <= $line && $l.getEndLine() >= $line) {
        formals.addForLineNumber($line, $result);
      }
      if ($l.getBeginLine() > $line) {
        return;
      }
      
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
      return 523 + 881 * nonterminal.hashCode() + 401 * parameters.hashCode() + 953 * formals.hashCode() ; 
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
  
    @Override
    public Object clone()  {
      return newInstance(getClass(), src, (IConstructor) null , clone(nonterminal), clone(parameters), clone(formals));
    }
            
  }
}