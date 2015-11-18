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


import org.rascalmpl.value.IConstructor;
import org.rascalmpl.value.ISourceLocation;

public abstract class StringLiteral extends AbstractAST {
  public StringLiteral(ISourceLocation src, IConstructor node) {
    super(src /* we forget node on purpose */);
  }

  
  public boolean hasBody() {
    return false;
  }

  public java.util.List<org.rascalmpl.ast.StringPart> getBody() {
    throw new UnsupportedOperationException();
  }
  public boolean hasIndent() {
    return false;
  }

  public org.rascalmpl.ast.Indentation getIndent() {
    throw new UnsupportedOperationException();
  }

  

  
  public boolean isDefault() {
    return false;
  }

  static public class Default extends StringLiteral {
    // Production: sig("Default",[arg("org.rascalmpl.ast.Indentation","indent"),arg("java.util.List\<org.rascalmpl.ast.StringPart\>","body")],breakable=false)
  
    
    private final org.rascalmpl.ast.Indentation indent;
    private final java.util.List<org.rascalmpl.ast.StringPart> body;
  
    public Default(ISourceLocation src, IConstructor node , org.rascalmpl.ast.Indentation indent,  java.util.List<org.rascalmpl.ast.StringPart> body) {
      super(src, node);
      
      this.indent = indent;
      this.body = body;
    }
  
    @Override
    public boolean isDefault() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitStringLiteralDefault(this);
    }
  
    @Override
    protected void addForLineNumber(int $line, java.util.List<AbstractAST> $result) {
      if (getLocation().getBeginLine() == $line) {
        $result.add(this);
      }
      ISourceLocation $l;
      
      $l = indent.getLocation();
      if ($l.hasLineColumn() && $l.getBeginLine() <= $line && $l.getEndLine() >= $line) {
        indent.addForLineNumber($line, $result);
      }
      if ($l.getBeginLine() > $line) {
        return;
      }
      
      for (AbstractAST $elem : body) {
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
      if (!(o instanceof Default)) {
        return false;
      }        
      Default tmp = (Default) o;
      return true && tmp.indent.equals(this.indent) && tmp.body.equals(this.body) ; 
    }
   
    @Override
    public int hashCode() {
      return 421 + 541 * indent.hashCode() + 509 * body.hashCode() ; 
    } 
  
    
    @Override
    public org.rascalmpl.ast.Indentation getIndent() {
      return this.indent;
    }
  
    @Override
    public boolean hasIndent() {
      return true;
    }
    @Override
    public java.util.List<org.rascalmpl.ast.StringPart> getBody() {
      return this.body;
    }
  
    @Override
    public boolean hasBody() {
      return true;
    }	
  
    @Override
    public Object clone()  {
      return newInstance(getClass(), src, (IConstructor) null , clone(indent), clone(body));
    }
            
  }
}