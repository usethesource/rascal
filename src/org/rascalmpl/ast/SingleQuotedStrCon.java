
package org.rascalmpl.ast;


import org.eclipse.imp.pdb.facts.INode;


public abstract class SingleQuotedStrCon extends AbstractAST {
  public SingleQuotedStrCon(INode node) {
    super(node);
  }
  


static public class Ambiguity extends SingleQuotedStrCon {
  private final java.util.List<org.rascalmpl.ast.SingleQuotedStrCon> alternatives;

  public Ambiguity(INode node, java.util.List<org.rascalmpl.ast.SingleQuotedStrCon> alternatives) {
    super(node);
    this.alternatives = java.util.Collections.unmodifiableList(alternatives);
  }

  public java.util.List<org.rascalmpl.ast.SingleQuotedStrCon> getAlternatives() {
   return alternatives;
  }

  public <T> T accept(IASTVisitor<T> v) {
	return v.visitSingleQuotedStrConAmbiguity(this);
  }
}



 
static public class Lexical extends SingleQuotedStrCon {
  private final java.lang.String string;
  public Lexical(INode node, java.lang.String string) {
    super(node);
    this.string = string;
  }
  public java.lang.String getString() {
    return string;
  }
  public <T> T accept(IASTVisitor<T> v) {
    return v.visitSingleQuotedStrConLexical(this);
  }
}





}
