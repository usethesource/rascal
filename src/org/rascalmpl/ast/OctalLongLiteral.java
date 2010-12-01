
package org.rascalmpl.ast;


import org.eclipse.imp.pdb.facts.INode;


public abstract class OctalLongLiteral extends AbstractAST {
  public OctalLongLiteral(INode node) {
    super(node);
  }
  


static public class Ambiguity extends OctalLongLiteral {
  private final java.util.List<org.rascalmpl.ast.OctalLongLiteral> alternatives;

  public Ambiguity(INode node, java.util.List<org.rascalmpl.ast.OctalLongLiteral> alternatives) {
    super(node);
    this.alternatives = java.util.Collections.unmodifiableList(alternatives);
  }

  public java.util.List<org.rascalmpl.ast.OctalLongLiteral> getAlternatives() {
   return alternatives;
  }

  public <T> T accept(IASTVisitor<T> v) {
	return v.visitOctalLongLiteralAmbiguity(this);
  }
}



 
static public class Lexical extends OctalLongLiteral {
  private final java.lang.String string;
  public Lexical(INode node, java.lang.String string) {
    super(node);
    this.string = string;
  }
  public java.lang.String getString() {
    return string;
  }
  public <T> T accept(IASTVisitor<T> v) {
    return v.visitOctalLongLiteralLexical(this);
  }
}





}
