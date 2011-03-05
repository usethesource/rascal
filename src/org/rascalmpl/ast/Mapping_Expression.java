
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


public abstract class Mapping_Expression extends AbstractAST {
  public Mapping_Expression(INode node) {
    super(node);
  }
  

  public boolean hasTo() {
    return false;
  }

  public org.rascalmpl.ast.Expression getTo() {
    throw new UnsupportedOperationException();
  }

  public boolean hasFrom() {
    return false;
  }

  public org.rascalmpl.ast.Expression getFrom() {
    throw new UnsupportedOperationException();
  }


static public class Ambiguity extends Mapping_Expression {
  private final java.util.List<org.rascalmpl.ast.Mapping_Expression> alternatives;

  public Ambiguity(INode node, java.util.List<org.rascalmpl.ast.Mapping_Expression> alternatives) {
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
  
  public java.util.List<org.rascalmpl.ast.Mapping_Expression> getAlternatives() {
   return alternatives;
  }

  public <T> T accept(IASTVisitor<T> v) {
	return v.visitMapping_ExpressionAmbiguity(this);
  }
}





  public boolean isDefault() {
    return false;
  }
  
static public class Default extends Mapping_Expression {
  // Production: sig("Default",[arg("org.rascalmpl.ast.Expression","from"),arg("org.rascalmpl.ast.Expression","to")])

  
     private final org.rascalmpl.ast.Expression from;
  
     private final org.rascalmpl.ast.Expression to;
  

  
public Default(INode node , org.rascalmpl.ast.Expression from,  org.rascalmpl.ast.Expression to) {
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
    return visitor.visitMapping_ExpressionDefault(this);
  }
  
  
     @Override
     public org.rascalmpl.ast.Expression getFrom() {
        return this.from;
     }
     
     @Override
     public boolean hasFrom() {
        return true;
     }
  
     @Override
     public org.rascalmpl.ast.Expression getTo() {
        return this.to;
     }
     
     @Override
     public boolean hasTo() {
        return true;
     }
  	
}



}
