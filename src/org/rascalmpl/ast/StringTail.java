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

public abstract class StringTail extends AbstractAST {
  public StringTail(IConstructor node) {
    super();
  }

  
  public boolean hasExpression() {
    return false;
  }

  public org.rascalmpl.ast.Expression getExpression() {
    throw new UnsupportedOperationException();
  }
  public boolean hasMid() {
    return false;
  }

  public org.rascalmpl.ast.MidStringChars getMid() {
    throw new UnsupportedOperationException();
  }
  public boolean hasPost() {
    return false;
  }

  public org.rascalmpl.ast.PostStringChars getPost() {
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

  

  
  public boolean isMidInterpolated() {
    return false;
  }

  static public class MidInterpolated extends StringTail {
    // Production: sig("MidInterpolated",[arg("org.rascalmpl.ast.MidStringChars","mid"),arg("org.rascalmpl.ast.Expression","expression"),arg("org.rascalmpl.ast.StringTail","tail")])
  
    
    private final org.rascalmpl.ast.MidStringChars mid;
    private final org.rascalmpl.ast.Expression expression;
    private final org.rascalmpl.ast.StringTail tail;
  
    public MidInterpolated(IConstructor node , org.rascalmpl.ast.MidStringChars mid,  org.rascalmpl.ast.Expression expression,  org.rascalmpl.ast.StringTail tail) {
      super(node);
      
      this.mid = mid;
      this.expression = expression;
      this.tail = tail;
    }
  
    @Override
    public boolean isMidInterpolated() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitStringTailMidInterpolated(this);
    }
  
    @Override
    public boolean equals(Object o) {
      if (!(o instanceof MidInterpolated)) {
        return false;
      }        
      MidInterpolated tmp = (MidInterpolated) o;
      return true && tmp.mid.equals(this.mid) && tmp.expression.equals(this.expression) && tmp.tail.equals(this.tail) ; 
    }
   
    @Override
    public int hashCode() {
      return 547 + 443 * mid.hashCode() + 997 * expression.hashCode() + 167 * tail.hashCode() ; 
    } 
  
    
    @Override
    public org.rascalmpl.ast.MidStringChars getMid() {
      return this.mid;
    }
  
    @Override
    public boolean hasMid() {
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
  public boolean isMidTemplate() {
    return false;
  }

  static public class MidTemplate extends StringTail {
    // Production: sig("MidTemplate",[arg("org.rascalmpl.ast.MidStringChars","mid"),arg("org.rascalmpl.ast.StringTemplate","template"),arg("org.rascalmpl.ast.StringTail","tail")])
  
    
    private final org.rascalmpl.ast.MidStringChars mid;
    private final org.rascalmpl.ast.StringTemplate template;
    private final org.rascalmpl.ast.StringTail tail;
  
    public MidTemplate(IConstructor node , org.rascalmpl.ast.MidStringChars mid,  org.rascalmpl.ast.StringTemplate template,  org.rascalmpl.ast.StringTail tail) {
      super(node);
      
      this.mid = mid;
      this.template = template;
      this.tail = tail;
    }
  
    @Override
    public boolean isMidTemplate() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitStringTailMidTemplate(this);
    }
  
    @Override
    public boolean equals(Object o) {
      if (!(o instanceof MidTemplate)) {
        return false;
      }        
      MidTemplate tmp = (MidTemplate) o;
      return true && tmp.mid.equals(this.mid) && tmp.template.equals(this.template) && tmp.tail.equals(this.tail) ; 
    }
   
    @Override
    public int hashCode() {
      return 761 + 47 * mid.hashCode() + 647 * template.hashCode() + 397 * tail.hashCode() ; 
    } 
  
    
    @Override
    public org.rascalmpl.ast.MidStringChars getMid() {
      return this.mid;
    }
  
    @Override
    public boolean hasMid() {
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
  public boolean isPost() {
    return false;
  }

  static public class Post extends StringTail {
    // Production: sig("Post",[arg("org.rascalmpl.ast.PostStringChars","post")])
  
    
    private final org.rascalmpl.ast.PostStringChars post;
  
    public Post(IConstructor node , org.rascalmpl.ast.PostStringChars post) {
      super(node);
      
      this.post = post;
    }
  
    @Override
    public boolean isPost() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitStringTailPost(this);
    }
  
    @Override
    public boolean equals(Object o) {
      if (!(o instanceof Post)) {
        return false;
      }        
      Post tmp = (Post) o;
      return true && tmp.post.equals(this.post) ; 
    }
   
    @Override
    public int hashCode() {
      return 127 + 47 * post.hashCode() ; 
    } 
  
    
    @Override
    public org.rascalmpl.ast.PostStringChars getPost() {
      return this.post;
    }
  
    @Override
    public boolean hasPost() {
      return true;
    }	
  }
}