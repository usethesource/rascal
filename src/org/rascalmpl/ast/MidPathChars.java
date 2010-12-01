
package org.rascalmpl.ast;


import org.eclipse.imp.pdb.facts.INode;


public abstract class MidPathChars extends AbstractAST {
  public MidPathChars(INode node) {
    super(node);
  }
  


static public class Ambiguity extends MidPathChars {
  private final java.util.List<org.rascalmpl.ast.MidPathChars> alternatives;

  public Ambiguity(INode node, java.util.List<org.rascalmpl.ast.MidPathChars> alternatives) {
    super(node);
    this.alternatives = java.util.Collections.unmodifiableList(alternatives);
  }

  public java.util.List<org.rascalmpl.ast.MidPathChars> getAlternatives() {
   return alternatives;
  }

  public <T> T accept(IASTVisitor<T> v) {
	return v.visitMidPathCharsAmbiguity(this);
  }
}



 
static public class Lexical extends MidPathChars {
  private final java.lang.String string;
  public Lexical(INode node, java.lang.String string) {
    super(node);
    this.string = string;
  }
  public java.lang.String getString() {
    return string;
  }
  public <T> T accept(IASTVisitor<T> v) {
    return v.visitMidPathCharsLexical(this);
  }
}





}
