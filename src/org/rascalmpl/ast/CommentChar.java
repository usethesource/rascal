
package org.rascalmpl.ast;


import org.eclipse.imp.pdb.facts.INode;


public abstract class CommentChar extends AbstractAST {
  public CommentChar(INode node) {
    super(node);
  }
  


static public class Ambiguity extends CommentChar {
  private final java.util.List<org.rascalmpl.ast.CommentChar> alternatives;

  public Ambiguity(INode node, java.util.List<org.rascalmpl.ast.CommentChar> alternatives) {
    super(node);
    this.alternatives = java.util.Collections.unmodifiableList(alternatives);
  }

  public java.util.List<org.rascalmpl.ast.CommentChar> getAlternatives() {
   return alternatives;
  }

  public <T> T accept(IASTVisitor<T> v) {
	return v.visitCommentCharAmbiguity(this);
  }
}



 
static public class Lexical extends CommentChar {
  private final java.lang.String string;
  public Lexical(INode node, java.lang.String string) {
    super(node);
    this.string = string;
  }
  public java.lang.String getString() {
    return string;
  }
  public <T> T accept(IASTVisitor<T> v) {
    return v.visitCommentCharLexical(this);
  }
}





}
