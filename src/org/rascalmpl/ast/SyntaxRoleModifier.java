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
public abstract class SyntaxRoleModifier extends AbstractAST {
  public SyntaxRoleModifier(ISourceLocation src, IConstructor node) {
    super(src /* we forget node on purpose */);
  }

  
  public boolean hasArg() {
    return false;
  }

  public org.rascalmpl.ast.TypeArg getArg() {
    throw new UnsupportedOperationException();
  }

  

  
  public boolean isData() {
    return false;
  }

  static public class Data extends SyntaxRoleModifier {
    // Production: sig("Data",[arg("org.rascalmpl.ast.TypeArg","arg")],breakable=false)
  
    
    private final org.rascalmpl.ast.TypeArg arg;
  
    public Data(ISourceLocation src, IConstructor node , org.rascalmpl.ast.TypeArg arg) {
      super(src, node);
      
      this.arg = arg;
    }
  
    @Override
    public boolean isData() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitSyntaxRoleModifierData(this);
    }
  
    @Override
    protected void addForLineNumber(int $line, java.util.List<AbstractAST> $result) {
      if (getLocation().getBeginLine() == $line) {
        $result.add(this);
      }
      ISourceLocation $l;
      
      $l = arg.getLocation();
      if ($l.hasLineColumn() && $l.getBeginLine() <= $line && $l.getEndLine() >= $line) {
        arg.addForLineNumber($line, $result);
      }
      if ($l.getBeginLine() > $line) {
        return;
      }
      
    }
  
    @Override
    public boolean equals(Object o) {
      if (!(o instanceof Data)) {
        return false;
      }        
      Data tmp = (Data) o;
      return true && tmp.arg.equals(this.arg) ; 
    }
   
    @Override
    public int hashCode() {
      return 29 + 947 * arg.hashCode() ; 
    } 
  
    
    @Override
    public org.rascalmpl.ast.TypeArg getArg() {
      return this.arg;
    }
  
    @Override
    public boolean hasArg() {
      return true;
    }	
  
    @Override
    public Object clone()  {
      return newInstance(getClass(), src, (IConstructor) null , clone(arg));
    }
            
  }
  public boolean isKeyword() {
    return false;
  }

  static public class Keyword extends SyntaxRoleModifier {
    // Production: sig("Keyword",[arg("org.rascalmpl.ast.TypeArg","arg")],breakable=false)
  
    
    private final org.rascalmpl.ast.TypeArg arg;
  
    public Keyword(ISourceLocation src, IConstructor node , org.rascalmpl.ast.TypeArg arg) {
      super(src, node);
      
      this.arg = arg;
    }
  
    @Override
    public boolean isKeyword() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitSyntaxRoleModifierKeyword(this);
    }
  
    @Override
    protected void addForLineNumber(int $line, java.util.List<AbstractAST> $result) {
      if (getLocation().getBeginLine() == $line) {
        $result.add(this);
      }
      ISourceLocation $l;
      
      $l = arg.getLocation();
      if ($l.hasLineColumn() && $l.getBeginLine() <= $line && $l.getEndLine() >= $line) {
        arg.addForLineNumber($line, $result);
      }
      if ($l.getBeginLine() > $line) {
        return;
      }
      
    }
  
    @Override
    public boolean equals(Object o) {
      if (!(o instanceof Keyword)) {
        return false;
      }        
      Keyword tmp = (Keyword) o;
      return true && tmp.arg.equals(this.arg) ; 
    }
   
    @Override
    public int hashCode() {
      return 883 + 743 * arg.hashCode() ; 
    } 
  
    
    @Override
    public org.rascalmpl.ast.TypeArg getArg() {
      return this.arg;
    }
  
    @Override
    public boolean hasArg() {
      return true;
    }	
  
    @Override
    public Object clone()  {
      return newInstance(getClass(), src, (IConstructor) null , clone(arg));
    }
            
  }
  public boolean isLayout() {
    return false;
  }

  static public class Layout extends SyntaxRoleModifier {
    // Production: sig("Layout",[arg("org.rascalmpl.ast.TypeArg","arg")],breakable=false)
  
    
    private final org.rascalmpl.ast.TypeArg arg;
  
    public Layout(ISourceLocation src, IConstructor node , org.rascalmpl.ast.TypeArg arg) {
      super(src, node);
      
      this.arg = arg;
    }
  
    @Override
    public boolean isLayout() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitSyntaxRoleModifierLayout(this);
    }
  
    @Override
    protected void addForLineNumber(int $line, java.util.List<AbstractAST> $result) {
      if (getLocation().getBeginLine() == $line) {
        $result.add(this);
      }
      ISourceLocation $l;
      
      $l = arg.getLocation();
      if ($l.hasLineColumn() && $l.getBeginLine() <= $line && $l.getEndLine() >= $line) {
        arg.addForLineNumber($line, $result);
      }
      if ($l.getBeginLine() > $line) {
        return;
      }
      
    }
  
    @Override
    public boolean equals(Object o) {
      if (!(o instanceof Layout)) {
        return false;
      }        
      Layout tmp = (Layout) o;
      return true && tmp.arg.equals(this.arg) ; 
    }
   
    @Override
    public int hashCode() {
      return 313 + 607 * arg.hashCode() ; 
    } 
  
    
    @Override
    public org.rascalmpl.ast.TypeArg getArg() {
      return this.arg;
    }
  
    @Override
    public boolean hasArg() {
      return true;
    }	
  
    @Override
    public Object clone()  {
      return newInstance(getClass(), src, (IConstructor) null , clone(arg));
    }
            
  }
  public boolean isLexical() {
    return false;
  }

  static public class Lexical extends SyntaxRoleModifier {
    // Production: sig("Lexical",[arg("org.rascalmpl.ast.TypeArg","arg")],breakable=false)
  
    
    private final org.rascalmpl.ast.TypeArg arg;
  
    public Lexical(ISourceLocation src, IConstructor node , org.rascalmpl.ast.TypeArg arg) {
      super(src, node);
      
      this.arg = arg;
    }
  
    @Override
    public boolean isLexical() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitSyntaxRoleModifierLexical(this);
    }
  
    @Override
    protected void addForLineNumber(int $line, java.util.List<AbstractAST> $result) {
      if (getLocation().getBeginLine() == $line) {
        $result.add(this);
      }
      ISourceLocation $l;
      
      $l = arg.getLocation();
      if ($l.hasLineColumn() && $l.getBeginLine() <= $line && $l.getEndLine() >= $line) {
        arg.addForLineNumber($line, $result);
      }
      if ($l.getBeginLine() > $line) {
        return;
      }
      
    }
  
    @Override
    public boolean equals(Object o) {
      if (!(o instanceof Lexical)) {
        return false;
      }        
      Lexical tmp = (Lexical) o;
      return true && tmp.arg.equals(this.arg) ; 
    }
   
    @Override
    public int hashCode() {
      return 727 + 883 * arg.hashCode() ; 
    } 
  
    
    @Override
    public org.rascalmpl.ast.TypeArg getArg() {
      return this.arg;
    }
  
    @Override
    public boolean hasArg() {
      return true;
    }	
  
    @Override
    public Object clone()  {
      return newInstance(getClass(), src, (IConstructor) null , clone(arg));
    }
            
  }
  public boolean isSyntax() {
    return false;
  }

  static public class Syntax extends SyntaxRoleModifier {
    // Production: sig("Syntax",[arg("org.rascalmpl.ast.TypeArg","arg")],breakable=false)
  
    
    private final org.rascalmpl.ast.TypeArg arg;
  
    public Syntax(ISourceLocation src, IConstructor node , org.rascalmpl.ast.TypeArg arg) {
      super(src, node);
      
      this.arg = arg;
    }
  
    @Override
    public boolean isSyntax() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitSyntaxRoleModifierSyntax(this);
    }
  
    @Override
    protected void addForLineNumber(int $line, java.util.List<AbstractAST> $result) {
      if (getLocation().getBeginLine() == $line) {
        $result.add(this);
      }
      ISourceLocation $l;
      
      $l = arg.getLocation();
      if ($l.hasLineColumn() && $l.getBeginLine() <= $line && $l.getEndLine() >= $line) {
        arg.addForLineNumber($line, $result);
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
      return true && tmp.arg.equals(this.arg) ; 
    }
   
    @Override
    public int hashCode() {
      return 617 + 233 * arg.hashCode() ; 
    } 
  
    
    @Override
    public org.rascalmpl.ast.TypeArg getArg() {
      return this.arg;
    }
  
    @Override
    public boolean hasArg() {
      return true;
    }	
  
    @Override
    public Object clone()  {
      return newInstance(getClass(), src, (IConstructor) null , clone(arg));
    }
            
  }
}