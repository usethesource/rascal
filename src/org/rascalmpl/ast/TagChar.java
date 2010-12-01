
package org.rascalmpl.ast;


import org.eclipse.imp.pdb.facts.INode;


public abstract class TagChar extends AbstractAST {
  public TagChar(INode node) {
    super(node);
  }
  


static public class Ambiguity extends TagChar {
  private final java.util.List<org.rascalmpl.ast.TagChar> alternatives;

  public Ambiguity(INode node, java.util.List<org.rascalmpl.ast.TagChar> alternatives) {
    super(node);
    this.alternatives = java.util.Collections.unmodifiableList(alternatives);
  }

  public java.util.List<org.rascalmpl.ast.TagChar> getAlternatives() {
   return alternatives;
  }

  public <T> T accept(IASTVisitor<T> v) {
	return v.visitTagCharAmbiguity(this);
  }
}



 
static public class Lexical extends TagChar {
  private final java.lang.String string;
  public Lexical(INode node, java.lang.String string) {
    super(node);
    this.string = string;
  }
  public java.lang.String getString() {
    return string;
  }
  public <T> T accept(IASTVisitor<T> v) {
    return v.visitTagCharLexical(this);
  }
}





}
