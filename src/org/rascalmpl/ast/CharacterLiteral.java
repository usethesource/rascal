package org.rascalmpl.ast; 
import org.eclipse.imp.pdb.facts.INode; 
public abstract class CharacterLiteral extends AbstractAST { 
  static public class Lexical extends CharacterLiteral {
	private final String string;
         public Lexical(INode node, String string) {
		this.node = node;
		this.string = string;
	}
	public String getString() {
		return string;
	}

 	public <T> T accept(IASTVisitor<T> v) {
     		return v.visitCharacterLiteralLexical(this);
  	}
} static public class Ambiguity extends CharacterLiteral {
  private final java.util.List<org.rascalmpl.ast.CharacterLiteral> alternatives;
  public Ambiguity(INode node, java.util.List<org.rascalmpl.ast.CharacterLiteral> alternatives) {
	this.alternatives = java.util.Collections.unmodifiableList(alternatives);
         this.node = node;
  }
  public java.util.List<org.rascalmpl.ast.CharacterLiteral> getAlternatives() {
	return alternatives;
  }
  
  public <T> T accept(IASTVisitor<T> v) {
     return v.visitCharacterLiteralAmbiguity(this);
  }
} public abstract <T> T accept(IASTVisitor<T> visitor);
}