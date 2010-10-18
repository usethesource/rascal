package org.rascalmpl.ast; 
import org.eclipse.imp.pdb.facts.INode; 
public abstract class Test extends AbstractAST { 
  public org.rascalmpl.ast.Tags getTags() { throw new UnsupportedOperationException(); } public org.rascalmpl.ast.Expression getExpression() { throw new UnsupportedOperationException(); } public boolean hasTags() { return false; } public boolean hasExpression() { return false; } public boolean isUnlabeled() { return false; }
static public class Unlabeled extends Test {
/** tags:Tags "test" expression:Expression -> Test {cons("Unlabeled")} */
	protected Unlabeled(INode node, org.rascalmpl.ast.Tags tags, org.rascalmpl.ast.Expression expression) {
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

private final org.rascalmpl.ast.Tags tags;
	public org.rascalmpl.ast.Tags getTags() { return tags; }
	private final org.rascalmpl.ast.Expression expression;
	public org.rascalmpl.ast.Expression getExpression() { return expression; }	
}
static public class Ambiguity extends Test {
  private final java.util.List<org.rascalmpl.ast.Test> alternatives;
  protected Ambiguity(INode node, java.util.List<org.rascalmpl.ast.Test> alternatives) {
	this.alternatives = java.util.Collections.unmodifiableList(alternatives);
         this.node = node;
  }
  public java.util.List<org.rascalmpl.ast.Test> getAlternatives() {
	return alternatives;
  }
  
  public <T> T accept(IASTVisitor<T> v) {
     return v.visitTestAmbiguity(this);
  }
} public org.rascalmpl.ast.StringLiteral getLabeled() { throw new UnsupportedOperationException(); } public boolean hasLabeled() { return false; }
public boolean isLabeled() { return false; }
static public class Labeled extends Test {
/** tags:Tags "test" expression:Expression ":" labeled:StringLiteral -> Test {cons("Labeled")} */
	protected Labeled(INode node, org.rascalmpl.ast.Tags tags, org.rascalmpl.ast.Expression expression, org.rascalmpl.ast.StringLiteral labeled) {
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

private final org.rascalmpl.ast.Tags tags;
	public org.rascalmpl.ast.Tags getTags() { return tags; }
	private final org.rascalmpl.ast.Expression expression;
	public org.rascalmpl.ast.Expression getExpression() { return expression; }
	private final org.rascalmpl.ast.StringLiteral labeled;
	public org.rascalmpl.ast.StringLiteral getLabeled() { return labeled; }	
}
 public abstract <T> T accept(IASTVisitor<T> visitor);
}