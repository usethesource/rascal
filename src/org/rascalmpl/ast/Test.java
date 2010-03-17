package org.rascalmpl.ast; 
import org.eclipse.imp.pdb.facts.INode; 
public abstract class Test extends AbstractAST { 
  public org.rascalmpl.ast.Tags getTags() { throw new UnsupportedOperationException(); } public org.rascalmpl.ast.Expression getExpression() { throw new UnsupportedOperationException(); } public boolean hasTags() { return false; } public boolean hasExpression() { return false; } public boolean isUnlabeled() { return false; }
static public class Unlabeled extends Test {
/** tags:Tags "test" expression:Expression -> Test {cons("Unlabeled")} */
	public Unlabeled(INode node, org.rascalmpl.ast.Tags tags, org.rascalmpl.ast.Expression expression) {
		this.node = node;
		this.tags = tags;
		this.expression = expression;
	}
	@Override
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitTestUnlabeled(this);
	}

	@Override
	public boolean isUnlabeled() { return true; }

	@Override
	public boolean hasTags() { return true; }
	@Override
	public boolean hasExpression() { return true; }

private final org.rascalmpl.ast.Tags tags;
	@Override
	public org.rascalmpl.ast.Tags getTags() { return tags; }
	private final org.rascalmpl.ast.Expression expression;
	@Override
	public org.rascalmpl.ast.Expression getExpression() { return expression; }	
}
static public class Ambiguity extends Test {
  private final java.util.List<org.rascalmpl.ast.Test> alternatives;
  public Ambiguity(INode node, java.util.List<org.rascalmpl.ast.Test> alternatives) {
	this.alternatives = java.util.Collections.unmodifiableList(alternatives);
         this.node = node;
  }
  public java.util.List<org.rascalmpl.ast.Test> getAlternatives() {
	return alternatives;
  }
  
  @Override
public <T> T accept(IASTVisitor<T> v) {
     return v.visitTestAmbiguity(this);
  }
} public org.rascalmpl.ast.StringLiteral getLabeled() { throw new UnsupportedOperationException(); } public boolean hasLabeled() { return false; }
public boolean isLabeled() { return false; }
static public class Labeled extends Test {
/** tags:Tags "test" expression:Expression ":" labeled:StringLiteral -> Test {cons("Labeled")} */
	public Labeled(INode node, org.rascalmpl.ast.Tags tags, org.rascalmpl.ast.Expression expression, org.rascalmpl.ast.StringLiteral labeled) {
		this.node = node;
		this.tags = tags;
		this.expression = expression;
		this.labeled = labeled;
	}
	@Override
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitTestLabeled(this);
	}

	@Override
	public boolean isLabeled() { return true; }

	@Override
	public boolean hasTags() { return true; }
	@Override
	public boolean hasExpression() { return true; }
	@Override
	public boolean hasLabeled() { return true; }

private final org.rascalmpl.ast.Tags tags;
	@Override
	public org.rascalmpl.ast.Tags getTags() { return tags; }
	private final org.rascalmpl.ast.Expression expression;
	@Override
	public org.rascalmpl.ast.Expression getExpression() { return expression; }
	private final org.rascalmpl.ast.StringLiteral labeled;
	@Override
	public org.rascalmpl.ast.StringLiteral getLabeled() { return labeled; }	
}
 @Override
public abstract <T> T accept(IASTVisitor<T> visitor);
}