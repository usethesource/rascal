package org.rascalmpl.ast; 
import org.eclipse.imp.pdb.facts.INode; 
public abstract class SingleCharacter extends AbstractAST { 
  static public class Lexical extends SingleCharacter {
	private final String string;
         public Lexical(INode node, String string) {
		this.node = node;
		this.string = string;
	}
	public String getString() {
		return string;
	}

 	public <T> T accept(IASTVisitor<T> v) {
     		return v.visitSingleCharacterLexical(this);
  	}
} static public class Ambiguity extends SingleCharacter {
  private final java.util.List<org.rascalmpl.ast.SingleCharacter> alternatives;
  public Ambiguity(INode node, java.util.List<org.rascalmpl.ast.SingleCharacter> alternatives) {
	this.alternatives = java.util.Collections.unmodifiableList(alternatives);
         this.node = node;
  }
  public java.util.List<org.rascalmpl.ast.SingleCharacter> getAlternatives() {
	return alternatives;
  }
  
  public <T> T accept(IASTVisitor<T> v) {
     return v.visitSingleCharacterAmbiguity(this);
  }
} public abstract <T> T accept(IASTVisitor<T> visitor);
}