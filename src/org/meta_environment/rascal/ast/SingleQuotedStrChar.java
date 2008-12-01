package org.meta_environment.rascal.ast; 
import org.eclipse.imp.pdb.facts.ITree; 
public abstract class SingleQuotedStrChar extends AbstractAST { 
  static public class Lexical extends SingleQuotedStrChar {
	private String string;
	/*package*/ Lexical(ITree tree, String string) {
		this.tree = tree;
		this.string = string;
	}
	public String getString() {
		return string;
	}

 	public <T> T accept(IASTVisitor<T> v) {
     		return v.visitSingleQuotedStrCharLexical(this);
  	}
} static public class Ambiguity extends SingleQuotedStrChar {
  private final java.util.List<org.meta_environment.rascal.ast.SingleQuotedStrChar> alternatives;
  public Ambiguity(ITree tree, java.util.List<org.meta_environment.rascal.ast.SingleQuotedStrChar> alternatives) {
	this.alternatives = java.util.Collections.unmodifiableList(alternatives);
         this.tree = tree;
  }
  public java.util.List<org.meta_environment.rascal.ast.SingleQuotedStrChar> getAlternatives() {
	return alternatives;
  }
  
  public <T> T accept(IASTVisitor<T> v) {
     return v.visitSingleQuotedStrCharAmbiguity(this);
  }
} public abstract <T> T accept(IASTVisitor<T> visitor);
}