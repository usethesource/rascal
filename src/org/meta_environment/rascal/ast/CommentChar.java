package org.meta_environment.rascal.ast; 
import org.eclipse.imp.pdb.facts.INode; 
public abstract class CommentChar extends AbstractAST { 
  static public class Lexical extends CommentChar {
	private String string;
         public Lexical(INode node, String string) {
		this.node = node;
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
  public Ambiguity(INode node, java.util.List<org.meta_environment.rascal.ast.CommentChar> alternatives) {
	this.alternatives = java.util.Collections.unmodifiableList(alternatives);
         this.node = node;
  }
  public java.util.List<org.meta_environment.rascal.ast.CommentChar> getAlternatives() {
	return alternatives;
  }
  
  public <T> T accept(IASTVisitor<T> v) {
     return v.visitCommentCharAmbiguity(this);
  }
} public abstract <T> T accept(IASTVisitor<T> visitor);
}