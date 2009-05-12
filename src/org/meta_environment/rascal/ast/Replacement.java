package org.meta_environment.rascal.ast; 
import org.eclipse.imp.pdb.facts.INode; 
public abstract class Replacement extends AbstractAST { 
  public org.meta_environment.rascal.ast.Expression getReplacementExpression() { throw new UnsupportedOperationException(); } public boolean hasReplacementExpression() { return false; } public boolean isUnconditional() { return false; }
static public class Unconditional extends Replacement {
/* replacementExpression:Expression -> Replacement {cons("Unconditional")} */
	private Unconditional() {
		super();
	}
	/*package*/ Unconditional(INode node, org.meta_environment.rascal.ast.Expression replacementExpression) {
		this.node = node;
		this.replacementExpression = replacementExpression;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitReplacementUnconditional(this);
	}

	public boolean isUnconditional() { return true; }

	public boolean hasReplacementExpression() { return true; }

private org.meta_environment.rascal.ast.Expression replacementExpression;
	public org.meta_environment.rascal.ast.Expression getReplacementExpression() { return replacementExpression; }
	private void $setReplacementExpression(org.meta_environment.rascal.ast.Expression x) { this.replacementExpression = x; }
	public Unconditional setReplacementExpression(org.meta_environment.rascal.ast.Expression x) { 
		Unconditional z = new Unconditional();
 		z.$setReplacementExpression(x);
		return z;
	}	
}
static public class Ambiguity extends Replacement {
  private final java.util.List<org.meta_environment.rascal.ast.Replacement> alternatives;
  public Ambiguity(INode node, java.util.List<org.meta_environment.rascal.ast.Replacement> alternatives) {
	this.alternatives = java.util.Collections.unmodifiableList(alternatives);
         this.node = node;
  }
  public java.util.List<org.meta_environment.rascal.ast.Replacement> getAlternatives() {
	return alternatives;
  }
  
  public <T> T accept(IASTVisitor<T> v) {
     return v.visitReplacementAmbiguity(this);
  }
} public java.util.List<org.meta_environment.rascal.ast.Expression> getConditions() { throw new UnsupportedOperationException(); } public boolean hasConditions() { return false; }
public boolean isConditional() { return false; }
static public class Conditional extends Replacement {
/* replacementExpression:Expression "when" conditions:{Expression ","}+ -> Replacement {cons("Conditional")} */
	private Conditional() {
		super();
	}
	/*package*/ Conditional(INode node, org.meta_environment.rascal.ast.Expression replacementExpression, java.util.List<org.meta_environment.rascal.ast.Expression> conditions) {
		this.node = node;
		this.replacementExpression = replacementExpression;
		this.conditions = conditions;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitReplacementConditional(this);
	}

	public boolean isConditional() { return true; }

	public boolean hasReplacementExpression() { return true; }
	public boolean hasConditions() { return true; }

private org.meta_environment.rascal.ast.Expression replacementExpression;
	public org.meta_environment.rascal.ast.Expression getReplacementExpression() { return replacementExpression; }
	private void $setReplacementExpression(org.meta_environment.rascal.ast.Expression x) { this.replacementExpression = x; }
	public Conditional setReplacementExpression(org.meta_environment.rascal.ast.Expression x) { 
		Conditional z = new Conditional();
 		z.$setReplacementExpression(x);
		return z;
	}
	private java.util.List<org.meta_environment.rascal.ast.Expression> conditions;
	public java.util.List<org.meta_environment.rascal.ast.Expression> getConditions() { return conditions; }
	private void $setConditions(java.util.List<org.meta_environment.rascal.ast.Expression> x) { this.conditions = x; }
	public Conditional setConditions(java.util.List<org.meta_environment.rascal.ast.Expression> x) { 
		Conditional z = new Conditional();
 		z.$setConditions(x);
		return z;
	}	
}
 public abstract <T> T accept(IASTVisitor<T> visitor);
}