
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


public abstract class Visibility extends AbstractAST {
  public Visibility(INode node) {
    super(node);
  }
  


static public class Ambiguity extends Visibility {
  private final java.util.List<org.rascalmpl.ast.Visibility> alternatives;

  public Ambiguity(INode node, java.util.List<org.rascalmpl.ast.Visibility> alternatives) {
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
  
  public java.util.List<org.rascalmpl.ast.Visibility> getAlternatives() {
   return alternatives;
  }

  public <T> T accept(IASTVisitor<T> v) {
	return v.visitVisibilityAmbiguity(this);
  }
}





  public boolean isPublic() {
    return false;
  }
  
static public class Public extends Visibility {
  // Production: sig("Public",[])

  

  
public Public(INode node ) {
  super(node);
  
}


  @Override
  public boolean isPublic() { 
    return true; 
  }

  @Override
  public <T> T accept(IASTVisitor<T> visitor) {
    return visitor.visitVisibilityPublic(this);
  }
  
  	
}


  public boolean isDefault() {
    return false;
  }
  
static public class Default extends Visibility {
  // Production: sig("Default",[])

  

  
public Default(INode node ) {
  super(node);
  
}


  @Override
  public boolean isDefault() { 
    return true; 
  }

  @Override
  public <T> T accept(IASTVisitor<T> visitor) {
    return visitor.visitVisibilityDefault(this);
  }
  
  	
}


  public boolean isPrivate() {
    return false;
  }
  
static public class Private extends Visibility {
  // Production: sig("Private",[])

  

  
public Private(INode node ) {
  super(node);
  
}


  @Override
  public boolean isPrivate() { 
    return true; 
  }

  @Override
  public <T> T accept(IASTVisitor<T> visitor) {
    return visitor.visitVisibilityPrivate(this);
  }
  
  	
}



}
