
package org.rascalmpl.ast;


import org.eclipse.imp.pdb.facts.INode;


public abstract class Word extends AbstractAST {
  public Word(INode node) {
    super(node);
  }
  


static public class Ambiguity extends Word {
  private final java.util.List<org.rascalmpl.ast.Word> alternatives;

  public Ambiguity(INode node, java.util.List<org.rascalmpl.ast.Word> alternatives) {
    super(node);
    this.alternatives = java.util.Collections.unmodifiableList(alternatives);
  }

  public java.util.List<org.rascalmpl.ast.Word> getAlternatives() {
   return alternatives;
  }

  public <T> T accept(IASTVisitor<T> v) {
	return v.visitWordAmbiguity(this);
  }
}



 
static public class Lexical extends Word {
  private final java.lang.String string;
  public Lexical(INode node, java.lang.String string) {
    super(node);
    this.string = string;
  }
  public java.lang.String getString() {
    return string;
  }
  public <T> T accept(IASTVisitor<T> v) {
    return v.visitWordLexical(this);
  }
}





}
