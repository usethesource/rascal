package org.meta_environment.rascal.ast; 
import org.eclipse.imp.pdb.facts.ITree; 
public abstract class EscapeSequence extends AbstractAST { 
  static public class Lexical extends EscapeSequence {
	private String string;
	/*package*/ Lexical(ITree tree, String string) {
		this.tree = tree;
		this.string = string;
	}
	public String getString() {
		return string;
	}

 	@Override
	public <T> T accept(IASTVisitor<T> v) {
     		return v.visitEscapeSequenceLexical(this);
  	}
} static public class Ambiguity extends EscapeSequence {
  private final java.util.List<org.meta_environment.rascal.ast.EscapeSequence> alternatives;
  public Ambiguity(java.util.List<org.meta_environment.rascal.ast.EscapeSequence> alternatives) {
	this.alternatives = java.util.Collections.unmodifiableList(alternatives);
  }
  public java.util.List<org.meta_environment.rascal.ast.EscapeSequence> getAlternatives() {
	return alternatives;
  }
  
  @Override
public <T> T accept(IASTVisitor<T> v) {
     return v.visitEscapeSequenceAmbiguity(this);
  }
} @Override
public abstract <T> T accept(IASTVisitor<T> visitor);
}