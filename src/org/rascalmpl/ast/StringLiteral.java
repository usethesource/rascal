package org.rascalmpl.ast; 
import org.eclipse.imp.pdb.facts.INode; 
public abstract class StringLiteral extends AbstractAST { 
  public org.rascalmpl.ast.PreStringChars getPre() { throw new UnsupportedOperationException(); } public org.rascalmpl.ast.Expression getExpression() { throw new UnsupportedOperationException(); } public org.rascalmpl.ast.StringTail getTail() { throw new UnsupportedOperationException(); } public boolean hasPre() { return false; } public boolean hasExpression() { return false; } public boolean hasTail() { return false; } public boolean isInterpolated() { return false; }
static public class Interpolated extends StringLiteral {
/** pre:PreStringChars expression:Expression tail:StringTail -> StringLiteral {cons("Interpolated")} */
	public Interpolated(INode node, org.rascalmpl.ast.PreStringChars pre, org.rascalmpl.ast.Expression expression, org.rascalmpl.ast.StringTail tail) {
		this.node = node;
		this.pre = pre;
		this.expression = expression;
		this.tail = tail;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitStringLiteralInterpolated(this);
	}

	public boolean isInterpolated() { return true; }

	public boolean hasPre() { return true; }
	public boolean hasExpression() { return true; }
	public boolean hasTail() { return true; }

private final org.rascalmpl.ast.PreStringChars pre;
	public org.rascalmpl.ast.PreStringChars getPre() { return pre; }
	private final org.rascalmpl.ast.Expression expression;
	public org.rascalmpl.ast.Expression getExpression() { return expression; }
	private final org.rascalmpl.ast.StringTail tail;
	public org.rascalmpl.ast.StringTail getTail() { return tail; }	
}
static public class Ambiguity extends StringLiteral {
  private final java.util.List<org.rascalmpl.ast.StringLiteral> alternatives;
  public Ambiguity(INode node, java.util.List<org.rascalmpl.ast.StringLiteral> alternatives) {
	this.alternatives = java.util.Collections.unmodifiableList(alternatives);
         this.node = node;
  }
  public java.util.List<org.rascalmpl.ast.StringLiteral> getAlternatives() {
	return alternatives;
  }
  
  public <T> T accept(IASTVisitor<T> v) {
     return v.visitStringLiteralAmbiguity(this);
  }
} public org.rascalmpl.ast.StringTemplate getTemplate() { throw new UnsupportedOperationException(); } public boolean hasTemplate() { return false; } public boolean isTemplate() { return false; }
static public class Template extends StringLiteral {
/** pre:PreStringChars template:StringTemplate tail:StringTail -> StringLiteral {cons("Template")} */
	public Template(INode node, org.rascalmpl.ast.PreStringChars pre, org.rascalmpl.ast.StringTemplate template, org.rascalmpl.ast.StringTail tail) {
		this.node = node;
		this.pre = pre;
		this.template = template;
		this.tail = tail;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitStringLiteralTemplate(this);
	}

	public boolean isTemplate() { return true; }

	public boolean hasPre() { return true; }
	public boolean hasTemplate() { return true; }
	public boolean hasTail() { return true; }

private final org.rascalmpl.ast.PreStringChars pre;
	public org.rascalmpl.ast.PreStringChars getPre() { return pre; }
	private final org.rascalmpl.ast.StringTemplate template;
	public org.rascalmpl.ast.StringTemplate getTemplate() { return template; }
	private final org.rascalmpl.ast.StringTail tail;
	public org.rascalmpl.ast.StringTail getTail() { return tail; }	
} public abstract <T> T accept(IASTVisitor<T> visitor); public org.rascalmpl.ast.StringConstant getConstant() { throw new UnsupportedOperationException(); }
public boolean hasConstant() { return false; }
public boolean isNonInterpolated() { return false; }
static public class NonInterpolated extends StringLiteral {
/** constant:StringConstant -> StringLiteral {cons("NonInterpolated")} */
	public NonInterpolated(INode node, org.rascalmpl.ast.StringConstant constant) {
		this.node = node;
		this.constant = constant;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitStringLiteralNonInterpolated(this);
	}

	public boolean isNonInterpolated() { return true; }

	public boolean hasConstant() { return true; }

private final org.rascalmpl.ast.StringConstant constant;
	public org.rascalmpl.ast.StringConstant getConstant() { return constant; }	
}
}