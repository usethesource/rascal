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

public abstract class SyntaxDefinition extends AbstractAST {
  public SyntaxDefinition(IConstructor node) {
    super();
  }

  
  public boolean hasProduction() {
    return false;
  }

  public org.rascalmpl.ast.Prod getProduction() {
    throw new UnsupportedOperationException();
  }
  public boolean hasStart() {
    return false;
  }

  public org.rascalmpl.ast.Start getStart() {
    throw new UnsupportedOperationException();
  }
  public boolean hasDefined() {
    return false;
  }

  public org.rascalmpl.ast.Sym getDefined() {
    throw new UnsupportedOperationException();
  }
  public boolean hasVis() {
    return false;
  }

  public org.rascalmpl.ast.Visibility getVis() {
    throw new UnsupportedOperationException();
  }

  

  
  public boolean isKeyword() {
    return false;
  }

  static public class Keyword extends SyntaxDefinition {
    // Production: sig("Keyword",[arg("org.rascalmpl.ast.Sym","defined"),arg("org.rascalmpl.ast.Prod","production")])
  
    
    private final org.rascalmpl.ast.Sym defined;
    private final org.rascalmpl.ast.Prod production;
  
    public Keyword(IConstructor node , org.rascalmpl.ast.Sym defined,  org.rascalmpl.ast.Prod production) {
      super(node);
      
      this.defined = defined;
      this.production = production;
    }
  
    @Override
    public boolean isKeyword() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitSyntaxDefinitionKeyword(this);
    }
  
    @Override
    public boolean equals(Object o) {
      if (!(o instanceof Keyword)) {
        return false;
      }        
      Keyword tmp = (Keyword) o;
      return true && tmp.defined.equals(this.defined) && tmp.production.equals(this.production) ; 
    }
   
    @Override
    public int hashCode() {
      return 929 + 29 * defined.hashCode() + 733 * production.hashCode() ; 
    } 
  
    
    @Override
    public org.rascalmpl.ast.Sym getDefined() {
      return this.defined;
    }
  
    @Override
    public boolean hasDefined() {
      return true;
    }
    @Override
    public org.rascalmpl.ast.Prod getProduction() {
      return this.production;
    }
  
    @Override
    public boolean hasProduction() {
      return true;
    }	
  }
  public boolean isLanguage() {
    return false;
  }

  static public class Language extends SyntaxDefinition {
    // Production: sig("Language",[arg("org.rascalmpl.ast.Start","start"),arg("org.rascalmpl.ast.Sym","defined"),arg("org.rascalmpl.ast.Prod","production")])
  
    
    private final org.rascalmpl.ast.Start start;
    private final org.rascalmpl.ast.Sym defined;
    private final org.rascalmpl.ast.Prod production;
  
    public Language(IConstructor node , org.rascalmpl.ast.Start start,  org.rascalmpl.ast.Sym defined,  org.rascalmpl.ast.Prod production) {
      super(node);
      
      this.start = start;
      this.defined = defined;
      this.production = production;
    }
  
    @Override
    public boolean isLanguage() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitSyntaxDefinitionLanguage(this);
    }
  
    @Override
    public boolean equals(Object o) {
      if (!(o instanceof Language)) {
        return false;
      }        
      Language tmp = (Language) o;
      return true && tmp.start.equals(this.start) && tmp.defined.equals(this.defined) && tmp.production.equals(this.production) ; 
    }
   
    @Override
    public int hashCode() {
      return 727 + 359 * start.hashCode() + 577 * defined.hashCode() + 787 * production.hashCode() ; 
    } 
  
    
    @Override
    public org.rascalmpl.ast.Start getStart() {
      return this.start;
    }
  
    @Override
    public boolean hasStart() {
      return true;
    }
    @Override
    public org.rascalmpl.ast.Sym getDefined() {
      return this.defined;
    }
  
    @Override
    public boolean hasDefined() {
      return true;
    }
    @Override
    public org.rascalmpl.ast.Prod getProduction() {
      return this.production;
    }
  
    @Override
    public boolean hasProduction() {
      return true;
    }	
  }
  public boolean isLayout() {
    return false;
  }

  static public class Layout extends SyntaxDefinition {
    // Production: sig("Layout",[arg("org.rascalmpl.ast.Visibility","vis"),arg("org.rascalmpl.ast.Sym","defined"),arg("org.rascalmpl.ast.Prod","production")])
  
    
    private final org.rascalmpl.ast.Visibility vis;
    private final org.rascalmpl.ast.Sym defined;
    private final org.rascalmpl.ast.Prod production;
  
    public Layout(IConstructor node , org.rascalmpl.ast.Visibility vis,  org.rascalmpl.ast.Sym defined,  org.rascalmpl.ast.Prod production) {
      super(node);
      
      this.vis = vis;
      this.defined = defined;
      this.production = production;
    }
  
    @Override
    public boolean isLayout() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitSyntaxDefinitionLayout(this);
    }
  
    @Override
    public boolean equals(Object o) {
      if (!(o instanceof Layout)) {
        return false;
      }        
      Layout tmp = (Layout) o;
      return true && tmp.vis.equals(this.vis) && tmp.defined.equals(this.defined) && tmp.production.equals(this.production) ; 
    }
   
    @Override
    public int hashCode() {
      return 181 + 809 * vis.hashCode() + 193 * defined.hashCode() + 163 * production.hashCode() ; 
    } 
  
    
    @Override
    public org.rascalmpl.ast.Visibility getVis() {
      return this.vis;
    }
  
    @Override
    public boolean hasVis() {
      return true;
    }
    @Override
    public org.rascalmpl.ast.Sym getDefined() {
      return this.defined;
    }
  
    @Override
    public boolean hasDefined() {
      return true;
    }
    @Override
    public org.rascalmpl.ast.Prod getProduction() {
      return this.production;
    }
  
    @Override
    public boolean hasProduction() {
      return true;
    }	
  }
  public boolean isLexical() {
    return false;
  }

  static public class Lexical extends SyntaxDefinition {
    // Production: sig("Lexical",[arg("org.rascalmpl.ast.Sym","defined"),arg("org.rascalmpl.ast.Prod","production")])
  
    
    private final org.rascalmpl.ast.Sym defined;
    private final org.rascalmpl.ast.Prod production;
  
    public Lexical(IConstructor node , org.rascalmpl.ast.Sym defined,  org.rascalmpl.ast.Prod production) {
      super(node);
      
      this.defined = defined;
      this.production = production;
    }
  
    @Override
    public boolean isLexical() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitSyntaxDefinitionLexical(this);
    }
  
    @Override
    public boolean equals(Object o) {
      if (!(o instanceof Lexical)) {
        return false;
      }        
      Lexical tmp = (Lexical) o;
      return true && tmp.defined.equals(this.defined) && tmp.production.equals(this.production) ; 
    }
   
    @Override
    public int hashCode() {
      return 859 + 883 * defined.hashCode() + 509 * production.hashCode() ; 
    } 
  
    
    @Override
    public org.rascalmpl.ast.Sym getDefined() {
      return this.defined;
    }
  
    @Override
    public boolean hasDefined() {
      return true;
    }
    @Override
    public org.rascalmpl.ast.Prod getProduction() {
      return this.production;
    }
  
    @Override
    public boolean hasProduction() {
      return true;
    }	
  }
  public boolean isToken() {
    return false;
  }

  static public class Token extends SyntaxDefinition {
    // Production: sig("Token",[arg("org.rascalmpl.ast.Sym","defined"),arg("org.rascalmpl.ast.Prod","production")])
  
    
    private final org.rascalmpl.ast.Sym defined;
    private final org.rascalmpl.ast.Prod production;
  
    public Token(IConstructor node , org.rascalmpl.ast.Sym defined,  org.rascalmpl.ast.Prod production) {
      super(node);
      
      this.defined = defined;
      this.production = production;
    }
  
    @Override
    public boolean isToken() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitSyntaxDefinitionToken(this);
    }
  
    @Override
    public boolean equals(Object o) {
      if (!(o instanceof Token)) {
        return false;
      }        
      Token tmp = (Token) o;
      return true && tmp.defined.equals(this.defined) && tmp.production.equals(this.production) ; 
    }
   
    @Override
    public int hashCode() {
      return 103 + 107 * defined.hashCode() + 271 * production.hashCode() ; 
    } 
  
    
    @Override
    public org.rascalmpl.ast.Sym getDefined() {
      return this.defined;
    }
  
    @Override
    public boolean hasDefined() {
      return true;
    }
    @Override
    public org.rascalmpl.ast.Prod getProduction() {
      return this.production;
    }
  
    @Override
    public boolean hasProduction() {
      return true;
    }	
  }
}