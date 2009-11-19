package org.meta_environment.rascal.ast; 
import org.eclipse.imp.pdb.facts.INode; 
public abstract class TimePartNoTZ extends AbstractAST { 
  static public class Lexical extends TimePartNoTZ {
	private final String string;
         public Lexical(INode node, String string) {
		this.node = node;
		this.string = string;
	}
	public String getString() {
		return string;
	}

 	public <T> T accept(IASTVisitor<T> v) {
     		return v.visitTimePartNoTZLexical(this);
  	}
} static public class Ambiguity extends TimePartNoTZ {
  private final java.util.List<org.meta_environment.rascal.ast.TimePartNoTZ> alternatives;
  public Ambiguity(INode node, java.util.List<org.meta_environment.rascal.ast.TimePartNoTZ> alternatives) {
	this.alternatives = java.util.Collections.unmodifiableList(alternatives);
         this.node = node;
  }
  public java.util.List<org.meta_environment.rascal.ast.TimePartNoTZ> getAlternatives() {
	return alternatives;
  }
  
  public <T> T accept(IASTVisitor<T> v) {
     return v.visitTimePartNoTZAmbiguity(this);
  }
} public abstract <T> T accept(IASTVisitor<T> visitor);
}