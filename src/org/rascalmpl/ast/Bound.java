
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


public abstract class Bound extends AbstractAST {
  public Bound(ISourceLocation loc) {
    super(loc);
  }
  

  public boolean hasExpression() {
    return false;
  }

  public org.rascalmpl.ast.Expression getExpression() {
    throw new UnsupportedOperationException();
  }


static public class Ambiguity extends Bound {
  private final java.util.List<org.rascalmpl.ast.Bound> alternatives;

  public Ambiguity(ISourceLocation loc, java.util.List<org.rascalmpl.ast.Bound> alternatives) {
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
  
  public java.util.List<org.rascalmpl.ast.Bound> getAlternatives() {
   return alternatives;
  }

  public <T> T accept(IASTVisitor<T> v) {
	return v.visitBoundAmbiguity(this);
  }
}





  public boolean isEmpty() {
    return false;
  }
  
static public class Empty extends Bound {
  // Production: sig("Empty",[])

  

  
public Empty(ISourceLocation loc) {
  super(loc);
  
}


  @Override
  public boolean isEmpty() { 
    return true; 
  }

  @Override
  public <T> T accept(IASTVisitor<T> visitor) {
    return visitor.visitBoundEmpty(this);
  }
  
  	
}


  public boolean isDefault() {
    return false;
  }
  
static public class Default extends Bound {
  // Production: sig("Default",[arg("org.rascalmpl.ast.Expression","expression")])

  
     private final org.rascalmpl.ast.Expression expression;
  

  
public Default(ISourceLocation loc, org.rascalmpl.ast.Expression expression) {
  super(loc);
  
    this.expression = expression;
  
}


  @Override
  public boolean isDefault() { 
    return true; 
  }

  @Override
  public <T> T accept(IASTVisitor<T> visitor) {
    return visitor.visitBoundDefault(this);
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



}
