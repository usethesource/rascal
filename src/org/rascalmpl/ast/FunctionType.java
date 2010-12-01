
package org.rascalmpl.ast;


import org.eclipse.imp.pdb.facts.INode;


public abstract class FunctionType extends AbstractAST {
  public FunctionType(INode node) {
    super(node);
  }
  

  public boolean hasArguments() {
    return false;
  }

  public java.util.List<org.rascalmpl.ast.TypeArg> getArguments() {
    throw new UnsupportedOperationException();
  }

  public boolean hasType() {
    return false;
  }

  public org.rascalmpl.ast.Type getType() {
    throw new UnsupportedOperationException();
  }


static public class Ambiguity extends FunctionType {
  private final java.util.List<org.rascalmpl.ast.FunctionType> alternatives;

  public Ambiguity(INode node, java.util.List<org.rascalmpl.ast.FunctionType> alternatives) {
    super(node);
    this.alternatives = java.util.Collections.unmodifiableList(alternatives);
  }

  public java.util.List<org.rascalmpl.ast.FunctionType> getAlternatives() {
   return alternatives;
  }

  public <T> T accept(IASTVisitor<T> v) {
	return v.visitFunctionTypeAmbiguity(this);
  }
}





  public boolean isTypeArguments() {
    return false;
  }
  
static public class TypeArguments extends FunctionType {
  // Production: sig("TypeArguments",[arg("org.rascalmpl.ast.Type","type"),arg("java.util.List\<org.rascalmpl.ast.TypeArg\>","arguments")])

  
     private final org.rascalmpl.ast.Type type;
  
     private final java.util.List<org.rascalmpl.ast.TypeArg> arguments;
  

  
public TypeArguments(INode node , org.rascalmpl.ast.Type type,  java.util.List<org.rascalmpl.ast.TypeArg> arguments) {
  super(node);
  
    this.type = type;
  
    this.arguments = arguments;
  
}


  @Override
  public boolean isTypeArguments() { 
    return true; 
  }

  @Override
  public <T> T accept(IASTVisitor<T> visitor) {
    return visitor.visitFunctionTypeTypeArguments(this);
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
     public java.util.List<org.rascalmpl.ast.TypeArg> getArguments() {
        return this.arguments;
     }
     
     @Override
     public boolean hasArguments() {
        return true;
     }
  	
}



}
