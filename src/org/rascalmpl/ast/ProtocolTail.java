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
	public Mid(INode node, org.rascalmpl.ast.MidProtocolChars mid, org.rascalmpl.ast.Expression expression, org.rascalmpl.ast.ProtocolTail tail) {
		this.node = node;
		this.mid = mid;
		this.expression = expression;
		this.tail = tail;
	}
	@Override
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitProtocolTailMid(this);
	}

	@Override
	public boolean isMid() { return true; }

	@Override
	public boolean hasMid() { return true; }
	@Override
	public boolean hasExpression() { return true; }
	@Override
	public boolean hasTail() { return true; }

private final org.rascalmpl.ast.MidProtocolChars mid;
	@Override
	public org.rascalmpl.ast.MidProtocolChars getMid() { return mid; }
	private final org.rascalmpl.ast.Expression expression;
	@Override
	public org.rascalmpl.ast.Expression getExpression() { return expression; }
	private final org.rascalmpl.ast.ProtocolTail tail;
	@Override
	public org.rascalmpl.ast.ProtocolTail getTail() { return tail; }	
}
static public class Ambiguity extends ProtocolTail {
  private final java.util.List<org.rascalmpl.ast.ProtocolTail> alternatives;
  public Ambiguity(INode node, java.util.List<org.rascalmpl.ast.ProtocolTail> alternatives) {
	this.alternatives = java.util.Collections.unmodifiableList(alternatives);
         this.node = node;
  }
  public java.util.List<org.rascalmpl.ast.ProtocolTail> getAlternatives() {
	return alternatives;
  }
  
  @Override
public <T> T accept(IASTVisitor<T> v) {
     return v.visitProtocolTailAmbiguity(this);
  }
} 
public org.rascalmpl.ast.PostProtocolChars getPost() { throw new UnsupportedOperationException(); }
public boolean hasPost() { return false; }
public boolean isPost() { return false; }
static public class Post extends ProtocolTail {
/** post:PostProtocolChars -> ProtocolTail {cons("Post")} */
	public Post(INode node, org.rascalmpl.ast.PostProtocolChars post) {
		this.node = node;
		this.post = post;
	}
	@Override
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitProtocolTailPost(this);
	}

	@Override
	public boolean isPost() { return true; }

	@Override
	public boolean hasPost() { return true; }

private final org.rascalmpl.ast.PostProtocolChars post;
	@Override
	public org.rascalmpl.ast.PostProtocolChars getPost() { return post; }	
}
 @Override
public abstract <T> T accept(IASTVisitor<T> visitor);
}