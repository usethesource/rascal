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
public abstract class Literal extends AbstractAST {
  public Literal(ISourceLocation src, IConstructor node) {
    super(src /* we forget node on purpose */);
  }

  
  public boolean hasBooleanLiteral() {
    return false;
  }

  public org.rascalmpl.ast.BooleanLiteral getBooleanLiteral() {
    throw new UnsupportedOperationException();
  }
  public boolean hasDateTimeLiteral() {
    return false;
  }

  public org.rascalmpl.ast.DateTimeLiteral getDateTimeLiteral() {
    throw new UnsupportedOperationException();
  }
  public boolean hasIntegerLiteral() {
    return false;
  }

  public org.rascalmpl.ast.IntegerLiteral getIntegerLiteral() {
    throw new UnsupportedOperationException();
  }
  public boolean hasLocationLiteral() {
    return false;
  }

  public org.rascalmpl.ast.LocationLiteral getLocationLiteral() {
    throw new UnsupportedOperationException();
  }
  public boolean hasRationalLiteral() {
    return false;
  }

  public org.rascalmpl.ast.RationalLiteral getRationalLiteral() {
    throw new UnsupportedOperationException();
  }
  public boolean hasRealLiteral() {
    return false;
  }

  public org.rascalmpl.ast.RealLiteral getRealLiteral() {
    throw new UnsupportedOperationException();
  }
  public boolean hasRegExpLiteral() {
    return false;
  }

  public org.rascalmpl.ast.RegExpLiteral getRegExpLiteral() {
    throw new UnsupportedOperationException();
  }
  public boolean hasStringLiteral() {
    return false;
  }

  public org.rascalmpl.ast.StringLiteral getStringLiteral() {
    throw new UnsupportedOperationException();
  }

  

  
  public boolean isBoolean() {
    return false;
  }

  static public class Boolean extends Literal {
    // Production: sig("Boolean",[arg("org.rascalmpl.ast.BooleanLiteral","booleanLiteral")],breakable=false)
  
    
    private final org.rascalmpl.ast.BooleanLiteral booleanLiteral;
  
    public Boolean(ISourceLocation src, IConstructor node , org.rascalmpl.ast.BooleanLiteral booleanLiteral) {
      super(src, node);
      
      this.booleanLiteral = booleanLiteral;
    }
  
    @Override
    public boolean isBoolean() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitLiteralBoolean(this);
    }
  
    @Override
    protected void addForLineNumber(int $line, java.util.List<AbstractAST> $result) {
      if (getLocation().getBeginLine() == $line) {
        $result.add(this);
      }
      ISourceLocation $l;
      
      $l = booleanLiteral.getLocation();
      if ($l.hasLineColumn() && $l.getBeginLine() <= $line && $l.getEndLine() >= $line) {
        booleanLiteral.addForLineNumber($line, $result);
      }
      if ($l.getBeginLine() > $line) {
        return;
      }
      
    }
  
    @Override
    public boolean equals(Object o) {
      if (!(o instanceof Boolean)) {
        return false;
      }        
      Boolean tmp = (Boolean) o;
      return true && tmp.booleanLiteral.equals(this.booleanLiteral) ; 
    }
   
    @Override
    public int hashCode() {
      return 631 + 173 * booleanLiteral.hashCode() ; 
    } 
  
    
    @Override
    public org.rascalmpl.ast.BooleanLiteral getBooleanLiteral() {
      return this.booleanLiteral;
    }
  
    @Override
    public boolean hasBooleanLiteral() {
      return true;
    }	
  
    @Override
    public Object clone()  {
      return newInstance(getClass(), src, (IConstructor) null , clone(booleanLiteral));
    }
            
  }
  public boolean isDateTime() {
    return false;
  }

  static public class DateTime extends Literal {
    // Production: sig("DateTime",[arg("org.rascalmpl.ast.DateTimeLiteral","dateTimeLiteral")],breakable=false)
  
    
    private final org.rascalmpl.ast.DateTimeLiteral dateTimeLiteral;
  
    public DateTime(ISourceLocation src, IConstructor node , org.rascalmpl.ast.DateTimeLiteral dateTimeLiteral) {
      super(src, node);
      
      this.dateTimeLiteral = dateTimeLiteral;
    }
  
    @Override
    public boolean isDateTime() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitLiteralDateTime(this);
    }
  
    @Override
    protected void addForLineNumber(int $line, java.util.List<AbstractAST> $result) {
      if (getLocation().getBeginLine() == $line) {
        $result.add(this);
      }
      ISourceLocation $l;
      
      $l = dateTimeLiteral.getLocation();
      if ($l.hasLineColumn() && $l.getBeginLine() <= $line && $l.getEndLine() >= $line) {
        dateTimeLiteral.addForLineNumber($line, $result);
      }
      if ($l.getBeginLine() > $line) {
        return;
      }
      
    }
  
    @Override
    public boolean equals(Object o) {
      if (!(o instanceof DateTime)) {
        return false;
      }        
      DateTime tmp = (DateTime) o;
      return true && tmp.dateTimeLiteral.equals(this.dateTimeLiteral) ; 
    }
   
    @Override
    public int hashCode() {
      return 103 + 257 * dateTimeLiteral.hashCode() ; 
    } 
  
    
    @Override
    public org.rascalmpl.ast.DateTimeLiteral getDateTimeLiteral() {
      return this.dateTimeLiteral;
    }
  
    @Override
    public boolean hasDateTimeLiteral() {
      return true;
    }	
  
    @Override
    public Object clone()  {
      return newInstance(getClass(), src, (IConstructor) null , clone(dateTimeLiteral));
    }
            
  }
  public boolean isInteger() {
    return false;
  }

  static public class Integer extends Literal {
    // Production: sig("Integer",[arg("org.rascalmpl.ast.IntegerLiteral","integerLiteral")],breakable=false)
  
    
    private final org.rascalmpl.ast.IntegerLiteral integerLiteral;
  
    public Integer(ISourceLocation src, IConstructor node , org.rascalmpl.ast.IntegerLiteral integerLiteral) {
      super(src, node);
      
      this.integerLiteral = integerLiteral;
    }
  
    @Override
    public boolean isInteger() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitLiteralInteger(this);
    }
  
    @Override
    protected void addForLineNumber(int $line, java.util.List<AbstractAST> $result) {
      if (getLocation().getBeginLine() == $line) {
        $result.add(this);
      }
      ISourceLocation $l;
      
      $l = integerLiteral.getLocation();
      if ($l.hasLineColumn() && $l.getBeginLine() <= $line && $l.getEndLine() >= $line) {
        integerLiteral.addForLineNumber($line, $result);
      }
      if ($l.getBeginLine() > $line) {
        return;
      }
      
    }
  
    @Override
    public boolean equals(Object o) {
      if (!(o instanceof Integer)) {
        return false;
      }        
      Integer tmp = (Integer) o;
      return true && tmp.integerLiteral.equals(this.integerLiteral) ; 
    }
   
    @Override
    public int hashCode() {
      return 283 + 521 * integerLiteral.hashCode() ; 
    } 
  
    
    @Override
    public org.rascalmpl.ast.IntegerLiteral getIntegerLiteral() {
      return this.integerLiteral;
    }
  
    @Override
    public boolean hasIntegerLiteral() {
      return true;
    }	
  
    @Override
    public Object clone()  {
      return newInstance(getClass(), src, (IConstructor) null , clone(integerLiteral));
    }
            
  }
  public boolean isLocation() {
    return false;
  }

  static public class Location extends Literal {
    // Production: sig("Location",[arg("org.rascalmpl.ast.LocationLiteral","locationLiteral")],breakable=false)
  
    
    private final org.rascalmpl.ast.LocationLiteral locationLiteral;
  
    public Location(ISourceLocation src, IConstructor node , org.rascalmpl.ast.LocationLiteral locationLiteral) {
      super(src, node);
      
      this.locationLiteral = locationLiteral;
    }
  
    @Override
    public boolean isLocation() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitLiteralLocation(this);
    }
  
    @Override
    protected void addForLineNumber(int $line, java.util.List<AbstractAST> $result) {
      if (getLocation().getBeginLine() == $line) {
        $result.add(this);
      }
      ISourceLocation $l;
      
      $l = locationLiteral.getLocation();
      if ($l.hasLineColumn() && $l.getBeginLine() <= $line && $l.getEndLine() >= $line) {
        locationLiteral.addForLineNumber($line, $result);
      }
      if ($l.getBeginLine() > $line) {
        return;
      }
      
    }
  
    @Override
    public boolean equals(Object o) {
      if (!(o instanceof Location)) {
        return false;
      }        
      Location tmp = (Location) o;
      return true && tmp.locationLiteral.equals(this.locationLiteral) ; 
    }
   
    @Override
    public int hashCode() {
      return 127 + 89 * locationLiteral.hashCode() ; 
    } 
  
    
    @Override
    public org.rascalmpl.ast.LocationLiteral getLocationLiteral() {
      return this.locationLiteral;
    }
  
    @Override
    public boolean hasLocationLiteral() {
      return true;
    }	
  
    @Override
    public Object clone()  {
      return newInstance(getClass(), src, (IConstructor) null , clone(locationLiteral));
    }
            
  }
  public boolean isRational() {
    return false;
  }

  static public class Rational extends Literal {
    // Production: sig("Rational",[arg("org.rascalmpl.ast.RationalLiteral","rationalLiteral")],breakable=false)
  
    
    private final org.rascalmpl.ast.RationalLiteral rationalLiteral;
  
    public Rational(ISourceLocation src, IConstructor node , org.rascalmpl.ast.RationalLiteral rationalLiteral) {
      super(src, node);
      
      this.rationalLiteral = rationalLiteral;
    }
  
    @Override
    public boolean isRational() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitLiteralRational(this);
    }
  
    @Override
    protected void addForLineNumber(int $line, java.util.List<AbstractAST> $result) {
      if (getLocation().getBeginLine() == $line) {
        $result.add(this);
      }
      ISourceLocation $l;
      
      $l = rationalLiteral.getLocation();
      if ($l.hasLineColumn() && $l.getBeginLine() <= $line && $l.getEndLine() >= $line) {
        rationalLiteral.addForLineNumber($line, $result);
      }
      if ($l.getBeginLine() > $line) {
        return;
      }
      
    }
  
    @Override
    public boolean equals(Object o) {
      if (!(o instanceof Rational)) {
        return false;
      }        
      Rational tmp = (Rational) o;
      return true && tmp.rationalLiteral.equals(this.rationalLiteral) ; 
    }
   
    @Override
    public int hashCode() {
      return 739 + 547 * rationalLiteral.hashCode() ; 
    } 
  
    
    @Override
    public org.rascalmpl.ast.RationalLiteral getRationalLiteral() {
      return this.rationalLiteral;
    }
  
    @Override
    public boolean hasRationalLiteral() {
      return true;
    }	
  
    @Override
    public Object clone()  {
      return newInstance(getClass(), src, (IConstructor) null , clone(rationalLiteral));
    }
            
  }
  public boolean isReal() {
    return false;
  }

  static public class Real extends Literal {
    // Production: sig("Real",[arg("org.rascalmpl.ast.RealLiteral","realLiteral")],breakable=false)
  
    
    private final org.rascalmpl.ast.RealLiteral realLiteral;
  
    public Real(ISourceLocation src, IConstructor node , org.rascalmpl.ast.RealLiteral realLiteral) {
      super(src, node);
      
      this.realLiteral = realLiteral;
    }
  
    @Override
    public boolean isReal() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitLiteralReal(this);
    }
  
    @Override
    protected void addForLineNumber(int $line, java.util.List<AbstractAST> $result) {
      if (getLocation().getBeginLine() == $line) {
        $result.add(this);
      }
      ISourceLocation $l;
      
      $l = realLiteral.getLocation();
      if ($l.hasLineColumn() && $l.getBeginLine() <= $line && $l.getEndLine() >= $line) {
        realLiteral.addForLineNumber($line, $result);
      }
      if ($l.getBeginLine() > $line) {
        return;
      }
      
    }
  
    @Override
    public boolean equals(Object o) {
      if (!(o instanceof Real)) {
        return false;
      }        
      Real tmp = (Real) o;
      return true && tmp.realLiteral.equals(this.realLiteral) ; 
    }
   
    @Override
    public int hashCode() {
      return 3 + 373 * realLiteral.hashCode() ; 
    } 
  
    
    @Override
    public org.rascalmpl.ast.RealLiteral getRealLiteral() {
      return this.realLiteral;
    }
  
    @Override
    public boolean hasRealLiteral() {
      return true;
    }	
  
    @Override
    public Object clone()  {
      return newInstance(getClass(), src, (IConstructor) null , clone(realLiteral));
    }
            
  }
  public boolean isRegExp() {
    return false;
  }

  static public class RegExp extends Literal {
    // Production: sig("RegExp",[arg("org.rascalmpl.ast.RegExpLiteral","regExpLiteral")],breakable=false)
  
    
    private final org.rascalmpl.ast.RegExpLiteral regExpLiteral;
  
    public RegExp(ISourceLocation src, IConstructor node , org.rascalmpl.ast.RegExpLiteral regExpLiteral) {
      super(src, node);
      
      this.regExpLiteral = regExpLiteral;
    }
  
    @Override
    public boolean isRegExp() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitLiteralRegExp(this);
    }
  
    @Override
    protected void addForLineNumber(int $line, java.util.List<AbstractAST> $result) {
      if (getLocation().getBeginLine() == $line) {
        $result.add(this);
      }
      ISourceLocation $l;
      
      $l = regExpLiteral.getLocation();
      if ($l.hasLineColumn() && $l.getBeginLine() <= $line && $l.getEndLine() >= $line) {
        regExpLiteral.addForLineNumber($line, $result);
      }
      if ($l.getBeginLine() > $line) {
        return;
      }
      
    }
  
    @Override
    public boolean equals(Object o) {
      if (!(o instanceof RegExp)) {
        return false;
      }        
      RegExp tmp = (RegExp) o;
      return true && tmp.regExpLiteral.equals(this.regExpLiteral) ; 
    }
   
    @Override
    public int hashCode() {
      return 31 + 283 * regExpLiteral.hashCode() ; 
    } 
  
    
    @Override
    public org.rascalmpl.ast.RegExpLiteral getRegExpLiteral() {
      return this.regExpLiteral;
    }
  
    @Override
    public boolean hasRegExpLiteral() {
      return true;
    }	
  
    @Override
    public Object clone()  {
      return newInstance(getClass(), src, (IConstructor) null , clone(regExpLiteral));
    }
            
  }
  public boolean isString() {
    return false;
  }

  static public class String extends Literal {
    // Production: sig("String",[arg("org.rascalmpl.ast.StringLiteral","stringLiteral")],breakable=false)
  
    
    private final org.rascalmpl.ast.StringLiteral stringLiteral;
  
    public String(ISourceLocation src, IConstructor node , org.rascalmpl.ast.StringLiteral stringLiteral) {
      super(src, node);
      
      this.stringLiteral = stringLiteral;
    }
  
    @Override
    public boolean isString() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitLiteralString(this);
    }
  
    @Override
    protected void addForLineNumber(int $line, java.util.List<AbstractAST> $result) {
      if (getLocation().getBeginLine() == $line) {
        $result.add(this);
      }
      ISourceLocation $l;
      
      $l = stringLiteral.getLocation();
      if ($l.hasLineColumn() && $l.getBeginLine() <= $line && $l.getEndLine() >= $line) {
        stringLiteral.addForLineNumber($line, $result);
      }
      if ($l.getBeginLine() > $line) {
        return;
      }
      
    }
  
    @Override
    public boolean equals(Object o) {
      if (!(o instanceof String)) {
        return false;
      }        
      String tmp = (String) o;
      return true && tmp.stringLiteral.equals(this.stringLiteral) ; 
    }
   
    @Override
    public int hashCode() {
      return 461 + 7 * stringLiteral.hashCode() ; 
    } 
  
    
    @Override
    public org.rascalmpl.ast.StringLiteral getStringLiteral() {
      return this.stringLiteral;
    }
  
    @Override
    public boolean hasStringLiteral() {
      return true;
    }	
  
    @Override
    public Object clone()  {
      return newInstance(getClass(), src, (IConstructor) null , clone(stringLiteral));
    }
            
  }
}