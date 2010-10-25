package org.rascalmpl.ast; 
import org.eclipse.imp.pdb.facts.INode; 
public abstract class StringTail extends AbstractAST { 
  public org.rascalmpl.ast.MidStringChars getMid() { throw new UnsupportedOperationException(); } public org.rascalmpl.ast.Expression getExpression() { throw new UnsupportedOperationException(); } public org.rascalmpl.ast.StringTail getTail() { throw new UnsupportedOperationException(); } public boolean hasMid() { return false; } public boolean hasExpression() { return false; } public boolean hasTail() { return false; } public boolean isMidInterpolated() { return false; }
static public class MidInterpolated extends StringTail {
/** mid:MidStringChars expression:Expression tail:StringTail -> StringTail {cons("MidInterpolated")} */
	protected MidInterpolated(INode node, org.rascalmpl.ast.MidStringChars mid, org.rascalmpl.ast.Expression expression, org.rascalmpl.ast.StringTail tail) {
		this.node = node;
		this.mid = mid;
		this.expression = expression;
		this.tail = tail;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitStringTailMidInterpolated(this);
	}

	public boolean isMidInterpolated() { return true; }

	public boolean hasMid() { return true; }
	public boolean hasExpression() { return true; }
	public boolean hasTail() { return true; }

private final org.rascalmpl.ast.MidStringChars mid;
	public org.rascalmpl.ast.MidStringChars getMid() { return mid; }
	private final org.rascalmpl.ast.Expression expression;
	public org.rascalmpl.ast.Expression getExpression() { return expression; }
	private final org.rascalmpl.ast.StringTail tail;
	public org.rascalmpl.ast.StringTail getTail() { return tail; }	
}
static public class Ambiguity extends StringTail {
  private final java.util.List<org.rascalmpl.ast.StringTail> alternatives;
  protected Ambiguity(INode node, java.util.List<org.rascalmpl.ast.StringTail> alternatives) {
	this.alternatives = java.util.Collections.unmodifiableList(alternatives);
         this.node = node;
  }
  public java.util.List<org.rascalmpl.ast.StringTail> getAlternatives() {
	return alternatives;
  }
  
  public <T> T accept(IASTVisitor<T> v) {
     return v.visitStringTailAmbiguity(this);
  }
} public org.rascalmpl.ast.StringTemplate getTemplate() { throw new UnsupportedOperationException(); } public boolean hasTemplate() { return false; } public boolean isMidTemplate() { return false; }
static public class MidTemplate extends StringTail {
/** mid:MidStringChars template:StringTemplate tail:StringTail -> StringTail {cons("MidTemplate")} */
	protected MidTemplate(INode node, org.rascalmpl.ast.MidStringChars mid, org.rascalmpl.ast.StringTemplate template, org.rascalmpl.ast.StringTail tail) {
		this.node = node;
		this.mid = mid;
		this.template = template;
		this.tail = tail;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitStringTailMidTemplate(this);
	}

	public boolean isMidTemplate() { return true; }

	public boolean hasMid() { return true; }
	public boolean hasTemplate() { return true; }
	public boolean hasTail() { return true; }

private final org.rascalmpl.ast.MidStringChars mid;
	public org.rascalmpl.ast.MidStringChars getMid() { return mid; }
	private final org.rascalmpl.ast.StringTemplate template;
	public org.rascalmpl.ast.StringTemplate getTemplate() { return template; }
	private final org.rascalmpl.ast.StringTail tail;
	public org.rascalmpl.ast.StringTail getTail() { return tail; }	
} public abstract <T> T accept(IASTVisitor<T> visitor); public org.rascalmpl.ast.PostStringChars getPost() { throw new UnsupportedOperationException(); }
public boolean hasPost() { return false; }
public boolean isPost() { return false; }
static public class Post extends StringTail {
/** post:PostStringChars -> StringTail {cons("Post")} */
	protected Post(INode node, org.rascalmpl.ast.PostStringChars post) {
		this.node = node;
		this.post = post;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitStringTailPost(this);
	}

	public boolean isPost() { return true; }

	public boolean hasPost() { return true; }

private final org.rascalmpl.ast.PostStringChars post;
	public org.rascalmpl.ast.PostStringChars getPost() { return post; }	
}
}