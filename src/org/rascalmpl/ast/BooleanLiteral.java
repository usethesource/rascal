
package org.rascalmpl.ast;


import org.eclipse.imp.pdb.facts.INode;


public abstract class BooleanLiteral extends AbstractAST {
  public BooleanLiteral(INode node) {
    super(node);
  }
  


static public class Ambiguity extends BooleanLiteral {
  private final java.util.List<org.rascalmpl.ast.BooleanLiteral> alternatives;

  public Ambiguity(INode node, java.util.List<org.rascalmpl.ast.BooleanLiteral> alternatives) {
    super(node);
    this.alternatives = java.util.Collections.unmodifiableList(alternatives);
  }

  public java.util.List<org.rascalmpl.ast.BooleanLiteral> getAlternatives() {
   return alternatives;
  }

  public <T> T accept(IASTVisitor<T> v) {
	return v.visitBooleanLiteralAmbiguity(this);
  }
}



 
static public class Lexical extends BooleanLiteral {
  private final java.lang.String string;
  public Lexical(INode node, java.lang.String string) {
    super(node);
    this.string = string;
  }
  public java.lang.String getString() {
    return string;
  }
  public <T> T accept(IASTVisitor<T> v) {
    return v.visitBooleanLiteralLexical(this);
  }
}





}
