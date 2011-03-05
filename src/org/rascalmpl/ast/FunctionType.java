
package org.rascalmpl.ast;


import org.eclipse.imp.pdb.facts.INode;

import org.rascalmpl.interpreter.asserts.Ambiguous;

import org.eclipse.imp.pdb.facts.IConstructor;

import org.eclipse.imp.pdb.facts.IValue;

import org.rascalmpl.interpreter.BooleanEvaluator;

import org.rascalmpl.interpreter.Evaluator;

import org.rascalmpl.interpreter.PatternEvaluator;

import org.rascalmpl.interpreter.asserts.Ambiguous;

import org.rascalmpl.interpreter.env.Environment;

import org.rascalmpl.interpreter.matching.IBooleanResult;

import org.rascalmpl.interpreter.matching.IMatchingResult;

import org.rascalmpl.interpreter.result.Result;


public abstract class FunctionType extends AbstractAST {
  public FunctionType(INode node) {
    super(node);
  }
  

  public boolean hasArguments() {
    return false;
  }

  public java.util.List<org.rascalmpl.ast.TypeArg> getArguments() {
    throw new UnsupportedOperationException();
  }

  public boolean hasType() {
    return false;
  }

  public org.rascalmpl.ast.Type getType() {
    throw new UnsupportedOperationException();
  }


static public class Ambiguity extends FunctionType {
  private final java.util.List<org.rascalmpl.ast.FunctionType> alternatives;

  public Ambiguity(INode node, java.util.List<org.rascalmpl.ast.FunctionType> alternatives) {
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
  
  @Override
  public IBooleanResult buildBooleanBacktracker(BooleanEvaluator __eval) {
    throw new Ambiguous((IConstructor) this.getTree());
  }

  @Override
  public IMatchingResult buildMatcher(PatternEvaluator __eval) {
    throw new Ambiguous((IConstructor) this.getTree());
  }
  
  public java.util.List<org.rascalmpl.ast.FunctionType> getAlternatives() {
   return alternatives;
  }

  public <T> T accept(IASTVisitor<T> v) {
	return v.visitFunctionTypeAmbiguity(this);
  }
}





  public boolean isTypeArguments() {
    return false;
  }
  
static public class TypeArguments extends FunctionType {
  // Production: sig("TypeArguments",[arg("org.rascalmpl.ast.Type","type"),arg("java.util.List\<org.rascalmpl.ast.TypeArg\>","arguments")])

  
     private final org.rascalmpl.ast.Type type;
  
     private final java.util.List<org.rascalmpl.ast.TypeArg> arguments;
  

  
public TypeArguments(INode node , org.rascalmpl.ast.Type type,  java.util.List<org.rascalmpl.ast.TypeArg> arguments) {
  super(node);
  
    this.type = type;
  
    this.arguments = arguments;
  
}


  @Override
  public boolean isTypeArguments() { 
    return true; 
  }

  @Override
  public <T> T accept(IASTVisitor<T> visitor) {
    return visitor.visitFunctionTypeTypeArguments(this);
  }
  
  
     @Override
     public org.rascalmpl.ast.Type getType() {
        return this.type;
     }
     
     @Override
     public boolean hasType() {
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
  	
}



}
