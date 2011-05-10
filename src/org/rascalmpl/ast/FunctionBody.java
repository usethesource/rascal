
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


public abstract class FunctionBody extends AbstractAST {
  public FunctionBody(IConstructor node) {
    super(node);
  }
  

  public boolean hasStatements() {
    return false;
  }

  public java.util.List<org.rascalmpl.ast.Statement> getStatements() {
    throw new UnsupportedOperationException();
  }


static public class Ambiguity extends FunctionBody {
  private final java.util.List<org.rascalmpl.ast.FunctionBody> alternatives;

  public Ambiguity(IConstructor node, java.util.List<org.rascalmpl.ast.FunctionBody> alternatives) {
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
  
  public java.util.List<org.rascalmpl.ast.FunctionBody> getAlternatives() {
   return alternatives;
  }

  public <T> T accept(IASTVisitor<T> v) {
	return v.visitFunctionBodyAmbiguity(this);
  }
}





  public boolean isDefault() {
    return false;
  }
  
static public class Default extends FunctionBody {
  // Production: sig("Default",[arg("java.util.List\<org.rascalmpl.ast.Statement\>","statements")])

  
     private final java.util.List<org.rascalmpl.ast.Statement> statements;
  

  
public Default(IConstructor node , java.util.List<org.rascalmpl.ast.Statement> statements) {
  super(node);
  
    this.statements = statements;
  
}


  @Override
  public boolean isDefault() { 
    return true; 
  }

  @Override
  public <T> T accept(IASTVisitor<T> visitor) {
    return visitor.visitFunctionBodyDefault(this);
  }
  
  
     @Override
     public java.util.List<org.rascalmpl.ast.Statement> getStatements() {
        return this.statements;
     }
     
     @Override
     public boolean hasStatements() {
        return true;
     }
  	
}



}
