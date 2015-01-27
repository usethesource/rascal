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

public abstract class IntegerLiteral extends AbstractAST {
  public IntegerLiteral(IConstructor node) {
    super();
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
    // Production: sig("DecimalIntegerLiteral",[arg("org.rascalmpl.ast.DecimalIntegerLiteral","decimal")])
  
    
    private final org.rascalmpl.ast.DecimalIntegerLiteral decimal;
  
    public DecimalIntegerLiteral(IConstructor node , org.rascalmpl.ast.DecimalIntegerLiteral decimal) {
      super(node);
      
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
    public boolean equals(Object o) {
      if (!(o instanceof DecimalIntegerLiteral)) {
        return false;
      }        
      DecimalIntegerLiteral tmp = (DecimalIntegerLiteral) o;
      return true && tmp.decimal.equals(this.decimal) ; 
    }
   
    @Override
    public int hashCode() {
      return 83 + 7 * decimal.hashCode() ; 
    } 
  
    
    @Override
    public org.rascalmpl.ast.DecimalIntegerLiteral getDecimal() {
      return this.decimal;
    }
  
    @Override
    public boolean hasDecimal() {
      return true;
    }	
  }
  public boolean isHexIntegerLiteral() {
    return false;
  }

  static public class HexIntegerLiteral extends IntegerLiteral {
    // Production: sig("HexIntegerLiteral",[arg("org.rascalmpl.ast.HexIntegerLiteral","hex")])
  
    
    private final org.rascalmpl.ast.HexIntegerLiteral hex;
  
    public HexIntegerLiteral(IConstructor node , org.rascalmpl.ast.HexIntegerLiteral hex) {
      super(node);
      
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
    public boolean equals(Object o) {
      if (!(o instanceof HexIntegerLiteral)) {
        return false;
      }        
      HexIntegerLiteral tmp = (HexIntegerLiteral) o;
      return true && tmp.hex.equals(this.hex) ; 
    }
   
    @Override
    public int hashCode() {
      return 569 + 827 * hex.hashCode() ; 
    } 
  
    
    @Override
    public org.rascalmpl.ast.HexIntegerLiteral getHex() {
      return this.hex;
    }
  
    @Override
    public boolean hasHex() {
      return true;
    }	
  }
  public boolean isOctalIntegerLiteral() {
    return false;
  }

  static public class OctalIntegerLiteral extends IntegerLiteral {
    // Production: sig("OctalIntegerLiteral",[arg("org.rascalmpl.ast.OctalIntegerLiteral","octal")])
  
    
    private final org.rascalmpl.ast.OctalIntegerLiteral octal;
  
    public OctalIntegerLiteral(IConstructor node , org.rascalmpl.ast.OctalIntegerLiteral octal) {
      super(node);
      
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
    public boolean equals(Object o) {
      if (!(o instanceof OctalIntegerLiteral)) {
        return false;
      }        
      OctalIntegerLiteral tmp = (OctalIntegerLiteral) o;
      return true && tmp.octal.equals(this.octal) ; 
    }
   
    @Override
    public int hashCode() {
      return 379 + 131 * octal.hashCode() ; 
    } 
  
    
    @Override
    public org.rascalmpl.ast.OctalIntegerLiteral getOctal() {
      return this.octal;
    }
  
    @Override
    public boolean hasOctal() {
      return true;
    }	
  }
}