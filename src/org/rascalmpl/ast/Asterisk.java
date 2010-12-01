
package org.rascalmpl.ast;


import org.eclipse.imp.pdb.facts.INode;


public abstract class Asterisk extends AbstractAST {
  public Asterisk(INode node) {
    super(node);
  }
  


static public class Ambiguity extends Asterisk {
  private final java.util.List<org.rascalmpl.ast.Asterisk> alternatives;

  public Ambiguity(INode node, java.util.List<org.rascalmpl.ast.Asterisk> alternatives) {
    super(node);
    this.alternatives = java.util.Collections.unmodifiableList(alternatives);
  }

  public java.util.List<org.rascalmpl.ast.Asterisk> getAlternatives() {
   return alternatives;
  }

  public <T> T accept(IASTVisitor<T> v) {
	return v.visitAsteriskAmbiguity(this);
  }
}



 
static public class Lexical extends Asterisk {
  private final java.lang.String string;
  public Lexical(INode node, java.lang.String string) {
    super(node);
    this.string = string;
  }
  public java.lang.String getString() {
    return string;
  }
  public <T> T accept(IASTVisitor<T> v) {
    return v.visitAsteriskLexical(this);
  }
}





}
