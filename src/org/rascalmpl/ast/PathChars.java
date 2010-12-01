
package org.rascalmpl.ast;


import org.eclipse.imp.pdb.facts.INode;


public abstract class PathChars extends AbstractAST {
  public PathChars(INode node) {
    super(node);
  }
  


static public class Ambiguity extends PathChars {
  private final java.util.List<org.rascalmpl.ast.PathChars> alternatives;

  public Ambiguity(INode node, java.util.List<org.rascalmpl.ast.PathChars> alternatives) {
    super(node);
    this.alternatives = java.util.Collections.unmodifiableList(alternatives);
  }

  public java.util.List<org.rascalmpl.ast.PathChars> getAlternatives() {
   return alternatives;
  }

  public <T> T accept(IASTVisitor<T> v) {
	return v.visitPathCharsAmbiguity(this);
  }
}



 
static public class Lexical extends PathChars {
  private final java.lang.String string;
  public Lexical(INode node, java.lang.String string) {
    super(node);
    this.string = string;
  }
  public java.lang.String getString() {
    return string;
  }
  public <T> T accept(IASTVisitor<T> v) {
    return v.visitPathCharsLexical(this);
  }
}





}
