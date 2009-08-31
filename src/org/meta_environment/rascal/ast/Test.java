package org.meta_environment.rascal.ast; 
import org.eclipse.imp.pdb.facts.INode; 
public abstract class Test extends AbstractAST { 
  public org.meta_environment.rascal.ast.Tags getTags() { throw new UnsupportedOperationException(); } public org.meta_environment.rascal.ast.Expression getExpression() { throw new UnsupportedOperationException(); } public boolean hasTags() { return false; } public boolean hasExpression() { return false; } public boolean isUnlabeled() { return false; }
static public class Unlabeled extends Test {
/** tags:Tags "test" expression:Expression -> Test {cons("Unlabeled")} */
	private Unlabeled() {
		super();
	}
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

private org.meta_environment.rascal.ast.Tags tags;
	public org.meta_environment.rascal.ast.Tags getTags() { return tags; }
	private void $setTags(org.meta_environment.rascal.ast.Tags x) { this.tags = x; }
	public Unlabeled setTags(org.meta_environment.rascal.ast.Tags x) { 
		Unlabeled z = new Unlabeled();
 		z.$setTags(x);
		return z;
	}
	private org.meta_environment.rascal.ast.Expression expression;
	public org.meta_environment.rascal.ast.Expression getExpression() { return expression; }
	private void $setExpression(org.meta_environment.rascal.ast.Expression x) { this.expression = x; }
	public Unlabeled setExpression(org.meta_environment.rascal.ast.Expression x) { 
		Unlabeled z = new Unlabeled();
 		z.$setExpression(x);
		return z;
	}	
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
	private Labeled() {
		super();
	}
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

private org.meta_environment.rascal.ast.Tags tags;
	public org.meta_environment.rascal.ast.Tags getTags() { return tags; }
	private void $setTags(org.meta_environment.rascal.ast.Tags x) { this.tags = x; }
	public Labeled setTags(org.meta_environment.rascal.ast.Tags x) { 
		Labeled z = new Labeled();
 		z.$setTags(x);
		return z;
	}
	private org.meta_environment.rascal.ast.Expression expression;
	public org.meta_environment.rascal.ast.Expression getExpression() { return expression; }
	private void $setExpression(org.meta_environment.rascal.ast.Expression x) { this.expression = x; }
	public Labeled setExpression(org.meta_environment.rascal.ast.Expression x) { 
		Labeled z = new Labeled();
 		z.$setExpression(x);
		return z;
	}
	private org.meta_environment.rascal.ast.StringLiteral labeled;
	public org.meta_environment.rascal.ast.StringLiteral getLabeled() { return labeled; }
	private void $setLabeled(org.meta_environment.rascal.ast.StringLiteral x) { this.labeled = x; }
	public Labeled setLabeled(org.meta_environment.rascal.ast.StringLiteral x) { 
		Labeled z = new Labeled();
 		z.$setLabeled(x);
		return z;
	}	
}
 public abstract <T> T accept(IASTVisitor<T> visitor);
}