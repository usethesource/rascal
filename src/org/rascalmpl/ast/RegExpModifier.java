
package org.rascalmpl.ast;


import org.eclipse.imp.pdb.facts.INode;


public abstract class RegExpModifier extends AbstractAST {
  public RegExpModifier(INode node) {
    super(node);
  }
  


static public class Ambiguity extends RegExpModifier {
  private final java.util.List<org.rascalmpl.ast.RegExpModifier> alternatives;

  public Ambiguity(INode node, java.util.List<org.rascalmpl.ast.RegExpModifier> alternatives) {
    super(node);
    this.alternatives = java.util.Collections.unmodifiableList(alternatives);
  }

  public java.util.List<org.rascalmpl.ast.RegExpModifier> getAlternatives() {
   return alternatives;
  }

  public <T> T accept(IASTVisitor<T> v) {
	return v.visitRegExpModifierAmbiguity(this);
  }
}



 
static public class Lexical extends RegExpModifier {
  private final java.lang.String string;
  public Lexical(INode node, java.lang.String string) {
    super(node);
    this.string = string;
  }
  public java.lang.String getString() {
    return string;
  }
  public <T> T accept(IASTVisitor<T> v) {
    return v.visitRegExpModifierLexical(this);
  }
}





}
