
package org.rascalmpl.ast;


import org.eclipse.imp.pdb.facts.INode;


public abstract class Visit extends AbstractAST {
  public Visit(INode node) {
    super(node);
  }
  

  public boolean hasSubject() {
    return false;
  }

  public org.rascalmpl.ast.Expression getSubject() {
    throw new UnsupportedOperationException();
  }

  public boolean hasCases() {
    return false;
  }

  public java.util.List<org.rascalmpl.ast.Case> getCases() {
    throw new UnsupportedOperationException();
  }

  public boolean hasStrategy() {
    return false;
  }

  public org.rascalmpl.ast.Strategy getStrategy() {
    throw new UnsupportedOperationException();
  }


static public class Ambiguity extends Visit {
  private final java.util.List<org.rascalmpl.ast.Visit> alternatives;

  public Ambiguity(INode node, java.util.List<org.rascalmpl.ast.Visit> alternatives) {
    super(node);
    this.alternatives = java.util.Collections.unmodifiableList(alternatives);
  }

  public java.util.List<org.rascalmpl.ast.Visit> getAlternatives() {
   return alternatives;
  }

  public <T> T accept(IASTVisitor<T> v) {
	return v.visitVisitAmbiguity(this);
  }
}





  public boolean isDefaultStrategy() {
    return false;
  }
  
static public class DefaultStrategy extends Visit {
  // Production: sig("DefaultStrategy",[arg("org.rascalmpl.ast.Expression","subject"),arg("java.util.List\<org.rascalmpl.ast.Case\>","cases")])

  
     private final org.rascalmpl.ast.Expression subject;
  
     private final java.util.List<org.rascalmpl.ast.Case> cases;
  

  
public DefaultStrategy(INode node , org.rascalmpl.ast.Expression subject,  java.util.List<org.rascalmpl.ast.Case> cases) {
  super(node);
  
    this.subject = subject;
  
    this.cases = cases;
  
}


  @Override
  public boolean isDefaultStrategy() { 
    return true; 
  }

  @Override
  public <T> T accept(IASTVisitor<T> visitor) {
    return visitor.visitVisitDefaultStrategy(this);
  }
  
  
     @Override
     public org.rascalmpl.ast.Expression getSubject() {
        return this.subject;
     }
     
     @Override
     public boolean hasSubject() {
        return true;
     }
  
     @Override
     public java.util.List<org.rascalmpl.ast.Case> getCases() {
        return this.cases;
     }
     
     @Override
     public boolean hasCases() {
        return true;
     }
  	
}


  public boolean isGivenStrategy() {
    return false;
  }
  
static public class GivenStrategy extends Visit {
  // Production: sig("GivenStrategy",[arg("org.rascalmpl.ast.Strategy","strategy"),arg("org.rascalmpl.ast.Expression","subject"),arg("java.util.List\<org.rascalmpl.ast.Case\>","cases")])

  
     private final org.rascalmpl.ast.Strategy strategy;
  
     private final org.rascalmpl.ast.Expression subject;
  
     private final java.util.List<org.rascalmpl.ast.Case> cases;
  

  
public GivenStrategy(INode node , org.rascalmpl.ast.Strategy strategy,  org.rascalmpl.ast.Expression subject,  java.util.List<org.rascalmpl.ast.Case> cases) {
  super(node);
  
    this.strategy = strategy;
  
    this.subject = subject;
  
    this.cases = cases;
  
}


  @Override
  public boolean isGivenStrategy() { 
    return true; 
  }

  @Override
  public <T> T accept(IASTVisitor<T> visitor) {
    return visitor.visitVisitGivenStrategy(this);
  }
  
  
     @Override
     public org.rascalmpl.ast.Strategy getStrategy() {
        return this.strategy;
     }
     
     @Override
     public boolean hasStrategy() {
        return true;
     }
  
     @Override
     public org.rascalmpl.ast.Expression getSubject() {
        return this.subject;
     }
     
     @Override
     public boolean hasSubject() {
        return true;
     }
  
     @Override
     public java.util.List<org.rascalmpl.ast.Case> getCases() {
        return this.cases;
     }
     
     @Override
     public boolean hasCases() {
        return true;
     }
  	
}



}
