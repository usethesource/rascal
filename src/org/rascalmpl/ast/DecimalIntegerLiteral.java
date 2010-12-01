
package org.rascalmpl.ast;


import org.eclipse.imp.pdb.facts.INode;


public abstract class DecimalIntegerLiteral extends AbstractAST {
  public DecimalIntegerLiteral(INode node) {
    super(node);
  }
  


static public class Ambiguity extends DecimalIntegerLiteral {
  private final java.util.List<org.rascalmpl.ast.DecimalIntegerLiteral> alternatives;

  public Ambiguity(INode node, java.util.List<org.rascalmpl.ast.DecimalIntegerLiteral> alternatives) {
    super(node);
    this.alternatives = java.util.Collections.unmodifiableList(alternatives);
  }

  public java.util.List<org.rascalmpl.ast.DecimalIntegerLiteral> getAlternatives() {
   return alternatives;
  }

  public <T> T accept(IASTVisitor<T> v) {
	return v.visitDecimalIntegerLiteralAmbiguity(this);
  }
}



 
static public class Lexical extends DecimalIntegerLiteral {
  private final java.lang.String string;
  public Lexical(INode node, java.lang.String string) {
    super(node);
    this.string = string;
  }
  public java.lang.String getString() {
    return string;
  }
  public <T> T accept(IASTVisitor<T> v) {
    return v.visitDecimalIntegerLiteralLexical(this);
  }
}





}
