package org.meta_environment.rascal.ast; 
import org.eclipse.imp.pdb.facts.INode; 
public abstract class StringTail extends AbstractAST { 
  public org.meta_environment.rascal.ast.MidStringChars getMid() { throw new UnsupportedOperationException(); }
	public org.meta_environment.rascal.ast.Expression getExpression() { throw new UnsupportedOperationException(); }
	public org.meta_environment.rascal.ast.StringTail getTail() { throw new UnsupportedOperationException(); }
public boolean hasMid() { return false; }
	public boolean hasExpression() { return false; }
	public boolean hasTail() { return false; }
public boolean isMid() { return false; }
static public class Mid extends StringTail {
/** mid:MidStringChars expression:Expression tail:StringTail -> StringTail {cons("Mid")} */
	public Mid(INode node, org.meta_environment.rascal.ast.MidStringChars mid, org.meta_environment.rascal.ast.Expression expression, org.meta_environment.rascal.ast.StringTail tail) {
		this.node = node;
		this.mid = mid;
		this.expression = expression;
		this.tail = tail;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitStringTailMid(this);
	}

	public boolean isMid() { return true; }

	public boolean hasMid() { return true; }
	public boolean hasExpression() { return true; }
	public boolean hasTail() { return true; }

private final org.meta_environment.rascal.ast.MidStringChars mid;
	public org.meta_environment.rascal.ast.MidStringChars getMid() { return mid; }
	private final org.meta_environment.rascal.ast.Expression expression;
	public org.meta_environment.rascal.ast.Expression getExpression() { return expression; }
	private final org.meta_environment.rascal.ast.StringTail tail;
	public org.meta_environment.rascal.ast.StringTail getTail() { return tail; }	
}
static public class Ambiguity extends StringTail {
  private final java.util.List<org.meta_environment.rascal.ast.StringTail> alternatives;
  public Ambiguity(INode node, java.util.List<org.meta_environment.rascal.ast.StringTail> alternatives) {
	this.alternatives = java.util.Collections.unmodifiableList(alternatives);
         this.node = node;
  }
  public java.util.List<org.meta_environment.rascal.ast.StringTail> getAlternatives() {
	return alternatives;
  }
  
  public <T> T accept(IASTVisitor<T> v) {
     return v.visitStringTailAmbiguity(this);
  }
} 
public org.meta_environment.rascal.ast.PostStringChars getPost() { throw new UnsupportedOperationException(); }
public boolean hasPost() { return false; }
public boolean isPost() { return false; }
static public class Post extends StringTail {
/** post:PostStringChars -> StringTail {cons("Post")} */
	public Post(INode node, org.meta_environment.rascal.ast.PostStringChars post) {
		this.node = node;
		this.post = post;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitStringTailPost(this);
	}

	public boolean isPost() { return true; }

	public boolean hasPost() { return true; }

private final org.meta_environment.rascal.ast.PostStringChars post;
	public org.meta_environment.rascal.ast.PostStringChars getPost() { return post; }	
}
 public abstract <T> T accept(IASTVisitor<T> visitor);
}