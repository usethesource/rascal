package org.rascalmpl.ast; 
import org.eclipse.imp.pdb.facts.INode; 
public abstract class PathTail extends AbstractAST { 
  public org.rascalmpl.ast.MidPathChars getMid() { throw new UnsupportedOperationException(); }
	public org.rascalmpl.ast.Expression getExpression() { throw new UnsupportedOperationException(); }
	public org.rascalmpl.ast.PathTail getTail() { throw new UnsupportedOperationException(); }
public boolean hasMid() { return false; }
	public boolean hasExpression() { return false; }
	public boolean hasTail() { return false; }
public boolean isMid() { return false; }
static public class Mid extends PathTail {
/** mid:MidPathChars expression:Expression tail:PathTail -> PathTail {cons("Mid")} */
	public Mid(INode node, org.rascalmpl.ast.MidPathChars mid, org.rascalmpl.ast.Expression expression, org.rascalmpl.ast.PathTail tail) {
		this.node = node;
		this.mid = mid;
		this.expression = expression;
		this.tail = tail;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitPathTailMid(this);
	}

	public boolean isMid() { return true; }

	public boolean hasMid() { return true; }
	public boolean hasExpression() { return true; }
	public boolean hasTail() { return true; }

private final org.rascalmpl.ast.MidPathChars mid;
	public org.rascalmpl.ast.MidPathChars getMid() { return mid; }
	private final org.rascalmpl.ast.Expression expression;
	public org.rascalmpl.ast.Expression getExpression() { return expression; }
	private final org.rascalmpl.ast.PathTail tail;
	public org.rascalmpl.ast.PathTail getTail() { return tail; }	
}
static public class Ambiguity extends PathTail {
  private final java.util.List<org.rascalmpl.ast.PathTail> alternatives;
  public Ambiguity(INode node, java.util.List<org.rascalmpl.ast.PathTail> alternatives) {
	this.alternatives = java.util.Collections.unmodifiableList(alternatives);
         this.node = node;
  }
  public java.util.List<org.rascalmpl.ast.PathTail> getAlternatives() {
	return alternatives;
  }
  
  public <T> T accept(IASTVisitor<T> v) {
     return v.visitPathTailAmbiguity(this);
  }
} 
public org.rascalmpl.ast.PostPathChars getPost() { throw new UnsupportedOperationException(); }
public boolean hasPost() { return false; }
public boolean isPost() { return false; }
static public class Post extends PathTail {
/** post:PostPathChars -> PathTail {cons("Post")} */
	public Post(INode node, org.rascalmpl.ast.PostPathChars post) {
		this.node = node;
		this.post = post;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitPathTailPost(this);
	}

	public boolean isPost() { return true; }

	public boolean hasPost() { return true; }

private final org.rascalmpl.ast.PostPathChars post;
	public org.rascalmpl.ast.PostPathChars getPost() { return post; }	
}
 public abstract <T> T accept(IASTVisitor<T> visitor);
}