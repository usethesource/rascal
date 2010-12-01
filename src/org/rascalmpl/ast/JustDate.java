
package org.rascalmpl.ast;


import org.eclipse.imp.pdb.facts.INode;


public abstract class JustDate extends AbstractAST {
  public JustDate(INode node) {
    super(node);
  }
  


static public class Ambiguity extends JustDate {
  private final java.util.List<org.rascalmpl.ast.JustDate> alternatives;

  public Ambiguity(INode node, java.util.List<org.rascalmpl.ast.JustDate> alternatives) {
    super(node);
    this.alternatives = java.util.Collections.unmodifiableList(alternatives);
  }

  public java.util.List<org.rascalmpl.ast.JustDate> getAlternatives() {
   return alternatives;
  }

  public <T> T accept(IASTVisitor<T> v) {
	return v.visitJustDateAmbiguity(this);
  }
}



 
static public class Lexical extends JustDate {
  private final java.lang.String string;
  public Lexical(INode node, java.lang.String string) {
    super(node);
    this.string = string;
  }
  public java.lang.String getString() {
    return string;
  }
  public <T> T accept(IASTVisitor<T> v) {
    return v.visitJustDateLexical(this);
  }
}





}
