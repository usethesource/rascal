package org.meta_environment.rascal.ast; 
import org.eclipse.imp.pdb.facts.ITree; 
public abstract class Visit extends AbstractAST { 
  public org.meta_environment.rascal.ast.Expression getSubject() { throw new UnsupportedOperationException(); } public java.util.List<org.meta_environment.rascal.ast.Case> getCases() { throw new UnsupportedOperationException(); } public boolean hasSubject() { return false; } public boolean hasCases() { return false; } public boolean isDefaultStrategy() { return false; }
static public class DefaultStrategy extends Visit {
/* "visit" "(" subject:Expression ")" "{" cases:Case+ "}" -> Visit {cons("DefaultStrategy")} */
	private DefaultStrategy() { }
	/*package*/ DefaultStrategy(ITree tree, org.meta_environment.rascal.ast.Expression subject, java.util.List<org.meta_environment.rascal.ast.Case> cases) {
		this.tree = tree;
		this.subject = subject;
		this.cases = cases;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitVisitDefaultStrategy(this);
	}

	public boolean isDefaultStrategy() { return true; }

	public boolean hasSubject() { return true; }
	public boolean hasCases() { return true; }

private org.meta_environment.rascal.ast.Expression subject;
	public org.meta_environment.rascal.ast.Expression getSubject() { return subject; }
	private void $setSubject(org.meta_environment.rascal.ast.Expression x) { this.subject = x; }
	public DefaultStrategy setSubject(org.meta_environment.rascal.ast.Expression x) { 
		DefaultStrategy z = new DefaultStrategy();
 		z.$setSubject(x);
		return z;
	}
	private java.util.List<org.meta_environment.rascal.ast.Case> cases;
	public java.util.List<org.meta_environment.rascal.ast.Case> getCases() { return cases; }
	private void $setCases(java.util.List<org.meta_environment.rascal.ast.Case> x) { this.cases = x; }
	public DefaultStrategy setCases(java.util.List<org.meta_environment.rascal.ast.Case> x) { 
		DefaultStrategy z = new DefaultStrategy();
 		z.$setCases(x);
		return z;
	}	
}
static public class Ambiguity extends Visit {
  private final java.util.List<org.meta_environment.rascal.ast.Visit> alternatives;
  public Ambiguity(java.util.List<org.meta_environment.rascal.ast.Visit> alternatives) {
	this.alternatives = java.util.Collections.unmodifiableList(alternatives);
  }
  public java.util.List<org.meta_environment.rascal.ast.Visit> getAlternatives() {
	return alternatives;
  }
  
  public <T> T accept(IASTVisitor<T> v) {
     return v.visitVisitAmbiguity(this);
  }
} 
public org.meta_environment.rascal.ast.Strategy getStrategy() { throw new UnsupportedOperationException(); } public boolean hasStrategy() { return false; } public boolean isGivenStrategy() { return false; }
static public class GivenStrategy extends Visit {
/* strategy:Strategy "visit" "(" subject:Expression ")" "{" cases:Case+ "}" -> Visit {cons("GivenStrategy")} */
	private GivenStrategy() { }
	/*package*/ GivenStrategy(ITree tree, org.meta_environment.rascal.ast.Strategy strategy, org.meta_environment.rascal.ast.Expression subject, java.util.List<org.meta_environment.rascal.ast.Case> cases) {
		this.tree = tree;
		this.strategy = strategy;
		this.subject = subject;
		this.cases = cases;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitVisitGivenStrategy(this);
	}

	public boolean isGivenStrategy() { return true; }

	public boolean hasStrategy() { return true; }
	public boolean hasSubject() { return true; }
	public boolean hasCases() { return true; }

private org.meta_environment.rascal.ast.Strategy strategy;
	public org.meta_environment.rascal.ast.Strategy getStrategy() { return strategy; }
	private void $setStrategy(org.meta_environment.rascal.ast.Strategy x) { this.strategy = x; }
	public GivenStrategy setStrategy(org.meta_environment.rascal.ast.Strategy x) { 
		GivenStrategy z = new GivenStrategy();
 		z.$setStrategy(x);
		return z;
	}
	private org.meta_environment.rascal.ast.Expression subject;
	public org.meta_environment.rascal.ast.Expression getSubject() { return subject; }
	private void $setSubject(org.meta_environment.rascal.ast.Expression x) { this.subject = x; }
	public GivenStrategy setSubject(org.meta_environment.rascal.ast.Expression x) { 
		GivenStrategy z = new GivenStrategy();
 		z.$setSubject(x);
		return z;
	}
	private java.util.List<org.meta_environment.rascal.ast.Case> cases;
	public java.util.List<org.meta_environment.rascal.ast.Case> getCases() { return cases; }
	private void $setCases(java.util.List<org.meta_environment.rascal.ast.Case> x) { this.cases = x; }
	public GivenStrategy setCases(java.util.List<org.meta_environment.rascal.ast.Case> x) { 
		GivenStrategy z = new GivenStrategy();
 		z.$setCases(x);
		return z;
	}	
}
 public abstract <T> T accept(IASTVisitor<T> visitor);
}