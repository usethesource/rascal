
package org.rascalmpl.ast;


import org.eclipse.imp.pdb.facts.INode;


public abstract class RegExp extends AbstractAST {
  public RegExp(INode node) {
    super(node);
  }
  


static public class Ambiguity extends RegExp {
  private final java.util.List<org.rascalmpl.ast.RegExp> alternatives;

  public Ambiguity(INode node, java.util.List<org.rascalmpl.ast.RegExp> alternatives) {
    super(node);
    this.alternatives = java.util.Collections.unmodifiableList(alternatives);
  }

  public java.util.List<org.rascalmpl.ast.RegExp> getAlternatives() {
   return alternatives;
  }

  public <T> T accept(IASTVisitor<T> v) {
	return v.visitRegExpAmbiguity(this);
  }
}



 
static public class Lexical extends RegExp {
  private final java.lang.String string;
  public Lexical(INode node, java.lang.String string) {
    super(node);
    this.string = string;
  }
  public java.lang.String getString() {
    return string;
  }
  public <T> T accept(IASTVisitor<T> v) {
    return v.visitRegExpLexical(this);
  }
}





}
