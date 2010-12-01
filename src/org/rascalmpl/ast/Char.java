
package org.rascalmpl.ast;


import org.eclipse.imp.pdb.facts.INode;


public abstract class Char extends AbstractAST {
  public Char(INode node) {
    super(node);
  }
  


static public class Ambiguity extends Char {
  private final java.util.List<org.rascalmpl.ast.Char> alternatives;

  public Ambiguity(INode node, java.util.List<org.rascalmpl.ast.Char> alternatives) {
    super(node);
    this.alternatives = java.util.Collections.unmodifiableList(alternatives);
  }

  public java.util.List<org.rascalmpl.ast.Char> getAlternatives() {
   return alternatives;
  }

  public <T> T accept(IASTVisitor<T> v) {
	return v.visitCharAmbiguity(this);
  }
}



 
static public class Lexical extends Char {
  private final java.lang.String string;
  public Lexical(INode node, java.lang.String string) {
    super(node);
    this.string = string;
  }
  public java.lang.String getString() {
    return string;
  }
  public <T> T accept(IASTVisitor<T> v) {
    return v.visitCharLexical(this);
  }
}





}
