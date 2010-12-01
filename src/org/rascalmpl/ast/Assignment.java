
package org.rascalmpl.ast;


import org.eclipse.imp.pdb.facts.INode;


public abstract class Assignment extends AbstractAST {
  public Assignment(INode node) {
    super(node);
  }
  


static public class Ambiguity extends Assignment {
  private final java.util.List<org.rascalmpl.ast.Assignment> alternatives;

  public Ambiguity(INode node, java.util.List<org.rascalmpl.ast.Assignment> alternatives) {
    super(node);
    this.alternatives = java.util.Collections.unmodifiableList(alternatives);
  }

  public java.util.List<org.rascalmpl.ast.Assignment> getAlternatives() {
   return alternatives;
  }

  public <T> T accept(IASTVisitor<T> v) {
	return v.visitAssignmentAmbiguity(this);
  }
}





  public boolean isAddition() {
    return false;
  }
  
static public class Addition extends Assignment {
  // Production: sig("Addition",[])

  

  
public Addition(INode node ) {
  super(node);
  
}


  @Override
  public boolean isAddition() { 
    return true; 
  }

  @Override
  public <T> T accept(IASTVisitor<T> visitor) {
    return visitor.visitAssignmentAddition(this);
  }
  
  	
}


  public boolean isIfDefined() {
    return false;
  }
  
static public class IfDefined extends Assignment {
  // Production: sig("IfDefined",[])

  

  
public IfDefined(INode node ) {
  super(node);
  
}


  @Override
  public boolean isIfDefined() { 
    return true; 
  }

  @Override
  public <T> T accept(IASTVisitor<T> visitor) {
    return visitor.visitAssignmentIfDefined(this);
  }
  
  	
}


  public boolean isDivision() {
    return false;
  }
  
static public class Division extends Assignment {
  // Production: sig("Division",[])

  

  
public Division(INode node ) {
  super(node);
  
}


  @Override
  public boolean isDivision() { 
    return true; 
  }

  @Override
  public <T> T accept(IASTVisitor<T> visitor) {
    return visitor.visitAssignmentDivision(this);
  }
  
  	
}


  public boolean isProduct() {
    return false;
  }
  
static public class Product extends Assignment {
  // Production: sig("Product",[])

  

  
public Product(INode node ) {
  super(node);
  
}


  @Override
  public boolean isProduct() { 
    return true; 
  }

  @Override
  public <T> T accept(IASTVisitor<T> visitor) {
    return visitor.visitAssignmentProduct(this);
  }
  
  	
}


  public boolean isIntersection() {
    return false;
  }
  
static public class Intersection extends Assignment {
  // Production: sig("Intersection",[])

  

  
public Intersection(INode node ) {
  super(node);
  
}


  @Override
  public boolean isIntersection() { 
    return true; 
  }

  @Override
  public <T> T accept(IASTVisitor<T> visitor) {
    return visitor.visitAssignmentIntersection(this);
  }
  
  	
}


  public boolean isSubtraction() {
    return false;
  }
  
static public class Subtraction extends Assignment {
  // Production: sig("Subtraction",[])

  

  
public Subtraction(INode node ) {
  super(node);
  
}


  @Override
  public boolean isSubtraction() { 
    return true; 
  }

  @Override
  public <T> T accept(IASTVisitor<T> visitor) {
    return visitor.visitAssignmentSubtraction(this);
  }
  
  	
}


  public boolean isDefault() {
    return false;
  }
  
static public class Default extends Assignment {
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
    return visitor.visitAssignmentDefault(this);
  }
  
  	
}



}
