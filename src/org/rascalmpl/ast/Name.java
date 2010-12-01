
package org.rascalmpl.ast;


import org.eclipse.imp.pdb.facts.INode;


public abstract class Name extends AbstractAST {
  public Name(INode node) {
    super(node);
  }
  


static public class Ambiguity extends Name {
  private final java.util.List<org.rascalmpl.ast.Name> alternatives;

  public Ambiguity(INode node, java.util.List<org.rascalmpl.ast.Name> alternatives) {
    super(node);
    this.alternatives = java.util.Collections.unmodifiableList(alternatives);
  }

  public java.util.List<org.rascalmpl.ast.Name> getAlternatives() {
   return alternatives;
  }

  public <T> T accept(IASTVisitor<T> v) {
	return v.visitNameAmbiguity(this);
  }
}



 
static public class Lexical extends Name {
  private final java.lang.String string;
  public Lexical(INode node, java.lang.String string) {
    super(node);
    this.string = string;
  }
  public java.lang.String getString() {
    return string;
  }
  public <T> T accept(IASTVisitor<T> v) {
    return v.visitNameLexical(this);
  }
}





}
