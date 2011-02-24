
package org.rascalmpl.ast;


import org.eclipse.imp.pdb.facts.INode;

import org.rascalmpl.interpreter.asserts.Ambiguous;

import org.eclipse.imp.pdb.facts.IConstructor;

import org.eclipse.imp.pdb.facts.IValue;

import org.eclipse.imp.pdb.facts.type.Type;

import org.rascalmpl.interpreter.BooleanEvaluator;

import org.rascalmpl.interpreter.Evaluator;

import org.rascalmpl.interpreter.PatternEvaluator;

import org.rascalmpl.interpreter.asserts.Ambiguous;

import org.rascalmpl.interpreter.env.Environment;

import org.rascalmpl.interpreter.matching.IBooleanResult;

import org.rascalmpl.interpreter.matching.IMatchingResult;

import org.rascalmpl.interpreter.result.Result;


public abstract class Toplevel extends AbstractAST {
  public Toplevel(INode node) {
    super(node);
  }
  

  public boolean hasDeclaration() {
    return false;
  }

  public org.rascalmpl.ast.Declaration getDeclaration() {
    throw new UnsupportedOperationException();
  }


static public class Ambiguity extends Toplevel {
  private final java.util.List<org.rascalmpl.ast.Toplevel> alternatives;

  public Ambiguity(INode node, java.util.List<org.rascalmpl.ast.Toplevel> alternatives) {
    super(node);
    this.alternatives = java.util.Collections.unmodifiableList(alternatives);
  }

  @Override
  public Result<IValue> interpret(Evaluator __eval) {
    throw new Ambiguous((IConstructor) this.getTree());
  }
  
  @Override
  public Type typeOf(Environment env) {
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
  
  public java.util.List<org.rascalmpl.ast.Toplevel> getAlternatives() {
   return alternatives;
  }

  public <T> T accept(IASTVisitor<T> v) {
	return v.visitToplevelAmbiguity(this);
  }
}





  public boolean isGivenVisibility() {
    return false;
  }
  
static public class GivenVisibility extends Toplevel {
  // Production: sig("GivenVisibility",[arg("org.rascalmpl.ast.Declaration","declaration")])

  
     private final org.rascalmpl.ast.Declaration declaration;
  

  
public GivenVisibility(INode node , org.rascalmpl.ast.Declaration declaration) {
  super(node);
  
    this.declaration = declaration;
  
}


  @Override
  public boolean isGivenVisibility() { 
    return true; 
  }

  @Override
  public <T> T accept(IASTVisitor<T> visitor) {
    return visitor.visitToplevelGivenVisibility(this);
  }
  
  
     @Override
     public org.rascalmpl.ast.Declaration getDeclaration() {
        return this.declaration;
     }
     
     @Override
     public boolean hasDeclaration() {
        return true;
     }
  	
}



}
