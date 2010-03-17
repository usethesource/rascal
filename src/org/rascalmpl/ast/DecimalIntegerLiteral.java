package org.rascalmpl.ast; 
import org.eclipse.imp.pdb.facts.INode; 
public abstract class DecimalIntegerLiteral extends AbstractAST { 
  static public class Lexical extends DecimalIntegerLiteral {
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
     		return v.visitDecimalIntegerLiteralLexical(this);
  	}
} static public class Ambiguity extends DecimalIntegerLiteral {
  private final java.util.List<org.rascalmpl.ast.DecimalIntegerLiteral> alternatives;
  public Ambiguity(INode node, java.util.List<org.rascalmpl.ast.DecimalIntegerLiteral> alternatives) {
	this.alternatives = java.util.Collections.unmodifiableList(alternatives);
         this.node = node;
  }
  public java.util.List<org.rascalmpl.ast.DecimalIntegerLiteral> getAlternatives() {
	return alternatives;
  }
  
  @Override
public <T> T accept(IASTVisitor<T> v) {
     return v.visitDecimalIntegerLiteralAmbiguity(this);
  }
} @Override
public abstract <T> T accept(IASTVisitor<T> visitor);
}