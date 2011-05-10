
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


public abstract class Formals extends AbstractAST {
  public Formals(IConstructor node) {
    super(node);
  }
  

  public boolean hasFormals() {
    return false;
  }

  public java.util.List<org.rascalmpl.ast.Expression> getFormals() {
    throw new UnsupportedOperationException();
  }


static public class Ambiguity extends Formals {
  private final java.util.List<org.rascalmpl.ast.Formals> alternatives;

  public Ambiguity(IConstructor node, java.util.List<org.rascalmpl.ast.Formals> alternatives) {
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
  
  public java.util.List<org.rascalmpl.ast.Formals> getAlternatives() {
   return alternatives;
  }

  public <T> T accept(IASTVisitor<T> v) {
	return v.visitFormalsAmbiguity(this);
  }
}





  public boolean isDefault() {
    return false;
  }
  
static public class Default extends Formals {
  // Production: sig("Default",[arg("java.util.List\<org.rascalmpl.ast.Expression\>","formals")])

  
     private final java.util.List<org.rascalmpl.ast.Expression> formals;
  

  
public Default(IConstructor node , java.util.List<org.rascalmpl.ast.Expression> formals) {
  super(node);
  
    this.formals = formals;
  
}


  @Override
  public boolean isDefault() { 
    return true; 
  }

  @Override
  public <T> T accept(IASTVisitor<T> visitor) {
    return visitor.visitFormalsDefault(this);
  }
  
  
     @Override
     public java.util.List<org.rascalmpl.ast.Expression> getFormals() {
        return this.formals;
     }
     
     @Override
     public boolean hasFormals() {
        return true;
     }
  	
}



}
