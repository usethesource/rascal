package org.rascalmpl.ast; 
import org.eclipse.imp.pdb.facts.INode; 
public abstract class ProtocolTail extends AbstractAST { 
  public org.rascalmpl.ast.MidProtocolChars getMid() { throw new UnsupportedOperationException(); }
	public org.rascalmpl.ast.Expression getExpression() { throw new UnsupportedOperationException(); }
	public org.rascalmpl.ast.ProtocolTail getTail() { throw new UnsupportedOperationException(); }
public boolean hasMid() { return false; }
	public boolean hasExpression() { return false; }
	public boolean hasTail() { return false; }
public boolean isMid() { return false; }
static public class Mid extends ProtocolTail {
/** mid:MidProtocolChars expression:Expression tail:ProtocolTail -> ProtocolTail {cons("Mid")} */
	protected Mid(INode node, org.rascalmpl.ast.MidProtocolChars mid, org.rascalmpl.ast.Expression expression, org.rascalmpl.ast.ProtocolTail tail) {
		this.node = node;
		this.mid = mid;
		this.expression = expression;
		this.tail = tail;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitProtocolTailMid(this);
	}

	public boolean isMid() { return true; }

	public boolean hasMid() { return true; }
	public boolean hasExpression() { return true; }
	public boolean hasTail() { return true; }

private final org.rascalmpl.ast.MidProtocolChars mid;
	public org.rascalmpl.ast.MidProtocolChars getMid() { return mid; }
	private final org.rascalmpl.ast.Expression expression;
	public org.rascalmpl.ast.Expression getExpression() { return expression; }
	private final org.rascalmpl.ast.ProtocolTail tail;
	public org.rascalmpl.ast.ProtocolTail getTail() { return tail; }	
}
static public class Ambiguity extends ProtocolTail {
  private final java.util.List<org.rascalmpl.ast.ProtocolTail> alternatives;
  protected Ambiguity(INode node, java.util.List<org.rascalmpl.ast.ProtocolTail> alternatives) {
	this.alternatives = java.util.Collections.unmodifiableList(alternatives);
         this.node = node;
  }
  public java.util.List<org.rascalmpl.ast.ProtocolTail> getAlternatives() {
	return alternatives;
  }
  
  public <T> T accept(IASTVisitor<T> v) {
     return v.visitProtocolTailAmbiguity(this);
  }
} 
public org.rascalmpl.ast.PostProtocolChars getPost() { throw new UnsupportedOperationException(); }
public boolean hasPost() { return false; }
public boolean isPost() { return false; }
static public class Post extends ProtocolTail {
/** post:PostProtocolChars -> ProtocolTail {cons("Post")} */
	protected Post(INode node, org.rascalmpl.ast.PostProtocolChars post) {
		this.node = node;
		this.post = post;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitProtocolTailPost(this);
	}

	public boolean isPost() { return true; }

	public boolean hasPost() { return true; }

private final org.rascalmpl.ast.PostProtocolChars post;
	public org.rascalmpl.ast.PostProtocolChars getPost() { return post; }	
}
 public abstract <T> T accept(IASTVisitor<T> visitor);
}