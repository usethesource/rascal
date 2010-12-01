
package org.rascalmpl.ast;


import org.eclipse.imp.pdb.facts.INode;


public abstract class MidStringChars extends AbstractAST {
  public MidStringChars(INode node) {
    super(node);
  }
  


static public class Ambiguity extends MidStringChars {
  private final java.util.List<org.rascalmpl.ast.MidStringChars> alternatives;

  public Ambiguity(INode node, java.util.List<org.rascalmpl.ast.MidStringChars> alternatives) {
    super(node);
    this.alternatives = java.util.Collections.unmodifiableList(alternatives);
  }

  public java.util.List<org.rascalmpl.ast.MidStringChars> getAlternatives() {
   return alternatives;
  }

  public <T> T accept(IASTVisitor<T> v) {
	return v.visitMidStringCharsAmbiguity(this);
  }
}



 
static public class Lexical extends MidStringChars {
  private final java.lang.String string;
  public Lexical(INode node, java.lang.String string) {
    super(node);
    this.string = string;
  }
  public java.lang.String getString() {
    return string;
  }
  public <T> T accept(IASTVisitor<T> v) {
    return v.visitMidStringCharsLexical(this);
  }
}





}
