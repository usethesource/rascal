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
public abstract class StructuredType extends AbstractAST {
  public StructuredType(ISourceLocation src, IConstructor node) {
    super(src /* we forget node on purpose */);
  }

  
  public boolean hasArguments() {
    return false;
  }

  public java.util.List<org.rascalmpl.ast.TypeArg> getArguments() {
    throw new UnsupportedOperationException();
  }
  public boolean hasBasicType() {
    return false;
  }

  public org.rascalmpl.ast.BasicType getBasicType() {
    throw new UnsupportedOperationException();
  }

  

  
  public boolean isDefault() {
    return false;
  }

  static public class Default extends StructuredType {
    // Production: sig("Default",[arg("org.rascalmpl.ast.BasicType","basicType"),arg("java.util.List\<org.rascalmpl.ast.TypeArg\>","arguments")],breakable=false)
  
    
    private final org.rascalmpl.ast.BasicType basicType;
    private final java.util.List<org.rascalmpl.ast.TypeArg> arguments;
  
    public Default(ISourceLocation src, IConstructor node , org.rascalmpl.ast.BasicType basicType,  java.util.List<org.rascalmpl.ast.TypeArg> arguments) {
      super(src, node);
      
      this.basicType = basicType;
      this.arguments = arguments;
    }
  
    @Override
    public boolean isDefault() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitStructuredTypeDefault(this);
    }
  
    @Override
    protected void addForLineNumber(int $line, java.util.List<AbstractAST> $result) {
      if (getLocation().getBeginLine() == $line) {
        $result.add(this);
      }
      ISourceLocation $l;
      
      $l = basicType.getLocation();
      if ($l.hasLineColumn() && $l.getBeginLine() <= $line && $l.getEndLine() >= $line) {
        basicType.addForLineNumber($line, $result);
      }
      if ($l.getBeginLine() > $line) {
        return;
      }
      
      for (AbstractAST $elem : arguments) {
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
      return true && tmp.basicType.equals(this.basicType) && tmp.arguments.equals(this.arguments) ; 
    }
   
    @Override
    public int hashCode() {
      return 653 + 73 * basicType.hashCode() + 991 * arguments.hashCode() ; 
    } 
  
    
    @Override
    public org.rascalmpl.ast.BasicType getBasicType() {
      return this.basicType;
    }
  
    @Override
    public boolean hasBasicType() {
      return true;
    }
    @Override
    public java.util.List<org.rascalmpl.ast.TypeArg> getArguments() {
      return this.arguments;
    }
  
    @Override
    public boolean hasArguments() {
      return true;
    }	
  
    @Override
    public Object clone()  {
      return newInstance(getClass(), src, (IConstructor) null , clone(basicType), clone(arguments));
    }
            
  }
}