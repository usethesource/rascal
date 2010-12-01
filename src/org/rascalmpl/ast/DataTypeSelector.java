
package org.rascalmpl.ast;


import org.eclipse.imp.pdb.facts.INode;


public abstract class DataTypeSelector extends AbstractAST {
  public DataTypeSelector(INode node) {
    super(node);
  }
  

  public boolean hasSort() {
    return false;
  }

  public org.rascalmpl.ast.QualifiedName getSort() {
    throw new UnsupportedOperationException();
  }

  public boolean hasProduction() {
    return false;
  }

  public org.rascalmpl.ast.Name getProduction() {
    throw new UnsupportedOperationException();
  }


static public class Ambiguity extends DataTypeSelector {
  private final java.util.List<org.rascalmpl.ast.DataTypeSelector> alternatives;

  public Ambiguity(INode node, java.util.List<org.rascalmpl.ast.DataTypeSelector> alternatives) {
    super(node);
    this.alternatives = java.util.Collections.unmodifiableList(alternatives);
  }

  public java.util.List<org.rascalmpl.ast.DataTypeSelector> getAlternatives() {
   return alternatives;
  }

  public <T> T accept(IASTVisitor<T> v) {
	return v.visitDataTypeSelectorAmbiguity(this);
  }
}





  public boolean isSelector() {
    return false;
  }
  
static public class Selector extends DataTypeSelector {
  // Production: sig("Selector",[arg("org.rascalmpl.ast.QualifiedName","sort"),arg("org.rascalmpl.ast.Name","production")])

  
     private final org.rascalmpl.ast.QualifiedName sort;
  
     private final org.rascalmpl.ast.Name production;
  

  
public Selector(INode node , org.rascalmpl.ast.QualifiedName sort,  org.rascalmpl.ast.Name production) {
  super(node);
  
    this.sort = sort;
  
    this.production = production;
  
}


  @Override
  public boolean isSelector() { 
    return true; 
  }

  @Override
  public <T> T accept(IASTVisitor<T> visitor) {
    return visitor.visitDataTypeSelectorSelector(this);
  }
  
  
     @Override
     public org.rascalmpl.ast.QualifiedName getSort() {
        return this.sort;
     }
     
     @Override
     public boolean hasSort() {
        return true;
     }
  
     @Override
     public org.rascalmpl.ast.Name getProduction() {
        return this.production;
     }
     
     @Override
     public boolean hasProduction() {
        return true;
     }
  	
}



}
