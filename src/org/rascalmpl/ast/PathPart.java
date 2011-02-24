
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


public abstract class PathPart extends AbstractAST {
  public PathPart(INode node) {
    super(node);
  }
  

  public boolean hasPre() {
    return false;
  }

  public org.rascalmpl.ast.PrePathChars getPre() {
    throw new UnsupportedOperationException();
  }

  public boolean hasExpression() {
    return false;
  }

  public org.rascalmpl.ast.Expression getExpression() {
    throw new UnsupportedOperationException();
  }

  public boolean hasTail() {
    return false;
  }

  public org.rascalmpl.ast.PathTail getTail() {
    throw new UnsupportedOperationException();
  }

  public boolean hasPathChars() {
    return false;
  }

  public org.rascalmpl.ast.PathChars getPathChars() {
    throw new UnsupportedOperationException();
  }


static public class Ambiguity extends PathPart {
  private final java.util.List<org.rascalmpl.ast.PathPart> alternatives;

  public Ambiguity(INode node, java.util.List<org.rascalmpl.ast.PathPart> alternatives) {
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
  
  public java.util.List<org.rascalmpl.ast.PathPart> getAlternatives() {
   return alternatives;
  }

  public <T> T accept(IASTVisitor<T> v) {
	return v.visitPathPartAmbiguity(this);
  }
}





  public boolean isNonInterpolated() {
    return false;
  }
  
static public class NonInterpolated extends PathPart {
  // Production: sig("NonInterpolated",[arg("org.rascalmpl.ast.PathChars","pathChars")])

  
     private final org.rascalmpl.ast.PathChars pathChars;
  

  
public NonInterpolated(INode node , org.rascalmpl.ast.PathChars pathChars) {
  super(node);
  
    this.pathChars = pathChars;
  
}


  @Override
  public boolean isNonInterpolated() { 
    return true; 
  }

  @Override
  public <T> T accept(IASTVisitor<T> visitor) {
    return visitor.visitPathPartNonInterpolated(this);
  }
  
  
     @Override
     public org.rascalmpl.ast.PathChars getPathChars() {
        return this.pathChars;
     }
     
     @Override
     public boolean hasPathChars() {
        return true;
     }
  	
}


  public boolean isInterpolated() {
    return false;
  }
  
static public class Interpolated extends PathPart {
  // Production: sig("Interpolated",[arg("org.rascalmpl.ast.PrePathChars","pre"),arg("org.rascalmpl.ast.Expression","expression"),arg("org.rascalmpl.ast.PathTail","tail")])

  
     private final org.rascalmpl.ast.PrePathChars pre;
  
     private final org.rascalmpl.ast.Expression expression;
  
     private final org.rascalmpl.ast.PathTail tail;
  

  
public Interpolated(INode node , org.rascalmpl.ast.PrePathChars pre,  org.rascalmpl.ast.Expression expression,  org.rascalmpl.ast.PathTail tail) {
  super(node);
  
    this.pre = pre;
  
    this.expression = expression;
  
    this.tail = tail;
  
}


  @Override
  public boolean isInterpolated() { 
    return true; 
  }

  @Override
  public <T> T accept(IASTVisitor<T> visitor) {
    return visitor.visitPathPartInterpolated(this);
  }
  
  
     @Override
     public org.rascalmpl.ast.PrePathChars getPre() {
        return this.pre;
     }
     
     @Override
     public boolean hasPre() {
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
     public org.rascalmpl.ast.PathTail getTail() {
        return this.tail;
     }
     
     @Override
     public boolean hasTail() {
        return true;
     }
  	
}



}
