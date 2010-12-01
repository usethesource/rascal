
package org.rascalmpl.ast;


import org.eclipse.imp.pdb.facts.INode;


public abstract class HexIntegerLiteral extends AbstractAST {
  public HexIntegerLiteral(INode node) {
    super(node);
  }
  


static public class Ambiguity extends HexIntegerLiteral {
  private final java.util.List<org.rascalmpl.ast.HexIntegerLiteral> alternatives;

  public Ambiguity(INode node, java.util.List<org.rascalmpl.ast.HexIntegerLiteral> alternatives) {
    super(node);
    this.alternatives = java.util.Collections.unmodifiableList(alternatives);
  }

  public java.util.List<org.rascalmpl.ast.HexIntegerLiteral> getAlternatives() {
   return alternatives;
  }

  public <T> T accept(IASTVisitor<T> v) {
	return v.visitHexIntegerLiteralAmbiguity(this);
  }
}



 
static public class Lexical extends HexIntegerLiteral {
  private final java.lang.String string;
  public Lexical(INode node, java.lang.String string) {
    super(node);
    this.string = string;
  }
  public java.lang.String getString() {
    return string;
  }
  public <T> T accept(IASTVisitor<T> v) {
    return v.visitHexIntegerLiteralLexical(this);
  }
}





}
