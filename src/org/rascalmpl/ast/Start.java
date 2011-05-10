
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


public abstract class Start extends AbstractAST {
  public Start(IConstructor node) {
    super(node);
  }
  


static public class Ambiguity extends Start {
  private final java.util.List<org.rascalmpl.ast.Start> alternatives;

  public Ambiguity(IConstructor node, java.util.List<org.rascalmpl.ast.Start> alternatives) {
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
  
  public java.util.List<org.rascalmpl.ast.Start> getAlternatives() {
   return alternatives;
  }

  public <T> T accept(IASTVisitor<T> v) {
	return v.visitStartAmbiguity(this);
  }
}





  public boolean isAbsent() {
    return false;
  }
  
static public class Absent extends Start {
  // Production: sig("Absent",[])

  

  
public Absent(IConstructor node ) {
  super(node);
  
}


  @Override
  public boolean isAbsent() { 
    return true; 
  }

  @Override
  public <T> T accept(IASTVisitor<T> visitor) {
    return visitor.visitStartAbsent(this);
  }
  
  	
}


  public boolean isPresent() {
    return false;
  }
  
static public class Present extends Start {
  // Production: sig("Present",[])

  

  
public Present(IConstructor node ) {
  super(node);
  
}


  @Override
  public boolean isPresent() { 
    return true; 
  }

  @Override
  public <T> T accept(IASTVisitor<T> visitor) {
    return visitor.visitStartPresent(this);
  }
  
  	
}



}
