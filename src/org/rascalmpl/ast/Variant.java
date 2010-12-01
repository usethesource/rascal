
package org.rascalmpl.ast;


import org.eclipse.imp.pdb.facts.INode;


public abstract class Variant extends AbstractAST {
  public Variant(INode node) {
    super(node);
  }
  

  public boolean hasArguments() {
    return false;
  }

  public java.util.List<org.rascalmpl.ast.TypeArg> getArguments() {
    throw new UnsupportedOperationException();
  }

  public boolean hasName() {
    return false;
  }

  public org.rascalmpl.ast.Name getName() {
    throw new UnsupportedOperationException();
  }


static public class Ambiguity extends Variant {
  private final java.util.List<org.rascalmpl.ast.Variant> alternatives;

  public Ambiguity(INode node, java.util.List<org.rascalmpl.ast.Variant> alternatives) {
    super(node);
    this.alternatives = java.util.Collections.unmodifiableList(alternatives);
  }

  public java.util.List<org.rascalmpl.ast.Variant> getAlternatives() {
   return alternatives;
  }

  public <T> T accept(IASTVisitor<T> v) {
	return v.visitVariantAmbiguity(this);
  }
}





  public boolean isNAryConstructor() {
    return false;
  }
  
static public class NAryConstructor extends Variant {
  // Production: sig("NAryConstructor",[arg("org.rascalmpl.ast.Name","name"),arg("java.util.List\<org.rascalmpl.ast.TypeArg\>","arguments")])

  
     private final org.rascalmpl.ast.Name name;
  
     private final java.util.List<org.rascalmpl.ast.TypeArg> arguments;
  

  
public NAryConstructor(INode node , org.rascalmpl.ast.Name name,  java.util.List<org.rascalmpl.ast.TypeArg> arguments) {
  super(node);
  
    this.name = name;
  
    this.arguments = arguments;
  
}


  @Override
  public boolean isNAryConstructor() { 
    return true; 
  }

  @Override
  public <T> T accept(IASTVisitor<T> visitor) {
    return visitor.visitVariantNAryConstructor(this);
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
     public java.util.List<org.rascalmpl.ast.TypeArg> getArguments() {
        return this.arguments;
     }
     
     @Override
     public boolean hasArguments() {
        return true;
     }
  	
}



}
