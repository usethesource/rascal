
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


public abstract class NoElseMayFollow extends AbstractAST {
  public NoElseMayFollow(IConstructor node) {
    super(node);
  }
  


static public class Ambiguity extends NoElseMayFollow {
  private final java.util.List<org.rascalmpl.ast.NoElseMayFollow> alternatives;

  public Ambiguity(IConstructor node, java.util.List<org.rascalmpl.ast.NoElseMayFollow> alternatives) {
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
  
  public java.util.List<org.rascalmpl.ast.NoElseMayFollow> getAlternatives() {
   return alternatives;
  }

  public <T> T accept(IASTVisitor<T> v) {
	return v.visitNoElseMayFollowAmbiguity(this);
  }
}





  public boolean isDefault() {
    return false;
  }
  
static public class Default extends NoElseMayFollow {
  // Production: sig("Default",[])

  

  
public Default(IConstructor node ) {
  super(node);
  
}


  @Override
  public boolean isDefault() { 
    return true; 
  }

  @Override
  public <T> T accept(IASTVisitor<T> visitor) {
    return visitor.visitNoElseMayFollowDefault(this);
  }
  
  	
}



}
