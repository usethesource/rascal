package org.meta_environment.rascal.ast; 
import org.eclipse.imp.pdb.facts.INode; 
public abstract class StringLiteral extends AbstractAST { 
  public org.meta_environment.rascal.ast.PreStringChars getPre() { throw new UnsupportedOperationException(); } public org.meta_environment.rascal.ast.Expression getExpression() { throw new UnsupportedOperationException(); } public org.meta_environment.rascal.ast.StringTail getTail() { throw new UnsupportedOperationException(); } public boolean hasPre() { return false; } public boolean hasExpression() { return false; } public boolean hasTail() { return false; } public boolean isInterpolated() { return false; }
static public class Interpolated extends StringLiteral {
/** pre:PreStringChars expression:Expression tail:StringTail -> StringLiteral {cons("Interpolated")} */
	public Interpolated(INode node, org.meta_environment.rascal.ast.PreStringChars pre, org.meta_environment.rascal.ast.Expression expression, org.meta_environment.rascal.ast.StringTail tail) {
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

private final org.meta_environment.rascal.ast.PreStringChars pre;
	public org.meta_environment.rascal.ast.PreStringChars getPre() { return pre; }
	private final org.meta_environment.rascal.ast.Expression expression;
	public org.meta_environment.rascal.ast.Expression getExpression() { return expression; }
	private final org.meta_environment.rascal.ast.StringTail tail;
	public org.meta_environment.rascal.ast.StringTail getTail() { return tail; }	
}
static public class Ambiguity extends StringLiteral {
  private final java.util.List<org.meta_environment.rascal.ast.StringLiteral> alternatives;
  public Ambiguity(INode node, java.util.List<org.meta_environment.rascal.ast.StringLiteral> alternatives) {
	this.alternatives = java.util.Collections.unmodifiableList(alternatives);
         this.node = node;
  }
  public java.util.List<org.meta_environment.rascal.ast.StringLiteral> getAlternatives() {
	return alternatives;
  }
  
  public <T> T accept(IASTVisitor<T> v) {
     return v.visitStringLiteralAmbiguity(this);
  }
} public org.meta_environment.rascal.ast.StringTemplate getTemplate() { throw new UnsupportedOperationException(); } public boolean hasTemplate() { return false; } public boolean isTemplate() { return false; }
static public class Template extends StringLiteral {
/** pre:PreStringChars template:StringTemplate tail:StringTail -> StringLiteral {cons("Template")} */
	public Template(INode node, org.meta_environment.rascal.ast.PreStringChars pre, org.meta_environment.rascal.ast.StringTemplate template, org.meta_environment.rascal.ast.StringTail tail) {
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

private final org.meta_environment.rascal.ast.PreStringChars pre;
	public org.meta_environment.rascal.ast.PreStringChars getPre() { return pre; }
	private final org.meta_environment.rascal.ast.StringTemplate template;
	public org.meta_environment.rascal.ast.StringTemplate getTemplate() { return template; }
	private final org.meta_environment.rascal.ast.StringTail tail;
	public org.meta_environment.rascal.ast.StringTail getTail() { return tail; }	
} public abstract <T> T accept(IASTVisitor<T> visitor); public org.meta_environment.rascal.ast.StringConstant getConstant() { throw new UnsupportedOperationException(); }
public boolean hasConstant() { return false; }
public boolean isNonInterpolated() { return false; }
static public class NonInterpolated extends StringLiteral {
/** constant:StringConstant -> StringLiteral {cons("NonInterpolated")} */
	public NonInterpolated(INode node, org.meta_environment.rascal.ast.StringConstant constant) {
		this.node = node;
		this.constant = constant;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitStringLiteralNonInterpolated(this);
	}

	public boolean isNonInterpolated() { return true; }

	public boolean hasConstant() { return true; }

private final org.meta_environment.rascal.ast.StringConstant constant;
	public org.meta_environment.rascal.ast.StringConstant getConstant() { return constant; }	
}
}