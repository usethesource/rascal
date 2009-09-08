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
	private Interpolated() {
		super();
	}
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

private org.meta_environment.rascal.ast.PreProtocolChars pre;
	public org.meta_environment.rascal.ast.PreProtocolChars getPre() { return pre; }
	private void $setPre(org.meta_environment.rascal.ast.PreProtocolChars x) { this.pre = x; }
	public Interpolated setPre(org.meta_environment.rascal.ast.PreProtocolChars x) { 
		Interpolated z = new Interpolated();
 		z.$setPre(x);
		return z;
	}
	private org.meta_environment.rascal.ast.Expression expression;
	public org.meta_environment.rascal.ast.Expression getExpression() { return expression; }
	private void $setExpression(org.meta_environment.rascal.ast.Expression x) { this.expression = x; }
	public Interpolated setExpression(org.meta_environment.rascal.ast.Expression x) { 
		Interpolated z = new Interpolated();
 		z.$setExpression(x);
		return z;
	}
	private org.meta_environment.rascal.ast.ProtocolTail tail;
	public org.meta_environment.rascal.ast.ProtocolTail getTail() { return tail; }
	private void $setTail(org.meta_environment.rascal.ast.ProtocolTail x) { this.tail = x; }
	public Interpolated setTail(org.meta_environment.rascal.ast.ProtocolTail x) { 
		Interpolated z = new Interpolated();
 		z.$setTail(x);
		return z;
	}	
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
	private NonInterpolated() {
		super();
	}
	public NonInterpolated(INode node, org.meta_environment.rascal.ast.ProtocolChars protocolChars) {
		this.node = node;
		this.protocolChars = protocolChars;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitProtocolPartNonInterpolated(this);
	}

	public boolean isNonInterpolated() { return true; }

	public boolean hasProtocolChars() { return true; }

private org.meta_environment.rascal.ast.ProtocolChars protocolChars;
	public org.meta_environment.rascal.ast.ProtocolChars getProtocolChars() { return protocolChars; }
	private void $setProtocolChars(org.meta_environment.rascal.ast.ProtocolChars x) { this.protocolChars = x; }
	public NonInterpolated setProtocolChars(org.meta_environment.rascal.ast.ProtocolChars x) { 
		NonInterpolated z = new NonInterpolated();
 		z.$setProtocolChars(x);
		return z;
	}	
}
 public abstract <T> T accept(IASTVisitor<T> visitor);
}