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

public abstract class KeywordFormals extends AbstractAST {
  public KeywordFormals(IConstructor node) {
    super();
  }

  
  public boolean hasKeywordFormalList() {
    return false;
  }

  public java.util.List<org.rascalmpl.ast.KeywordFormal> getKeywordFormalList() {
    throw new UnsupportedOperationException();
  }
  public boolean hasOptionalComma() {
    return false;
  }

  public org.rascalmpl.ast.OptionalComma getOptionalComma() {
    throw new UnsupportedOperationException();
  }

  

  
  public boolean isDefault() {
    return false;
  }

  static public class Default extends KeywordFormals {
    // Production: sig("Default",[arg("org.rascalmpl.ast.OptionalComma","optionalComma"),arg("java.util.List\<org.rascalmpl.ast.KeywordFormal\>","keywordFormalList")])
  
    
    private final org.rascalmpl.ast.OptionalComma optionalComma;
    private final java.util.List<org.rascalmpl.ast.KeywordFormal> keywordFormalList;
  
    public Default(IConstructor node , org.rascalmpl.ast.OptionalComma optionalComma,  java.util.List<org.rascalmpl.ast.KeywordFormal> keywordFormalList) {
      super(node);
      
      this.optionalComma = optionalComma;
      this.keywordFormalList = keywordFormalList;
    }
  
    @Override
    public boolean isDefault() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitKeywordFormalsDefault(this);
    }
  
    @Override
    public boolean equals(Object o) {
      if (!(o instanceof Default)) {
        return false;
      }        
      Default tmp = (Default) o;
      return true && tmp.optionalComma.equals(this.optionalComma) && tmp.keywordFormalList.equals(this.keywordFormalList) ; 
    }
   
    @Override
    public int hashCode() {
      return 853 + 887 * optionalComma.hashCode() + 991 * keywordFormalList.hashCode() ; 
    } 
  
    
    @Override
    public org.rascalmpl.ast.OptionalComma getOptionalComma() {
      return this.optionalComma;
    }
  
    @Override
    public boolean hasOptionalComma() {
      return true;
    }
    @Override
    public java.util.List<org.rascalmpl.ast.KeywordFormal> getKeywordFormalList() {
      return this.keywordFormalList;
    }
  
    @Override
    public boolean hasKeywordFormalList() {
      return true;
    }	
  }
  public boolean isNone() {
    return false;
  }

  static public class None extends KeywordFormals {
    // Production: sig("None",[])
  
    
  
    public None(IConstructor node ) {
      super(node);
      
    }
  
    @Override
    public boolean isNone() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitKeywordFormalsNone(this);
    }
  
    @Override
    public boolean equals(Object o) {
      if (!(o instanceof None)) {
        return false;
      }        
      None tmp = (None) o;
      return true ; 
    }
   
    @Override
    public int hashCode() {
      return 521 ; 
    } 
  
    	
  }
}