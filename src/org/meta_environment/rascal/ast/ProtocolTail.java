package org.meta_environment.rascal.ast; 
import org.eclipse.imp.pdb.facts.INode; 
public abstract class ProtocolTail extends AbstractAST { 
  public org.meta_environment.rascal.ast.MidProtocolChars getMid() { throw new UnsupportedOperationException(); }
	public org.meta_environment.rascal.ast.Expression getExpression() { throw new UnsupportedOperationException(); }
	public org.meta_environment.rascal.ast.ProtocolTail getTail() { throw new UnsupportedOperationException(); }
public boolean hasMid() { return false; }
	public boolean hasExpression() { return false; }
	public boolean hasTail() { return false; }
public boolean isMid() { return false; }
static public class Mid extends ProtocolTail {
/** mid:MidProtocolChars expression:Expression tail:ProtocolTail -> ProtocolTail {cons("Mid")} */
	private Mid() {
		super();
	}
	public Mid(INode node, org.meta_environment.rascal.ast.MidProtocolChars mid, org.meta_environment.rascal.ast.Expression expression, org.meta_environment.rascal.ast.ProtocolTail tail) {
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

private org.meta_environment.rascal.ast.MidProtocolChars mid;
	public org.meta_environment.rascal.ast.MidProtocolChars getMid() { return mid; }
	private void $setMid(org.meta_environment.rascal.ast.MidProtocolChars x) { this.mid = x; }
	public Mid setMid(org.meta_environment.rascal.ast.MidProtocolChars x) { 
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
	private org.meta_environment.rascal.ast.ProtocolTail tail;
	public org.meta_environment.rascal.ast.ProtocolTail getTail() { return tail; }
	private void $setTail(org.meta_environment.rascal.ast.ProtocolTail x) { this.tail = x; }
	public Mid setTail(org.meta_environment.rascal.ast.ProtocolTail x) { 
		Mid z = new Mid();
 		z.$setTail(x);
		return z;
	}	
}
static public class Ambiguity extends ProtocolTail {
  private final java.util.List<org.meta_environment.rascal.ast.ProtocolTail> alternatives;
  public Ambiguity(INode node, java.util.List<org.meta_environment.rascal.ast.ProtocolTail> alternatives) {
	this.alternatives = java.util.Collections.unmodifiableList(alternatives);
         this.node = node;
  }
  public java.util.List<org.meta_environment.rascal.ast.ProtocolTail> getAlternatives() {
	return alternatives;
  }
  
  public <T> T accept(IASTVisitor<T> v) {
     return v.visitProtocolTailAmbiguity(this);
  }
} 
public org.meta_environment.rascal.ast.PostProtocolChars getPost() { throw new UnsupportedOperationException(); }
public boolean hasPost() { return false; }
public boolean isPost() { return false; }
static public class Post extends ProtocolTail {
/** post:PostProtocolChars -> ProtocolTail {cons("Post")} */
	private Post() {
		super();
	}
	public Post(INode node, org.meta_environment.rascal.ast.PostProtocolChars post) {
		this.node = node;
		this.post = post;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitProtocolTailPost(this);
	}

	public boolean isPost() { return true; }

	public boolean hasPost() { return true; }

private org.meta_environment.rascal.ast.PostProtocolChars post;
	public org.meta_environment.rascal.ast.PostProtocolChars getPost() { return post; }
	private void $setPost(org.meta_environment.rascal.ast.PostProtocolChars x) { this.post = x; }
	public Post setPost(org.meta_environment.rascal.ast.PostProtocolChars x) { 
		Post z = new Post();
 		z.$setPost(x);
		return z;
	}	
}
 public abstract <T> T accept(IASTVisitor<T> visitor);
}