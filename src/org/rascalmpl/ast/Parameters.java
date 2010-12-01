
package org.rascalmpl.ast;


import org.eclipse.imp.pdb.facts.INode;


public abstract class Parameters extends AbstractAST {
  public Parameters(INode node) {
    super(node);
  }
  

  public boolean hasFormals() {
    return false;
  }

  public org.rascalmpl.ast.Formals getFormals() {
    throw new UnsupportedOperationException();
  }


static public class Ambiguity extends Parameters {
  private final java.util.List<org.rascalmpl.ast.Parameters> alternatives;

  public Ambiguity(INode node, java.util.List<org.rascalmpl.ast.Parameters> alternatives) {
    super(node);
    this.alternatives = java.util.Collections.unmodifiableList(alternatives);
  }

  public java.util.List<org.rascalmpl.ast.Parameters> getAlternatives() {
   return alternatives;
  }

  public <T> T accept(IASTVisitor<T> v) {
	return v.visitParametersAmbiguity(this);
  }
}





  public boolean isVarArgs() {
    return false;
  }
  
static public class VarArgs extends Parameters {
  // Production: sig("VarArgs",[arg("org.rascalmpl.ast.Formals","formals")])

  
     private final org.rascalmpl.ast.Formals formals;
  

  
public VarArgs(INode node , org.rascalmpl.ast.Formals formals) {
  super(node);
  
    this.formals = formals;
  
}


  @Override
  public boolean isVarArgs() { 
    return true; 
  }

  @Override
  public <T> T accept(IASTVisitor<T> visitor) {
    return visitor.visitParametersVarArgs(this);
  }
  
  
     @Override
     public org.rascalmpl.ast.Formals getFormals() {
        return this.formals;
     }
     
     @Override
     public boolean hasFormals() {
        return true;
     }
  	
}


  public boolean isDefault() {
    return false;
  }
  
static public class Default extends Parameters {
  // Production: sig("Default",[arg("org.rascalmpl.ast.Formals","formals")])

  
     private final org.rascalmpl.ast.Formals formals;
  

  
public Default(INode node , org.rascalmpl.ast.Formals formals) {
  super(node);
  
    this.formals = formals;
  
}


  @Override
  public boolean isDefault() { 
    return true; 
  }

  @Override
  public <T> T accept(IASTVisitor<T> visitor) {
    return visitor.visitParametersDefault(this);
  }
  
  
     @Override
     public org.rascalmpl.ast.Formals getFormals() {
        return this.formals;
     }
     
     @Override
     public boolean hasFormals() {
        return true;
     }
  	
}



}
