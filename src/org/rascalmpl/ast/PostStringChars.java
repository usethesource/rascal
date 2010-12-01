
package org.rascalmpl.ast;


import org.eclipse.imp.pdb.facts.INode;


public abstract class PostStringChars extends AbstractAST {
  public PostStringChars(INode node) {
    super(node);
  }
  


static public class Ambiguity extends PostStringChars {
  private final java.util.List<org.rascalmpl.ast.PostStringChars> alternatives;

  public Ambiguity(INode node, java.util.List<org.rascalmpl.ast.PostStringChars> alternatives) {
    super(node);
    this.alternatives = java.util.Collections.unmodifiableList(alternatives);
  }

  public java.util.List<org.rascalmpl.ast.PostStringChars> getAlternatives() {
   return alternatives;
  }

  public <T> T accept(IASTVisitor<T> v) {
	return v.visitPostStringCharsAmbiguity(this);
  }
}



 
static public class Lexical extends PostStringChars {
  private final java.lang.String string;
  public Lexical(INode node, java.lang.String string) {
    super(node);
    this.string = string;
  }
  public java.lang.String getString() {
    return string;
  }
  public <T> T accept(IASTVisitor<T> v) {
    return v.visitPostStringCharsLexical(this);
  }
}





}
