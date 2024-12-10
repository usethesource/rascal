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
public abstract class Class extends AbstractAST {
  public Class(ISourceLocation src, IConstructor node) {
    super(src /* we forget node on purpose */);
  }

  
  public boolean hasRanges() {
    return false;
  }

  public java.util.List<org.rascalmpl.ast.Range> getRanges() {
    throw new UnsupportedOperationException();
  }
  public boolean hasCharClass() {
    return false;
  }

  public org.rascalmpl.ast.Class getCharClass() {
    throw new UnsupportedOperationException();
  }
  public boolean hasLhs() {
    return false;
  }

  public org.rascalmpl.ast.Class getLhs() {
    throw new UnsupportedOperationException();
  }
  public boolean hasRhs() {
    return false;
  }

  public org.rascalmpl.ast.Class getRhs() {
    throw new UnsupportedOperationException();
  }

  

  
  public boolean isBracket() {
    return false;
  }

  static public class Bracket extends Class {
    // Production: sig("Bracket",[arg("org.rascalmpl.ast.Class","charClass")],breakable=false)
  
    
    private final org.rascalmpl.ast.Class charClass;
  
    public Bracket(ISourceLocation src, IConstructor node , org.rascalmpl.ast.Class charClass) {
      super(src, node);
      
      this.charClass = charClass;
    }
  
    @Override
    public boolean isBracket() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitClassBracket(this);
    }
  
    @Override
    protected void addForLineNumber(int $line, java.util.List<AbstractAST> $result) {
      if (getLocation().getBeginLine() == $line) {
        $result.add(this);
      }
      ISourceLocation $l;
      
      $l = charClass.getLocation();
      if ($l.hasLineColumn() && $l.getBeginLine() <= $line && $l.getEndLine() >= $line) {
        charClass.addForLineNumber($line, $result);
      }
      if ($l.getBeginLine() > $line) {
        return;
      }
      
    }
  
    @Override
    public boolean equals(Object o) {
      if (!(o instanceof Bracket)) {
        return false;
      }        
      Bracket tmp = (Bracket) o;
      return true && tmp.charClass.equals(this.charClass) ; 
    }
   
    @Override
    public int hashCode() {
      return 13 + 103 * charClass.hashCode() ; 
    } 
  
    
    @Override
    public org.rascalmpl.ast.Class getCharClass() {
      return this.charClass;
    }
  
    @Override
    public boolean hasCharClass() {
      return true;
    }	
  
    @Override
    public Object clone()  {
      return newInstance(getClass(), src, (IConstructor) null , clone(charClass));
    }
            
  }
  public boolean isComplement() {
    return false;
  }

  static public class Complement extends Class {
    // Production: sig("Complement",[arg("org.rascalmpl.ast.Class","charClass")],breakable=false)
  
    
    private final org.rascalmpl.ast.Class charClass;
  
    public Complement(ISourceLocation src, IConstructor node , org.rascalmpl.ast.Class charClass) {
      super(src, node);
      
      this.charClass = charClass;
    }
  
    @Override
    public boolean isComplement() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitClassComplement(this);
    }
  
    @Override
    protected void addForLineNumber(int $line, java.util.List<AbstractAST> $result) {
      if (getLocation().getBeginLine() == $line) {
        $result.add(this);
      }
      ISourceLocation $l;
      
      $l = charClass.getLocation();
      if ($l.hasLineColumn() && $l.getBeginLine() <= $line && $l.getEndLine() >= $line) {
        charClass.addForLineNumber($line, $result);
      }
      if ($l.getBeginLine() > $line) {
        return;
      }
      
    }
  
    @Override
    public boolean equals(Object o) {
      if (!(o instanceof Complement)) {
        return false;
      }        
      Complement tmp = (Complement) o;
      return true && tmp.charClass.equals(this.charClass) ; 
    }
   
    @Override
    public int hashCode() {
      return 109 + 61 * charClass.hashCode() ; 
    } 
  
    
    @Override
    public org.rascalmpl.ast.Class getCharClass() {
      return this.charClass;
    }
  
    @Override
    public boolean hasCharClass() {
      return true;
    }	
  
    @Override
    public Object clone()  {
      return newInstance(getClass(), src, (IConstructor) null , clone(charClass));
    }
            
  }
  public boolean isDifference() {
    return false;
  }

  static public class Difference extends Class {
    // Production: sig("Difference",[arg("org.rascalmpl.ast.Class","lhs"),arg("org.rascalmpl.ast.Class","rhs")],breakable=false)
  
    
    private final org.rascalmpl.ast.Class lhs;
    private final org.rascalmpl.ast.Class rhs;
  
    public Difference(ISourceLocation src, IConstructor node , org.rascalmpl.ast.Class lhs,  org.rascalmpl.ast.Class rhs) {
      super(src, node);
      
      this.lhs = lhs;
      this.rhs = rhs;
    }
  
    @Override
    public boolean isDifference() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitClassDifference(this);
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
      if (!(o instanceof Difference)) {
        return false;
      }        
      Difference tmp = (Difference) o;
      return true && tmp.lhs.equals(this.lhs) && tmp.rhs.equals(this.rhs) ; 
    }
   
    @Override
    public int hashCode() {
      return 79 + 367 * lhs.hashCode() + 587 * rhs.hashCode() ; 
    } 
  
    
    @Override
    public org.rascalmpl.ast.Class getLhs() {
      return this.lhs;
    }
  
    @Override
    public boolean hasLhs() {
      return true;
    }
    @Override
    public org.rascalmpl.ast.Class getRhs() {
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
  public boolean isIntersection() {
    return false;
  }

  static public class Intersection extends Class {
    // Production: sig("Intersection",[arg("org.rascalmpl.ast.Class","lhs"),arg("org.rascalmpl.ast.Class","rhs")],breakable=false)
  
    
    private final org.rascalmpl.ast.Class lhs;
    private final org.rascalmpl.ast.Class rhs;
  
    public Intersection(ISourceLocation src, IConstructor node , org.rascalmpl.ast.Class lhs,  org.rascalmpl.ast.Class rhs) {
      super(src, node);
      
      this.lhs = lhs;
      this.rhs = rhs;
    }
  
    @Override
    public boolean isIntersection() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitClassIntersection(this);
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
      if (!(o instanceof Intersection)) {
        return false;
      }        
      Intersection tmp = (Intersection) o;
      return true && tmp.lhs.equals(this.lhs) && tmp.rhs.equals(this.rhs) ; 
    }
   
    @Override
    public int hashCode() {
      return 433 + 47 * lhs.hashCode() + 131 * rhs.hashCode() ; 
    } 
  
    
    @Override
    public org.rascalmpl.ast.Class getLhs() {
      return this.lhs;
    }
  
    @Override
    public boolean hasLhs() {
      return true;
    }
    @Override
    public org.rascalmpl.ast.Class getRhs() {
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
  public boolean isSimpleCharclass() {
    return false;
  }

  static public class SimpleCharclass extends Class {
    // Production: sig("SimpleCharclass",[arg("java.util.List\<org.rascalmpl.ast.Range\>","ranges")],breakable=false)
  
    
    private final java.util.List<org.rascalmpl.ast.Range> ranges;
  
    public SimpleCharclass(ISourceLocation src, IConstructor node , java.util.List<org.rascalmpl.ast.Range> ranges) {
      super(src, node);
      
      this.ranges = ranges;
    }
  
    @Override
    public boolean isSimpleCharclass() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitClassSimpleCharclass(this);
    }
  
    @Override
    protected void addForLineNumber(int $line, java.util.List<AbstractAST> $result) {
      if (getLocation().getBeginLine() == $line) {
        $result.add(this);
      }
      ISourceLocation $l;
      
      for (AbstractAST $elem : ranges) {
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
      if (!(o instanceof SimpleCharclass)) {
        return false;
      }        
      SimpleCharclass tmp = (SimpleCharclass) o;
      return true && tmp.ranges.equals(this.ranges) ; 
    }
   
    @Override
    public int hashCode() {
      return 733 + 491 * ranges.hashCode() ; 
    } 
  
    
    @Override
    public java.util.List<org.rascalmpl.ast.Range> getRanges() {
      return this.ranges;
    }
  
    @Override
    public boolean hasRanges() {
      return true;
    }	
  
    @Override
    public Object clone()  {
      return newInstance(getClass(), src, (IConstructor) null , clone(ranges));
    }
            
  }
  public boolean isUnion() {
    return false;
  }

  static public class Union extends Class {
    // Production: sig("Union",[arg("org.rascalmpl.ast.Class","lhs"),arg("org.rascalmpl.ast.Class","rhs")],breakable=false)
  
    
    private final org.rascalmpl.ast.Class lhs;
    private final org.rascalmpl.ast.Class rhs;
  
    public Union(ISourceLocation src, IConstructor node , org.rascalmpl.ast.Class lhs,  org.rascalmpl.ast.Class rhs) {
      super(src, node);
      
      this.lhs = lhs;
      this.rhs = rhs;
    }
  
    @Override
    public boolean isUnion() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitClassUnion(this);
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
      if (!(o instanceof Union)) {
        return false;
      }        
      Union tmp = (Union) o;
      return true && tmp.lhs.equals(this.lhs) && tmp.rhs.equals(this.rhs) ; 
    }
   
    @Override
    public int hashCode() {
      return 829 + 937 * lhs.hashCode() + 409 * rhs.hashCode() ; 
    } 
  
    
    @Override
    public org.rascalmpl.ast.Class getLhs() {
      return this.lhs;
    }
  
    @Override
    public boolean hasLhs() {
      return true;
    }
    @Override
    public org.rascalmpl.ast.Class getRhs() {
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
}