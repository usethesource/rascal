
package org.rascalmpl.ast;


import org.eclipse.imp.pdb.facts.INode;


public abstract class Declarator extends AbstractAST {
  public Declarator(INode node) {
    super(node);
  }
  

  public boolean hasVariables() {
    return false;
  }

  public java.util.List<org.rascalmpl.ast.Variable> getVariables() {
    throw new UnsupportedOperationException();
  }

  public boolean hasType() {
    return false;
  }

  public org.rascalmpl.ast.Type getType() {
    throw new UnsupportedOperationException();
  }


static public class Ambiguity extends Declarator {
  private final java.util.List<org.rascalmpl.ast.Declarator> alternatives;

  public Ambiguity(INode node, java.util.List<org.rascalmpl.ast.Declarator> alternatives) {
    super(node);
    this.alternatives = java.util.Collections.unmodifiableList(alternatives);
  }

  public java.util.List<org.rascalmpl.ast.Declarator> getAlternatives() {
   return alternatives;
  }

  public <T> T accept(IASTVisitor<T> v) {
	return v.visitDeclaratorAmbiguity(this);
  }
}





  public boolean isDefault() {
    return false;
  }
  
static public class Default extends Declarator {
  // Production: sig("Default",[arg("org.rascalmpl.ast.Type","type"),arg("java.util.List\<org.rascalmpl.ast.Variable\>","variables")])

  
     private final org.rascalmpl.ast.Type type;
  
     private final java.util.List<org.rascalmpl.ast.Variable> variables;
  

  
public Default(INode node , org.rascalmpl.ast.Type type,  java.util.List<org.rascalmpl.ast.Variable> variables) {
  super(node);
  
    this.type = type;
  
    this.variables = variables;
  
}


  @Override
  public boolean isDefault() { 
    return true; 
  }

  @Override
  public <T> T accept(IASTVisitor<T> visitor) {
    return visitor.visitDeclaratorDefault(this);
  }
  
  
     @Override
     public org.rascalmpl.ast.Type getType() {
        return this.type;
     }
     
     @Override
     public boolean hasType() {
        return true;
     }
  
     @Override
     public java.util.List<org.rascalmpl.ast.Variable> getVariables() {
        return this.variables;
     }
     
     @Override
     public boolean hasVariables() {
        return true;
     }
  	
}



}
