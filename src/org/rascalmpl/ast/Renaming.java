
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


public abstract class Renaming extends AbstractAST {
  public Renaming(IConstructor node) {
    super(node);
  }
  

  public boolean hasFrom() {
    return false;
  }

  public org.rascalmpl.ast.Name getFrom() {
    throw new UnsupportedOperationException();
  }

  public boolean hasTo() {
    return false;
  }

  public org.rascalmpl.ast.Name getTo() {
    throw new UnsupportedOperationException();
  }


static public class Ambiguity extends Renaming {
  private final java.util.List<org.rascalmpl.ast.Renaming> alternatives;

  public Ambiguity(IConstructor node, java.util.List<org.rascalmpl.ast.Renaming> alternatives) {
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
  
  public java.util.List<org.rascalmpl.ast.Renaming> getAlternatives() {
   return alternatives;
  }

  public <T> T accept(IASTVisitor<T> v) {
	return v.visitRenamingAmbiguity(this);
  }
}





  public boolean isDefault() {
    return false;
  }
  
static public class Default extends Renaming {
  // Production: sig("Default",[arg("org.rascalmpl.ast.Name","from"),arg("org.rascalmpl.ast.Name","to")])

  
     private final org.rascalmpl.ast.Name from;
  
     private final org.rascalmpl.ast.Name to;
  

  
public Default(IConstructor node , org.rascalmpl.ast.Name from,  org.rascalmpl.ast.Name to) {
  super(node);
  
    this.from = from;
  
    this.to = to;
  
}


  @Override
  public boolean isDefault() { 
    return true; 
  }

  @Override
  public <T> T accept(IASTVisitor<T> visitor) {
    return visitor.visitRenamingDefault(this);
  }
  
  
     @Override
     public org.rascalmpl.ast.Name getFrom() {
        return this.from;
     }
     
     @Override
     public boolean hasFrom() {
        return true;
     }
  
     @Override
     public org.rascalmpl.ast.Name getTo() {
        return this.to;
     }
     
     @Override
     public boolean hasTo() {
        return true;
     }
  	
}



}
