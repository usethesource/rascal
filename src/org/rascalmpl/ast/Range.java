
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


public abstract class Range extends AbstractAST {
  public Range(ISourceLocation loc) {
    super(loc);
  }
  

  public boolean hasEnd() {
    return false;
  }

  public org.rascalmpl.ast.Char getEnd() {
    throw new UnsupportedOperationException();
  }

  public boolean hasStart() {
    return false;
  }

  public org.rascalmpl.ast.Char getStart() {
    throw new UnsupportedOperationException();
  }

  public boolean hasCharacter() {
    return false;
  }

  public org.rascalmpl.ast.Char getCharacter() {
    throw new UnsupportedOperationException();
  }


static public class Ambiguity extends Range {
  private final java.util.List<org.rascalmpl.ast.Range> alternatives;

  public Ambiguity(ISourceLocation loc, java.util.List<org.rascalmpl.ast.Range> alternatives) {
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
  
  public java.util.List<org.rascalmpl.ast.Range> getAlternatives() {
   return alternatives;
  }

  public <T> T accept(IASTVisitor<T> v) {
	return v.visitRangeAmbiguity(this);
  }
}





  public boolean isFromTo() {
    return false;
  }
  
static public class FromTo extends Range {
  // Production: sig("FromTo",[arg("org.rascalmpl.ast.Char","start"),arg("org.rascalmpl.ast.Char","end")])

  
     private final org.rascalmpl.ast.Char start;
  
     private final org.rascalmpl.ast.Char end;
  

  
public FromTo(ISourceLocation loc, org.rascalmpl.ast.Char start,  org.rascalmpl.ast.Char end) {
  super(loc);
  
    this.start = start;
  
    this.end = end;
  
}


  @Override
  public boolean isFromTo() { 
    return true; 
  }

  @Override
  public <T> T accept(IASTVisitor<T> visitor) {
    return visitor.visitRangeFromTo(this);
  }
  
  
     @Override
     public org.rascalmpl.ast.Char getStart() {
        return this.start;
     }
     
     @Override
     public boolean hasStart() {
        return true;
     }
  
     @Override
     public org.rascalmpl.ast.Char getEnd() {
        return this.end;
     }
     
     @Override
     public boolean hasEnd() {
        return true;
     }
  	
}


  public boolean isCharacter() {
    return false;
  }
  
static public class Character extends Range {
  // Production: sig("Character",[arg("org.rascalmpl.ast.Char","character")])

  
     private final org.rascalmpl.ast.Char character;
  

  
public Character(ISourceLocation loc, org.rascalmpl.ast.Char character) {
  super(loc);
  
    this.character = character;
  
}


  @Override
  public boolean isCharacter() { 
    return true; 
  }

  @Override
  public <T> T accept(IASTVisitor<T> visitor) {
    return visitor.visitRangeCharacter(this);
  }
  
  
     @Override
     public org.rascalmpl.ast.Char getCharacter() {
        return this.character;
     }
     
     @Override
     public boolean hasCharacter() {
        return true;
     }
  	
}



}
