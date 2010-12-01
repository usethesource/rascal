
package org.rascalmpl.ast;


import org.eclipse.imp.pdb.facts.INode;


public abstract class CaseInsensitiveStringConstant extends AbstractAST {
  public CaseInsensitiveStringConstant(INode node) {
    super(node);
  }
  


static public class Ambiguity extends CaseInsensitiveStringConstant {
  private final java.util.List<org.rascalmpl.ast.CaseInsensitiveStringConstant> alternatives;

  public Ambiguity(INode node, java.util.List<org.rascalmpl.ast.CaseInsensitiveStringConstant> alternatives) {
    super(node);
    this.alternatives = java.util.Collections.unmodifiableList(alternatives);
  }

  public java.util.List<org.rascalmpl.ast.CaseInsensitiveStringConstant> getAlternatives() {
   return alternatives;
  }

  public <T> T accept(IASTVisitor<T> v) {
	return v.visitCaseInsensitiveStringConstantAmbiguity(this);
  }
}



 
static public class Lexical extends CaseInsensitiveStringConstant {
  private final java.lang.String string;
  public Lexical(INode node, java.lang.String string) {
    super(node);
    this.string = string;
  }
  public java.lang.String getString() {
    return string;
  }
  public <T> T accept(IASTVisitor<T> v) {
    return v.visitCaseInsensitiveStringConstantLexical(this);
  }
}





}
