package org.meta_environment.rascal.ast; 
import org.eclipse.imp.pdb.facts.INode; 
public abstract class Test extends AbstractAST { 
  public org.meta_environment.rascal.ast.Tags getTags() { throw new UnsupportedOperationException(); } public org.meta_environment.rascal.ast.Expression getExpression() { throw new UnsupportedOperationException(); } public boolean hasTags() { return false; } public boolean hasExpression() { return false; } public boolean isUnlabeled() { return false; }
static public class Unlabeled extends Test {
/** tags:Tags "test" expression:Expression -> Test {cons("Unlabeled")} */
	public Unlabeled(INode node, org.meta_environment.rascal.ast.Tags tags, org.meta_environment.rascal.ast.Expression expression) {
		this.node = node;
		this.tags = tags;
		this.expression = expression;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitTestUnlabeled(this);
	}

	public boolean isUnlabeled() { return true; }

	public boolean hasTags() { return true; }
	public boolean hasExpression() { return true; }

private final org.meta_environment.rascal.ast.Tags tags;
	public org.meta_environment.rascal.ast.Tags getTags() { return tags; }
	private final org.meta_environment.rascal.ast.Expression expression;
	public org.meta_environment.rascal.ast.Expression getExpression() { return expression; }	
}
static public class Ambiguity extends Test {
  private final java.util.List<org.meta_environment.rascal.ast.Test> alternatives;
  public Ambiguity(INode node, java.util.List<org.meta_environment.rascal.ast.Test> alternatives) {
	this.alternatives = java.util.Collections.unmodifiableList(alternatives);
         this.node = node;
  }
  public java.util.List<org.meta_environment.rascal.ast.Test> getAlternatives() {
	return alternatives;
  }
  
  public <T> T accept(IASTVisitor<T> v) {
     return v.visitTestAmbiguity(this);
  }
} public org.meta_environment.rascal.ast.StringLiteral getLabeled() { throw new UnsupportedOperationException(); } public boolean hasLabeled() { return false; }
public boolean isLabeled() { return false; }
static public class Labeled extends Test {
/** tags:Tags "test" expression:Expression ":" labeled:StringLiteral -> Test {cons("Labeled")} */
	public Labeled(INode node, org.meta_environment.rascal.ast.Tags tags, org.meta_environment.rascal.ast.Expression expression, org.meta_environment.rascal.ast.StringLiteral labeled) {
		this.node = node;
		this.tags = tags;
		this.expression = expression;
		this.labeled = labeled;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitTestLabeled(this);
	}

	public boolean isLabeled() { return true; }

	public boolean hasTags() { return true; }
	public boolean hasExpression() { return true; }
	public boolean hasLabeled() { return true; }

private final org.meta_environment.rascal.ast.Tags tags;
	public org.meta_environment.rascal.ast.Tags getTags() { return tags; }
	private final org.meta_environment.rascal.ast.Expression expression;
	public org.meta_environment.rascal.ast.Expression getExpression() { return expression; }
	private final org.meta_environment.rascal.ast.StringLiteral labeled;
	public org.meta_environment.rascal.ast.StringLiteral getLabeled() { return labeled; }	
}
 public abstract <T> T accept(IASTVisitor<T> visitor);
}