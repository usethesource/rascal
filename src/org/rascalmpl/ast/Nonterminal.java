
package org.rascalmpl.ast;


import org.eclipse.imp.pdb.facts.INode;


public abstract class Nonterminal extends AbstractAST {
  public Nonterminal(INode node) {
    super(node);
  }
  


static public class Ambiguity extends Nonterminal {
  private final java.util.List<org.rascalmpl.ast.Nonterminal> alternatives;

  public Ambiguity(INode node, java.util.List<org.rascalmpl.ast.Nonterminal> alternatives) {
    super(node);
    this.alternatives = java.util.Collections.unmodifiableList(alternatives);
  }

  public java.util.List<org.rascalmpl.ast.Nonterminal> getAlternatives() {
   return alternatives;
  }

  public <T> T accept(IASTVisitor<T> v) {
	return v.visitNonterminalAmbiguity(this);
  }
}



 
static public class Lexical extends Nonterminal {
  private final java.lang.String string;
  public Lexical(INode node, java.lang.String string) {
    super(node);
    this.string = string;
  }
  public java.lang.String getString() {
    return string;
  }
  public <T> T accept(IASTVisitor<T> v) {
    return v.visitNonterminalLexical(this);
  }
}





}
