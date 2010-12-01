
package org.rascalmpl.ast;


import org.eclipse.imp.pdb.facts.INode;


public abstract class NamedRegExp extends AbstractAST {
  public NamedRegExp(INode node) {
    super(node);
  }
  


static public class Ambiguity extends NamedRegExp {
  private final java.util.List<org.rascalmpl.ast.NamedRegExp> alternatives;

  public Ambiguity(INode node, java.util.List<org.rascalmpl.ast.NamedRegExp> alternatives) {
    super(node);
    this.alternatives = java.util.Collections.unmodifiableList(alternatives);
  }

  public java.util.List<org.rascalmpl.ast.NamedRegExp> getAlternatives() {
   return alternatives;
  }

  public <T> T accept(IASTVisitor<T> v) {
	return v.visitNamedRegExpAmbiguity(this);
  }
}



 
static public class Lexical extends NamedRegExp {
  private final java.lang.String string;
  public Lexical(INode node, java.lang.String string) {
    super(node);
    this.string = string;
  }
  public java.lang.String getString() {
    return string;
  }
  public <T> T accept(IASTVisitor<T> v) {
    return v.visitNamedRegExpLexical(this);
  }
}





}
