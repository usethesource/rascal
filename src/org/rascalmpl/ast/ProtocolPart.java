package org.rascalmpl.ast; 
import org.eclipse.imp.pdb.facts.INode; 
public abstract class ProtocolPart extends AbstractAST { 
  public org.rascalmpl.ast.PreProtocolChars getPre() { throw new UnsupportedOperationException(); }
	public org.rascalmpl.ast.Expression getExpression() { throw new UnsupportedOperationException(); }
	public org.rascalmpl.ast.ProtocolTail getTail() { throw new UnsupportedOperationException(); }
public boolean hasPre() { return false; }
	public boolean hasExpression() { return false; }
	public boolean hasTail() { return false; }
public boolean isInterpolated() { return false; }
static public class Interpolated extends ProtocolPart {
/** pre:PreProtocolChars expression:Expression tail:ProtocolTail -> ProtocolPart {cons("Interpolated")} */
	public Interpolated(INode node, org.rascalmpl.ast.PreProtocolChars pre, org.rascalmpl.ast.Expression expression, org.rascalmpl.ast.ProtocolTail tail) {
		this.node = node;
		this.pre = pre;
		this.expression = expression;
		this.tail = tail;
	}
	@Override
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitProtocolPartInterpolated(this);
	}

	@Override
	public boolean isInterpolated() { return true; }

	@Override
	public boolean hasPre() { return true; }
	@Override
	public boolean hasExpression() { return true; }
	@Override
	public boolean hasTail() { return true; }

private final org.rascalmpl.ast.PreProtocolChars pre;
	@Override
	public org.rascalmpl.ast.PreProtocolChars getPre() { return pre; }
	private final org.rascalmpl.ast.Expression expression;
	@Override
	public org.rascalmpl.ast.Expression getExpression() { return expression; }
	private final org.rascalmpl.ast.ProtocolTail tail;
	@Override
	public org.rascalmpl.ast.ProtocolTail getTail() { return tail; }	
}
static public class Ambiguity extends ProtocolPart {
  private final java.util.List<org.rascalmpl.ast.ProtocolPart> alternatives;
  public Ambiguity(INode node, java.util.List<org.rascalmpl.ast.ProtocolPart> alternatives) {
	this.alternatives = java.util.Collections.unmodifiableList(alternatives);
         this.node = node;
  }
  public java.util.List<org.rascalmpl.ast.ProtocolPart> getAlternatives() {
	return alternatives;
  }
  
  @Override
public <T> T accept(IASTVisitor<T> v) {
     return v.visitProtocolPartAmbiguity(this);
  }
} 
public org.rascalmpl.ast.ProtocolChars getProtocolChars() { throw new UnsupportedOperationException(); }
public boolean hasProtocolChars() { return false; }
public boolean isNonInterpolated() { return false; }
static public class NonInterpolated extends ProtocolPart {
/** protocolChars:ProtocolChars -> ProtocolPart {cons("NonInterpolated")} */
	public NonInterpolated(INode node, org.rascalmpl.ast.ProtocolChars protocolChars) {
		this.node = node;
		this.protocolChars = protocolChars;
	}
	@Override
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitProtocolPartNonInterpolated(this);
	}

	@Override
	public boolean isNonInterpolated() { return true; }

	@Override
	public boolean hasProtocolChars() { return true; }

private final org.rascalmpl.ast.ProtocolChars protocolChars;
	@Override
	public org.rascalmpl.ast.ProtocolChars getProtocolChars() { return protocolChars; }	
}
 @Override
public abstract <T> T accept(IASTVisitor<T> visitor);
}