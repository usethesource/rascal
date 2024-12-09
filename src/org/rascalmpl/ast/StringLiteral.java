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
public abstract class StringLiteral extends AbstractAST {
  public StringLiteral(ISourceLocation src, IConstructor node) {
    super(src /* we forget node on purpose */);
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
    // Production: sig("Interpolated",[arg("org.rascalmpl.ast.PreStringChars","pre"),arg("org.rascalmpl.ast.Expression","expression"),arg("org.rascalmpl.ast.StringTail","tail")],breakable=false)
  
    
    private final org.rascalmpl.ast.PreStringChars pre;
    private final org.rascalmpl.ast.Expression expression;
    private final org.rascalmpl.ast.StringTail tail;
  
    public Interpolated(ISourceLocation src, IConstructor node , org.rascalmpl.ast.PreStringChars pre,  org.rascalmpl.ast.Expression expression,  org.rascalmpl.ast.StringTail tail) {
      super(src, node);
      
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
    protected void addForLineNumber(int $line, java.util.List<AbstractAST> $result) {
      if (getLocation().getBeginLine() == $line) {
        $result.add(this);
      }
      ISourceLocation $l;
      
      $l = pre.getLocation();
      if ($l.hasLineColumn() && $l.getBeginLine() <= $line && $l.getEndLine() >= $line) {
        pre.addForLineNumber($line, $result);
      }
      if ($l.getBeginLine() > $line) {
        return;
      }
      
      $l = expression.getLocation();
      if ($l.hasLineColumn() && $l.getBeginLine() <= $line && $l.getEndLine() >= $line) {
        expression.addForLineNumber($line, $result);
      }
      if ($l.getBeginLine() > $line) {
        return;
      }
      
      $l = tail.getLocation();
      if ($l.hasLineColumn() && $l.getBeginLine() <= $line && $l.getEndLine() >= $line) {
        tail.addForLineNumber($line, $result);
      }
      if ($l.getBeginLine() > $line) {
        return;
      }
      
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
      return 157 + 283 * pre.hashCode() + 947 * expression.hashCode() + 541 * tail.hashCode() ; 
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
  
    @Override
    public Object clone()  {
      return newInstance(getClass(), src, (IConstructor) null , clone(pre), clone(expression), clone(tail));
    }
            
  }
  public boolean isNonInterpolated() {
    return false;
  }

  static public class NonInterpolated extends StringLiteral {
    // Production: sig("NonInterpolated",[arg("org.rascalmpl.ast.StringConstant","constant")],breakable=false)
  
    
    private final org.rascalmpl.ast.StringConstant constant;
  
    public NonInterpolated(ISourceLocation src, IConstructor node , org.rascalmpl.ast.StringConstant constant) {
      super(src, node);
      
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
    protected void addForLineNumber(int $line, java.util.List<AbstractAST> $result) {
      if (getLocation().getBeginLine() == $line) {
        $result.add(this);
      }
      ISourceLocation $l;
      
      $l = constant.getLocation();
      if ($l.hasLineColumn() && $l.getBeginLine() <= $line && $l.getEndLine() >= $line) {
        constant.addForLineNumber($line, $result);
      }
      if ($l.getBeginLine() > $line) {
        return;
      }
      
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
      return 103 + 619 * constant.hashCode() ; 
    } 
  
    
    @Override
    public org.rascalmpl.ast.StringConstant getConstant() {
      return this.constant;
    }
  
    @Override
    public boolean hasConstant() {
      return true;
    }	
  
    @Override
    public Object clone()  {
      return newInstance(getClass(), src, (IConstructor) null , clone(constant));
    }
            
  }
  public boolean isTemplate() {
    return false;
  }

  static public class Template extends StringLiteral {
    // Production: sig("Template",[arg("org.rascalmpl.ast.PreStringChars","pre"),arg("org.rascalmpl.ast.StringTemplate","template"),arg("org.rascalmpl.ast.StringTail","tail")],breakable=false)
  
    
    private final org.rascalmpl.ast.PreStringChars pre;
    private final org.rascalmpl.ast.StringTemplate template;
    private final org.rascalmpl.ast.StringTail tail;
  
    public Template(ISourceLocation src, IConstructor node , org.rascalmpl.ast.PreStringChars pre,  org.rascalmpl.ast.StringTemplate template,  org.rascalmpl.ast.StringTail tail) {
      super(src, node);
      
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
    protected void addForLineNumber(int $line, java.util.List<AbstractAST> $result) {
      if (getLocation().getBeginLine() == $line) {
        $result.add(this);
      }
      ISourceLocation $l;
      
      $l = pre.getLocation();
      if ($l.hasLineColumn() && $l.getBeginLine() <= $line && $l.getEndLine() >= $line) {
        pre.addForLineNumber($line, $result);
      }
      if ($l.getBeginLine() > $line) {
        return;
      }
      
      $l = template.getLocation();
      if ($l.hasLineColumn() && $l.getBeginLine() <= $line && $l.getEndLine() >= $line) {
        template.addForLineNumber($line, $result);
      }
      if ($l.getBeginLine() > $line) {
        return;
      }
      
      $l = tail.getLocation();
      if ($l.hasLineColumn() && $l.getBeginLine() <= $line && $l.getEndLine() >= $line) {
        tail.addForLineNumber($line, $result);
      }
      if ($l.getBeginLine() > $line) {
        return;
      }
      
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
      return 421 + 541 * pre.hashCode() + 509 * template.hashCode() + 941 * tail.hashCode() ; 
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
  
    @Override
    public Object clone()  {
      return newInstance(getClass(), src, (IConstructor) null , clone(pre), clone(template), clone(tail));
    }
            
  }
}