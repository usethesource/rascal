package org.rascalmpl.ast; 
import org.eclipse.imp.pdb.facts.INode; 
public abstract class Nonterminal extends AbstractAST { 
static public class Lexical extends Nonterminal {
	private final String string;
         protected Lexical(INode node, String string) {
		this.node = node;
		this.string = string;
	}
	public String getString() {
		return string;
	}

 	public <T> T accept(IASTVisitor<T> v) {
     		return v.visitNonterminalLexical(this);
  	}
}
static public class Ambiguity extends Nonterminal {
  private final java.util.List<org.rascalmpl.ast.Nonterminal> alternatives;
  protected Ambiguity(INode node, java.util.List<org.rascalmpl.ast.Nonterminal> alternatives) {
	this.alternatives = java.util.Collections.unmodifiableList(alternatives);
         this.node = node;
  }
  public java.util.List<org.rascalmpl.ast.Nonterminal> getAlternatives() {
	return alternatives;
  }
  
  public <T> T accept(IASTVisitor<T> v) {
     return v.visitNonterminalAmbiguity(this);
  }
}
}