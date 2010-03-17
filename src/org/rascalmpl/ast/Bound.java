package org.rascalmpl.ast; 
import org.eclipse.imp.pdb.facts.INode; 
public abstract class Bound extends AbstractAST { 
  public boolean isEmpty() { return false; }
static public class Empty extends Bound {
/**  -> Bound {cons("Empty")} */
	public Empty(INode node) {
		this.node = node;
	}
	@Override
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitBoundEmpty(this);
	}

	@Override
	public boolean isEmpty() { return true; }	
}
static public class Ambiguity extends Bound {
  private final java.util.List<org.rascalmpl.ast.Bound> alternatives;
  public Ambiguity(INode node, java.util.List<org.rascalmpl.ast.Bound> alternatives) {
	this.alternatives = java.util.Collections.unmodifiableList(alternatives);
         this.node = node;
  }
  public java.util.List<org.rascalmpl.ast.Bound> getAlternatives() {
	return alternatives;
  }
  
  @Override
public <T> T accept(IASTVisitor<T> v) {
     return v.visitBoundAmbiguity(this);
  }
} 
public org.rascalmpl.ast.Expression getExpression() { throw new UnsupportedOperationException(); }
public boolean hasExpression() { return false; }
public boolean isDefault() { return false; }
static public class Default extends Bound {
/** ";" expression:Expression -> Bound {cons("Default")} */
	public Default(INode node, org.rascalmpl.ast.Expression expression) {
		this.node = node;
		this.expression = expression;
	}
	@Override
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitBoundDefault(this);
	}

	@Override
	public boolean isDefault() { return true; }

	@Override
	public boolean hasExpression() { return true; }

private final org.rascalmpl.ast.Expression expression;
	@Override
	public org.rascalmpl.ast.Expression getExpression() { return expression; }	
}
 @Override
public abstract <T> T accept(IASTVisitor<T> visitor);
}