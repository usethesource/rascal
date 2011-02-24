
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


public abstract class Test extends AbstractAST {
  public Test(INode node) {
    super(node);
  }
  

  public boolean hasLabeled() {
    return false;
  }

  public org.rascalmpl.ast.StringLiteral getLabeled() {
    throw new UnsupportedOperationException();
  }

  public boolean hasTags() {
    return false;
  }

  public org.rascalmpl.ast.Tags getTags() {
    throw new UnsupportedOperationException();
  }

  public boolean hasExpression() {
    return false;
  }

  public org.rascalmpl.ast.Expression getExpression() {
    throw new UnsupportedOperationException();
  }


static public class Ambiguity extends Test {
  private final java.util.List<org.rascalmpl.ast.Test> alternatives;

  public Ambiguity(INode node, java.util.List<org.rascalmpl.ast.Test> alternatives) {
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
  
  public java.util.List<org.rascalmpl.ast.Test> getAlternatives() {
   return alternatives;
  }

  public <T> T accept(IASTVisitor<T> v) {
	return v.visitTestAmbiguity(this);
  }
}





  public boolean isUnlabeled() {
    return false;
  }
  
static public class Unlabeled extends Test {
  // Production: sig("Unlabeled",[arg("org.rascalmpl.ast.Tags","tags"),arg("org.rascalmpl.ast.Expression","expression")])

  
     private final org.rascalmpl.ast.Tags tags;
  
     private final org.rascalmpl.ast.Expression expression;
  

  
public Unlabeled(INode node , org.rascalmpl.ast.Tags tags,  org.rascalmpl.ast.Expression expression) {
  super(node);
  
    this.tags = tags;
  
    this.expression = expression;
  
}


  @Override
  public boolean isUnlabeled() { 
    return true; 
  }

  @Override
  public <T> T accept(IASTVisitor<T> visitor) {
    return visitor.visitTestUnlabeled(this);
  }
  
  
     @Override
     public org.rascalmpl.ast.Tags getTags() {
        return this.tags;
     }
     
     @Override
     public boolean hasTags() {
        return true;
     }
  
     @Override
     public org.rascalmpl.ast.Expression getExpression() {
        return this.expression;
     }
     
     @Override
     public boolean hasExpression() {
        return true;
     }
  	
}


  public boolean isLabeled() {
    return false;
  }
  
static public class Labeled extends Test {
  // Production: sig("Labeled",[arg("org.rascalmpl.ast.Tags","tags"),arg("org.rascalmpl.ast.Expression","expression"),arg("org.rascalmpl.ast.StringLiteral","labeled")])

  
     private final org.rascalmpl.ast.Tags tags;
  
     private final org.rascalmpl.ast.Expression expression;
  
     private final org.rascalmpl.ast.StringLiteral labeled;
  

  
public Labeled(INode node , org.rascalmpl.ast.Tags tags,  org.rascalmpl.ast.Expression expression,  org.rascalmpl.ast.StringLiteral labeled) {
  super(node);
  
    this.tags = tags;
  
    this.expression = expression;
  
    this.labeled = labeled;
  
}


  @Override
  public boolean isLabeled() { 
    return true; 
  }

  @Override
  public <T> T accept(IASTVisitor<T> visitor) {
    return visitor.visitTestLabeled(this);
  }
  
  
     @Override
     public org.rascalmpl.ast.Tags getTags() {
        return this.tags;
     }
     
     @Override
     public boolean hasTags() {
        return true;
     }
  
     @Override
     public org.rascalmpl.ast.Expression getExpression() {
        return this.expression;
     }
     
     @Override
     public boolean hasExpression() {
        return true;
     }
  
     @Override
     public org.rascalmpl.ast.StringLiteral getLabeled() {
        return this.labeled;
     }
     
     @Override
     public boolean hasLabeled() {
        return true;
     }
  	
}



}
