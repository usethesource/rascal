
package org.rascalmpl.ast;


import org.eclipse.imp.pdb.facts.INode;


public abstract class OctalEscapeSequence extends AbstractAST {
  public OctalEscapeSequence(INode node) {
    super(node);
  }
  


static public class Ambiguity extends OctalEscapeSequence {
  private final java.util.List<org.rascalmpl.ast.OctalEscapeSequence> alternatives;

  public Ambiguity(INode node, java.util.List<org.rascalmpl.ast.OctalEscapeSequence> alternatives) {
    super(node);
    this.alternatives = java.util.Collections.unmodifiableList(alternatives);
  }

  public java.util.List<org.rascalmpl.ast.OctalEscapeSequence> getAlternatives() {
   return alternatives;
  }

  public <T> T accept(IASTVisitor<T> v) {
	return v.visitOctalEscapeSequenceAmbiguity(this);
  }
}



 
static public class Lexical extends OctalEscapeSequence {
  private final java.lang.String string;
  public Lexical(INode node, java.lang.String string) {
    super(node);
    this.string = string;
  }
  public java.lang.String getString() {
    return string;
  }
  public <T> T accept(IASTVisitor<T> v) {
    return v.visitOctalEscapeSequenceLexical(this);
  }
}





}
