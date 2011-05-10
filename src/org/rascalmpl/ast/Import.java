
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


public abstract class Import extends AbstractAST {
  public Import(IConstructor node) {
    super(node);
  }
  

  public boolean hasSyntax() {
    return false;
  }

  public org.rascalmpl.ast.SyntaxDefinition getSyntax() {
    throw new UnsupportedOperationException();
  }

  public boolean hasModule() {
    return false;
  }

  public org.rascalmpl.ast.ImportedModule getModule() {
    throw new UnsupportedOperationException();
  }


static public class Ambiguity extends Import {
  private final java.util.List<org.rascalmpl.ast.Import> alternatives;

  public Ambiguity(IConstructor node, java.util.List<org.rascalmpl.ast.Import> alternatives) {
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
  
  public java.util.List<org.rascalmpl.ast.Import> getAlternatives() {
   return alternatives;
  }

  public <T> T accept(IASTVisitor<T> v) {
	return v.visitImportAmbiguity(this);
  }
}





  public boolean isExtend() {
    return false;
  }
  
static public class Extend extends Import {
  // Production: sig("Extend",[arg("org.rascalmpl.ast.ImportedModule","module")])

  
     private final org.rascalmpl.ast.ImportedModule module;
  

  
public Extend(IConstructor node , org.rascalmpl.ast.ImportedModule module) {
  super(node);
  
    this.module = module;
  
}


  @Override
  public boolean isExtend() { 
    return true; 
  }

  @Override
  public <T> T accept(IASTVisitor<T> visitor) {
    return visitor.visitImportExtend(this);
  }
  
  
     @Override
     public org.rascalmpl.ast.ImportedModule getModule() {
        return this.module;
     }
     
     @Override
     public boolean hasModule() {
        return true;
     }
  	
}


  public boolean isDefault() {
    return false;
  }
  
static public class Default extends Import {
  // Production: sig("Default",[arg("org.rascalmpl.ast.ImportedModule","module")])

  
     private final org.rascalmpl.ast.ImportedModule module;
  

  
public Default(IConstructor node , org.rascalmpl.ast.ImportedModule module) {
  super(node);
  
    this.module = module;
  
}


  @Override
  public boolean isDefault() { 
    return true; 
  }

  @Override
  public <T> T accept(IASTVisitor<T> visitor) {
    return visitor.visitImportDefault(this);
  }
  
  
     @Override
     public org.rascalmpl.ast.ImportedModule getModule() {
        return this.module;
     }
     
     @Override
     public boolean hasModule() {
        return true;
     }
  	
}


  public boolean isSyntax() {
    return false;
  }
  
static public class Syntax extends Import {
  // Production: sig("Syntax",[arg("org.rascalmpl.ast.SyntaxDefinition","syntax")])

  
     private final org.rascalmpl.ast.SyntaxDefinition syntax;
  

  
public Syntax(IConstructor node , org.rascalmpl.ast.SyntaxDefinition syntax) {
  super(node);
  
    this.syntax = syntax;
  
}


  @Override
  public boolean isSyntax() { 
    return true; 
  }

  @Override
  public <T> T accept(IASTVisitor<T> visitor) {
    return visitor.visitImportSyntax(this);
  }
  
  
     @Override
     public org.rascalmpl.ast.SyntaxDefinition getSyntax() {
        return this.syntax;
     }
     
     @Override
     public boolean hasSyntax() {
        return true;
     }
  	
}



}
