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
public abstract class Replacement extends AbstractAST {
  public Replacement(ISourceLocation src, IConstructor node) {
    super(src /* we forget node on purpose */);
  }

  
  public boolean hasConditions() {
    return false;
  }

  public java.util.List<org.rascalmpl.ast.Expression> getConditions() {
    throw new UnsupportedOperationException();
  }
  public boolean hasReplacementExpression() {
    return false;
  }

  public org.rascalmpl.ast.Expression getReplacementExpression() {
    throw new UnsupportedOperationException();
  }

  

  
  public boolean isConditional() {
    return false;
  }

  static public class Conditional extends Replacement {
    // Production: sig("Conditional",[arg("org.rascalmpl.ast.Expression","replacementExpression"),arg("java.util.List\<org.rascalmpl.ast.Expression\>","conditions")],breakable=false)
  
    
    private final org.rascalmpl.ast.Expression replacementExpression;
    private final java.util.List<org.rascalmpl.ast.Expression> conditions;
  
    public Conditional(ISourceLocation src, IConstructor node , org.rascalmpl.ast.Expression replacementExpression,  java.util.List<org.rascalmpl.ast.Expression> conditions) {
      super(src, node);
      
      this.replacementExpression = replacementExpression;
      this.conditions = conditions;
    }
  
    @Override
    public boolean isConditional() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitReplacementConditional(this);
    }
  
    @Override
    protected void addForLineNumber(int $line, java.util.List<AbstractAST> $result) {
      if (getLocation().getBeginLine() == $line) {
        $result.add(this);
      }
      ISourceLocation $l;
      
      $l = replacementExpression.getLocation();
      if ($l.hasLineColumn() && $l.getBeginLine() <= $line && $l.getEndLine() >= $line) {
        replacementExpression.addForLineNumber($line, $result);
      }
      if ($l.getBeginLine() > $line) {
        return;
      }
      
      for (AbstractAST $elem : conditions) {
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
      if (!(o instanceof Conditional)) {
        return false;
      }        
      Conditional tmp = (Conditional) o;
      return true && tmp.replacementExpression.equals(this.replacementExpression) && tmp.conditions.equals(this.conditions) ; 
    }
   
    @Override
    public int hashCode() {
      return 881 + 3 * replacementExpression.hashCode() + 293 * conditions.hashCode() ; 
    } 
  
    
    @Override
    public org.rascalmpl.ast.Expression getReplacementExpression() {
      return this.replacementExpression;
    }
  
    @Override
    public boolean hasReplacementExpression() {
      return true;
    }
    @Override
    public java.util.List<org.rascalmpl.ast.Expression> getConditions() {
      return this.conditions;
    }
  
    @Override
    public boolean hasConditions() {
      return true;
    }	
  
    @Override
    public Object clone()  {
      return newInstance(getClass(), src, (IConstructor) null , clone(replacementExpression), clone(conditions));
    }
            
  }
  public boolean isUnconditional() {
    return false;
  }

  static public class Unconditional extends Replacement {
    // Production: sig("Unconditional",[arg("org.rascalmpl.ast.Expression","replacementExpression")],breakable=false)
  
    
    private final org.rascalmpl.ast.Expression replacementExpression;
  
    public Unconditional(ISourceLocation src, IConstructor node , org.rascalmpl.ast.Expression replacementExpression) {
      super(src, node);
      
      this.replacementExpression = replacementExpression;
    }
  
    @Override
    public boolean isUnconditional() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitReplacementUnconditional(this);
    }
  
    @Override
    protected void addForLineNumber(int $line, java.util.List<AbstractAST> $result) {
      if (getLocation().getBeginLine() == $line) {
        $result.add(this);
      }
      ISourceLocation $l;
      
      $l = replacementExpression.getLocation();
      if ($l.hasLineColumn() && $l.getBeginLine() <= $line && $l.getEndLine() >= $line) {
        replacementExpression.addForLineNumber($line, $result);
      }
      if ($l.getBeginLine() > $line) {
        return;
      }
      
    }
  
    @Override
    public boolean equals(Object o) {
      if (!(o instanceof Unconditional)) {
        return false;
      }        
      Unconditional tmp = (Unconditional) o;
      return true && tmp.replacementExpression.equals(this.replacementExpression) ; 
    }
   
    @Override
    public int hashCode() {
      return 281 + 433 * replacementExpression.hashCode() ; 
    } 
  
    
    @Override
    public org.rascalmpl.ast.Expression getReplacementExpression() {
      return this.replacementExpression;
    }
  
    @Override
    public boolean hasReplacementExpression() {
      return true;
    }	
  
    @Override
    public Object clone()  {
      return newInstance(getClass(), src, (IConstructor) null , clone(replacementExpression));
    }
            
  }
}