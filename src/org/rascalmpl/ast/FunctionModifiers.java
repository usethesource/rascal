
package org.rascalmpl.ast;


import org.eclipse.imp.pdb.facts.INode;


public abstract class FunctionModifiers extends AbstractAST {
  public FunctionModifiers(INode node) {
    super(node);
  }
  

  public boolean hasModifiers() {
    return false;
  }

  public java.util.List<org.rascalmpl.ast.FunctionModifier> getModifiers() {
    throw new UnsupportedOperationException();
  }


static public class Ambiguity extends FunctionModifiers {
  private final java.util.List<org.rascalmpl.ast.FunctionModifiers> alternatives;

  public Ambiguity(INode node, java.util.List<org.rascalmpl.ast.FunctionModifiers> alternatives) {
    super(node);
    this.alternatives = java.util.Collections.unmodifiableList(alternatives);
  }

  public java.util.List<org.rascalmpl.ast.FunctionModifiers> getAlternatives() {
   return alternatives;
  }

  public <T> T accept(IASTVisitor<T> v) {
	return v.visitFunctionModifiersAmbiguity(this);
  }
}





  public boolean isList() {
    return false;
  }
  
static public class List extends FunctionModifiers {
  // Production: sig("List",[arg("java.util.List\<org.rascalmpl.ast.FunctionModifier\>","modifiers")])

  
     private final java.util.List<org.rascalmpl.ast.FunctionModifier> modifiers;
  

  
public List(INode node , java.util.List<org.rascalmpl.ast.FunctionModifier> modifiers) {
  super(node);
  
    this.modifiers = modifiers;
  
}


  @Override
  public boolean isList() { 
    return true; 
  }

  @Override
  public <T> T accept(IASTVisitor<T> visitor) {
    return visitor.visitFunctionModifiersList(this);
  }
  
  
     @Override
     public java.util.List<org.rascalmpl.ast.FunctionModifier> getModifiers() {
        return this.modifiers;
     }
     
     @Override
     public boolean hasModifiers() {
        return true;
     }
  	
}



}
