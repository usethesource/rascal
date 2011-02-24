
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


public abstract class SyntaxDefinition extends AbstractAST {
  public SyntaxDefinition(INode node) {
    super(node);
  }
  

  public boolean hasStart() {
    return false;
  }

  public org.rascalmpl.ast.Start getStart() {
    throw new UnsupportedOperationException();
  }

  public boolean hasProduction() {
    return false;
  }

  public org.rascalmpl.ast.Prod getProduction() {
    throw new UnsupportedOperationException();
  }

  public boolean hasDefined() {
    return false;
  }

  public org.rascalmpl.ast.Sym getDefined() {
    throw new UnsupportedOperationException();
  }


static public class Ambiguity extends SyntaxDefinition {
  private final java.util.List<org.rascalmpl.ast.SyntaxDefinition> alternatives;

  public Ambiguity(INode node, java.util.List<org.rascalmpl.ast.SyntaxDefinition> alternatives) {
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
  
  public java.util.List<org.rascalmpl.ast.SyntaxDefinition> getAlternatives() {
   return alternatives;
  }

  public <T> T accept(IASTVisitor<T> v) {
	return v.visitSyntaxDefinitionAmbiguity(this);
  }
}





  public boolean isLanguage() {
    return false;
  }
  
static public class Language extends SyntaxDefinition {
  // Production: sig("Language",[arg("org.rascalmpl.ast.Start","start"),arg("org.rascalmpl.ast.Sym","defined"),arg("org.rascalmpl.ast.Prod","production")])

  
     private final org.rascalmpl.ast.Start start;
  
     private final org.rascalmpl.ast.Sym defined;
  
     private final org.rascalmpl.ast.Prod production;
  

  
public Language(INode node , org.rascalmpl.ast.Start start,  org.rascalmpl.ast.Sym defined,  org.rascalmpl.ast.Prod production) {
  super(node);
  
    this.start = start;
  
    this.defined = defined;
  
    this.production = production;
  
}


  @Override
  public boolean isLanguage() { 
    return true; 
  }

  @Override
  public <T> T accept(IASTVisitor<T> visitor) {
    return visitor.visitSyntaxDefinitionLanguage(this);
  }
  
  
     @Override
     public org.rascalmpl.ast.Start getStart() {
        return this.start;
     }
     
     @Override
     public boolean hasStart() {
        return true;
     }
  
     @Override
     public org.rascalmpl.ast.Sym getDefined() {
        return this.defined;
     }
     
     @Override
     public boolean hasDefined() {
        return true;
     }
  
     @Override
     public org.rascalmpl.ast.Prod getProduction() {
        return this.production;
     }
     
     @Override
     public boolean hasProduction() {
        return true;
     }
  	
}


  public boolean isLayout() {
    return false;
  }
  
static public class Layout extends SyntaxDefinition {
  // Production: sig("Layout",[arg("org.rascalmpl.ast.Sym","defined"),arg("org.rascalmpl.ast.Prod","production")])

  
     private final org.rascalmpl.ast.Sym defined;
  
     private final org.rascalmpl.ast.Prod production;
  

  
public Layout(INode node , org.rascalmpl.ast.Sym defined,  org.rascalmpl.ast.Prod production) {
  super(node);
  
    this.defined = defined;
  
    this.production = production;
  
}


  @Override
  public boolean isLayout() { 
    return true; 
  }

  @Override
  public <T> T accept(IASTVisitor<T> visitor) {
    return visitor.visitSyntaxDefinitionLayout(this);
  }
  
  
     @Override
     public org.rascalmpl.ast.Sym getDefined() {
        return this.defined;
     }
     
     @Override
     public boolean hasDefined() {
        return true;
     }
  
     @Override
     public org.rascalmpl.ast.Prod getProduction() {
        return this.production;
     }
     
     @Override
     public boolean hasProduction() {
        return true;
     }
  	
}



}
