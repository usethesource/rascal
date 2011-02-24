
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


public abstract class Class extends AbstractAST {
  public Class(INode node) {
    super(node);
  }
  

  public boolean hasCharClass() {
    return false;
  }

  public org.rascalmpl.ast.Class getCharClass() {
    throw new UnsupportedOperationException();
  }

  public boolean hasCharclass() {
    return false;
  }

  public org.rascalmpl.ast.Class getCharclass() {
    throw new UnsupportedOperationException();
  }

  public boolean hasRhs() {
    return false;
  }

  public org.rascalmpl.ast.Class getRhs() {
    throw new UnsupportedOperationException();
  }

  public boolean hasLhs() {
    return false;
  }

  public org.rascalmpl.ast.Class getLhs() {
    throw new UnsupportedOperationException();
  }

  public boolean hasRanges() {
    return false;
  }

  public java.util.List<org.rascalmpl.ast.Range> getRanges() {
    throw new UnsupportedOperationException();
  }


static public class Ambiguity extends Class {
  private final java.util.List<org.rascalmpl.ast.Class> alternatives;

  public Ambiguity(INode node, java.util.List<org.rascalmpl.ast.Class> alternatives) {
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
  
  public java.util.List<org.rascalmpl.ast.Class> getAlternatives() {
   return alternatives;
  }

  public <T> T accept(IASTVisitor<T> v) {
	return v.visitClassAmbiguity(this);
  }
}





  public boolean isUnion() {
    return false;
  }
  
static public class Union extends Class {
  // Production: sig("Union",[arg("org.rascalmpl.ast.Class","lhs"),arg("org.rascalmpl.ast.Class","rhs")])

  
     private final org.rascalmpl.ast.Class lhs;
  
     private final org.rascalmpl.ast.Class rhs;
  

  
public Union(INode node , org.rascalmpl.ast.Class lhs,  org.rascalmpl.ast.Class rhs) {
  super(node);
  
    this.lhs = lhs;
  
    this.rhs = rhs;
  
}


  @Override
  public boolean isUnion() { 
    return true; 
  }

  @Override
  public <T> T accept(IASTVisitor<T> visitor) {
    return visitor.visitClassUnion(this);
  }
  
  
     @Override
     public org.rascalmpl.ast.Class getLhs() {
        return this.lhs;
     }
     
     @Override
     public boolean hasLhs() {
        return true;
     }
  
     @Override
     public org.rascalmpl.ast.Class getRhs() {
        return this.rhs;
     }
     
     @Override
     public boolean hasRhs() {
        return true;
     }
  	
}


  public boolean isDifference() {
    return false;
  }
  
static public class Difference extends Class {
  // Production: sig("Difference",[arg("org.rascalmpl.ast.Class","lhs"),arg("org.rascalmpl.ast.Class","rhs")])

  
     private final org.rascalmpl.ast.Class lhs;
  
     private final org.rascalmpl.ast.Class rhs;
  

  
public Difference(INode node , org.rascalmpl.ast.Class lhs,  org.rascalmpl.ast.Class rhs) {
  super(node);
  
    this.lhs = lhs;
  
    this.rhs = rhs;
  
}


  @Override
  public boolean isDifference() { 
    return true; 
  }

  @Override
  public <T> T accept(IASTVisitor<T> visitor) {
    return visitor.visitClassDifference(this);
  }
  
  
     @Override
     public org.rascalmpl.ast.Class getLhs() {
        return this.lhs;
     }
     
     @Override
     public boolean hasLhs() {
        return true;
     }
  
     @Override
     public org.rascalmpl.ast.Class getRhs() {
        return this.rhs;
     }
     
     @Override
     public boolean hasRhs() {
        return true;
     }
  	
}


  public boolean isSimpleCharclass() {
    return false;
  }
  
static public class SimpleCharclass extends Class {
  // Production: sig("SimpleCharclass",[arg("java.util.List\<org.rascalmpl.ast.Range\>","ranges")])

  
     private final java.util.List<org.rascalmpl.ast.Range> ranges;
  

  
public SimpleCharclass(INode node , java.util.List<org.rascalmpl.ast.Range> ranges) {
  super(node);
  
    this.ranges = ranges;
  
}


  @Override
  public boolean isSimpleCharclass() { 
    return true; 
  }

  @Override
  public <T> T accept(IASTVisitor<T> visitor) {
    return visitor.visitClassSimpleCharclass(this);
  }
  
  
     @Override
     public java.util.List<org.rascalmpl.ast.Range> getRanges() {
        return this.ranges;
     }
     
     @Override
     public boolean hasRanges() {
        return true;
     }
  	
}


  public boolean isIntersection() {
    return false;
  }
  
static public class Intersection extends Class {
  // Production: sig("Intersection",[arg("org.rascalmpl.ast.Class","lhs"),arg("org.rascalmpl.ast.Class","rhs")])

  
     private final org.rascalmpl.ast.Class lhs;
  
     private final org.rascalmpl.ast.Class rhs;
  

  
public Intersection(INode node , org.rascalmpl.ast.Class lhs,  org.rascalmpl.ast.Class rhs) {
  super(node);
  
    this.lhs = lhs;
  
    this.rhs = rhs;
  
}


  @Override
  public boolean isIntersection() { 
    return true; 
  }

  @Override
  public <T> T accept(IASTVisitor<T> visitor) {
    return visitor.visitClassIntersection(this);
  }
  
  
     @Override
     public org.rascalmpl.ast.Class getLhs() {
        return this.lhs;
     }
     
     @Override
     public boolean hasLhs() {
        return true;
     }
  
     @Override
     public org.rascalmpl.ast.Class getRhs() {
        return this.rhs;
     }
     
     @Override
     public boolean hasRhs() {
        return true;
     }
  	
}


  public boolean isComplement() {
    return false;
  }
  
static public class Complement extends Class {
  // Production: sig("Complement",[arg("org.rascalmpl.ast.Class","charClass")])

  
     private final org.rascalmpl.ast.Class charClass;
  

  
public Complement(INode node , org.rascalmpl.ast.Class charClass) {
  super(node);
  
    this.charClass = charClass;
  
}


  @Override
  public boolean isComplement() { 
    return true; 
  }

  @Override
  public <T> T accept(IASTVisitor<T> visitor) {
    return visitor.visitClassComplement(this);
  }
  
  
     @Override
     public org.rascalmpl.ast.Class getCharClass() {
        return this.charClass;
     }
     
     @Override
     public boolean hasCharClass() {
        return true;
     }
  	
}


  public boolean isBracket() {
    return false;
  }
  
static public class Bracket extends Class {
  // Production: sig("Bracket",[arg("org.rascalmpl.ast.Class","charclass")])

  
     private final org.rascalmpl.ast.Class charclass;
  

  
public Bracket(INode node , org.rascalmpl.ast.Class charclass) {
  super(node);
  
    this.charclass = charclass;
  
}


  @Override
  public boolean isBracket() { 
    return true; 
  }

  @Override
  public <T> T accept(IASTVisitor<T> visitor) {
    return visitor.visitClassBracket(this);
  }
  
  
     @Override
     public org.rascalmpl.ast.Class getCharclass() {
        return this.charclass;
     }
     
     @Override
     public boolean hasCharclass() {
        return true;
     }
  	
}



}
