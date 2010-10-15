package org.rascalmpl.ast; 
import org.eclipse.imp.pdb.facts.INode; 
public abstract class Char extends AbstractAST { 
  static public class Lexical extends Char {
	private final String string;
         protected Lexical(INode node, String string) {
		this.node = node;
		this.string = string;
	}
	public String getString() {
		return string;
	}

 	public <T> T accept(IASTVisitor<T> v) {
     		return v.visitCharLexical(this);
  	}
} static public class Ambiguity extends Char {
  private final java.util.List<org.rascalmpl.ast.Char> alternatives;
  protected Ambiguity(INode node, java.util.List<org.rascalmpl.ast.Char> alternatives) {
	this.alternatives = java.util.Collections.unmodifiableList(alternatives);
         this.node = node;
  }
  public java.util.List<org.rascalmpl.ast.Char> getAlternatives() {
	return alternatives;
  }
  
  public <T> T accept(IASTVisitor<T> v) {
     return v.visitCharAmbiguity(this);
  }
} public abstract <T> T accept(IASTVisitor<T> visitor);
}