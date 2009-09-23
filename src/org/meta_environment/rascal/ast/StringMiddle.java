package org.meta_environment.rascal.ast; 
import org.eclipse.imp.pdb.facts.INode; 
public abstract class StringMiddle extends AbstractAST { 
  public org.meta_environment.rascal.ast.MidStringChars getMid() { throw new UnsupportedOperationException(); } public boolean hasMid() { return false; } public boolean isMid() { return false; }
static public class Mid extends StringMiddle {
/** mid:MidStringChars -> StringMiddle {cons("Mid")} */
	public Mid(INode node, org.meta_environment.rascal.ast.MidStringChars mid) {
		this.node = node;
		this.mid = mid;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitStringMiddleMid(this);
	}

	public boolean isMid() { return true; }

	public boolean hasMid() { return true; }

private final org.meta_environment.rascal.ast.MidStringChars mid;
	public org.meta_environment.rascal.ast.MidStringChars getMid() { return mid; }	
}
static public class Ambiguity extends StringMiddle {
  private final java.util.List<org.meta_environment.rascal.ast.StringMiddle> alternatives;
  public Ambiguity(INode node, java.util.List<org.meta_environment.rascal.ast.StringMiddle> alternatives) {
	this.alternatives = java.util.Collections.unmodifiableList(alternatives);
         this.node = node;
  }
  public java.util.List<org.meta_environment.rascal.ast.StringMiddle> getAlternatives() {
	return alternatives;
  }
  
  public <T> T accept(IASTVisitor<T> v) {
     return v.visitStringMiddleAmbiguity(this);
  }
} public org.meta_environment.rascal.ast.Expression getExpression() { throw new UnsupportedOperationException(); } public org.meta_environment.rascal.ast.StringMiddle getTail() { throw new UnsupportedOperationException(); } public boolean hasExpression() { return false; } public boolean hasTail() { return false; } public boolean isInterpolated() { return false; }
static public class Interpolated extends StringMiddle {
/** mid:MidStringChars expression:Expression tail:StringMiddle -> StringMiddle {cons("Interpolated")} */
	public Interpolated(INode node, org.meta_environment.rascal.ast.MidStringChars mid, org.meta_environment.rascal.ast.Expression expression, org.meta_environment.rascal.ast.StringMiddle tail) {
		this.node = node;
		this.mid = mid;
		this.expression = expression;
		this.tail = tail;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitStringMiddleInterpolated(this);
	}

	public boolean isInterpolated() { return true; }

	public boolean hasMid() { return true; }
	public boolean hasExpression() { return true; }
	public boolean hasTail() { return true; }

private final org.meta_environment.rascal.ast.MidStringChars mid;
	public org.meta_environment.rascal.ast.MidStringChars getMid() { return mid; }
	private final org.meta_environment.rascal.ast.Expression expression;
	public org.meta_environment.rascal.ast.Expression getExpression() { return expression; }
	private final org.meta_environment.rascal.ast.StringMiddle tail;
	public org.meta_environment.rascal.ast.StringMiddle getTail() { return tail; }	
} public abstract <T> T accept(IASTVisitor<T> visitor); public org.meta_environment.rascal.ast.StringTemplate getTemplate() { throw new UnsupportedOperationException(); } public boolean hasTemplate() { return false; } public boolean isTemplate() { return false; }
static public class Template extends StringMiddle {
/** mid:MidStringChars template:StringTemplate tail:StringMiddle -> StringMiddle {cons("Template")} */
	public Template(INode node, org.meta_environment.rascal.ast.MidStringChars mid, org.meta_environment.rascal.ast.StringTemplate template, org.meta_environment.rascal.ast.StringMiddle tail) {
		this.node = node;
		this.mid = mid;
		this.template = template;
		this.tail = tail;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitStringMiddleTemplate(this);
	}

	public boolean isTemplate() { return true; }

	public boolean hasMid() { return true; }
	public boolean hasTemplate() { return true; }
	public boolean hasTail() { return true; }

private final org.meta_environment.rascal.ast.MidStringChars mid;
	public org.meta_environment.rascal.ast.MidStringChars getMid() { return mid; }
	private final org.meta_environment.rascal.ast.StringTemplate template;
	public org.meta_environment.rascal.ast.StringTemplate getTemplate() { return template; }
	private final org.meta_environment.rascal.ast.StringMiddle tail;
	public org.meta_environment.rascal.ast.StringMiddle getTail() { return tail; }	
}
}