
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


public abstract class PreModule extends AbstractAST {
  public PreModule(IConstructor node) {
    super(node);
  }
  

  public boolean hasHeader() {
    return false;
  }

  public org.rascalmpl.ast.Header getHeader() {
    throw new UnsupportedOperationException();
  }


static public class Ambiguity extends PreModule {
  private final java.util.List<org.rascalmpl.ast.PreModule> alternatives;

  public Ambiguity(IConstructor node, java.util.List<org.rascalmpl.ast.PreModule> alternatives) {
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
  
  public java.util.List<org.rascalmpl.ast.PreModule> getAlternatives() {
   return alternatives;
  }

  public <T> T accept(IASTVisitor<T> v) {
	return v.visitPreModuleAmbiguity(this);
  }
}





  public boolean isDefault() {
    return false;
  }
  
static public class Default extends PreModule {
  // Production: sig("Default",[arg("org.rascalmpl.ast.Header","header")])

  
     private final org.rascalmpl.ast.Header header;
  

  
public Default(IConstructor node , org.rascalmpl.ast.Header header) {
  super(node);
  
    this.header = header;
  
}


  @Override
  public boolean isDefault() { 
    return true; 
  }

  @Override
  public <T> T accept(IASTVisitor<T> visitor) {
    return visitor.visitPreModuleDefault(this);
  }
  
  
     @Override
     public org.rascalmpl.ast.Header getHeader() {
        return this.header;
     }
     
     @Override
     public boolean hasHeader() {
        return true;
     }
  	
}



}
