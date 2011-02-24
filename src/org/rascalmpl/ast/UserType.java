
package org.rascalmpl.ast;


import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.INode;
import org.eclipse.imp.pdb.facts.ISourceLocation;
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


public abstract class UserType extends AbstractAST {
  public UserType(ISourceLocation loc) {
    super(loc);
  }
  

  public boolean hasParameters() {
    return false;
  }

  public java.util.List<org.rascalmpl.ast.Type> getParameters() {
    throw new UnsupportedOperationException();
  }

  public boolean hasName() {
    return false;
  }

  public org.rascalmpl.ast.QualifiedName getName() {
    throw new UnsupportedOperationException();
  }


static public class Ambiguity extends UserType {
  private final java.util.List<org.rascalmpl.ast.UserType> alternatives;

  public Ambiguity(ISourceLocation loc, java.util.List<org.rascalmpl.ast.UserType> alternatives) {
    super(loc);
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
  
  public java.util.List<org.rascalmpl.ast.UserType> getAlternatives() {
   return alternatives;
  }

  public <T> T accept(IASTVisitor<T> v) {
	return v.visitUserTypeAmbiguity(this);
  }
}





  public boolean isParametric() {
    return false;
  }
  
static public class Parametric extends UserType {
  // Production: sig("Parametric",[arg("org.rascalmpl.ast.QualifiedName","name"),arg("java.util.List\<org.rascalmpl.ast.Type\>","parameters")])

  
     private final org.rascalmpl.ast.QualifiedName name;
  
     private final java.util.List<org.rascalmpl.ast.Type> parameters;
  

  
public Parametric(ISourceLocation loc, org.rascalmpl.ast.QualifiedName name,  java.util.List<org.rascalmpl.ast.Type> parameters) {
  super(loc);
  
    this.name = name;
  
    this.parameters = parameters;
  
}


  @Override
  public boolean isParametric() { 
    return true; 
  }

  @Override
  public <T> T accept(IASTVisitor<T> visitor) {
    return visitor.visitUserTypeParametric(this);
  }
  
  
     @Override
     public org.rascalmpl.ast.QualifiedName getName() {
        return this.name;
     }
     
     @Override
     public boolean hasName() {
        return true;
     }
  
     @Override
     public java.util.List<org.rascalmpl.ast.Type> getParameters() {
        return this.parameters;
     }
     
     @Override
     public boolean hasParameters() {
        return true;
     }
  	
}


  public boolean isName() {
    return false;
  }
  
static public class Name extends UserType {
  // Production: sig("Name",[arg("org.rascalmpl.ast.QualifiedName","name")])

  
     private final org.rascalmpl.ast.QualifiedName name;
  

  
public Name(ISourceLocation loc, org.rascalmpl.ast.QualifiedName name) {
  super(loc);
  
    this.name = name;
  
}


  @Override
  public boolean isName() { 
    return true; 
  }

  @Override
  public <T> T accept(IASTVisitor<T> visitor) {
    return visitor.visitUserTypeName(this);
  }
  
  
     @Override
     public org.rascalmpl.ast.QualifiedName getName() {
        return this.name;
     }
     
     @Override
     public boolean hasName() {
        return true;
     }
  	
}



}
