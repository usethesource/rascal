package org.rascalmpl.ast; 
import org.eclipse.imp.pdb.facts.INode; 
public abstract class DecimalLongLiteral extends AbstractAST { 
  static public class Lexical extends DecimalLongLiteral {
	private final String string;
         protected Lexical(INode node, String string) {
		this.node = node;
		this.string = string;
	}
	public String getString() {
		return string;
	}

 	public <T> T accept(IASTVisitor<T> v) {
     		return v.visitDecimalLongLiteralLexical(this);
  	}
} static public class Ambiguity extends DecimalLongLiteral {
  private final java.util.List<org.rascalmpl.ast.DecimalLongLiteral> alternatives;
  protected Ambiguity(INode node, java.util.List<org.rascalmpl.ast.DecimalLongLiteral> alternatives) {
	this.alternatives = java.util.Collections.unmodifiableList(alternatives);
         this.node = node;
  }
  public java.util.List<org.rascalmpl.ast.DecimalLongLiteral> getAlternatives() {
	return alternatives;
  }
  
  public <T> T accept(IASTVisitor<T> v) {
     return v.visitDecimalLongLiteralAmbiguity(this);
  }
} public abstract <T> T accept(IASTVisitor<T> visitor);
}