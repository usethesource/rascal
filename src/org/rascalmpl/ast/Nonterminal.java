package org.rascalmpl.ast; 
import org.eclipse.imp.pdb.facts.INode; 
public abstract class Nonterminal extends AbstractAST { 
static public class Lexical extends Nonterminal {
	private final String string;
         public Lexical(INode node, String string) {
		this.node = node;
		this.string = string;
	}
	public String getString() {
		return string;
	}

 	@Override
	public <T> T accept(IASTVisitor<T> v) {
     		return v.visitNonterminalLexical(this);
  	}
}
static public class Ambiguity extends Nonterminal {
  private final java.util.List<org.rascalmpl.ast.Nonterminal> alternatives;
  public Ambiguity(INode node, java.util.List<org.rascalmpl.ast.Nonterminal> alternatives) {
	this.alternatives = java.util.Collections.unmodifiableList(alternatives);
         this.node = node;
  }
  public java.util.List<org.rascalmpl.ast.Nonterminal> getAlternatives() {
	return alternatives;
  }
  
  @Override
public <T> T accept(IASTVisitor<T> v) {
     return v.visitNonterminalAmbiguity(this);
  }
}
}