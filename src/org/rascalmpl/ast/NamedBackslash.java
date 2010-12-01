
package org.rascalmpl.ast;


import org.eclipse.imp.pdb.facts.INode;


public abstract class NamedBackslash extends AbstractAST {
  public NamedBackslash(INode node) {
    super(node);
  }
  


static public class Ambiguity extends NamedBackslash {
  private final java.util.List<org.rascalmpl.ast.NamedBackslash> alternatives;

  public Ambiguity(INode node, java.util.List<org.rascalmpl.ast.NamedBackslash> alternatives) {
    super(node);
    this.alternatives = java.util.Collections.unmodifiableList(alternatives);
  }

  public java.util.List<org.rascalmpl.ast.NamedBackslash> getAlternatives() {
   return alternatives;
  }

  public <T> T accept(IASTVisitor<T> v) {
	return v.visitNamedBackslashAmbiguity(this);
  }
}



 
static public class Lexical extends NamedBackslash {
  private final java.lang.String string;
  public Lexical(INode node, java.lang.String string) {
    super(node);
    this.string = string;
  }
  public java.lang.String getString() {
    return string;
  }
  public <T> T accept(IASTVisitor<T> v) {
    return v.visitNamedBackslashLexical(this);
  }
}





}
