
package org.rascalmpl.ast;


import org.eclipse.imp.pdb.facts.INode;


public abstract class RealLiteral extends AbstractAST {
  public RealLiteral(INode node) {
    super(node);
  }
  


static public class Ambiguity extends RealLiteral {
  private final java.util.List<org.rascalmpl.ast.RealLiteral> alternatives;

  public Ambiguity(INode node, java.util.List<org.rascalmpl.ast.RealLiteral> alternatives) {
    super(node);
    this.alternatives = java.util.Collections.unmodifiableList(alternatives);
  }

  public java.util.List<org.rascalmpl.ast.RealLiteral> getAlternatives() {
   return alternatives;
  }

  public <T> T accept(IASTVisitor<T> v) {
	return v.visitRealLiteralAmbiguity(this);
  }
}



 
static public class Lexical extends RealLiteral {
  private final java.lang.String string;
  public Lexical(INode node, java.lang.String string) {
    super(node);
    this.string = string;
  }
  public java.lang.String getString() {
    return string;
  }
  public <T> T accept(IASTVisitor<T> v) {
    return v.visitRealLiteralLexical(this);
  }
}





}
