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

public abstract class StringLiteral extends AbstractAST {
  public StringLiteral(IConstructor node) {
    super();
  }

  
  public boolean hasExpression() {
    return false;
  }

  public org.rascalmpl.ast.Expression getExpression() {
    throw new UnsupportedOperationException();
  }
  public boolean hasPre() {
    return false;
  }

  public org.rascalmpl.ast.PreStringChars getPre() {
    throw new UnsupportedOperationException();
  }
  public boolean hasConstant() {
    return false;
  }

  public org.rascalmpl.ast.StringConstant getConstant() {
    throw new UnsupportedOperationException();
  }
  public boolean hasTail() {
    return false;
  }

  public org.rascalmpl.ast.StringTail getTail() {
    throw new UnsupportedOperationException();
  }
  public boolean hasTemplate() {
    return false;
  }

  public org.rascalmpl.ast.StringTemplate getTemplate() {
    throw new UnsupportedOperationException();
  }

  

  
  public boolean isInterpolated() {
    return false;
  }

  static public class Interpolated extends StringLiteral {
    // Production: sig("Interpolated",[arg("org.rascalmpl.ast.PreStringChars","pre"),arg("org.rascalmpl.ast.Expression","expression"),arg("org.rascalmpl.ast.StringTail","tail")])
  
    
    private final org.rascalmpl.ast.PreStringChars pre;
    private final org.rascalmpl.ast.Expression expression;
    private final org.rascalmpl.ast.StringTail tail;
  
    public Interpolated(IConstructor node , org.rascalmpl.ast.PreStringChars pre,  org.rascalmpl.ast.Expression expression,  org.rascalmpl.ast.StringTail tail) {
      super(node);
      
      this.pre = pre;
      this.expression = expression;
      this.tail = tail;
    }
  
    @Override
    public boolean isInterpolated() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitStringLiteralInterpolated(this);
    }
  
    @Override
    public boolean equals(Object o) {
      if (!(o instanceof Interpolated)) {
        return false;
      }        
      Interpolated tmp = (Interpolated) o;
      return true && tmp.pre.equals(this.pre) && tmp.expression.equals(this.expression) && tmp.tail.equals(this.tail) ; 
    }
   
    @Override
    public int hashCode() {
      return 149 + 739 * pre.hashCode() + 991 * expression.hashCode() + 47 * tail.hashCode() ; 
    } 
  
    
    @Override
    public org.rascalmpl.ast.PreStringChars getPre() {
      return this.pre;
    }
  
    @Override
    public boolean hasPre() {
      return true;
    }
    @Override
    public org.rascalmpl.ast.Expression getExpression() {
      return this.expression;
    }
  
    @Override
    public boolean hasExpression() {
      return true;
    }
    @Override
    public org.rascalmpl.ast.StringTail getTail() {
      return this.tail;
    }
  
    @Override
    public boolean hasTail() {
      return true;
    }	
  }
  public boolean isNonInterpolated() {
    return false;
  }

  static public class NonInterpolated extends StringLiteral {
    // Production: sig("NonInterpolated",[arg("org.rascalmpl.ast.StringConstant","constant")])
  
    
    private final org.rascalmpl.ast.StringConstant constant;
  
    public NonInterpolated(IConstructor node , org.rascalmpl.ast.StringConstant constant) {
      super(node);
      
      this.constant = constant;
    }
  
    @Override
    public boolean isNonInterpolated() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitStringLiteralNonInterpolated(this);
    }
  
    @Override
    public boolean equals(Object o) {
      if (!(o instanceof NonInterpolated)) {
        return false;
      }        
      NonInterpolated tmp = (NonInterpolated) o;
      return true && tmp.constant.equals(this.constant) ; 
    }
   
    @Override
    public int hashCode() {
      return 863 + 59 * constant.hashCode() ; 
    } 
  
    
    @Override
    public org.rascalmpl.ast.StringConstant getConstant() {
      return this.constant;
    }
  
    @Override
    public boolean hasConstant() {
      return true;
    }	
  }
  public boolean isTemplate() {
    return false;
  }

  static public class Template extends StringLiteral {
    // Production: sig("Template",[arg("org.rascalmpl.ast.PreStringChars","pre"),arg("org.rascalmpl.ast.StringTemplate","template"),arg("org.rascalmpl.ast.StringTail","tail")])
  
    
    private final org.rascalmpl.ast.PreStringChars pre;
    private final org.rascalmpl.ast.StringTemplate template;
    private final org.rascalmpl.ast.StringTail tail;
  
    public Template(IConstructor node , org.rascalmpl.ast.PreStringChars pre,  org.rascalmpl.ast.StringTemplate template,  org.rascalmpl.ast.StringTail tail) {
      super(node);
      
      this.pre = pre;
      this.template = template;
      this.tail = tail;
    }
  
    @Override
    public boolean isTemplate() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitStringLiteralTemplate(this);
    }
  
    @Override
    public boolean equals(Object o) {
      if (!(o instanceof Template)) {
        return false;
      }        
      Template tmp = (Template) o;
      return true && tmp.pre.equals(this.pre) && tmp.template.equals(this.template) && tmp.tail.equals(this.tail) ; 
    }
   
    @Override
    public int hashCode() {
      return 449 + 577 * pre.hashCode() + 797 * template.hashCode() + 461 * tail.hashCode() ; 
    } 
  
    
    @Override
    public org.rascalmpl.ast.PreStringChars getPre() {
      return this.pre;
    }
  
    @Override
    public boolean hasPre() {
      return true;
    }
    @Override
    public org.rascalmpl.ast.StringTemplate getTemplate() {
      return this.template;
    }
  
    @Override
    public boolean hasTemplate() {
      return true;
    }
    @Override
    public org.rascalmpl.ast.StringTail getTail() {
      return this.tail;
    }
  
    @Override
    public boolean hasTail() {
      return true;
    }	
  }
}