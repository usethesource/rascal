
package org.rascalmpl.ast;


import org.eclipse.imp.pdb.facts.INode;


public abstract class RegExpLiteral extends AbstractAST {
  public RegExpLiteral(INode node) {
    super(node);
  }
  


static public class Ambiguity extends RegExpLiteral {
  private final java.util.List<org.rascalmpl.ast.RegExpLiteral> alternatives;

  public Ambiguity(INode node, java.util.List<org.rascalmpl.ast.RegExpLiteral> alternatives) {
    super(node);
    this.alternatives = java.util.Collections.unmodifiableList(alternatives);
  }

  public java.util.List<org.rascalmpl.ast.RegExpLiteral> getAlternatives() {
   return alternatives;
  }

  public <T> T accept(IASTVisitor<T> v) {
	return v.visitRegExpLiteralAmbiguity(this);
  }
}



 
static public class Lexical extends RegExpLiteral {
  private final java.lang.String string;
  public Lexical(INode node, java.lang.String string) {
    super(node);
    this.string = string;
  }
  public java.lang.String getString() {
    return string;
  }
  public <T> T accept(IASTVisitor<T> v) {
    return v.visitRegExpLiteralLexical(this);
  }
}





}
