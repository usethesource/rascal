
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


public abstract class TypeArg extends AbstractAST {
  public TypeArg(IConstructor node) {
    super(node);
  }
  

  public boolean hasName() {
    return false;
  }

  public org.rascalmpl.ast.Name getName() {
    throw new UnsupportedOperationException();
  }

  public boolean hasType() {
    return false;
  }

  public org.rascalmpl.ast.Type getType() {
    throw new UnsupportedOperationException();
  }


static public class Ambiguity extends TypeArg {
  private final java.util.List<org.rascalmpl.ast.TypeArg> alternatives;

  public Ambiguity(IConstructor node, java.util.List<org.rascalmpl.ast.TypeArg> alternatives) {
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
  
  public java.util.List<org.rascalmpl.ast.TypeArg> getAlternatives() {
   return alternatives;
  }

  public <T> T accept(IASTVisitor<T> v) {
	return v.visitTypeArgAmbiguity(this);
  }
}





  public boolean isNamed() {
    return false;
  }
  
static public class Named extends TypeArg {
  // Production: sig("Named",[arg("org.rascalmpl.ast.Type","type"),arg("org.rascalmpl.ast.Name","name")])

  
     private final org.rascalmpl.ast.Type type;
  
     private final org.rascalmpl.ast.Name name;
  

  
public Named(IConstructor node , org.rascalmpl.ast.Type type,  org.rascalmpl.ast.Name name) {
  super(node);
  
    this.type = type;
  
    this.name = name;
  
}


  @Override
  public boolean isNamed() { 
    return true; 
  }

  @Override
  public <T> T accept(IASTVisitor<T> visitor) {
    return visitor.visitTypeArgNamed(this);
  }
  
  
     @Override
     public org.rascalmpl.ast.Type getType() {
        return this.type;
     }
     
     @Override
     public boolean hasType() {
        return true;
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


  public boolean isDefault() {
    return false;
  }
  
static public class Default extends TypeArg {
  // Production: sig("Default",[arg("org.rascalmpl.ast.Type","type")])

  
     private final org.rascalmpl.ast.Type type;
  

  
public Default(IConstructor node , org.rascalmpl.ast.Type type) {
  super(node);
  
    this.type = type;
  
}


  @Override
  public boolean isDefault() { 
    return true; 
  }

  @Override
  public <T> T accept(IASTVisitor<T> visitor) {
    return visitor.visitTypeArgDefault(this);
  }
  
  
     @Override
     public org.rascalmpl.ast.Type getType() {
        return this.type;
     }
     
     @Override
     public boolean hasType() {
        return true;
     }
  	
}



}
