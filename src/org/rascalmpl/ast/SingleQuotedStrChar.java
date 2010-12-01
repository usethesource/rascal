
package org.rascalmpl.ast;


import org.eclipse.imp.pdb.facts.INode;


public abstract class SingleQuotedStrChar extends AbstractAST {
  public SingleQuotedStrChar(INode node) {
    super(node);
  }
  


static public class Ambiguity extends SingleQuotedStrChar {
  private final java.util.List<org.rascalmpl.ast.SingleQuotedStrChar> alternatives;

  public Ambiguity(INode node, java.util.List<org.rascalmpl.ast.SingleQuotedStrChar> alternatives) {
    super(node);
    this.alternatives = java.util.Collections.unmodifiableList(alternatives);
  }

  public java.util.List<org.rascalmpl.ast.SingleQuotedStrChar> getAlternatives() {
   return alternatives;
  }

  public <T> T accept(IASTVisitor<T> v) {
	return v.visitSingleQuotedStrCharAmbiguity(this);
  }
}



 
static public class Lexical extends SingleQuotedStrChar {
  private final java.lang.String string;
  public Lexical(INode node, java.lang.String string) {
    super(node);
    this.string = string;
  }
  public java.lang.String getString() {
    return string;
  }
  public <T> T accept(IASTVisitor<T> v) {
    return v.visitSingleQuotedStrCharLexical(this);
  }
}





}
