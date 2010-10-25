package org.rascalmpl.ast; 
import org.eclipse.imp.pdb.facts.INode; 
public abstract class PathPart extends AbstractAST { 
  public org.rascalmpl.ast.PrePathChars getPre() { throw new UnsupportedOperationException(); }
	public org.rascalmpl.ast.Expression getExpression() { throw new UnsupportedOperationException(); }
	public org.rascalmpl.ast.PathTail getTail() { throw new UnsupportedOperationException(); }
public boolean hasPre() { return false; }
	public boolean hasExpression() { return false; }
	public boolean hasTail() { return false; }
public boolean isInterpolated() { return false; }
static public class Interpolated extends PathPart {
/** pre:PrePathChars expression:Expression tail:PathTail -> PathPart {cons("Interpolated")} */
	protected Interpolated(INode node, org.rascalmpl.ast.PrePathChars pre, org.rascalmpl.ast.Expression expression, org.rascalmpl.ast.PathTail tail) {
		this.node = node;
		this.pre = pre;
		this.expression = expression;
		this.tail = tail;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitPathPartInterpolated(this);
	}

	public boolean isInterpolated() { return true; }

	public boolean hasPre() { return true; }
	public boolean hasExpression() { return true; }
	public boolean hasTail() { return true; }

private final org.rascalmpl.ast.PrePathChars pre;
	public org.rascalmpl.ast.PrePathChars getPre() { return pre; }
	private final org.rascalmpl.ast.Expression expression;
	public org.rascalmpl.ast.Expression getExpression() { return expression; }
	private final org.rascalmpl.ast.PathTail tail;
	public org.rascalmpl.ast.PathTail getTail() { return tail; }	
}
static public class Ambiguity extends PathPart {
  private final java.util.List<org.rascalmpl.ast.PathPart> alternatives;
  protected Ambiguity(INode node, java.util.List<org.rascalmpl.ast.PathPart> alternatives) {
	this.alternatives = java.util.Collections.unmodifiableList(alternatives);
         this.node = node;
  }
  public java.util.List<org.rascalmpl.ast.PathPart> getAlternatives() {
	return alternatives;
  }
  
  public <T> T accept(IASTVisitor<T> v) {
     return v.visitPathPartAmbiguity(this);
  }
} 
public org.rascalmpl.ast.PathChars getPathChars() { throw new UnsupportedOperationException(); }
public boolean hasPathChars() { return false; }
public boolean isNonInterpolated() { return false; }
static public class NonInterpolated extends PathPart {
/** pathChars:PathChars -> PathPart {cons("NonInterpolated")} */
	protected NonInterpolated(INode node, org.rascalmpl.ast.PathChars pathChars) {
		this.node = node;
		this.pathChars = pathChars;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitPathPartNonInterpolated(this);
	}

	public boolean isNonInterpolated() { return true; }

	public boolean hasPathChars() { return true; }

private final org.rascalmpl.ast.PathChars pathChars;
	public org.rascalmpl.ast.PathChars getPathChars() { return pathChars; }	
}
 public abstract <T> T accept(IASTVisitor<T> visitor);
}