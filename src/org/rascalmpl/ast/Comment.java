package org.rascalmpl.ast; 
import org.eclipse.imp.pdb.facts.INode; 
public abstract class Comment extends AbstractAST { 
  static public class Lexical extends Comment {
	private final String string;
         protected Lexical(INode node, String string) {
		this.node = node;
		this.string = string;
	}
	public String getString() {
		return string;
	}

 	public <T> T accept(IASTVisitor<T> v) {
     		return v.visitCommentLexical(this);
  	}
} static public class Ambiguity extends Comment {
  private final java.util.List<org.rascalmpl.ast.Comment> alternatives;
  protected Ambiguity(INode node, java.util.List<org.rascalmpl.ast.Comment> alternatives) {
	this.alternatives = java.util.Collections.unmodifiableList(alternatives);
         this.node = node;
  }
  public java.util.List<org.rascalmpl.ast.Comment> getAlternatives() {
	return alternatives;
  }
  
  public <T> T accept(IASTVisitor<T> v) {
     return v.visitCommentAmbiguity(this);
  }
} public abstract <T> T accept(IASTVisitor<T> visitor);
}