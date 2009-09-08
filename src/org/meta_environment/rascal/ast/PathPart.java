package org.meta_environment.rascal.ast; 
import org.eclipse.imp.pdb.facts.INode; 
public abstract class PathPart extends AbstractAST { 
  public org.meta_environment.rascal.ast.PrePathChars getPre() { throw new UnsupportedOperationException(); }
	public org.meta_environment.rascal.ast.Expression getExpression() { throw new UnsupportedOperationException(); }
	public org.meta_environment.rascal.ast.PathTail getTail() { throw new UnsupportedOperationException(); }
public boolean hasPre() { return false; }
	public boolean hasExpression() { return false; }
	public boolean hasTail() { return false; }
public boolean isInterpolated() { return false; }
static public class Interpolated extends PathPart {
/** pre:PrePathChars expression:Expression tail:PathTail -> PathPart {cons("Interpolated")} */
	private Interpolated() {
		super();
	}
	public Interpolated(INode node, org.meta_environment.rascal.ast.PrePathChars pre, org.meta_environment.rascal.ast.Expression expression, org.meta_environment.rascal.ast.PathTail tail) {
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

private org.meta_environment.rascal.ast.PrePathChars pre;
	public org.meta_environment.rascal.ast.PrePathChars getPre() { return pre; }
	private void $setPre(org.meta_environment.rascal.ast.PrePathChars x) { this.pre = x; }
	public Interpolated setPre(org.meta_environment.rascal.ast.PrePathChars x) { 
		Interpolated z = new Interpolated();
 		z.$setPre(x);
		return z;
	}
	private org.meta_environment.rascal.ast.Expression expression;
	public org.meta_environment.rascal.ast.Expression getExpression() { return expression; }
	private void $setExpression(org.meta_environment.rascal.ast.Expression x) { this.expression = x; }
	public Interpolated setExpression(org.meta_environment.rascal.ast.Expression x) { 
		Interpolated z = new Interpolated();
 		z.$setExpression(x);
		return z;
	}
	private org.meta_environment.rascal.ast.PathTail tail;
	public org.meta_environment.rascal.ast.PathTail getTail() { return tail; }
	private void $setTail(org.meta_environment.rascal.ast.PathTail x) { this.tail = x; }
	public Interpolated setTail(org.meta_environment.rascal.ast.PathTail x) { 
		Interpolated z = new Interpolated();
 		z.$setTail(x);
		return z;
	}	
}
static public class Ambiguity extends PathPart {
  private final java.util.List<org.meta_environment.rascal.ast.PathPart> alternatives;
  public Ambiguity(INode node, java.util.List<org.meta_environment.rascal.ast.PathPart> alternatives) {
	this.alternatives = java.util.Collections.unmodifiableList(alternatives);
         this.node = node;
  }
  public java.util.List<org.meta_environment.rascal.ast.PathPart> getAlternatives() {
	return alternatives;
  }
  
  public <T> T accept(IASTVisitor<T> v) {
     return v.visitPathPartAmbiguity(this);
  }
} 
public org.meta_environment.rascal.ast.PathChars getPathChars() { throw new UnsupportedOperationException(); }
public boolean hasPathChars() { return false; }
public boolean isNonInterpolated() { return false; }
static public class NonInterpolated extends PathPart {
/** pathChars:PathChars -> PathPart {cons("NonInterpolated")} */
	private NonInterpolated() {
		super();
	}
	public NonInterpolated(INode node, org.meta_environment.rascal.ast.PathChars pathChars) {
		this.node = node;
		this.pathChars = pathChars;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitPathPartNonInterpolated(this);
	}

	public boolean isNonInterpolated() { return true; }

	public boolean hasPathChars() { return true; }

private org.meta_environment.rascal.ast.PathChars pathChars;
	public org.meta_environment.rascal.ast.PathChars getPathChars() { return pathChars; }
	private void $setPathChars(org.meta_environment.rascal.ast.PathChars x) { this.pathChars = x; }
	public NonInterpolated setPathChars(org.meta_environment.rascal.ast.PathChars x) { 
		NonInterpolated z = new NonInterpolated();
 		z.$setPathChars(x);
		return z;
	}	
}
 public abstract <T> T accept(IASTVisitor<T> visitor);
}