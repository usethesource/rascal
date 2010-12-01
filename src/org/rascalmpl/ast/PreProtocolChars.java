
package org.rascalmpl.ast;


import org.eclipse.imp.pdb.facts.INode;


public abstract class PreProtocolChars extends AbstractAST {
  public PreProtocolChars(INode node) {
    super(node);
  }
  


static public class Ambiguity extends PreProtocolChars {
  private final java.util.List<org.rascalmpl.ast.PreProtocolChars> alternatives;

  public Ambiguity(INode node, java.util.List<org.rascalmpl.ast.PreProtocolChars> alternatives) {
    super(node);
    this.alternatives = java.util.Collections.unmodifiableList(alternatives);
  }

  public java.util.List<org.rascalmpl.ast.PreProtocolChars> getAlternatives() {
   return alternatives;
  }

  public <T> T accept(IASTVisitor<T> v) {
	return v.visitPreProtocolCharsAmbiguity(this);
  }
}



 
static public class Lexical extends PreProtocolChars {
  private final java.lang.String string;
  public Lexical(INode node, java.lang.String string) {
    super(node);
    this.string = string;
  }
  public java.lang.String getString() {
    return string;
  }
  public <T> T accept(IASTVisitor<T> v) {
    return v.visitPreProtocolCharsLexical(this);
  }
}





}
