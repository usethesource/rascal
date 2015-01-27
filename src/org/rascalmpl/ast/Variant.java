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

public abstract class Variant extends AbstractAST {
  public Variant(IConstructor node) {
    super();
  }

  
  public boolean hasArguments() {
    return false;
  }

  public java.util.List<org.rascalmpl.ast.TypeArg> getArguments() {
    throw new UnsupportedOperationException();
  }
  public boolean hasKeywordArguments() {
    return false;
  }

  public org.rascalmpl.ast.KeywordFormals getKeywordArguments() {
    throw new UnsupportedOperationException();
  }
  public boolean hasName() {
    return false;
  }

  public org.rascalmpl.ast.Name getName() {
    throw new UnsupportedOperationException();
  }

  

  
  public boolean isNAryConstructor() {
    return false;
  }

  static public class NAryConstructor extends Variant {
    // Production: sig("NAryConstructor",[arg("org.rascalmpl.ast.Name","name"),arg("java.util.List\<org.rascalmpl.ast.TypeArg\>","arguments"),arg("org.rascalmpl.ast.KeywordFormals","keywordArguments")])
  
    
    private final org.rascalmpl.ast.Name name;
    private final java.util.List<org.rascalmpl.ast.TypeArg> arguments;
    private final org.rascalmpl.ast.KeywordFormals keywordArguments;
  
    public NAryConstructor(IConstructor node , org.rascalmpl.ast.Name name,  java.util.List<org.rascalmpl.ast.TypeArg> arguments,  org.rascalmpl.ast.KeywordFormals keywordArguments) {
      super(node);
      
      this.name = name;
      this.arguments = arguments;
      this.keywordArguments = keywordArguments;
    }
  
    @Override
    public boolean isNAryConstructor() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitVariantNAryConstructor(this);
    }
  
    @Override
    public boolean equals(Object o) {
      if (!(o instanceof NAryConstructor)) {
        return false;
      }        
      NAryConstructor tmp = (NAryConstructor) o;
      return true && tmp.name.equals(this.name) && tmp.arguments.equals(this.arguments) && tmp.keywordArguments.equals(this.keywordArguments) ; 
    }
   
    @Override
    public int hashCode() {
      return 953 + 719 * name.hashCode() + 313 * arguments.hashCode() + 389 * keywordArguments.hashCode() ; 
    } 
  
    
    @Override
    public org.rascalmpl.ast.Name getName() {
      return this.name;
    }
  
    @Override
    public boolean hasName() {
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
    public org.rascalmpl.ast.KeywordFormals getKeywordArguments() {
      return this.keywordArguments;
    }
  
    @Override
    public boolean hasKeywordArguments() {
      return true;
    }	
  }
}