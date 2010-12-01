
package org.rascalmpl.ast;


import org.eclipse.imp.pdb.facts.INode;


public abstract class StringConstant extends AbstractAST {
  public StringConstant(INode node) {
    super(node);
  }
  


static public class Ambiguity extends StringConstant {
  private final java.util.List<org.rascalmpl.ast.StringConstant> alternatives;

  public Ambiguity(INode node, java.util.List<org.rascalmpl.ast.StringConstant> alternatives) {
    super(node);
    this.alternatives = java.util.Collections.unmodifiableList(alternatives);
  }

  public java.util.List<org.rascalmpl.ast.StringConstant> getAlternatives() {
   return alternatives;
  }

  public <T> T accept(IASTVisitor<T> v) {
	return v.visitStringConstantAmbiguity(this);
  }
}



 
static public class Lexical extends StringConstant {
  private final java.lang.String string;
  public Lexical(INode node, java.lang.String string) {
    super(node);
    this.string = string;
  }
  public java.lang.String getString() {
    return string;
  }
  public <T> T accept(IASTVisitor<T> v) {
    return v.visitStringConstantLexical(this);
  }
}





}
