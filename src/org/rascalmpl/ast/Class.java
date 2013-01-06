/*******************************************************************************
 * Copyright (c) 2009-2012 CWI
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
import org.eclipse.imp.pdb.facts.IValue;
import org.rascalmpl.interpreter.IEvaluator;
import org.rascalmpl.interpreter.asserts.Ambiguous;
import org.rascalmpl.interpreter.env.Environment;
import org.rascalmpl.interpreter.result.Result;

public abstract class Class extends AbstractAST {
  public Class(IConstructor node) {
    super();
  }

  
  public boolean hasRanges() {
    return false;
  }

  public java.util.List<org.rascalmpl.ast.Range> getRanges() {
    throw new UnsupportedOperationException();
  }
  public boolean hasCharClass() {
    return false;
  }

  public org.rascalmpl.ast.Class getCharClass() {
    throw new UnsupportedOperationException();
  }
  public boolean hasCharclass() {
    return false;
  }

  public org.rascalmpl.ast.Class getCharclass() {
    throw new UnsupportedOperationException();
  }
  public boolean hasLhs() {
    return false;
  }

  public org.rascalmpl.ast.Class getLhs() {
    throw new UnsupportedOperationException();
  }
  public boolean hasRhs() {
    return false;
  }

  public org.rascalmpl.ast.Class getRhs() {
    throw new UnsupportedOperationException();
  }

  static public class Ambiguity extends Class {
    private final java.util.List<org.rascalmpl.ast.Class> alternatives;
    private final IConstructor node;
           
    public Ambiguity(IConstructor node, java.util.List<org.rascalmpl.ast.Class> alternatives) {
      super(node);
      this.node = node;
      this.alternatives = java.util.Collections.unmodifiableList(alternatives);
    }
    
    @Override
    public IConstructor getTree() {
      return node;
    }
  
    @Override
    public AbstractAST findNode(int offset) {
      return null;
    }
  
    @Override
    public Result<IValue> interpret(IEvaluator<Result<IValue>> __eval) {
      throw new Ambiguous(src);
    }
      
    @Override
    public org.eclipse.imp.pdb.facts.type.Type typeOf(Environment env) {
      throw new Ambiguous(src);
    }
    
    public java.util.List<org.rascalmpl.ast.Class> getAlternatives() {
      return alternatives;
    }
    
    public <T> T accept(IASTVisitor<T> v) {
    	return v.visitClassAmbiguity(this);
    }
  }

  

  
  public boolean isBracket() {
    return false;
  }

  static public class Bracket extends Class {
    // Production: sig("Bracket",[arg("org.rascalmpl.ast.Class","charclass")])
  
    
    private final org.rascalmpl.ast.Class charclass;
  
    public Bracket(IConstructor node , org.rascalmpl.ast.Class charclass) {
      super(node);
      
      this.charclass = charclass;
    }
  
    @Override
    public boolean isBracket() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitClassBracket(this);
    }
  
    
    @Override
    public org.rascalmpl.ast.Class getCharclass() {
      return this.charclass;
    }
  
    @Override
    public boolean hasCharclass() {
      return true;
    }	
  }
  public boolean isComplement() {
    return false;
  }

  static public class Complement extends Class {
    // Production: sig("Complement",[arg("org.rascalmpl.ast.Class","charClass")])
  
    
    private final org.rascalmpl.ast.Class charClass;
  
    public Complement(IConstructor node , org.rascalmpl.ast.Class charClass) {
      super(node);
      
      this.charClass = charClass;
    }
  
    @Override
    public boolean isComplement() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitClassComplement(this);
    }
  
    
    @Override
    public org.rascalmpl.ast.Class getCharClass() {
      return this.charClass;
    }
  
    @Override
    public boolean hasCharClass() {
      return true;
    }	
  }
  public boolean isDifference() {
    return false;
  }

  static public class Difference extends Class {
    // Production: sig("Difference",[arg("org.rascalmpl.ast.Class","lhs"),arg("org.rascalmpl.ast.Class","rhs")])
  
    
    private final org.rascalmpl.ast.Class lhs;
    private final org.rascalmpl.ast.Class rhs;
  
    public Difference(IConstructor node , org.rascalmpl.ast.Class lhs,  org.rascalmpl.ast.Class rhs) {
      super(node);
      
      this.lhs = lhs;
      this.rhs = rhs;
    }
  
    @Override
    public boolean isDifference() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitClassDifference(this);
    }
  
    
    @Override
    public org.rascalmpl.ast.Class getLhs() {
      return this.lhs;
    }
  
    @Override
    public boolean hasLhs() {
      return true;
    }
    @Override
    public org.rascalmpl.ast.Class getRhs() {
      return this.rhs;
    }
  
    @Override
    public boolean hasRhs() {
      return true;
    }	
  }
  public boolean isIntersection() {
    return false;
  }

  static public class Intersection extends Class {
    // Production: sig("Intersection",[arg("org.rascalmpl.ast.Class","lhs"),arg("org.rascalmpl.ast.Class","rhs")])
  
    
    private final org.rascalmpl.ast.Class lhs;
    private final org.rascalmpl.ast.Class rhs;
  
    public Intersection(IConstructor node , org.rascalmpl.ast.Class lhs,  org.rascalmpl.ast.Class rhs) {
      super(node);
      
      this.lhs = lhs;
      this.rhs = rhs;
    }
  
    @Override
    public boolean isIntersection() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitClassIntersection(this);
    }
  
    
    @Override
    public org.rascalmpl.ast.Class getLhs() {
      return this.lhs;
    }
  
    @Override
    public boolean hasLhs() {
      return true;
    }
    @Override
    public org.rascalmpl.ast.Class getRhs() {
      return this.rhs;
    }
  
    @Override
    public boolean hasRhs() {
      return true;
    }	
  }
  public boolean isSimpleCharclass() {
    return false;
  }

  static public class SimpleCharclass extends Class {
    // Production: sig("SimpleCharclass",[arg("java.util.List\<org.rascalmpl.ast.Range\>","ranges")])
  
    
    private final java.util.List<org.rascalmpl.ast.Range> ranges;
  
    public SimpleCharclass(IConstructor node , java.util.List<org.rascalmpl.ast.Range> ranges) {
      super(node);
      
      this.ranges = ranges;
    }
  
    @Override
    public boolean isSimpleCharclass() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitClassSimpleCharclass(this);
    }
  
    
    @Override
    public java.util.List<org.rascalmpl.ast.Range> getRanges() {
      return this.ranges;
    }
  
    @Override
    public boolean hasRanges() {
      return true;
    }	
  }
  public boolean isUnion() {
    return false;
  }

  static public class Union extends Class {
    // Production: sig("Union",[arg("org.rascalmpl.ast.Class","lhs"),arg("org.rascalmpl.ast.Class","rhs")])
  
    
    private final org.rascalmpl.ast.Class lhs;
    private final org.rascalmpl.ast.Class rhs;
  
    public Union(IConstructor node , org.rascalmpl.ast.Class lhs,  org.rascalmpl.ast.Class rhs) {
      super(node);
      
      this.lhs = lhs;
      this.rhs = rhs;
    }
  
    @Override
    public boolean isUnion() { 
      return true; 
    }
  
    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
      return visitor.visitClassUnion(this);
    }
  
    
    @Override
    public org.rascalmpl.ast.Class getLhs() {
      return this.lhs;
    }
  
    @Override
    public boolean hasLhs() {
      return true;
    }
    @Override
    public org.rascalmpl.ast.Class getRhs() {
      return this.rhs;
    }
  
    @Override
    public boolean hasRhs() {
      return true;
    }	
  }
}