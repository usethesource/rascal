
package org.rascalmpl.ast;


import org.eclipse.imp.pdb.facts.INode;


public abstract class TimeZonePart extends AbstractAST {
  public TimeZonePart(INode node) {
    super(node);
  }
  


static public class Ambiguity extends TimeZonePart {
  private final java.util.List<org.rascalmpl.ast.TimeZonePart> alternatives;

  public Ambiguity(INode node, java.util.List<org.rascalmpl.ast.TimeZonePart> alternatives) {
    super(node);
    this.alternatives = java.util.Collections.unmodifiableList(alternatives);
  }

  public java.util.List<org.rascalmpl.ast.TimeZonePart> getAlternatives() {
   return alternatives;
  }

  public <T> T accept(IASTVisitor<T> v) {
	return v.visitTimeZonePartAmbiguity(this);
  }
}



 
static public class Lexical extends TimeZonePart {
  private final java.lang.String string;
  public Lexical(INode node, java.lang.String string) {
    super(node);
    this.string = string;
  }
  public java.lang.String getString() {
    return string;
  }
  public <T> T accept(IASTVisitor<T> v) {
    return v.visitTimeZonePartLexical(this);
  }
}





}
