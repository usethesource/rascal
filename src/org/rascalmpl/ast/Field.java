
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


public abstract class Field extends AbstractAST {
  public Field(IConstructor node) {
    super(node);
  }
  

  public boolean hasFieldIndex() {
    return false;
  }

  public org.rascalmpl.ast.IntegerLiteral getFieldIndex() {
    throw new UnsupportedOperationException();
  }

  public boolean hasFieldName() {
    return false;
  }

  public org.rascalmpl.ast.Name getFieldName() {
    throw new UnsupportedOperationException();
  }


static public class Ambiguity extends Field {
  private final java.util.List<org.rascalmpl.ast.Field> alternatives;

  public Ambiguity(IConstructor node, java.util.List<org.rascalmpl.ast.Field> alternatives) {
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
  
  public java.util.List<org.rascalmpl.ast.Field> getAlternatives() {
   return alternatives;
  }

  public <T> T accept(IASTVisitor<T> v) {
	return v.visitFieldAmbiguity(this);
  }
}





  public boolean isName() {
    return false;
  }
  
static public class Name extends Field {
  // Production: sig("Name",[arg("org.rascalmpl.ast.Name","fieldName")])

  
     private final org.rascalmpl.ast.Name fieldName;
  

  
public Name(IConstructor node , org.rascalmpl.ast.Name fieldName) {
  super(node);
  
    this.fieldName = fieldName;
  
}


  @Override
  public boolean isName() { 
    return true; 
  }

  @Override
  public <T> T accept(IASTVisitor<T> visitor) {
    return visitor.visitFieldName(this);
  }
  
  
     @Override
     public org.rascalmpl.ast.Name getFieldName() {
        return this.fieldName;
     }
     
     @Override
     public boolean hasFieldName() {
        return true;
     }
  	
}


  public boolean isIndex() {
    return false;
  }
  
static public class Index extends Field {
  // Production: sig("Index",[arg("org.rascalmpl.ast.IntegerLiteral","fieldIndex")])

  
     private final org.rascalmpl.ast.IntegerLiteral fieldIndex;
  

  
public Index(IConstructor node , org.rascalmpl.ast.IntegerLiteral fieldIndex) {
  super(node);
  
    this.fieldIndex = fieldIndex;
  
}


  @Override
  public boolean isIndex() { 
    return true; 
  }

  @Override
  public <T> T accept(IASTVisitor<T> visitor) {
    return visitor.visitFieldIndex(this);
  }
  
  
     @Override
     public org.rascalmpl.ast.IntegerLiteral getFieldIndex() {
        return this.fieldIndex;
     }
     
     @Override
     public boolean hasFieldIndex() {
        return true;
     }
  	
}



}
