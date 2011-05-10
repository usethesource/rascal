
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


public abstract class Parameters extends AbstractAST {
  public Parameters(IConstructor node) {
    super(node);
  }
  

  public boolean hasFormals() {
    return false;
  }

  public org.rascalmpl.ast.Formals getFormals() {
    throw new UnsupportedOperationException();
  }


static public class Ambiguity extends Parameters {
  private final java.util.List<org.rascalmpl.ast.Parameters> alternatives;

  public Ambiguity(IConstructor node, java.util.List<org.rascalmpl.ast.Parameters> alternatives) {
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
  
  public java.util.List<org.rascalmpl.ast.Parameters> getAlternatives() {
   return alternatives;
  }

  public <T> T accept(IASTVisitor<T> v) {
	return v.visitParametersAmbiguity(this);
  }
}





  public boolean isVarArgs() {
    return false;
  }
  
static public class VarArgs extends Parameters {
  // Production: sig("VarArgs",[arg("org.rascalmpl.ast.Formals","formals")])

  
     private final org.rascalmpl.ast.Formals formals;
  

  
public VarArgs(IConstructor node , org.rascalmpl.ast.Formals formals) {
  super(node);
  
    this.formals = formals;
  
}


  @Override
  public boolean isVarArgs() { 
    return true; 
  }

  @Override
  public <T> T accept(IASTVisitor<T> visitor) {
    return visitor.visitParametersVarArgs(this);
  }
  
  
     @Override
     public org.rascalmpl.ast.Formals getFormals() {
        return this.formals;
     }
     
     @Override
     public boolean hasFormals() {
        return true;
     }
  	
}


  public boolean isDefault() {
    return false;
  }
  
static public class Default extends Parameters {
  // Production: sig("Default",[arg("org.rascalmpl.ast.Formals","formals")])

  
     private final org.rascalmpl.ast.Formals formals;
  

  
public Default(IConstructor node , org.rascalmpl.ast.Formals formals) {
  super(node);
  
    this.formals = formals;
  
}


  @Override
  public boolean isDefault() { 
    return true; 
  }

  @Override
  public <T> T accept(IASTVisitor<T> visitor) {
    return visitor.visitParametersDefault(this);
  }
  
  
     @Override
     public org.rascalmpl.ast.Formals getFormals() {
        return this.formals;
     }
     
     @Override
     public boolean hasFormals() {
        return true;
     }
  	
}



}
