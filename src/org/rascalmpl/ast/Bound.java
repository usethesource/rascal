
package org.rascalmpl.ast;


import org.eclipse.imp.pdb.facts.INode;


public abstract class Bound extends AbstractAST {
  public Bound(INode node) {
    super(node);
  }
  

  public boolean hasExpression() {
    return false;
  }

  public org.rascalmpl.ast.Expression getExpression() {
    throw new UnsupportedOperationException();
  }


static public class Ambiguity extends Bound {
  private final java.util.List<org.rascalmpl.ast.Bound> alternatives;

  public Ambiguity(INode node, java.util.List<org.rascalmpl.ast.Bound> alternatives) {
    super(node);
    this.alternatives = java.util.Collections.unmodifiableList(alternatives);
  }

  public java.util.List<org.rascalmpl.ast.Bound> getAlternatives() {
   return alternatives;
  }

  public <T> T accept(IASTVisitor<T> v) {
	return v.visitBoundAmbiguity(this);
  }
}





  public boolean isEmpty() {
    return false;
  }
  
static public class Empty extends Bound {
  // Production: sig("Empty",[])

  

  
public Empty(INode node ) {
  super(node);
  
}


  @Override
  public boolean isEmpty() { 
    return true; 
  }

  @Override
  public <T> T accept(IASTVisitor<T> visitor) {
    return visitor.visitBoundEmpty(this);
  }
  
  	
}


  public boolean isDefault() {
    return false;
  }
  
static public class Default extends Bound {
  // Production: sig("Default",[arg("org.rascalmpl.ast.Expression","expression")])

  
     private final org.rascalmpl.ast.Expression expression;
  

  
public Default(INode node , org.rascalmpl.ast.Expression expression) {
  super(node);
  
    this.expression = expression;
  
}


  @Override
  public boolean isDefault() { 
    return true; 
  }

  @Override
  public <T> T accept(IASTVisitor<T> visitor) {
    return visitor.visitBoundDefault(this);
  }
  
  
     @Override
     public org.rascalmpl.ast.Expression getExpression() {
        return this.expression;
     }
     
     @Override
     public boolean hasExpression() {
        return true;
     }
  	
}



}
