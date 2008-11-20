package org.meta_environment.rascal.ast; 
import org.eclipse.imp.pdb.facts.ITree; 
public abstract class ValueProducer extends AbstractAST { 
  public org.meta_environment.rascal.ast.Expression getPattern() { throw new UnsupportedOperationException(); } public org.meta_environment.rascal.ast.Expression getExpression() { throw new UnsupportedOperationException(); } public boolean hasPattern() { return false; } public boolean hasExpression() { return false; } public boolean isDefaultStrategy() { return false; }
static public class DefaultStrategy extends ValueProducer {
/* pattern:Expression ":" expression:Expression -> ValueProducer {cons("DefaultStrategy")} */
	private DefaultStrategy() { }
	/*package*/ DefaultStrategy(ITree tree, org.meta_environment.rascal.ast.Expression pattern, org.meta_environment.rascal.ast.Expression expression) {
		this.tree = tree;
		this.pattern = pattern;
		this.expression = expression;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitValueProducerDefaultStrategy(this);
	}

	public boolean isDefaultStrategy() { return true; }

	public boolean hasPattern() { return true; }
	public boolean hasExpression() { return true; }

private org.meta_environment.rascal.ast.Expression pattern;
	public org.meta_environment.rascal.ast.Expression getPattern() { return pattern; }
	private void $setPattern(org.meta_environment.rascal.ast.Expression x) { this.pattern = x; }
	public DefaultStrategy setPattern(org.meta_environment.rascal.ast.Expression x) { 
		DefaultStrategy z = new DefaultStrategy();
 		z.$setPattern(x);
		return z;
	}
	private org.meta_environment.rascal.ast.Expression expression;
	public org.meta_environment.rascal.ast.Expression getExpression() { return expression; }
	private void $setExpression(org.meta_environment.rascal.ast.Expression x) { this.expression = x; }
	public DefaultStrategy setExpression(org.meta_environment.rascal.ast.Expression x) { 
		DefaultStrategy z = new DefaultStrategy();
 		z.$setExpression(x);
		return z;
	}	
}
static public class Ambiguity extends ValueProducer {
  private final java.util.List<org.meta_environment.rascal.ast.ValueProducer> alternatives;
  public Ambiguity(java.util.List<org.meta_environment.rascal.ast.ValueProducer> alternatives) {
	this.alternatives = java.util.Collections.unmodifiableList(alternatives);
  }
  public java.util.List<org.meta_environment.rascal.ast.ValueProducer> getAlternatives() {
	return alternatives;
  }
  
  public <T> T accept(IASTVisitor<T> v) {
     return v.visitValueProducerAmbiguity(this);
  }
} 
public org.meta_environment.rascal.ast.Strategy getStrategy() { throw new UnsupportedOperationException(); } public boolean hasStrategy() { return false; } public boolean isGivenStrategy() { return false; }
static public class GivenStrategy extends ValueProducer {
/* strategy:Strategy pattern:Expression ":" expression:Expression -> ValueProducer {cons("GivenStrategy")} */
	private GivenStrategy() { }
	/*package*/ GivenStrategy(ITree tree, org.meta_environment.rascal.ast.Strategy strategy, org.meta_environment.rascal.ast.Expression pattern, org.meta_environment.rascal.ast.Expression expression) {
		this.tree = tree;
		this.strategy = strategy;
		this.pattern = pattern;
		this.expression = expression;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitValueProducerGivenStrategy(this);
	}

	public boolean isGivenStrategy() { return true; }

	public boolean hasStrategy() { return true; }
	public boolean hasPattern() { return true; }
	public boolean hasExpression() { return true; }

private org.meta_environment.rascal.ast.Strategy strategy;
	public org.meta_environment.rascal.ast.Strategy getStrategy() { return strategy; }
	private void $setStrategy(org.meta_environment.rascal.ast.Strategy x) { this.strategy = x; }
	public GivenStrategy setStrategy(org.meta_environment.rascal.ast.Strategy x) { 
		GivenStrategy z = new GivenStrategy();
 		z.$setStrategy(x);
		return z;
	}
	private org.meta_environment.rascal.ast.Expression pattern;
	public org.meta_environment.rascal.ast.Expression getPattern() { return pattern; }
	private void $setPattern(org.meta_environment.rascal.ast.Expression x) { this.pattern = x; }
	public GivenStrategy setPattern(org.meta_environment.rascal.ast.Expression x) { 
		GivenStrategy z = new GivenStrategy();
 		z.$setPattern(x);
		return z;
	}
	private org.meta_environment.rascal.ast.Expression expression;
	public org.meta_environment.rascal.ast.Expression getExpression() { return expression; }
	private void $setExpression(org.meta_environment.rascal.ast.Expression x) { this.expression = x; }
	public GivenStrategy setExpression(org.meta_environment.rascal.ast.Expression x) { 
		GivenStrategy z = new GivenStrategy();
 		z.$setExpression(x);
		return z;
	}	
}
 public abstract <T> T accept(IASTVisitor<T> visitor);
}