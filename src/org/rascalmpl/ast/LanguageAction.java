
package org.rascalmpl.ast;


import org.eclipse.imp.pdb.facts.INode;


public abstract class LanguageAction extends AbstractAST {
  public LanguageAction(INode node) {
    super(node);
  }
  

  public boolean hasExpression() {
    return false;
  }

  public org.rascalmpl.ast.Expression getExpression() {
    throw new UnsupportedOperationException();
  }

  public boolean hasStatements() {
    return false;
  }

  public java.util.List<org.rascalmpl.ast.Statement> getStatements() {
    throw new UnsupportedOperationException();
  }


static public class Ambiguity extends LanguageAction {
  private final java.util.List<org.rascalmpl.ast.LanguageAction> alternatives;

  public Ambiguity(INode node, java.util.List<org.rascalmpl.ast.LanguageAction> alternatives) {
    super(node);
    this.alternatives = java.util.Collections.unmodifiableList(alternatives);
  }

  public java.util.List<org.rascalmpl.ast.LanguageAction> getAlternatives() {
   return alternatives;
  }

  public <T> T accept(IASTVisitor<T> v) {
	return v.visitLanguageActionAmbiguity(this);
  }
}





  public boolean isBuild() {
    return false;
  }
  
static public class Build extends LanguageAction {
  // Production: sig("Build",[arg("org.rascalmpl.ast.Expression","expression")])

  
     private final org.rascalmpl.ast.Expression expression;
  

  
public Build(INode node , org.rascalmpl.ast.Expression expression) {
  super(node);
  
    this.expression = expression;
  
}


  @Override
  public boolean isBuild() { 
    return true; 
  }

  @Override
  public <T> T accept(IASTVisitor<T> visitor) {
    return visitor.visitLanguageActionBuild(this);
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


  public boolean isAction() {
    return false;
  }
  
static public class Action extends LanguageAction {
  // Production: sig("Action",[arg("java.util.List\<org.rascalmpl.ast.Statement\>","statements")])

  
     private final java.util.List<org.rascalmpl.ast.Statement> statements;
  

  
public Action(INode node , java.util.List<org.rascalmpl.ast.Statement> statements) {
  super(node);
  
    this.statements = statements;
  
}


  @Override
  public boolean isAction() { 
    return true; 
  }

  @Override
  public <T> T accept(IASTVisitor<T> visitor) {
    return visitor.visitLanguageActionAction(this);
  }
  
  
     @Override
     public java.util.List<org.rascalmpl.ast.Statement> getStatements() {
        return this.statements;
     }
     
     @Override
     public boolean hasStatements() {
        return true;
     }
  	
}



}
