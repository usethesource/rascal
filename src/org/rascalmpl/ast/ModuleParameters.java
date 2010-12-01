
package org.rascalmpl.ast;


import org.eclipse.imp.pdb.facts.INode;


public abstract class ModuleParameters extends AbstractAST {
  public ModuleParameters(INode node) {
    super(node);
  }
  

  public boolean hasParameters() {
    return false;
  }

  public java.util.List<org.rascalmpl.ast.TypeVar> getParameters() {
    throw new UnsupportedOperationException();
  }


static public class Ambiguity extends ModuleParameters {
  private final java.util.List<org.rascalmpl.ast.ModuleParameters> alternatives;

  public Ambiguity(INode node, java.util.List<org.rascalmpl.ast.ModuleParameters> alternatives) {
    super(node);
    this.alternatives = java.util.Collections.unmodifiableList(alternatives);
  }

  public java.util.List<org.rascalmpl.ast.ModuleParameters> getAlternatives() {
   return alternatives;
  }

  public <T> T accept(IASTVisitor<T> v) {
	return v.visitModuleParametersAmbiguity(this);
  }
}





  public boolean isDefault() {
    return false;
  }
  
static public class Default extends ModuleParameters {
  // Production: sig("Default",[arg("java.util.List\<org.rascalmpl.ast.TypeVar\>","parameters")])

  
     private final java.util.List<org.rascalmpl.ast.TypeVar> parameters;
  

  
public Default(INode node , java.util.List<org.rascalmpl.ast.TypeVar> parameters) {
  super(node);
  
    this.parameters = parameters;
  
}


  @Override
  public boolean isDefault() { 
    return true; 
  }

  @Override
  public <T> T accept(IASTVisitor<T> visitor) {
    return visitor.visitModuleParametersDefault(this);
  }
  
  
     @Override
     public java.util.List<org.rascalmpl.ast.TypeVar> getParameters() {
        return this.parameters;
     }
     
     @Override
     public boolean hasParameters() {
        return true;
     }
  	
}



}
