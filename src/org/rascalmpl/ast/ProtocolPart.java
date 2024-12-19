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
public abstract class ProtocolPart extends AbstractAST {
  public ProtocolPart(ISourceLocation src, IConstructor node) {
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

  public org.rascalmpl.ast.PreProtocolChars getPre() {
    throw new UnsupportedOperationException();
  }
  public boolean hasProtocolChars() {
    return false;
  }

  public org.rascalmpl.ast.ProtocolChars getProtocolChars() {
    throw new UnsupportedOperationException();
  }
  public boolean hasTail() {
    return false;
  }

  public org.rascalmpl.ast.ProtocolTail getTail() {
    throw new UnsupportedOperationException();
  }

  

  
  public boolean isInterpolated() {
    return false;
  }

  static public class Interpolated extends ProtocolPart {
    // Production: sig("Interpolated",[arg("org.rascalmpl.ast.PreProtocolChars","pre"),arg("org.rascalmpl.ast.Expression","expression"),arg("org.rascalmpl.ast.ProtocolTail","tail")],breakable=false)
  
    
    private final org.rascalmpl.ast.PreProtocolChars pre;
    private final org.rascalmpl.ast.Expression expression;
    private final org.rascalmpl.ast.ProtocolTail tail;
  
    public Interpolated(ISourceLocation src, IConstructor node , org.rascalmpl.ast.PreProtocolChars pre,  org.rascalmpl.ast.Expression expression,  org.rascalmpl.ast.ProtocolTail tail) {
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
      return visitor.visitProtocolPartInterpolated(this);
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
      return 349 + 139 * pre.hashCode() + 73 * expression.hashCode() + 439 * tail.hashCode() ; 
    } 
  
    
    @Override
    public org.rascalmpl.ast.PreProtocolChars getPre() {
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
    public org.rascalmpl.ast.ProtocolTail getTail() {
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

  static public class NonInterpolated extends ProtocolPart {
    // Production: sig("NonInterpolated",[arg("org.rascalmpl.ast.ProtocolChars","protocolChars")],breakable=false)
  
    
    private final org.rascalmpl.ast.ProtocolChars protocolChars;
  
    public NonInterpolated(ISourceLocation src, IConstructor node , org.rascalmpl.ast.ProtocolChars protocolChars) {
      super(src, node);
      
      this.protocolChars = protocolChars;
    }
  
    @Override
    public boolean isNonInterpolated() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitProtocolPartNonInterpolated(this);
    }
  
    @Override
    protected void addForLineNumber(int $line, java.util.List<AbstractAST> $result) {
      if (getLocation().getBeginLine() == $line) {
        $result.add(this);
      }
      ISourceLocation $l;
      
      $l = protocolChars.getLocation();
      if ($l.hasLineColumn() && $l.getBeginLine() <= $line && $l.getEndLine() >= $line) {
        protocolChars.addForLineNumber($line, $result);
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
      return true && tmp.protocolChars.equals(this.protocolChars) ; 
    }
   
    @Override
    public int hashCode() {
      return 191 + 89 * protocolChars.hashCode() ; 
    } 
  
    
    @Override
    public org.rascalmpl.ast.ProtocolChars getProtocolChars() {
      return this.protocolChars;
    }
  
    @Override
    public boolean hasProtocolChars() {
      return true;
    }	
  
    @Override
    public Object clone()  {
      return newInstance(getClass(), src, (IConstructor) null , clone(protocolChars));
    }
            
  }
}