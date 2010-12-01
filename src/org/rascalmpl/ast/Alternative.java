
package org.rascalmpl.ast;


import org.eclipse.imp.pdb.facts.INode;


public abstract class Alternative extends AbstractAST {
  public Alternative(INode node) {
    super(node);
  }
  

  public boolean hasName() {
    return false;
  }

  public org.rascalmpl.ast.Name getName() {
    throw new UnsupportedOperationException();
  }

  public boolean hasType() {
    return false;
  }

  public org.rascalmpl.ast.Type getType() {
    throw new UnsupportedOperationException();
  }


static public class Ambiguity extends Alternative {
  private final java.util.List<org.rascalmpl.ast.Alternative> alternatives;

  public Ambiguity(INode node, java.util.List<org.rascalmpl.ast.Alternative> alternatives) {
    super(node);
    this.alternatives = java.util.Collections.unmodifiableList(alternatives);
  }

  public java.util.List<org.rascalmpl.ast.Alternative> getAlternatives() {
   return alternatives;
  }

  public <T> T accept(IASTVisitor<T> v) {
	return v.visitAlternativeAmbiguity(this);
  }
}





  public boolean isNamedType() {
    return false;
  }
  
static public class NamedType extends Alternative {
  // Production: sig("NamedType",[arg("org.rascalmpl.ast.Name","name"),arg("org.rascalmpl.ast.Type","type")])

  
     private final org.rascalmpl.ast.Name name;
  
     private final org.rascalmpl.ast.Type type;
  

  
public NamedType(INode node , org.rascalmpl.ast.Name name,  org.rascalmpl.ast.Type type) {
  super(node);
  
    this.name = name;
  
    this.type = type;
  
}


  @Override
  public boolean isNamedType() { 
    return true; 
  }

  @Override
  public <T> T accept(IASTVisitor<T> visitor) {
    return visitor.visitAlternativeNamedType(this);
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
     public org.rascalmpl.ast.Type getType() {
        return this.type;
     }
     
     @Override
     public boolean hasType() {
        return true;
     }
  	
}



}
