
package org.rascalmpl.ast;


import org.eclipse.imp.pdb.facts.INode;


public abstract class MidProtocolChars extends AbstractAST {
  public MidProtocolChars(INode node) {
    super(node);
  }
  


static public class Ambiguity extends MidProtocolChars {
  private final java.util.List<org.rascalmpl.ast.MidProtocolChars> alternatives;

  public Ambiguity(INode node, java.util.List<org.rascalmpl.ast.MidProtocolChars> alternatives) {
    super(node);
    this.alternatives = java.util.Collections.unmodifiableList(alternatives);
  }

  public java.util.List<org.rascalmpl.ast.MidProtocolChars> getAlternatives() {
   return alternatives;
  }

  public <T> T accept(IASTVisitor<T> v) {
	return v.visitMidProtocolCharsAmbiguity(this);
  }
}



 
static public class Lexical extends MidProtocolChars {
  private final java.lang.String string;
  public Lexical(INode node, java.lang.String string) {
    super(node);
    this.string = string;
  }
  public java.lang.String getString() {
    return string;
  }
  public <T> T accept(IASTVisitor<T> v) {
    return v.visitMidProtocolCharsLexical(this);
  }
}





}
