
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


public abstract class ProtocolTail extends AbstractAST {
  public ProtocolTail(IConstructor node) {
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

  public boolean hasPost() {
    return false;
  }

  public org.rascalmpl.ast.PostProtocolChars getPost() {
    throw new UnsupportedOperationException();
  }

  public boolean hasMid() {
    return false;
  }

  public org.rascalmpl.ast.MidProtocolChars getMid() {
    throw new UnsupportedOperationException();
  }


static public class Ambiguity extends ProtocolTail {
  private final java.util.List<org.rascalmpl.ast.ProtocolTail> alternatives;

  public Ambiguity(IConstructor node, java.util.List<org.rascalmpl.ast.ProtocolTail> alternatives) {
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
  
  public java.util.List<org.rascalmpl.ast.ProtocolTail> getAlternatives() {
   return alternatives;
  }

  public <T> T accept(IASTVisitor<T> v) {
	return v.visitProtocolTailAmbiguity(this);
  }
}





  public boolean isPost() {
    return false;
  }
  
static public class Post extends ProtocolTail {
  // Production: sig("Post",[arg("org.rascalmpl.ast.PostProtocolChars","post")])

  
     private final org.rascalmpl.ast.PostProtocolChars post;
  

  
public Post(IConstructor node , org.rascalmpl.ast.PostProtocolChars post) {
  super(node);
  
    this.post = post;
  
}


  @Override
  public boolean isPost() { 
    return true; 
  }

  @Override
  public <T> T accept(IASTVisitor<T> visitor) {
    return visitor.visitProtocolTailPost(this);
  }
  
  
     @Override
     public org.rascalmpl.ast.PostProtocolChars getPost() {
        return this.post;
     }
     
     @Override
     public boolean hasPost() {
        return true;
     }
  	
}


  public boolean isMid() {
    return false;
  }
  
static public class Mid extends ProtocolTail {
  // Production: sig("Mid",[arg("org.rascalmpl.ast.MidProtocolChars","mid"),arg("org.rascalmpl.ast.Expression","expression"),arg("org.rascalmpl.ast.ProtocolTail","tail")])

  
     private final org.rascalmpl.ast.MidProtocolChars mid;
  
     private final org.rascalmpl.ast.Expression expression;
  
     private final org.rascalmpl.ast.ProtocolTail tail;
  

  
public Mid(IConstructor node , org.rascalmpl.ast.MidProtocolChars mid,  org.rascalmpl.ast.Expression expression,  org.rascalmpl.ast.ProtocolTail tail) {
  super(node);
  
    this.mid = mid;
  
    this.expression = expression;
  
    this.tail = tail;
  
}


  @Override
  public boolean isMid() { 
    return true; 
  }

  @Override
  public <T> T accept(IASTVisitor<T> visitor) {
    return visitor.visitProtocolTailMid(this);
  }
  
  
     @Override
     public org.rascalmpl.ast.MidProtocolChars getMid() {
        return this.mid;
     }
     
     @Override
     public boolean hasMid() {
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
