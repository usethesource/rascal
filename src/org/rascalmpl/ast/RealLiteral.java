package org.rascalmpl.ast; 
import org.eclipse.imp.pdb.facts.INode; 
public abstract class RealLiteral extends AbstractAST { 
  static public class Lexical extends RealLiteral {
	private final String string;
         public Lexical(INode node, String string) {
		this.node = node;
		this.string = string;
	}
	public String getString() {
		return string;
	}

 	@Override
	public <T> T accept(IASTVisitor<T> v) {
     		return v.visitRealLiteralLexical(this);
  	}
} static public class Ambiguity extends RealLiteral {
  private final java.util.List<org.rascalmpl.ast.RealLiteral> alternatives;
  public Ambiguity(INode node, java.util.List<org.rascalmpl.ast.RealLiteral> alternatives) {
	this.alternatives = java.util.Collections.unmodifiableList(alternatives);
         this.node = node;
  }
  public java.util.List<org.rascalmpl.ast.RealLiteral> getAlternatives() {
	return alternatives;
  }
  
  @Override
public <T> T accept(IASTVisitor<T> v) {
     return v.visitRealLiteralAmbiguity(this);
  }
} @Override
public abstract <T> T accept(IASTVisitor<T> visitor);
}