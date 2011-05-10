
package org.rascalmpl.ast;


import org.eclipse.imp.pdb.facts.IConstructor;

import org.eclipse.imp.pdb.facts.IConstructor;

import org.eclipse.imp.pdb.facts.IValue;

import org.rascalmpl.interpreter.Evaluator;

import org.rascalmpl.interpreter.asserts.Ambiguous;

import org.rascalmpl.interpreter.env.Environment;

import org.rascalmpl.interpreter.matching.IBooleanResult;

import org.rascalmpl.interpreter.matching.IMatchingResult;

import org.rascalmpl.interpreter.result.Result;


public abstract class TypeVar extends AbstractAST {
  public TypeVar(IConstructor node) {
    super(node);
  }
  

  public boolean hasBound() {
    return false;
  }

  public org.rascalmpl.ast.Type getBound() {
    throw new UnsupportedOperationException();
  }

  public boolean hasName() {
    return false;
  }

  public org.rascalmpl.ast.Name getName() {
    throw new UnsupportedOperationException();
  }


static public class Ambiguity extends TypeVar {
  private final java.util.List<org.rascalmpl.ast.TypeVar> alternatives;

  public Ambiguity(IConstructor node, java.util.List<org.rascalmpl.ast.TypeVar> alternatives) {
    super(node);
    this.alternatives = java.util.Collections.unmodifiableList(alternatives);
  }

  @Override
  public Result<IValue> interpret(Evaluator __eval) {
    throw new Ambiguous((IConstructor) this.getTree());
  }
  
  @Override
  public org.eclipse.imp.pdb.facts.type.Type typeOf(Environment env) {
    throw new Ambiguous((IConstructor) this.getTree());
  }
  
  public java.util.List<org.rascalmpl.ast.TypeVar> getAlternatives() {
   return alternatives;
  }

  public <T> T accept(IASTVisitor<T> v) {
	return v.visitTypeVarAmbiguity(this);
  }
}





  public boolean isFree() {
    return false;
  }
  
static public class Free extends TypeVar {
  // Production: sig("Free",[arg("org.rascalmpl.ast.Name","name")])

  
     private final org.rascalmpl.ast.Name name;
  

  
public Free(IConstructor node , org.rascalmpl.ast.Name name) {
  super(node);
  
    this.name = name;
  
}


  @Override
  public boolean isFree() { 
    return true; 
  }

  @Override
  public <T> T accept(IASTVisitor<T> visitor) {
    return visitor.visitTypeVarFree(this);
  }
  
  
     @Override
     public org.rascalmpl.ast.Name getName() {
        return this.name;
     }
     
     @Override
     public boolean hasName() {
        return true;
     }
  	
}


  public boolean isBounded() {
    return false;
  }
  
static public class Bounded extends TypeVar {
  // Production: sig("Bounded",[arg("org.rascalmpl.ast.Name","name"),arg("org.rascalmpl.ast.Type","bound")])

  
     private final org.rascalmpl.ast.Name name;
  
     private final org.rascalmpl.ast.Type bound;
  

  
public Bounded(IConstructor node , org.rascalmpl.ast.Name name,  org.rascalmpl.ast.Type bound) {
  super(node);
  
    this.name = name;
  
    this.bound = bound;
  
}


  @Override
  public boolean isBounded() { 
    return true; 
  }

  @Override
  public <T> T accept(IASTVisitor<T> visitor) {
    return visitor.visitTypeVarBounded(this);
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
     public org.rascalmpl.ast.Type getBound() {
        return this.bound;
     }
     
     @Override
     public boolean hasBound() {
        return true;
     }
  	
}



}
