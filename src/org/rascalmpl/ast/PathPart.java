/*******************************************************************************
 * Copyright (c) 2009-2011 CWI
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
 *******************************************************************************/
package org.rascalmpl.ast;


import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.ISourceLocation;
import org.eclipse.imp.pdb.facts.IValue;
import org.rascalmpl.interpreter.Evaluator;
import org.rascalmpl.interpreter.asserts.Ambiguous;
import org.rascalmpl.interpreter.env.Environment;
import org.rascalmpl.interpreter.result.Result;

public abstract class PathPart extends AbstractAST {
  public PathPart(IConstructor node) {
    super(node);
  }

  
  public boolean hasPre() {
    return false;
  }

  public org.rascalmpl.ast.PrePathChars getPre() {
    throw new UnsupportedOperationException();
  }
  public boolean hasExpression() {
    return false;
  }

  public org.rascalmpl.ast.Expression getExpression() {
    throw new UnsupportedOperationException();
  }
  public boolean hasTail() {
    return false;
  }

  public org.rascalmpl.ast.PathTail getTail() {
    throw new UnsupportedOperationException();
  }
  public boolean hasPathChars() {
    return false;
  }

  public org.rascalmpl.ast.PathChars getPathChars() {
    throw new UnsupportedOperationException();
  }

  static public class Ambiguity extends PathPart {
    private final java.util.List<org.rascalmpl.ast.PathPart> alternatives;
    private final IConstructor node;
           
    public Ambiguity(IConstructor node, java.util.List<org.rascalmpl.ast.PathPart> alternatives) {
      super(node);
      this.node = node;
      this.alternatives = java.util.Collections.unmodifiableList(alternatives);
    }
    
    @Override
    public IConstructor getTree() {
      return node;
    }
  
  
    @Override
    public Result<IValue> interpret(Evaluator __eval) {
      throw new Ambiguous(node);
    }
      
    @Override
    public org.eclipse.imp.pdb.facts.type.Type typeOf(Environment env) {
      throw new Ambiguous(node);
    }
    
    public java.util.List<org.rascalmpl.ast.PathPart> getAlternatives() {
      return alternatives;
    }
    
    public <T> T accept(IASTVisitor<T> v) {
    	return v.visitPathPartAmbiguity(this);
    }
  }

  

  
  public boolean isNonInterpolated() {
    return false;
  }

  static public class NonInterpolated extends PathPart {
    // Production: sig("NonInterpolated",[arg("org.rascalmpl.ast.PathChars","pathChars")])
  
    
    private final org.rascalmpl.ast.PathChars pathChars;
  
    public NonInterpolated(IConstructor node , org.rascalmpl.ast.PathChars pathChars) {
      super(node);
      
      this.pathChars = pathChars;
    }
  
    @Override
    public boolean isNonInterpolated() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitPathPartNonInterpolated(this);
    }
  
    
    @Override
    public org.rascalmpl.ast.PathChars getPathChars() {
      return this.pathChars;
    }
  
  
    @Override
    public boolean hasPathChars() {
      return true;
    }	
  }
  public boolean isInterpolated() {
    return false;
  }

  static public class Interpolated extends PathPart {
    // Production: sig("Interpolated",[arg("org.rascalmpl.ast.PrePathChars","pre"),arg("org.rascalmpl.ast.Expression","expression"),arg("org.rascalmpl.ast.PathTail","tail")])
  
    
    private final org.rascalmpl.ast.PrePathChars pre;
    private final org.rascalmpl.ast.Expression expression;
    private final org.rascalmpl.ast.PathTail tail;
  
    public Interpolated(IConstructor node , org.rascalmpl.ast.PrePathChars pre,  org.rascalmpl.ast.Expression expression,  org.rascalmpl.ast.PathTail tail) {
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
      return visitor.visitPathPartInterpolated(this);
    }
  
    
    @Override
    public org.rascalmpl.ast.PrePathChars getPre() {
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
    public org.rascalmpl.ast.PathTail getTail() {
      return this.tail;
    }
  
  
    @Override
    public boolean hasTail() {
      return true;
    }	
  }
}
