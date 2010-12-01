
package org.rascalmpl.ast;


import org.eclipse.imp.pdb.facts.INode;


public abstract class StringCharacter extends AbstractAST {
  public StringCharacter(INode node) {
    super(node);
  }
  


static public class Ambiguity extends StringCharacter {
  private final java.util.List<org.rascalmpl.ast.StringCharacter> alternatives;

  public Ambiguity(INode node, java.util.List<org.rascalmpl.ast.StringCharacter> alternatives) {
    super(node);
    this.alternatives = java.util.Collections.unmodifiableList(alternatives);
  }

  public java.util.List<org.rascalmpl.ast.StringCharacter> getAlternatives() {
   return alternatives;
  }

  public <T> T accept(IASTVisitor<T> v) {
	return v.visitStringCharacterAmbiguity(this);
  }
}



 
static public class Lexical extends StringCharacter {
  private final java.lang.String string;
  public Lexical(INode node, java.lang.String string) {
    super(node);
    this.string = string;
  }
  public java.lang.String getString() {
    return string;
  }
  public <T> T accept(IASTVisitor<T> v) {
    return v.visitStringCharacterLexical(this);
  }
}





}
