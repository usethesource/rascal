
package org.rascalmpl.ast;


import org.eclipse.imp.pdb.facts.INode;


public abstract class PostProtocolChars extends AbstractAST {
  public PostProtocolChars(INode node) {
    super(node);
  }
  


static public class Ambiguity extends PostProtocolChars {
  private final java.util.List<org.rascalmpl.ast.PostProtocolChars> alternatives;

  public Ambiguity(INode node, java.util.List<org.rascalmpl.ast.PostProtocolChars> alternatives) {
    super(node);
    this.alternatives = java.util.Collections.unmodifiableList(alternatives);
  }

  public java.util.List<org.rascalmpl.ast.PostProtocolChars> getAlternatives() {
   return alternatives;
  }

  public <T> T accept(IASTVisitor<T> v) {
	return v.visitPostProtocolCharsAmbiguity(this);
  }
}



 
static public class Lexical extends PostProtocolChars {
  private final java.lang.String string;
  public Lexical(INode node, java.lang.String string) {
    super(node);
    this.string = string;
  }
  public java.lang.String getString() {
    return string;
  }
  public <T> T accept(IASTVisitor<T> v) {
    return v.visitPostProtocolCharsLexical(this);
  }
}





}
