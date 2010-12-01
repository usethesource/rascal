
package org.rascalmpl.ast;


import org.eclipse.imp.pdb.facts.INode;


public abstract class HexLongLiteral extends AbstractAST {
  public HexLongLiteral(INode node) {
    super(node);
  }
  


static public class Ambiguity extends HexLongLiteral {
  private final java.util.List<org.rascalmpl.ast.HexLongLiteral> alternatives;

  public Ambiguity(INode node, java.util.List<org.rascalmpl.ast.HexLongLiteral> alternatives) {
    super(node);
    this.alternatives = java.util.Collections.unmodifiableList(alternatives);
  }

  public java.util.List<org.rascalmpl.ast.HexLongLiteral> getAlternatives() {
   return alternatives;
  }

  public <T> T accept(IASTVisitor<T> v) {
	return v.visitHexLongLiteralAmbiguity(this);
  }
}



 
static public class Lexical extends HexLongLiteral {
  private final java.lang.String string;
  public Lexical(INode node, java.lang.String string) {
    super(node);
    this.string = string;
  }
  public java.lang.String getString() {
    return string;
  }
  public <T> T accept(IASTVisitor<T> v) {
    return v.visitHexLongLiteralLexical(this);
  }
}





}
