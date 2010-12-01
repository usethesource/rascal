
package org.rascalmpl.ast;


import org.eclipse.imp.pdb.facts.INode;


public abstract class Visibility extends AbstractAST {
  public Visibility(INode node) {
    super(node);
  }
  


static public class Ambiguity extends Visibility {
  private final java.util.List<org.rascalmpl.ast.Visibility> alternatives;

  public Ambiguity(INode node, java.util.List<org.rascalmpl.ast.Visibility> alternatives) {
    super(node);
    this.alternatives = java.util.Collections.unmodifiableList(alternatives);
  }

  public java.util.List<org.rascalmpl.ast.Visibility> getAlternatives() {
   return alternatives;
  }

  public <T> T accept(IASTVisitor<T> v) {
	return v.visitVisibilityAmbiguity(this);
  }
}





  public boolean isPublic() {
    return false;
  }
  
static public class Public extends Visibility {
  // Production: sig("Public",[])

  

  
public Public(INode node ) {
  super(node);
  
}


  @Override
  public boolean isPublic() { 
    return true; 
  }

  @Override
  public <T> T accept(IASTVisitor<T> visitor) {
    return visitor.visitVisibilityPublic(this);
  }
  
  	
}


  public boolean isDefault() {
    return false;
  }
  
static public class Default extends Visibility {
  // Production: sig("Default",[])

  

  
public Default(INode node ) {
  super(node);
  
}


  @Override
  public boolean isDefault() { 
    return true; 
  }

  @Override
  public <T> T accept(IASTVisitor<T> visitor) {
    return visitor.visitVisibilityDefault(this);
  }
  
  	
}


  public boolean isPrivate() {
    return false;
  }
  
static public class Private extends Visibility {
  // Production: sig("Private",[])

  

  
public Private(INode node ) {
  super(node);
  
}


  @Override
  public boolean isPrivate() { 
    return true; 
  }

  @Override
  public <T> T accept(IASTVisitor<T> visitor) {
    return visitor.visitVisibilityPrivate(this);
  }
  
  	
}



}
