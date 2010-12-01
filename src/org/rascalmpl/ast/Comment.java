
package org.rascalmpl.ast;


import org.eclipse.imp.pdb.facts.INode;


public abstract class Comment extends AbstractAST {
  public Comment(INode node) {
    super(node);
  }
  


static public class Ambiguity extends Comment {
  private final java.util.List<org.rascalmpl.ast.Comment> alternatives;

  public Ambiguity(INode node, java.util.List<org.rascalmpl.ast.Comment> alternatives) {
    super(node);
    this.alternatives = java.util.Collections.unmodifiableList(alternatives);
  }

  public java.util.List<org.rascalmpl.ast.Comment> getAlternatives() {
   return alternatives;
  }

  public <T> T accept(IASTVisitor<T> v) {
	return v.visitCommentAmbiguity(this);
  }
}



 
static public class Lexical extends Comment {
  private final java.lang.String string;
  public Lexical(INode node, java.lang.String string) {
    super(node);
    this.string = string;
  }
  public java.lang.String getString() {
    return string;
  }
  public <T> T accept(IASTVisitor<T> v) {
    return v.visitCommentLexical(this);
  }
}





}
