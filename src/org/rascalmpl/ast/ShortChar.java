
package org.rascalmpl.ast;


import org.eclipse.imp.pdb.facts.INode;


public abstract class ShortChar extends AbstractAST {
  public ShortChar(INode node) {
    super(node);
  }
  


static public class Ambiguity extends ShortChar {
  private final java.util.List<org.rascalmpl.ast.ShortChar> alternatives;

  public Ambiguity(INode node, java.util.List<org.rascalmpl.ast.ShortChar> alternatives) {
    super(node);
    this.alternatives = java.util.Collections.unmodifiableList(alternatives);
  }

  public java.util.List<org.rascalmpl.ast.ShortChar> getAlternatives() {
   return alternatives;
  }

  public <T> T accept(IASTVisitor<T> v) {
	return v.visitShortCharAmbiguity(this);
  }
}



 
static public class Lexical extends ShortChar {
  private final java.lang.String string;
  public Lexical(INode node, java.lang.String string) {
    super(node);
    this.string = string;
  }
  public java.lang.String getString() {
    return string;
  }
  public <T> T accept(IASTVisitor<T> v) {
    return v.visitShortCharLexical(this);
  }
}





}
