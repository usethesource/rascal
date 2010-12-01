
package org.rascalmpl.ast;


import org.eclipse.imp.pdb.facts.INode;


public abstract class PreStringChars extends AbstractAST {
  public PreStringChars(INode node) {
    super(node);
  }
  


static public class Ambiguity extends PreStringChars {
  private final java.util.List<org.rascalmpl.ast.PreStringChars> alternatives;

  public Ambiguity(INode node, java.util.List<org.rascalmpl.ast.PreStringChars> alternatives) {
    super(node);
    this.alternatives = java.util.Collections.unmodifiableList(alternatives);
  }

  public java.util.List<org.rascalmpl.ast.PreStringChars> getAlternatives() {
   return alternatives;
  }

  public <T> T accept(IASTVisitor<T> v) {
	return v.visitPreStringCharsAmbiguity(this);
  }
}



 
static public class Lexical extends PreStringChars {
  private final java.lang.String string;
  public Lexical(INode node, java.lang.String string) {
    super(node);
    this.string = string;
  }
  public java.lang.String getString() {
    return string;
  }
  public <T> T accept(IASTVisitor<T> v) {
    return v.visitPreStringCharsLexical(this);
  }
}





}
