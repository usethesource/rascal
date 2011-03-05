
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


public abstract class Tag extends AbstractAST {
  public Tag(INode node) {
    super(node);
  }
  

  public boolean hasContents() {
    return false;
  }

  public org.rascalmpl.ast.TagString getContents() {
    throw new UnsupportedOperationException();
  }

  public boolean hasExpression() {
    return false;
  }

  public org.rascalmpl.ast.Expression getExpression() {
    throw new UnsupportedOperationException();
  }

  public boolean hasName() {
    return false;
  }

  public org.rascalmpl.ast.Name getName() {
    throw new UnsupportedOperationException();
  }


static public class Ambiguity extends Tag {
  private final java.util.List<org.rascalmpl.ast.Tag> alternatives;

  public Ambiguity(INode node, java.util.List<org.rascalmpl.ast.Tag> alternatives) {
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
  
  public java.util.List<org.rascalmpl.ast.Tag> getAlternatives() {
   return alternatives;
  }

  public <T> T accept(IASTVisitor<T> v) {
	return v.visitTagAmbiguity(this);
  }
}





  public boolean isDefault() {
    return false;
  }
  
static public class Default extends Tag {
  // Production: sig("Default",[arg("org.rascalmpl.ast.Name","name"),arg("org.rascalmpl.ast.TagString","contents")])

  
     private final org.rascalmpl.ast.Name name;
  
     private final org.rascalmpl.ast.TagString contents;
  

  
public Default(INode node , org.rascalmpl.ast.Name name,  org.rascalmpl.ast.TagString contents) {
  super(node);
  
    this.name = name;
  
    this.contents = contents;
  
}


  @Override
  public boolean isDefault() { 
    return true; 
  }

  @Override
  public <T> T accept(IASTVisitor<T> visitor) {
    return visitor.visitTagDefault(this);
  }
  
  
     @Override
     public org.rascalmpl.ast.Name getName() {
        return this.name;
     }
     
     @Override
     public boolean hasName() {
        return true;
     }
  
     @Override
     public org.rascalmpl.ast.TagString getContents() {
        return this.contents;
     }
     
     @Override
     public boolean hasContents() {
        return true;
     }
  	
}


  public boolean isExpression() {
    return false;
  }
  
static public class Expression extends Tag {
  // Production: sig("Expression",[arg("org.rascalmpl.ast.Name","name"),arg("org.rascalmpl.ast.Expression","expression")])

  
     private final org.rascalmpl.ast.Name name;
  
     private final org.rascalmpl.ast.Expression expression;
  

  
public Expression(INode node , org.rascalmpl.ast.Name name,  org.rascalmpl.ast.Expression expression) {
  super(node);
  
    this.name = name;
  
    this.expression = expression;
  
}


  @Override
  public boolean isExpression() { 
    return true; 
  }

  @Override
  public <T> T accept(IASTVisitor<T> visitor) {
    return visitor.visitTagExpression(this);
  }
  
  
     @Override
     public org.rascalmpl.ast.Name getName() {
        return this.name;
     }
     
     @Override
     public boolean hasName() {
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


  public boolean isEmpty() {
    return false;
  }
  
static public class Empty extends Tag {
  // Production: sig("Empty",[arg("org.rascalmpl.ast.Name","name")])

  
     private final org.rascalmpl.ast.Name name;
  

  
public Empty(INode node , org.rascalmpl.ast.Name name) {
  super(node);
  
    this.name = name;
  
}


  @Override
  public boolean isEmpty() { 
    return true; 
  }

  @Override
  public <T> T accept(IASTVisitor<T> visitor) {
    return visitor.visitTagEmpty(this);
  }
  
  
     @Override
     public org.rascalmpl.ast.Name getName() {
        return this.name;
     }
     
     @Override
     public boolean hasName() {
        return true;
     }
  	
}



}
