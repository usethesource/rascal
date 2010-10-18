package org.rascalmpl.ast; 
import org.eclipse.imp.pdb.facts.INode; 
public abstract class Bound extends AbstractAST { 
  public boolean isEmpty() { return false; }
static public class Empty extends Bound {
/**  -> Bound {cons("Empty")} */
	protected Empty(INode node) {
		this.node = node;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitBoundEmpty(this);
	}

	public boolean isEmpty() { return true; }	
}
static public class Ambiguity extends Bound {
  private final java.util.List<org.rascalmpl.ast.Bound> alternatives;
  protected Ambiguity(INode node, java.util.List<org.rascalmpl.ast.Bound> alternatives) {
	this.alternatives = java.util.Collections.unmodifiableList(alternatives);
         this.node = node;
  }
  public java.util.List<org.rascalmpl.ast.Bound> getAlternatives() {
	return alternatives;
  }
  
  public <T> T accept(IASTVisitor<T> v) {
     return v.visitBoundAmbiguity(this);
  }
} 
public org.rascalmpl.ast.Expression getExpression() { throw new UnsupportedOperationException(); }
public boolean hasExpression() { return false; }
public boolean isDefault() { return false; }
static public class Default extends Bound {
/** ";" expression:Expression -> Bound {cons("Default")} */
	protected Default(INode node, org.rascalmpl.ast.Expression expression) {
		this.node = node;
		this.expression = expression;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitBoundDefault(this);
	}

	public boolean isDefault() { return true; }

	public boolean hasExpression() { return true; }

private final org.rascalmpl.ast.Expression expression;
	public org.rascalmpl.ast.Expression getExpression() { return expression; }	
}
 public abstract <T> T accept(IASTVisitor<T> visitor);
}