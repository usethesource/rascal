
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


public abstract class Kind extends AbstractAST {
  public Kind(ISourceLocation loc) {
    super(loc);
  }
  


static public class Ambiguity extends Kind {
  private final java.util.List<org.rascalmpl.ast.Kind> alternatives;

  public Ambiguity(ISourceLocation loc, java.util.List<org.rascalmpl.ast.Kind> alternatives) {
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
  
  public java.util.List<org.rascalmpl.ast.Kind> getAlternatives() {
   return alternatives;
  }

  public <T> T accept(IASTVisitor<T> v) {
	return v.visitKindAmbiguity(this);
  }
}





  public boolean isModule() {
    return false;
  }
  
static public class Module extends Kind {
  // Production: sig("Module",[])

  

  
public Module(ISourceLocation loc) {
  super(loc);
  
}


  @Override
  public boolean isModule() { 
    return true; 
  }

  @Override
  public <T> T accept(IASTVisitor<T> visitor) {
    return visitor.visitKindModule(this);
  }
  
  	
}


  public boolean isRule() {
    return false;
  }
  
static public class Rule extends Kind {
  // Production: sig("Rule",[])

  

  
public Rule(ISourceLocation loc) {
  super(loc);
  
}


  @Override
  public boolean isRule() { 
    return true; 
  }

  @Override
  public <T> T accept(IASTVisitor<T> visitor) {
    return visitor.visitKindRule(this);
  }
  
  	
}


  public boolean isVariable() {
    return false;
  }
  
static public class Variable extends Kind {
  // Production: sig("Variable",[])

  

  
public Variable(ISourceLocation loc) {
  super(loc);
  
}


  @Override
  public boolean isVariable() { 
    return true; 
  }

  @Override
  public <T> T accept(IASTVisitor<T> visitor) {
    return visitor.visitKindVariable(this);
  }
  
  	
}


  public boolean isAnno() {
    return false;
  }
  
static public class Anno extends Kind {
  // Production: sig("Anno",[])

  

  
public Anno(ISourceLocation loc) {
  super(loc);
  
}


  @Override
  public boolean isAnno() { 
    return true; 
  }

  @Override
  public <T> T accept(IASTVisitor<T> visitor) {
    return visitor.visitKindAnno(this);
  }
  
  	
}


  public boolean isFunction() {
    return false;
  }
  
static public class Function extends Kind {
  // Production: sig("Function",[])

  

  
public Function(ISourceLocation loc) {
  super(loc);
  
}


  @Override
  public boolean isFunction() { 
    return true; 
  }

  @Override
  public <T> T accept(IASTVisitor<T> visitor) {
    return visitor.visitKindFunction(this);
  }
  
  	
}


  public boolean isData() {
    return false;
  }
  
static public class Data extends Kind {
  // Production: sig("Data",[])

  

  
public Data(ISourceLocation loc) {
  super(loc);
  
}


  @Override
  public boolean isData() { 
    return true; 
  }

  @Override
  public <T> T accept(IASTVisitor<T> visitor) {
    return visitor.visitKindData(this);
  }
  
  	
}


  public boolean isTag() {
    return false;
  }
  
static public class Tag extends Kind {
  // Production: sig("Tag",[])

  

  
public Tag(ISourceLocation loc) {
  super(loc);
  
}


  @Override
  public boolean isTag() { 
    return true; 
  }

  @Override
  public <T> T accept(IASTVisitor<T> visitor) {
    return visitor.visitKindTag(this);
  }
  
  	
}


  public boolean isView() {
    return false;
  }
  
static public class View extends Kind {
  // Production: sig("View",[])

  

  
public View(ISourceLocation loc) {
  super(loc);
  
}


  @Override
  public boolean isView() { 
    return true; 
  }

  @Override
  public <T> T accept(IASTVisitor<T> visitor) {
    return visitor.visitKindView(this);
  }
  
  	
}


  public boolean isAlias() {
    return false;
  }
  
static public class Alias extends Kind {
  // Production: sig("Alias",[])

  

  
public Alias(ISourceLocation loc) {
  super(loc);
  
}


  @Override
  public boolean isAlias() { 
    return true; 
  }

  @Override
  public <T> T accept(IASTVisitor<T> visitor) {
    return visitor.visitKindAlias(this);
  }
  
  	
}


  public boolean isAll() {
    return false;
  }
  
static public class All extends Kind {
  // Production: sig("All",[])

  

  
public All(ISourceLocation loc) {
  super(loc);
  
}


  @Override
  public boolean isAll() { 
    return true; 
  }

  @Override
  public <T> T accept(IASTVisitor<T> visitor) {
    return visitor.visitKindAll(this);
  }
  
  	
}



}
