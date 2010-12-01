
package org.rascalmpl.ast;


import org.eclipse.imp.pdb.facts.INode;


public abstract class Case extends AbstractAST {
  public Case(INode node) {
    super(node);
  }
  

  public boolean hasPatternWithAction() {
    return false;
  }

  public org.rascalmpl.ast.PatternWithAction getPatternWithAction() {
    throw new UnsupportedOperationException();
  }

  public boolean hasStatement() {
    return false;
  }

  public org.rascalmpl.ast.Statement getStatement() {
    throw new UnsupportedOperationException();
  }


static public class Ambiguity extends Case {
  private final java.util.List<org.rascalmpl.ast.Case> alternatives;

  public Ambiguity(INode node, java.util.List<org.rascalmpl.ast.Case> alternatives) {
    super(node);
    this.alternatives = java.util.Collections.unmodifiableList(alternatives);
  }

  public java.util.List<org.rascalmpl.ast.Case> getAlternatives() {
   return alternatives;
  }

  public <T> T accept(IASTVisitor<T> v) {
	return v.visitCaseAmbiguity(this);
  }
}





  public boolean isPatternWithAction() {
    return false;
  }
  
static public class PatternWithAction extends Case {
  // Production: sig("PatternWithAction",[arg("org.rascalmpl.ast.PatternWithAction","patternWithAction")])

  
     private final org.rascalmpl.ast.PatternWithAction patternWithAction;
  

  
public PatternWithAction(INode node , org.rascalmpl.ast.PatternWithAction patternWithAction) {
  super(node);
  
    this.patternWithAction = patternWithAction;
  
}


  @Override
  public boolean isPatternWithAction() { 
    return true; 
  }

  @Override
  public <T> T accept(IASTVisitor<T> visitor) {
    return visitor.visitCasePatternWithAction(this);
  }
  
  
     @Override
     public org.rascalmpl.ast.PatternWithAction getPatternWithAction() {
        return this.patternWithAction;
     }
     
     @Override
     public boolean hasPatternWithAction() {
        return true;
     }
  	
}


  public boolean isDefault() {
    return false;
  }
  
static public class Default extends Case {
  // Production: sig("Default",[arg("org.rascalmpl.ast.Statement","statement")])

  
     private final org.rascalmpl.ast.Statement statement;
  

  
public Default(INode node , org.rascalmpl.ast.Statement statement) {
  super(node);
  
    this.statement = statement;
  
}


  @Override
  public boolean isDefault() { 
    return true; 
  }

  @Override
  public <T> T accept(IASTVisitor<T> visitor) {
    return visitor.visitCaseDefault(this);
  }
  
  
     @Override
     public org.rascalmpl.ast.Statement getStatement() {
        return this.statement;
     }
     
     @Override
     public boolean hasStatement() {
        return true;
     }
  	
}



}
