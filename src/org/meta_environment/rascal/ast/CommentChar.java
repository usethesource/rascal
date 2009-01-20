package org.meta_environment.rascal.ast; 
import org.eclipse.imp.pdb.facts.ITree; 
public abstract class CommentChar extends AbstractAST { 
  static public class Lexical extends CommentChar {
	private String string;
	/*package*/ Lexical(ITree tree, String string) {
		this.tree = tree;
		this.string = string;
	}
	public String getString() {
		return string;
	}

 	public <T> T accept(IASTVisitor<T> v) {
     		return v.visitCommentCharLexical(this);
  	}
} static public class Ambiguity extends CommentChar {
  private final java.util.List<org.meta_environment.rascal.ast.CommentChar> alternatives;
  public Ambiguity(ITree tree, java.util.List<org.meta_environment.rascal.ast.CommentChar> alternatives) {
	this.alternatives = java.util.Collections.unmodifiableList(alternatives);
         this.tree = tree;
  }
  public java.util.List<org.meta_environment.rascal.ast.CommentChar> getAlternatives() {
	return alternatives;
  }
  
  public <T> T accept(IASTVisitor<T> v) {
     return v.visitCommentCharAmbiguity(this);
  }
} public abstract <T> T accept(IASTVisitor<T> visitor);
}