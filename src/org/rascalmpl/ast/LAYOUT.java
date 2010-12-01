
package org.rascalmpl.ast;


import org.eclipse.imp.pdb.facts.INode;


public abstract class LAYOUT extends AbstractAST {
  public LAYOUT(INode node) {
    super(node);
  }
  


static public class Ambiguity extends LAYOUT {
  private final java.util.List<org.rascalmpl.ast.LAYOUT> alternatives;

  public Ambiguity(INode node, java.util.List<org.rascalmpl.ast.LAYOUT> alternatives) {
    super(node);
    this.alternatives = java.util.Collections.unmodifiableList(alternatives);
  }

  public java.util.List<org.rascalmpl.ast.LAYOUT> getAlternatives() {
   return alternatives;
  }

  public <T> T accept(IASTVisitor<T> v) {
	return v.visitLAYOUTAmbiguity(this);
  }
}



 
static public class Lexical extends LAYOUT {
  private final java.lang.String string;
  public Lexical(INode node, java.lang.String string) {
    super(node);
    this.string = string;
  }
  public java.lang.String getString() {
    return string;
  }
  public <T> T accept(IASTVisitor<T> v) {
    return v.visitLAYOUTLexical(this);
  }
}





}
