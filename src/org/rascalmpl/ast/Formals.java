
package org.rascalmpl.ast;


import org.eclipse.imp.pdb.facts.INode;


public abstract class Formals extends AbstractAST {
  public Formals(INode node) {
    super(node);
  }
  

  public boolean hasFormals() {
    return false;
  }

  public java.util.List<org.rascalmpl.ast.Expression> getFormals() {
    throw new UnsupportedOperationException();
  }


static public class Ambiguity extends Formals {
  private final java.util.List<org.rascalmpl.ast.Formals> alternatives;

  public Ambiguity(INode node, java.util.List<org.rascalmpl.ast.Formals> alternatives) {
    super(node);
    this.alternatives = java.util.Collections.unmodifiableList(alternatives);
  }

  public java.util.List<org.rascalmpl.ast.Formals> getAlternatives() {
   return alternatives;
  }

  public <T> T accept(IASTVisitor<T> v) {
	return v.visitFormalsAmbiguity(this);
  }
}





  public boolean isDefault() {
    return false;
  }
  
static public class Default extends Formals {
  // Production: sig("Default",[arg("java.util.List\<org.rascalmpl.ast.Expression\>","formals")])

  
     private final java.util.List<org.rascalmpl.ast.Expression> formals;
  

  
public Default(INode node , java.util.List<org.rascalmpl.ast.Expression> formals) {
  super(node);
  
    this.formals = formals;
  
}


  @Override
  public boolean isDefault() { 
    return true; 
  }

  @Override
  public <T> T accept(IASTVisitor<T> visitor) {
    return visitor.visitFormalsDefault(this);
  }
  
  
     @Override
     public java.util.List<org.rascalmpl.ast.Expression> getFormals() {
        return this.formals;
     }
     
     @Override
     public boolean hasFormals() {
        return true;
     }
  	
}



}
