
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


public abstract class DataTypeSelector extends AbstractAST {
  public DataTypeSelector(INode node) {
    super(node);
  }
  

  public boolean hasSort() {
    return false;
  }

  public org.rascalmpl.ast.QualifiedName getSort() {
    throw new UnsupportedOperationException();
  }

  public boolean hasProduction() {
    return false;
  }

  public org.rascalmpl.ast.Name getProduction() {
    throw new UnsupportedOperationException();
  }


static public class Ambiguity extends DataTypeSelector {
  private final java.util.List<org.rascalmpl.ast.DataTypeSelector> alternatives;

  public Ambiguity(INode node, java.util.List<org.rascalmpl.ast.DataTypeSelector> alternatives) {
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
  
  public java.util.List<org.rascalmpl.ast.DataTypeSelector> getAlternatives() {
   return alternatives;
  }

  public <T> T accept(IASTVisitor<T> v) {
	return v.visitDataTypeSelectorAmbiguity(this);
  }
}





  public boolean isSelector() {
    return false;
  }
  
static public class Selector extends DataTypeSelector {
  // Production: sig("Selector",[arg("org.rascalmpl.ast.QualifiedName","sort"),arg("org.rascalmpl.ast.Name","production")])

  
     private final org.rascalmpl.ast.QualifiedName sort;
  
     private final org.rascalmpl.ast.Name production;
  

  
public Selector(INode node , org.rascalmpl.ast.QualifiedName sort,  org.rascalmpl.ast.Name production) {
  super(node);
  
    this.sort = sort;
  
    this.production = production;
  
}


  @Override
  public boolean isSelector() { 
    return true; 
  }

  @Override
  public <T> T accept(IASTVisitor<T> visitor) {
    return visitor.visitDataTypeSelectorSelector(this);
  }
  
  
     @Override
     public org.rascalmpl.ast.QualifiedName getSort() {
        return this.sort;
     }
     
     @Override
     public boolean hasSort() {
        return true;
     }
  
     @Override
     public org.rascalmpl.ast.Name getProduction() {
        return this.production;
     }
     
     @Override
     public boolean hasProduction() {
        return true;
     }
  	
}



}
