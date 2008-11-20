package org.meta_environment.rascal.ast; 
import org.eclipse.imp.pdb.facts.ITree; 
public abstract class BooleanLiteral extends AbstractAST { 
  static public class Lexical extends BooleanLiteral {
	private String string;
	/*package*/ Lexical(ITree tree, String string) {
		this.tree = tree;
		this.string = string;
	}
	public String getString() {
		return string;
	}

 	public <T> T accept(IASTVisitor<T> v) {
     		return v.visitBooleanLiteralLexical(this);
  	}
} static public class Ambiguity extends BooleanLiteral {
  private final java.util.List<org.meta_environment.rascal.ast.BooleanLiteral> alternatives;
  public Ambiguity(java.util.List<org.meta_environment.rascal.ast.BooleanLiteral> alternatives) {
	this.alternatives = java.util.Collections.unmodifiableList(alternatives);
  }
  public java.util.List<org.meta_environment.rascal.ast.BooleanLiteral> getAlternatives() {
	return alternatives;
  }
  
  public <T> T accept(IASTVisitor<T> v) {
     return v.visitBooleanLiteralAmbiguity(this);
  }
} public abstract <T> T accept(IASTVisitor<T> visitor);
}