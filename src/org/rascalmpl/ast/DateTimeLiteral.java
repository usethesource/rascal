
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


public abstract class DateTimeLiteral extends AbstractAST {
  public DateTimeLiteral(ISourceLocation loc) {
    super(loc);
  }
  

  public boolean hasDateAndTime() {
    return false;
  }

  public org.rascalmpl.ast.DateAndTime getDateAndTime() {
    throw new UnsupportedOperationException();
  }

  public boolean hasTime() {
    return false;
  }

  public org.rascalmpl.ast.JustTime getTime() {
    throw new UnsupportedOperationException();
  }

  public boolean hasDate() {
    return false;
  }

  public org.rascalmpl.ast.JustDate getDate() {
    throw new UnsupportedOperationException();
  }


static public class Ambiguity extends DateTimeLiteral {
  private final java.util.List<org.rascalmpl.ast.DateTimeLiteral> alternatives;

  public Ambiguity(ISourceLocation loc, java.util.List<org.rascalmpl.ast.DateTimeLiteral> alternatives) {
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
  
  public java.util.List<org.rascalmpl.ast.DateTimeLiteral> getAlternatives() {
   return alternatives;
  }

  public <T> T accept(IASTVisitor<T> v) {
	return v.visitDateTimeLiteralAmbiguity(this);
  }
}





  public boolean isDateAndTimeLiteral() {
    return false;
  }
  
static public class DateAndTimeLiteral extends DateTimeLiteral {
  // Production: sig("DateAndTimeLiteral",[arg("org.rascalmpl.ast.DateAndTime","dateAndTime")])

  
     private final org.rascalmpl.ast.DateAndTime dateAndTime;
  

  
public DateAndTimeLiteral(ISourceLocation loc, org.rascalmpl.ast.DateAndTime dateAndTime) {
  super(loc);
  
    this.dateAndTime = dateAndTime;
  
}


  @Override
  public boolean isDateAndTimeLiteral() { 
    return true; 
  }

  @Override
  public <T> T accept(IASTVisitor<T> visitor) {
    return visitor.visitDateTimeLiteralDateAndTimeLiteral(this);
  }
  
  
     @Override
     public org.rascalmpl.ast.DateAndTime getDateAndTime() {
        return this.dateAndTime;
     }
     
     @Override
     public boolean hasDateAndTime() {
        return true;
     }
  	
}


  public boolean isTimeLiteral() {
    return false;
  }
  
static public class TimeLiteral extends DateTimeLiteral {
  // Production: sig("TimeLiteral",[arg("org.rascalmpl.ast.JustTime","time")])

  
     private final org.rascalmpl.ast.JustTime time;
  

  
public TimeLiteral(ISourceLocation loc, org.rascalmpl.ast.JustTime time) {
  super(loc);
  
    this.time = time;
  
}


  @Override
  public boolean isTimeLiteral() { 
    return true; 
  }

  @Override
  public <T> T accept(IASTVisitor<T> visitor) {
    return visitor.visitDateTimeLiteralTimeLiteral(this);
  }
  
  
     @Override
     public org.rascalmpl.ast.JustTime getTime() {
        return this.time;
     }
     
     @Override
     public boolean hasTime() {
        return true;
     }
  	
}


  public boolean isDateLiteral() {
    return false;
  }
  
static public class DateLiteral extends DateTimeLiteral {
  // Production: sig("DateLiteral",[arg("org.rascalmpl.ast.JustDate","date")])

  
     private final org.rascalmpl.ast.JustDate date;
  

  
public DateLiteral(ISourceLocation loc, org.rascalmpl.ast.JustDate date) {
  super(loc);
  
    this.date = date;
  
}


  @Override
  public boolean isDateLiteral() { 
    return true; 
  }

  @Override
  public <T> T accept(IASTVisitor<T> visitor) {
    return visitor.visitDateTimeLiteralDateLiteral(this);
  }
  
  
     @Override
     public org.rascalmpl.ast.JustDate getDate() {
        return this.date;
     }
     
     @Override
     public boolean hasDate() {
        return true;
     }
  	
}



}
