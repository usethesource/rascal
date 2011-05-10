
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


public abstract class Body extends AbstractAST {
  public Body(IConstructor node) {
    super(node);
  }
  

  public boolean hasToplevels() {
    return false;
  }

  public java.util.List<org.rascalmpl.ast.Toplevel> getToplevels() {
    throw new UnsupportedOperationException();
  }


static public class Ambiguity extends Body {
  private final java.util.List<org.rascalmpl.ast.Body> alternatives;

  public Ambiguity(IConstructor node, java.util.List<org.rascalmpl.ast.Body> alternatives) {
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
  
  public java.util.List<org.rascalmpl.ast.Body> getAlternatives() {
   return alternatives;
  }

  public <T> T accept(IASTVisitor<T> v) {
	return v.visitBodyAmbiguity(this);
  }
}





  public boolean isToplevels() {
    return false;
  }
  
static public class Toplevels extends Body {
  // Production: sig("Toplevels",[arg("java.util.List\<org.rascalmpl.ast.Toplevel\>","toplevels")])

  
     private final java.util.List<org.rascalmpl.ast.Toplevel> toplevels;
  

  
public Toplevels(IConstructor node , java.util.List<org.rascalmpl.ast.Toplevel> toplevels) {
  super(node);
  
    this.toplevels = toplevels;
  
}


  @Override
  public boolean isToplevels() { 
    return true; 
  }

  @Override
  public <T> T accept(IASTVisitor<T> visitor) {
    return visitor.visitBodyToplevels(this);
  }
  
  
     @Override
     public java.util.List<org.rascalmpl.ast.Toplevel> getToplevels() {
        return this.toplevels;
     }
     
     @Override
     public boolean hasToplevels() {
        return true;
     }
  	
}



}
