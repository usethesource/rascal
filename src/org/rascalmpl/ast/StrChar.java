
package org.rascalmpl.ast;


import org.eclipse.imp.pdb.facts.INode;


public abstract class StrChar extends AbstractAST {
  public StrChar(INode node) {
    super(node);
  }
  


static public class Ambiguity extends StrChar {
  private final java.util.List<org.rascalmpl.ast.StrChar> alternatives;

  public Ambiguity(INode node, java.util.List<org.rascalmpl.ast.StrChar> alternatives) {
    super(node);
    this.alternatives = java.util.Collections.unmodifiableList(alternatives);
  }

  public java.util.List<org.rascalmpl.ast.StrChar> getAlternatives() {
   return alternatives;
  }

  public <T> T accept(IASTVisitor<T> v) {
	return v.visitStrCharAmbiguity(this);
  }
}



 
static public class Lexical extends StrChar {
  private final java.lang.String string;
  public Lexical(INode node, java.lang.String string) {
    super(node);
    this.string = string;
  }
  public java.lang.String getString() {
    return string;
  }
  public <T> T accept(IASTVisitor<T> v) {
    return v.visitStrCharLexical(this);
  }
}





}
