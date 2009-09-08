package org.meta_environment.rascal.ast; 
import org.eclipse.imp.pdb.facts.INode; 
public abstract class PathTail extends AbstractAST { 
  public org.meta_environment.rascal.ast.MidPathChars getMid() { throw new UnsupportedOperationException(); }
	public org.meta_environment.rascal.ast.Expression getExpression() { throw new UnsupportedOperationException(); }
	public org.meta_environment.rascal.ast.PathTail getTail() { throw new UnsupportedOperationException(); }
public boolean hasMid() { return false; }
	public boolean hasExpression() { return false; }
	public boolean hasTail() { return false; }
public boolean isMid() { return false; }
static public class Mid extends PathTail {
/** mid:MidPathChars expression:Expression tail:PathTail -> PathTail {cons("Mid")} */
	private Mid() {
		super();
	}
	public Mid(INode node, org.meta_environment.rascal.ast.MidPathChars mid, org.meta_environment.rascal.ast.Expression expression, org.meta_environment.rascal.ast.PathTail tail) {
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

private org.meta_environment.rascal.ast.MidPathChars mid;
	public org.meta_environment.rascal.ast.MidPathChars getMid() { return mid; }
	private void $setMid(org.meta_environment.rascal.ast.MidPathChars x) { this.mid = x; }
	public Mid setMid(org.meta_environment.rascal.ast.MidPathChars x) { 
		Mid z = new Mid();
 		z.$setMid(x);
		return z;
	}
	private org.meta_environment.rascal.ast.Expression expression;
	public org.meta_environment.rascal.ast.Expression getExpression() { return expression; }
	private void $setExpression(org.meta_environment.rascal.ast.Expression x) { this.expression = x; }
	public Mid setExpression(org.meta_environment.rascal.ast.Expression x) { 
		Mid z = new Mid();
 		z.$setExpression(x);
		return z;
	}
	private org.meta_environment.rascal.ast.PathTail tail;
	public org.meta_environment.rascal.ast.PathTail getTail() { return tail; }
	private void $setTail(org.meta_environment.rascal.ast.PathTail x) { this.tail = x; }
	public Mid setTail(org.meta_environment.rascal.ast.PathTail x) { 
		Mid z = new Mid();
 		z.$setTail(x);
		return z;
	}	
}
static public class Ambiguity extends PathTail {
  private final java.util.List<org.meta_environment.rascal.ast.PathTail> alternatives;
  public Ambiguity(INode node, java.util.List<org.meta_environment.rascal.ast.PathTail> alternatives) {
	this.alternatives = java.util.Collections.unmodifiableList(alternatives);
         this.node = node;
  }
  public java.util.List<org.meta_environment.rascal.ast.PathTail> getAlternatives() {
	return alternatives;
  }
  
  public <T> T accept(IASTVisitor<T> v) {
     return v.visitPathTailAmbiguity(this);
  }
} 
public org.meta_environment.rascal.ast.PostPathChars getPost() { throw new UnsupportedOperationException(); }
public boolean hasPost() { return false; }
public boolean isPost() { return false; }
static public class Post extends PathTail {
/** post:PostPathChars -> PathTail {cons("Post")} */
	private Post() {
		super();
	}
	public Post(INode node, org.meta_environment.rascal.ast.PostPathChars post) {
		this.node = node;
		this.post = post;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitPathTailPost(this);
	}

	public boolean isPost() { return true; }

	public boolean hasPost() { return true; }

private org.meta_environment.rascal.ast.PostPathChars post;
	public org.meta_environment.rascal.ast.PostPathChars getPost() { return post; }
	private void $setPost(org.meta_environment.rascal.ast.PostPathChars x) { this.post = x; }
	public Post setPost(org.meta_environment.rascal.ast.PostPathChars x) { 
		Post z = new Post();
 		z.$setPost(x);
		return z;
	}	
}
 public abstract <T> T accept(IASTVisitor<T> visitor);
}