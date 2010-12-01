
package org.rascalmpl.ast;


import org.eclipse.imp.pdb.facts.INode;


public abstract class UnicodeEscape extends AbstractAST {
  public UnicodeEscape(INode node) {
    super(node);
  }
  


static public class Ambiguity extends UnicodeEscape {
  private final java.util.List<org.rascalmpl.ast.UnicodeEscape> alternatives;

  public Ambiguity(INode node, java.util.List<org.rascalmpl.ast.UnicodeEscape> alternatives) {
    super(node);
    this.alternatives = java.util.Collections.unmodifiableList(alternatives);
  }

  public java.util.List<org.rascalmpl.ast.UnicodeEscape> getAlternatives() {
   return alternatives;
  }

  public <T> T accept(IASTVisitor<T> v) {
	return v.visitUnicodeEscapeAmbiguity(this);
  }
}



 
static public class Lexical extends UnicodeEscape {
  private final java.lang.String string;
  public Lexical(INode node, java.lang.String string) {
    super(node);
    this.string = string;
  }
  public java.lang.String getString() {
    return string;
  }
  public <T> T accept(IASTVisitor<T> v) {
    return v.visitUnicodeEscapeLexical(this);
  }
}





}
