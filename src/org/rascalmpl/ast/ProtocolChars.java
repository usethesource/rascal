
package org.rascalmpl.ast;


import org.eclipse.imp.pdb.facts.INode;


public abstract class ProtocolChars extends AbstractAST {
  public ProtocolChars(INode node) {
    super(node);
  }
  


static public class Ambiguity extends ProtocolChars {
  private final java.util.List<org.rascalmpl.ast.ProtocolChars> alternatives;

  public Ambiguity(INode node, java.util.List<org.rascalmpl.ast.ProtocolChars> alternatives) {
    super(node);
    this.alternatives = java.util.Collections.unmodifiableList(alternatives);
  }

  public java.util.List<org.rascalmpl.ast.ProtocolChars> getAlternatives() {
   return alternatives;
  }

  public <T> T accept(IASTVisitor<T> v) {
	return v.visitProtocolCharsAmbiguity(this);
  }
}



 
static public class Lexical extends ProtocolChars {
  private final java.lang.String string;
  public Lexical(INode node, java.lang.String string) {
    super(node);
    this.string = string;
  }
  public java.lang.String getString() {
    return string;
  }
  public <T> T accept(IASTVisitor<T> v) {
    return v.visitProtocolCharsLexical(this);
  }
}





}
