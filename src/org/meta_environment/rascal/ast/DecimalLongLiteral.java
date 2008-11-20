package org.meta_environment.rascal.ast; 
import org.eclipse.imp.pdb.facts.ITree; 
public abstract class DecimalLongLiteral extends AbstractAST { 
  static public class Lexical extends DecimalLongLiteral {
	private String string;
	/*package*/ Lexical(ITree tree, String string) {
		this.tree = tree;
		this.string = string;
	}
	public String getString() {
		return string;
	}

 	public <T> T accept(IASTVisitor<T> v) {
     		return v.visitDecimalLongLiteralLexical(this);
  	}
} static public class Ambiguity extends DecimalLongLiteral {
  private final java.util.List<org.meta_environment.rascal.ast.DecimalLongLiteral> alternatives;
  public Ambiguity(java.util.List<org.meta_environment.rascal.ast.DecimalLongLiteral> alternatives) {
	this.alternatives = java.util.Collections.unmodifiableList(alternatives);
  }
  public java.util.List<org.meta_environment.rascal.ast.DecimalLongLiteral> getAlternatives() {
	return alternatives;
  }
  
  public <T> T accept(IASTVisitor<T> v) {
     return v.visitDecimalLongLiteralAmbiguity(this);
  }
} public abstract <T> T accept(IASTVisitor<T> visitor);
}