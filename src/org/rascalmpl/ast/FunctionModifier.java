
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


public abstract class FunctionModifier extends AbstractAST {
  public FunctionModifier(IConstructor node) {
    super(node);
  }
  


static public class Ambiguity extends FunctionModifier {
  private final java.util.List<org.rascalmpl.ast.FunctionModifier> alternatives;

  public Ambiguity(IConstructor node, java.util.List<org.rascalmpl.ast.FunctionModifier> alternatives) {
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
  
  public java.util.List<org.rascalmpl.ast.FunctionModifier> getAlternatives() {
   return alternatives;
  }

  public <T> T accept(IASTVisitor<T> v) {
	return v.visitFunctionModifierAmbiguity(this);
  }
}





  public boolean isDefault() {
    return false;
  }
  
static public class Default extends FunctionModifier {
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
    return visitor.visitFunctionModifierDefault(this);
  }
  
  	
}


  public boolean isJava() {
    return false;
  }
  
static public class Java extends FunctionModifier {
  // Production: sig("Java",[])

  

  
public Java(IConstructor node ) {
  super(node);
  
}


  @Override
  public boolean isJava() { 
    return true; 
  }

  @Override
  public <T> T accept(IASTVisitor<T> visitor) {
    return visitor.visitFunctionModifierJava(this);
  }
  
  	
}



}
