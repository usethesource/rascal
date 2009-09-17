package org.meta_environment.rascal.ast; 
import org.eclipse.imp.pdb.facts.INode; 
public abstract class ProtocolPart extends AbstractAST { 
  public org.meta_environment.rascal.ast.PreProtocolChars getPre() { throw new UnsupportedOperationException(); }
	public org.meta_environment.rascal.ast.Expression getExpression() { throw new UnsupportedOperationException(); }
	public org.meta_environment.rascal.ast.ProtocolTail getTail() { throw new UnsupportedOperationException(); }
public boolean hasPre() { return false; }
	public boolean hasExpression() { return false; }
	public boolean hasTail() { return false; }
public boolean isInterpolated() { return false; }
static public class Interpolated extends ProtocolPart {
/** pre:PreProtocolChars expression:Expression tail:ProtocolTail -> ProtocolPart {cons("Interpolated")} */
	public Interpolated(INode node, org.meta_environment.rascal.ast.PreProtocolChars pre, org.meta_environment.rascal.ast.Expression expression, org.meta_environment.rascal.ast.ProtocolTail tail) {
		this.node = node;
		this.pre = pre;
		this.expression = expression;
		this.tail = tail;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitProtocolPartInterpolated(this);
	}

	public boolean isInterpolated() { return true; }

	public boolean hasPre() { return true; }
	public boolean hasExpression() { return true; }
	public boolean hasTail() { return true; }

private final org.meta_environment.rascal.ast.PreProtocolChars pre;
	public org.meta_environment.rascal.ast.PreProtocolChars getPre() { return pre; }
	private final org.meta_environment.rascal.ast.Expression expression;
	public org.meta_environment.rascal.ast.Expression getExpression() { return expression; }
	private final org.meta_environment.rascal.ast.ProtocolTail tail;
	public org.meta_environment.rascal.ast.ProtocolTail getTail() { return tail; }	
}
static public class Ambiguity extends ProtocolPart {
  private final java.util.List<org.meta_environment.rascal.ast.ProtocolPart> alternatives;
  public Ambiguity(INode node, java.util.List<org.meta_environment.rascal.ast.ProtocolPart> alternatives) {
	this.alternatives = java.util.Collections.unmodifiableList(alternatives);
         this.node = node;
  }
  public java.util.List<org.meta_environment.rascal.ast.ProtocolPart> getAlternatives() {
	return alternatives;
  }
  
  public <T> T accept(IASTVisitor<T> v) {
     return v.visitProtocolPartAmbiguity(this);
  }
} 
public org.meta_environment.rascal.ast.ProtocolChars getProtocolChars() { throw new UnsupportedOperationException(); }
public boolean hasProtocolChars() { return false; }
public boolean isNonInterpolated() { return false; }
static public class NonInterpolated extends ProtocolPart {
/** protocolChars:ProtocolChars -> ProtocolPart {cons("NonInterpolated")} */
	public NonInterpolated(INode node, org.meta_environment.rascal.ast.ProtocolChars protocolChars) {
		this.node = node;
		this.protocolChars = protocolChars;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitProtocolPartNonInterpolated(this);
	}

	public boolean isNonInterpolated() { return true; }

	public boolean hasProtocolChars() { return true; }

private final org.meta_environment.rascal.ast.ProtocolChars protocolChars;
	public org.meta_environment.rascal.ast.ProtocolChars getProtocolChars() { return protocolChars; }	
}
 public abstract <T> T accept(IASTVisitor<T> visitor);
}