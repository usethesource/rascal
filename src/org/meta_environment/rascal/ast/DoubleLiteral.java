package org.meta_environment.rascal.ast; 
import org.eclipse.imp.pdb.facts.ITree; 
public abstract class DoubleLiteral extends AbstractAST { 
  static public class Lexical extends DoubleLiteral {
	private String string;
	/*package*/ Lexical(ITree tree, String string) {
		this.tree = tree;
		this.string = string;
	}
	public String getString() {
		return string;
	}

 	public <T> T accept(IASTVisitor<T> v) {
     		return v.visitDoubleLiteralLexical(this);
  	}
} static public class Ambiguity extends DoubleLiteral {
  private final java.util.List<org.meta_environment.rascal.ast.DoubleLiteral> alternatives;
  public Ambiguity(ITree tree, java.util.List<org.meta_environment.rascal.ast.DoubleLiteral> alternatives) {
	this.alternatives = java.util.Collections.unmodifiableList(alternatives);
         this.tree = tree;
  }
  public java.util.List<org.meta_environment.rascal.ast.DoubleLiteral> getAlternatives() {
	return alternatives;
  }
  
  public <T> T accept(IASTVisitor<T> v) {
     return v.visitDoubleLiteralAmbiguity(this);
  }
} public abstract <T> T accept(IASTVisitor<T> visitor);
}