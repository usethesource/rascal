
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


public abstract class ModuleActuals extends AbstractAST {
  public ModuleActuals(IConstructor node) {
    super(node);
  }
  

  public boolean hasTypes() {
    return false;
  }

  public java.util.List<org.rascalmpl.ast.Type> getTypes() {
    throw new UnsupportedOperationException();
  }


static public class Ambiguity extends ModuleActuals {
  private final java.util.List<org.rascalmpl.ast.ModuleActuals> alternatives;

  public Ambiguity(IConstructor node, java.util.List<org.rascalmpl.ast.ModuleActuals> alternatives) {
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
  
  public java.util.List<org.rascalmpl.ast.ModuleActuals> getAlternatives() {
   return alternatives;
  }

  public <T> T accept(IASTVisitor<T> v) {
	return v.visitModuleActualsAmbiguity(this);
  }
}





  public boolean isDefault() {
    return false;
  }
  
static public class Default extends ModuleActuals {
  // Production: sig("Default",[arg("java.util.List\<org.rascalmpl.ast.Type\>","types")])

  
     private final java.util.List<org.rascalmpl.ast.Type> types;
  

  
public Default(IConstructor node , java.util.List<org.rascalmpl.ast.Type> types) {
  super(node);
  
    this.types = types;
  
}


  @Override
  public boolean isDefault() { 
    return true; 
  }

  @Override
  public <T> T accept(IASTVisitor<T> visitor) {
    return visitor.visitModuleActualsDefault(this);
  }
  
  
     @Override
     public java.util.List<org.rascalmpl.ast.Type> getTypes() {
        return this.types;
     }
     
     @Override
     public boolean hasTypes() {
        return true;
     }
  	
}



}
