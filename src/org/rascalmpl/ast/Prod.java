
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


public abstract class Prod extends AbstractAST {
  public Prod(INode node) {
    super(node);
  }
  

  public boolean hasArgs() {
    return false;
  }

  public java.util.List<org.rascalmpl.ast.Sym> getArgs() {
    throw new UnsupportedOperationException();
  }

  public boolean hasGroup() {
    return false;
  }

  public org.rascalmpl.ast.Prod getGroup() {
    throw new UnsupportedOperationException();
  }

  public boolean hasLhs() {
    return false;
  }

  public org.rascalmpl.ast.Prod getLhs() {
    throw new UnsupportedOperationException();
  }

  public boolean hasModifiers() {
    return false;
  }

  public java.util.List<org.rascalmpl.ast.ProdModifier> getModifiers() {
    throw new UnsupportedOperationException();
  }

  public boolean hasRhs() {
    return false;
  }

  public org.rascalmpl.ast.Prod getRhs() {
    throw new UnsupportedOperationException();
  }

  public boolean hasAssociativity() {
    return false;
  }

  public org.rascalmpl.ast.Assoc getAssociativity() {
    throw new UnsupportedOperationException();
  }

  public boolean hasName() {
    return false;
  }

  public org.rascalmpl.ast.Name getName() {
    throw new UnsupportedOperationException();
  }

  public boolean hasProd() {
    return false;
  }

  public org.rascalmpl.ast.Prod getProd() {
    throw new UnsupportedOperationException();
  }

  public boolean hasReferenced() {
    return false;
  }

  public org.rascalmpl.ast.Name getReferenced() {
    throw new UnsupportedOperationException();
  }

  public boolean hasAction() {
    return false;
  }

  public org.rascalmpl.ast.LanguageAction getAction() {
    throw new UnsupportedOperationException();
  }


static public class Ambiguity extends Prod {
  private final java.util.List<org.rascalmpl.ast.Prod> alternatives;

  public Ambiguity(INode node, java.util.List<org.rascalmpl.ast.Prod> alternatives) {
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
  
  public java.util.List<org.rascalmpl.ast.Prod> getAlternatives() {
   return alternatives;
  }

  public <T> T accept(IASTVisitor<T> v) {
	return v.visitProdAmbiguity(this);
  }
}





  public boolean isAll() {
    return false;
  }
  
static public class All extends Prod {
  // Production: sig("All",[arg("org.rascalmpl.ast.Prod","lhs"),arg("org.rascalmpl.ast.Prod","rhs")])

  
     private final org.rascalmpl.ast.Prod lhs;
  
     private final org.rascalmpl.ast.Prod rhs;
  

  
public All(INode node , org.rascalmpl.ast.Prod lhs,  org.rascalmpl.ast.Prod rhs) {
  super(node);
  
    this.lhs = lhs;
  
    this.rhs = rhs;
  
}


  @Override
  public boolean isAll() { 
    return true; 
  }

  @Override
  public <T> T accept(IASTVisitor<T> visitor) {
    return visitor.visitProdAll(this);
  }
  
  
     @Override
     public org.rascalmpl.ast.Prod getLhs() {
        return this.lhs;
     }
     
     @Override
     public boolean hasLhs() {
        return true;
     }
  
     @Override
     public org.rascalmpl.ast.Prod getRhs() {
        return this.rhs;
     }
     
     @Override
     public boolean hasRhs() {
        return true;
     }
  	
}


  public boolean isFollow() {
    return false;
  }
  
static public class Follow extends Prod {
  // Production: sig("Follow",[arg("org.rascalmpl.ast.Prod","lhs"),arg("org.rascalmpl.ast.Prod","rhs")])

  
     private final org.rascalmpl.ast.Prod lhs;
  
     private final org.rascalmpl.ast.Prod rhs;
  

  
public Follow(INode node , org.rascalmpl.ast.Prod lhs,  org.rascalmpl.ast.Prod rhs) {
  super(node);
  
    this.lhs = lhs;
  
    this.rhs = rhs;
  
}


  @Override
  public boolean isFollow() { 
    return true; 
  }

  @Override
  public <T> T accept(IASTVisitor<T> visitor) {
    return visitor.visitProdFollow(this);
  }
  
  
     @Override
     public org.rascalmpl.ast.Prod getLhs() {
        return this.lhs;
     }
     
     @Override
     public boolean hasLhs() {
        return true;
     }
  
     @Override
     public org.rascalmpl.ast.Prod getRhs() {
        return this.rhs;
     }
     
     @Override
     public boolean hasRhs() {
        return true;
     }
  	
}


  public boolean isFirst() {
    return false;
  }
  
static public class First extends Prod {
  // Production: sig("First",[arg("org.rascalmpl.ast.Prod","lhs"),arg("org.rascalmpl.ast.Prod","rhs")])

  
     private final org.rascalmpl.ast.Prod lhs;
  
     private final org.rascalmpl.ast.Prod rhs;
  

  
public First(INode node , org.rascalmpl.ast.Prod lhs,  org.rascalmpl.ast.Prod rhs) {
  super(node);
  
    this.lhs = lhs;
  
    this.rhs = rhs;
  
}


  @Override
  public boolean isFirst() { 
    return true; 
  }

  @Override
  public <T> T accept(IASTVisitor<T> visitor) {
    return visitor.visitProdFirst(this);
  }
  
  
     @Override
     public org.rascalmpl.ast.Prod getLhs() {
        return this.lhs;
     }
     
     @Override
     public boolean hasLhs() {
        return true;
     }
  
     @Override
     public org.rascalmpl.ast.Prod getRhs() {
        return this.rhs;
     }
     
     @Override
     public boolean hasRhs() {
        return true;
     }
  	
}


  public boolean isUnlabeled() {
    return false;
  }
  
static public class Unlabeled extends Prod {
  // Production: sig("Unlabeled",[arg("java.util.List\<org.rascalmpl.ast.ProdModifier\>","modifiers"),arg("java.util.List\<org.rascalmpl.ast.Sym\>","args")])

  
     private final java.util.List<org.rascalmpl.ast.ProdModifier> modifiers;
  
     private final java.util.List<org.rascalmpl.ast.Sym> args;
  

  
public Unlabeled(INode node , java.util.List<org.rascalmpl.ast.ProdModifier> modifiers,  java.util.List<org.rascalmpl.ast.Sym> args) {
  super(node);
  
    this.modifiers = modifiers;
  
    this.args = args;
  
}


  @Override
  public boolean isUnlabeled() { 
    return true; 
  }

  @Override
  public <T> T accept(IASTVisitor<T> visitor) {
    return visitor.visitProdUnlabeled(this);
  }
  
  
     @Override
     public java.util.List<org.rascalmpl.ast.ProdModifier> getModifiers() {
        return this.modifiers;
     }
     
     @Override
     public boolean hasModifiers() {
        return true;
     }
  
     @Override
     public java.util.List<org.rascalmpl.ast.Sym> getArgs() {
        return this.args;
     }
     
     @Override
     public boolean hasArgs() {
        return true;
     }
  	
}


  public boolean isLabeled() {
    return false;
  }
  
static public class Labeled extends Prod {
  // Production: sig("Labeled",[arg("java.util.List\<org.rascalmpl.ast.ProdModifier\>","modifiers"),arg("org.rascalmpl.ast.Name","name"),arg("java.util.List\<org.rascalmpl.ast.Sym\>","args")])

  
     private final java.util.List<org.rascalmpl.ast.ProdModifier> modifiers;
  
     private final org.rascalmpl.ast.Name name;
  
     private final java.util.List<org.rascalmpl.ast.Sym> args;
  

  
public Labeled(INode node , java.util.List<org.rascalmpl.ast.ProdModifier> modifiers,  org.rascalmpl.ast.Name name,  java.util.List<org.rascalmpl.ast.Sym> args) {
  super(node);
  
    this.modifiers = modifiers;
  
    this.name = name;
  
    this.args = args;
  
}


  @Override
  public boolean isLabeled() { 
    return true; 
  }

  @Override
  public <T> T accept(IASTVisitor<T> visitor) {
    return visitor.visitProdLabeled(this);
  }
  
  
     @Override
     public java.util.List<org.rascalmpl.ast.ProdModifier> getModifiers() {
        return this.modifiers;
     }
     
     @Override
     public boolean hasModifiers() {
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
  
     @Override
     public java.util.List<org.rascalmpl.ast.Sym> getArgs() {
        return this.args;
     }
     
     @Override
     public boolean hasArgs() {
        return true;
     }
  	
}


  public boolean isAction() {
    return false;
  }
  
static public class Action extends Prod {
  // Production: sig("Action",[arg("org.rascalmpl.ast.Prod","prod"),arg("org.rascalmpl.ast.LanguageAction","action")])

  
     private final org.rascalmpl.ast.Prod prod;
  
     private final org.rascalmpl.ast.LanguageAction action;
  

  
public Action(INode node , org.rascalmpl.ast.Prod prod,  org.rascalmpl.ast.LanguageAction action) {
  super(node);
  
    this.prod = prod;
  
    this.action = action;
  
}


  @Override
  public boolean isAction() { 
    return true; 
  }

  @Override
  public <T> T accept(IASTVisitor<T> visitor) {
    return visitor.visitProdAction(this);
  }
  
  
     @Override
     public org.rascalmpl.ast.Prod getProd() {
        return this.prod;
     }
     
     @Override
     public boolean hasProd() {
        return true;
     }
  
     @Override
     public org.rascalmpl.ast.LanguageAction getAction() {
        return this.action;
     }
     
     @Override
     public boolean hasAction() {
        return true;
     }
  	
}


  public boolean isReference() {
    return false;
  }
  
static public class Reference extends Prod {
  // Production: sig("Reference",[arg("org.rascalmpl.ast.Name","referenced")])

  
     private final org.rascalmpl.ast.Name referenced;
  

  
public Reference(INode node , org.rascalmpl.ast.Name referenced) {
  super(node);
  
    this.referenced = referenced;
  
}


  @Override
  public boolean isReference() { 
    return true; 
  }

  @Override
  public <T> T accept(IASTVisitor<T> visitor) {
    return visitor.visitProdReference(this);
  }
  
  
     @Override
     public org.rascalmpl.ast.Name getReferenced() {
        return this.referenced;
     }
     
     @Override
     public boolean hasReferenced() {
        return true;
     }
  	
}


  public boolean isReject() {
    return false;
  }
  
static public class Reject extends Prod {
  // Production: sig("Reject",[arg("org.rascalmpl.ast.Prod","lhs"),arg("org.rascalmpl.ast.Prod","rhs")])

  
     private final org.rascalmpl.ast.Prod lhs;
  
     private final org.rascalmpl.ast.Prod rhs;
  

  
public Reject(INode node , org.rascalmpl.ast.Prod lhs,  org.rascalmpl.ast.Prod rhs) {
  super(node);
  
    this.lhs = lhs;
  
    this.rhs = rhs;
  
}


  @Override
  public boolean isReject() { 
    return true; 
  }

  @Override
  public <T> T accept(IASTVisitor<T> visitor) {
    return visitor.visitProdReject(this);
  }
  
  
     @Override
     public org.rascalmpl.ast.Prod getLhs() {
        return this.lhs;
     }
     
     @Override
     public boolean hasLhs() {
        return true;
     }
  
     @Override
     public org.rascalmpl.ast.Prod getRhs() {
        return this.rhs;
     }
     
     @Override
     public boolean hasRhs() {
        return true;
     }
  	
}


  public boolean isOthers() {
    return false;
  }
  
static public class Others extends Prod {
  // Production: sig("Others",[])

  

  
public Others(INode node ) {
  super(node);
  
}


  @Override
  public boolean isOthers() { 
    return true; 
  }

  @Override
  public <T> T accept(IASTVisitor<T> visitor) {
    return visitor.visitProdOthers(this);
  }
  
  	
}


  public boolean isAssociativityGroup() {
    return false;
  }
  
static public class AssociativityGroup extends Prod {
  // Production: sig("AssociativityGroup",[arg("org.rascalmpl.ast.Assoc","associativity"),arg("org.rascalmpl.ast.Prod","group")])

  
     private final org.rascalmpl.ast.Assoc associativity;
  
     private final org.rascalmpl.ast.Prod group;
  

  
public AssociativityGroup(INode node , org.rascalmpl.ast.Assoc associativity,  org.rascalmpl.ast.Prod group) {
  super(node);
  
    this.associativity = associativity;
  
    this.group = group;
  
}


  @Override
  public boolean isAssociativityGroup() { 
    return true; 
  }

  @Override
  public <T> T accept(IASTVisitor<T> visitor) {
    return visitor.visitProdAssociativityGroup(this);
  }
  
  
     @Override
     public org.rascalmpl.ast.Assoc getAssociativity() {
        return this.associativity;
     }
     
     @Override
     public boolean hasAssociativity() {
        return true;
     }
  
     @Override
     public org.rascalmpl.ast.Prod getGroup() {
        return this.group;
     }
     
     @Override
     public boolean hasGroup() {
        return true;
     }
  	
}



}
