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

public abstract class DateTimeLiteral extends AbstractAST {
  public DateTimeLiteral(IConstructor node) {
    super();
  }

  
  public boolean hasDateAndTime() {
    return false;
  }

  public org.rascalmpl.ast.DateAndTime getDateAndTime() {
    throw new UnsupportedOperationException();
  }
  public boolean hasDate() {
    return false;
  }

  public org.rascalmpl.ast.JustDate getDate() {
    throw new UnsupportedOperationException();
  }
  public boolean hasTime() {
    return false;
  }

  public org.rascalmpl.ast.JustTime getTime() {
    throw new UnsupportedOperationException();
  }

  

  
  public boolean isDateAndTimeLiteral() {
    return false;
  }

  static public class DateAndTimeLiteral extends DateTimeLiteral {
    // Production: sig("DateAndTimeLiteral",[arg("org.rascalmpl.ast.DateAndTime","dateAndTime")])
  
    
    private final org.rascalmpl.ast.DateAndTime dateAndTime;
  
    public DateAndTimeLiteral(IConstructor node , org.rascalmpl.ast.DateAndTime dateAndTime) {
      super(node);
      
      this.dateAndTime = dateAndTime;
    }
  
    @Override
    public boolean isDateAndTimeLiteral() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitDateTimeLiteralDateAndTimeLiteral(this);
    }
  
    @Override
    public boolean equals(Object o) {
      if (!(o instanceof DateAndTimeLiteral)) {
        return false;
      }        
      DateAndTimeLiteral tmp = (DateAndTimeLiteral) o;
      return true && tmp.dateAndTime.equals(this.dateAndTime) ; 
    }
   
    @Override
    public int hashCode() {
      return 163 + 317 * dateAndTime.hashCode() ; 
    } 
  
    
    @Override
    public org.rascalmpl.ast.DateAndTime getDateAndTime() {
      return this.dateAndTime;
    }
  
    @Override
    public boolean hasDateAndTime() {
      return true;
    }	
  }
  public boolean isDateLiteral() {
    return false;
  }

  static public class DateLiteral extends DateTimeLiteral {
    // Production: sig("DateLiteral",[arg("org.rascalmpl.ast.JustDate","date")])
  
    
    private final org.rascalmpl.ast.JustDate date;
  
    public DateLiteral(IConstructor node , org.rascalmpl.ast.JustDate date) {
      super(node);
      
      this.date = date;
    }
  
    @Override
    public boolean isDateLiteral() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitDateTimeLiteralDateLiteral(this);
    }
  
    @Override
    public boolean equals(Object o) {
      if (!(o instanceof DateLiteral)) {
        return false;
      }        
      DateLiteral tmp = (DateLiteral) o;
      return true && tmp.date.equals(this.date) ; 
    }
   
    @Override
    public int hashCode() {
      return 727 + 23 * date.hashCode() ; 
    } 
  
    
    @Override
    public org.rascalmpl.ast.JustDate getDate() {
      return this.date;
    }
  
    @Override
    public boolean hasDate() {
      return true;
    }	
  }
  public boolean isTimeLiteral() {
    return false;
  }

  static public class TimeLiteral extends DateTimeLiteral {
    // Production: sig("TimeLiteral",[arg("org.rascalmpl.ast.JustTime","time")])
  
    
    private final org.rascalmpl.ast.JustTime time;
  
    public TimeLiteral(IConstructor node , org.rascalmpl.ast.JustTime time) {
      super(node);
      
      this.time = time;
    }
  
    @Override
    public boolean isTimeLiteral() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitDateTimeLiteralTimeLiteral(this);
    }
  
    @Override
    public boolean equals(Object o) {
      if (!(o instanceof TimeLiteral)) {
        return false;
      }        
      TimeLiteral tmp = (TimeLiteral) o;
      return true && tmp.time.equals(this.time) ; 
    }
   
    @Override
    public int hashCode() {
      return 349 + 751 * time.hashCode() ; 
    } 
  
    
    @Override
    public org.rascalmpl.ast.JustTime getTime() {
      return this.time;
    }
  
    @Override
    public boolean hasTime() {
      return true;
    }	
  }
}