
package org.rascalmpl.ast;


import org.eclipse.imp.pdb.facts.INode;


public abstract class PostPathChars extends AbstractAST {
  public PostPathChars(INode node) {
    super(node);
  }
  


static public class Ambiguity extends PostPathChars {
  private final java.util.List<org.rascalmpl.ast.PostPathChars> alternatives;

  public Ambiguity(INode node, java.util.List<org.rascalmpl.ast.PostPathChars> alternatives) {
    super(node);
    this.alternatives = java.util.Collections.unmodifiableList(alternatives);
  }

  public java.util.List<org.rascalmpl.ast.PostPathChars> getAlternatives() {
   return alternatives;
  }

  public <T> T accept(IASTVisitor<T> v) {
	return v.visitPostPathCharsAmbiguity(this);
  }
}



 
static public class Lexical extends PostPathChars {
  private final java.lang.String string;
  public Lexical(INode node, java.lang.String string) {
    super(node);
    this.string = string;
  }
  public java.lang.String getString() {
    return string;
  }
  public <T> T accept(IASTVisitor<T> v) {
    return v.visitPostPathCharsLexical(this);
  }
}





}
