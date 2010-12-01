
package org.rascalmpl.ast;


import org.eclipse.imp.pdb.facts.INode;


public abstract class LocalVariableDeclaration extends AbstractAST {
  public LocalVariableDeclaration(INode node) {
    super(node);
  }
  

  public boolean hasDeclarator() {
    return false;
  }

  public org.rascalmpl.ast.Declarator getDeclarator() {
    throw new UnsupportedOperationException();
  }


static public class Ambiguity extends LocalVariableDeclaration {
  private final java.util.List<org.rascalmpl.ast.LocalVariableDeclaration> alternatives;

  public Ambiguity(INode node, java.util.List<org.rascalmpl.ast.LocalVariableDeclaration> alternatives) {
    super(node);
    this.alternatives = java.util.Collections.unmodifiableList(alternatives);
  }

  public java.util.List<org.rascalmpl.ast.LocalVariableDeclaration> getAlternatives() {
   return alternatives;
  }

  public <T> T accept(IASTVisitor<T> v) {
	return v.visitLocalVariableDeclarationAmbiguity(this);
  }
}





  public boolean isDefault() {
    return false;
  }
  
static public class Default extends LocalVariableDeclaration {
  // Production: sig("Default",[arg("org.rascalmpl.ast.Declarator","declarator")])

  
     private final org.rascalmpl.ast.Declarator declarator;
  

  
public Default(INode node , org.rascalmpl.ast.Declarator declarator) {
  super(node);
  
    this.declarator = declarator;
  
}


  @Override
  public boolean isDefault() { 
    return true; 
  }

  @Override
  public <T> T accept(IASTVisitor<T> visitor) {
    return visitor.visitLocalVariableDeclarationDefault(this);
  }
  
  
     @Override
     public org.rascalmpl.ast.Declarator getDeclarator() {
        return this.declarator;
     }
     
     @Override
     public boolean hasDeclarator() {
        return true;
     }
  	
}


  public boolean isDynamic() {
    return false;
  }
  
static public class Dynamic extends LocalVariableDeclaration {
  // Production: sig("Dynamic",[arg("org.rascalmpl.ast.Declarator","declarator")])

  
     private final org.rascalmpl.ast.Declarator declarator;
  

  
public Dynamic(INode node , org.rascalmpl.ast.Declarator declarator) {
  super(node);
  
    this.declarator = declarator;
  
}


  @Override
  public boolean isDynamic() { 
    return true; 
  }

  @Override
  public <T> T accept(IASTVisitor<T> visitor) {
    return visitor.visitLocalVariableDeclarationDynamic(this);
  }
  
  
     @Override
     public org.rascalmpl.ast.Declarator getDeclarator() {
        return this.declarator;
     }
     
     @Override
     public boolean hasDeclarator() {
        return true;
     }
  	
}



}
