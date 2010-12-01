
package org.rascalmpl.ast;


import org.eclipse.imp.pdb.facts.INode;


public abstract class ParameterizedNonterminal extends AbstractAST {
  public ParameterizedNonterminal(INode node) {
    super(node);
  }
  


static public class Ambiguity extends ParameterizedNonterminal {
  private final java.util.List<org.rascalmpl.ast.ParameterizedNonterminal> alternatives;

  public Ambiguity(INode node, java.util.List<org.rascalmpl.ast.ParameterizedNonterminal> alternatives) {
    super(node);
    this.alternatives = java.util.Collections.unmodifiableList(alternatives);
  }

  public java.util.List<org.rascalmpl.ast.ParameterizedNonterminal> getAlternatives() {
   return alternatives;
  }

  public <T> T accept(IASTVisitor<T> v) {
	return v.visitParameterizedNonterminalAmbiguity(this);
  }
}



 
static public class Lexical extends ParameterizedNonterminal {
  private final java.lang.String string;
  public Lexical(INode node, java.lang.String string) {
    super(node);
    this.string = string;
  }
  public java.lang.String getString() {
    return string;
  }
  public <T> T accept(IASTVisitor<T> v) {
    return v.visitParameterizedNonterminalLexical(this);
  }
}





}
