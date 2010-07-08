package org.rascalmpl.ast; 
import org.eclipse.imp.pdb.facts.INode; 
public abstract class Replacement extends AbstractAST { 
  public org.rascalmpl.ast.Expression getReplacementExpression() { throw new UnsupportedOperationException(); } public boolean hasReplacementExpression() { return false; } public boolean isUnconditional() { return false; }
static public class Unconditional extends Replacement {
/** replacementExpression:Expression -> Replacement {cons("Unconditional")} */
	public Unconditional(INode node, org.rascalmpl.ast.Expression replacementExpression) {
		this.node = node;
		this.replacementExpression = replacementExpression;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitReplacementUnconditional(this);
	}

	public boolean isUnconditional() { return true; }

	public boolean hasReplacementExpression() { return true; }

private final org.rascalmpl.ast.Expression replacementExpression;
	public org.rascalmpl.ast.Expression getReplacementExpression() { return replacementExpression; }	
}
static public class Ambiguity extends Replacement {
  private final java.util.List<org.rascalmpl.ast.Replacement> alternatives;
  public Ambiguity(INode node, java.util.List<org.rascalmpl.ast.Replacement> alternatives) {
	this.alternatives = java.util.Collections.unmodifiableList(alternatives);
         this.node = node;
  }
  public java.util.List<org.rascalmpl.ast.Replacement> getAlternatives() {
	return alternatives;
  }
  
  public <T> T accept(IASTVisitor<T> v) {
     return v.visitReplacementAmbiguity(this);
  }
} public java.util.List<org.rascalmpl.ast.Expression> getConditions() { throw new UnsupportedOperationException(); } public boolean hasConditions() { return false; }
public boolean isConditional() { return false; }
static public class Conditional extends Replacement {
/** replacementExpression:Expression "when" conditions:{Expression ","}+ -> Replacement {cons("Conditional")} */
	public Conditional(INode node, org.rascalmpl.ast.Expression replacementExpression, java.util.List<org.rascalmpl.ast.Expression> conditions) {
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

private final org.rascalmpl.ast.Expression replacementExpression;
	public org.rascalmpl.ast.Expression getReplacementExpression() { return replacementExpression; }
	private final java.util.List<org.rascalmpl.ast.Expression> conditions;
	public java.util.List<org.rascalmpl.ast.Expression> getConditions() { return conditions; }	
}
 public abstract <T> T accept(IASTVisitor<T> visitor);
}