
package org.rascalmpl.ast;


import org.eclipse.imp.pdb.facts.INode;


public abstract class URLChars extends AbstractAST {
  public URLChars(INode node) {
    super(node);
  }
  


static public class Ambiguity extends URLChars {
  private final java.util.List<org.rascalmpl.ast.URLChars> alternatives;

  public Ambiguity(INode node, java.util.List<org.rascalmpl.ast.URLChars> alternatives) {
    super(node);
    this.alternatives = java.util.Collections.unmodifiableList(alternatives);
  }

  public java.util.List<org.rascalmpl.ast.URLChars> getAlternatives() {
   return alternatives;
  }

  public <T> T accept(IASTVisitor<T> v) {
	return v.visitURLCharsAmbiguity(this);
  }
}



 
static public class Lexical extends URLChars {
  private final java.lang.String string;
  public Lexical(INode node, java.lang.String string) {
    super(node);
    this.string = string;
  }
  public java.lang.String getString() {
    return string;
  }
  public <T> T accept(IASTVisitor<T> v) {
    return v.visitURLCharsLexical(this);
  }
}





}
