package org.meta_environment.rascal.ast; 
import org.eclipse.imp.pdb.facts.ITree; 
public abstract class RegExp extends AbstractAST { 
  static public class Lexical extends RegExp {
	private String string;
	/*package*/ Lexical(ITree tree, String string) {
		this.tree = tree;
		this.string = string;
	}
	public String getString() {
		return string;
	}

 	public <T> T accept(IASTVisitor<T> v) {
     		return v.visitRegExpLexical(this);
  	}
} static public class Ambiguity extends RegExp {
  private final java.util.List<org.meta_environment.rascal.ast.RegExp> alternatives;
  public Ambiguity(java.util.List<org.meta_environment.rascal.ast.RegExp> alternatives) {
	this.alternatives = java.util.Collections.unmodifiableList(alternatives);
  }
  public java.util.List<org.meta_environment.rascal.ast.RegExp> getAlternatives() {
	return alternatives;
  }
  
  public <T> T accept(IASTVisitor<T> v) {
     return v.visitRegExpAmbiguity(this);
  }
} public abstract <T> T accept(IASTVisitor<T> visitor);
}