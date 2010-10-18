package org.rascalmpl.ast; 
import org.eclipse.imp.pdb.facts.INode; 
public abstract class RegExpLiteral extends AbstractAST { 
static public class Lexical extends RegExpLiteral {
	private final String string;
         protected Lexical(INode node, String string) {
		this.node = node;
		this.string = string;
	}
	public String getString() {
		return string;
	}

 	public <T> T accept(IASTVisitor<T> v) {
     		return v.visitRegExpLiteralLexical(this);
  	}
}
static public class Ambiguity extends RegExpLiteral {
  private final java.util.List<org.rascalmpl.ast.RegExpLiteral> alternatives;
  protected Ambiguity(INode node, java.util.List<org.rascalmpl.ast.RegExpLiteral> alternatives) {
	this.alternatives = java.util.Collections.unmodifiableList(alternatives);
         this.node = node;
  }
  public java.util.List<org.rascalmpl.ast.RegExpLiteral> getAlternatives() {
	return alternatives;
  }
  
  public <T> T accept(IASTVisitor<T> v) {
     return v.visitRegExpLiteralAmbiguity(this);
  }
}
}