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
public abstract class IntegerLiteral extends AbstractAST {
  public IntegerLiteral(ISourceLocation src, IConstructor node) {
    super(src /* we forget node on purpose */);
  }

  
  public boolean hasDecimal() {
    return false;
  }

  public org.rascalmpl.ast.DecimalIntegerLiteral getDecimal() {
    throw new UnsupportedOperationException();
  }
  public boolean hasHex() {
    return false;
  }

  public org.rascalmpl.ast.HexIntegerLiteral getHex() {
    throw new UnsupportedOperationException();
  }
  public boolean hasOctal() {
    return false;
  }

  public org.rascalmpl.ast.OctalIntegerLiteral getOctal() {
    throw new UnsupportedOperationException();
  }

  

  
  public boolean isDecimalIntegerLiteral() {
    return false;
  }

  static public class DecimalIntegerLiteral extends IntegerLiteral {
    // Production: sig("DecimalIntegerLiteral",[arg("org.rascalmpl.ast.DecimalIntegerLiteral","decimal")],breakable=false)
  
    
    private final org.rascalmpl.ast.DecimalIntegerLiteral decimal;
  
    public DecimalIntegerLiteral(ISourceLocation src, IConstructor node , org.rascalmpl.ast.DecimalIntegerLiteral decimal) {
      super(src, node);
      
      this.decimal = decimal;
    }
  
    @Override
    public boolean isDecimalIntegerLiteral() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitIntegerLiteralDecimalIntegerLiteral(this);
    }
  
    @Override
    protected void addForLineNumber(int $line, java.util.List<AbstractAST> $result) {
      if (getLocation().getBeginLine() == $line) {
        $result.add(this);
      }
      ISourceLocation $l;
      
      $l = decimal.getLocation();
      if ($l.hasLineColumn() && $l.getBeginLine() <= $line && $l.getEndLine() >= $line) {
        decimal.addForLineNumber($line, $result);
      }
      if ($l.getBeginLine() > $line) {
        return;
      }
      
    }
  
    @Override
    public boolean equals(Object o) {
      if (!(o instanceof DecimalIntegerLiteral)) {
        return false;
      }        
      DecimalIntegerLiteral tmp = (DecimalIntegerLiteral) o;
      return true && tmp.decimal.equals(this.decimal) ; 
    }
   
    @Override
    public int hashCode() {
      return 331 + 17 * decimal.hashCode() ; 
    } 
  
    
    @Override
    public org.rascalmpl.ast.DecimalIntegerLiteral getDecimal() {
      return this.decimal;
    }
  
    @Override
    public boolean hasDecimal() {
      return true;
    }	
  
    @Override
    public Object clone()  {
      return newInstance(getClass(), src, (IConstructor) null , clone(decimal));
    }
            
  }
  public boolean isHexIntegerLiteral() {
    return false;
  }

  static public class HexIntegerLiteral extends IntegerLiteral {
    // Production: sig("HexIntegerLiteral",[arg("org.rascalmpl.ast.HexIntegerLiteral","hex")],breakable=false)
  
    
    private final org.rascalmpl.ast.HexIntegerLiteral hex;
  
    public HexIntegerLiteral(ISourceLocation src, IConstructor node , org.rascalmpl.ast.HexIntegerLiteral hex) {
      super(src, node);
      
      this.hex = hex;
    }
  
    @Override
    public boolean isHexIntegerLiteral() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitIntegerLiteralHexIntegerLiteral(this);
    }
  
    @Override
    protected void addForLineNumber(int $line, java.util.List<AbstractAST> $result) {
      if (getLocation().getBeginLine() == $line) {
        $result.add(this);
      }
      ISourceLocation $l;
      
      $l = hex.getLocation();
      if ($l.hasLineColumn() && $l.getBeginLine() <= $line && $l.getEndLine() >= $line) {
        hex.addForLineNumber($line, $result);
      }
      if ($l.getBeginLine() > $line) {
        return;
      }
      
    }
  
    @Override
    public boolean equals(Object o) {
      if (!(o instanceof HexIntegerLiteral)) {
        return false;
      }        
      HexIntegerLiteral tmp = (HexIntegerLiteral) o;
      return true && tmp.hex.equals(this.hex) ; 
    }
   
    @Override
    public int hashCode() {
      return 401 + 313 * hex.hashCode() ; 
    } 
  
    
    @Override
    public org.rascalmpl.ast.HexIntegerLiteral getHex() {
      return this.hex;
    }
  
    @Override
    public boolean hasHex() {
      return true;
    }	
  
    @Override
    public Object clone()  {
      return newInstance(getClass(), src, (IConstructor) null , clone(hex));
    }
            
  }
  public boolean isOctalIntegerLiteral() {
    return false;
  }

  static public class OctalIntegerLiteral extends IntegerLiteral {
    // Production: sig("OctalIntegerLiteral",[arg("org.rascalmpl.ast.OctalIntegerLiteral","octal")],breakable=false)
  
    
    private final org.rascalmpl.ast.OctalIntegerLiteral octal;
  
    public OctalIntegerLiteral(ISourceLocation src, IConstructor node , org.rascalmpl.ast.OctalIntegerLiteral octal) {
      super(src, node);
      
      this.octal = octal;
    }
  
    @Override
    public boolean isOctalIntegerLiteral() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitIntegerLiteralOctalIntegerLiteral(this);
    }
  
    @Override
    protected void addForLineNumber(int $line, java.util.List<AbstractAST> $result) {
      if (getLocation().getBeginLine() == $line) {
        $result.add(this);
      }
      ISourceLocation $l;
      
      $l = octal.getLocation();
      if ($l.hasLineColumn() && $l.getBeginLine() <= $line && $l.getEndLine() >= $line) {
        octal.addForLineNumber($line, $result);
      }
      if ($l.getBeginLine() > $line) {
        return;
      }
      
    }
  
    @Override
    public boolean equals(Object o) {
      if (!(o instanceof OctalIntegerLiteral)) {
        return false;
      }        
      OctalIntegerLiteral tmp = (OctalIntegerLiteral) o;
      return true && tmp.octal.equals(this.octal) ; 
    }
   
    @Override
    public int hashCode() {
      return 449 + 463 * octal.hashCode() ; 
    } 
  
    
    @Override
    public org.rascalmpl.ast.OctalIntegerLiteral getOctal() {
      return this.octal;
    }
  
    @Override
    public boolean hasOctal() {
      return true;
    }	
  
    @Override
    public Object clone()  {
      return newInstance(getClass(), src, (IConstructor) null , clone(octal));
    }
            
  }
}