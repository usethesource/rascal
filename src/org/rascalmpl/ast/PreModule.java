
package org.rascalmpl.ast;


import org.eclipse.imp.pdb.facts.INode;


public abstract class PreModule extends AbstractAST {
  public PreModule(INode node) {
    super(node);
  }
  

  public boolean hasHeader() {
    return false;
  }

  public org.rascalmpl.ast.Header getHeader() {
    throw new UnsupportedOperationException();
  }


static public class Ambiguity extends PreModule {
  private final java.util.List<org.rascalmpl.ast.PreModule> alternatives;

  public Ambiguity(INode node, java.util.List<org.rascalmpl.ast.PreModule> alternatives) {
    super(node);
    this.alternatives = java.util.Collections.unmodifiableList(alternatives);
  }

  public java.util.List<org.rascalmpl.ast.PreModule> getAlternatives() {
   return alternatives;
  }

  public <T> T accept(IASTVisitor<T> v) {
	return v.visitPreModuleAmbiguity(this);
  }
}





  public boolean isDefault() {
    return false;
  }
  
static public class Default extends PreModule {
  // Production: sig("Default",[arg("org.rascalmpl.ast.Header","header")])

  
     private final org.rascalmpl.ast.Header header;
  

  
public Default(INode node , org.rascalmpl.ast.Header header) {
  super(node);
  
    this.header = header;
  
}


  @Override
  public boolean isDefault() { 
    return true; 
  }

  @Override
  public <T> T accept(IASTVisitor<T> visitor) {
    return visitor.visitPreModuleDefault(this);
  }
  
  
     @Override
     public org.rascalmpl.ast.Header getHeader() {
        return this.header;
     }
     
     @Override
     public boolean hasHeader() {
        return true;
     }
  	
}



}
