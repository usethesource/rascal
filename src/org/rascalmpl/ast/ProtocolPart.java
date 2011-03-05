
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


public abstract class ProtocolPart extends AbstractAST {
  public ProtocolPart(INode node) {
    super(node);
  }
  

  public boolean hasTail() {
    return false;
  }

  public org.rascalmpl.ast.ProtocolTail getTail() {
    throw new UnsupportedOperationException();
  }

  public boolean hasExpression() {
    return false;
  }

  public org.rascalmpl.ast.Expression getExpression() {
    throw new UnsupportedOperationException();
  }

  public boolean hasProtocolChars() {
    return false;
  }

  public org.rascalmpl.ast.ProtocolChars getProtocolChars() {
    throw new UnsupportedOperationException();
  }

  public boolean hasPre() {
    return false;
  }

  public org.rascalmpl.ast.PreProtocolChars getPre() {
    throw new UnsupportedOperationException();
  }


static public class Ambiguity extends ProtocolPart {
  private final java.util.List<org.rascalmpl.ast.ProtocolPart> alternatives;

  public Ambiguity(INode node, java.util.List<org.rascalmpl.ast.ProtocolPart> alternatives) {
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
  
  public java.util.List<org.rascalmpl.ast.ProtocolPart> getAlternatives() {
   return alternatives;
  }

  public <T> T accept(IASTVisitor<T> v) {
	return v.visitProtocolPartAmbiguity(this);
  }
}





  public boolean isNonInterpolated() {
    return false;
  }
  
static public class NonInterpolated extends ProtocolPart {
  // Production: sig("NonInterpolated",[arg("org.rascalmpl.ast.ProtocolChars","protocolChars")])

  
     private final org.rascalmpl.ast.ProtocolChars protocolChars;
  

  
public NonInterpolated(INode node , org.rascalmpl.ast.ProtocolChars protocolChars) {
  super(node);
  
    this.protocolChars = protocolChars;
  
}


  @Override
  public boolean isNonInterpolated() { 
    return true; 
  }

  @Override
  public <T> T accept(IASTVisitor<T> visitor) {
    return visitor.visitProtocolPartNonInterpolated(this);
  }
  
  
     @Override
     public org.rascalmpl.ast.ProtocolChars getProtocolChars() {
        return this.protocolChars;
     }
     
     @Override
     public boolean hasProtocolChars() {
        return true;
     }
  	
}


  public boolean isInterpolated() {
    return false;
  }
  
static public class Interpolated extends ProtocolPart {
  // Production: sig("Interpolated",[arg("org.rascalmpl.ast.PreProtocolChars","pre"),arg("org.rascalmpl.ast.Expression","expression"),arg("org.rascalmpl.ast.ProtocolTail","tail")])

  
     private final org.rascalmpl.ast.PreProtocolChars pre;
  
     private final org.rascalmpl.ast.Expression expression;
  
     private final org.rascalmpl.ast.ProtocolTail tail;
  

  
public Interpolated(INode node , org.rascalmpl.ast.PreProtocolChars pre,  org.rascalmpl.ast.Expression expression,  org.rascalmpl.ast.ProtocolTail tail) {
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
    return visitor.visitProtocolPartInterpolated(this);
  }
  
  
     @Override
     public org.rascalmpl.ast.PreProtocolChars getPre() {
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
     public org.rascalmpl.ast.ProtocolTail getTail() {
        return this.tail;
     }
     
     @Override
     public boolean hasTail() {
        return true;
     }
  	
}



}
