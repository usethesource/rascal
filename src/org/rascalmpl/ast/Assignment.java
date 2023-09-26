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
public abstract class Assignment extends AbstractAST {
  public Assignment(ISourceLocation src, IConstructor node) {
    super(src /* we forget node on purpose */);
  }

  

  

  
  public boolean isAddition() {
    return false;
  }

  static public class Addition extends Assignment {
    // Production: sig("Addition",[],breakable=false)
  
    
  
    public Addition(ISourceLocation src, IConstructor node ) {
      super(src, node);
      
    }
  
    @Override
    public boolean isAddition() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitAssignmentAddition(this);
    }
  
    @Override
    protected void addForLineNumber(int $line, java.util.List<AbstractAST> $result) {
      if (getLocation().getBeginLine() == $line) {
        $result.add(this);
      }
      ISourceLocation $l;
      
    }
  
    @Override
    public boolean equals(Object o) {
      if (!(o instanceof Addition)) {
        return false;
      }        
      Addition tmp = (Addition) o;
      return true ; 
    }
   
    @Override
    public int hashCode() {
      return 431 ; 
    } 
  
    	
  
    @Override
    public Object clone()  {
      return newInstance(getClass(), src, (IConstructor) null );
    }
            
  }
  public boolean isAppend() {
    return false;
  }

  static public class Append extends Assignment {
    // Production: sig("Append",[],breakable=false)
  
    
  
    public Append(ISourceLocation src, IConstructor node ) {
      super(src, node);
      
    }
  
    @Override
    public boolean isAppend() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitAssignmentAppend(this);
    }
  
    @Override
    protected void addForLineNumber(int $line, java.util.List<AbstractAST> $result) {
      if (getLocation().getBeginLine() == $line) {
        $result.add(this);
      }
      ISourceLocation $l;
      
    }
  
    @Override
    public boolean equals(Object o) {
      if (!(o instanceof Append)) {
        return false;
      }        
      Append tmp = (Append) o;
      return true ; 
    }
   
    @Override
    public int hashCode() {
      return 751 ; 
    } 
  
    	
  
    @Override
    public Object clone()  {
      return newInstance(getClass(), src, (IConstructor) null );
    }
            
  }
  public boolean isDefault() {
    return false;
  }

  static public class Default extends Assignment {
    // Production: sig("Default",[],breakable=false)
  
    
  
    public Default(ISourceLocation src, IConstructor node ) {
      super(src, node);
      
    }
  
    @Override
    public boolean isDefault() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitAssignmentDefault(this);
    }
  
    @Override
    protected void addForLineNumber(int $line, java.util.List<AbstractAST> $result) {
      if (getLocation().getBeginLine() == $line) {
        $result.add(this);
      }
      ISourceLocation $l;
      
    }
  
    @Override
    public boolean equals(Object o) {
      if (!(o instanceof Default)) {
        return false;
      }        
      Default tmp = (Default) o;
      return true ; 
    }
   
    @Override
    public int hashCode() {
      return 449 ; 
    } 
  
    	
  
    @Override
    public Object clone()  {
      return newInstance(getClass(), src, (IConstructor) null );
    }
            
  }
  public boolean isDivision() {
    return false;
  }

  static public class Division extends Assignment {
    // Production: sig("Division",[],breakable=false)
  
    
  
    public Division(ISourceLocation src, IConstructor node ) {
      super(src, node);
      
    }
  
    @Override
    public boolean isDivision() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitAssignmentDivision(this);
    }
  
    @Override
    protected void addForLineNumber(int $line, java.util.List<AbstractAST> $result) {
      if (getLocation().getBeginLine() == $line) {
        $result.add(this);
      }
      ISourceLocation $l;
      
    }
  
    @Override
    public boolean equals(Object o) {
      if (!(o instanceof Division)) {
        return false;
      }        
      Division tmp = (Division) o;
      return true ; 
    }
   
    @Override
    public int hashCode() {
      return 557 ; 
    } 
  
    	
  
    @Override
    public Object clone()  {
      return newInstance(getClass(), src, (IConstructor) null );
    }
            
  }
  public boolean isIfDefined() {
    return false;
  }

  static public class IfDefined extends Assignment {
    // Production: sig("IfDefined",[],breakable=false)
  
    
  
    public IfDefined(ISourceLocation src, IConstructor node ) {
      super(src, node);
      
    }
  
    @Override
    public boolean isIfDefined() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitAssignmentIfDefined(this);
    }
  
    @Override
    protected void addForLineNumber(int $line, java.util.List<AbstractAST> $result) {
      if (getLocation().getBeginLine() == $line) {
        $result.add(this);
      }
      ISourceLocation $l;
      
    }
  
    @Override
    public boolean equals(Object o) {
      if (!(o instanceof IfDefined)) {
        return false;
      }        
      IfDefined tmp = (IfDefined) o;
      return true ; 
    }
   
    @Override
    public int hashCode() {
      return 61 ; 
    } 
  
    	
  
    @Override
    public Object clone()  {
      return newInstance(getClass(), src, (IConstructor) null );
    }
            
  }
  public boolean isIntersection() {
    return false;
  }

  static public class Intersection extends Assignment {
    // Production: sig("Intersection",[],breakable=false)
  
    
  
    public Intersection(ISourceLocation src, IConstructor node ) {
      super(src, node);
      
    }
  
    @Override
    public boolean isIntersection() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitAssignmentIntersection(this);
    }
  
    @Override
    protected void addForLineNumber(int $line, java.util.List<AbstractAST> $result) {
      if (getLocation().getBeginLine() == $line) {
        $result.add(this);
      }
      ISourceLocation $l;
      
    }
  
    @Override
    public boolean equals(Object o) {
      if (!(o instanceof Intersection)) {
        return false;
      }        
      Intersection tmp = (Intersection) o;
      return true ; 
    }
   
    @Override
    public int hashCode() {
      return 503 ; 
    } 
  
    	
  
    @Override
    public Object clone()  {
      return newInstance(getClass(), src, (IConstructor) null );
    }
            
  }
  public boolean isProduct() {
    return false;
  }

  static public class Product extends Assignment {
    // Production: sig("Product",[],breakable=false)
  
    
  
    public Product(ISourceLocation src, IConstructor node ) {
      super(src, node);
      
    }
  
    @Override
    public boolean isProduct() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitAssignmentProduct(this);
    }
  
    @Override
    protected void addForLineNumber(int $line, java.util.List<AbstractAST> $result) {
      if (getLocation().getBeginLine() == $line) {
        $result.add(this);
      }
      ISourceLocation $l;
      
    }
  
    @Override
    public boolean equals(Object o) {
      if (!(o instanceof Product)) {
        return false;
      }        
      Product tmp = (Product) o;
      return true ; 
    }
   
    @Override
    public int hashCode() {
      return 41 ; 
    } 
  
    	
  
    @Override
    public Object clone()  {
      return newInstance(getClass(), src, (IConstructor) null );
    }
            
  }
  public boolean isSubtraction() {
    return false;
  }

  static public class Subtraction extends Assignment {
    // Production: sig("Subtraction",[],breakable=false)
  
    
  
    public Subtraction(ISourceLocation src, IConstructor node ) {
      super(src, node);
      
    }
  
    @Override
    public boolean isSubtraction() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitAssignmentSubtraction(this);
    }
  
    @Override
    protected void addForLineNumber(int $line, java.util.List<AbstractAST> $result) {
      if (getLocation().getBeginLine() == $line) {
        $result.add(this);
      }
      ISourceLocation $l;
      
    }
  
    @Override
    public boolean equals(Object o) {
      if (!(o instanceof Subtraction)) {
        return false;
      }        
      Subtraction tmp = (Subtraction) o;
      return true ; 
    }
   
    @Override
    public int hashCode() {
      return 293 ; 
    } 
  
    	
  
    @Override
    public Object clone()  {
      return newInstance(getClass(), src, (IConstructor) null );
    }
            
  }
}