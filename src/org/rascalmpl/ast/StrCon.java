
package org.rascalmpl.ast;


import org.eclipse.imp.pdb.facts.INode;


public abstract class StrCon extends AbstractAST {
  public StrCon(INode node) {
    super(node);
  }
  


static public class Ambiguity extends StrCon {
  private final java.util.List<org.rascalmpl.ast.StrCon> alternatives;

  public Ambiguity(INode node, java.util.List<org.rascalmpl.ast.StrCon> alternatives) {
    super(node);
    this.alternatives = java.util.Collections.unmodifiableList(alternatives);
  }

  public java.util.List<org.rascalmpl.ast.StrCon> getAlternatives() {
   return alternatives;
  }

  public <T> T accept(IASTVisitor<T> v) {
	return v.visitStrConAmbiguity(this);
  }
}



 
static public class Lexical extends StrCon {
  private final java.lang.String string;
  public Lexical(INode node, java.lang.String string) {
    super(node);
    this.string = string;
  }
  public java.lang.String getString() {
    return string;
  }
  public <T> T accept(IASTVisitor<T> v) {
    return v.visitStrConLexical(this);
  }
}





}
