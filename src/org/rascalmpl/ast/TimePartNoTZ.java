
package org.rascalmpl.ast;


import org.eclipse.imp.pdb.facts.INode;


public abstract class TimePartNoTZ extends AbstractAST {
  public TimePartNoTZ(INode node) {
    super(node);
  }
  


static public class Ambiguity extends TimePartNoTZ {
  private final java.util.List<org.rascalmpl.ast.TimePartNoTZ> alternatives;

  public Ambiguity(INode node, java.util.List<org.rascalmpl.ast.TimePartNoTZ> alternatives) {
    super(node);
    this.alternatives = java.util.Collections.unmodifiableList(alternatives);
  }

  public java.util.List<org.rascalmpl.ast.TimePartNoTZ> getAlternatives() {
   return alternatives;
  }

  public <T> T accept(IASTVisitor<T> v) {
	return v.visitTimePartNoTZAmbiguity(this);
  }
}



 
static public class Lexical extends TimePartNoTZ {
  private final java.lang.String string;
  public Lexical(INode node, java.lang.String string) {
    super(node);
    this.string = string;
  }
  public java.lang.String getString() {
    return string;
  }
  public <T> T accept(IASTVisitor<T> v) {
    return v.visitTimePartNoTZLexical(this);
  }
}





}
