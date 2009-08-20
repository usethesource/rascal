package org.meta_environment.rascal.ast; 
import org.eclipse.imp.pdb.facts.INode;
public abstract class EscapeSequence extends AbstractAST { 
  static public class Lexical extends EscapeSequence {
	private String string;
         public Lexical(INode node, String string) {
		this.node = node;
		this.string = string;
	}
	public String getString() {
		return string;
	}

 	public <T> T accept(IASTVisitor<T> v) {
     		return v.visitEscapeSequenceLexical(this);
  	}
} static public class Ambiguity extends EscapeSequence {
  private final java.util.List<org.meta_environment.rascal.ast.EscapeSequence> alternatives;
  public Ambiguity(INode node, java.util.List<org.meta_environment.rascal.ast.EscapeSequence> alternatives) {
	this.alternatives = java.util.Collections.unmodifiableList(alternatives);
         this.node = node;
  }
  public java.util.List<org.meta_environment.rascal.ast.EscapeSequence> getAlternatives() {
	return alternatives;
  }
  
  public <T> T accept(IASTVisitor<T> v) {
     return v.visitEscapeSequenceAmbiguity(this);
  }
} public abstract <T> T accept(IASTVisitor<T> visitor);
}