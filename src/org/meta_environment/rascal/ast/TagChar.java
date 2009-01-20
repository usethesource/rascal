package org.meta_environment.rascal.ast; 
import org.eclipse.imp.pdb.facts.ITree; 
public abstract class TagChar extends AbstractAST { 
  static public class Lexical extends TagChar {
	private String string;
	/*package*/ Lexical(ITree tree, String string) {
		this.tree = tree;
		this.string = string;
	}
	public String getString() {
		return string;
	}

 	public <T> T accept(IASTVisitor<T> v) {
     		return v.visitTagCharLexical(this);
  	}
} static public class Ambiguity extends TagChar {
  private final java.util.List<org.meta_environment.rascal.ast.TagChar> alternatives;
  public Ambiguity(ITree tree, java.util.List<org.meta_environment.rascal.ast.TagChar> alternatives) {
	this.alternatives = java.util.Collections.unmodifiableList(alternatives);
         this.tree = tree;
  }
  public java.util.List<org.meta_environment.rascal.ast.TagChar> getAlternatives() {
	return alternatives;
  }
  
  public <T> T accept(IASTVisitor<T> v) {
     return v.visitTagCharAmbiguity(this);
  }
} public abstract <T> T accept(IASTVisitor<T> visitor);
}