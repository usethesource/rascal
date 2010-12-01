
package org.rascalmpl.ast;


import org.eclipse.imp.pdb.facts.INode;


public abstract class Formal extends AbstractAST {
  public Formal(INode node) {
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


static public class Ambiguity extends Formal {
  private final java.util.List<org.rascalmpl.ast.Formal> alternatives;

  public Ambiguity(INode node, java.util.List<org.rascalmpl.ast.Formal> alternatives) {
    super(node);
    this.alternatives = java.util.Collections.unmodifiableList(alternatives);
  }

  public java.util.List<org.rascalmpl.ast.Formal> getAlternatives() {
   return alternatives;
  }

  public <T> T accept(IASTVisitor<T> v) {
	return v.visitFormalAmbiguity(this);
  }
}





  public boolean isTypeName() {
    return false;
  }
  
static public class TypeName extends Formal {
  // Production: sig("TypeName",[arg("org.rascalmpl.ast.Type","type"),arg("org.rascalmpl.ast.Name","name")])

  
     private final org.rascalmpl.ast.Type type;
  
     private final org.rascalmpl.ast.Name name;
  

  
public TypeName(INode node , org.rascalmpl.ast.Type type,  org.rascalmpl.ast.Name name) {
  super(node);
  
    this.type = type;
  
    this.name = name;
  
}


  @Override
  public boolean isTypeName() { 
    return true; 
  }

  @Override
  public <T> T accept(IASTVisitor<T> visitor) {
    return visitor.visitFormalTypeName(this);
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
     public org.rascalmpl.ast.Name getName() {
        return this.name;
     }
     
     @Override
     public boolean hasName() {
        return true;
     }
  	
}



}
