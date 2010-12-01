
package org.rascalmpl.ast;


import org.eclipse.imp.pdb.facts.INode;


public abstract class OctalIntegerLiteral extends AbstractAST {
  public OctalIntegerLiteral(INode node) {
    super(node);
  }
  


static public class Ambiguity extends OctalIntegerLiteral {
  private final java.util.List<org.rascalmpl.ast.OctalIntegerLiteral> alternatives;

  public Ambiguity(INode node, java.util.List<org.rascalmpl.ast.OctalIntegerLiteral> alternatives) {
    super(node);
    this.alternatives = java.util.Collections.unmodifiableList(alternatives);
  }

  public java.util.List<org.rascalmpl.ast.OctalIntegerLiteral> getAlternatives() {
   return alternatives;
  }

  public <T> T accept(IASTVisitor<T> v) {
	return v.visitOctalIntegerLiteralAmbiguity(this);
  }
}



 
static public class Lexical extends OctalIntegerLiteral {
  private final java.lang.String string;
  public Lexical(INode node, java.lang.String string) {
    super(node);
    this.string = string;
  }
  public java.lang.String getString() {
    return string;
  }
  public <T> T accept(IASTVisitor<T> v) {
    return v.visitOctalIntegerLiteralLexical(this);
  }
}





}
