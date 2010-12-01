
package org.rascalmpl.ast;


import org.eclipse.imp.pdb.facts.INode;


public abstract class Renamings extends AbstractAST {
  public Renamings(INode node) {
    super(node);
  }
  

  public boolean hasRenamings() {
    return false;
  }

  public java.util.List<org.rascalmpl.ast.Renaming> getRenamings() {
    throw new UnsupportedOperationException();
  }


static public class Ambiguity extends Renamings {
  private final java.util.List<org.rascalmpl.ast.Renamings> alternatives;

  public Ambiguity(INode node, java.util.List<org.rascalmpl.ast.Renamings> alternatives) {
    super(node);
    this.alternatives = java.util.Collections.unmodifiableList(alternatives);
  }

  public java.util.List<org.rascalmpl.ast.Renamings> getAlternatives() {
   return alternatives;
  }

  public <T> T accept(IASTVisitor<T> v) {
	return v.visitRenamingsAmbiguity(this);
  }
}





  public boolean isDefault() {
    return false;
  }
  
static public class Default extends Renamings {
  // Production: sig("Default",[arg("java.util.List\<org.rascalmpl.ast.Renaming\>","renamings")])

  
     private final java.util.List<org.rascalmpl.ast.Renaming> renamings;
  

  
public Default(INode node , java.util.List<org.rascalmpl.ast.Renaming> renamings) {
  super(node);
  
    this.renamings = renamings;
  
}


  @Override
  public boolean isDefault() { 
    return true; 
  }

  @Override
  public <T> T accept(IASTVisitor<T> visitor) {
    return visitor.visitRenamingsDefault(this);
  }
  
  
     @Override
     public java.util.List<org.rascalmpl.ast.Renaming> getRenamings() {
        return this.renamings;
     }
     
     @Override
     public boolean hasRenamings() {
        return true;
     }
  	
}



}
