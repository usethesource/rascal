
package org.rascalmpl.ast;


import org.eclipse.imp.pdb.facts.INode;


public abstract class QualifiedName extends AbstractAST {
  public QualifiedName(INode node) {
    super(node);
  }
  

  public boolean hasNames() {
    return false;
  }

  public java.util.List<org.rascalmpl.ast.Name> getNames() {
    throw new UnsupportedOperationException();
  }


static public class Ambiguity extends QualifiedName {
  private final java.util.List<org.rascalmpl.ast.QualifiedName> alternatives;

  public Ambiguity(INode node, java.util.List<org.rascalmpl.ast.QualifiedName> alternatives) {
    super(node);
    this.alternatives = java.util.Collections.unmodifiableList(alternatives);
  }

  public java.util.List<org.rascalmpl.ast.QualifiedName> getAlternatives() {
   return alternatives;
  }

  public <T> T accept(IASTVisitor<T> v) {
	return v.visitQualifiedNameAmbiguity(this);
  }
}





  public boolean isDefault() {
    return false;
  }
  
static public class Default extends QualifiedName {
  // Production: sig("Default",[arg("java.util.List\<org.rascalmpl.ast.Name\>","names")])

  
     private final java.util.List<org.rascalmpl.ast.Name> names;
  

  
public Default(INode node , java.util.List<org.rascalmpl.ast.Name> names) {
  super(node);
  
    this.names = names;
  
}


  @Override
  public boolean isDefault() { 
    return true; 
  }

  @Override
  public <T> T accept(IASTVisitor<T> visitor) {
    return visitor.visitQualifiedNameDefault(this);
  }
  
  
     @Override
     public java.util.List<org.rascalmpl.ast.Name> getNames() {
        return this.names;
     }
     
     @Override
     public boolean hasNames() {
        return true;
     }
  	
}



}
