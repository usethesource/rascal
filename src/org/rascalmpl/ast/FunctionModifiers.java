
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


public abstract class FunctionModifiers extends AbstractAST {
  public FunctionModifiers(IConstructor node) {
    super(node);
  }
  

  public boolean hasModifiers() {
    return false;
  }

  public java.util.List<org.rascalmpl.ast.FunctionModifier> getModifiers() {
    throw new UnsupportedOperationException();
  }


static public class Ambiguity extends FunctionModifiers {
  private final java.util.List<org.rascalmpl.ast.FunctionModifiers> alternatives;

  public Ambiguity(IConstructor node, java.util.List<org.rascalmpl.ast.FunctionModifiers> alternatives) {
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
  
  public java.util.List<org.rascalmpl.ast.FunctionModifiers> getAlternatives() {
   return alternatives;
  }

  public <T> T accept(IASTVisitor<T> v) {
	return v.visitFunctionModifiersAmbiguity(this);
  }
}





  public boolean isList() {
    return false;
  }
  
static public class List extends FunctionModifiers {
  // Production: sig("List",[arg("java.util.List\<org.rascalmpl.ast.FunctionModifier\>","modifiers")])

  
     private final java.util.List<org.rascalmpl.ast.FunctionModifier> modifiers;
  

  
public List(IConstructor node , java.util.List<org.rascalmpl.ast.FunctionModifier> modifiers) {
  super(node);
  
    this.modifiers = modifiers;
  
}


  @Override
  public boolean isList() { 
    return true; 
  }

  @Override
  public <T> T accept(IASTVisitor<T> visitor) {
    return visitor.visitFunctionModifiersList(this);
  }
  
  
     @Override
     public java.util.List<org.rascalmpl.ast.FunctionModifier> getModifiers() {
        return this.modifiers;
     }
     
     @Override
     public boolean hasModifiers() {
        return true;
     }
  	
}



}
