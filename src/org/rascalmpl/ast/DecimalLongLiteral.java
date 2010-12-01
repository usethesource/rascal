
package org.rascalmpl.ast;


import org.eclipse.imp.pdb.facts.INode;


public abstract class DecimalLongLiteral extends AbstractAST {
  public DecimalLongLiteral(INode node) {
    super(node);
  }
  


static public class Ambiguity extends DecimalLongLiteral {
  private final java.util.List<org.rascalmpl.ast.DecimalLongLiteral> alternatives;

  public Ambiguity(INode node, java.util.List<org.rascalmpl.ast.DecimalLongLiteral> alternatives) {
    super(node);
    this.alternatives = java.util.Collections.unmodifiableList(alternatives);
  }

  public java.util.List<org.rascalmpl.ast.DecimalLongLiteral> getAlternatives() {
   return alternatives;
  }

  public <T> T accept(IASTVisitor<T> v) {
	return v.visitDecimalLongLiteralAmbiguity(this);
  }
}



 
static public class Lexical extends DecimalLongLiteral {
  private final java.lang.String string;
  public Lexical(INode node, java.lang.String string) {
    super(node);
    this.string = string;
  }
  public java.lang.String getString() {
    return string;
  }
  public <T> T accept(IASTVisitor<T> v) {
    return v.visitDecimalLongLiteralLexical(this);
  }
}





}
