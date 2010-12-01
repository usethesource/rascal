
package org.rascalmpl.ast;


import org.eclipse.imp.pdb.facts.INode;


public abstract class EscapedName extends AbstractAST {
  public EscapedName(INode node) {
    super(node);
  }
  


static public class Ambiguity extends EscapedName {
  private final java.util.List<org.rascalmpl.ast.EscapedName> alternatives;

  public Ambiguity(INode node, java.util.List<org.rascalmpl.ast.EscapedName> alternatives) {
    super(node);
    this.alternatives = java.util.Collections.unmodifiableList(alternatives);
  }

  public java.util.List<org.rascalmpl.ast.EscapedName> getAlternatives() {
   return alternatives;
  }

  public <T> T accept(IASTVisitor<T> v) {
	return v.visitEscapedNameAmbiguity(this);
  }
}



 
static public class Lexical extends EscapedName {
  private final java.lang.String string;
  public Lexical(INode node, java.lang.String string) {
    super(node);
    this.string = string;
  }
  public java.lang.String getString() {
    return string;
  }
  public <T> T accept(IASTVisitor<T> v) {
    return v.visitEscapedNameLexical(this);
  }
}





}
