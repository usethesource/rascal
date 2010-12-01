
package org.rascalmpl.ast;


import org.eclipse.imp.pdb.facts.INode;


public abstract class TagString extends AbstractAST {
  public TagString(INode node) {
    super(node);
  }
  


static public class Ambiguity extends TagString {
  private final java.util.List<org.rascalmpl.ast.TagString> alternatives;

  public Ambiguity(INode node, java.util.List<org.rascalmpl.ast.TagString> alternatives) {
    super(node);
    this.alternatives = java.util.Collections.unmodifiableList(alternatives);
  }

  public java.util.List<org.rascalmpl.ast.TagString> getAlternatives() {
   return alternatives;
  }

  public <T> T accept(IASTVisitor<T> v) {
	return v.visitTagStringAmbiguity(this);
  }
}



 
static public class Lexical extends TagString {
  private final java.lang.String string;
  public Lexical(INode node, java.lang.String string) {
    super(node);
    this.string = string;
  }
  public java.lang.String getString() {
    return string;
  }
  public <T> T accept(IASTVisitor<T> v) {
    return v.visitTagStringLexical(this);
  }
}





}
