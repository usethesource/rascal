
package org.rascalmpl.ast;


import org.eclipse.imp.pdb.facts.INode;


public abstract class DateAndTime extends AbstractAST {
  public DateAndTime(INode node) {
    super(node);
  }
  


static public class Ambiguity extends DateAndTime {
  private final java.util.List<org.rascalmpl.ast.DateAndTime> alternatives;

  public Ambiguity(INode node, java.util.List<org.rascalmpl.ast.DateAndTime> alternatives) {
    super(node);
    this.alternatives = java.util.Collections.unmodifiableList(alternatives);
  }

  public java.util.List<org.rascalmpl.ast.DateAndTime> getAlternatives() {
   return alternatives;
  }

  public <T> T accept(IASTVisitor<T> v) {
	return v.visitDateAndTimeAmbiguity(this);
  }
}



 
static public class Lexical extends DateAndTime {
  private final java.lang.String string;
  public Lexical(INode node, java.lang.String string) {
    super(node);
    this.string = string;
  }
  public java.lang.String getString() {
    return string;
  }
  public <T> T accept(IASTVisitor<T> v) {
    return v.visitDateAndTimeLexical(this);
  }
}





}
