
package org.rascalmpl.ast;


import org.eclipse.imp.pdb.facts.INode;


public abstract class FunctionModifier extends AbstractAST {
  public FunctionModifier(INode node) {
    super(node);
  }
  


static public class Ambiguity extends FunctionModifier {
  private final java.util.List<org.rascalmpl.ast.FunctionModifier> alternatives;

  public Ambiguity(INode node, java.util.List<org.rascalmpl.ast.FunctionModifier> alternatives) {
    super(node);
    this.alternatives = java.util.Collections.unmodifiableList(alternatives);
  }

  public java.util.List<org.rascalmpl.ast.FunctionModifier> getAlternatives() {
   return alternatives;
  }

  public <T> T accept(IASTVisitor<T> v) {
	return v.visitFunctionModifierAmbiguity(this);
  }
}





  public boolean isDefault() {
    return false;
  }
  
static public class Default extends FunctionModifier {
  // Production: sig("Default",[])

  

  
public Default(INode node ) {
  super(node);
  
}


  @Override
  public boolean isDefault() { 
    return true; 
  }

  @Override
  public <T> T accept(IASTVisitor<T> visitor) {
    return visitor.visitFunctionModifierDefault(this);
  }
  
  	
}


  public boolean isJava() {
    return false;
  }
  
static public class Java extends FunctionModifier {
  // Production: sig("Java",[])

  

  
public Java(INode node ) {
  super(node);
  
}


  @Override
  public boolean isJava() { 
    return true; 
  }

  @Override
  public <T> T accept(IASTVisitor<T> visitor) {
    return visitor.visitFunctionModifierJava(this);
  }
  
  	
}



}
